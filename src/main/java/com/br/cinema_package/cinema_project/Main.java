package com.br.cinema_package.cinema_project;

import java.sql.Time;
import java.util.*;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        GerenciaFilme gerenciador = new GerenciaFilme();
        List<Filme> filmes = gerenciador.carregarFilmesDoBanco();

        int opcao;
        int idFilme = -1;
        String fileira = "";
        int coluna = -1;

        do 
        {
            System.out.println("\n======= CINEMA =======");
            System.out.println("1 - Listar filmes");
            System.out.println("2 - Reservar assentos");
            System.out.println("3 - Ver histórico de reservas");
            System.out.println("4 - Cancelar Reserva");
            System.out.println("5 - Buscar Filme");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) 
            {
                case 1:
                    gerenciador.listarFilmes(filmes);
                    break;
                case 2:
                    System.out.print("Digite o ID do filme: ");
                    idFilme = scanner.nextInt();
                    gerenciador.exibirMapaSala(idFilme);

                    List<Horario> horarios = gerenciador.listarHorariosPorFilme(idFilme);
                    if (horarios.isEmpty()) 
                    {
                        System.out.println("Nenhum horário disponível para este filme.");
                        break;
                    }

                    System.out.println("Horários disponíveis:");
                    for (int i = 0; i < horarios.size(); i++) 
                    {
                        System.out.println((i + 1) + " - " + horarios.get(i).getHorario());
                    }

                    System.out.print("Escolha um horário (número): ");
                    int escolhaHorario = scanner.nextInt();
                    if (escolhaHorario < 1 || escolhaHorario > horarios.size()) {
                    	System.out.println("Horário Inválido.");
                    	break;
                    }
                  
                    Time horarioEscolhido = horarios.get(escolhaHorario - 1).getHorario();

                    System.out.print("Escolha a fileira (A-T): ");
                    fileira = scanner.next().toUpperCase();

                    System.out.print("Escolha a coluna (1-10): ");
                    coluna = scanner.nextInt();

                    gerenciador.reservarAssentoEspecifico(idFilme, fileira, coluna, horarioEscolhido);
                    break;
                case 3:
                    gerenciador.verHistorico();
                    break;
                case 4:
                    // Aqui futuramente você pode implementar o cancelamento de reservas
                    System.out.println("Funcionalidade ainda em desenvolvimento.");
                    break;
                case 5:
                    System.out.print("Digite o título do filme: ");
                    String tituloBusca = scanner.nextLine();
                    gerenciador.buscarFilmePorTitulo(tituloBusca);
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        scanner.close();
    }
}