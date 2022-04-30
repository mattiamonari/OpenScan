package com.company.tests;

import com.company.components.BlockInfo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.web3j.protocol.Web3j;

public class TestsBlockInfo extends Application {
    Web3j web3j;
    BorderPane p;
    Scene scene;
    BlockInfo blockInfo;
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
        //p.setCenter(blockInfo);
        scene = new Scene(blockInfo , 600 , 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initComponents() {
        p = new BorderPane();
        blockInfo = new BlockInfo(400, 400, "0x", "0x", "10","1000", null);
    }
}

