/*
 * Classe modelo padrão JAVA
 */
package br.facol.modelo;

import java.util.Calendar;

public class Funcionario {
	private long id;
	private String nome;
	private Calendar nascimento;
	private String email;
	private String telefone;

	public Funcionario() {
	}

	public Funcionario(long id, String nome, Calendar nascimento, String email, String telefone) {
		this.id = id;
		this.nome = nome;
		this.nascimento = nascimento;
		this.email = email;
		this.telefone = telefone;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Calendar getNascimento() {
		return nascimento;
	}

	public void setNascimento(Calendar nascimento) {
		this.nascimento = nascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
