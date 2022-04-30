package com.company.tests;

import com.company.components.VisualBlock;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.web3j.protocol.Web3j;

public class TestsVisualBlock extends Application {
    Web3j web3j;
    BorderPane p;
    Scene scene;
    VisualBlock block;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        //Instantiate all the variables.
        initComponents();

        //Scene creation
        p.setBottom(block);
        scene = new Scene(p , 400 , 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initComponents() {
        p = new BorderPane();
        block = new VisualBlock("14444", "0x07d07b6c13db30632fd4a308752f0583a5cfcdfe481017b49edd2960fbc726cc",
                "0x34c51a83259d28389b2837d1bab78bd5e5ec96f1", "1000000", 25, 300, 100, false
        );
        BorderPane.setMargin(block, new Insets(0,0,150,0));
    }
}