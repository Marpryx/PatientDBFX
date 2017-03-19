/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maryna.patientdb.persistence;

import com.maryna.patientdb.beans.MedicationData;
import com.maryna.patientdb.beans.PatientData;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 * Interface contains major CRUD method for working with HOSPITAL database.
 * @author marynaprix
 */
public interface PatientDAO {
    
    public ObservableList<PatientData> findTableAll() throws SQLException;
    public PatientData updatePatientObj(PatientData oldPatient, PatientData newPatient);
    public MedicationData updateMedicationObj(MedicationData oldMedication, MedicationData newMedication);

    public PatientData findById(int key) throws SQLException;
    public PatientData findById(int key, PatientData patient)  throws SQLException;
    public ArrayList<PatientData> findAll() throws SQLException;
    
    public MedicationData findMedicationById(int id) throws SQLException;
    public MedicationData findMedicationById(int id, int patientId) throws SQLException;
    public MedicationData findMedicationById(int id, int patientId, MedicationData medication) throws SQLException;

    //CRUD methods for PatientData
    public int insertPatient(PatientData patient) throws SQLException;
    public int updatePatient(PatientData patient) throws SQLException;
    public int deletePatient(PatientData patient) throws SQLException;    

    //CRUD methods for MedicationData
    public int insertMedicat(MedicationData medication) throws SQLException;
    public int updateMedicat(MedicationData medication) throws SQLException;
    public int deleteMedicat(MedicationData medication) throws SQLException;


}
