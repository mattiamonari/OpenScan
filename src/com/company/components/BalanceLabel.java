package com.company.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;

public class BalanceLabel extends FlowPane {

    @FXML
    private Label balance;
    @FXML
    private URL location;

    private String balanceValue = "0.00000000000";

    public BalanceLabel() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("balancelabel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBalanceValue("0.00000000000");
    }

    public BalanceLabel(String balanceValue, int fontSize) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("balancelabel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        balance.setFont(new Font(fontSize));
        setBalanceValue(balanceValue);
    }

    public void setBalanceValue(String balanceValue) {
        this.balanceValue = balanceValue;
        balance.setText("Balance: " + balanceValue);
    }

    public String getBalanceValue() {   return balanceValue;    }

}
