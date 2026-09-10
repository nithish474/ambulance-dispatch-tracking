package com.ambulance.dispatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.ambulance.dispatch.exceptions.InvalidEmergencyException;

public class AmbulanceDispatcher {

    private final List<Ambulance> ambulances;
    private final List<Emergency> emergencyHistory;
    private final PriorityQueue<Emergency> waitingQueue;

    public AmbulanceDispatcher() {

        ambulances = new ArrayList<>();
        emergencyHistory = new ArrayList<>();

        waitingQueue = new PriorityQueue<>(
                Comparator.comparing(
                        Emergency::getPriority,
                        Comparator.comparingInt(
                                EmergencyPriority::getLevel))
                        .thenComparingDouble(
                                Emergency::getEstimatedDistance)
        );
    }

    // =========================================================
    // ADD AMBULANCE
    // =========================================================

    public void addAmbulance(Ambulance ambulance) {

        if (ambulance == null) {

            throw new IllegalArgumentException(
                    "Ambulance cannot be null.");
        }

        ambulances.add(ambulance);

        System.out.println(
                "Ambulance added: "
                        + ambulance.getAmbulanceId());
    }

    // =========================================================
    // SUBMIT EMERGENCY
    // =========================================================

    public void submitEmergency(Emergency emergency)
            throws InvalidEmergencyException {

        validateEmergency(emergency);

        emergencyHistory.add(emergency);

        System.out.println();
        System.out.println(
                "Emergency request received for patient: "
                        + emergency.getPatientId());

        Ambulance ambulance =
                findBestAmbulance(emergency);

        if (ambulance != null) {

            assignAmbulance(
                    emergency,
                    ambulance);

        } else {

            waitingQueue.offer(emergency);

            emergency.setStatus(
                    EmergencyStatus.WAITING);

            System.out.println(
                    "No suitable ambulance available.");

            System.out.println(
                    "Emergency added to waiting queue.");
        }
    }

    // =========================================================
    // VALIDATE EMERGENCY
    // =========================================================

    private void validateEmergency(
            Emergency emergency)
            throws InvalidEmergencyException {

        if (emergency == null) {

            throw new InvalidEmergencyException(
                    "Emergency cannot be null.");
        }

        if (emergency.getPatientId() == null ||
                emergency.getPatientId().isBlank()) {

            throw new InvalidEmergencyException(
                    "Patient ID is required.");
        }

        if (emergency.getEmergencyType() == null) {

            throw new InvalidEmergencyException(
                    "Emergency type is required.");
        }

        if (emergency.getPriority() == null) {

            throw new InvalidEmergencyException(
                    "Emergency priority is required.");
        }

        if (emergency.getPickupLocation() == null ||
                emergency.getPickupLocation().isBlank()) {

            throw new InvalidEmergencyException(
                    "Pickup location is required.");
        }

        if (emergency.getDestinationHospital() == null ||
                emergency.getDestinationHospital().isBlank()) {

            throw new InvalidEmergencyException(
                    "Destination hospital is required.");
        }

        if (emergency.getEstimatedDistance() <= 0) {

            throw new InvalidEmergencyException(
                    "Distance must be greater than zero.");
        }
    }

    // =========================================================
    // FIND BEST AMBULANCE
    // =========================================================

    private Ambulance findBestAmbulance(
            Emergency emergency) {

        return ambulances.stream()

                .filter(Ambulance::isAvailable)

                .filter(ambulance ->
                        isSuitableType(
                                ambulance,
                                emergency))

                .min(
                        Comparator
                                .comparingInt(
                                        (Ambulance a) ->
                                                typeDifference(
                                                        a,
                                                        emergency))

                                .thenComparing(
                                        this::ambulanceDistance)
                )

                .orElse(null);
    }

    // =========================================================
    // CHECK AMBULANCE TYPE
    // =========================================================

    private boolean isSuitableType(
            Ambulance ambulance,
            Emergency emergency) {

        // Critical emergencies need ICU or ALS
        if (emergency.getPriority()
                == EmergencyPriority.CRITICAL) {

            return ambulance.getType()
                    == AmbulanceType.ICU
                    ||
                    ambulance.getType()
                    == AmbulanceType.ADVANCED_LIFE_SUPPORT;
        }

        // High emergencies can use ALS, ICU or Basic
        if (emergency.getPriority()
                == EmergencyPriority.HIGH) {

            return ambulance.getType()
                    == AmbulanceType.ADVANCED_LIFE_SUPPORT
                    ||
                    ambulance.getType()
                    == AmbulanceType.ICU
                    ||
                    ambulance.getType()
                    == AmbulanceType.BASIC;
        }

        // Moderate and Normal can use any ambulance
        return true;
    }

    // =========================================================
    // CALCULATE TYPE PRIORITY
    // =========================================================

    private int typeDifference(
            Ambulance ambulance,
            Emergency emergency) {

        // Critical:
        // ICU is preferred over ALS
        if (emergency.getPriority()
                == EmergencyPriority.CRITICAL) {

            if (ambulance.getType()
                    == AmbulanceType.ICU) {

                return 0;
            }

            return 1;
        }

        // High:
        // ALS is preferred, then ICU, then Basic
        if (emergency.getPriority()
                == EmergencyPriority.HIGH) {

            if (ambulance.getType()
                    == AmbulanceType.ADVANCED_LIFE_SUPPORT) {

                return 0;
            }

            if (ambulance.getType()
                    == AmbulanceType.ICU) {

                return 1;
            }

            return 2;
        }

        // Moderate / Normal:
        // Basic is preferred
        if (ambulance.getType()
                == AmbulanceType.BASIC) {

            return 0;
        }

        if (ambulance.getType()
                == AmbulanceType.ADVANCED_LIFE_SUPPORT) {

            return 1;
        }

        return 2;
    }

    // =========================================================
    // AMBULANCE DISTANCE
    // =========================================================

    private double ambulanceDistance(
            Ambulance ambulance) {

        /*
         * Simulated distance values.
         * In a real-world application these
         * could come from GPS / Maps API.
         */

        return switch (ambulance.getAmbulanceId()) {

            case "AMB101" -> 3.0;

            case "AMB102" -> 7.0;

            case "AMB103" -> 12.0;

            case "AMB200" -> 5.0;

            default -> 20.0;
        };
    }

    // =========================================================
    // ASSIGN AMBULANCE
    // =========================================================

    private void assignAmbulance(
            Emergency emergency,
            Ambulance ambulance) {

        ambulance.setState(
                AmbulanceState.DISPATCHED);

        emergency.assignAmbulance(
                ambulance);

        emergency.setStatus(
                EmergencyStatus.DISPATCHED);

        double distance =
                ambulanceDistance(ambulance);

        double averageSpeed = 40.0;

        double arrivalTime =
                (distance / averageSpeed) * 60;

        emergency.setEstimatedArrivalTime(
                arrivalTime);

        System.out.println(
                "Ambulance assigned: "
                        + ambulance.getAmbulanceId());

        System.out.println(
                "Driver: "
                        + ambulance.getDriver().getName());

        System.out.println(
                "Estimated distance: "
                        + distance
                        + " km");

        System.out.println(
                "Estimated arrival time: "
                        + String.format(
                                "%.2f minutes",
                                arrivalTime));
    }

    // =========================================================
    // UPDATE AMBULANCE STATE
    // =========================================================

    public void updateAmbulanceState(
            String ambulanceId,
            AmbulanceState newState) {

        Ambulance ambulance =
                findAmbulance(ambulanceId);

        if (ambulance == null) {

            System.out.println(
                    "Ambulance not found.");

            return;
        }

        if (newState == null) {

            System.out.println(
                    "Invalid ambulance state.");

            return;
        }

        ambulance.setState(newState);

        Emergency emergency =
                findActiveEmergency(ambulance);

        /*
         * Update emergency status only when
         * this ambulance actually has an
         * active emergency.
         */
        if (emergency != null) {

            switch (newState) {

                case EN_ROUTE:

                    emergency.setStatus(
                            EmergencyStatus.EN_ROUTE);

                    break;

                case PATIENT_PICKED_UP:

                    emergency.setStatus(
                            EmergencyStatus.PATIENT_PICKED_UP);

                    break;

                case HOSPITAL_ARRIVED:

                    emergency.setStatus(
                            EmergencyStatus.HOSPITAL_ARRIVED);

                    break;

                case AVAILABLE:

                    /*
                     * Current emergency is completed
                     * when ambulance becomes available.
                     */
                    emergency.setStatus(
                            EmergencyStatus.COMPLETED);

                    break;

                default:

                    break;
            }
        }

        /*
         * IMPORTANT:
         *
         * When an ambulance becomes AVAILABLE,
         * automatically check the waiting queue.
         *
         * This is the fix for the waiting queue.
         */
        if (newState == AmbulanceState.AVAILABLE) {

            processWaitingQueue();
        }

        System.out.println(
                ambulanceId
                        + " state changed to "
                        + newState);
    }

    // =========================================================
    // FIND ACTIVE EMERGENCY
    // =========================================================

    private Emergency findActiveEmergency(
            Ambulance ambulance) {

        return emergencyHistory.stream()

                .filter(e ->
                        e.getAssignedAmbulance()
                                == ambulance)

                .filter(e ->
                        e.getStatus()
                                != EmergencyStatus.COMPLETED)

                .findFirst()

                .orElse(null);
    }

    // =========================================================
    // FIND AMBULANCE
    // =========================================================

    private Ambulance findAmbulance(
            String ambulanceId) {

        if (ambulanceId == null) {

            return null;
        }

        return ambulances.stream()

                .filter(a ->
                        a.getAmbulanceId()
                                .equals(ambulanceId))

                .findFirst()

                .orElse(null);
    }

    // =========================================================
    // PROCESS WAITING QUEUE
    // =========================================================

    private void processWaitingQueue() {

        while (!waitingQueue.isEmpty()) {

            /*
             * Get the highest-priority emergency
             * from the queue.
             */
            Emergency emergency =
                    waitingQueue.peek();

            /*
             * Find the best available ambulance
             * for that emergency.
             */
            Ambulance ambulance =
                    findBestAmbulance(emergency);

            /*
             * No suitable ambulance available yet.
             * Keep the emergency in the queue.
             */
            if (ambulance == null) {

                break;
            }

            /*
             * Remove emergency from waiting queue.
             */
            waitingQueue.poll();

            /*
             * Assign ambulance.
             */
            assignAmbulance(
                    emergency,
                    ambulance);
        }
    }

    // =========================================================
    // GET EMERGENCY HISTORY
    // =========================================================

    public List<Emergency> getEmergencyHistory() {

        return new ArrayList<>(
                emergencyHistory);
    }

    // =========================================================
    // GET WAITING QUEUE SIZE
    // =========================================================

    public int getWaitingQueueSize() {

        return waitingQueue.size();
    }

    // =========================================================
    // GET ALL AMBULANCES
    // =========================================================

    public List<Ambulance> getAmbulances() {

        return new ArrayList<>(
                ambulances);
    }
}
