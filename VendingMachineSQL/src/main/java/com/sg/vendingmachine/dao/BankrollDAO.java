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
public interface BankrollDAO {

    public BigDecimal addMoney(BigDecimal amount);

    public BigDecimal getCurrentMoney();

    public void clearAllMoney();
}
