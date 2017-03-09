/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class FlooringMasteryOrdersDAOSTUB implements FlooringMasteryOrdersDao {

    List<Orders> orderList;
    final String FILE_PREFIX;
    final String FILE_HEADER;

    public FlooringMasteryOrdersDAOSTUB(String filePrefix, String fileHeader) {
        orderList = new ArrayList<>();
        this.FILE_PREFIX = filePrefix;
        this.FILE_HEADER = fileHeader;

    }

    @Override
    public Orders addOrder(LocalDate date, Orders order) throws FMPersistenceException, FMDuplicateOrderNumberException, FMInvalidDataException {
        for (Orders currOrder : orderList) {
            if (currOrder.getOrderNumber() == currOrder.getOrderNumber()) {
                throw new FMDuplicateOrderNumberException("Duplicate File Added");
            }
        }
        orderList.add(order);
        return null;
    }

    @Override
    public Orders removeOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException {
        Orders returnOrder = null;
        for (Orders orders : orderList) {
            if (order.getOrderNumber() == (orders.getOrderNumber())) {
                returnOrder = orders;
            }
        }
        orderList.remove(returnOrder);
        return returnOrder;
    }

    @Override
    public Orders updateOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException {
        Orders returnOrder = null;
//Todo: This may not be working ... Test further in training mode ...
        for (Orders orders : orderList) {
            if (order.equals(orders)) {
                orderList.add(order);
                returnOrder = orders;
            }
        }
        orderList.remove(returnOrder);
        return returnOrder;
    }

    @Override
    public List<Orders> getAllOrdersForDate(LocalDate date) throws FMPersistenceException, FMInvalidDataException {
        return orderList;
    }

}
