/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.model.Item;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class InventoryDAOInMemImpl implements InventoryDAO {

    private List<Item> inventory;
    private final String INVENTORY_FILE;
    private final String DELIM = "::";

    public InventoryDAOInMemImpl(String INVENTORY_FILE) {
        inventory = new ArrayList<>();
        this.INVENTORY_FILE = INVENTORY_FILE;
//        this.INVENTORY_FILE = "VendingMachineInventory.txt";
    }

    @Override
    public List<Item> loadInventory() throws VendingMachineOperationException {
        Scanner in;
        int id = 1;
        inventory.clear();

        try {
            in = new Scanner(new BufferedReader(new FileReader(INVENTORY_FILE)));
            while (in.hasNextLine()) {
                Item item = new Item();

                String currLine = in.nextLine();
                String[] tokens = currLine.split(DELIM);

                item.setItemId(id++);
                item.setItemName(tokens[0]);
                item.setItemCost(new BigDecimal(tokens[1]));
                item.setItemQuantity(Integer.parseInt(tokens[2]));

                inventory.add(item);
            }
        } catch (FileNotFoundException e) {
            throw new VendingMachineOperationException("Couldnt Load Inventory" + e.getMessage());
        }
        in.close();

        return inventory;

    }

    @Override
    public Item getItem(int itemId) {
        return inventory.get(itemId - 1);
    }

    @Override
    public Item decreaseItemQuantity(int itemId) {
        for (Item item : inventory) {
            if (item.getItemId() == itemId) {
                item.setItemQuantity(item.getItemQuantity() - 1);
                return item;
            }

        }
        return null;

    }

    @Override
    public List<Item> getInventory() {
        return inventory;
    }

}
