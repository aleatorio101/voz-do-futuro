package app;
import dao.UsuarioDAO;
import dao.PropostaDAO;
import model.Usuario;
import model.Proposta;

public class Main {
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PropostaDAO propostaDAO = new PropostaDAO();


        Usuario u1 = new Usuario("Ana Silva", "ana@escola.com", "1234", "ALUNO");
        usuarioDAO.salvar(u1);


        Proposta p1 = new Proposta("Reduzir plástico nas escolas",
                "Proposta para reduzir uso de copos plásticos nas cantinas.", 1);
        propostaDAO.salvar(p1);


        System.out.println("=== Usuários ===");
        usuarioDAO.listar().forEach(u -> System.out.println(u.getNome() + " - " + u.getTipo()));

        System.out.println("\n=== Propostas ===");
        propostaDAO.listar().forEach(p -> System.out.println(p.getTitulo() + " - " + p.getStatus()));
    }
}
