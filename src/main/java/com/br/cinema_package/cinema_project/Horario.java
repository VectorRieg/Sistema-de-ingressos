package com.br.cinema_package.cinema_project;

import java.sql.Time;

public class Horario 
{
    //Atributos
	private int id;
    private int idFilme;
    private Time horario;
    //Começa e seta os valores
    public Horario(int id, int idFilme, Time horario) 
    {
        this.id = id;
        this.idFilme = idFilme;
        this.horario = horario;
    }
    //Getters 
    public int getId() { return id; }
    public int getIdFilme() { return idFilme; }
    public Time getHorario() { return horario; }
    @Override
    public String toString() { return horario.toString(); }
    
}
