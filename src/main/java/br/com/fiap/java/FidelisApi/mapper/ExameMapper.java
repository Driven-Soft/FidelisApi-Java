package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.ExameRequest;
import br.com.fiap.java.FidelisApi.dto.response.ExameResponse;
import br.com.fiap.java.FidelisApi.entity.Exame;

public final class ExameMapper {

    private ExameMapper() {
    }

    public static Exame toEntity(ExameRequest request) {
        return Exame.builder()
                .tipo(request.getTipo())
                .descricao(request.getDescricao())
                .resultado(request.getResultado())
                .data(request.getData())
                .build();
    }

    public static ExameResponse toResponse(Exame entity) {
        return ExameResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .descricao(entity.getDescricao())
                .resultado(entity.getResultado())
                .data(entity.getData())
                .consultaId(entity.getConsulta() != null ? entity.getConsulta().getId() : null)
                .build();
    }
}
