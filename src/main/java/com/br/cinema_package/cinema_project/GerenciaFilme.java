package com.br.cinema_package.cinema_project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class GerenciaFilme 
{	
	//Caminho pro JSON pra salvar localmente
    private static final String Filme_FILE = "Filmes.json";

    public void salvarFilmes(List<Filme> filmes) 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Filme_FILE))) 
        {
            for (Filme filme : filmes) 
            {
            	//Escreve a String do filme no arquivo
                bw.write(filme.toFileString());
                bw.newLine(); //Pula a linha
            }
        } catch (IOException e) 
        
        {
            System.out.println("Erro ao salvar filmes: " + e.getMessage());
        }
    }

    public void listarFilmes(List<Filme> filmes) 
    {
        for (Filme filme : filmes) 
        {
            System.out.println(filme); //Mostra o filme
        }
    }
    public void salvarFilmeNoBanco(Filme filme) 
    {
    	String sql = "INSERT INTO filmes (titulo, descricao, diretor, genero, duracao, assento_total, assento_disponivel, preco) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = Conexao.conectar(); //Conecta com o banco
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
        	//Define os parametros do query
            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getDescricao());
            stmt.setString(3, filme.getDiretor());
            stmt.setString(4, filme.getGenero());
            stmt.setString(5, filme.getDuracao());
            stmt.setInt(6, filme.getAssentoTotal());
            stmt.setInt(7, filme.getAssentoDisponivel());
            stmt.setInt(8, filme.getPreco());
            //Executa e retorna o ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                int idFilmeGerado = rs.getInt("id");
                System.out.println("Filme salvo com sucesso no banco! ID: " + idFilmeGerado);
                //Cria a sala pro filme
                criarSalaParaFilme(idFilmeGerado, 20, 10); // Ex: 20 fileiras (A-T), 10 colunas (1-10)
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao salvar filme: " + e.getMessage());
        }
    }
    public void criarSalaParaFilme(int idFilme, int linhas, int colunas) 
    {
        String sql = "INSERT INTO salas (id_filme, fileira, coluna) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {

            for (char row = 'A'; row < 'A' + linhas; row++) 
            {
                for (int col = 1; col <= colunas; col++) 
                {
                    stmt.setInt(1, idFilme);
                    stmt.setString(2, String.valueOf(row));
                    stmt.setInt(3, col);
                    stmt.addBatch(); 
                }
            }
            stmt.executeBatch(); //Executa tudo
            System.out.println("Sala criada para o filme " + idFilme);
        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao criar sala: " + e.getMessage());
        }
    }
    public void importarFilmesParaBanco(List<Filme> filmes) 
    {
        for (Filme filme : filmes) 
        {
            salvarFilmeNoBanco(filme);
        }
    }
    public List<Filme> carregarFilmesDoBanco() 
    {
        List<Filme> filmes = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String usuario = "postgres";
        String senha = "123546789";

        String sql = "SELECT id, titulo, descricao, diretor, genero, duracao, assento_total, assento_disponivel, preco FROM filmes";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) 
        {

            while (rs.next()) 
            {
            	//Cria o objeto Filme a partir dos dados do banco
                Filme filme = new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("diretor"),
                    rs.getString("genero"),
                    rs.getString("duracao"),
                    rs.getInt("assento_total"),
                    rs.getInt("assento_disponivel"),
                    rs.getInt("preco")
                );
                filmes.add(filme); //Adiciona na lista
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao carregar filmes do banco: " + e.getMessage());
        }

        return filmes;
    }
    public void atualizarAssentosNoBanco(int idFilme, int novoAssentoDisponivel) 
    {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String usuario = "postgres";
        String senha = "123546789";

        String sql = "UPDATE filmes SET assento_disponivel = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha);
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {

            pstmt.setInt(1, novoAssentoDisponivel);
            pstmt.setInt(2, idFilme);

            int linhasAfetadas = pstmt.executeUpdate(); //Executa o UPTADE
            if (linhasAfetadas > 0) 
            {
                System.out.println("Assentos atualizados com sucesso no banco.");
            } else 
            
            {
                System.out.println("Nenhum filme encontrado com esse ID.");
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao atualizar assentos: " + e.getMessage());
        }
    }
    
    public void salvarHistorico(int idFilme, String titulo, String fileira, int coluna, Time horarioEscolhido) 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Reserva_Historico.txt", true))) 
        {
        	//Cria a linha com as informações da reserva
            String registro = String.format("Filme ID: %d | Título: %s | Assento: %s%d | Horário: %s", 
                                            idFilme, titulo, fileira, coluna, horarioEscolhido.toString());
            bw.write(registro); //Escreve no arquivo
            bw.newLine(); //Nova linha
            System.out.println("Histórico salvo com sucesso.");
        } catch (IOException e) 
        
        {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    } 
    public void buscarFilmePorTitulo(String titulo) 
    {
        String sql = "SELECT * FROM filmes WHERE LOWER(titulo) LIKE LOWER(?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();

            boolean encontrou = false;
            while (rs.next()) 
            {
                encontrou = true;
                int id = rs.getInt("id");
                String nome = rs.getString("titulo");
                String genero = rs.getString("genero");
                String duracao = rs.getString("duracao");
                //Mostra o filme encontrado
                System.out.println("\nFilme encontrado:");
                System.out.println("ID: " + id);
                System.out.println("Título: " + nome);
                System.out.println("Gênero: " + genero);
                System.out.println("Duração: " + (duracao != null ? duracao : "Não informada"));
            }

            if (!encontrou) 
            {
                System.out.println("Nenhum filme encontrado com esse título.");
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao buscar filme: " + e.getMessage());
        }
    }
    public List<Horario> listarHorariosPorFilme(int idFilme) 
    {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT * FROM horarios WHERE id_filme = ? ORDER BY horario";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {

            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                int id = rs.getInt("id");
                Time horario = rs.getTime("horario");
                horarios.add(new Horario(id, idFilme, horario));
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao buscar horários: " + e.getMessage());
        }

        return horarios;
    }

}
