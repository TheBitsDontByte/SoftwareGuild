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
import java.io.IOException;
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
public class FlooringMasteryOrdersDaoFileTrainingImpl implements FlooringMasteryOrdersDao {

    private Map<LocalDate, Map<Integer, Orders>> allOrders;
    private LocalDate currentDate;
    private final String FILE_PREFIX;
    private final String FILE_HEADER;

    public FlooringMasteryOrdersDaoFileTrainingImpl(String filePrefix, String fileHeader) {
        allOrders = new HashMap<>();
        this.currentDate = LocalDate.now();
        this.FILE_PREFIX = filePrefix;
        this.FILE_HEADER = fileHeader;
    }

    @Override
    public Orders addOrder(LocalDate date, Orders order) throws FMPersistenceException, FMDuplicateOrderNumberException, FMInvalidDataException {
        if (date == null || order == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        if (!allOrders.containsKey(date)) {
            allOrders.put(date, new HashMap<>());
            readFile(date);
        }

        if (allOrders.get(date).containsKey(order.getOrderNumber())) {
            throw new FMDuplicateOrderNumberException("Duplicate Order Number Detected. Try Again, Contact Admin If Problem Persits");
        }
        Orders returnOrder = allOrders.get(date).put(order.getOrderNumber(), order);

        return returnOrder;
    }

    @Override
    public Orders removeOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException {
        if (date == null || order == null || !allOrders.containsKey(date)) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }

        checkForFile(date);
        if (!allOrders.containsKey(date)) {
            allOrders.put(date, new HashMap<>());
            readFile(date);
        }
        Orders returnOrder = allOrders.get(date).remove(order.getOrderNumber());

        return returnOrder;
    }

    @Override
    public Orders updateOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException {
        if (date == null || order == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        if (!allOrders.containsKey(date)) {
            allOrders.put(date, new HashMap<>());
            readFile(date);
        }

        Orders returnOrder = allOrders.get(date).put(order.getOrderNumber(), order);

        return returnOrder;

    }

    @Override
    public List<Orders> getAllOrdersForDate(LocalDate date) throws FMPersistenceException, FMInvalidDataException {
        //Need to read file to get orders from date
        if (date == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        checkForFile(date);
        if (!allOrders.containsKey(date)) {
            allOrders.put(date, new HashMap<>());
            readFile(date);
        }
        return new ArrayList<>(allOrders.get(date).values());

    }

    private String getFileName(LocalDate date) {
        String formatDate = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        return FILE_PREFIX + "_" + formatDate + ".txt";

    }

    private boolean checkForFile(LocalDate date) throws FMPersistenceException {
        File file = new File(getFileName(date));

        if (!file.isFile() && !allOrders.containsKey(date)) {
            throw new FMPersistenceException("Couldn't Find Orders For Specified Date. Please Try Again.");
        }
        return true;
    }

    private void readFile(LocalDate date) throws FMPersistenceException {
        File file = new File(getFileName(date));

        if (!file.isFile()) {
            return;
        }

        Scanner scan;

        try {

            scan = new Scanner(new BufferedReader(new FileReader(file)));
            while (scan.hasNextLine()) {
                String currLine = scan.nextLine();
                if (currLine.contains("OrderNumber")) {
                    continue;
                }
                //0=OrderNumber,1=CustomerName, 2=State, 3=TaxRate, 4=ProductType, 5=Area, 6=CostPerSquareFoot,
                //7=LaborCostPerSquareFoot, 8=MaterialCost, 9=LaborCost, 10=Tax, 11=Total
                String[] tokens = currLine.split(",");
                Products currProduct = new Products();
                currProduct.setProductName(tokens[4]);
                currProduct.setCostSQFT(new BigDecimal(tokens[6]));
                currProduct.setLaborCostSQFT(new BigDecimal(tokens[7]));

                Taxes currTax = new Taxes();
                currTax.setState(tokens[2]);
                currTax.setTaxes(new BigDecimal(tokens[3]));

                Orders currOrder = new Orders();
                currOrder.setOrderDate(date);
                currOrder.setOrderNumber(Integer.parseInt(tokens[0]));
                currOrder.setCustomerName(tokens[1]);
                currOrder.setStateTax(currTax);
                currOrder.setProduct(currProduct);
                currOrder.setTotalSQFT(new BigDecimal(tokens[5]));
                Map<Integer, Orders> currMap = new HashMap<>();
                allOrders.get(date).put(currOrder.getOrderNumber(), currOrder);
            }
        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't load data. Contact System Admin.");
        }
        scan.close();

    }
}
