package com.br.cinema_package.cinema_project;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@SuppressWarnings("unused")
public class GerenciaReserva 
{
    public void reservarAssentoEspecifico(int idFilme, String titulo, String fileira, int coluna, Time horarioEscolhido) 
    {
        //Abre o hibernate e inicia a transação
    	Session sessao = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sessao.beginTransaction();

        try 
        {	
        	//Encontra o assento da fileira, coluna, o filme e o horario
            String hql = "FROM salaEntity WHERE idfilme = :idFilme AND fileira = :fileira AND coluna = :coluna AND horario = :horario";
            Query<salaEntity> query = sessao.createQuery(hql, salaEntity.class);
            query.setParameter("idFilme", idFilme);
            query.setParameter("fileira", fileira);
            query.setParameter("coluna", coluna);
            query.setParameter("horario", horarioEscolhido);

            salaEntity assento = query.uniqueResult(); //Resultado unico

            if (assento != null) 
            {
                if (assento.getReservado()) 
                {
                    System.out.println("Assento já está reservado!");
                } else 
                
                {
                    assento.setReservado(true); //Marca como reservado
                    sessao.update(assento); //Atualiza o banco

                    //Cria e preenche o histórico
                    historicoReservaEntity historico = new historicoReservaEntity();
                    historico.setIdFilme(idFilme);
                    historico.setStatus("Reservado");
                    historico.setTitulo(titulo);
                    historico.setFileira(fileira);
                    historico.setColuna(coluna);
                    historico.setHorario(horarioEscolhido);

                    sessao.persist(historico); //Coloca o historico no banco

                    atualizarAssentosDisponiveisHibernate(sessao, idFilme); //Atualiza os assentos disponiveis

                    System.out.println("Assento reservado com sucesso no horário " + horarioEscolhido + "!");
                }
            } else 
            
            {
                System.out.println("Assento não encontrado.");
            }

            tx.commit(); //Confirma
        } catch (Exception e) 
        
        {
            if (tx != null) tx.rollback(); //Desfaz se der erro
            System.out.println("Erro ao reservar assento específico: " + e.getMessage());
        } finally 
        
        {
            sessao.close(); //Fecha
        }
    }
    public void atualizarAssentosDisponiveisHibernate(Session session, int idFilme) 
    {
        try 
        {
            //Conta os assentos disponíveis
            String hqlCount = "SELECT COUNT(*) FROM salaEntity WHERE idfilme = :idFilme AND reservado = false";
            Long disponiveis = (Long) session.createQuery(hqlCount)
                                             .setParameter("idFilme", idFilme)
                                             .uniqueResult();

            //Atualiza a tabela de filmes com o novo valor
            String hqlUpdate = "UPDATE Filme SET assentoDisponivel = :disponiveis WHERE id = :idFilme";
            session.createQuery(hqlUpdate)
                   .setParameter("disponiveis", disponiveis.intValue())
                   .setParameter("idFilme", idFilme)
                   .executeUpdate();

        } catch (Exception e) 
        
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
        	//Define o ID do filme
            stmt.setInt(1, idFilme);
            ResultSet rs = stmt.executeQuery(); //Executa
            if (rs.next()) 
            {
            	//Cria o objeto Filme
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
        return null; //Se não encontrar o Filme
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
            ResultSet rs = countStmt.executeQuery(); //Conta os assentos não reservados

            if (rs.next()) 
            {
                int disponiveis = rs.getInt(1); //Pega o valor da contagem
                updateStmt.setInt(1, disponiveis);
                updateStmt.setInt(2, idFilme);
                updateStmt.executeUpdate(); //Atualiza o banco
            }

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao atualizar assentos disponíveis: " + e.getMessage());
        }
    }
    public void verHistorico() 
    {
        Session sessao = HibernateUtil.getSessionFactory().openSession();
        
        try 
        {
            String hql = "FROM historicoReservaEntity";
            List<historicoReservaEntity> historicoList = sessao.createQuery(hql, historicoReservaEntity.class).list();

            System.out.println("===== Histórico de Reservas =====");
            for (historicoReservaEntity h : historicoList) 
            {
            	//Mostra os dados de cada reserva
                String titulo = h.getTitulo();
                String fileira = h.getFileira();
                int coluna = h.getColuna();
                Time horario = h.getHorario();
                String status = h.getStatus();

                System.out.println("Filme: " + titulo + " | Assento: " + fileira + coluna + " | Horário: " + horario + " | Status: " + status);
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        } 
        finally 
        {
            sessao.close();
        }
    }
    public void salvarHistorico(String titulo, String fileira, int coluna, Time horarioEscolhido) 
    {
        String sql = "INSERT INTO historico_reservas ( titulo_filme, fileira, coluna, horario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {

            stmt.setString(1, titulo);
            stmt.setString(2, fileira);
            stmt.setInt(3, coluna);
            stmt.setTime(4, horarioEscolhido);

            stmt.executeUpdate();
            System.out.println("Histórico salvo com sucesso.");

        } catch (SQLException e) 
        
        {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }
    public void verReservasPorFilme(int idFilme) 
    {
        Session sessao = HibernateUtil.getSessionFactory().openSession();

        try 
        {
        	//Busca o historico por filme
            String hql = "FROM historicoReservaEntity WHERE idFilme = :idFilme ORDER BY horario";
            List<historicoReservaEntity> reservas = sessao.createQuery(hql, historicoReservaEntity.class)
                                                          .setParameter("idFilme", idFilme)
                                                          .list();

            if (reservas.isEmpty()) 
            {
                System.out.println("Nenhuma reserva encontrada para o filme com ID " + idFilme);
            } else 
            
            {
                System.out.println("===== Reservas para o Filme ID " + idFilme + " =====");
                for (historicoReservaEntity reserva : reservas) 
                {
                	//Mostra os dados da reserva
                    String titulo = reserva.getTitulo();
                    String fileira = reserva.getFileira();
                    int coluna = reserva.getColuna();
                    Time horario = reserva.getHorario();

                    System.out.println("Filme: " + titulo + " | Assento: " + fileira + coluna + " | Horário: " + horario);
                }
            }

        } catch (Exception e) 
        
        {
            System.out.println("Erro ao buscar reservas do filme: " + e.getMessage());
        } finally 
        
        {
            sessao.close();
        }
    }
    public void cancelarReserva(int idFilme, String fileira, int coluna, Time horario) 
    {
        Session sessao = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sessao.beginTransaction();

        try 
        {
        	//Busca o assento que vai ser cancelado
            String hql = "FROM salaEntity WHERE idfilme = :idFilme AND fileira = :fileira AND coluna = :coluna AND horario = :horario";
            Query<salaEntity> query = sessao.createQuery(hql, salaEntity.class);
            query.setParameter("idFilme", idFilme);
            query.setParameter("fileira", fileira);
            query.setParameter("coluna", coluna);
            query.setParameter("horario", horario);

            salaEntity assento = query.uniqueResult();

            if (assento != null && assento.getReservado()) 
            {
                assento.setReservado(false); //Desmarca a reserva
                sessao.update(assento);
                //Atualiza no banco pra Cancelado
                String hqlHistorico = "FROM historicoReservaEntity WHERE idFilme = :idFilme AND fileira = :fileira AND coluna = :coluna AND horario = :horario AND status = 'Reservado'";
                Query<historicoReservaEntity> histQuery = sessao.createQuery(hqlHistorico, historicoReservaEntity.class);
                histQuery.setParameter("idFilme", idFilme);
                histQuery.setParameter("fileira", fileira);
                histQuery.setParameter("coluna", coluna);
                histQuery.setParameter("horario", horario);

                historicoReservaEntity historico = histQuery.uniqueResult();

                if (historico != null) 
                {
                    historico.setStatus("Cancelado");
                    sessao.update(historico);
                }

                atualizarAssentosDisponiveisHibernate(sessao, idFilme); //Atualiza o banco

                System.out.println("Reserva cancelada com sucesso!");
            } else 
            
            {
                System.out.println("Assento não está reservado ou não foi encontrado.");
            }

            tx.commit(); //Confirma
        } catch (Exception e) 
        
        {
            if (tx != null) tx.rollback(); //Desfaz se der erro
            System.out.println("Erro ao cancelar reserva: " + e.getMessage());
        } finally 
        
        {
            sessao.close();
        }
    }
}