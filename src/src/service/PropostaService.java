package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PropostaDAO;
import model.*;
import app.Main.HttpResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PropostaService {
    private final PropostaDAO dao;
    private final Gson gson;

    public PropostaService() {
        this.dao = new PropostaDAO();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public HttpResponse handleRequest(String method, String path, InputStream requestBody) {
        try {
            switch (method) {
                case "GET":
                    return listarPropostas();
                case "POST":
                    return criarProposta(requestBody);
                case "PUT":
                    return atualizarProposta(path, requestBody);
                case "DELETE":
                    return deletarProposta(path);
                default:
                    return new HttpResponse(405, gson.toJson(new Response("Método não permitido")));
            }
        } catch (Exception e) {
            return new HttpResponse(500, gson.toJson(new Response(e.getMessage())));
        }
    }

    private HttpResponse listarPropostas() throws Exception {
        List<Proposta> propostas = dao.listarTodas();
        List<PropostaResponseDTO> responseList = propostas.stream()
                .map(PropostaResponseDTO::new)
                .collect(Collectors.toList());
        return new HttpResponse(200, gson.toJson(responseList));
    }

    private HttpResponse criarProposta(InputStream requestBody) throws Exception {
        PropostaDTO dto = gson.fromJson(
                new InputStreamReader(requestBody, StandardCharsets.UTF_8),
                PropostaDTO.class
        );

        Proposta nova = new Proposta();
        nova.setTitulo(dto.getTitulo());
        nova.setDescricao(dto.getDescricao());
        nova.setUsuarioId(dto.getUsuarioId());
        nova.setStatus("ENVIADA");
        nova.setDataEnvio(LocalDateTime.now());

        dao.inserir(nova);
        return new HttpResponse(201, gson.toJson(new Response("Proposta cadastrada com sucesso!")));
    }

    private HttpResponse atualizarProposta(String path, InputStream requestBody) throws Exception {
        String[] parts = path.split("/");
        if (parts.length < 3) {
            return new HttpResponse(400, gson.toJson(new Response("ID da proposta não informado na URL")));
        }
        
        int id = Integer.parseInt(parts[2]);
        
        PropostaUpdateDTO updateDTO = gson.fromJson(
                new InputStreamReader(requestBody, StandardCharsets.UTF_8),
                PropostaUpdateDTO.class
        );

        Proposta p = new Proposta();
        p.setId(id);
        p.setTitulo(updateDTO.getTitulo());
        p.setDescricao(updateDTO.getDescricao());
        p.setStatus(updateDTO.getStatus());

        dao.atualizar(p);
        return new HttpResponse(200, gson.toJson(new Response("Proposta atualizada com sucesso!")));
    }

    private HttpResponse deletarProposta(String path) throws Exception {
        String[] parts = path.split("/");
        if (parts.length < 3) {
            return new HttpResponse(400, gson.toJson(new Response("ID da proposta não informado na URL")));
        }
        
        int id = Integer.parseInt(parts[2]);
        dao.deletar(id);
        return new HttpResponse(200, gson.toJson(new Response("Proposta removida com sucesso!")));
    }

    private static class Response {
        private final String mensagem;
        
        public Response(String mensagem) {
            this.mensagem = mensagem;
        }
    }
}