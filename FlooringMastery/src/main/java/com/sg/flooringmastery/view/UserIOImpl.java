/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class UserIOImpl implements UserIO {

    Scanner scan;

//DEPENDS ON PROGRAM IMPLEMENTING 
    final String SPACING = "  ";

    public UserIOImpl() {
        scan = new Scanner(System.in);
    }
////////////////////////////////////////////////////////////////////////////////    

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void print(String message) {
        System.out.print(message);
    }
////////////////////////////////////////////////////////////////////////////////

    @Override
    public double readDouble(String prompt) {
        return getValidDouble(prompt);
    }

    @Override
    public double readDouble(String prompt, double min, double max) {

        boolean isValid = false;
        double number = 0;

        while (!isValid) {

            number = getValidDouble(prompt);
            isValid = isInRangeDouble(number, min, max);

        }
        return number;
    }

    private double getValidDouble(String prompt) {

        double number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            try {
                String input = scan.nextLine();
                number = Double.parseDouble(input);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println(SPACING + "Invalid input\n");
            }

        }

        return number;
    }

    private boolean isInRangeDouble(double number, double min, double max) {

        if (number < min || number > max) {
            System.out.println(SPACING + "Input out of range\n");
            return false;
        }

        return true;

    }
////////////////////////////////////////////////////////////////////////////////

    @Override
    public float readFloat(String prompt) {
        return getValidFloat(prompt);
    }

    @Override
    public float readFloat(String prompt, float min, float max) {

        boolean isValid = false;
        float number = 0;

        while (!isValid) {

            number = getValidFloat(prompt);
            isValid = isInRangeFloat(number, min, max);

        }
        return number;
    }

    private float getValidFloat(String prompt) {

        float number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            try {
                String input = scan.nextLine();
                number = Float.parseFloat(input);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println(SPACING + "Invalid input\n");
            }

        }

        return number;
    }

    private boolean isInRangeFloat(float number, float min, float max) {

        if (number < min || number > max) {
            System.out.println(SPACING + "Input out of range\n");
            return false;
        }

        return true;

    }
////////////////////////////////////////////////////////////////////////////////

    @Override
    public int readInt(String prompt) {
        return getValidInt(prompt);
    }

    @Override
    public int readInt(String prompt, int min, int max) {

        boolean isValid = false;
        int number = 0;

        while (!isValid) {

            number = getValidInt(prompt);
            isValid = isInRangeInt(number, min, max);

        }
        return number;

    }

    private int getValidInt(String prompt) {

        int number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            try {
                String input = scan.nextLine();
                number = Integer.parseInt(input);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println(SPACING + "Invalid input\n");
            }

        }

        return number;
    }

    private boolean isInRangeInt(int number, int min, int max) {

        if (number < min || number > max) {
            System.out.println(SPACING + "Input out of range\n");
            return false;
        }

        return true;

    }
////////////////////////////////////////////////////////////////////////////////

    @Override
    public long readLong(String prompt) {
        return getValidLong(prompt);
    }

    @Override
    public long readLong(String prompt, long min, long max) {

        boolean isValid = false;
        long number = 0;

        while (!isValid) {

            number = getValidLong(prompt);
            isValid = isInRangeLong(number, min, max);

        }
        return number;

    }

    private long getValidLong(String prompt) {

        long number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            try {
                String input = scan.nextLine();
                number = Integer.parseInt(input);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println(SPACING + "Invalid input\n");
            }

        }

        return number;
    }

    private boolean isInRangeLong(long number, long min, long max) {

        if (number < min || number > max) {
            System.out.println(SPACING + "Input out of range\n");
            return false;
        }

        return true;

    }
////////////////////////////////////////////////////////////////////////////////

    @Override
    public String readString(String prompt) {

        String input;
        do {
            System.out.print(prompt);
            input = scan.nextLine();
            input = input.replace("\t", "");
            input = input.trim();

            if (input.length() > 0) {
                break;
            }

            System.out.println(SPACING + "No string entered\n");
        } while (input.length() <= 0);
        return input;
    }

    @Override
    public String readTrimmedString(String prompt) {
        System.out.print(prompt);
        String input = scan.nextLine();
        input = input.replace("\t", "");
        input = input.trim();
        return input.trim();
    }

//////////////////////////////////////////////////////////////////////////////
    @Override
    public void enterToReturn(String prompt) {
        System.out.print(prompt);
        scan.nextLine();
    }

    @Override
    public BigDecimal readCurrency(String prompt, double min, double max) {
        boolean isValid = false;
        BigDecimal currBD = null;
        while (!isValid) {
            Double currDouble = readDouble(prompt, min, max);
            String currString = currDouble.toString();
            if (currString.length() > 4) {
                System.out.println("Invalid input, try again");
                continue;
            }
            currBD = new BigDecimal(currString);
            isValid = true;

        }
        return currBD;

    }

    //MAY CHANGE --- PRINTING OR RETURNING A STRING
    @Override
    public String getCurrencyAsString(BigDecimal currency) {
        //System.out.printf("$%.2f", currency);
        return String.format("$%.2f", currency);

    }

    @Override
    public int readIntForSecretMenu(String prompt, int min, int max) {
        boolean isValid = false;
        int number = 0;

        while (!isValid) {

            number = getValidIntForMenu(prompt);
            if (number == 19847560) {
                return number;
            }
            isValid = isInRangeIntForMenu(number, min, max);

        }
        return number;
    }

    private int getValidIntForMenu(String prompt) {

        int number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            try {
                String input = scan.nextLine();
                number = Integer.parseInt(input);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println(SPACING + "Invalid input\n");
            }

        }

        return number;
    }

    private boolean isInRangeIntForMenu(int number, int min, int max) {

        if (number < min || number > max) {
            System.out.println(SPACING + "Input out of range\n");
            return false;
        }

        return true;

    }

}
