package model;

public class PropostaResponseDTO {
    private int id;
    private String titulo;
    private String descricao;
    private int usuarioId;
    private String status;
    private String dataEnvio;

    public PropostaResponseDTO(Proposta proposta) {
        this.id = proposta.getId();
        this.titulo = proposta.getTitulo();
        this.descricao = proposta.getDescricao();
        this.usuarioId = proposta.getUsuarioId();
        this.status = proposta.getStatus();
        this.dataEnvio = proposta.getDataEnvio() != null ? proposta.getDataEnvio().toString() : null;
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

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}