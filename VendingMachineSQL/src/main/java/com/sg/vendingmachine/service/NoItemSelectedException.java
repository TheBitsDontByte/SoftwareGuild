/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.service;

/**
 *
 * @author chris
 */
public class NoItemSelectedException extends Exception {

    public NoItemSelectedException(String message) {
        super(message);
    }

    public NoItemSelectedException(String message, Throwable cause) {
        super(message, cause);
    }

}
