/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maryna.patientdb.controllers;

import com.maryna.patientdb.MainAppFX;
import com.maryna.patientdb.beans.PatientData;
import com.maryna.patientdb.persistence.PatientDAO;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static javafx.application.ConditionalFeature.FXML;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import jfxtras.scene.control.LocalDateTimeTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marynaprix
 */
public class PatientFXMLController {

    // Real programmers use logging, not System.out.println
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private PatientDAO patientDAO;
    private PatientData patient;

    // Reference to the main application.
    private MainAppFX mainApp;
    
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<PatientData> patientDataList = FXCollections.observableArrayList();

    // The @FXML annotation on a class variable results in the matching
    // reference being injected into the variable
    // label is defined in the fxml file
    @FXML
    private TextField patientIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField diagnosisField;
    @FXML
    // http://jfxtras.org/doc/8.0/jfxtras-controls/jfxtras/scene/control/LocalDateTimeTextField.html#setText-java.lang.String-
    private LocalDateTimeTextField admissionDateField;

    @FXML
    private TableView<PatientData> patientDataTable;

    @FXML
    private TableColumn<PatientData, Number> patientIdColumn;

    @FXML
    private TableColumn<PatientData, String> firstNameColumn;

    @FXML
    private TableColumn<PatientData, String> lastNameColumn;

    @FXML
    private TableColumn<PatientData, String> diagnosisColumn;

    @FXML
    private TableColumn<PatientData, LocalDateTime> admissionDateColumn;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public PatientFXMLController() {
        super();
        // Create empty Patient object for initialize method, where later this object will be updated with data from DB
        patient = new PatientData();
    }
    
       /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. Useful if a control must be
     * dynamically configured such as loading data into a table.
     */
    @FXML
    private void initialize() {
        log.info("initialize called");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Initializing form fields
        Bindings.bindBidirectional(patientIdField.textProperty(), patient.patientIdProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(firstNameField.textProperty(), patient.firstNameProperty());
        Bindings.bindBidirectional(lastNameField.textProperty(), patient.lastNameProperty());
        Bindings.bindBidirectional(addressField.textProperty(), patient.addressProperty());
        Bindings.bindBidirectional(phoneField.textProperty(), patient.phoneProperty());
        Bindings.bindBidirectional(diagnosisField.textProperty(), patient.diagnosisProperty());
        Bindings.bindBidirectional(admissionDateField.localDateTimeProperty(), patient.addmissionDateProperty());
        admissionDateField.setDateTimeFormatter(formatter);
        

        // Initializing Table with Patient data
        patientIdColumn.setCellValueFactory(cellData -> cellData.getValue().patientIdProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        diagnosisColumn.setCellValueFactory(cellData -> cellData.getValue().diagnosisProperty());

        admissionDateColumn.setCellValueFactory(cellData -> cellData.getValue().addmissionDateProperty());

        /* Formatting LocalDateTime
         * http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
         */
        admissionDateColumn.setCellFactory(column -> {
            return new TableCell<PatientData, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        // Format date.
                        setText(formatter.format(item));
                    }
                }
            };
        });

        
        adjustColumnWidths();

        // Updating form fields with Patient data from selected rows
        patientDataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // NOTE, newSelection object returns Patients with child data, 
            // but updatePatientObj method recreates child data too
            if (newSelection != null) {
                log.debug("newSelection: " + newSelection.toString());
                patientDAO.updatePatientObj(patient, newSelection);
                log.debug("patient:      " + patient.toString());
            }
        });
        
        /*
         ******************************
         ***********Validation*********
         ******************************
         */
        Validation.getValidator()
                .validateIntTextField(patientIdField, log);
        Validation.getValidator()
                .validateStringtextField(lastNameField, log, 30);
        Validation.getValidator()
                .validateStringtextField(firstNameField, log, 30);
        Validation.getValidator()
                .validateFullStringtextField(diagnosisField, log, 60);
        releaseDateField.parseErrorCallbackProperty().set((Callback<Throwable, Void>) (Throwable p) -> {
            return null;
        });
        admissionDateField.parseErrorCallbackProperty().set((Callback<Throwable, Void>) (Throwable p) -> {
            return null;
        });

        /*
         ******************************
         ***********Validation*********
         ******************************
         */
// additional validation for 'releaseDateField'  DOES NOT WORK CORRECTLY, PROBABLY ISSUE IN THE FRAMEWORK :(       
//        releaseDateField.focusedProperty().addListener((obs, oldSelection, newSelection) -> {
//            log.debug("picker shoving before? - " + releaseDateField.isPickerShowing());
//            if(!Objects.equals(newSelection, oldSelection))
//            releaseDateField.setPickerShowing(true);
//            log.debug("picker shoving after? - " + releaseDateField.isPickerShowing());
//        });
    } 
    
    
    @FXML
    void clearAllFields(ActionEvent event) {

    }

    @FXML
    void deletePatientRecord(ActionEvent event) {

    }

    @FXML
    void exitProgram(ActionEvent event) {

    }

    @FXML
    void loadMedicationWindow(ActionEvent event) {

    }

    @FXML
    void nextPatientRecord(ActionEvent event) {

    }

    @FXML
    void previousPatientRecord(ActionEvent event) {

    }

    @FXML
    void reportTotalPatientCost(ActionEvent event) {

    }

    @FXML
    void savePatientRecord(ActionEvent event) {

    }

    @FXML
    void searchPatientRecord(ActionEvent event) {

    }
    
    
    /**
     * Sets the width of the columns based on a percentage of the overall width
     *
     * This needs to enhanced so that it uses the width of the anchor pane it is
     * in and then changes the width as the table grows.
     */
    private void adjustColumnWidths() {
        // Get the current width of the table
        double width = patientDataTable.getPrefWidth();
        // Set width of each column
        patientIdColumn.setPrefWidth(width * .14);
        firstNameColumn.setPrefWidth(width * .17);
        lastNameColumn.setPrefWidth(width * .17);
        diagnosisColumn.setPrefWidth(width * .25);
        admissionDateColumn.setPrefWidth(width * .27);
        
    }

}

    

