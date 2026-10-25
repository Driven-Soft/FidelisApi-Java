(function () {
  var OUTRO = "#outro";

  function iniciarCombo(combo) {
    var select = combo.querySelector("[data-combo-select]");
    var input = combo.querySelector("[data-combo-input]");
    if (!select || !input) {
      return;
    }

    var opcoes = lerOpcoes(select);
    var origem = origemDoFiltro(combo);

    function grupoAtual() {
      if (!origem) {
        return null;
      }
      if (origem.tagName !== "SELECT") {
        return origem.getAttribute("data-grupo");
      }
      if (origem.selectedIndex < 0) {
        return null;
      }
      return origem.options[origem.selectedIndex].getAttribute("data-grupo");
    }

    function opcoesVisiveis() {
      var grupo = grupoAtual();
      return opcoes.filter(function (opcao) {
        return (
          !grupo ||
          grupo === "TODOS" ||
          opcao.grupo === "TODOS" ||
          opcao.grupo === grupo
        );
      });
    }

    function estaVisivel(valor) {
      return opcoesVisiveis().some(function (opcao) {
        return opcao.valor === valor;
      });
    }

    function reconstruirOpcoes() {
      while (select.firstChild) {
        select.removeChild(select.firstChild);
      }
      select.appendChild(novaOpcao("", "Selecione uma opção"));
      opcoesVisiveis().forEach(function (opcao) {
        select.appendChild(novaOpcao(opcao.valor, opcao.valor));
      });
      select.appendChild(novaOpcao(OUTRO, "Outro (digitar)"));
    }

    function mostrarTexto(visivel) {
      input.classList.toggle("d-none", !visivel);
      input.required = visivel;
    }

    function selecionarPeloValor(valor) {
      if (!valor) {
        select.value = "";
        mostrarTexto(false);
      } else if (estaVisivel(valor)) {
        select.value = valor;
        mostrarTexto(false);
      } else {
        select.value = OUTRO;
        mostrarTexto(true);
      }
    }

    select.addEventListener("change", function () {
      if (select.value === OUTRO) {
        input.value = "";
        mostrarTexto(true);
        input.focus();
      } else {
        input.value = select.value;
        mostrarTexto(false);
      }
    });

    if (origem) {
      origem.addEventListener("change", function () {
        var estavaEmOutro = select.value === OUTRO;
        var valor = input.value;

        reconstruirOpcoes();

        if (estavaEmOutro) {
          select.value = OUTRO;
          mostrarTexto(true);
        } else if (estaVisivel(valor)) {
          select.value = valor;
        } else {
          select.value = "";
          input.value = "";
        }
      });
    }

    select.classList.remove("d-none");
    select.required = true;
    reconstruirOpcoes();
    selecionarPeloValor(input.value);
  }

  function lerOpcoes(select) {
    var lista = [];
    Array.prototype.forEach.call(select.options, function (opcao) {
      if (opcao.value !== "" && opcao.value !== OUTRO) {
        lista.push({
          valor: opcao.value,
          grupo: opcao.getAttribute("data-grupo") || "TODOS",
        });
      }
    });
    return lista;
  }

  function origemDoFiltro(combo) {
    var id = combo.getAttribute("data-depende-de");
    return id ? document.getElementById(id) : null;
  }

  function novaOpcao(valor, texto) {
    var opcao = document.createElement("option");
    opcao.value = valor;
    opcao.textContent = texto;
    return opcao;
  }

  function iniciar() {
    var combos = document.querySelectorAll("[data-combo]");
    Array.prototype.forEach.call(combos, iniciarCombo);
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", iniciar);
  } else {
    iniciar();
  }
})();