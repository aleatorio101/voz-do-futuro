package app;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.UsuarioDAO;
import dao.PropostaDAO;
import model.PropostaDTO;
import model.PropostaResponseDTO;
import model.Usuario;
import model.Proposta;

public class Main {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/usuarios", new UsuarioHandler());
        server.createContext("/propostas", new PropostaHandler());

        server.setExecutor(null);
        System.out.println("Servidor rodando em http://localhost:8080/");
        server.start();
    }

    static class UsuarioHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            UsuarioDAO dao = new UsuarioDAO();
            String response = "";
            int status = 200;

            try {
                switch (method) {
                    case "GET":
                        List<Usuario> usuarios = dao.listarTodos();
                        response = gson.toJson(usuarios);
                        break;

                    case "POST":
                        Usuario novo = gson.fromJson(
                            new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8),
                            Usuario.class
                        );
                        dao.inserir(novo);
                        response = gson.toJson(new Response("Usuário cadastrado com sucesso!"));
                        break;

                    default:
                        status = 405;
                        response = gson.toJson(new Response("Método não permitido"));
                        break;
                }
            } catch (Exception e) {
                status = 500;
                response = gson.toJson(new Response(e.getMessage()));
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(status, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class PropostaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            PropostaDAO dao = new PropostaDAO();
            String response = "";
            int status = 200;

            try {
                switch (method) {
                    case "GET":
                        List<Proposta> propostas = dao.listarTodas();
                        List<PropostaResponseDTO> responseList = propostas.stream()
                                .map(PropostaResponseDTO::new)
                                .collect(Collectors.toList());
                        response = gson.toJson(responseList);
                        break;


                    case "POST":

                        PropostaDTO dto = gson.fromJson(
                                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8),
                                PropostaDTO.class
                        );


                        Proposta nova = new Proposta();
                        nova.setTitulo(dto.getTitulo());
                        nova.setDescricao(dto.getDescricao());
                        nova.setUsuarioId(dto.getUsuarioId());


                        nova.setStatus("ENVIADA");
                        nova.setDataEnvio(LocalDateTime.now());


                        dao.inserir(nova);
                        response = gson.toJson(new Response("Proposta cadastrada com sucesso!"));
                        break;

                    default:
                        status = 405;
                        response = gson.toJson(new Response("Método não permitido"));
                        break;
                }
            } catch (Exception e) {
                status = 500;
                response = gson.toJson(new Response(e.getMessage()));
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(status, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }



    private static class Response {
        private final String mensagem;
        
        public Response(String mensagem) {
            this.mensagem = mensagem;
        }
    }
}