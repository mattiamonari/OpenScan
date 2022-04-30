package com.company;

import com.company.components.BlockInfo;
import com.company.components.VisualAddress;
import com.company.utils.ErrorHandler;
import com.company.utils.EthereumUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SearchWindow extends BorderPane {

    Web3j web3j;

    @FXML
    Button backButton;
    @FXML
    TextField input;

    public SearchWindow(Stage primaryStage){
        web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchwindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        backButton.setOnAction(e -> mainWindowPage(primaryStage));
        input.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                try {
                    searchOnTheBlockchain(input.getText());
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void mainWindowPage(Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.setScene(new Scene(new MainWindow(primaryStage),600,600));
            }
        });
    }

    private void searchOnTheBlockchain(String value) throws ExecutionException, InterruptedException, IOException {
        if(EthereumUtils.isBlock(value, web3j))
        {
            EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(new BigInteger(value)),true).sendAsync().get().getBlock();
            String currentBlock = block.getNumber().toString(); //TODO RENAME CurentBlock, make local (?)
            String hash = block.getHash();
            String miner = block.getMiner();
            String txCount = String.valueOf(block.getTransactions().size());
            List<Transaction> txs = new ArrayList<>();
            for (int i = 0; i < Integer.valueOf(txCount); i++) {
                txs.add(web3j.ethGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(new BigInteger(value)),new BigInteger(String.valueOf(i))).sendAsync().get().getTransaction().get());
            }
            BlockInfo blockInfo = new BlockInfo(550, 500, miner, hash, txCount, currentBlock, txs);
            this.setCenter(blockInfo);
        }
        else if(EthereumUtils.isAddress(value))
        {

            BigInteger balanceInWei = web3j.ethGetBalance(value, DefaultBlockParameterName.LATEST).sendAsync().get().getBalance();
            BigDecimal balance = Convert.fromWei(new BigDecimal(balanceInWei), Convert.Unit.ETHER);
            BigInteger txCountBig = web3j.ethGetTransactionCount(value, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            VisualAddress visualAddress = new VisualAddress(value.toLowerCase(Locale.ROOT), balance.setScale(7, RoundingMode.FLOOR).toString(), txCountBig.toString());
            this.setCenter(visualAddress);
        }
        else if(EthereumUtils.isHash(value))
        {
            EthBlock.Block block = web3j.ethGetBlockByHash(value, true).sendAsync().get().getBlock();
            if (block == null){
                Optional<Transaction> tx = web3j.ethGetTransactionByHash(value).sendAsync().get().getTransaction();
                System.out.println("From: " + tx.get().getTo() + ", To: " + tx.get().getFrom());
            }
            else {
                System.out.println(block.getTransactions().listIterator());
            }

        }
        else
            ErrorHandler.wrongInputValue();
    }

    //TODO S Pop up screen message if wrong value
}
