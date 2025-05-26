package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String USUARIO_ROOT_EMAIL = "root@email.com";
	private static final String USUARIO_ROOT_SENHA = "1234";
	private static final String BASE_URL_USUARIOS = "/usuarios";
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuarioRoot());
	}
	
	@Test
	@DisplayName("Esse Teste Deve Cadastrar um novo usuário com sucesso!")
	public void deveCadastrarUsuario () {
		
		//Given  (dados que vamos trabalhar)
			Usuario usuario = TestBuilder.criarUsuario(null, "Renata Negrini", "renata@email.com", "12345678");
		
		//When (o que vamos fazer)
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST , requisicao, Usuario.class);
		//os parametros tratase do primeiro parametro recurso e procedimento a ser executado, qual sera o metodo dessa URI (post), requisição trata-se do objeto instanciado com os dados que queremos enviar, e Usuario.class é nós falando o tipo de dado que será enviado.
		
			
		//Then 
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode()); // falamos o que esperamos receber de retorno e de onde a resposta virá.
		assertEquals("Renata Negrini", resposta.getBody().getNome());
		assertEquals("renata@email.com", resposta.getBody().getUsuario());
	}
	
	
	@Test
	@DisplayName("Não deve permitir a repetição de um cadastro com o mesmo usuario")
	public void naoDeveDuplicarUsuario () {
		//Given  (dados que vamos trabalhar)
			Usuario usuario = TestBuilder.criarUsuario(null, "Lucas Manrick", "lucas@email.com", "87654321");
			usuarioService.cadastrarUsuario(usuario);
			
		//When  (o que vamos fazer/quando)
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST , requisicao, Usuario.class);
		//os parametros tratase do primeiro parametro recurso e procedimento a ser executado, qual sera o metodo dessa URI (post), requisição trata-se do objeto instanciado com os dados que queremos enviar, e Usuario.class é nós falando o tipo de dado que será enviado.
		
		
		//Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve atualizar os dados de um usuário com sucesso")
	public void deveAtualizarUsuario() {
		//Given  (dados que vamos trabalhar)
		Usuario usuario = TestBuilder.criarUsuario(null, "Giovana", "Giovana@email.com", "12345678");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);
		
		var usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId(),"Giovana Lucia", "GiovanaLucia@email.com", "12345678");
		
		
		//When  (o que vamos fazer/quando)
				HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
				ResponseEntity<Usuario> resposta = testRestTemplate
						.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA) //Faz autenticação com os dados definidos acima, pois para ter a atualização ele precisa
						.exchange(BASE_URL_USUARIOS + "/atualizar", HttpMethod.PUT , requisicao, Usuario.class);
				
				//os parametros tratase do primeiro parametro recurso e procedimento a ser executado, qual sera o metodo dessa URI (post), requisição trata-se do objeto instanciado com os dados que queremos enviar, e Usuario.class é nós falando o tipo de dado que será enviado.
				
				
		//Then 		
		assertEquals(HttpStatus.OK, resposta.getStatusCode()); // falamos o que esperamos receber de retorno e de onde a resposta virá.
		assertEquals("Giovana Lucia", resposta.getBody().getNome());
		assertEquals("GiovanaLucia@email.com", resposta.getBody().getUsuario());

	}
	
	@Test
	@DisplayName("Deve listar todos os usuários com sucesso")
	public void deveListarTodosUsuarios() {
		
		 //Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Jovani Almeida", "jovani_almeida@email.com.br", "12345678"));
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Carlos Garcia", "carlos_garcia@email.com.br", "12345678"));
		
		//When
		ResponseEntity<Usuario[]> resposta = testRestTemplate
					.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
					.exchange(BASE_URL_USUARIOS, HttpMethod.GET, null, Usuario[].class);
		
		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		}
		   
		
		@Test
		@DisplayName("Deve pegar um usuario por seu id")
		public void deveListarUmUsuarioPeloId() {
			 //Given
			usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Jovani Almeida", "jovani_almeida@email.com.br", "12345678"));
			Optional<Usuario> returnRegister = usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Carlos Garcia", "carlos_garcia@email.com.br", "12345678"));

			
			//When
			ResponseEntity<Usuario> resposta = testRestTemplate
						.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
						.exchange(BASE_URL_USUARIOS + "/" + returnRegister.get().getId(), HttpMethod.GET, null, Usuario.class);
		   
			//Then
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
			assertNotNull(resposta.getBody());
	}
		
		@Test
		@DisplayName("Deve autenticar o usuario")
		public void deveAutenticarOUsuario() {
		 Usuario creatingUser = TestBuilder.criarUsuario(null, "Kleber Teles", "kevlerTeles@email.com.br", "12345678");
		 usuarioService.cadastrarUsuario(creatingUser);
		 
		 System.out.println(usuarioService.getAllUsers());
		 
		 UsuarioLogin tentativaLogin = TestBuilder.criandoUsuarioLogin("kevlerTeles@email.com.br","12345678");
		 
		 
		//When (o que vamos fazer)
			HttpEntity<UsuarioLogin> requisicao = new HttpEntity<UsuarioLogin>(tentativaLogin);
			ResponseEntity<UsuarioLogin> resposta = testRestTemplate.exchange(BASE_URL_USUARIOS + "/logar", HttpMethod.POST , requisicao, UsuarioLogin.class);
		 
			
		//Then
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		}
		
}
