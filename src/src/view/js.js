const API_MAIN = "Main";

// get do lado direito para criar os cards dinamicamente
function carregarPropostas() {
  $.ajax({
    url: `${API_MAIN}?action=listarPropostas`, // verificar a rota e arrumar essa porra
    type: "GET",
    dataType: "json",
    success: function (res) {
      if (!res || res.length === 0) {
        $("#listaPropostas").html(`
            <div class="glass p-4 text-center text-muted">
              Nenhuma proposta ainda. Envie a primeira usando o formulário ao lado. 🎉
            </div>
          `);
        return;
      }

      $("#listaPropostas").empty(); //vazio
      res.forEach(function (p) {
        const card = `
            <div class="glass p-3 p-md-4">
              <h5 class="fw-semibold mb-0">${p.titulo}</h5>
              <p class="mb-1 text-muted">${p.descricao}</p>
              <small>Status: ${p.status}</small>
            </div>`;
        $("#listaPropostas").append(card);
      });
    },
    error: function () {
        $("#listaPropostas").html(`
          <div class="glass p-4 text-center text-muted">
            Nenhuma proposta ainda.<br> Envie a primeira usando o formulário ao lado. 🎉
          </div>
        `);
    },
  });
}
$(document).ready(carregarPropostas);

// post usando ajax do formulário esquerdo
$("#formProposta").on("submit", function (e) {
  e.preventDefault();

  // validação para nao ir sem itens obrigatórios para o ajax
  if (!this.checkValidity()) {
    this.classList.add("was-validated");
    return;
  }

  //Lucas lembra que eu falei que tinha como formatar?
  const proposta = {
    titulo: $("#titulo").val().trim(), // da pra formatar cada um deles assim ou dentro do ajax
    descricao: $("#descricao").val().trim(),
    usuarioId: parseInt($("#usuarioId").val()),
    status: "ENVIADA",
    dataEnvio: new Date().toISOString().slice(0, 19).replace("T", " "),
  };

  $.ajax({
    url: `${API_MAIN}?action=criarProposta`, // verificar onde demonios esta a rota
    type: "POST",
    contentType: "application/json", // ai no caso a gente pode formatar aqui dentro tb legal neh?
    data: JSON.stringify(proposta),
    success: function (res) {
      console.log("Proposta enviada com sucesso:", res); //lembrar de retirar o log dps de testar
      showToastOk();
      $("#formProposta")[0].reset();
      $("#formProposta").removeClass("was-validated");
      carregarPropostas(); // para atualizar ao fazer o post
    },
    error: function (xhr) {
      alert("Erro ao enviar proposta.");
      console.error(xhr.responseText || xhr);
    },
  });
});

function showToastOk() {
  const toastEl = document.getElementById("toastOk");
  const toast = new bootstrap.Toast(toastEl, { delay: 2000 });
  toast.show();
}
