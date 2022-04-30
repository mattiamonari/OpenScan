package com.company;

import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
//TODO ENHANCE THIS CLASS

public class TokenInspector {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Credentials c = Credentials.create("0xB8c77482e45F1F44dE1745F52C74426C631bDD52");
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        ERC20 erc20 = ERC20.load("0xB8c77482e45F1F44dE1745F52C74426C631bDD52" , web3j , c , new DefaultGasProvider());
        try {
            BigInteger balance = erc20.balanceOf("0xb4b3351918a9bedc7d386c6a685c42e69920b34d").sendAsync().get();
            String name = erc20.name().sendAsync().get();
            String sym = erc20.symbol().sendAsync().get();
            String contract = web3j.ethGetCode("0x5C69bEe701ef814a2B6a3EDD4B1652CB9cc5aA6f",
                    DefaultBlockParameterName.LATEST).sendAsync().get(30, TimeUnit.SECONDS).getCode();

            System.out.println("Balance of: " + name + "(" + sym + ")" + ": " + balance + "\n Here's the contract " +
                    "binary:"+ contract);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }



    }
}
