(function () {
  var modal = document.getElementById("modalExclusao");
  if (!modal) {
    return;
  }

  var formulario = modal.querySelector("[data-exclusao-form]");
  var mensagem = modal.querySelector("[data-exclusao-mensagem]");

  modal.addEventListener("show.bs.modal", function (evento) {
    var botao = evento.relatedTarget;
    if (!botao) {
      return;
    }
    formulario.setAttribute("action", botao.getAttribute("data-exclusao-acao"));
    mensagem.textContent = botao.getAttribute("data-exclusao-texto");
  });
})();