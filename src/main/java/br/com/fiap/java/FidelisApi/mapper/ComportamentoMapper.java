package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.ComportamentoRequest;
import br.com.fiap.java.FidelisApi.dto.response.ComportamentoResponse;
import br.com.fiap.java.FidelisApi.entity.Comportamento;

public final class ComportamentoMapper {

    private ComportamentoMapper() {
    }

    public static Comportamento toEntity(ComportamentoRequest request) {
        return Comportamento.builder()
                .data(request.getData())
                .descricao(request.getDescricao())
                .build();
    }

    public static ComportamentoResponse toResponse(Comportamento entity) {
        return ComportamentoResponse.builder()
                .id(entity.getId())
                .data(entity.getData())
                .descricao(entity.getDescricao())
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .build();
    }
}
