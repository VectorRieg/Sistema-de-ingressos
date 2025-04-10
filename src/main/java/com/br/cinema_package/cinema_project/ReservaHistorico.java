package com.br.cinema_package.cinema_project;

import java.time.LocalDateTime;

public class ReservaHistorico 
{
    private String titulo;
    private int assento;
    private LocalDateTime data;

    public ReservaHistorico(String titulo, int assento, LocalDateTime data) 
    {
        this.titulo = titulo;
        this.assento = assento;
        this.data = data;
    }

    @Override
    public String toString() 
    {
        return "Filme: " + titulo + " | Assentos: " + assento + " | Data: " + data;
    }
}