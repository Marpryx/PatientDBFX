package com.maryna.patientdb;

import com.maryna.patientdb.beans.PatientData;
import com.maryna.patientdb.persistence.PatientDAO;
import com.maryna.patientdb.persistence.PatientDAOImplement;
import static javafx.application.Application.launch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Starts a JavaFX application
 *
 *
 */
public class MainAppFX extends Application {

    // to write out text to the debugging logs
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // The primary window/frame of the application
    private Stage primaryStage;
    
    //PatientDAO interface reference field
    private final PatientDAO patientDAO;

    //Patient object that will be passed to medication controller
    private PatientData patient;
    
    /**
     * Constructor
     */
    public MainAppFX() {
        super();
        patientDAO = new PatientDAOImplement();
    }

    /**
     * Start point of an application
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("Program Starts Here");

        // The Stage comes from the framework so make a copy to use elsewhere
        this.primaryStage = primaryStage; // the main window of app
        // Create the Scene and put it on the Stage
        configureStage();

        // Set the window title
        primaryStage.setTitle("Hospital database administration tool");
        // Raise the curtain on the Stage
        this.primaryStage.show();
    }

    /**
     * Loads Patient FXML layout
     */
    public void loadPatientParentWindow() {

        try {
            // Instantiate the FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            // Set the location of the fxml file in the FXMLLoader
            loader.setLocation(MainAppFX.class.getResource("/fxml/Patient.fxml"));

            // Parent is the base class for all nodes that have children in the
            // scene graph such as AnchorPane and most other containers
            Parent parent = (AnchorPane) loader.load();

            // Load the parent into a Scene
            Scene scene = new Scene(parent);

            // Put the Scene on Stage
            primaryStage.setScene(scene);

            // Give the PatientFXMLController controller access to the main app.
            PatientFXMLController controller = loader.getController();
            controller.setMainAppFX(this);
            // Pass PatientDAO instance to the Patient controller
            controller.setPatientDAO(patientDAO);
            // Load data onto Patient table
            controller.displayPatientTable();

        } catch (IOException | SQLException ex) { // getting resources or files could fail
            log.error(null, ex);
            System.exit(1);
        }
    }


    /**
     * Main method to start the app
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}
