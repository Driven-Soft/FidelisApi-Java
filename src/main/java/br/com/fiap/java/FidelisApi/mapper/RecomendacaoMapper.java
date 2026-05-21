package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.RecomendacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.RecomendacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;

public final class RecomendacaoMapper {

    private RecomendacaoMapper() {
    }

    public static Recomendacao toEntity(RecomendacaoRequest request) {
        return Recomendacao.builder()
                .tipo(request.getTipo())
                .descricao(request.getDescricao())
                .dataRecomendacao(request.getDataRecomendacao())
                .build();
    }

    public static RecomendacaoResponse toResponse(Recomendacao entity) {
        return RecomendacaoResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .descricao(entity.getDescricao())
                .dataRecomendacao(entity.getDataRecomendacao())
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .build();
    }
}
