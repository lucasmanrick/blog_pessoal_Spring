package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

	List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);
	
	
	//na pratica estamos criando um novo modo de consulta fora do padrão (CRUD) cada um dos itens digitados refere-se a uma parte da query que implementamos
	
	//na pratica estamos fazendo isso:  SELECT * FROM tb_postagens WHERE titulo LIKE "%?%";  link de ajuda: https://github.com/conteudoGeneration/cookbook_java_fullstack/blob/main/04_spring/guia_jpa.md
}
