package com.br.cinema_package.cinema_project;

import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reservas")
public class reservaEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	//Atributos
    private int id;
    private String titulo;
    private int quantidade;
    private Time horario;
    //Getters e Setters
    public int getId() { return id; }    
    public String getTitulo() { return titulo; }    
    public int getQuantidade() { return quantidade; }    
    public Time getHorario() { return horario; }
    
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setHorario(Time horario) { this.horario = horario; }
    
}
