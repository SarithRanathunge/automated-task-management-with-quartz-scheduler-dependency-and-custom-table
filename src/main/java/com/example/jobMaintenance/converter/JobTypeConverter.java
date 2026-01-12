package com.example.jobMaintenance.converter;

import com.example.jobMaintenance.constants.JobType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JobTypeConverter implements AttributeConverter<JobType, String> {
    @Override
    public String convertToDatabaseColumn(JobType jobType) {
        return jobType == null ? null : jobType.getDisplayName();
    }

    @Override
    public JobType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (JobType type : JobType.values()) {
            if (type.getDisplayName().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown job type: " + dbData);
    }
}
