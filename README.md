# 🎟️ Sistema de Ingressos para Cinema

Este é um sistema backend para reserva de ingressos de filmes, desenvolvido em Java. Ele permite que usuários visualizem filmes disponíveis, façam reservas, consultem histórico e que administradores gerenciem os filmes no sistema.

## 🚀 Funcionalidades

- 📽️ Listar filmes disponíveis
- 🔍 Filtrar filmes por título, data, local, gênero e ID
- 📄 Ver detalhes de um filme
- 🪑 Reservar ingressos informando quantidade e forma de pagamento
- 🕓 Consultar histórico de reservas
- 🛠️ Adicionar, remover e atualizar filmes (função de administrador)

## 🧰 Tecnologias Utilizadas

- Java
 
- Spring Data JPA
- PostgreSQL
- Postman

## 🔌 Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET    | `/movie` | Lista todos os filmes |
| GET    | `/movie/{id}` | Detalhes de um filme |
| GET    | `/movie?genre=Drama` | Filmes por gênero |
| GET    | `/movie?date=2024-05-10` | Filmes por data |
| GET    | `/movie?title=Whiplash` | Filmes por título |
| GET    | `/movie?location=Cineplexx` | Filmes por local |
| GET    | `/movie/booking/history` | Histórico de reservas |
| POST   | `/movie` | Cadastrar novo filme |
| POST   | `/movie/booking/{movieId}/{tickets}/{payment}` | Reservar ingressos |
| PUT    | `/movie/{movieId}` | Atualizar filme |
| DELETE | `/movie/{movieId}` | Remover filme |

## 👥 Desenvolvedores

- [VectorRieg](https://github.com/VectorRieg)  
- [luanaalbuuquerque4](https://github.com/luanaalbuuquerque4)

---

🧠 Projeto desenvolvido com fins acadêmicos, demonstrando uso de Java + PostgreSQL.
