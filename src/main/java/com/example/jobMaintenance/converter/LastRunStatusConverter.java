package com.example.jobMaintenance.converter;

import com.example.jobMaintenance.constants.JobCurrentStatus;
import com.example.jobMaintenance.constants.LastRunStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LastRunStatusConverter implements AttributeConverter<LastRunStatus, String> {
    @Override
    public String convertToDatabaseColumn(LastRunStatus lastRunStatus) {
        return lastRunStatus == null ? null : lastRunStatus.getDisplayName();
    }

    @Override
    public LastRunStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (LastRunStatus type : LastRunStatus.values()) {
            if (type.getDisplayName().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown last run status: " + dbData);
    }
}
