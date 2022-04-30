package com.company.tests;

import com.company.components.VisualAddress;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.web3j.protocol.Web3j;


public class TestsVisualAddress extends Application {
    Web3j web3j;
    BorderPane p;
    Scene scene;
    VisualAddress visualAddress;
    Label title;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        //Instantiate all the variables.
        initComponents();

        //Scene creation
        scene = new Scene(p , 600, 600);
        p.setCenter(visualAddress);
        p.setTop(title);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initComponents() {
        p = new BorderPane();
        title = new Label("Tests for the Visual Address Class");
        BorderPane.setAlignment(title, Pos.CENTER);
        title.setFont(Font.font("", FontWeight.BOLD, 25));
        visualAddress = new VisualAddress("0x6AF5117a911C97577F46B4DDC38C9123710A3FEf", "1000000", "10000");
    }
}

