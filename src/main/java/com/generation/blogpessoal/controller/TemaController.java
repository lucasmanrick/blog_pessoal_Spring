package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {
	
	@Autowired
	private TemaRepository temaRepository;
	
	
	@GetMapping()
	public ResponseEntity<List<Tema>> getAllTheme () {
		return  ResponseEntity.ok(temaRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Tema> getThemeById (@PathVariable long id) {
		Optional<Tema> result = temaRepository.findById(id);
		if(result.isEmpty()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
		return ResponseEntity.ok(result.get());
		
	}
	

	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Tema>> getThemeById (@PathVariable String descricao) {
		List<Tema> result = temaRepository.findAllByDescricaoContainsIgnoreCase(descricao);
		if(result.isEmpty()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
		return ResponseEntity.ok(result);
	}
	
	
	@PostMapping()
	public ResponseEntity<Tema> post (@Valid @RequestBody Tema temaBody) {
		if(temaBody.getId() != null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(temaBody)); 
	}
	
	
	@PutMapping()
	public ResponseEntity<Tema> put (@Valid @RequestBody Tema temaBody) {
		if(temaBody.getId() == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
		if(temaRepository.findById(temaBody.getId()).isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(temaBody)); 
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTheme (@PathVariable long id) {
		temaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); // basicamente o orElseThrow, caso ele não encontre pelo id determinado ele cai nesse else ja com o throw not found.
		
		temaRepository.deleteById(id);
	}
	
}
