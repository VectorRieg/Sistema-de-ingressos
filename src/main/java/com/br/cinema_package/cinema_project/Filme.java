package com.br.cinema_package.cinema_project;

public class Filme 
{
	//Atributos
    private int id;
    private String titulo;
    private String descricao;
    private String diretor;
    private String genero;
    private String duracao;
    private int assentoTotal;
    private int assentoDisponivel;
    private int preco;
    //Construtor com todos os atributos pra criar o objeto Filme com todos os valores 
    public Filme(int id, String titulo, String descricao, String diretor, String genero, String duracao, int assentoTotal, int assentoDisponivel, int preco) 
    {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.diretor = diretor;
        this.genero = genero;
        this.duracao = duracao;
        this.assentoTotal = assentoTotal;
        this.assentoDisponivel = assentoDisponivel;
        this.preco = preco;
    }
    //Getters e Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getDiretor() { return diretor; }
    public String getGenero() { return genero; }
    public String getDuracao() { return duracao; }
    public int getAssentoTotal() { return assentoTotal; }
    public int getAssentoDisponivel() { return assentoDisponivel; }
    public int getPreco() { return preco; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    public void setAssentoTotal(int assentoTotal) { this.assentoTotal = assentoTotal; }
    public void setAssentoDisponivel(int assentoDisponivel) { this.assentoDisponivel = assentoDisponivel; }
    public void setPreco(int preco) { this.preco = preco; }
    //Converte os dados do objeto pra uma String em CSV
    public String toFileString() 
    {
        return id + "," + titulo + "," + descricao + "," + diretor + "," + genero + "," + duracao + "," + assentoTotal + "," + assentoDisponivel + "," + preco;
    }

    public static Filme fromFileString(String line) //Divide a String por virgula
    {
        String[] parts = line.split(",");
        return new Filme(
            Integer.parseInt(parts[0]),
            parts[1],
            parts[2],
            parts[3],
            parts[4],
            parts[5],
            Integer.parseInt(parts[6]),
            Integer.parseInt(parts[7]),
            Integer.parseInt(parts[8])
        );
    } 	
    //Sobrescreve o toString pra mostrar os dados do filme no terminal
    @Override
    public String toString() 
    {
        return id + " - " + titulo + " | Descrição: " + descricao + " | Diretor: " + diretor + " | Gênero: " + genero + " | Duração:  " + duracao + " | Assentos: " + assentoDisponivel + " | Preço: R$" + preco;
    }
}
