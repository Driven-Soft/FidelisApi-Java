package br.com.fiap.java.FidelisApi.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PetStatusConverter implements AttributeConverter<PetStatus, String> {

    @Override
    public String convertToDatabaseColumn(PetStatus attribute) {
        return attribute == null ? null : String.valueOf(attribute.getCode());
    }

    @Override
    public PetStatus convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isBlank() ? null : PetStatus.fromCode(dbData.charAt(0));
    }
}
