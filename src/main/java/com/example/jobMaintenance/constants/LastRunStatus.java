package com.example.jobMaintenance.constants;

public enum LastRunStatus {
    SUCCESS("success"),
    FAILURE("failure"),
    NOT_DONE("not done");

    private final String displayName;

    LastRunStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static LastRunStatus fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Last run status cannot be null");
        }
        for (LastRunStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid last run status: " + value);
    }
}
