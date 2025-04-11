package com.br.cinema_package.cinema_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.Transaction;
//conn = conection
//stmt = statement
//tx   = transaction
public class GerenciaSala 
{
	public void criarSalaParaFilme(int idFilme, int linhas, int colunas, List<Time> horarios) 
	{
		//Nome da sala mais o ID (Sala 1)
	    String nomeSala = "Sala " + idFilme;
	    //Abre o hibernate e inicia a transação
	    Session sessao = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = sessao.beginTransaction();

	    try 
	    {
	    	//Loop pra fileira
	        for (char row = 'A'; row < 'A' + linhas; row++) 
	        {
	        	//Loop pra coluna
	            for (int col = 1; col <= colunas; col++) 
	            {
	            	//Loop pro horario
	                for (Time horario : horarios) 
	                {
	                	//Cria o salaEntity pra representar o objeto
	                    salaEntity sala = new salaEntity();
	                    sala.setIdFilme(idFilme);
	                    sala.setNomeSala(nomeSala);
	                    sala.setFileira(String.valueOf(row));
	                    sala.setColuna(col);
	                    sala.setHorario(horario);
	                    sala.setReservado(false);

	                    sessao.persist(sala); //Hibernate salva o objeto no banco
	                }
	            }
	        }
	        tx.commit(); //Finaliza
	        System.out.println("Sala '" + nomeSala + "' criada com horários para o filme " + idFilme);
	    } catch (Exception e) 
	    
	    {
	        tx.rollback(); //Desfaz tudo
	        System.out.println("Erro ao criar sala: " + e.getMessage());
	    } finally 
	    
	    {
	    	sessao.close(); //Fecha a sessão com o banco
	    }
	}
	public void exibirMapaSala(int idFilme) 
    {
		//O mapa conecta a fileira e a coluna pra ficar verdadeiro ou falso (reservado ou livre)
        Map<String, Boolean> mapaAssentos = new HashMap<>();
        //Guarda a informação sem duplicar
        Set<String> fileiras = new TreeSet<>();
        Set<Integer> colunas = new TreeSet<>();

        String sql = "SELECT fileira, coluna, reservado FROM salas WHERE id_filme = ?";
        //Conecta e prepara o a consulta aq de cima	
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            //Define o ID do filme
            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {	
            	//Pega os valores da linha
                String fileira = rs.getString("fileira");
                int coluna = rs.getInt("coluna");
                boolean reservado = rs.getBoolean("reservado");
                //Cria uma chave (A-5)
                mapaAssentos.put(fileira + "-" + coluna, reservado);
                fileiras.add(fileira);
                colunas.add(coluna);
            }
            //Mapinha
            System.out.println("Mapa da sala (○ = livre, X = reservado):");
            for (String fileira : fileiras) 
            {
                for (int col : colunas) 
                {
                	//Verifica se ta reservado
                    String key = fileira + "-" + col;
                    String simbolo = mapaAssentos.getOrDefault(key, false) ? "X" : "○";
                    System.out.print("[" + simbolo + "] ");
                }
                System.out.println();
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao exibir mapa da sala: " + e.getMessage());
        }
    }
}
