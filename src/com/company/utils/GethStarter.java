package com.company.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GethStarter {
    public static void main(String[] args) throws IOException, InterruptedException {
        //        Credentials c = Credentials.create("0xB8c77482e45F1F44dE1745F52C74426C631bDD52");
        //        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        //        ERC20 erc20 = ERC20.load("0xB8c77482e45F1F44dE1745F52C74426C631bDD52", web3j, c, new DefaultGasProvider());
        //        try {
        //            System.out.println(erc20.balanceOf("0xb4b3351918a9bedc7d386c6a685c42e69920b34d").sendAsync().get());
        //        } catch (InterruptedException | ExecutionException e) {
        //            e.printStackTrace();
        //        }
        Process process = Runtime.getRuntime().exec("geth --syncmode \"light\" --datadir=\"D:/Ethereum/geth\" --datadir.ancient=\"D:/Ethereum/geth/ancient\" --http");
        LogStreamReader lsr = new LogStreamReader(process.getErrorStream());
        Thread thread = new Thread(lsr, "LogStreamReader");
        thread.start();
        Thread.sleep(10000);
        process.destroy();
        thread.interrupt();



    }

    public static class LogStreamReader implements Runnable {

        private BufferedReader reader;

        public LogStreamReader(InputStream is) {
            this.reader = new BufferedReader(new InputStreamReader(is));
        }

        public void run() {
            try {
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
