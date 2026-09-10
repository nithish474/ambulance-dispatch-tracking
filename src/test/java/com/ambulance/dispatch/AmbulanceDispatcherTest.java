package com.ambulance.dispatch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.ambulance.dispatch.exceptions.InvalidEmergencyException;
class AmbulanceDispatcherTest {
    private AmbulanceDispatcher createDispatcher() {
        AmbulanceDispatcher dispatcher =
                new AmbulanceDispatcher();
        Driver driver1 =
                new Driver(
                        "D1",
                        "Driver One",
                        "9000000001");
        Driver driver2 =
                new Driver(
                        "D2",
                        "Driver Two",
                        "9000000002");
        dispatcher.addAmbulance(
                new Ambulance(
                        "AMB101",
                        AmbulanceType.BASIC,
                        driver1));
        dispatcher.addAmbulance(
                new Ambulance(
                        "AMB102",
                        AmbulanceType.ICU,
                        driver2));
        return dispatcher;
    }
    @Test
    void criticalEmergencyShouldGetICUAmbulance()
            throws InvalidEmergencyException {

        AmbulanceDispatcher dispatcher =
                createDispatcher();
        Emergency emergency =
                new Emergency(
                        "P001",
                        EmergencyType.CARDIAC,
                        EmergencyPriority.CRITICAL,
                        "Location A",
                        "Hospital A",
                        5);
        dispatcher.submitEmergency(emergency);
        assertNotNull(
                emergency.getAssignedAmbulance());
        assertEquals(
                AmbulanceType.ICU,
                emergency
                        .getAssignedAmbulance()
                        .getType());
    }
    @Test
    void emergencyShouldBeDispatched()
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                createDispatcher();
        Emergency emergency =
                new Emergency(
                        "P002",
                        EmergencyType.ACCIDENT,
                        EmergencyPriority.HIGH,
                        "Location B",
                        "Hospital B",
                        10);
        dispatcher.submitEmergency(emergency);
        assertEquals(
                EmergencyStatus.DISPATCHED,
                emergency.getStatus());
    }
    @Test
    void ambulanceShouldBecomeUnavailableAfterDispatch()
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                createDispatcher();
        Emergency emergency =
                new Emergency(
                        "P003",
                        EmergencyType.ACCIDENT,
                        EmergencyPriority.HIGH,
                        "Location C",
                        "Hospital C",
                        10);
        dispatcher.submitEmergency(emergency);
        assertFalse(
                emergency
                        .getAssignedAmbulance()
                        .isAvailable());
    }
    @Test
    void emergencyShouldEnterWaitingQueueWhenNoAmbulanceExists()
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                new AmbulanceDispatcher();
        Emergency emergency =
                new Emergency(
                        "P004",
                        EmergencyType.ACCIDENT,
                        EmergencyPriority.HIGH,
                        "Location D",
                        "Hospital D",
                        10);
        dispatcher.submitEmergency(emergency);
        assertEquals(
                EmergencyStatus.WAITING,
                emergency.getStatus());
        assertEquals(
                1,
                dispatcher.getWaitingQueueSize());
    }
    @Test
    void invalidPatientIdShouldThrowException() {
        AmbulanceDispatcher dispatcher =
                createDispatcher();
        Emergency emergency =
                new Emergency(
                        "",
                        EmergencyType.FEVER,
                        EmergencyPriority.NORMAL,
                        "Location",
                        "Hospital",
                        5);
        assertThrows(
                InvalidEmergencyException.class,
                () -> dispatcher.submitEmergency(emergency));
    }
    @Test
    void ambulanceStateShouldUpdate()
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                createDispatcher();
        Emergency emergency =
                new Emergency(
                        "P005",
                        EmergencyType.CARDIAC,
                        EmergencyPriority.CRITICAL,
                        "Location E",
                        "Hospital E",
                        5);
        dispatcher.submitEmergency(emergency);
        dispatcher.updateAmbulanceState(
                "AMB102",
                AmbulanceState.EN_ROUTE);
        assertEquals(
                EmergencyStatus.EN_ROUTE,
                emergency.getStatus());
        dispatcher.updateAmbulanceState(
                "AMB102",
                AmbulanceState.PATIENT_PICKED_UP);
        assertEquals(
                EmergencyStatus.PATIENT_PICKED_UP,
                emergency.getStatus());
        dispatcher.updateAmbulanceState(
                "AMB102",
                AmbulanceState.HOSPITAL_ARRIVED);
        assertEquals(
                EmergencyStatus.HOSPITAL_ARRIVED,
                emergency.getStatus());
    }
    @Test
    void emergencyHistoryShouldBeMaintained()
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                createDispatcher();
        Emergency emergency =
                new Emergency(
                        "P006",
                        EmergencyType.FEVER,
                        EmergencyPriority.NORMAL,
                        "Location F",
                        "Hospital F",
                        7);
        dispatcher.submitEmergency(emergency);
        assertEquals(
                1,
                dispatcher.getEmergencyHistory().size());
    }
    @Test
    void waitingEmergencyShouldBeAutomaticallyAllocated()
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                new AmbulanceDispatcher();
        Driver driver =
                new Driver(
                        "D10",
                        "Driver Ten",
                        "9111111111");
        Ambulance ambulance =
                new Ambulance(
                        "AMB200",
                        AmbulanceType.BASIC,
                        driver);
        Emergency emergency =
                new Emergency(
                        "P007",
                        EmergencyType.FEVER,
                        EmergencyPriority.NORMAL,
                        "Location G",
                        "Hospital G",
                        6);
        dispatcher.submitEmergency(emergency);
        assertEquals(
                1,
                dispatcher.getWaitingQueueSize());
        dispatcher.addAmbulance(ambulance);
        dispatcher.updateAmbulanceState(
                "AMB200",
                AmbulanceState.AVAILABLE);
        assertEquals(
                EmergencyStatus.DISPATCHED,
                emergency.getStatus());
    }
}