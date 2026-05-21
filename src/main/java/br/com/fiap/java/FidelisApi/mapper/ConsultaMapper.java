package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.dto.response.ConsultaResponse;
import br.com.fiap.java.FidelisApi.entity.Consulta;

public final class ConsultaMapper {

    private ConsultaMapper() {
    }

    public static Consulta toEntity(ConsultaRequest request) {
        return Consulta.builder()
                .dataHora(request.getDataHora())
                .tipo(request.getTipo())
                .diagnostico(request.getDiagnostico())
                .observacoes(request.getObservacoes())
                .dataRetorno(request.getDataRetorno())
                .build();
    }

    public static ConsultaResponse toResponse(Consulta entity) {
        return ConsultaResponse.builder()
                .id(entity.getId())
                .dataHora(entity.getDataHora())
                .tipo(entity.getTipo())
                .diagnostico(entity.getDiagnostico())
                .observacoes(entity.getObservacoes())
                .dataRetorno(entity.getDataRetorno())
                .veterinarioId(entity.getVeterinario() != null ? entity.getVeterinario().getId() : null)
                .veterinarioNome(entity.getVeterinario() != null ? entity.getVeterinario().getNome() : null)
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .petNome(entity.getPet() != null ? entity.getPet().getNome() : null)
                .build();
    }
}
