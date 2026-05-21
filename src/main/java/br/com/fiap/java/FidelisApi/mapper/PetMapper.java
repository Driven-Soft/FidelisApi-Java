package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.dto.response.PetResponse;
import br.com.fiap.java.FidelisApi.entity.Pet;

public final class PetMapper {

    private PetMapper() {
    }

    public static Pet toEntity(PetRequest request) {
        return Pet.builder()
                .nome(request.getNome())
                .especie(request.getEspecie())
                .raca(request.getRaca())
                .sexo(request.getSexo())
                .dataNascimento(request.getDataNascimento())
                .status(request.getStatus())
                .fotoUrl(request.getFotoUrl())
                .build();
    }

    public static PetResponse toResponse(Pet entity) {
        return PetResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .especie(entity.getEspecie())
                .raca(entity.getRaca())
                .sexo(entity.getSexo())
                .dataNascimento(entity.getDataNascimento())
                .status(entity.getStatus())
                .fotoUrl(entity.getFotoUrl())
                .tutorId(entity.getTutor() != null ? entity.getTutor().getId() : null)
                .tutorNome(entity.getTutor() != null ? entity.getTutor().getNome() : null)
                .clinicaId(entity.getClinica() != null ? entity.getClinica().getId() : null)
                .clinicaNome(entity.getClinica() != null ? entity.getClinica().getNome() : null)
                .build();
    }
}
