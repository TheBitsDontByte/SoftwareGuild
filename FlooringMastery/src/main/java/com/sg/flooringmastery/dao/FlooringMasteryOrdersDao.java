/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author chris
 */
public interface FlooringMasteryOrdersDao {

    /**
     *
     * @param date Date of order to be pulled up
     * @param order Order to be added
     * @return Order if found, null if not
     * @throws FMPersistenceException
     * @throws FMDuplicateOrderNumberException
     * @throws FMInvalidDataException
     */
    public Orders addOrder(LocalDate date, Orders order) throws FMPersistenceException, FMDuplicateOrderNumberException, FMInvalidDataException;

    /**
     *
     * @param date Date of order to be pulled up
     * @param order Order to be removed
     * @return Order if found, null if not
     * @throws FMPersistenceException
     * @throws FMInvalidDataException
     */
    public Orders removeOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException;

    /**
     *
     * @param date Date of order we edited and are updating in storage
     * @param order Order with updated info
     * @return Original, non-edited order if same date, null if date was edited.
     * @throws FMPersistenceException
     * @throws FMInvalidDataException
     */
    public Orders updateOrder(LocalDate date, Orders order) throws FMPersistenceException, FMInvalidDataException;

    /**
     *
     * @param date Date for which we want all orders returned
     * @return A list of all orders on said date
     * @throws FMPersistenceException
     * @throws FMInvalidDataException
     */
    public List<Orders> getAllOrdersForDate(LocalDate date) throws FMPersistenceException, FMInvalidDataException;

}
