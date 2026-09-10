package com.ambulance.dispatch;
public class Ambulance {
    private final String ambulanceId;
    private final AmbulanceType type;
    private final Driver driver;
    private AmbulanceState state;
    public Ambulance(
            String ambulanceId,
            AmbulanceType type,
            Driver driver) {
        this.ambulanceId = ambulanceId;
        this.type = type;
        this.driver = driver;
        this.state = AmbulanceState.AVAILABLE;
    }
    public String getAmbulanceId() {
        return ambulanceId;
    }
    public AmbulanceType getType() {
        return type;
    }
    public Driver getDriver() {
        return driver;
    }
    public AmbulanceState getState() {
        return state;
    }
    public boolean isAvailable() {
        return state == AmbulanceState.AVAILABLE;
    }
    public void setState(AmbulanceState state) {
        this.state = state;
    }
    @Override
    public String toString() {
        return ambulanceId +
                " | Type: " + type +
                " | Driver: " + driver.getName() +
                " | State: " + state;
    }
}