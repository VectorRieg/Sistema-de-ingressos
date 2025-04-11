package com.br.cinema_package.cinema_project;

import java.sql.Time;
import java.util.*;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        GerenciaFilme gerenciador = new GerenciaFilme();
        GerenciaReserva gerenciaReserva = new GerenciaReserva();
        GerenciaSala gerenciaSala = new GerenciaSala();
        //Carrega os filmes do banco de dados
        List<Filme> filmes = gerenciador.carregarFilmesDoBanco();
        //Variaveis
        int opcao;
        int idFilme = -1;
        String fileira = "";
        int coluna = -1;

        do 
        {
        	//Menu Principal
            System.out.println("\n======= CINEMA =======");
            System.out.println("1 - Listar filmes");
            System.out.println("2 - Reservar assentos");
            System.out.println("3 - Ver histórico de reservas");
            System.out.println("4 - Cancelar Reserva");
            System.out.println("5 - Buscar Filme");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); //Limpa buffer

            switch (opcao) 
            {
                case 1:
                	//Lista os filmes
                    gerenciador.listarFilmes(filmes);
                    break;
                case 2:
                    System.out.print("Digite o ID do filme: ");
                    idFilme = scanner.nextInt();
                    //Exibe o mapa
                    gerenciaSala.exibirMapaSala(idFilme);
                    //Busca os horarios
                    List<Horario> horarios = gerenciador.listarHorariosPorFilme(idFilme);
                    if (horarios.isEmpty()) 
                    {
                        System.out.println("Nenhum horário disponível para este filme.");
                        break;
                    }
                    //Exibe os horarios
                    System.out.println("Horários disponíveis:");
                    for (int i = 0; i < horarios.size(); i++) 
                    {
                        System.out.println((i + 1) + " - " + horarios.get(i).getHorario());
                    }
                    
                    System.out.print("Escolha um horário (número): ");
                    int escolhaHorario = scanner.nextInt();
                    if (escolhaHorario < 1 || escolhaHorario > horarios.size()) 
                    {
                    	System.out.println("Horário Inválido.");
                    	break;
                    }
                    //Horario escolhido
                    Time horarioEscolhido = horarios.get(escolhaHorario - 1).getHorario();
                    //Escolhe a fileira
                    System.out.print("Escolha a fileira (A-T): ");
                    fileira = scanner.next().toUpperCase();
                    if (!fileira.matches("[A-T]")) 
                    {
                        System.out.println("Fileira inválida.");
                        break;
                    }
                    //Escolhe a coluna
                    System.out.print("Escolha a coluna (1-10): ");
                    coluna = scanner.nextInt();
                    if (coluna < 1 || coluna > 10) 
                    {
                        System.out.println("Coluna inválida.");
                        break;
                    }
                    //Busca o ID do titulo
                    String titulo = "";
                    for (Filme f : filmes) 
                    {
                        if (f.getId() == idFilme) 
                        {
                            titulo = f.getTitulo();
                            break;
                        }
                    }
                               
                    gerenciaReserva.reservarAssentoEspecifico(idFilme, titulo, fileira, coluna, horarioEscolhido);
                    break;
                case 3:
                	//Historico de reserva
                    gerenciaReserva.verHistorico();
                    break;
                case 4:
                    System.out.print("Digite o ID do filme: ");
                    idFilme = scanner.nextInt();
                    scanner.nextLine(); //Limpa buffer
                    //Horarios pro cancelamento
                    List<Horario> horariosCancel = gerenciador.listarHorariosPorFilme(idFilme);
                    if (horariosCancel.isEmpty()) 
                    {
                        System.out.println("Nenhum horário disponível para este filme.");
                        break;
                    }

                    System.out.println("Horários disponíveis:");
                    for (int i = 0; i < horariosCancel.size(); i++) 
                    {
                        System.out.println((i + 1) + " - " + horariosCancel.get(i).getHorario());
                    }

                    System.out.print("Escolha um horário (número): ");
                    int escolhaHorarioCancel = scanner.nextInt();
                    if (escolhaHorarioCancel < 1 || escolhaHorarioCancel > horariosCancel.size()) 
                    {
                        System.out.println("Horário inválido.");
                        break;
                    }
                    //Pega o horario cancelado
                    Time horarioCancelado = horariosCancel.get(escolhaHorarioCancel - 1).getHorario();
                    //Pega e confirma a fileira
                    System.out.print("Escolha a fileira (A-T): ");
                    fileira = scanner.next().toUpperCase();
                    if (!fileira.matches("[A-T]")) 
                    {
                        System.out.println("Fileira inválida.");
                        break;
                    }
                    //Pega e confirma a coluna
                    System.out.print("Escolha a coluna (1-10): ");
                    coluna = scanner.nextInt();
                    if (coluna < 1 || coluna > 10) 
                    {
                        System.out.println("Coluna inválida.");
                        break;
                    }

                    gerenciaReserva.cancelarReserva(idFilme, fileira, coluna, horarioCancelado);
                    break;

                case 5:
                	//Busca filme pelo titulo
                    System.out.print("Digite o título do filme: ");
                    String tituloBusca = scanner.nextLine();
                    gerenciador.buscarFilmePorTitulo(tituloBusca);
                    break;
                case 0:
                	//KYS
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);//Final do loop

        scanner.close();
    }
}