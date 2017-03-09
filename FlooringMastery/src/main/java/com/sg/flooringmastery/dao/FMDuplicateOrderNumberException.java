/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

/**
 *
 * @author chris
 */
public class FMDuplicateOrderNumberException extends Exception {

    public FMDuplicateOrderNumberException(String message) {
        super(message);
    }

    public FMDuplicateOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }

}
