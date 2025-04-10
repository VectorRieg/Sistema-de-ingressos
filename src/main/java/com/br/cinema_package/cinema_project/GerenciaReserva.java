package com.br.cinema_package.cinema_project;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("unused")
public class GerenciaReserva 
{
    private static final String Historico_FILE = "Reserva_Historico.txt";

    public static void adicionarReserva(GerenciaReserva historico) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Historico_FILE, true))) 
        {
            writer.write(historico.toString());
            writer.newLine();
        } catch (IOException e) 
        
        {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    public static void verHistorico() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Historico_FILE))) 
        {
            String line;
            System.out.println("\n=== Histórico de Reservas ===");
            while ((line = reader.readLine()) != null) 
            {
                System.out.println(line);
            }
        } catch (IOException e) 
        
        {
            System.out.println("Erro ao ler histórico: " + e.getMessage());
        }
    }
}