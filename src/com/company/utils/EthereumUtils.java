package com.company.utils;

import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class EthereumUtils {

    public static boolean isAddress(String add){
        return(add.length() == 42 && StringUtils.isAlphanumeric(add) && (add.startsWith("0x")));
    }

    public static boolean isHash(String hash){
        return (hash.length() == 66 && StringUtils.isAlphanumeric(hash) && (hash.startsWith("0x")));
    }

    public static boolean isBlock(String block, Web3j w){

        BigInteger currBlock = BigInteger.ZERO;

        try {
            currBlock = w.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).sendAsync().get().getBlock().getNumber();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            if (new BigInteger(block).compareTo(currBlock) < 1)
                return true;
            else
                throw new NumberFormatException();
        } catch(NumberFormatException e){
            return false;
        }
    }
}
