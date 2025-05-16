package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController //determina que essa classe é uma controller
@RequestMapping("/postagens") // determina que essa classe trata de todas requisições que tenha /postagens no http
@CrossOrigin(origins = "*", allowedHeaders = "*") //faz com que o servidor aceite requisições do front end, pois inicialmente o servidor só aceita requisições do proprio servidor (local), quando ambos estiverem em deploy o crossorigin vai liberar o dominio que pode acessar a API. O * faz com que ele aceite todos os dominios. O allowedHeaders serve para que informações como o token possa ser enviada nas requisições
public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository; // injestão de dependencia é um tipo de inversão de controle, o autowired trata-se de um processo agilizado para trabalharmos, é como se pegasse a parte dificil de um processo longo e encurta-se (o barista é a classe controller, os ingredientes é a classe repository, o autowired basicamente é a definição que não queremos nos preocupar com instanciação de classe e criar registros no banco de dados). estamos transferindo a responsabilidade de instanciar a postagem e demais classes da model, para o autowired.


	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		//select * from db_postagens
		return ResponseEntity.ok(postagemRepository.findAll());
	}
}	
