package br.com.sgc.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.sgc.access.mapper.HistoricoImportacaoMapper;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.GETHistoricoImportacaoResponseDto;
import br.com.sgc.enums.SituacaoEnum;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.HistoricoImportacaoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContribuicaoService {
    
	@Autowired
	private ImportServiceAnsync importService;
	
	@Autowired
	private HistoricoImportacaoMapper historicoMapper;
	
	@Autowired
	private HistoricoImportacaoRepository historicoRepository;
	
    public CabecalhoResponsePublisherDto processarContribuicoes(final MultipartFile file) throws RegistroException, IOException {
        
    	importService.validarTipoArquivo(file);
    	
        String idRequisicao = UUID.randomUUID().toString();
        
        log.info("Requisição: {}", idRequisicao);
        
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        
        importService.processar(workbook, file.getOriginalFilename(), idRequisicao);
		
		CabecalhoResponsePublisherDto response = CabecalhoResponsePublisherDto.builder()
			.ticket(idRequisicao)
			.build();
		
	    return response; 

    }
    
    public List<GETHistoricoImportacaoResponseDto> buscarHistoricoImportacao(List<SituacaoEnum> situacao){
    	
    	List<GETHistoricoImportacaoResponseDto> response = 
    			this.historicoMapper.toGETHistoricoImportacaoResponseDto(this.historicoRepository.findBySituacaoIn(situacao));
    	
    	return response;
    	
    }
}
