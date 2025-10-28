package app;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.PropostaService;
import service.UsuarioService;

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
        private final UsuarioService usuarioService;

        public UsuarioHandler() {
            this.usuarioService = new UsuarioService();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            HttpResponse response = usuarioService.handleRequest(method, path, exchange.getRequestBody());
            
            sendResponse(exchange, response);
        }
    }

    static class PropostaHandler implements HttpHandler {
        private final PropostaService propostaService;

        public PropostaHandler() {
            this.propostaService = new PropostaService();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            HttpResponse response = propostaService.handleRequest(method, path, exchange.getRequestBody());
            
            sendResponse(exchange, response);
        }
    }

    private static void sendResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(response.getStatus(), response.getBody().getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBody().getBytes());
        }
    }

    public static class HttpResponse {
        private final int status;
        private final String body;

        public HttpResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public int getStatus() { return status; }
        public String getBody() { return body; }
    }

    private static class Response {
        private final String mensagem;
        
        public Response(String mensagem) {
            this.mensagem = mensagem;
        }
    }
}