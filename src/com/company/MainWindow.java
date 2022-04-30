package com.company;

import com.company.components.VisualBlock;
import com.company.utils.ErrorHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.HyperlinkLabel;
import org.controlsfx.control.Notifications;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainWindow extends BorderPane {

    //region Local variables decleration
    private Web3j web3j;
    private TranslateTransition tt;
    private Timeline timeline;
    private String currentBlock, lastBlock;
    private Stage primaryStage;
    private VisualBlock visualBlock;
    private boolean isAnimationFinished;
    private boolean clientError = false;
    private int peerCount;

    //endregion

    //region FXML variables declaration
    @FXML
    VBox aniCont;
    @FXML
    Button seaPagBut;
    @FXML
    Label appDesc;
    @FXML
    Button lastBlockPag;
    @FXML
    Button walletPag;
    @FXML
    HyperlinkLabel hll;

    //endregion

    public MainWindow(Stage primaryStage) {

        this.primaryStage = primaryStage;

        peerCount = 0;

        //Load FXML for the scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainwindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Instantiate all the variables.
        initComponents();

        //Write long texts into the appropriate Text Container
        setLongTexts();

        //Setup the Block Animation
        setupBlockAnimation(-500, 600, visualBlock);

        //Scene creation
        aniCont.getChildren().addAll(visualBlock);

        //This Task make a first control on the Geth client
        //If it encounters some errors the checkForNewBlocks() method will NOT be called and will pop-up a dialog.
        checkForClient();

        if(!clientError)
            playBlockResearch();

    }

    //TODO: IMPROVE THIS METHOD
    private void setupNotificationsForPeers() {
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    BigInteger currentPeerCount = BigInteger.valueOf(-1);
                    try {
                        currentPeerCount = web3j.netPeerCount().sendAsync().get().getQuantity();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (currentPeerCount.compareTo(BigInteger.valueOf(peerCount)) > 0 ) {
                        Notifications.create()
                                .text("New peer found!")
                                .graphic(new Pane()) // sets node to display
                                .hideAfter(Duration.INDEFINITE)
                                .show();
                    }
                    else
                    {
                        if(peerCount == 0) {
                            Notifications.create().text("You're currently out of peer. Try to pass later.").graphic(new Pane())
                                    .hideAfter(Duration.INDEFINITE).show();
                        }
                    }
                    peerCount = Integer.parseInt(currentPeerCount.toString());
                }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    private void checkForClient() {

        Task<Void> task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception {
                //Added cause non-main Threads in JavaFX can't edit UI
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        EthBlock b = null;
                        try {
                            b = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).sendAsync().get();
                        }
                        catch (InterruptedException e) {
                            //Custom error for interupted execution
                        }
                        catch (ExecutionException e) {
                            clientError = true;
                            ErrorHandler.noGethConnection();
                        }

                        //Could block be still null here?
                        if(b.getError() != null){
                            clientError = true;
                            ErrorHandler.noPeersError();
                        }

                        if(clientError) {
                            stopBlockResearch();
                        }
                        else
                            //TODO DO I NEED TO DO DOMETHING IN HERE?
                        ;
                    }
                });
                return null;
            }
        };

        new Thread(task).start();
    }

    private void initComponents() {

        //All this for the animation
        isAnimationFinished = true;
        tt = new TranslateTransition(Duration.seconds(5));
        visualBlock = new VisualBlock("-1", "-1" , "-1", "-1", 30, 400, 45, true);
        visualBlock.setVisible(false);
        lastBlock = "0";
        currentBlock = "0";
        web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> checkForNewBlocks()));
        timeline.setCycleCount(Animation.INDEFINITE);

        //Setting up the closure
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        //Events setup
        seaPagBut.setOnAction(e -> searchPage());
        lastBlockPag.setOnAction(e -> lastBlocksPage());
        walletPag.setOnAction(e -> walletPage());
        hll.setOnAction(e -> openHyperLink(e, "https://ethereum.org"));

    }

    //TODO: IF A NEW BLOCK ARRIVES WHILE THE ANIMATION IS PLAYING, THE BLOCK NUMBER CHANGES. NOT GOOD, IMPLEMENT A
    // SOLUTION. COULD A QUEUE DO THAT? WOULD ALSO BE USEFUL FOR PRELOADING THE BLOCKS IN GRIDPANEPROVE
    private void checkForNewBlocks(){

        //TODO
        //This workaround will eventually broke the Application. We must find a way
        //To check periodically for the Errors and if the research can proceed, restart it.
        checkForClient();

        EthBlock.Block block = null;
        String hash = "-----";
        String miner = "-----";
        String txCount = "-----";

        try {
            var response = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).sendAsync().get();
            var error = response.getError();
            if(error == null)
            {
                block = response.getBlock();
            }
            else
            {
                if(error.getCode() == -32000)
                    stopBlockResearch();
                return;
            }

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            ErrorHandler.noGethConnection();
        }

        if(!clientError && (block != null)){
            currentBlock = block.getNumber().toString(); //TODO RENAME CurentBlock, make local (?)
            hash = block.getHash();
            miner = block.getMiner();
            txCount = String.valueOf(block.getTransactions().size());
        }

        if(Integer.parseInt(currentBlock) > Integer.parseInt(lastBlock) && isAnimationFinished)
        {
            lastBlock = currentBlock;
            visualBlock.setBlockNumber(currentBlock);
            visualBlock.setMinerValue(miner);
            visualBlock.setHashValue(hash);
            visualBlock.setTxCountValue(txCount);
            isAnimationFinished = false;
            tt.playFromStart();
            visualBlock.setVisible(true);
        }
        tt.statusProperty().addListener((observableValue , oldValue , newValue) -> {
            isAnimationFinished = true;
        });

    }

    //region Utilities methods

    public void playBlockResearch(){
        timeline.playFromStart();
    }

    public void stopBlockResearch(){
        timeline.stop();
        timeline.jumpTo(Duration.ZERO);
        tt.jumpTo(Duration.ZERO);
        tt.stop();

    }

    private void setLongTexts() {
        appDesc.setText("""
                Welcome. This app aims to be a free and ease to use scanner/explorer for the Ethereum blockchain. It's well known that online there are many others software which provides same (and more!) functionalities, but as secondary goal for the project we wanted to sensibilize people in the use of the low-level technologies  around blockhain. In fact, this application requires at least an instance of the Geth light client.  
                
                """);
        appDesc.setWrapText(true);
        appDesc.setPadding(new Insets(10,10,10,10));
    }

    private void setupBlockAnimation(int fromX, int toX, Node node) {
        tt.setFromX(fromX);
        tt.setToX(toX);
        tt.setNode(node);
    }

    //endregion

    //region Events methods
    private void openHyperLink(ActionEvent event, String URL){
        Hyperlink link = (Hyperlink) event.getSource();
        final String str = link == null ? "" : link.getText();
        if (str != null){
            try {
                Desktop.getDesktop().browse(new URL(URL).toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchPage(){

        //IF NOT CLIENT ERROR USEFUL?

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    primaryStage.setScene(new Scene(new SearchWindow(primaryStage),600,600));
                    stopBlockResearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void lastBlocksPage() {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    primaryStage.setScene(new Scene(new LastBlocks(primaryStage),600,400));
                    stopBlockResearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void walletPage(){
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    primaryStage.setScene(new Scene(new SmallWallet(primaryStage),600,600));
                    stopBlockResearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //endregion

}

