package com.generation.blogpessoal.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity // define a classe Postagem como uma entidade no banco de dados
@Table(name = "tb_temas")
public class Tema {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 60)
	@NotBlank(message="o campo descrição da tabela tema deve ser preenchido")
	@Size(min = 0, max = 60, message = "o tamanho do campo descrição da tabela tema deve ter no maximo 60 caracteres e no minimo 0.")
	private String descricao;
	
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // aki definimos que o campo/atributo dessa tabela tema, fetch sendo lazy ele só carrega os dados desse campo caso requisitemos ele, se não requisitar-mos ele não puxa os dados, já se colocassemos o tipo EAGER ele ja traria os dados. O mappedBy referencia ao campo/atributo "TEMA" da tabela POSTAGEM e por fim o cascade faz com que se um registro for deletado ele deleta das demais tabelas que tem essa linkagem/relacionamento.
	@JsonIgnoreProperties("tema") // ignora recursividade ao fazer um get, ou seja ao fazer um get nessa tabela ele nao fica referenciando a tabela postagem e a tabela postagem voltando a referenciar a esta infinitamente.
	private List<Postagem> postagem;

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
