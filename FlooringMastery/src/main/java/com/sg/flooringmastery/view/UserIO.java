/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.view;

import java.math.BigDecimal;

/**
 *
 * @author chris
 */
public interface UserIO {

    void print(String message);

    void println(String message);

    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);

    String readString(String prompt);

    String readTrimmedString(String prompt);

    void enterToReturn(String prompt);

///////////////////// This Vending Machines Implementation
    BigDecimal readCurrency(String prompt, double min, double max);

    String getCurrencyAsString(BigDecimal currency);

    int readIntForSecretMenu(String prompt, int min, int max);
}
