package com.company.components;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;

import static com.company.utils.SystemUtilities.copyToClipboardText;


//TODO IMPROVE BLOCK UI QUALITY
public class VisualBlock extends BorderPane {

    @FXML
    private Label blockNumber;
    @FXML
    private Label hash;
    @FXML
    private Label miner;
    @FXML
    private Label txCount;
    @FXML
    private VBox infoContainer;

    private String hashValue, minerValue, txCountValue;

    public VisualBlock(int fontSize, int prefWidth, int prefHeight, boolean isAnimated){

        this.hashValue = "-1";
        this.minerValue = "-1";
        this.txCountValue = "-1";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("visualblock.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        blockNumber.setText("number");
        setHashValue("-1");
        setTxCountValue("-1");
        setMinerValue("-1");
        setupLayout(prefWidth, prefHeight);
        setLabelsFont(fontSize);


        installTooltip(isAnimated);
    }

    public VisualBlock(String number, String hashValue, String minerValue, String txCountValue, int fontSize,
                       int prefWidth, int prefHeight, boolean isAnimated){

        this.hashValue = hashValue;
        this.minerValue = minerValue;
        this.txCountValue = txCountValue;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("visualblock.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        blockNumber.setText(number);
        setHashValue(hashValue);
        setTxCountValue(txCountValue);
        setMinerValue(minerValue);
        setupLayout(prefWidth, prefHeight);
        setLabelsFont(fontSize);


        installTooltip(isAnimated);
    }

    private void installTooltip(boolean isAnimated) {
        //isAnimated helps to differentiate between block that are in the main window and the other blocks.
        if (!isAnimated){
            final String copyText = "This block was mined by " + minerValue + ". \nIt has the hash: " + hashValue;
            Tooltip.install(this , new Tooltip(copyText +  "\nClick to" + " copy this text!"));
            EventHandler<MouseEvent> last16BlockHandler = e -> {
                try {
                    copyToClipboardText("This block was mined by " + minerValue + ". \nIt has the hash: " + hashValue);
                    //TODO NOTIFICATION (?)
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, last16BlockHandler);
        }
    }

    private void setupLayout(int prefWidth, int prefHeight) {
        this.setBackground(new Background(new BackgroundFill(Color.DARKGRAY , new CornerRadii(5), Insets.EMPTY)));
        this.setPrefSize(prefWidth , prefHeight);
        this.setMaxSize(prefWidth, prefHeight);
        this.setPadding(Insets.EMPTY);
        this.setBorder(new Border(new BorderStroke(Color.BLACK , BorderStrokeStyle.SOLID , new CornerRadii(5), BorderWidths.DEFAULT)));
    }

    private void setLabelsFont(int fontSize) {
        blockNumber.setFont(Font.font("", FontWeight.BOLD, fontSize));
        hash.setFont(Font.font("", FontWeight.NORMAL, fontSize-5));
        miner.setFont(Font.font("", FontWeight.NORMAL, fontSize-5));
        txCount.setFont(Font.font("", FontWeight.NORMAL, fontSize-5));
    }

    public void setTxCountValue(String txCountValue) {
        this.txCountValue = txCountValue;
        if(txCountValue != null && txCountValue != "" ) {
            int value = Integer.valueOf(txCountValue);
            if (value > 0 && value < 100) {
                txCount.setStyle("-fx-text-fill: green;");
            } else if (value > 100 && value < 200) {
                txCount.setStyle("-fx-text-fill: orange;");
            } else if (value > 200) {
                txCount.setStyle("-fx-text-fill: red;");
            }
        }
        txCount.setText("TxCount: " + txCountValue);
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
        hash.setText("Hash: " + hashValue);
    }

    public void setMinerValue(String minerValue) {
        this.minerValue = minerValue;
        miner.setText("Mined by: " + minerValue);
    }

    public void setBlockNumber(String number) {
        blockNumber.setText(number);
    }
}
