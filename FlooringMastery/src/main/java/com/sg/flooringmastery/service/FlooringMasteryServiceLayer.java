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
import com.sg.flooringmastery.dao.FlooringMasteryOrdersDao;
import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author chris
 */
public interface FlooringMasteryServiceLayer {

    /**
     * Method to set training/production modes. Will Trigger the use of one DAO
     * as opposed to another. Will start as false.
     *
     * @param trainingMode
     */
    public boolean setMode(FlooringMasteryOrdersDao ordersDao) throws FMPersistenceException;

    /**
     * Used to check which mode the program is currently in. True is training
     * mode, false is production.
     *
     * @return True indicates trainingMode is on. False indicates production
     * mode is on.
     */
    public boolean getMode();

    /**
     * Returns a list of all orders found on said date, null if no orders.
     *
     * @param date Date for the orders.
     * @return List of Orders or null
     * @throws FMPersistenceException
     */
    public List<Orders> getAllOrdersForDate(LocalDate date) throws FMPersistenceException, FMInvalidDataException;

    /**
     * Returns a list of all product info, should NOT be null. Used to setup the
     * menu choices.
     *
     * @return List of all products. Null needs to throw an exception.
     * @throws FMPersistenceException
     */
    public List<Products> getAllProducts() throws FMPersistenceException, FMInvalidDataException;

    /**
     * Returns a lit of all tax info, should NOT be null. Used to setup the menu
     * choices.
     *
     * @return List of all products. Null needs to throw an exception.
     * @throws FMPersistenceException
     */
    public List<Taxes> getAllTaxInfo() throws FMPersistenceException, FMInvalidDataException;

    /**
     * Returns a unique ID, used in the creation of new orders.
     *
     * @return App/Order wide unique ID for new orders.
     * @throws FMPersistenceException
     */
    public Integer getUniqueID() throws FMPersistenceException, FMInvalidDataException;

    /**
     * Persists an order. Should be fully defined (filled in ?) order at this
     * point. Throw exception on any field being null/blank.
     *
     * @param order Newly created order to be persisted.
     * @return null if a new order for the day and the previous entry if being
     * updated on the same day
     * @throws FMPersistenceException
     */
    public Orders addNewOrder(Orders order) throws FMPersistenceException, FMInvalidDataException, FMDuplicateOrderNumberException;

    /**
     * Removes an order from storage. Used to edit or permanently delete orders.
     *
     * @param order The order to be removed.
     * @return null if order is not found. Order if successfully removed.
     * @throws FMPersistenceException
     */
    public Orders removeOrder(Orders order) throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException;

    /**
     * Updates an Order. Can either Replace an existing order (for a given date)
     * or be a new order if the date has been udpated.
     *
     * @param order Order that has been removed, edited and is being put back
     * into storage.
     * @return Null.
     * @throws FMPersistenceException
     * @throws FMInvalidDataException
     */
    public Orders updateAddOrder(Orders editedOrder, Orders OriginalOrder) throws FMPersistenceException, FMInvalidDataException;

    /**
     * Remove an order temporarily. Will be replaced with an edited version.
     *
     * @param order
     * @return
     * @throws FMPersistenceException
     * @throws FMInvalidDataException
     */
    public Orders updateRemoveOrder(Orders order) throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException;

}
