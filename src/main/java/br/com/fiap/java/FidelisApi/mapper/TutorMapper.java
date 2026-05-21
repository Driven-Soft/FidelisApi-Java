package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.TutorRequest;
import br.com.fiap.java.FidelisApi.dto.response.TutorResponse;
import br.com.fiap.java.FidelisApi.entity.Tutor;

public final class TutorMapper {

    private TutorMapper() {
    }

    public static Tutor toEntity(TutorRequest request) {
        return Tutor.builder()
                .cpf(request.getCpf())
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .telefone(request.getTelefone())
                .endereco(request.getEndereco())
                .dataCriacao(request.getDataCriacao())
                .build();
    }

    public static TutorResponse toResponse(Tutor entity) {
        return TutorResponse.builder()
                .id(entity.getId())
                .cpf(entity.getCpf())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .telefone(entity.getTelefone())
                .endereco(entity.getEndereco())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }
}
