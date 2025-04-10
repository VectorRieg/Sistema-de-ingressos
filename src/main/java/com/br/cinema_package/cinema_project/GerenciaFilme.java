package com.br.cinema_package.cinema_project;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public class GerenciaFilme 
{
    private static final String Filme_FILE = "Filmes.json";
    private static final String Historico_FILE = "Reserva_Historico.txt";

    public void salvarFilmes(List<Filme> filmes) 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Filme_FILE))) 
        {
            for (Filme filme : filmes) 
            {
                bw.write(filme.toFileString());
                bw.newLine();
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
            System.out.println(filme);
        }
    }
    public void salvarHistorico(String titulo, String fileira, int coluna, Time horarioEscolhido) {
        String sql = "INSERT INTO historico_reservas ( titulo_filme, fileira, coluna, horario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, fileira);
            stmt.setInt(3, coluna);
            stmt.setTime(4, horarioEscolhido);

            stmt.executeUpdate();
            System.out.println("Histórico salvo com sucesso.");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }
    public void verHistorico() {
        String sql = "SELECT titulo_filme, fileira, coluna, horario FROM historico_reservas";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("===== Histórico de Reservas =====");
            while (rs.next()) {
                String titulo = rs.getString("titulo_filme");
                String fileira = rs.getString("fileira");
                int coluna = rs.getInt("coluna");
                Time horario = rs.getTime("horario");

                System.out.println("Filme: " + titulo + " | Assento: " + fileira + coluna + " | Horário: " + horario);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        }
    }

    public void salvarFilmeNoBanco(Filme filme) 
    {
    	String sql = "INSERT INTO filmes (titulo, descricao, diretor, genero, duracao, assento_total, assento_disponivel, preco) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = Conexao.conectar();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) 
        {

            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getDescricao());
            stmt.setString(3, filme.getDiretor());
            stmt.setString(4, filme.getGenero());
            stmt.setString(5, filme.getDuracao());
            stmt.setInt(6, filme.getAssentoTotal());
            stmt.setInt(7, filme.getAssentoDisponivel());
            stmt.setInt(8, filme.getPreco());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                int idFilmeGerado = rs.getInt("id");
                System.out.println("Filme salvo com sucesso no banco! ID: " + idFilmeGerado);

                criarSalaParaFilme(idFilmeGerado, 20, 10); // Ex: 20 fileiras (A-T), 10 colunas (1-10)
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao salvar filme: " + e.getMessage());
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
                filmes.add(filme);
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

            int linhasAfetadas = pstmt.executeUpdate();
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
            stmt.executeBatch();
            System.out.println("Sala criada para o filme " + idFilme);
        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao criar sala: " + e.getMessage());
        }
    }
    public void exibirMapaSala(int idFilme) 
    {
        Map<String, Boolean> mapaAssentos = new HashMap<>();
        Set<String> fileiras = new TreeSet<>();
        Set<Integer> colunas = new TreeSet<>();

        String sql = "SELECT fileira, coluna, reservado FROM salas WHERE id_filme = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            
            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                String fileira = rs.getString("fileira");
                int coluna = rs.getInt("coluna");
                boolean reservado = rs.getBoolean("reservado");

                mapaAssentos.put(fileira + "-" + coluna, reservado);
                fileiras.add(fileira);
                colunas.add(coluna);
            }

            System.out.println("Mapa da sala (○ = livre, X = reservado):");
            for (String fileira : fileiras) 
            {
                for (int col : colunas) 
                {
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
    public void reservarAssentoEspecifico(int idFilme, String fileira, int coluna, Time horarioEscolhido) 
    {
        String sqlVerifica = "SELECT reservado FROM salas WHERE id_filme = ? AND fileira = ? AND coluna = ? AND horario = ?";
        String sqlReserva = "UPDATE salas SET reservado = TRUE WHERE id_filme = ? AND fileira = ? AND coluna = ? AND horario = ?";
        String sqlHistorico = "INSERT INTO historico_reservas (id_filme, fileira, coluna, horario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement verificaStmt = conn.prepareStatement(sqlVerifica);
             PreparedStatement reservaStmt = conn.prepareStatement(sqlReserva);
             PreparedStatement historicoStmt = conn.prepareStatement(sqlHistorico)) 
        {

            verificaStmt.setInt(1, idFilme);
            verificaStmt.setString(2, fileira);
            verificaStmt.setInt(3, coluna);
            reservaStmt.setTime(4, horarioEscolhido);
            ResultSet rs = verificaStmt.executeQuery();

            if (rs.next()) 
            {
                boolean reservado = rs.getBoolean("reservado");
                if (reservado) 
                {
                    System.out.println("Assento já está reservado!");
                } else 
                
                {
                    reservaStmt.setInt(1, idFilme);
                    reservaStmt.setString(2, fileira);
                    reservaStmt.setInt(3, coluna);
                    reservaStmt.setTime(4, horarioEscolhido);
                    reservaStmt.executeUpdate();
                    
                    Filme filme = obterFilmePorId(idFilme);
                    
                    historicoStmt.setInt(1, idFilme);
                    historicoStmt.setString(2, fileira);
                    historicoStmt.setInt(3, coluna);
                    historicoStmt.setTime(4, horarioEscolhido);
                    historicoStmt.executeUpdate();

                    atualizarAssentosDisponiveis(idFilme);
                    System.out.println("Assento reservado com sucesso no horário " + horarioEscolhido + "!");
                    
                    salvarHistorico(idFilme, filme.getTitulo(), fileira, coluna, horarioEscolhido);
                }
            } else 
            
            {
                System.out.println("Assento não encontrado.");
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao reservar assento específico: " + e.getMessage());
        }
    }
    public void salvarHistorico(int idFilme, String titulo, String fileira, int coluna, Time horarioEscolhido) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Reserva_Historico.txt", true))) {
            String registro = String.format("Filme ID: %d | Título: %s | Assento: %s%d | Horário: %s", 
                                            idFilme, titulo, fileira, coluna, horarioEscolhido.toString());
            bw.write(registro);
            bw.newLine();
            System.out.println("Histórico salvo com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    public void atualizarAssentosDisponiveis(int idFilme) 
    {
        String sql = "SELECT COUNT(*) FROM salas WHERE id_filme = ? AND reservado = false";
        String sqlUpdate = "UPDATE filmes SET assento_disponivel = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement countStmt = conn.prepareStatement(sql);
             PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) 
        {

            countStmt.setInt(1, idFilme);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next()) 
            {
                int disponiveis = rs.getInt(1);
                updateStmt.setInt(1, disponiveis);
                updateStmt.setInt(2, idFilme);
                updateStmt.executeUpdate();
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao atualizar assentos disponíveis: " + e.getMessage());
        }
    }
    public Filme obterFilmePorId(int idFilme) 
    {
        String sql = "SELECT * FROM filmes WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                return new Filme(
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
            }
        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao buscar filme por ID: " + e.getMessage());
        }
        return null;
    }
    public void cancelarReservaEspecifica(int idFilme, String fileira, int coluna, Time horarioEscolhido) {
        String sqlVerifica = "SELECT reservado FROM salas WHERE id_filme = ? AND fileira = ? AND coluna = ? AND horario = ?";
        String sqlCancela = "UPDATE salas SET reservado = FALSE WHERE id_filme = ? AND fileira = ? AND coluna = ? AND horario = ?";
        String sqlDeleteHistorico = "DELETE FROM historico_reservas WHERE id_filme = ? AND fileira = ? AND coluna = ? AND horario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement verificaStmt = conn.prepareStatement(sqlVerifica);
             PreparedStatement cancelaStmt = conn.prepareStatement(sqlCancela);
             PreparedStatement deleteHistoricoStmt = conn.prepareStatement(sqlDeleteHistorico)) {

            verificaStmt.setInt(1, idFilme);
            verificaStmt.setString(2, fileira);
            verificaStmt.setInt(3, coluna);
            verificaStmt.setTime(4, horarioEscolhido);
            ResultSet rs = verificaStmt.executeQuery();

            if (rs.next()) {
                boolean reservado = rs.getBoolean("reservado");
                if (!reservado) {
                    System.out.println("Assento já está livre.");
                } else {
                    // Liberar o assento
                    cancelaStmt.setInt(1, idFilme);
                    cancelaStmt.setString(2, fileira);
                    cancelaStmt.setInt(3, coluna);
                    cancelaStmt.setTime(4, horarioEscolhido);
                    cancelaStmt.executeUpdate();

                    // Remover do histórico
                    deleteHistoricoStmt.setInt(1, idFilme);
                    deleteHistoricoStmt.setString(2, fileira);
                    deleteHistoricoStmt.setInt(3, coluna);
                    deleteHistoricoStmt.setTime(4, horarioEscolhido);
                    deleteHistoricoStmt.executeUpdate();

                    atualizarAssentosDisponiveis(idFilme);
                    System.out.println("Reserva cancelada com sucesso para o assento " + fileira + coluna + " no horário " + horarioEscolhido + ".");
                }
            } else {
                System.out.println("Assento não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cancelar reserva: " + e.getMessage());
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
    public List<Horario> listarHorariosPorFilme(int idFilme) {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT * FROM horarios WHERE id_filme = ? ORDER BY horario";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Time horario = rs.getTime("horario");
                horarios.add(new Horario(id, idFilme, horario));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar horários: " + e.getMessage());
        }

        return horarios;
    }
    public void verReservasPorFilme(int idFilme) {
        String sql = "SELECT h.fileira, h.coluna, h.horario, f.titulo " +
                     "FROM historico_reservas h " +
                     "JOIN filmes f ON h.id_filme = f.id " +
                     "WHERE h.id_filme = ? " +
                     "ORDER BY h.horario";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery();

            boolean encontrou = false;
            while (rs.next()) {
                encontrou = true;
                String titulo = rs.getString("titulo");
                String fileira = rs.getString("fileira");
                int coluna = rs.getInt("coluna");
                Time horario = rs.getTime("horario");

                System.out.println("Filme: " + titulo + " | Assento: " + fileira + coluna + " | Horário: " + horario);
            }

            if (!encontrou) {
                System.out.println("Nenhuma reserva encontrada para o filme com ID " + idFilme);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar reservas do filme: " + e.getMessage());
        }
    }
    public void criarSalaParaFilme(int idFilme, int linhas, int colunas, List<Time> horarios) {
        String sql = "INSERT INTO salas (id_filme, fileira, coluna, horario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (char row = 'A'; row < 'A' + linhas; row++) {
                for (int col = 1; col <= colunas; col++) {
                    for (Time horario : horarios) {
                        stmt.setInt(1, idFilme);
                        stmt.setString(2, String.valueOf(row));
                        stmt.setInt(3, col);
                        stmt.setTime(4, horario);
                        stmt.addBatch();
                    }
                }
            }
            stmt.executeBatch();
            System.out.println("Salas criadas com horários para o filme " + idFilme);
        } catch (SQLException e) {
            System.out.println("Erro ao criar sala: " + e.getMessage());
        }
    }
}
