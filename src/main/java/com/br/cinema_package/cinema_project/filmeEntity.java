package com.br.cinema_package.cinema_project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "filmes")
public class filmeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Atributos
    private int id;
    private String titulo;
    private String descricao;
    private String diretor;
    private String genero;
    private int assentoTotal;
    private int assentoDisponivel;
    private int preco;
    private String duracao;
    // Getters e setters
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }    
    public String getDiretor() { return diretor; }    
    public String getGenero() { return genero; }   
    public int getAssentoTotal() { return assentoTotal; }
    public int getAssentoDisponivel() { return assentoDisponivel; }
    public int getPreco() { return preco; }
    public String getDuracao() { return duracao; }
    
    public void setTitlo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDiretor(String diretor) { this.titulo = diretor; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setAssentoTotal(int assentoTotal) { this.assentoTotal = assentoTotal; }
    public void setAssentoDisponivel(int assentoDisponivel) { this.assentoDisponivel = assentoDisponivel; }
    public void setpreco(int preco) { this.preco = preco; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    
}