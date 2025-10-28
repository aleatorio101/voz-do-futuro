package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.UsuarioDAO;
import model.*;
import app.Main.HttpResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsuarioService {
    private final UsuarioDAO dao;
    private final Gson gson;

    public UsuarioService() {
        this.dao = new UsuarioDAO();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public HttpResponse handleRequest(String method, String path, InputStream requestBody) {
        try {
            if ("POST".equals(method) && path.endsWith("/usuarios/id")) {
                return buscarIdPorEmail(requestBody);
            } else if ("POST".equals(method) && path.endsWith("/usuarios")) {
                return criarUsuario(requestBody);
            } else if ("GET".equals(method)) {
                return listarUsuarios();
            } else {
                return new HttpResponse(405, gson.toJson(new Response("Método não permitido")));
            }
        } catch (Exception e) {
            return new HttpResponse(500, gson.toJson(new Response(e.getMessage())));
        }
    }

    private HttpResponse buscarIdPorEmail(InputStream requestBody) throws Exception {
        String requestBodyStr = new String(requestBody.readAllBytes());
        Map<String, String> body = gson.fromJson(requestBodyStr, Map.class);
        String email = body.get("email");

        Integer id = dao.buscarIdPorEmail(email);
        if (id != null) {
            return new HttpResponse(200, gson.toJson(Map.of("id", id)));
        } else {
            return new HttpResponse(404, gson.toJson(Map.of("erro", "Usuário não encontrado")));
        }
    }

    private HttpResponse criarUsuario(InputStream requestBody) throws Exception {
        String requestBodyStr = new String(requestBody.readAllBytes());
        UsuarioDTO usuarioDTO = gson.fromJson(requestBodyStr, UsuarioDTO.class);

        // Validação básica
        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().trim().isEmpty()) {
            return new HttpResponse(400, gson.toJson(new Response("Nome é obrigatório")));
        }
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            return new HttpResponse(400, gson.toJson(new Response("Email é obrigatório")));
        }
        if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().trim().isEmpty()) {
            return new HttpResponse(400, gson.toJson(new Response("Senha é obrigatória")));
        }

        Usuario novoUsuario = new Usuario(
                usuarioDTO.getNome(),
                usuarioDTO.getEmail(),
                usuarioDTO.getSenha(),
                usuarioDTO.getTipo()
        );

        dao.inserir(novoUsuario);
        return new HttpResponse(201, gson.toJson(new Response("Usuário criado com sucesso")));
    }

    private HttpResponse listarUsuarios() throws Exception {
        List<Usuario> usuarios = dao.listarTodos();
        List<UsuarioResponseDTO> responseList = usuarios.stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
        return new HttpResponse(200, gson.toJson(responseList));
    }

    private static class Response {
        private final String mensagem;

        public Response(String mensagem) {
            this.mensagem = mensagem;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}