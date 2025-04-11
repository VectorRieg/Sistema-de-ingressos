package com.br.cinema_package.cinema_project;

import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "salas")
public class salaEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	//Atributos
    private int id;
    private int idFilme;
    private int coluna;
    private String nomeSala;
    private String fileira;
    private Boolean reservado;
    private Time horario;
    //Getter e Setters
    public int getId() { return id; }  
    public int getIdFilme() { return idFilme; }   
    public int getColuna() { return coluna; }   
    public String getFNomeSala() { return nomeSala; }    
    public String getFileira() { return fileira; }    
    public Boolean getReservado() { return reservado; }    
    public Time getHorario() { return horario; }   
    
    public void setId(int id) { this.id = id; }
    public void setIdFilme(int idFilme) { this.idFilme = idFilme; }
    public void setColuna(int coluna) { this.coluna = coluna; }
    public void setNomeSala(String nomeSala) { this.nomeSala = nomeSala; }
    public void setFileira(String fileira) { this.fileira = fileira; }
    public void setReservado(Boolean reservado) { this.reservado = reservado; }
    public void setHorario(Time horario) { this.horario = horario; }
    
}
