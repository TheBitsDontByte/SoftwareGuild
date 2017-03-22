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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chris
 */
public class InventoryDAOTest {

    InventoryDAO dao;

    public InventoryDAOTest() {
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

    @Test
    public void testGet() {
        List<Item> inventory = dao.getInventory();
        assertEquals(9, inventory.size());
    }

    @Test
    public void testGetItem1() {
        Item item = dao.getItem(1);
        assertNotNull(item);
        assertEquals(10, item.getItemQuantity());
        assertTrue("Cake".equals(item.getItemName()));

    }

    @Test
    public void testDecreaseStock() {
        Item item = dao.getItem(4);
        int currStock = item.getItemQuantity();
        dao.decreaseItemQuantity(item.getItemId());
        dao.decreaseItemQuantity(item.getItemId());
        dao.decreaseItemQuantity(item.getItemId());
        item = dao.getItem(4);
        assertEquals(currStock - 3, item.getItemQuantity());

    }

}
