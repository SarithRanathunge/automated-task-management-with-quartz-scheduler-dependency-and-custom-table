package com.example.jobMaintenance.constants;

public enum JobType {
    HISTORICAL_MARKET_PRICES_DATA_UPDATE("historical market prices data update");

    private final String displayName;

    JobType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static JobType fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Job type cannot be null");
        }
        for (JobType type : values()) {
            if (type.displayName.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid job type: " + value);
    }
}
