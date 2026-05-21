package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.PrescricaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.PrescricaoResponse;
import br.com.fiap.java.FidelisApi.entity.Prescricao;

public final class PrescricaoMapper {

    private PrescricaoMapper() {
    }

    public static Prescricao toEntity(PrescricaoRequest request) {
        return Prescricao.builder()
                .dosagem(request.getDosagem())
                .frequencia(request.getFrequencia())
                .duracaoDias(request.getDuracaoDias())
                .observacao(request.getObservacao())
                .build();
    }

    public static PrescricaoResponse toResponse(Prescricao entity) {
        return PrescricaoResponse.builder()
                .id(entity.getId())
                .dosagem(entity.getDosagem())
                .frequencia(entity.getFrequencia())
                .duracaoDias(entity.getDuracaoDias())
                .observacao(entity.getObservacao())
                .consultaId(entity.getConsulta() != null ? entity.getConsulta().getId() : null)
                .build();
    }
}
