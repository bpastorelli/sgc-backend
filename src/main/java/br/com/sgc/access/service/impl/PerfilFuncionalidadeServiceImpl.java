package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.GETPerfilFuncionalidadeResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.filter.PerfilFuncionalidadeFilter;
import br.com.sgc.access.repositories.AcessoFuncionalidadeRepository;
import br.com.sgc.access.repositories.FuncionalidadeRepository;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PerfilFuncionalidadeServiceImpl implements ServicesAccess<List<CadastroAcessoFuncionalidadeDto>, List<AtualizaAcessoFuncionalidadeDto>, GETPerfilFuncionalidadeResponseDto, PerfilFuncionalidadeFilter> {
	
	@Autowired
	private FuncionalidadeRepository funcionalidadeRepository;
	
	@Autowired
	private AcessoFuncionalidadeRepository acessoFuncionalidadeRepository;
	
	@Override
	public GETPerfilFuncionalidadeResponseDto cadastra(List<CadastroAcessoFuncionalidadeDto> post) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GETPerfilFuncionalidadeResponseDto> cadastraEmLote(List<List<CadastroAcessoFuncionalidadeDto>> post)
			throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GETPerfilFuncionalidadeResponseDto atualiza(List<AtualizaAcessoFuncionalidadeDto> put, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<GETPerfilFuncionalidadeResponseDto> buscaPaginado(PerfilFuncionalidadeFilter filter, Pageable pageable)
			throws RegistroException {
		
		log.info("Buscando acesso funcionalidade(s)...");
		
		Response<List<GETPerfilFuncionalidadeResponseDto>> response = new Response<List<GETPerfilFuncionalidadeResponseDto>>(); 
		
		List<GETPerfilFuncionalidadeResponseDto> listAcessos = new ArrayList<GETPerfilFuncionalidadeResponseDto>();
		
		this.funcionalidadeRepository.findByIdModuloAndPosicao(filter.getIdModulo(), filter.getPosicao()).forEach(m -> {
			
			Optional<AcessoFuncionalidade> acessoFuncionalidade = this.acessoFuncionalidadeRepository.findByIdUsuarioAndFuncionalidadeIdAndModuloId(filter.getIdUsuario(), m.getId(), m.getIdModulo());
			
			GETPerfilFuncionalidadeResponseDto acesso = GETPerfilFuncionalidadeResponseDto
					.builder()
					.idFuncionalidade(m.getId())
					.nomeFuncionalidade(m.getDescricao())
					.pathFuncionalidade(m.getPathFuncionalidade())
					.acesso(acessoFuncionalidade.isPresent() ? acessoFuncionalidade.get().isAcesso() : false)
					.build();
				listAcessos.add(acesso);			
		});

		response.setData(listAcessos);
		return new PageImpl<>(response.getData(), pageable, listAcessos.size());
	}

	@Override
	public GETPerfilFuncionalidadeResponseDto busca(PerfilFuncionalidadeFilter filter, Pageable pageable) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GETPerfilFuncionalidadeResponseDto> atualizaEmLote(List<List<AtualizaAcessoFuncionalidadeDto>> put, Long id)
			throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

}
