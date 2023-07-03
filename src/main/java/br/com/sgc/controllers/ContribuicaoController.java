package br.com.sgc.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.services.ContribuicaoService;

@RestController
@RequestMapping("/sgc/contribuicao")
@CrossOrigin(origins = "*")
public class ContribuicaoController {
	
	@Autowired
	private ContribuicaoService service;
	
	@PostMapping(value = "/import")
	public ResponseEntity<?> importacaoContribuicao(@RequestParam("file") MultipartFile file) throws RegistroException, IOException, InterruptedException, ExecutionException{
		
		try {
			
			return ResponseEntity.ok().body(this.service.processarContribuicoes(file).get());
			
		} catch (RegistroException e) {
			
			return ResponseEntity.badRequest().body(e.getErros());
			
		}
		
	}

}
