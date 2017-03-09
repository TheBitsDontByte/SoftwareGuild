/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class FlooringMasteryOrdersDaoFileProductionImpl implements FlooringMasteryOrdersDao {

    private Map<Integer, Orders> ordersForDate;
    private final String DELIM = ",";
    private final String FILE_PREFIX;
    private final String FILE_HEADER;

    public FlooringMasteryOrdersDaoFileProductionImpl(String filePrefix, String fileHeader) {
        ordersForDate = new HashMap<>();
        this.FILE_PREFIX = filePrefix;
        this.FILE_HEADER = fileHeader;

    }

    @Override
    public Orders addOrder(LocalDate date, Orders order) throws FMPersistenceException, FMDuplicateOrderNumberException, FMInvalidDataException {
        if (date == null || order == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        readFile(date);
        if (ordersForDate.containsKey(order.getOrderNumber())) {
            throw new FMDuplicateOrderNumberException("Duplicate Order Number Detected. Try Again, Contact Admin If Problem Persits");
        }
        Orders returnOrder = ordersForDate.put(order.getOrderNumber(), order);
        writeFile(date);

        return returnOrder;
    }

    //Get all data from all files from stream
//    public List<Orders> getAllOrdersAllDates() throws FMPersistenceException {
//        File folder = new File("Orders");
//        File[] listOfFiles = folder.listFiles();
//        List<Orders> allOrders = new ArrayList<>();
//        for (File file : listOfFiles) {
//
//
//
//        }
//
//    }
    @Override
    public Orders removeOrder(LocalDate date, Orders order)
            throws FMPersistenceException,
            FMInvalidDataException,
            FMOrderNotFoundException {
        if (date == null || order == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        checkForFile(date);
        readFile(date);
        Orders returnOrder = ordersForDate.remove(order.getOrderNumber());
        if (returnOrder == null) {
            throw new FMOrderNotFoundException("Couldn't Find Order. Please Try Again");
        }
        writeFile(date);
        return returnOrder;
    }

    @Override
    public Orders updateOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException {
        if (date == null || order == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        readFile(date);
        Orders returnOrder = ordersForDate.put(order.getOrderNumber(), order);
        writeFile(date);
        return returnOrder;

    }

    @Override
    public List<Orders> getAllOrdersForDate(LocalDate date) throws FMPersistenceException, FMInvalidDataException {
        //Need to read file to get orders from date
        if (date == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        checkForFile(date);
        readFile(date);
        return new ArrayList<>(ordersForDate.values());

    }

    private String getFileName(LocalDate date) {
        String formatDate = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        return FILE_PREFIX + "_" + formatDate + ".txt";

    }

    private boolean checkForFile(LocalDate date) throws FMPersistenceException {

        File file = new File(getFileName(date));

        if (!file.isFile()) {
            throw new FMPersistenceException("Couldn't Find Orders For Specified Date. Please Try Again.");
        }
        return true;
    }

    private void readFile(LocalDate date) throws FMPersistenceException {
        File file = new File(getFileName(date));

        Scanner scan;

        ordersForDate.clear();
        try {
            //Checks in place to deal with where a file MUST be present
            //This is for add/update when a new date is chosen
            if (!file.isFile()) {
                file.createNewFile();
                return;
            }

            scan = new Scanner(new BufferedReader(new FileReader(file)));
            while (scan.hasNextLine()) {
                String currLine = scan.nextLine();
                //Todo: Think about a better way to set up my skip for the header line
                if (currLine.contains("OrderNumber")) {
                    continue;
                }
                //0=OrderNumber,1=CustomerName, 2=State, 3=TaxRate, 4=ProductType, 5=Area, 6=CostPerSquareFoot,
                //7=LaborCostPerSquareFoot, 8=MaterialCost, 9=LaborCost, 10=Tax, 11=Total
                String[] tokens = currLine.split(DELIM);
                Products currProduct = new Products();
                currProduct.setProductName(tokens[4]);
                currProduct.setCostSQFT(new BigDecimal(tokens[6]));
                currProduct.setLaborCostSQFT(new BigDecimal(tokens[7]));

                Taxes currTax = new Taxes();
                currTax.setState(tokens[2]);
                currTax.setTaxes(new BigDecimal(tokens[3]));

                Orders currOrder = new Orders();
                currOrder.setOrderNumber(Integer.parseInt(tokens[0]));
                currOrder.setCustomerName(tokens[1].replace('\u0375', ','));
                currOrder.setTotalSQFT(new BigDecimal(tokens[5]));
                currOrder.setOrderDate(date);
                currOrder.setStateTax(currTax);
                currOrder.setProduct(currProduct);
                ordersForDate.put(currOrder.getOrderNumber(), currOrder);
            }
        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't load data. Contact System Admin.");
        }
        scan.close();

    }

    private void writeFile(LocalDate date) throws FMPersistenceException {
        File file = new File(getFileName(date));

        if (!file.isFile() && ordersForDate.size() > 0) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new FMPersistenceException("Couldn't Create New Record For Date. Contact System Admin");
            }
        }

        if (file.isFile() && ordersForDate.isEmpty()) {
            file.delete();
            return;
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println(FILE_HEADER);
            for (Orders order : ordersForDate.values()) {

                String outString
                        = order.getOrderNumber() + DELIM
                        + order.getCustomerName().replace(',', '\u0375') + DELIM
                        + order.getStateTax().getState() + DELIM
                        + order.getStateTax().getTaxes() + DELIM
                        + order.getProduct().getProductName() + DELIM
                        + order.getTotalSQFT() + DELIM
                        + order.getProduct().getCostSQFT() + DELIM
                        + order.getProduct().getLaborCostSQFT() + DELIM
                        + order.getTotalMaterialCost() + DELIM
                        + order.getTotalLaborCost() + DELIM
                        + order.getTotalTax() + DELIM
                        + order.getTotalCost();

                out.println(outString);
                out.flush();
            }

        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't Write Data. Contact System Admin");
        }

    }

}
