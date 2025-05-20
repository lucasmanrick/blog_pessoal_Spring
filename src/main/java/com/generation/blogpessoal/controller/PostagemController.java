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

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController //determina que essa classe é uma controller
@RequestMapping("/postagens") // determina que essa classe trata de todas requisições que tenha /postagens no http
@CrossOrigin(origins = "*", allowedHeaders = "*") //faz com que o servidor aceite requisições do front end, pois inicialmente o servidor só aceita requisições do proprio servidor (local), quando ambos estiverem em deploy o crossorigin vai liberar o dominio que pode acessar a API. O * faz com que ele aceite todos os dominios. O allowedHeaders serve para que informações como o token possa ser enviada nas requisições
public class PostagemController {
	
	
	@Autowired
	private PostagemRepository postagemRepository; // injestão de dependencia é um tipo de inversão de controle, o autowired trata-se de um processo agilizado para trabalharmos, é como se pegasse a parte dificil de um processo longo e encurta-se (o barista é a classe controller, os ingredientes é a classe repository, o autowired basicamente é a definição que não queremos nos preocupar com instanciação de classe e criar registros no banco de dados). estamos transferindo a responsabilidade de instanciar a postagem e demais classes da model, para o autowired.

	
	@Autowired
	private TemaRepository temaRepository; // outra injeção de dependencia porem da tabela TEMA.

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		//select * from db_postagens
		return ResponseEntity.ok(postagemRepository.findAll());
	}
	
	@GetMapping("/{id}") //determinamos que pela url sera passado o id para ser usado na função getbyid
	public ResponseEntity<Postagem> getById(@PathVariable long id){  //O Path Variable faz com que o valor que sera utilizado no parametro, seja o valor passado pela URL que determinamos que seria /postagem/{id}. caso passassemos mais parametros depois do id, colocariamos mais parametros com pathvariable no metodo.
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))//o metodo .map retorna um Optional, ou seja null ou resposta, se tiver a resposta ele ja retorna.
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()) //caso não tenha um resultado ele retorna o status que nao foi encontrado.
				;
	}
	
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		//select * from db_postagens
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {//o @Valid serve para justamente validar se o objeto postagem que esta sendo enviado (parametro postagem) está com os dados compativeis com a model Postagem, se não estiver ele retorna o erro bad request se não envia a resposta abaixo.
		if(temaRepository.existsById(postagem.getTema().getId())) {
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)); 
		}
		 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Tema não existe", null);
		 
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {//o @Valid serve para justamente validar se o objeto postagem que esta sendo enviado (parametro postagem) está com os dados compativeis com a model Postagem, se não estiver ele retorna o erro bad request se não envia a resposta abaixo.
		if(postagem.getId() != null) { //se o campo id for enviado na requisição segue se não retorna erro
		
		if(postagemRepository.existsById(postagem.getId())) //verifica se o id existe no banco de dados para atualizar, pois se o id já existir ele ira atualizar, mas se o id não existir ele cria um novo registro e não é o que queremos.
			
			if(temaRepository.existsById(postagem.getTema().getId())) {//verifica se o tema sendo inserido existe na tabela temas.
			return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));  
			}else {
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Tema não existe", null);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  
	}
	
	@DeleteMapping("/{id}") 
	@ResponseStatus(HttpStatus.NO_CONTENT) // determina que esperamos o resultado no content
	public void delete(@PathVariable long id){  //O Path Variable faz com que o valor que sera utilizado no parametro, seja o valor passado pela URL que determinamos que seria /postagem/{id}. caso passassemos mais parametros depois do id, colocariamos mais parametros com pathvariable no metodo.
				Optional<Postagem> postagem = postagemRepository.findById(id); //verifica se o id inserido existe
				if (postagem.isEmpty()) //se não existir retorna erro falando que nao foi encontrado.
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				
				postagemRepository.deleteById(id); // se existir o id, deletamos
				
	}
	
	
}	
