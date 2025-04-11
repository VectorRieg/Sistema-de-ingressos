package com.br.cinema_package.cinema_project;

import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "horarios")
public class horarioEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	//Atributos
    private int id;
    private int idFilme;
    private Time horario;
    //Getters e Setters
    public int getId() { return id; }
    public int getIdFilme() { return idFilme; }
    public Time getHorario() { return horario; }
    
    public void setId(int id) { this.id = id; }
    public void setIdfilme(int idFilme) { this.idFilme = idFilme; }
    public void setHorario(Time horario) { this.horario = horario; }
    
}
