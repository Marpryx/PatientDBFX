package com.maryna.patientdb.beans;

import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author marynapryx
 */
public class MedicationData {

    private IntegerProperty id;
    private IntegerProperty patientId;
    private StringProperty medicat;
    private StringProperty dosage;
    private StringProperty form;
    private ObjectProperty<LocalDateTime> dateOfMedicat;

    /**
     * Default constructor
     */
    public MedicationData() {
        this(-1, 0, "", "", "", LocalDateTime.now());
    }

    /**
     * Non-default Constructor receives arguments
     *
     * @param id
     * @param patientId
     * @param medicat
     * @param dosage
     * @param form
     * @param dateOfMedicat
     */
    public MedicationData(final int id, final int patientId, final String medicat,
            final String dosage, final String form, final LocalDateTime dateOfMedicat) {
        super();
        this.id = new SimpleIntegerProperty(id);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.medicat = new SimpleStringProperty(medicat);
        this.dosage = new SimpleStringProperty(medicat);
        this.form = new SimpleStringProperty(medicat);
        this.dateOfMedicat = new SimpleObjectProperty<>(dateOfMedicat);
    }

    @Override
    public String toString() {
        return "MedicationData{" + "id=" + id.get() + ", patientId=" + patientId.get()
                + ", medicat=" + medicat.get() + ", dosage=" + dosage.get()
                + ", form=" + form.get() + ", dateOfMedicat=" + dateOfMedicat.get() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.patientId);
        hash = 97 * hash + Objects.hashCode(this.medicat);
        hash = 97 * hash + Objects.hashCode(this.dosage);
        hash = 97 * hash + Objects.hashCode(this.form);
        hash = 97 * hash + Objects.hashCode(this.dateOfMedicat);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MedicationData other = (MedicationData) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.patientId, other.patientId)) {
            return false;
        }
        if (!Objects.equals(this.medicat, other.medicat)) {
            return false;
        }
        if (!Objects.equals(this.dosage, other.dosage)) {
            return false;
        }
        if (!Objects.equals(this.form, other.form)) {
            return false;
        }
        if (!Objects.equals(this.dateOfMedicat, other.dateOfMedicat)) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id.get();
    }

    public void setId(final int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getPatientId() {
        return patientId.get();
    }

    public void setPatientId(final int patientId) {
        this.patientId.set(patientId);
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    public String getMedicat() {
        return medicat.get();
    }

    public void setMedicat(final String medicat) {
        this.medicat.set(medicat);
    }

    public StringProperty medicatProperty() {
        return medicat;
    }

    public String getDosage() {
        return dosage.get();
    }

    public void setDosage(final String dosage) {
        this.dosage.set(dosage); 
    }

    public StringProperty dosageProperty() {
        return dosage;
    }

    public String getForm() {
        return form.get();
    }

    public void setForm(final String form) {
        this.form.set(form);
    }

    public StringProperty formProperty() {
        return form;
    }

    public LocalDateTime getDateOfMedicat() {
        return dateOfMedicat.get();
    }

    public void setDateOfMedicat(final LocalDateTime dateOfMedicat) {
        this.dateOfMedicat.set(dateOfMedicat);
    }

    public ObjectProperty<LocalDateTime> dateOfMedProperty() {
        return dateOfMedicat;
    }

}
