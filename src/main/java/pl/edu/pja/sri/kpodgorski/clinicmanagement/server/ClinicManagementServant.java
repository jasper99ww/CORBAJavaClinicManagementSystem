package pl.edu.pja.sri.kpodgorski.clinicmanagement.server;

import pl.edu.pja.sri.kpodgorski.clinic_management.*;

import java.util.HashMap;
import java.util.Map;

public class ClinicManagementServant extends ManagementPOA {
    private final Map<String, Patient> patients = new HashMap<>();
    private final Map<String, Appointment> appointments = new HashMap<>();
    private final Map<String, Prescription> prescriptions = new HashMap<>();

    @Override
    public void registerPatient(Patient patientData) throws PatientNotFound {
        if (patients.containsKey(patientData.id)) {
            throw new PatientNotFound("Patient already exists with ID: " + patientData.id);
        }
        patients.put(patientData.id, patientData);
    }

    @Override
    public void updatePatient(Patient patientData) throws PatientNotFound {
        if (!patients.containsKey(patientData.id)) {
            throw new PatientNotFound("No patient found with ID: " + patientData.id);
        }
        patients.put(patientData.id, patientData);
    }

    @Override
    public Patient getPatient(String patientId) throws PatientNotFound {
        if (!patients.containsKey(patientId)) {
            throw new PatientNotFound("No patient found with ID: " + patientId);
        }
        return patients.get(patientId);
    }

    @Override
    public void deletePatient(String patientId) throws PatientNotFound {
        if (!patients.containsKey(patientId)) {
            throw new PatientNotFound("No patient found with ID: " + patientId);
        }
        patients.remove(patientId);
    }

    @Override
    public void scheduleAppointment(Appointment appointmentData) throws AppointmentConflict {
        if (appointments.containsKey(appointmentData.id)) {
            throw new AppointmentConflict("Appointment already exists with ID: " + appointmentData.id);
        }
        appointments.put(appointmentData.id, appointmentData);
    }

    @Override
    public void cancelAppointment(String appointmentId) throws AppointmentNotFound {
        if (!appointments.containsKey(appointmentId)) {
            throw new AppointmentNotFound("No appointment found with ID: " + appointmentId);
        }
        appointments.remove(appointmentId);
    }

    @Override
    public void issuePrescription(Prescription prescriptionData) throws PatientNotFound {
        if (!patients.containsKey(prescriptionData.patientId)) {
            throw new PatientNotFound("No patient found with ID: " + prescriptionData.patientId);
        }
        prescriptions.put(prescriptionData.id, prescriptionData);
    }
}
