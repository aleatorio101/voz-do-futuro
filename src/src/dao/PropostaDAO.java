package dao;

import java.sql.*;
import java.util.*;
import model.Proposta;
import java.time.LocalDateTime;

public class PropostaDAO {
    public List<Proposta> listarTodas() throws SQLException {
        List<Proposta> propostas = new ArrayList<>();

        String sql = "SELECT id, titulo, descricao, usuario_id, status, data_envio FROM propostas";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proposta p = new Proposta();
                p.setId(rs.getInt("id"));
                p.setTitulo(rs.getString("titulo"));
                p.setDescricao(rs.getString("descricao"));
                p.setUsuarioId(rs.getInt("usuario_id"));
                p.setStatus(rs.getString("status"));
                Timestamp dataEnvio = rs.getTimestamp("data_envio");
                if (dataEnvio != null) {
                    p.setDataEnvio(dataEnvio.toLocalDateTime());
                }
                propostas.add(p);
            }
        }
        return propostas;
    }

    public void inserir(Proposta proposta) throws SQLException {
        String sql = "INSERT INTO propostas (titulo, descricao, usuario_id, status, data_envio) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proposta.getTitulo());
            stmt.setString(2, proposta.getDescricao());
            stmt.setInt(3, proposta.getUsuarioId());
            stmt.setString(4, proposta.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(proposta.getDataEnvio() != null ? proposta.getDataEnvio() : LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }

    public void atualizar(Proposta proposta) throws SQLException {
        String sql = "UPDATE propostas SET titulo = ?, descricao = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proposta.getTitulo());
            stmt.setString(2, proposta.getDescricao());
            stmt.setString(3, proposta.getStatus());
            stmt.setInt(4, proposta.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM propostas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
