package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaModuloDto;
import br.com.sgc.access.dto.CadastroModuloDto;
import br.com.sgc.access.dto.GETModuloResponseDto;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.ModuloFilter;
import br.com.sgc.access.repositories.ModuloRepository;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.ModuloMapper;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ModuloServiceImpl implements ServicesAccess<CadastroModuloDto, AtualizaModuloDto, GETModuloResponseDto, ModuloFilter> {

	@Autowired
	private ModuloMapper mapper;
	
	@Autowired
	private QueryRepository<Modulo, ModuloFilter> query;
	
	@Autowired
	private Validators<CadastroModuloDto, AtualizaModuloDto> validar;
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Override
	public GETModuloResponseDto cadastra(CadastroModuloDto post) throws RegistroException {
		
		log.info("Cadastrando módulo...");
		Modulo modulo = this.mapper.cadastroModuloDtoToModulo(post);
		
		//Validação
		this.validar.validarPost(post);
		
		GETModuloResponseDto response = this.mapper.moduloToGETModuloResponseDto(this.moduloRepository.save(modulo));
		
		return response;
	}
	
	@Override
	public List<GETModuloResponseDto> cadastraEmLote(List<CadastroModuloDto> post) {

		log.info("Cadastrando módulos...");
		List<Modulo> listSalvar = new ArrayList<Modulo>();
		List<GETModuloResponseDto> response = new ArrayList<GETModuloResponseDto>();
		
		post.forEach(p -> {
			try {
				this.validar.validarPost(p);
			} catch (RegistroException e) {
				e.printStackTrace();
			}
			
			listSalvar.add(this.mapper.cadastroModuloDtoToModulo(p));
		});		
		
		this.moduloRepository.saveAll(listSalvar).forEach(m -> {
			response.add(this.mapper.moduloToGETModuloResponseDto(m));
		});
		
		return response;
		
	}

	@Override
	public GETModuloResponseDto atualiza(AtualizaModuloDto put, Long id) throws RegistroException {
		
		log.info("Atualizando módulo...");		
		Modulo modulo = this.moduloRepository.findById(id).get();
		
		modulo.setDescricao(put.getDescricao());
		modulo.setPathModulo(put.getPathModulo());
		modulo.setPosicao(put.getPosicao());
		
		//Validação
		this.validar.validarPut(put, id);
		
		GETModuloResponseDto response = this.mapper.moduloToGETModuloResponseDto(this.moduloRepository.save(modulo));
		
		return response;
	}

	@Override
	public Page<GETModuloResponseDto> buscaPaginado(ModuloFilter filter, Pageable pageable) {

		log.info("Buscando módulo(s)...");
		
		Response<List<GETModuloResponseDto>> response = new Response<List<GETModuloResponseDto>>(); 
		
		List<GETModuloResponseDto> listModulos = new ArrayList<GETModuloResponseDto>();
		
		this.query.query(filter, pageable).forEach(m -> {
			listModulos.add(this.mapper.moduloToGETModuloResponseDto(m));
		});
		
		response.setData(listModulos);
		return new PageImpl<>(response.getData(), pageable, this.query.totalRegistros(filter));
		
	}

	@Override
	public GETModuloResponseDto busca(ModuloFilter filter, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


}
