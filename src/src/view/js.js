const API_BASE = "http://localhost:8080";
let emailValidado = null;
let mapaUsuarios = {};

// Carregar mapa de usuários
function carregarMapaUsuarios() {
  $.ajax({
    url: `${API_BASE}/usuarios`,
    type: "GET",
    dataType: "json",
    success: function (usuarios) {
      mapaUsuarios = {};
      usuarios.forEach(function (usuario) {
        mapaUsuarios[usuario.id] = usuario.email;
      });
      carregarPropostas();
    },
    error: function (xhr) {
      console.error("Erro ao carregar usuários:", xhr);
      carregarPropostas();
    },
  });
}

// get do lado direito para criar os cards dinamicamente
function carregarPropostas() {
  $.ajax({
    url: `${API_BASE}/propostas`,
    type: "GET",
    dataType: "json",
    success: function (res) {
      if (!res || res.length === 0) {
        $("#listaPropostas").html(`
            <div class="glass p-4 text-center text-muted">
              Nenhuma proposta ainda. Envie a primeira usando o formulário ao lado.
            </div>
          `);
        return;
      }

      $("#listaPropostas").empty(); 
      res.forEach(function (p) {
        const card = `
            <div class="glass p-3 p-md-4 position-relative" id="proposta-${p.id}" data-usuario-id="${p.usuarioId}">
              <button class="btn btn-link p-0 editar-proposta position-absolute top-0 end-0 m-2" data-id="${p.id}" style="text-decoration: none; cursor: pointer;">
                ✏️
              </button>
              <h5 class="fw-semibold mb-0 titulo-proposta">${p.titulo}</h5>
              <p class="mb-1 text-muted descricao-proposta">${p.descricao}</p>
              <small class="status-proposta" data-status="${p.status}">Status: ${p.status}</small>
            </div>`;
        $("#listaPropostas").append(card);
      });

      // Event listeners para edição
      $(".editar-proposta").on("click", function() {
        const propostaId = $(this).data("id");
        entrarModoEdicao(propostaId);
      });
    },
    error: function () {
        $("#listaPropostas").html(`
          <div class="glass p-4 text-center text-muted">
            Nenhuma proposta ainda.<br> Envie a primeira usando o formulário ao lado. 
          </div>
        `);
    },
  });
}

// Entrar no modo de edição
function entrarModoEdicao(propostaId) {
  const card = $(`#proposta-${propostaId}`);
  const titulo = card.find(".titulo-proposta").text();
  const descricao = card.find(".descricao-proposta").text();
  const status = card.find(".status-proposta").data("status");

  card.html(`
    <div class="mb-2">
      <label class="small-label">Título</label>
      <input type="text" class="form-control titulo-edit" value="${titulo}">
    </div>
    <div class="mb-2">
      <label class="small-label">Descrição</label>
      <textarea class="form-control descricao-edit" rows="2">${descricao}</textarea>
    </div>
    <div class="mb-2">
      <label class="small-label">Status</label>
      <select class="form-select status-edit">
        <option value="ENVIADA" ${status === "ENVIADA" ? "selected" : ""}>ENVIADA</option>
        <option value="APROVADA" ${status === "APROVADA" ? "selected" : ""}>APROVADA</option>
        <option value="REJEITADA" ${status === "REJEITADA" ? "selected" : ""}>REJEITADA</option>
      </select>
    </div>
    <div class="d-flex gap-2">
      <button class="btn btn-sm btn-success salvar-proposta" data-id="${propostaId}">Salvar</button>
      <button class="btn btn-sm btn-secondary cancelar-edicao" data-id="${propostaId}">Cancelar</button>
    </div>
  `);

  $(".salvar-proposta").on("click", function() {
    const id = $(this).data("id");
    salvarProposta(id);
  });

  $(".cancelar-edicao").on("click", function() {
    carregarPropostas();
  });
}

function salvarProposta(propostaId) {
  const card = $(`#proposta-${propostaId}`);
  const proposta = {
    titulo: card.find(".titulo-edit").val().trim(),
    descricao: card.find(".descricao-edit").val().trim(),
    status: card.find(".status-edit").val(),
  };

  $.ajax({
    url: `${API_BASE}/propostas/${propostaId}`,
    type: "PUT",
    contentType: "application/json",
    data: JSON.stringify(proposta),
    success: function (res) {
      showToastOk();
      carregarPropostas();
    },
    error: function (xhr) {
      alert("Erro ao salvar proposta.");
      console.error(xhr.responseText || xhr);
    },
  });
}


$(document).ready(function() {
  carregarMapaUsuarios();
});

// post usando ajax do formulário esquerdo
$("#formProposta").on("submit", function (e) {
  e.preventDefault();
  if (!this.checkValidity()) {
    this.classList.add("was-validated");
    return;
  }

  const dados = {
    nome: $("#nome").val().trim(),
    email: $("#email").val().trim(),
    titulo: $("#titulo").val().trim(),
    descricao: $("#descricao").val().trim(),
  };

  const usuario = {
    nome: dados.nome,
    email: dados.email,
    senha: "padrao123", 
    tipo: "CIDADAO", 
  };

  $.ajax({
    url: `${API_BASE}/usuarios`,
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(usuario),
    success: function (resUsuario) {
      // Buscar o ID do usuário criado pelo email
      $.ajax({
        url: `${API_BASE}/usuarios/id`,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ email: dados.email }),
        success: function (resId) {
          const usuarioId = resId.id;

          const proposta = {
            titulo: dados.titulo,
            descricao: dados.descricao,
            usuarioId: usuarioId,
          };

          $.ajax({
            url: `${API_BASE}/propostas`,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(proposta),
            success: function (resProposta) {
              showToastOk();
              $("#formProposta")[0].reset();
              $("#formProposta").removeClass("was-validated");
              carregarPropostas(); 
            },
            error: function (xhr) {
              alert("Erro ao enviar proposta.");
              console.error(xhr.responseText || xhr);
            },
          });
        },
        error: function (xhr) {
          alert("Erro ao buscar ID do usuário.");
          console.error(xhr.responseText || xhr);
        },
      });
    },
    error: function (xhr) {
      alert("Erro ao criar usuário.");
      console.error(xhr.responseText || xhr);
    },
  });
});

function showToastOk() {
  const toastEl = document.getElementById("toastOk");
  const toast = new bootstrap.Toast(toastEl, { delay: 2000 });
  toast.show();
}
