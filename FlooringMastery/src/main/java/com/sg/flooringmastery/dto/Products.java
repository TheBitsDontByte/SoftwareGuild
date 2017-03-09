/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author chris
 */
public class Products {

    private String productName;
    private BigDecimal costSQFT;
    private BigDecimal laborCostSQFT;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getCostSQFT() {
        return costSQFT;
    }

    public void setCostSQFT(BigDecimal costSQFT) {
        this.costSQFT = costSQFT;
    }

    public BigDecimal getLaborCostSQFT() {
        return laborCostSQFT;
    }

    public void setLaborCostSQFT(BigDecimal laborCostSQFT) {
        this.laborCostSQFT = laborCostSQFT;
    }

    
}
