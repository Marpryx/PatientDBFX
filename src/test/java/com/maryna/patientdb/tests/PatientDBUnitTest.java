/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maryna.patientdb.tests;

import com.maryna.patientdb.beans.MedicationData;
import com.maryna.patientdb.beans.PatientData;
import com.maryna.patientdb.persistence.PatientDAO;
import com.maryna.patientdb.persistence.PatientDAOImplement;
import com.maryna.patientdb.util.ConnectionHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author marynaprix
 */
public class PatientDBUnitTest {

private final Logger log = Logger.getLogger(PatientDBUnitTest.class.getName());

 
 /**
     * Test inserting Patient record into the Database
     * @throws java.sql.SQLException
     */
    @Test
    public void testInsertPatient() throws SQLException {
    try (Connection connection = ConnectionHelper.getConnection()) {
        PatientData patient1 = new PatientData();
        patient1.setLastName("Last");
        patient1.setFirstName("First");
        patient1.setAddress("Address");
        patient1.setPhone("Phone");
        patient1.setDiagnosis("Diagnosis");
        patient1.setAddmissionDate(convertToLocalDateTime("2000-01-01 01:01:01"));

        PatientDAO pat = new PatientDAOImplement();
        pat.insertPatient(patient1);

        PatientData patient2 = pat.findById(patient1.getPatientId());
        assertEquals(patient2, patient1);
    }

    }
    
    /**
     * Test inserting Medication record into the Database
     * PATIENTID, MEDICAT, DOSAGE, FORM, DATEOFMED
     */
    @Test
    @Ignore
    public void testInsertMedication() throws SQLException {
    try (Connection connection = ConnectionHelper.getConnection()) {
        MedicationData medication1 = new MedicationData();
        medication1.setPatientId(1);
        medication1.setMedicat("Medication");
        medication1.setDosage("5 ml");
        medication1.setForm("drops");
        medication1.setDateOfMedicat(convertToLocalDateTime("2000-01-01 01:01:01"));
        
        PatientDAOImplement patient = new PatientDAOImplement();
        patient.insertMedicat(medication1);

        MedicationData medication2 = patient.findMedicationById(6);  
        assertEquals(medication2, medication1);
    }
    }
    
    
    /**
     * Test finding all Patient records and comparing with known amount = 5
     *
     * @throws SQLException
     */
    @Test
    public void testFindAll() throws SQLException {
    // we need to recreate database as one of the testmethods creates test Patient record,
    // but it can be before of after running this test method
    try (Connection connection = ConnectionHelper.getConnection()) {
        // we need to recreate database as one of the testmethods creates test Patient record,
        // but it can be before of after running this test method
        seedDatabase();

        PatientDAO patient = new PatientDAOImplement();
        ArrayList<PatientData> patData = patient.findAll();
        assertEquals("testFindAll: ", 5, patData.size());
    }
    }

    /**
     * Knowing details of the Patient with ID=4, this methods creates local
     * Patient object + details data then retrieves the same record using findID
     * and compares.
     *
     * FIRSTNAME, LASTNAME, ADDRESS, PHONE, DIAGNOSIS, ADMISSIONDATE
     * PATIENTID, MEDICAT, DOSAGE, FORM, DATEOFMED
     * @throws SQLException
     */
    @Test
    @Ignore
    public void testFindID() throws SQLException {
    try (Connection connection = ConnectionHelper.getConnection()) {
        MedicationData medication1 = new MedicationData(4, 4, "medication", "6 ml", "drops", convertToLocalDateTime("2014-07-14 14:00:00"));
        ArrayList<MedicationData> medications = new ArrayList<>();
        medications.add(medication1);
        
        PatientData patient1 = new PatientData(4, "Anna", "Fix","Address", "43670987245", "Pain",
                convertToLocalDateTime("2014-07-14 09:00:00"), medications);
        
        PatientDAO pat = new PatientDAOImplement();
        PatientData patient2 = pat.findById(4);

        assertEquals("testFindID for record 4: ", patient1, patient2);
    }
    }
    
    
    /**
     * Knowing details of the Patient(s) with LastName 'Stark', this methods
     * creates local ArrayList<Patient> object + details data then retrieves the
     * same record(s) using findByLastName and compares.
     *
     * @throws SQLException
     */
    @Test
    @Ignore
    public void testFindLastName() throws SQLException {
    try (Connection connection = ConnectionHelper.getConnection()) {
        MedicationData medication1 = new MedicationData(4, 4, "medication", "6 ml", "drops", convertToLocalDateTime("2014-07-14 14:00:00"));
        ArrayList<MedicationData> medications = new ArrayList<>();
        medications.add(medication1);
        
        PatientData patient1 = new PatientData(4, "Anna", "Fix","Address", "43670987245", "Pain",
                convertToLocalDateTime("2014-07-14 09:00:00"), medications);
        ArrayList<PatientData> patients1 = new ArrayList<>();
        patients1.add(patient1);

        PatientDAOImplement pat = new PatientDAOImplement();
        ArrayList<PatientData> patients2 = pat.findByLastName("Fix");

        assertEquals("testLastName for record 4: ", patients1, patients2);
    }
    }

     /**
     * Reads Patient record by Id from the DB into the bean, updates this bean
     * and updates it in the DB. Then reads this updated record into second bean
     * and compares both records.
     *
     * @throws SQLException
     */
    @Test
    public void testUpdateMaster() throws SQLException {
    try (Connection connection = ConnectionHelper.getConnection()) {
        PatientDAO patient = new PatientDAOImplement();

        PatientData patient1 = patient.findById(3);
        patient1.setLastName("Feonix");
        patient1.setFirstName("Kevin");
        patient1.setAddress("Address");
        patient1.setPhone("6478903846");
        patient1.setDiagnosis("Swallen leg");
        patient1.setAddmissionDate(convertToLocalDateTime("2000-01-01 01:01:01"));

        patient.updatePatient(patient1);

        PatientData patient2 = patient.findById(3);

        assertEquals(patient2, patient1);
    }
    }

    
     /**
     * Reads Medication record by Id from the DB into the bean, updates this
     * bean and updates it in the DB. Then reads this updated record into second
     * bean and compares both records.
     *
     * @throws SQLException
     */
    @Test
    @Ignore
    public void testUpdateMedication() throws SQLException {
    try (Connection connection = ConnectionHelper.getConnection()) {
        PatientDAOImplement patient = new PatientDAOImplement();

        MedicationData medication1 = patient.findMedicationById(1);
        medication1.setPatientId(1);
        medication1.setMedicat("Test medicat");
        medication1.setDosage("15 ml");
        medication1.setForm("drops");
        medication1.setDateOfMedicat(convertToLocalDateTime("2000-01-01 01:01:01"));

        patient.updateMedicat(medication1);

        MedicationData medication2 = patient.findMedicationById(1);

        assertEquals(medication2, medication1);
    }
    }

    
     /**
     * 1st verification: Finds Medication record by id, records it to the bean
     * and deletes record. Tries to find the same record and compares received
     * bean with null as null should be received. 2nd verification: counting
     * amount of records before and after deleting
     *
     * @throws SQLException
     */
    @Test
    @Ignore
    public void testDeleteMedication() throws SQLException {
    //1st verification:
    try (Connection connection = ConnectionHelper.getConnection()) {
        //1st verification:
        PatientDAOImplement patient = new PatientDAOImplement();
        PatientData patient1 = patient.findById(3);

        int medicationsBeforeDeleting = patient1.getMedication().size();
        log.info("Size of medications Before removing: " + medicationsBeforeDeleting);

        MedicationData medication1 = patient.findMedicationById(3);
        patient.deleteMedicat(medication1);
        MedicationData medication2 = patient.findMedicationById(3);
        assertEquals(null, medication2);

        //2nd verification
        PatientData patient2 = patient.findById(3);
        int medicationsAfterDeleting = patient2.getMedication().size();
        log.info("Size of medications After removing: " + medicationsAfterDeleting);
        assertEquals(medicationsBeforeDeleting - 1, medicationsAfterDeleting);
    }
    }

     /**
     * 1st verification: Finds Patient (Master) record by id, records it to the
     * bean and deletes record. Tries to find the same record and compares
     * received bean with null as null should be received. 2nd verification:
     * counting amount of records before and after deleting
     *
     * @throws SQLException
     */
    @Test
    public void testDeletePatient() throws SQLException {
    //1st verification:
    try (Connection connection = ConnectionHelper.getConnection()) {
        //1st verification:
        PatientDAO patient = new PatientDAOImplement();
        ArrayList<PatientData> patientsBeforeDeleting = patient.findAll();
        log.info("Size of patients Before removing: " + patientsBeforeDeleting.size());

        PatientData patient1 = patient.findById(5);
        patient.deletePatient(patient1);
        PatientData patient2 = patient.findById(5);
        assertEquals(null, patient2);

        //2nd verification
        ArrayList<PatientData> patientsAfterDeleting = patient.findAll();
        log.info("Size of patients After removing: " + patientsAfterDeleting.size());
        assertEquals(patientsBeforeDeleting.size() - 1, patientsAfterDeleting.size());
    }
    }
    
    
    /**
     * This routine recreates the database for every test. This makes sure that
     * a destructive test will not interfere with any other test.
     *
     * This routine is courtesy of Bartosz Majsak, an Arquillian developer at
     * JBoss
     */
    @Before
    public void seedDatabase() {
        final String seedDataScript = loadAsString("hospitaldb.sql");
        try (Connection connection = ConnectionHelper.getConnection();) {
            for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed seeding database", e);
        }
    }

    /**
     * The following methods support the seedDatabse method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                Scanner scanner = new Scanner(inputStream)) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }

    private List<String> splitStatements(Reader reader, String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<String>();
        try {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }
    
    /**
     * Converts string to LocalDateTime object
     *
     * @param dateTime
     * @return
     */
    private LocalDateTime convertToLocalDateTime(String dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

        return localDateTime;
    }
    
    
    

}
