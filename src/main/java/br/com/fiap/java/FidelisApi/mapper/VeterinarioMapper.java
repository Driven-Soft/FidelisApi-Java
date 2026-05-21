package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.VeterinarioRequest;
import br.com.fiap.java.FidelisApi.dto.response.VeterinarioResponse;
import br.com.fiap.java.FidelisApi.entity.Veterinario;

public final class VeterinarioMapper {

    private VeterinarioMapper() {
    }

    public static Veterinario toEntity(VeterinarioRequest request) {
        return Veterinario.builder()
                .cmvv(request.getCmvv())
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .especialidade(request.getEspecialidade())
                .dataCriacao(request.getDataCriacao())
                .build();
    }

    public static VeterinarioResponse toResponse(Veterinario entity) {
        return VeterinarioResponse.builder()
                .id(entity.getId())
                .cmvv(entity.getCmvv())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .especialidade(entity.getEspecialidade())
                .dataCriacao(entity.getDataCriacao())
                .clinicaId(entity.getClinica() != null ? entity.getClinica().getId() : null)
                .clinicaNome(entity.getClinica() != null ? entity.getClinica().getNome() : null)
                .build();
    }
}
