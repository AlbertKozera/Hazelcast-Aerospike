package dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dateOfTheVisit;
    private Patient patient;

    public Appointment(Date dateOfTheVisit, Patient patient) {
        this.dateOfTheVisit = dateOfTheVisit;
        this.patient = patient;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        return "{" + formatter.format(dateOfTheVisit) + ", pacjent[" + patient.getName() + ", " + patient.getLastName() + ", " + patient.getBirthYear() + ", " + patient.getPhoneNumber()
                + ", adresZamieszkania(" + patient.getResidentialAddress().getStreet() + ", " + patient.getResidentialAddress().getCity() + ", " + patient.getResidentialAddress().getZipCode() + ")"
                + ", adresZameldowania(" + patient.getRegisteredAddress().getStreet() + ", " + patient.getRegisteredAddress().getCity() + ", " + patient.getRegisteredAddress().getZipCode() + ")]}";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getDateOfTheVisit() {
        return dateOfTheVisit;
    }

    public void setDateOfTheVisit(Date dateOfTheVisit) {
        this.dateOfTheVisit = dateOfTheVisit;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
