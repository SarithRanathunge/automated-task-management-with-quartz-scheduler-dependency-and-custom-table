package com.example.jobMaintenance.constants;

public enum JobCurrentStatus {
    PENDING("pending"),
    STOPPED("stopped"),
    COMPLETED("completed");

    private final String displayName;

    JobCurrentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static JobCurrentStatus fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Job current status cannot be null");
        }
        for (JobCurrentStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid job current status: " + value);
    }
}
