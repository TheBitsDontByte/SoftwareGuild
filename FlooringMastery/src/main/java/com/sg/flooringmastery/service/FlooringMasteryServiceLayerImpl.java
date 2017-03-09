/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FMDuplicateOrderNumberException;
import com.sg.flooringmastery.dao.FMInvalidDataException;
import com.sg.flooringmastery.dao.FMOrderNotFoundException;
import com.sg.flooringmastery.dao.FMPersistenceException;
import com.sg.flooringmastery.dao.FlooringMasteryIdDao;
import com.sg.flooringmastery.dao.FlooringMasteryOrdersDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author chris
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    boolean trainingMode;

    FlooringMasteryOrdersDao ordersDao;
    FlooringMasteryProductDao productDao;
    FlooringMasteryTaxDao taxDao;
    FlooringMasteryIdDao idDao;
    int trainingId;

    FlooringMasteryServiceLayerImpl(
            FlooringMasteryOrdersDao ordersDao,
            FlooringMasteryTaxDao taxDao,
            FlooringMasteryProductDao productDao,
            FlooringMasteryIdDao idDao) {

        this.ordersDao = ordersDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.idDao = idDao;
    }

//Todo: Need to clear the map when a switch is maid.
    @Override
    public boolean setMode(FlooringMasteryOrdersDao ordersDao) {
        if (!this.ordersDao.equals(ordersDao)) {
            trainingMode = !trainingMode;
        }
        this.ordersDao = ordersDao;
        return trainingMode;
    }

    @Override
    public boolean getMode() {
        return this.trainingMode;
    }

    @Override
    public List<Orders> getAllOrdersForDate(LocalDate date) throws FMPersistenceException, FMInvalidDataException {
        if (date == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again.");
        }
        return ordersDao.getAllOrdersForDate(date);
    }

    @Override
    public List<Products> getAllProducts() throws FMPersistenceException, FMInvalidDataException {
        List<Products> prodList = productDao.getProductInfo();
        if (prodList.isEmpty()) {
            throw new FMInvalidDataException("Invalid Product Data Detected. Contact System Admin");
        }
        return prodList;
    }

    @Override
    public List<Taxes> getAllTaxInfo() throws FMPersistenceException, FMInvalidDataException {
        List<Taxes> taxList = taxDao.getTaxInfo();
        if (taxList.isEmpty()) {
            throw new FMInvalidDataException("Invalid Tax Data Detected. Contact System Admin");
        }
        return taxList;
    }

    @Override
    public Integer getUniqueID() throws FMPersistenceException, FMInvalidDataException {
        if (trainingMode) {

            return trainingId < idDao.getNewUniqueID() ? idDao.getNewUniqueID() : trainingId++;

        } else {

            Integer uniqueID = idDao.getNewUniqueID();
            idDao.setCurrentId(uniqueID);
            if (uniqueID < 1) {
                throw new FMInvalidDataException("Invalid ID Data Detected. Contact System Admin");
            }
            return uniqueID;
        }
    }

    @Override
    public Orders addNewOrder(Orders order) throws FMPersistenceException, FMInvalidDataException, FMDuplicateOrderNumberException {
        checkOrderFields(order);
        return ordersDao.addOrder(order.getOrderDate(), order);
    }

    @Override
    public Orders removeOrder(Orders order) throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException {
        checkOrderFields(order);
        Orders removedOrder = ordersDao.removeOrder(order.getOrderDate(), order);
        if (removedOrder == null) {
            throw new FMOrderNotFoundException("Couldn't Find Order. Please Try Again");
        }
        return removedOrder;
    }

    @Override
    public Orders updateAddOrder(Orders editedOrder, Orders originalOrder) throws FMPersistenceException, FMInvalidDataException {
        checkOrderFields(editedOrder);
        return ordersDao.updateOrder(editedOrder.getOrderDate(), editedOrder);
    }

    @Override
    public Orders updateRemoveOrder(Orders order) throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException {
        checkOrderFields(order);
        Orders removedOrder = ordersDao.removeOrder(order.getOrderDate(), order);
        if (removedOrder == null) {
            throw new FMOrderNotFoundException("Couldn't Find Order. Please Try Again");
        }
        return removedOrder;
    }

    private void checkOrderFields(Orders order) throws FMInvalidDataException {
        if (order == null) {
            throw new FMInvalidDataException("Invalid Data Detected. Please Try Again");
        }
        if (order.getCustomerName().isEmpty()) {
            throw new FMInvalidDataException("Invalid Customer Information Detected. Please Try Again.");
        }
        if (order.getOrderDate() == null) {
            throw new FMInvalidDataException("Invalid Date Detected. Please Try Again.");
        }
        if (order.getOrderNumber() < 1) {
            throw new FMInvalidDataException("Invalid Order Number Detected. Please Try Again.");
        }
        if (order.getProduct() == null || order.getStateTax() == null) {
            throw new FMInvalidDataException("Invalid Customer Information Detected. Please Try Again");
        }
        if (order.getTotalSQFT().compareTo(BigDecimal.ONE) < 1) {
            throw new FMInvalidDataException("Invalid Customer Information Detected. Please Try Again");
        }

    }

}
