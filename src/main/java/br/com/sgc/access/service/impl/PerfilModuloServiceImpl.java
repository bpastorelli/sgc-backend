package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaAcessoModuloDto;
import br.com.sgc.access.dto.CadastroAcessoModuloDto;
import br.com.sgc.access.dto.GETPerfilModuloResponseDto;
import br.com.sgc.access.entities.AcessoModulo;
import br.com.sgc.access.filter.PerfilModuloFilter;
import br.com.sgc.access.repositories.AcessoModuloRepository;
import br.com.sgc.access.repositories.ModuloRepository;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PerfilModuloServiceImpl implements ServicesAccess<List<CadastroAcessoModuloDto>, List<AtualizaAcessoModuloDto>, GETPerfilModuloResponseDto, PerfilModuloFilter> {
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private AcessoModuloRepository acessoModuloRepository;
	
	@Override
	public GETPerfilModuloResponseDto cadastra(List<CadastroAcessoModuloDto> post) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GETPerfilModuloResponseDto> cadastraEmLote(List<List<CadastroAcessoModuloDto>> post)
			throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GETPerfilModuloResponseDto atualiza(List<AtualizaAcessoModuloDto> put, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<GETPerfilModuloResponseDto> buscaPaginado(PerfilModuloFilter filter, Pageable pageable)
			throws RegistroException {
		
		log.info("Buscando acesso módulo(s)...");
		
		Response<List<GETPerfilModuloResponseDto>> response = new Response<List<GETPerfilModuloResponseDto>>(); 
		
		List<GETPerfilModuloResponseDto> listAcessos = new ArrayList<GETPerfilModuloResponseDto>();
		
		this.moduloRepository.findByPosicao(filter.getPosicao()).forEach(m -> {
			
			Optional<AcessoModulo> acessoModulo = this.acessoModuloRepository.findByIdUsuarioAndModuloId(filter.getIdUsuario(), m.getId());
			
			GETPerfilModuloResponseDto acesso = GETPerfilModuloResponseDto 
					.builder()
					.idModulo(m.getId())
					.nomeModulo(m.getDescricao())
					.pathModulo(m.getPathModulo())
					.acesso(acessoModulo.isPresent() ? acessoModulo.get().isAcesso() : false)
					.build();
				listAcessos.add(acesso);			
		});

		response.setData(listAcessos);
		return new PageImpl<>(response.getData(), pageable, listAcessos.size());
	}

	@Override
	public GETPerfilModuloResponseDto busca(PerfilModuloFilter filter, Pageable pageable) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

}
