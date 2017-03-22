/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import java.math.BigDecimal;

/**
 *
 * @author chris
 */
public class BankrollDAOInMemImpl implements BankrollDAO {

    BigDecimal currentBankroll;

    public BankrollDAOInMemImpl() {
        currentBankroll = BigDecimal.ZERO;
    }

    @Override
    public BigDecimal addMoney(BigDecimal amount) {
        currentBankroll = currentBankroll.add(amount);
        return currentBankroll;
    }

    @Override
    public void clearAllMoney() {
        currentBankroll = BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getCurrentMoney() {
        return currentBankroll;
    }

}
