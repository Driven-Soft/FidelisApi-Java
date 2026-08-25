package br.com.fiap.java.FidelisApi.common;

import br.com.fiap.java.FidelisApi.exception.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable build(int page, int size, String sort, String direction) {
        if (page < 0) {
            throw new BusinessException("O parâmetro 'page' deve ser maior ou igual a zero.");
        }
        if (size <= 0) {
            throw new BusinessException("O parâmetro 'size' deve ser maior que zero.");
        }

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(
                    "Direção de ordenação inválida: '" + direction + "'. Valores aceitos: ASC ou DESC.");
        }

        return PageRequest.of(page, size, sortDirection, sort);
    }
}
