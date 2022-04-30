package com.company.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class BlockInfo extends VBox {

    @FXML
    Label number;
    @FXML
    Label hash;
    @FXML
    Label miner;
    @FXML
    Label txCount;
    @FXML
    Label titleTx;
    @FXML
    ListView<String> txsView;

    ObservableList<String> txs;

    public BlockInfo(double width, double height, String miner, String hash, String txCount, String number, List<Transaction> l) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("blockinfo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        this.setMinSize(width,height);
        this.setMaxSize(width,height);

        txs = FXCollections.observableArrayList();
        addTxs(l);

        setHash(hash);
        setTxCount(txCount);
        setMiner(miner);
        setNumber(number);
        setFonts();
    }

    private void setFonts() {
        number.setFont(Font.font("", FontWeight.BOLD, 25));
        hash.setFont(Font.font("", FontWeight.SEMI_BOLD, 18));
        miner.setFont(Font.font("", FontWeight.SEMI_BOLD, 18));
        txCount.setFont(Font.font("", FontWeight.SEMI_BOLD, 18));
        titleTx.setFont(Font.font("", FontWeight.BOLD, 20));
    }

    private void addTxs(List<Transaction> l) {

        if(l != null && (l.size() != 0))
        {
            int len = Math.min(l.size() , 10);
            for (int i = 0; i < len; i++) {
                Transaction tx = l.get(i);
                BigDecimal fee = Convert.toWei(new BigDecimal(tx.getGas()), Convert.Unit.GWEI);
                String hash = tx.getHash();
                txs.add(hash+"From: " + tx.getFrom() + " --> To: " + tx.getTo() + ". Fee paid: " + Convert.fromWei(fee,
                        Convert.Unit.ETHER) + "ETH");
            }
            txsView.setItems(txs);
            //TODO WHAT AM I FCKING DOING IN HERE
            txsView.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<String>(){
                    final Tooltip tooltip = new Tooltip();
                    @Override
                    protected void updateItem(String s, boolean empty) {
                        super.updateItem(s, empty);

                        if (s == null || empty) {
                            setText(null);
                            setTooltip(null);
                        } else {
                            setText(s.substring(66));
                            tooltip.setText("Hash: " + s.substring(0,66));
                            setTooltip(tooltip);
                        }
                    }
                };

                cell.setOnMouseClicked(e-> {
                    try {
                        if(e.getClickCount() == 2)
                            Desktop.getDesktop().browse(new URL("https://etherscan.io/tx/" + cell.getTooltip().getText().substring(6)).toURI());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                });

                return cell;
            });
            txsView.setMaxHeight(len * 30f + 15f);
        }
    }

    public void setNumber(String number) {
        this.number.setText(this.number.getText() + number);
    }

    public void setHash(String hash) {
        this.hash.setText(this.hash.getText() + hash);
    }

    public void setMiner(String miner) {
        this.miner.setText(this.miner.getText() + miner);
    }

    public void setTxCount(String txCount) {
        this.txCount.setText(this.txCount.getText() + txCount);
    }

}
