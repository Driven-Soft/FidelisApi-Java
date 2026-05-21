package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.LembreteRequest;
import br.com.fiap.java.FidelisApi.dto.response.LembreteResponse;
import br.com.fiap.java.FidelisApi.entity.Lembrete;

public final class LembreteMapper {

    private LembreteMapper() {
    }

    public static Lembrete toEntity(LembreteRequest request) {
        return Lembrete.builder()
                .tipo(request.getTipo())
                .descricao(request.getDescricao())
                .dataPrevista(request.getDataPrevista())
                .status(request.getStatus())
                .build();
    }

    public static LembreteResponse toResponse(Lembrete entity) {
        return LembreteResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .descricao(entity.getDescricao())
                .dataPrevista(entity.getDataPrevista())
                .status(entity.getStatus())
                .tutorId(entity.getTutor() != null ? entity.getTutor().getId() : null)
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .build();
    }
}
