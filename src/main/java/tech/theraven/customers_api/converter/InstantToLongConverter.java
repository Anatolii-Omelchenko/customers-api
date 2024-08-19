package tech.theraven.customers_api.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;

@Converter(autoApply = true)
public class InstantToLongConverter implements AttributeConverter<Instant, Long> {

    @Override
    public Long convertToDatabaseColumn(Instant instant) {
        return instant != null ? instant.getEpochSecond() : null;
    }

    @Override
    public Instant convertToEntityAttribute(Long dbData) {
        return dbData != null ? Instant.ofEpochSecond(dbData) : null;
    }
}
