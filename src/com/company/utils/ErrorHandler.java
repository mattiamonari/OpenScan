package com.company.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ErrorHandler {


    public static void  noPeersError(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("No suitable peers available (RPC Code: -32000)");
        errorAlert.setContentText("""
                        Looks like your Geth can't find any peers to connect to.
                        If you're running the light version, this is normal, don't worry.
                        Try to restart the Geth client and wait.""");
        errorAlert.setOnHidden(event -> {
            //Platform.exit();
            //System.exit(0);
        });

        errorAlert.showAndWait();
    }

    public static void noGethConnection(){
        Alert errorAlert = new Alert(Alert.AlertType.WARNING);
        errorAlert.setTitle("Error with Geth client");
        errorAlert.setContentText("Looks like Geth client isn't running. Try to run it in your command line!");
        Optional<ButtonType> r = errorAlert.showAndWait();
        if (r.get() == ButtonType.OK){
            //Platform.exit();
            //System.exit(0);
        }
    }

    public static void wrongInputValue() {
        Alert errorAlert = new Alert(Alert.AlertType.WARNING);
        errorAlert.setTitle("Wrong input value");
        errorAlert.setContentText("""
                Looks like the entered input isn't correct. Unfortunately we don't support ENS domains!
                """);
        errorAlert.showAndWait();
    }
}
