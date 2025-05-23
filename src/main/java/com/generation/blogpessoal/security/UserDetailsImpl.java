package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L; //como a userdetails implementa a interface serializable somos obrigado a implementar esse campo

	private String username;
	private String password;
	private List<GrantedAuthority> authorities; //este atributo representa as autorizações do usuário no sistema

	public UserDetailsImpl(Usuario user) {
		this.username = user.getUsuario();
		this.password = user.getSenha();
	}

	public UserDetailsImpl() {	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { //esse campo vai retornar vazio, mas serve para gerenciar as autorizações do usuario (permissoes).

		return authorities; 
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {

		return username;
	}

	@Override
	public boolean isAccountNonExpired() { // verifica se sua conta está expirada
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // verifica se sua conta está bloqueada (temporariamente ou nao)
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { // implementa para nao expirar a conta.
		return true;
	}

	@Override
	public boolean isEnabled() { //verifica se está ativo no sistema (se tiver false por exemplo) bloqueia todos usuarios do sistema.
		return true;
	}

}