package br.com.fiap.java.FidelisApi.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LembreteStatusConverter implements AttributeConverter<LembreteStatus, String> {

    @Override
    public String convertToDatabaseColumn(LembreteStatus attribute) {
        return attribute == null ? null : String.valueOf(attribute.getCode());
    }

    @Override
    public LembreteStatus convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isBlank() ? null : LembreteStatus.fromCode(dbData.charAt(0));
    }
}
