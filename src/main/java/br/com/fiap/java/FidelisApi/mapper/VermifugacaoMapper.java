package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.VermifugacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.VermifugacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Vermifugacao;

public final class VermifugacaoMapper {

    private VermifugacaoMapper() {
    }

    public static Vermifugacao toEntity(VermifugacaoRequest request) {
        return Vermifugacao.builder()
                .produto(request.getProduto())
                .dataAplicacao(request.getDataAplicacao())
                .dataProxima(request.getDataProxima())
                .build();
    }

    public static VermifugacaoResponse toResponse(Vermifugacao entity) {
        return VermifugacaoResponse.builder()
                .id(entity.getId())
                .produto(entity.getProduto())
                .dataAplicacao(entity.getDataAplicacao())
                .dataProxima(entity.getDataProxima())
                .petId(entity.getPet() != null ? entity.getPet().getId() : null)
                .veterinarioId(entity.getVeterinario() != null ? entity.getVeterinario().getId() : null)
                .build();
    }
}
