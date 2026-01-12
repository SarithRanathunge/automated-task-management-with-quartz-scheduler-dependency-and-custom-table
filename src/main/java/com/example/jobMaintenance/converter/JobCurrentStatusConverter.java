package com.example.jobMaintenance.converter;

import com.example.jobMaintenance.constants.JobCurrentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JobCurrentStatusConverter implements AttributeConverter<JobCurrentStatus, String> {
    @Override
    public String convertToDatabaseColumn(JobCurrentStatus jobCurrentStatus) {
        return jobCurrentStatus == null ? null : jobCurrentStatus.getDisplayName();
    }

    @Override
    public JobCurrentStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (JobCurrentStatus type : JobCurrentStatus.values()) {
            if (type.getDisplayName().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown job current status type: " + dbData);
    }
}
