package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes;
import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.VacinacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;

public final class VacinacaoMapper {

    private VacinacaoMapper() {
    }

    public static Vacinacao toEntity(VacinacaoRequest request) {
        return Vacinacao.builder()
                .dataAplicacao(request.getDataAplicacao())
                .dataProxima(request.getDataProxima())
                .vacinaAplicada(CatalogoOpcoes.normalizarVacina(request.getVacinaAplicada()))
                .observacao(request.getObservacao())
                .build();
    }

    public static VacinacaoRequest toRequest(Vacinacao entity) {
        VacinacaoRequest request = new VacinacaoRequest();
        request.setDataAplicacao(entity.getDataAplicacao());
        request.setDataProxima(entity.getDataProxima());
        request.setVacinaAplicada(entity.getVacinaAplicada());
        request.setObservacao(entity.getObservacao());
        request.setPetId(entity.getPet() != null ? entity.getPet().getId() : null);
        request.setVeterinarioId(entity.getVeterinario() != null ? entity.getVeterinario().getId() : null);
        return request;
    }

    public static VacinacaoResponse toResponse(Vacinacao entity) {
        return VacinacaoResponse.builder()
                .id(entity.getId())
                .dataAplicacao(entity.getDataAplicacao())
                .dataProxima(entity.getDataProxima())
                .vacinaAplicada(entity.getVacinaAplicada())
                .observacao(entity.getObservacao())
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .veterinarioId(entity.getVeterinario() != null ? entity.getVeterinario().getId() : null)
                .build();
    }
}