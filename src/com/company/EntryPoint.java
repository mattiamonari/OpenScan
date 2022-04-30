package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EntryPoint extends Application {


    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("OpenScan");
        stage.getIcons().add(new Image("com/company/res/logo.png"));
        MainWindow mw = new MainWindow(stage);
        Scene scene = new Scene(mw, 600, 600);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    /*
    @Override
    public void start(Stage stage) throws IOException {
        FlowPane f = new FlowPane(Orientation.VERTICAL);
        VisualBlock v = new VisualBlock("100","0xdsdsdds","0xdddddd","10",18,200,100);
        VisualBlock v1 = new VisualBlock("100","0xdsdsdds","0xdddddd","10",18,200,100);
        f.getChildren().addAll(v,v1);
        Scene s = new Scene(f, 300,300);
        stage.setScene(s);
        stage.show();
    }*/



}
