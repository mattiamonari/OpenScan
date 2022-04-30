package com.company.components;

import com.company.utils.Blockies;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VisualAddress extends AnchorPane {

    private Label address, txCount;
    private BalanceLabel balance;
    private String txCountValue;
    private String addressValue;
    private final Blockies b;

    public VisualAddress(String value, String balance, String txCountValue) {

        b = new Blockies(value , 8 , 18); // 8*18 = 144 --> 150
        address = new Label("Address: ");
        address.setFont(Font.font("", FontWeight.BOLD , 18));
        this.balance = new BalanceLabel();
        txCount = new Label("TxCount: ");
        txCount.setFont(Font.font("", FontWeight.LIGHT , 20));

        this.txCountValue = "0";
        this.addressValue = "";
        this.balance.setBalanceValue("0");

        this.getChildren().addAll(b, address, this.balance, txCount);
        placeComponents();

        setAddress(value);
        setBalance(balance);
        setTxCount(txCountValue);


    }

    private void placeComponents(){

        this.setMaxWidth(600);
        this.setPrefHeight(200);

        AnchorPane.setTopAnchor(address, 120d);
        AnchorPane.setLeftAnchor(address, 25d);

        AnchorPane.setTopAnchor(b, 150d);
        AnchorPane.setLeftAnchor(b, 25d);

        AnchorPane.setTopAnchor(balance, 150d);
        AnchorPane.setLeftAnchor(balance, 190d);

        AnchorPane.setTopAnchor(txCount, 180d);
        AnchorPane.setLeftAnchor(txCount, 190d);

    }

    public String getAddress() {
        return addressValue;
    }

    public void setAddress(String address) {
        addressValue = address;
        this.address.setText("Address: " + addressValue);
    }

    public String getBalance() {
        return balance.getBalanceValue();
    }

    public void setBalance(String balance) {
        this.balance.setBalanceValue(balance);
    }

    public String getTxCount() {
        return txCountValue;
    }

    public void setTxCount(String txCount) {
        txCountValue = txCount;
        this.txCount.setText("TxCount: " + txCountValue);
    }



}