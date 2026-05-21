package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.ClinicaRequest;
import br.com.fiap.java.FidelisApi.dto.response.ClinicaResponse;
import br.com.fiap.java.FidelisApi.entity.Clinica;

public final class ClinicaMapper {

    private ClinicaMapper() {
    }

    public static Clinica toEntity(ClinicaRequest request) {
        return Clinica.builder()
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .endereco(request.getEndereco())
                .build();
    }

    public static ClinicaResponse toResponse(Clinica entity) {
        return ClinicaResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cnpj(entity.getCnpj())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .endereco(entity.getEndereco())
                .build();
    }
}
