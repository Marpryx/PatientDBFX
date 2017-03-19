/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maryna.patientdb.persistence;

import com.maryna.patientdb.beans.MedicationData;
import com.maryna.patientdb.beans.PatientData;
import com.maryna.patientdb.util.ConnectionHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author marynaprix
 */
public class PatientDAOImplement implements PatientDAO {

    private final Logger log = Logger.getLogger(PatientDAOImplement.class.getName());

    /**
     * Adds a Patient object as a record to the database.
     *
     * @param patient
     * @return
     * @throws SQLException
     */
    @Override
    public int insertPatient(PatientData patient) throws SQLException {
        int result = 0;

        String createQuery = "INSERT INTO PATIENT (FIRSTNAME, LASTNAME, ADDRESS, PHONE,"
                + " DIAGNOSIS, ADMISSIONDATE) VALUES (?,?,?,?,?,?)";

        // Using Java 1.7 try with resources
        // This ensures that the objects in the parenthesis () will be closed
        // when block ends. In this case the Connection, PreparedStatement and
        // the ResultSet will all be closed.
        try (Connection connection = ConnectionHelper.getConnection();
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                // Using 'Statement.RETURN_GENERATED_KEYS' will return auto generated key - patientId
                PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getAddress());
            statement.setString(4, patient.getPhone());
            statement.setString(5, patient.getDiagnosis());
            statement.setTimestamp(6, Timestamp.valueOf(patient.getAddmissionDate()));

            result = statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys();) {
                if (rs.next()) {
                    patient.setPatientId(rs.getInt(1));
                }
            }
        }
        log.info("# of Patient records inserted : " + result);
        return result;
    }

    /**
     * Creates Patient object from the current record in the passed to the
     * method ResultSet
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private PatientData createPatient(ResultSet resultSet) throws SQLException {

        PatientData patient = new PatientData();
        patient.setPatientId(resultSet.getInt("PATIENTID"));
        patient.setFirstName(resultSet.getString("FIRSTNAME"));
        patient.setLastName(resultSet.getString("LASTNAME"));
        patient.setAddress(resultSet.getString("ADDRESS"));
        patient.setPhone(resultSet.getString("PHONE"));
        patient.setDiagnosis(resultSet.getString("DIAGNOSIS"));
        patient.setAddmissionDate(resultSet.getTimestamp("ADMISSIONDATE").toLocalDateTime());

        log.info("Patient bean was created from ResultSet");
        return patient;
    }

    /**
     * OVERLOADED METHOD updates passed Patient object with data in the passed
     * to the method ResultSet
     *
     * @param ResultSet
     * @param PatientData
     * @return
     * @throws SQLException
     */
    private PatientData createPatient(ResultSet resultSet, PatientData patient) throws SQLException {

        patient.setPatientId(resultSet.getInt("PATIENTID"));
        patient.setFirstName(resultSet.getString("FIRSTNAME"));
        patient.setLastName(resultSet.getString("LASTNAME"));
        patient.setAddress(resultSet.getString("ADDRESS"));
        patient.setPhone(resultSet.getString("PHONE"));
        patient.setDiagnosis(resultSet.getString("DIAGNOSIS"));
        patient.setAddmissionDate(resultSet.getTimestamp("ADMISSIONDATE").toLocalDateTime());

        log.info("Patient bean was updated from ResultSet");

        return patient;
    }

    /**
     * This method will update all the fields of a Patient (Master) record
     * except ID.
     *
     * @param PatientData patient. An object with an existing ID and new data in
     * the fields
     * @return The number of records updated, should be 0 or 1
     * @throws SQLException
     *
     */
    @Override
    public int updatePatient(PatientData patient) throws SQLException {
        int result = 0;
        String updateQuery = "UPDATE PATIENT "
                + "SET FIRSTNAME=?, LASTNAME=?, ADDRESS=?, PHONE=?, DIAGNOSIS=?, ADMISSIONDATE=? "
                + "WHERE PATIENTID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement ps = connection.prepareStatement(updateQuery);) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getAddress());
            ps.setString(4, patient.getPhone());
            ps.setString(5, patient.getDiagnosis());
            ps.setTimestamp(6, Timestamp.valueOf(patient.getAddmissionDate()));
            ps.setInt(7, patient.getPatientId());

            result = ps.executeUpdate();
        }
        log.info("# of Patient records updated : " + result);

        return result;
    }

    /**
     * Updating Old Patient object with new Patient object data for Java FX
     * program
     *
     * @param oldPatient
     * @param newPatient
     * @return
     */
    @Override
    public PatientData updatePatientObj(PatientData oldPatient, PatientData newPatient) {
        //FIRSTNAME, LASTNAME, ADDRESS, PHONE, DIAGNOSIS, ADMISSIONDATE

        oldPatient.setPatientId(newPatient.getPatientId());
        oldPatient.setFirstName(newPatient.getFirstName());
        oldPatient.setLastName(newPatient.getLastName());
        oldPatient.setAddress(newPatient.getAddress());
        oldPatient.setPhone(newPatient.getPhone());
        oldPatient.setDiagnosis(newPatient.getDiagnosis());
        oldPatient.setAddmissionDate(newPatient.getAddmissionDate());

        //removing all objets from ArrayList Surgical/Inpatient/Medication objects
        if (oldPatient.getMedication() != null) {
            oldPatient.getMedication().clear();
        }
        try {

            if (oldPatient.getMedication() != null) {
                findMedication(newPatient.getPatientId(), oldPatient.getMedication());
            }
        } catch (Exception ex) {
            Logger.getLogger(PatientDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return oldPatient;
    }

    @Override
    public PatientData findById(int key) throws SQLException {

        PatientData patient = null;

        String selectQuery = "SELECT PATIENTID, FIRSTNAME, LASTNAME, ADDRESS, PHONE,"
                + " DIAGNOSIS, ADMISSIONDATE "
                + "FROM PATIENT "
                + "WHERE PATIENTID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectQuery);) {
            statement.setInt(1, key);
            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    patient = createPatient(resultSet);

                    findMedication(patient.getPatientId(), patient.getMedication());
                }
            }
        }

        log.info("Found Patient record with ID " + key + "? : " + (patient != null));
        return patient;
    }

    /**
     * OVERLOADED METHOD Retrieving Patient (Master) record by ID and writing
     * data in the provided bean, overwriting its data. This needed for data
     * binding in PatientFXMLController Retrieving ALL associated child (detail)
     * records.
     *
     * @param key
     * @param patient
     * @return PatientData patient
     * @throws java.sql.SQLException
     */
    @Override
    public PatientData findById(int key, PatientData patient) throws SQLException {

        String selectQuery = "SELECT PATIENTID, FIRSTNAME, LASTNAME,"
                + " ADDRESS, PHONE, DIAGNOSIS, ADMISSIONDATE"
                + "FROM PATIENT "
                + "WHERE PATIENTID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);) {
            pStatement.setInt(1, key);

            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = pStatement.executeQuery();) {
                if (resultSet.next()) {
                    createPatient(resultSet, patient);

                    findMedication(patient.getPatientId(), patient.getMedication());
                }
            }
        }

        log.info("Found Patient record with ID " + key + "? : " + (key == patient.getPatientId()));
        return patient;
    }

    /**
     * Retrieving Patient (Master) record(s) by LastName. Also, retrieving ALL
     * associated child (detail) records.
     *
     * @param LastName
     * @return ArrayList<PatientData>
     * @throws java.sql.SQLException
     */
    public ArrayList<PatientData> findByLastName(String LastName) throws SQLException {
        ArrayList<PatientData> patientRows = new ArrayList<>();
        PatientData patient = null;
//FIRSTNAME, LASTNAME, ADDRESS, PHONE, DIAGNOSIS, ADMISSIONDATE
        String selectQuery = "SELECT PATIENTID, FIRSTNAME, "
                + "LASTNAME, ADDRESS, PHONE, DIAGNOSIS, ADMISSIONDATE"
                + "FROM PATIENT "
                + "WHERE LASTNAME = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);) {
            pStatement.setString(1, LastName);
            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = pStatement.executeQuery();) {
                while (resultSet.next()) {
                    patient = createPatient(resultSet);

                    findMedication(patient.getPatientId(), patient.getMedication());

                    patientRows.add(patient);
                }
            }
        }
        log.info("# of Patient records found : " + patientRows.size());
        return patientRows;
    }

    /**
     * Retrieve all the records for the given table and returns the data as an
     * ObservableList of PatientData objects (WITH CHILD DATA) for a JavaFX
     * application
     *
     * @return ObservableList<PatientData>
     * @throws SQLException
     */
    @Override
    public ObservableList<PatientData> findTableAll() throws SQLException {
        ObservableList<PatientData> allPatientData = FXCollections.observableArrayList();
        PatientData patient = new PatientData();

        String selectQuery = "SELECT PATIENTID, FIRSTNAME, LASTNAME, ADDRESS,"
                + " PHONE, DIAGNOSIS, ADMISSIONDATE "
                + "FROM PATIENT";

        try (Connection connection = ConnectionHelper.getConnection();
                // You must use PreparedStatements to guard against SQL Injection
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);
                ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                patient = createPatient(resultSet);
                // adding all child data to the created Patient

                if (patient.getMedication() != null) {
                    findMedication(patient.getPatientId(), patient.getMedication());
                }

                // adding Patient object to the Observable List
                allPatientData.add(patient);

            }
        }
        log.info("Number of Patient records found : " + allPatientData.size());
        return allPatientData;
    }

    /**
     * Retrieves all the records for the given table (PATIENT) (WITHOUT CHILD
     * DATA !!!) and returns the data as an ArrayList of PatientData objects
     *
     * @return ArrayList<PatientData>
     * @throws java.sql.SQLException
     */
    @Override
    public ArrayList<PatientData> findAll() throws SQLException {
        ArrayList<PatientData> allParentRows = new ArrayList<>();

        String selectQuery = "SELECT PATIENTID, FIRSTNAME, LASTNAME, ADDRESS,"
                + " PHONE, DIAGNOSIS, ADMISSIONDATE "
                + "FROM PATIENT";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);
                ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                allParentRows.add(createPatient(resultSet));
            }
        }
        log.info("# of Patient records found : " + allParentRows.size());
        return allParentRows;
    }

    /**
     * Adds a MedicationData object as a record to the database.
     *
     * @param medication
     * @return
     * @throws SQLException
     */
    @Override
    public int insertMedicat(MedicationData medication) throws SQLException {
        int result = 0;

        String createQuery = "INSERT INTO MEDICATION (PATIENTID, MEDICAT, DOSAGE,"
                + " FORM, DATEOFMED) VALUES (?,?,?,?,?)";
        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, medication.getPatientId());
            statement.setString(2, medication.getMedicat());
            statement.setString(3, medication.getDosage());
            statement.setString(4, medication.getForm());
            statement.setTimestamp(5, Timestamp.valueOf(medication.getDateOfMedicat()));

            result = statement.executeUpdate();
            // second try-with-resources for ResultSet object
            try (ResultSet rs = statement.getGeneratedKeys();) {
                if (rs.next()) {
                    medication.setId(rs.getInt(1));
                }
            }
        }
        log.info("# of Medication records inserted : " + result);
        return result;
    }

    /**
     * Creates MedicationData object from the current record in the passed to
     * the method ResultSet
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private MedicationData createMedication(ResultSet resultSet) throws SQLException {

        MedicationData medication = new MedicationData();
        medication.setId(resultSet.getInt("ID"));
        medication.setPatientId(resultSet.getInt("PATIENTID"));
        medication.setMedicat(resultSet.getString("MEDICAT"));
        medication.setDosage(resultSet.getString("DOSAGE"));
        medication.setForm(resultSet.getString("FORM"));
        medication.setDateOfMedicat(resultSet.getTimestamp("DATEOFMED").toLocalDateTime());

        log.info("Medication bean was created from ResultSet");
        return medication;
    }

    /**
     * OVERLOADED METHOD updates passed MedicationData object with data in the
     * passed to the method ResultSet
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private MedicationData createMedication(ResultSet resultSet, MedicationData medication) throws SQLException {

        medication.setId(resultSet.getInt("ID"));
        medication.setPatientId(resultSet.getInt("PATIENTID"));
        medication.setMedicat(resultSet.getString("MEDICAT"));
        medication.setDosage(resultSet.getString("DOSAGE"));
        medication.setForm(resultSet.getString("FORM"));
        medication.setDateOfMedicat(resultSet.getTimestamp("DATEOFMED").toLocalDateTime());

        log.info("Medication bean was created from ResultSet");
        return medication;
    }

    /**
     * Updating old MedicationData object with new Medication object data for
     * Java FX program
     *
     * @param oldMedication
     * @param newMedication
     * @return
     */
    @Override
    public MedicationData updateMedicationObj(MedicationData oldMedication, MedicationData newMedication) {

        oldMedication.setId(newMedication.getId());
        oldMedication.setPatientId(newMedication.getPatientId());
        oldMedication.setMedicat(newMedication.getMedicat());
        oldMedication.setDosage(newMedication.getDosage());
        oldMedication.setForm(newMedication.getForm());
        oldMedication.setDateOfMedicat(newMedication.getDateOfMedicat());

        return oldMedication;
    }

    /**
     * This method will update all the fields of a MedicationData (Detail)
     * record except ID.
     *
     * @param MedicationData medication. An object with an existing ID and new
     * data in the fields
     * @return The number of records updated, should be 0 or 1
     * @throws SQLException
     *
     */
    @Override
    public int updateMedicat(MedicationData medication) throws SQLException {
        int result = 0;
//PATIENTID, MEDICAT, DOSAGE, FORM, DATEOFMED
        String updateQuery = "UPDATE MEDICATION "
                + "SET PATIENTID=?, MEDICAT=?, DOSAGE=?, FORM=?, DATEOFMED=? "
                + "WHERE ID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(updateQuery);) {

            statement.setInt(1, medication.getPatientId());
            statement.setString(3, medication.getMedicat());
            statement.setString(3, medication.getDosage());
            statement.setString(3, medication.getForm());
            statement.setTimestamp(2, Timestamp.valueOf(medication.getDateOfMedicat()));
            statement.setInt(6, medication.getId());

            result = statement.executeUpdate();
        }
        log.info("# of Medication records updated : " + result);
        return result;
    }

    /**
     * This method deletes a single MedicationData record based on the criteria
     * of the primary key field ID value.
     *
     * @param ID The primary key to use to identify the record that must be
     * deleted
     * @return The number of records deleted, should be 0 or 1
     * @throws SQLException
     */
    @Override
    public int deleteMedicat(MedicationData medication) throws SQLException {
        int result = 0;

        String query = "DELETE FROM MEDICATION WHERE ID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);) {
            ps.setInt(1, medication.getId());
            result = ps.executeUpdate();
        }
        log.info("# of MedicationData records deleted : " + result);
        return result;
    }

    /**
     * ============= Retrieving Medication (Detail) record by ID.
     *
     * @param id
     * @return MedicationData medication
     * @throws java.sql.SQLException
     */
    @Override
    public MedicationData findMedicationById(int id) throws SQLException {

        MedicationData medication = null;

        String selectQuery = "SELECT ID, PATIENTID, DATEOFMED, MED, "
                + "UNITCOST, UNITS "
                + "FROM MEDICATION "
                + "WHERE ID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);) {
            pStatement.setInt(1, id);
            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = pStatement.executeQuery();) {
                if (resultSet.next()) {
                    medication = createMedication(resultSet);
                }
            }
        }
        log.info("Found Medication record with ID " + id + "? : " + (medication != null));
        return medication;
    }

    /**
     * ===== OVERLOADED METHOD Retrieving Medication record by ID and Patient ID
     *
     * @param id
     * @param patientId
     * @return
     * @throws SQLException
     */
    @Override
    public MedicationData findMedicationById(int id, int patientId) throws SQLException {

        MedicationData medication = null;

        String selectQuery = "SELECT ID, PATIENTID, MEDICAT, DOSAGE, FORM, DATEOFMED "
                + "FROM MEDICATION "
                + "WHERE ID = ? "
                + "AND PATIENTID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);) {
            pStatement.setInt(1, id);
            pStatement.setInt(2, patientId);
            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = pStatement.executeQuery();) {
                if (resultSet.next()) {
                    medication = createMedication(resultSet);
                }
            }
        }
        log.info("Found Medication record with ID " + id + "? : " + (medication != null));
        return medication;
    }

    /**
     * Method that retrieves all MedicationData objects (db records) for given
     * patientId
     *
     * @param patientId
     * @param medication
     * @return
     * @throws SQLException
     */
    private int findMedication(int patientId, ArrayList<MedicationData> medication) throws SQLException {

        // clearing all objects from medication obj as we are creating new medication data set
        medication.clear();

        String selectQuery = "SELECT ID, PATIENTID, MEDICAT, DOSAGE, FORM, DATEOFMED"
                + "FROM MEDICATION WHERE PATIENTID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(selectQuery);) {
            pStatement.setInt(1, patientId);
            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = pStatement.executeQuery();) {
                while (resultSet.next()) {
                    medication.add(createMedication(resultSet));
                }
            }
        }
        log.info("# of Medication records found : " + medication.size());
        return medication.size();
    }

    @Override
    public MedicationData findMedicationById(int id, int patientId, MedicationData medication) throws SQLException {
        String query = "SELECT ID, PATIENTID, MEDICAT, DOSAGE, FORM, DATEOFMED "
                + "FROM MEDICATION "
                + "WHERE ID = ? "
                + "AND PATIENTID = ?";

        // first try-with-resources to close connections
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(query);) {
            pStatement.setInt(1, id);
            pStatement.setInt(2, patientId);
            // second try-with-resources for ResultSet object
            try (ResultSet resultSet = pStatement.executeQuery();) {
                if (resultSet.next()) {
                    medication = createMedication(resultSet, medication);
                }
            }
        }
        log.info("Found Medication record with ID " + id + "? : " + (medication.getId() == id));
        return medication;
    }

    /**
     * Deletes all MedicationData records related to the Patient (Master)
     *
     * @param patient
     * @return The number of deleted records from Medication table
     * @throws SQLException
     */
    private int deleteAllMedicat(PatientData patient) throws SQLException {
        int result = 0;

        String deleteQuery = "DELETE FROM MEDICATION WHERE PATIENTID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement statemen = connection.prepareStatement(deleteQuery);) {
            statemen.setInt(1, patient.getPatientId());
            result = statemen.executeUpdate();
        }
        log.info("# of MedicationData records deleted : " + result);
        return result;
    }

    @Override
    public int deletePatient(PatientData patient) throws SQLException {
        int result = 0;

        // Deleting all detail records AT FIRST
        deleteAllMedicat(patient);

        String deleteQuery = "DELETE FROM PATIENT WHERE PATIENTID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = ConnectionHelper.getConnection();
                PreparedStatement ps = connection.prepareStatement(deleteQuery);) {
            ps.setInt(1, patient.getPatientId());
            result = ps.executeUpdate();
        }
        log.info("# of Patient (Master) records deleted : " + result);
        return result;
    }

}
