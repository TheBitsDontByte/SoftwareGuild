/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.BankrollDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 *
 * @author chris
 */
public class BankrollService {

    BankrollDAO dao;

    @Inject
    BankrollService(BankrollDAO dao) {
        this.dao = dao;
    }

    public BigDecimal getCurrentAmount() {
        return dao.getCurrentMoney();
    }

    public BigDecimal addMoney(String amount) {
        switch (amount) {
            case "dollar":
                return dao.addMoney(new BigDecimal("1.00"));
            case "quarter":
                return dao.addMoney(new BigDecimal("0.25"));
            case "dime":
                return dao.addMoney(new BigDecimal("0.10"));
            default:
                return dao.addMoney(new BigDecimal("0.05"));
        }
    }

    public Map<String, Integer> makeChange(BigDecimal currMoney, BigDecimal price) throws InsufficientFundsException {
        if (currMoney.compareTo(price) < 0) {
            BigDecimal amountNeeded = price.subtract(currMoney);
            throw new InsufficientFundsException("Please insert $" + amountNeeded);
        }

        BigDecimal change = currMoney.subtract(price);
        change.setScale(2, RoundingMode.HALF_UP);
        int quarters = 0;
        int dimes = 0;
        int nickels = 0;
        int pennies = 0;

        while (change.compareTo(new BigDecimal("0.25")) >= 0) {
            quarters++;
            change = change.subtract(new BigDecimal("0.25")).setScale(2, RoundingMode.HALF_UP);;
        }
        while (change.compareTo(new BigDecimal("0.10")) >= 0) {
            dimes++;
            change = change.subtract(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        }
        while (change.compareTo(new BigDecimal("0.05")) >= 0) {
            nickels++;
            change = change.subtract(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        }
        while (change.compareTo(new BigDecimal("0.01")) >= 0) {
            pennies++;
            change = change.subtract(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_UP);
        }

        Map<String, Integer> currChange = new HashMap<>();
        currChange.put("quarters", quarters);
        currChange.put("dimes", dimes);
        currChange.put("nickels", nickels);
        currChange.put("pennies", pennies);

        dao.clearAllMoney();
        return currChange;
    }

    public String getChange(Map<String, Integer> currChange) {
        String change = "";
        if (currChange.isEmpty()) {
            return "";
        }
        if (currChange.get("quarters") > 0) {
            change += "Quarters - " + currChange.get("quarters") + " ";
        }
        if (currChange.get("dimes") > 0) {
            change += "Dimes - " + currChange.get("dimes") + " ";
        }
        if (currChange.get("nickels") > 0) {
            change += "Nickels - " + currChange.get("nickels") + " ";
        }
        if (currChange.get("pennies") > 0) {
            change += "Pennies - " + currChange.get("pennies") + " ";
        }
        currChange.clear();
        return change;
    }

}
