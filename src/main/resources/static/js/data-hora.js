(function () {
  var INICIO = 6 * 60;
  var FIM = 22 * 60;
  var PASSO = 30;

  function dois(numero) {
    return (numero < 10 ? "0" : "") + numero;
  }

  function formatarMinutos(minutos) {
    return dois(Math.floor(minutos / 60)) + ":" + dois(minutos % 60);
  }

  function minutosDaHora(hora) {
    var partes = hora.split(":");
    return parseInt(partes[0], 10) * 60 + parseInt(partes[1], 10);
  }

  function dataLocal(momento) {
    return (
      momento.getFullYear() +
      "-" +
      dois(momento.getMonth() + 1) +
      "-" +
      dois(momento.getDate())
    );
  }

  function proximoHorario(momento) {
    var minutos = momento.getHours() * 60 + momento.getMinutes();
    var proximo = Math.ceil(minutos / PASSO) * PASSO;
    return formatarMinutos(Math.min(Math.max(proximo, INICIO), FIM));
  }

  function montarOpcoes(select, horaExtra) {
    var horarios = [];
    for (var minutos = INICIO; minutos <= FIM; minutos += PASSO) {
      horarios.push(formatarMinutos(minutos));
    }
    if (horaExtra && horarios.indexOf(horaExtra) === -1) {
      horarios.push(horaExtra);
      horarios.sort();
    }
    while (select.firstChild) {
      select.removeChild(select.firstChild);
    }
    horarios.forEach(function (horario) {
      var opcao = document.createElement("option");
      opcao.value = horario;
      opcao.textContent = horario;
      select.appendChild(opcao);
    });
  }

  function iniciarBloco(bloco) {
    var data = bloco.querySelector("[data-data-hora-data]");
    var hora = bloco.querySelector("[data-data-hora-hora]");
    var valor = bloco.querySelector("[data-data-hora-valor]");
    if (!data || !hora || !valor) {
      return;
    }

    var exigeFuturo = bloco.getAttribute("data-minimo-hoje") === "true";
    var agora = new Date();
    var hoje = dataLocal(agora);
    var partes = (valor.value || "").split("T");
    var horaInicial = partes[1]
      ? partes[1].substring(0, 5)
      : proximoHorario(agora);

    montarOpcoes(hora, horaInicial);
    hora.value = horaInicial;
    data.value = partes[0] || hoje;
    if (exigeFuturo) {
      data.min = hoje;
    }

    function atualizar() {
      valor.value = data.value ? data.value + "T" + hora.value : "";

      var momento = new Date();
      var noPassado =
        exigeFuturo &&
        data.value === dataLocal(momento) &&
        minutosDaHora(hora.value) <
          momento.getHours() * 60 + momento.getMinutes();
      hora.setCustomValidity(
        noPassado ? "Escolha um horário a partir de agora" : ""
      );
    }

    data.addEventListener("input", atualizar);
    data.addEventListener("change", atualizar);
    hora.addEventListener("change", atualizar);
    atualizar();
  }

  function iniciar() {
    var blocos = document.querySelectorAll("[data-data-hora]");
    Array.prototype.forEach.call(blocos, iniciarBloco);
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", iniciar);
  } else {
    iniciar();
  }
})();