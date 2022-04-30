package com.company.tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class TestsBackButton extends Application {
    BorderPane p;
    Scene scene;
    Button bb;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        //Instantiate all the variables.
        initComponents();

        //Scene creation
        p.setCenter(bb);
        scene = new Scene(p , 600 , 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initComponents() {
        p = new BorderPane();
        bb = new Button("Back");
        bb.setPrefWidth(200);
        bb.getStyleClass().add("backbutton");
        bb.getStylesheets().add("file:./src/com/company/css/bb.css");
    }
}

