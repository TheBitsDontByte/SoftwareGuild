/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.model.Item;
import java.util.List;

/**
 *
 * @author chris
 */
public interface InventoryDAO {

    public List<Item> loadInventory() throws VendingMachineOperationException;

    public List<Item> getInventory();

    public Item getItem(int itemId);

    public Item decreaseItemQuantity(int itemId);

}
