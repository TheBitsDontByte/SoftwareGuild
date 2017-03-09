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
public class FMInvalidDataException extends Exception {

    public FMInvalidDataException(String message) {
        super(message);
    }

    public FMInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
