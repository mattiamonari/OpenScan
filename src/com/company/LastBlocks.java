package com.company;

import com.company.components.VisualBlock;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

public class LastBlocks extends GridPane {

    Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
    Task<Void> task;
    VisualBlock[] blocks;

    @FXML
    private ProgressBar blocksLoaded;

    @FXML
    private Button backButton;

    public LastBlocks(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lastblocks.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        backButton.setOnAction(event -> {
            Platform.runLater(() -> {
                primaryStage.setScene(new Scene(new MainWindow(primaryStage),600,600));
            });
        });

        createAndStartTask();

        blocksLoaded.progressProperty().bind(task.progressProperty());
    }

    private void loadBlocks() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.add(blocks[i*3+j], j, i);
                GridPane.setMargin(blocks[i*3+j], new Insets(5));
            }
        }
    }

    private void createAndStartTask() {

        task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception {
                int progress = 0;
                int lastBlock = 0;
                lastBlock = Integer.decode(web3j.ethBlockNumber().sendAsync().get().getResult());
                blocks = new VisualBlock[16];
                for (int i = 8; i >= 0; i--){
                    updateProgress(progress++ + 1, 27);
                    EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(lastBlock - i), true).sendAsync().get();
                    var error = block.getError();
                    updateProgress(progress++ + 1, 27);
                    if (error == null){
                        String txCount = String.valueOf(block.getBlock().getTransactions().size());
                        blocks[8 - i] = new VisualBlock(String.valueOf(lastBlock - i), block.getBlock().getHash(),
                                block.getBlock().getMiner(),
                                txCount, 18, 200, 100, false);
                    }
                    else
                    {
                        blocksLoaded.progressProperty().unbind();
                        createDefaultBlocks();
                        break;
                    }
                    updateProgress(progress++ + 1, 27);
                }
                blocksLoaded.setVisible(false);
                blocksLoaded.setDisable(true);
                return null;
            }
        };

        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newState) {
                if(newState== Worker.State.SUCCEEDED){
                    loadBlocks();
                }
            }
        });

        new Thread(task).start();

    }

    private void createDefaultBlocks() {
        for (int i = 0; i < 9; i++) {
            blocks[i] = new VisualBlock( 18, 200, 100, false);
            blocksLoaded.setProgress(i*100/9);
        }
    }
    //TODO: LESS BLOCKS, BUT MORE DETAILED.

}
