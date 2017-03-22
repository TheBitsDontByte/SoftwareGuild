/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chris
 */
public class UserData {

    private BigDecimal currentMoney;
    private Map<String, Integer> currentChange;
    private Item currentItem;
    private String message;

    public UserData() {
        currentMoney = BigDecimal.ZERO;
        currentChange = new HashMap<>();
        currentItem = null;
        message = "";
    }

    public BigDecimal getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(BigDecimal userMoney) {
        this.currentMoney = userMoney;
    }

    public Map<String, Integer> getCurrentChange() {
        return currentChange;
    }

    public void setCurrentChange(Map<String, Integer> currentChange) {
        this.currentChange = currentChange;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
