package com.ambulance.dispatch;
public class Driver {
    private final String driverId;
    private final String name;
    private final String phoneNumber;
    public Driver(String driverId, String name, String phoneNumber) {
        this.driverId = driverId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    public String getDriverId() {
        return driverId;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    @Override
    public String toString() {
        return name + " (" + driverId + ")";
    }
}