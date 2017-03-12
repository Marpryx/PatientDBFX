package com.maryna.patientdb;

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
import java.util.ResourceBundle;

/**
 * Basic class for starting a JavaFX application
 *
 *
 */
public class MainAppFX extends Application {

    // to write out text to the debugging logs
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // The primary window/frame of the application
    private Stage primaryStage;

    /**
     * Constructor
     */
    public MainAppFX() {
        super();
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
        primaryStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("title"));
        // Raise the curtain on the Stage
        primaryStage.show();
    }

    /**
     * Load the FXML and bundle, create a Scene and put the Scene on Stage.
     *
     * This approach allows to use loader.getController() to get a
     * reference to the fxml's controller should you need to pass data to it.
     * Not used in this archetype.
     */
    private void configureStage() {
        try {
            // Instantiate the FXMLLoader
            FXMLLoader loader = new FXMLLoader();

            // Set the location of the fxml file in the FXMLLoader
            loader.setLocation(MainAppFX.class.getResource("/fxml/Scene.fxml"));

            // Localize the loader with its bundle
            // Uses the default locale and if a matching bundle is not found
            // will then use MessagesBundle.properties
            loader.setResources(ResourceBundle.getBundle("MessagesBundle"));

            // Parent is the base class for all nodes that have children in the
            // scene graph such as AnchorPane and most other containers
            Parent parent = (AnchorPane) loader.load();

            // Load the parent into a Scene
            Scene scene = new Scene(parent);

            // Put the Scene on Stage
            primaryStage.setScene(scene);

        } catch (IOException ex) { // getting resources or files could fail
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
