/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author chris
 */
public class Orders {

    private LocalDate orderDate;
    private int orderNumber;
    private String customerName;
    //State and TaxRage
    private Taxes stateTax;
    //Product, laborCost, materialCost
    private Products product;
    private BigDecimal totalSQFT;

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Taxes getStateTax() {
        return stateTax;
    }

    public void setStateTax(Taxes stateTax) {
        this.stateTax = stateTax;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public BigDecimal getTotalSQFT() {
        return totalSQFT;
    }

    public void setTotalSQFT(BigDecimal totalSQFT) {
        this.totalSQFT = totalSQFT;
    }

    public BigDecimal getTotalMaterialCost() {
        return product.getCostSQFT().multiply(totalSQFT).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalLaborCost() {
        return product.getLaborCostSQFT().multiply(totalSQFT).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubTotal() {
        return getTotalMaterialCost().add(getTotalLaborCost()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalTax() {
        BigDecimal tax = stateTax.getTaxes().divide(new BigDecimal("100"));
        return getSubTotal().multiply(tax).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalCost() {
        return getSubTotal().add(getTotalTax()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.orderDate);
        hash = 71 * hash + this.orderNumber;
        hash = 71 * hash + Objects.hashCode(this.customerName);
        hash = 71 * hash + Objects.hashCode(this.stateTax);
        hash = 71 * hash + Objects.hashCode(this.product);
        hash = 71 * hash + Objects.hashCode(this.totalSQFT);

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Orders other = (Orders) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (!Objects.equals(this.stateTax.getState(), other.stateTax.getState())) {
            return false;
        }
        if (!Objects.equals(this.stateTax.getTaxes(), other.stateTax.getTaxes())) {
            return false;
        }
        if (!Objects.equals(this.product.getCostSQFT(), other.product.getCostSQFT())) {
            return false;
        }
        if (!Objects.equals(this.product.getLaborCostSQFT(), other.product.getLaborCostSQFT())) {
            return false;
        }
        if (!Objects.equals(this.product.getProductName(), other.product.getProductName())) {
            return false;
        }
        if (!Objects.equals(this.totalSQFT, other.totalSQFT)) {
            return false;
        }

        return true;
    }

}
