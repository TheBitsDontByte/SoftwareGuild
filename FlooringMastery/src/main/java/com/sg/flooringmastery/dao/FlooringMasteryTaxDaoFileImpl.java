/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Taxes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class FlooringMasteryTaxDaoFileImpl implements FlooringMasteryTaxDao {

    final String TAX_FILE;

    public FlooringMasteryTaxDaoFileImpl(String TAX_FILE) {
        this.TAX_FILE = TAX_FILE;
    }

    @Override
    public List<Taxes> getTaxInfo() throws FMPersistenceException {
        return readFile();
    }

    private List<Taxes> readFile() throws FMPersistenceException {
        File file = new File(TAX_FILE);
        List<Taxes> taxes = new ArrayList<>();
        Scanner scan;
        try {
            scan = new Scanner(new BufferedReader(new FileReader(file)));
            String template = scan.nextLine();
            while (scan.hasNextLine()) {
                String currLine = scan.nextLine();
                String[] tokens = currLine.split(",");
                //0 = state, 1 = taxrate
                Taxes currTax = new Taxes();
                currTax.setState(tokens[0]);
                currTax.setTaxes(new BigDecimal(tokens[1]));
                taxes.add(currTax);
            }
        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't Accesss Tax Data. Contact System Admin");
        }

        scan.close();

        return taxes;
    }

}
