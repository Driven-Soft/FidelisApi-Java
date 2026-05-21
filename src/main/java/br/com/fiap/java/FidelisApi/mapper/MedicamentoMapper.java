package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.MedicamentoRequest;
import br.com.fiap.java.FidelisApi.dto.response.MedicamentoResponse;
import br.com.fiap.java.FidelisApi.entity.Medicamento;

public final class MedicamentoMapper {

    private MedicamentoMapper() {
    }

    public static Medicamento toEntity(MedicamentoRequest request) {
        return Medicamento.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .build();
    }

    public static MedicamentoResponse toResponse(Medicamento entity) {
        return MedicamentoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .prescricaoId(entity.getPrescricao() != null ? entity.getPrescricao().getId() : null)
                .build();
    }
}
