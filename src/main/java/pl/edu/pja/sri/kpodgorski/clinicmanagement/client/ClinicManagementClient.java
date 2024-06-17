package pl.edu.pja.sri.kpodgorski.clinicmanagement.client;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import pl.edu.pja.sri.kpodgorski.clinic_management.*;

public class ClinicManagementClient {

    private static Management management;

    public static void main(String[] args) {
        try {
            // Inicjalizacja ORB
            ORB orb = ORB.init(args, null);

            // Odczyt referencji z usługi nazwowej
            org.omg.CORBA.Object objRef = readRefFromNamingService(orb, "Management");

            // Rzutowanie obiektu CORBA na obiekt Java (używamy interfejsu Management)
            management = ManagementHelper.narrow(objRef);

            // Testowanie metod
            registerPatient("1", "Andrzej", "Kowalski", "1999-05-06", "fever");
            updatePatient("1", "Andrzej", "Kowalski", "1999-05-06", "Updated medical history");
            getPatient("1");
            scheduleAppointment("1", "1", "2024-06-19", "14:30", "Dr. Nowak");
            cancelAppointment("1");
            issuePrescription("1", new String[]{"Gripex MAX", "Theraflu"});
            deletePatient("1");
        } catch (PatientNotFound | AppointmentConflict e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Metoda pomocnicza umożliwiająca odczyt referencji do obiektu CORBA z usługi nazwowej
     */

    private static org.omg.CORBA.Object readRefFromNamingService(ORB orb, String refName) throws Exception {
        // Uzyskanie referencji do usługi nazwowej
        org.omg.CORBA.Object o = orb.resolve_initial_references("NameService");

        // Rzutowanie obiektu CORBA (usługi nazwowej) na obiekt Java
        NamingContextExt rootContext = NamingContextExtHelper.narrow( o );

        // Tworzenie komponentu nazwowego
        NameComponent nc = new NameComponent(refName, "");

        // Tworzenie ścieżki nazwowej
        NameComponent[] path = {nc};

        // Pobranie obiektu z usługi nazwowej
        return rootContext.resolve(path);
    }

    private static void registerPatient(String id, String firstName, String lastName, String dob, String history) throws PatientNotFound {
        Patient newPatient = new Patient(id, firstName, lastName, dob, history);
        management.registerPatient(newPatient);
        System.out.printf("Patient %s %s registered successfully.\n", firstName, lastName);
    }

    private static void updatePatient(String id, String firstName, String lastName, String dob, String medicalHistory) throws PatientNotFound {
        Patient updatedPatient = new Patient(id, firstName, lastName, dob, medicalHistory);
        management.updatePatient(updatedPatient);
        System.out.printf("Patient %s's medical history updated successfully.\n", updatedPatient.firstName);
    }

    private static void getPatient(String id) throws PatientNotFound {
        Patient patient = management.getPatient(id);
        System.out.printf("Retrieved patient: %s %s, DOB: %s, History: %s\n",
                patient.firstName, patient.lastName, patient.dateOfBirth, patient.medicalHistory);
    }

    private static void deletePatient(String id) throws PatientNotFound {
        management.deletePatient(id);
        System.out.printf("Patient with ID %s deleted successfully.\n", id);
    }

    private static void scheduleAppointment(String id, String patientId, String date, String time, String doctor) throws AppointmentConflict {
        Appointment newAppointment = new Appointment(id, patientId, date, time, doctor);
        management.scheduleAppointment(newAppointment);
        System.out.printf("Appointment for patient ID %s with %s scheduled successfully on %s at %s.\n", patientId, doctor, date, time);
    }

    private static void cancelAppointment(String id) throws AppointmentNotFound {
        management.cancelAppointment(id);
        System.out.printf("Appointment ID %s canceled successfully.\n", id);
    }

    private static void issuePrescription(String patientId, String[] medications) throws PatientNotFound {
        Prescription newPrescription = new Prescription("1", patientId, medications);
        management.issuePrescription(newPrescription);
        System.out.printf("Prescription for patient ID %s issued successfully.\n", patientId);
    }

}
