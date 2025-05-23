package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.security.JwtService;
import com.generation.blogpessoal.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JwtService jwtService;
	
	
	@GetMapping()
	public ResponseEntity<List<Usuario>> getAllUsers () {
		return ResponseEntity.ok(usuarioService.getAllUsers());
	}
	
	
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> post (@Valid @RequestBody Usuario usuario) {
		return usuarioService.cadastrarUsuario(usuario)
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
				
	}
	
	
	
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> put (@Valid @RequestBody Usuario usuario, @RequestHeader("Authorization") String auth) {
		
		
		return usuarioService.atualizarUsuario(usuario)
				.map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
				
	}
	
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticar (@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin) {
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
				
	}
	
	
}
