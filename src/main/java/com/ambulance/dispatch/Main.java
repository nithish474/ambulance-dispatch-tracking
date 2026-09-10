package com.ambulance.dispatch;
import com.ambulance.dispatch.exceptions.InvalidEmergencyException;
public class Main {
    public static void main(String[] args)
            throws InvalidEmergencyException {
        AmbulanceDispatcher dispatcher =
                new AmbulanceDispatcher();
        Driver driver1 =
                new Driver(
                        "D101",
                        "Arun Kumar",
                        "9876543210");
        Driver driver2 =
                new Driver(
                        "D102",
                        "Rahul Singh",
                        "9876543211");
        Driver driver3 =
                new Driver(
                        "D103",
                        "Vijay Kumar",
                        "9876543212");
        Ambulance ambulance1 =
                new Ambulance(
                        "AMB101",
                        AmbulanceType.BASIC,
                        driver1);
        Ambulance ambulance2 =
                new Ambulance(
                        "AMB102",
                        AmbulanceType.ADVANCED_LIFE_SUPPORT,
                        driver2);
        Ambulance ambulance3 =
                new Ambulance(
                        "AMB103",
                        AmbulanceType.ICU,
                        driver3);
        dispatcher.addAmbulance(ambulance1);
        dispatcher.addAmbulance(ambulance2);
        dispatcher.addAmbulance(ambulance3);
        Emergency emergency1 =
                new Emergency(
                        "P001",
                        EmergencyType.CARDIAC,
                        EmergencyPriority.CRITICAL,
                        "Rasapudipalem",
                        "City Hospital",
                        8.0);
        dispatcher.submitEmergency(emergency1);
        Emergency emergency2 =
                new Emergency(
                        "P002",
                        EmergencyType.ACCIDENT,
                        EmergencyPriority.HIGH,
                        "Maddilapalem",
                        "Apollo Hospital",
                        10.0);
        dispatcher.submitEmergency(emergency2);
        Emergency emergency3 =
                new Emergency(
                        "P003",
                        EmergencyType.FEVER,
                        EmergencyPriority.NORMAL,
                        "Gajuwaka",
                        "Care Hospital",
                        15.0);
        dispatcher.submitEmergency(emergency3);
        System.out.println();
        System.out.println("========== AMBULANCE STATUS ==========");
        for (Ambulance ambulance :
                dispatcher.getAmbulances()) {
            System.out.println(ambulance);
        }
        System.out.println();
        System.out.println(
                "========== STATE UPDATE ==========");
        dispatcher.updateAmbulanceState(
                "AMB103",
                AmbulanceState.EN_ROUTE);
        dispatcher.updateAmbulanceState(
                "AMB103",
                AmbulanceState.PATIENT_PICKED_UP);
        dispatcher.updateAmbulanceState(
                "AMB103",
                AmbulanceState.HOSPITAL_ARRIVED);
        dispatcher.updateAmbulanceState(
                "AMB103",
                AmbulanceState.AVAILABLE);
        System.out.println();
        System.out.println(
                "========== EMERGENCY HISTORY ==========");
        for (Emergency emergency :
                dispatcher.getEmergencyHistory()) {
            System.out.println(emergency);
        }
        System.out.println();
        System.out.println(
                "Waiting Queue: " +
                        dispatcher.getWaitingQueueSize());
        System.out.println();
        System.out.println(
                "========================================");
        System.out.println(
                " Ambulance Dispatch System Completed");
        System.out.println(
                "========================================");
    }
}