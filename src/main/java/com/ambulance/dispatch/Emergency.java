package com.ambulance.dispatch;
public class Emergency {
    private final String patientId;
    private final EmergencyType emergencyType;
    private final EmergencyPriority priority;
    private final String pickupLocation;
    private final String destinationHospital;
    private final double estimatedDistance;
    private EmergencyStatus status;
    private Ambulance assignedAmbulance;
    private double estimatedArrivalTime;
    public Emergency(
            String patientId,
            EmergencyType emergencyType,
            EmergencyPriority priority,
            String pickupLocation,
            String destinationHospital,
            double estimatedDistance) {

        this.patientId = patientId;
        this.emergencyType = emergencyType;
        this.priority = priority;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;
        this.estimatedDistance = estimatedDistance;
        this.status = EmergencyStatus.WAITING;
    }
    public String getPatientId() {
        return patientId;
    }
    public EmergencyType getEmergencyType() {
        return emergencyType;
    }
    public EmergencyPriority getPriority() {
        return priority;
    }
    public String getPickupLocation() {
        return pickupLocation;
    }
    public String getDestinationHospital() {
        return destinationHospital;
    }
    public double getEstimatedDistance() {
        return estimatedDistance;
    }
    public EmergencyStatus getStatus() {
        return status;
    }
    public Ambulance getAssignedAmbulance() {
        return assignedAmbulance;
    }
    public double getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }
    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }
    public void assignAmbulance(Ambulance ambulance) {
        this.assignedAmbulance = ambulance;
    }
    public void setEstimatedArrivalTime(double estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }
    @Override
    public String toString() {
        return "Patient ID: " + patientId +
                " | Emergency: " + emergencyType +
                " | Priority: " + priority +
                " | Pickup: " + pickupLocation +
                " | Hospital: " + destinationHospital +
                " | Distance: " + estimatedDistance + " km" +
                " | Status: " + status;
    }
}