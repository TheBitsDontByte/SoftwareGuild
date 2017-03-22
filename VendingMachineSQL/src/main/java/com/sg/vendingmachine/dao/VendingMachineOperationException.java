/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

/**
 *
 * @author chris
 */
public class VendingMachineOperationException extends Exception {

    public VendingMachineOperationException(String message) {
        super(message);
    }

    public VendingMachineOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
