package model;

import java.time.LocalDateTime;

public class Proposta {
    private int id;
    private String titulo;
    private String descricao;
    private int usuarioId;
    private String status;
    private LocalDateTime dataEnvio;

    public Proposta() {
    }

    public Proposta(String titulo, String descricao, int usuarioId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.status = "ENVIADA";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
