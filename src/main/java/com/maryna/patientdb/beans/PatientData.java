package com.maryna.patientdb.beans;

import java.util.Objects;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author marynapryx
 */
public class PatientData {

    private IntegerProperty patientId;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty address;
    private StringProperty phone;
    private StringProperty diagnosis;
    private ArrayList<MedicationData> medication;
    private ObjectProperty<LocalDateTime> addmissionDate;

    /**
     * Default constructor
     */
    public PatientData() {
        this(-1, "", "", "", "", "", LocalDateTime.now(), new ArrayList<>());
    }

    /**
     * Non-default Constructor that receives arguments
     *
     * @param patientId
     * @param lastName
     * @param firstName
     * @param address
     * @param phone
     * @param diagnosis
     * @param addmissionDate
     */
    public PatientData(int patientId, String firstName, String lastName, String address,
                       String phone, String diagnosis, LocalDateTime addmissionDate, ArrayList<MedicationData> medication) {
        this.patientId = new SimpleIntegerProperty(patientId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.diagnosis = new SimpleStringProperty(diagnosis);
        this.addmissionDate = new SimpleObjectProperty<>(addmissionDate);
        this.medication = medication;

    }

    @Override
    public String toString() {
        return "PatientData{" + "patientId=" + patientId.get() +
                "firstName=" + firstName.get() + "," +
                "lastName=" + lastName.get() + ", " + 
                "address=" + address.get() + ", " +
                "phone=" + phone.get() + ", " +
                "diagnosis=" + diagnosis.get() + ", " + 
                "addmissionDate=" + addmissionDate.get() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.patientId.get());
        hash = 73 * hash + Objects.hashCode(this.firstName.get());
        hash = 73 * hash + Objects.hashCode(this.lastName.get());
        hash = 73 * hash + Objects.hashCode(this.address.get());
        hash = 73 * hash + Objects.hashCode(this.phone.get());
        hash = 73 * hash + Objects.hashCode(this.diagnosis.get());
        hash = 73 * hash + Objects.hashCode(this.addmissionDate.get());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PatientData other = (PatientData) obj;
        if (!Objects.equals(this.patientId.get(), other.patientId.get())) {
            return false;
        }
        if (!Objects.equals(this.firstName.get(), other.firstName.get())) {
            return false;
        }
        if (!Objects.equals(this.lastName.get(), other.lastName.get())) {
            return false;
        }
        if (!Objects.equals(this.address.get(), other.address.get())) {
            return false;
        }
        if (!Objects.equals(this.phone.get(), other.phone.get())) {
            return false;
        }
        if (!Objects.equals(this.diagnosis.get(), other.diagnosis.get())) {
            return false;
        }
        if (!Objects.equals(this.addmissionDate.get(), other.addmissionDate.get())) {
            return false;
        }

        return true;
    }

    /*
     Getters and Setters
     */
    public int getPatientId() {
        return patientId.get();
    }

    public void setPatientId(final int patientId) {
        this.patientId.set(patientId);
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }
    
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(final String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(final String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(final String address) {
        this.address.set(address);
    }

    public StringProperty address() {
        return address;
    }
    
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(final String phone) {
        this.phone.set(phone);
    }

    public StringProperty phone() {
        return phone;
    }

    public String getDiagnosis() {
        return diagnosis.get();
    }

    public void setDiagnosis(final String diagnosis) {
        this.diagnosis.set(diagnosis);
    }

    public StringProperty diagnosisProperty() {
        return diagnosis;
    }

    public LocalDateTime getAddmissionDate() {
        return addmissionDate.get();
    }

    public void setAddmissionDate(final LocalDateTime addmissionDate) {
        this.addmissionDate.set(addmissionDate);
    }
    
    public ObjectProperty<LocalDateTime> addmissionDateProperty(){
        return addmissionDate;
    }
    public ArrayList<MedicationData> getMedication() {
        return medication;
    }

    

}
