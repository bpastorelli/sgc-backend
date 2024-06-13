package br.com.sgc.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.sgc.ContribuicaoAvro;
import br.com.sgc.amqp.producer.impl.ContribuicaoProducer;
import br.com.sgc.commons.ValidaCPF;
import br.com.sgc.converter.Converter;
import br.com.sgc.dto.ContribuicaoDto;
import br.com.sgc.dto.LancamentoDto;
import br.com.sgc.entities.HistoricoImportacao;
import br.com.sgc.entities.Lancamento;
import br.com.sgc.entities.Morador;
import br.com.sgc.entities.VinculoResidencia;
import br.com.sgc.enums.DataTypeEnum;
import br.com.sgc.enums.SituacaoEnum;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.HistoricoImportacaoRepository;
import br.com.sgc.repositories.LancamentoRepository;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.repositories.VinculoResidenciaRepository;
import br.com.sgc.utils.Utils;
import br.com.sgc.utils.WorkbookUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImportService {
	
	private List<String> cpfs = new ArrayList<String>();
	
	private HistoricoImportacao historico;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private HistoricoImportacaoRepository historicoRepository;
	
    @Autowired
    private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private VinculoResidenciaRepository vinculoResidenciaRespository;
    
	private List<String> errorsList = new ArrayList<String>();
	
	private List<Morador> moradores = new ArrayList<Morador>();
	
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	
	private List<VinculoResidencia> vinculos = new ArrayList<VinculoResidencia>();

	private XSSFWorkbook workbook;
	
	@Autowired
	private ContribuicaoProducer producer;
	
	@Autowired
	private Converter<ContribuicaoAvro, List<LancamentoDto>> convert;
	
	private static int PAGE_SIZE = 1000;
	
    @Async("asyncExecutor")
    public void processar(final MultipartFile file, String idRequisicao) throws RegistroException, IOException {
    	
    	log.info("Iniciando o processamento de importação {}", file.getOriginalFilename());
    	
		this.salvarHistorico(file.getOriginalFilename(), SituacaoEnum.INICIANDO, idRequisicao);
        
        RegistroException errors = new RegistroException();
        
		if(file.isEmpty()) {
			this.addError("Nenhum arquivo selecionado para importação.");
			this.salvarHistorico(file.getName(), SituacaoEnum.FALHA, idRequisicao);
			throw errors;
		}
		
	    List<LancamentoDto> lancamentos = this.prepararDadosImportacao(file);
	    
		if(this.errorsList.size() > 0) {
			this.errorsList.forEach(erro -> errors.getErros().add(new ErroRegistro("","", erro)));
			this.salvarHistorico(file.getName(), SituacaoEnum.FALHA, idRequisicao);
			throw errors;
		}
		
		this.salvarHistorico(file.getOriginalFilename(), SituacaoEnum.IMPORTANDO, idRequisicao);
		
		int page;
		for(page = 1; page <= Math.round(this.calcularPaginas()); page++) {
			log.info("Enviando página: {}...", page);
			this.producer.producerAsync(this.convert.convert(Utils.getPage(lancamentos, page, PAGE_SIZE)));
		}
		
		log.info("Conteúdo enviado para processamento.");
    	
    }
    
	private List<LancamentoDto> prepararDadosImportacao(MultipartFile file) throws RegistroException, IOException{
		
		log.info("Realizando a leitura do arquivo {}", file.getName());
		
		this.errorsList = new ArrayList<String>();
		
		List<LancamentoDto> listPreparada = new ArrayList<LancamentoDto>();
		List<ContribuicaoDto> lancamentoList = this.getDataFromFile(file);
	    
		if(this.errorsList.size() == 0) 
			this.loadBases(); 
	    
		log.info("Validando os dados...");
		
		lancamentoList.forEach(l -> {
			
			//Valida o CPF
			if(!ValidaCPF.isCPF(l.getCpf()))
				this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | CPF inválido: " + l.getCpf() + ".");
			
			if(l.getDataPagamento().compareTo(LocalDate.now()) > 0)
				this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | Não é possível realizar um lançamento para uma data futura (" + Utils.dateFormat(l.getDataPagamento(),"dd/MM/yyyy") + ").");

			//Busca o morador a partir do cpf do arquivo .xls
			Optional<Morador> morador = moradores.stream()
							.filter(m -> m.getCpf().equals(l.getCpf()))
							.findFirst();
				
			//Caso o morador exista, inicia a validação dos dados
			if(morador.isPresent()) {	

				//Verifica se o morador está ativo
				if(morador.get().getPosicao() == 0)
					this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | O morador " + morador.get().getNome() + " está inativo.");
				
				
				Optional<VinculoResidencia> vinculo = this.vinculos.stream()
								.filter(v -> v.getMorador().getId().equals(morador.get().getId()))
								.findFirst();
				
				if(!vinculo.isPresent())
					this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | O morador " + morador.get().getNome() + " não está vinculado a uma residência.");

				
				//Teste se o valor é numerico
				if(!Utils.isNumeric(l.getValor().toString())) {
					this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | O valor informado de " + l.getValor().toString() + " não está caracterizado como numérico.");
				}else {		
					//Valida se existem lançamentos duplicados na planilha de importação
					if(lancamentoList.stream()
							.filter(p -> p.getCpf().trim().equals(l.getCpf().trim()))
							.filter(x -> x.getValor().equals(l.getValor()))
							.filter(y -> y.getDataPagamento().equals(l.getDataPagamento()))
							.count() > 1) {
						
						this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | O lançamento para o CPF " + l.getCpf() + " no valor de " + l.getValor() + " está duplicado.");
					}
				}
				
				//Valida se existem lançamentos duplicados na base dados				
				if(lancamentos.stream()						
						.filter(a -> a.getMorador().equals(morador.get()))
						.filter(x -> (x.getValor()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
								.equals(l.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN)))
						.filter(y -> y.getDataPagamento().equals(Utils.convertToLocalDateTime(l.getDataPagamento())))
						.count() > 0) {
					
					this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | O lançamento para o CPF " + l.getCpf() + " no valor de " + l.getValor() + " já existe na base de dados.");
				}	
				
				//Valida se existem lançamentos com valor Zero
				if(l.getValor().compareTo(new BigDecimal(0)) == 0)
					this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | Valor da contribuição não pode ser Zero. CPF: " + l.getCpf() + ".");
				
				if(this.errorsList.size() == 0){
					
					LancamentoDto lancamento = LancamentoDto.builder()
						.morador(morador.get())
						.dataPagamento(Utils.convertToLocalDateTime(l.getDataPagamento()))
						.valor(l.getValor())
						.documento(l.getDocumento())
						.periodo(l.getDataPagamento().getMonth().getValue() + "/" + l.getDataPagamento().getYear())
						.residencia(vinculo.isPresent() ? vinculo.get().getResidencia() : null)
						.requisicaoId(this.historico.getIdRequisicao())
						.build();
					
					listPreparada.add(lancamento);
					
				}
			
			}else {
				
				this.addError("Linha: " + (lancamentoList.indexOf(l) + 2) + " | Morador não encontrado para o CPF: " + l.getCpf() + ".");
				
			}

		});
	    
		return listPreparada;
	    
	}
	    	
	//Lê o aquivo de importação e trata os dados para ContribuicaoDto
	private List<ContribuicaoDto> getDataFromFile(MultipartFile file) throws IOException{
		
		log.info("Extraindo dados do arquivo {}...", file.getName());
		
		List<ContribuicaoDto> lancamentoList = new ArrayList<ContribuicaoDto>();
		
		if(!FilenameUtils.getExtension(file.getOriginalFilename()).equals("xlsx") 
				&& !FilenameUtils.getExtension(file.getOriginalFilename()).equals("xls"))
			this.addError("Formato de arquivo inválido. Formatos suportados: .xlsx e .xls");
		
		//Se não houver erro de tipo de arquivo, inicia a preparação
		if(this.errorsList.size() == 0) {			
			
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);
			
			this.validarArquivo(worksheet);
			
			if(worksheet.getPhysicalNumberOfRows() > 1 && this.errorsList.size() == 0) {
				
			    for(int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
		            
			    	XSSFRow row = worksheet.getRow(i);
			    	if(row.getCell(0) != null && row.getCell(0).getRawValue() != null) {
			    		
			    		ContribuicaoDto lanca = new ContribuicaoDto();
			    		lanca.setCpf(WorkbookUtils.<String>getCellValue(i, 0, worksheet, DataTypeEnum.CPF).replace(".", "").replace("-", ""));
			    		lanca.setDataPagamento(WorkbookUtils.<LocalDate>getCellValue(i, 1, worksheet, DataTypeEnum.DATE));
			    		lanca.setValor(WorkbookUtils.getCellValue(i, 2, worksheet, DataTypeEnum.BIG_DECIMAL));
			    		lanca.setDocumento(WorkbookUtils.getCellValue(i, 3, worksheet, DataTypeEnum.STRING));
			    		
			    		if(!this.cpfs.contains(lanca.getCpf()))
			    			this.cpfs.add(lanca.getCpf());
			    		
			    		lancamentoList.add(lanca);
			    	}
			       
			    }	
				
			}			
			
		}
		
		if(lancamentoList.size() == 0 && this.errorsList.size() == 0)
			this.addError("Não existem dados para importação.");
		
		return lancamentoList;
		
	}
	
	//Carrega as tabelas necessárias em memória para poupar requisições em banco.
	private void loadBases() {
		
		List<Long> codigosList = new ArrayList<>();
			
		//Busca todos os moradores uma única vez para utilização
		this.moradores = this.moradorRepository.findByCpfIn(this.cpfs);
		
		moradores.forEach(m -> { codigosList.add(m.getId()); });
		
		//Busca todos os lancamentos uma unica vez para utilização
		this.lancamentos = this.lancamentoRepository.findByMoradorIdIn(codigosList);
		    
		 //Busca todos os vinculos uma unica vez para uitilização
		this.vinculos = this.vinculoResidenciaRespository.findAll(); 
	
		
	}
	
	private void validarArquivo(XSSFSheet worksheet) {
		
		int colunas = 4;	
		if(worksheet.getRow(0).getPhysicalNumberOfCells() > colunas)
			this.addError("O arquivo possui mais colunas do que o padrão esperado de " + colunas + ".");
		
	}
	
	private void addError(String msg){
		
		if(!this.errorsList.contains(msg))
			this.errorsList.add(msg);
		
	}
	
	private void salvarHistorico(String file, SituacaoEnum situacao, String idRequisicao) {
		
		if(situacao.equals(SituacaoEnum.INICIANDO)) {
			this.historico = new HistoricoImportacao();
			this.historico.setIdRequisicao(idRequisicao);
			this.historico.setNomeArquivo(file);
			this.historico.setSituacao(situacao);			
		}else
			this.historico.setSituacao(situacao);
		
		this.historico = this.historicoRepository.save(historico);
		
	}
	
	private Long calcularPaginas() {
		
		Long pages = ((long) lancamentos.size() / PAGE_SIZE);
		Integer resto = (lancamentos.size() % PAGE_SIZE);
		pages = resto > 0 ? Math.round(pages) + 1 : pages;
		
		if(pages == 0)
			pages = Long.parseLong("1");
		
		return pages;
	}


}
