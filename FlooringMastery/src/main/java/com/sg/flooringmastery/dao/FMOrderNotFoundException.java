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
public class FMOrderNotFoundException extends Exception {

    public FMOrderNotFoundException(String message) {
        super(message);
    }

    public FMOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
