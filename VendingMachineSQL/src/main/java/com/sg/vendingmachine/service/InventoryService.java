/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.InventoryDAO;
import com.sg.vendingmachine.dao.VendingMachineOperationException;
import com.sg.vendingmachine.model.Item;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author chris
 */
public class InventoryService {

    InventoryDAO dao;

    @Inject
    public InventoryService(InventoryDAO inventoryDao) throws VendingMachineOperationException {
        this.dao = inventoryDao;
        dao.loadInventory();
    }

    public List<Item> getInventory() {
        return dao.getInventory();
    }

    public Item setCurrItem(int itemId) throws OutOfStockException {
        Item item = dao.getItem(itemId);
        if (item.getItemQuantity() <= 0) {
            throw new OutOfStockException("Sold Out !!!");
        }

        return item;

    }

    public void vendItem(Item item) {
        dao.decreaseItemQuantity(item.getItemId());
    }

}
