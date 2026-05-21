package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.HistoricoPesoRequest;
import br.com.fiap.java.FidelisApi.dto.response.HistoricoPesoResponse;
import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;

public final class HistoricoPesoMapper {

    private HistoricoPesoMapper() {
    }

    public static HistoricoPeso toEntity(HistoricoPesoRequest request) {
        return HistoricoPeso.builder()
                .pesoKg(request.getPesoKg())
                .dataMedicao(request.getDataMedicao())
                .observacao(request.getObservacao())
                .build();
    }

    public static HistoricoPesoResponse toResponse(HistoricoPeso entity) {
        return HistoricoPesoResponse.builder()
                .id(entity.getId())
                .pesoKg(entity.getPesoKg())
                .dataMedicao(entity.getDataMedicao())
                .observacao(entity.getObservacao())
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .build();
    }
}
