package com.company;

import com.company.components.BalanceLabel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT;

public class SmallWallet extends BorderPane {

    Web3j w;

    @FXML
    TextField mnemonic;
    @FXML
    Label addLab;
    @FXML
    VBox centerInfoVBox;
    @FXML
    Button backButton;

    public SmallWallet(Stage primaryStage) throws Exception {
        w = Web3j.build(new HttpService("http://127.0.0.1:8545"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("smallwallet.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mnemonic.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                    restoreWallet(mnemonic.getText());
                    mnemonic.setVisible(false);
            }
        });
        backButton.setOnAction(e -> mainWindowPag(primaryStage));

    }

    private void mainWindowPag(Stage primaryStage) {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                primaryStage.setScene(new Scene(new MainWindow(primaryStage),600,600));
            }
        });
    }

    private void restoreWallet(String text) {
        //Credentials credentials = WalletUtils.loadBip39Credentials(null, text);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(text, ""));
        int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0,0};
        Bip32ECKeyPair  x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
        Credentials credentials = Credentials.create(x);
        addLab.setText("Address: " + credentials.getAddress());
        BigInteger amount = BigInteger.ZERO;
        try {
            amount =
                    w.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getBalance();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        BalanceLabel b = new BalanceLabel(Convert.fromWei(new BigDecimal(amount), Convert.Unit.ETHER).toString(), 14);
        centerInfoVBox.getChildren().add(b);
        BorderPane.setMargin(b, new Insets(10));
        b.setPadding(new Insets(10));
    }
}
