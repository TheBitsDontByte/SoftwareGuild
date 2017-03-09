/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Products;
import java.io.BufferedReader;
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
public class FlooringMasteryProductDaoFileImpl implements FlooringMasteryProductDao {

    final String PRODUCT_FILE;

    public FlooringMasteryProductDaoFileImpl(String PRODUCT_FILE) {
        this.PRODUCT_FILE = PRODUCT_FILE;
    }

    @Override
    public List<Products> getProductInfo() throws FMPersistenceException {
        return readFile();
    }

    private List<Products> readFile() throws FMPersistenceException {

        List<Products> prods = new ArrayList<>();

        try (Scanner scan = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)))) {
            //Burn the Template line. ProductType=0, CostPerSquareFoot=1, LaborCostPerSquareFoot=2
            scan.nextLine();
            while (scan.hasNextLine()) {
                String currLine = scan.nextLine();
                String[] tokens = currLine.split(",");
                Products product = new Products();
                product.setProductName(tokens[0]);
                product.setCostSQFT(new BigDecimal(tokens[1]));
                product.setLaborCostSQFT(new BigDecimal(tokens[2]));
                prods.add(product);
            }

            scan.close();

        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't Access Product Data. Contact System Admin");
        }
        return prods;
    }

}
