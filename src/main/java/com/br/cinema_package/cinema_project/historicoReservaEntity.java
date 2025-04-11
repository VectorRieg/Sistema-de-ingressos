package com.br.cinema_package.cinema_project;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "historico_reservas")
public class historicoReservaEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Atributos
    private int id;
    private int idFilme;
    private String status;
    private String titulo;
    private String fileira;
    private int coluna;
    private Time horario;
    //Getters e Setters
    public int getIdFilme() { return idFilme; }    
    public String getStatus() { return status; }    
    public String getTitulo() { return titulo; }    
    public String getFileira() { return fileira; }    
    public int getColuna() { return coluna; }    
    public Time getHorario() { return horario; }
    
    public void setIdFilme(int idFilme) { this.idFilme = idFilme; }
    public void setStatus(String status) { this.status = status; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setFileira(String fileira) { this.fileira = fileira; }
    public void setColuna(int coluna) { this.coluna = coluna; }
    public void setHorario(Time horario) { this.horario = horario; }
    
}