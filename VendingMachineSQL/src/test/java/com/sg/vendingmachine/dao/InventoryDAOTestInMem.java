/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.model.Item;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chris
 */
public class InventoryDAOTestInMem {

    InventoryDAO dao;

    public InventoryDAOTestInMem() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("test-applicationContext.xml");
        dao = ctx.getBean("inventoryDao", InventoryDAO.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of loadInventory method, of class InventoryDAO.
     */
    @Test
    public void testLoadInventory() throws Exception {
        List<Item> items = dao.loadInventory();
        assertEquals(9, items.size());

    }

    /**
     * Test of getItem method, of class InventoryDAO.
     */
    @Test
    public void testGetItem() throws Exception {
        dao.loadInventory();
        Item item = dao.getItem(1);
        assertEquals(1, item.getItemId());

    }

    /**
     * Test of decreaseItemQuantity method, of class InventoryDAO.
     */
    @Test
    public void testDecreaseItemQuantity() throws Exception {
        dao.loadInventory();
        Item item = dao.getItem(1);

        int quantity = item.getItemQuantity();
        dao.decreaseItemQuantity(1);
        dao.decreaseItemQuantity(1);
        dao.decreaseItemQuantity(1);

        item = dao.getItem(1);
        assertEquals(quantity - 3, item.getItemQuantity());
    }

}
