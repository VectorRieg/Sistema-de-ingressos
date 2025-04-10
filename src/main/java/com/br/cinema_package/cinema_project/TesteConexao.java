package com.br.cinema_package.cinema_project;

import java.sql.Connection;

public class TesteConexao 
{
    public static void main(String[] args) 
    {
        Connection conn = Conexao.conectar();

        if (conn != null) 
        {
            System.out.println("✅ Conexão com o banco realizada com sucesso!");
        } 
        else 
        {
            System.out.println("❌ Falha na conexão.");
        }
    }
}