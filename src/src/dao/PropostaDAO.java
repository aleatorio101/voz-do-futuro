package dao;
import model.Proposta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropostaDAO {
    public void salvar(Proposta proposta) {
        String sql = "INSERT INTO propostas (titulo, descricao, usuario_id) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proposta.getTitulo());
            stmt.setString(2, proposta.getDescricao());
            stmt.setInt(3, proposta.getUsuarioId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Proposta> listar() {
        List<Proposta> lista = new ArrayList<>();
        String sql = "SELECT * FROM propostas";
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
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
