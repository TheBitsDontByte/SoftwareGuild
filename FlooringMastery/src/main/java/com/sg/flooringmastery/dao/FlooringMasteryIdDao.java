/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

/**
 *
 * @author chris
 */
public interface FlooringMasteryIdDao {

    /**
     * Reads the file that stores most recently used unique id, increments and
     * returns.
     *
     * @return Next available integer for use with orders.
     * @throws FMPersistenceException
     */
    public Integer getNewUniqueID() throws FMPersistenceException;

    public void setCurrentId(int id) throws FMPersistenceException;

}
