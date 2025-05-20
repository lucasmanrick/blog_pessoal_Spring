package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity // define a classe Postagem como uma entidade no banco de dados
@Table(name = "tb_postagens") // especifica o nome que queremos que a classe postagem tenha no banco de dados.
public class Postagem {

	@Id //define o campo id como primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //faz com que o atributo id auto incremente-se.
	private Long id;
	
	@Column (length = 100) // cria o atributo com o tamanho, nesse caso do varchar por ser string com no maximo 100 caracteres, ou seja é usado na hora de criar a tabela no bd
	@NotBlank(message = "O atributo título é Obrigatório!") // notblank serve para strings apenas (seria um not null para strings) e para demais tipos de dados usamos o notnull mesmo.
	@Size(min=5, max=100,message ="O campo titulo deve ter entre 5 e 100 caracteres.")  // ja o size limita que na hora de criar um registro, o campo em questão não pode passar dos 100 caracteres, ou seja é para validação de preenchimento.
	private String titulo;
	
	@Column (length = 1000) // cria o atributo com o tamanho, nesse caso do varchar por ser string com no maximo 100 caracteres, ou seja é usado na hora de criar a tabela no bd
	@NotBlank(message = "O atributo título é Obrigatório!") // notblank serve para strings apenas (seria um not null para strings) e para demais tipos de dados usamos o notnull mesmo.
	@Size(min=10, max=1000,message ="O campo titulo deve ter entre 10 e 1000 caracteres.")  // ja o size limita que na hora de criar um registro, o campo em questão não pode passar dos 100 caracteres, ou seja é para validação de preenchimento.
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem") //faz com que quando tiver requisição get evite/pare a recursividade infinita por estar referenciando os dados de tema.
	private Tema tema;

	
	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
	
	
}
