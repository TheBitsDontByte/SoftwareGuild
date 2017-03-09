/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chris
 */
public class FlooringMasteryOrdersDaoTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    FlooringMasteryOrdersDao fm = ctx.getBean("ordersProductionDao", FlooringMasteryOrdersDao.class);

    Orders testOrder = new Orders();
    LocalDate testDate = LocalDate.parse("2018-12-12");

    public FlooringMasteryOrdersDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {

        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));

        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Andrews");
        testOrder.setStateTax(tax1);
        testOrder.setProduct(product1);
        testOrder.setTotalSQFT(new BigDecimal("24.3"));
        testOrder.setOrderDate(testDate);

        try {
            List<Orders> orders = fm.getAllOrdersForDate(testDate);
            for (Orders order : orders) {
                System.out.println("Removing");
                fm.removeOrder(testDate, order);
            }
        } catch (Exception e) {
            System.out.println("error setting up");
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddOrder() throws Exception {
        try {

            fm.addOrder(testOrder.getOrderDate(), testOrder);
        } catch (Exception e) {
            fail("No reason to fail, check error" + e.getMessage());
        }

    }

    @Test
    public void testAddRemoveOrder() throws Exception {

        Orders returnOrder = fm.addOrder(testDate, testOrder);
        assertNull(returnOrder);

        Orders fromDao = fm.removeOrder(testDate, testOrder);
        assertEquals(fromDao, testOrder);

    }

    @Test
    public void testAddingDuplicateOrder() throws Exception {

        fm.addOrder(testDate, testOrder);

        try {
            fm.addOrder(testDate, testOrder);
            fail("Should not allow duplicate orders");
        } catch (Exception e) {

        }

    }

    @Test
    public void testGetAllOrders() throws Exception {

        fm.addOrder(testDate, testOrder);
        assertEquals(1, fm.getAllOrdersForDate(testDate).size());

        Products product2 = new Products();
        product2.setCostSQFT(new BigDecimal("1.25"));
        product2.setLaborCostSQFT(new BigDecimal("1.25"));
        product2.setProductName("Wood");

        Taxes tax2 = new Taxes();
        tax2.setState("TX");
        tax2.setTaxes(new BigDecimal("8.25"));

        Orders order2 = new Orders();
        order2.setOrderNumber(2);
        order2.setCustomerName("Andrews");
        order2.setStateTax(tax2);
        order2.setProduct(product2);
        order2.setTotalSQFT(new BigDecimal("24.3"));
        order2.setOrderDate(LocalDate.now());

        fm.addOrder(testDate, order2);
        assertEquals(2, fm.getAllOrdersForDate(testDate).size());

    }

    @Test
    public void testEditOrder() throws Exception {

        fm.addOrder(testDate, testOrder);

        Orders orderToEdit = fm.removeOrder(testDate, testOrder);
        orderToEdit.setCustomerName("Chance");
        fm.updateOrder(testDate, orderToEdit);

        //List Should only have one, get elemnt 0
        List<Orders> orders = fm.getAllOrdersForDate(testDate);
        Orders editedOrder = orders.get(0);

        assertTrue(editedOrder.getCustomerName().equals("Chance"));

    }

    @Test
    public void testEditFileDifferntDate() throws Exception {

        fm.addOrder(testDate, testOrder);

        Orders orderToEdit = fm.removeOrder(testDate, testOrder);
        orderToEdit.setOrderDate(testDate.plusWeeks(1));
        fm.updateOrder(orderToEdit.getOrderDate(), orderToEdit);

        Orders editedOrder = fm.removeOrder(orderToEdit.getOrderDate(), orderToEdit);
        assertFalse(editedOrder.equals(testOrder));
        assertTrue(editedOrder.equals(orderToEdit));

    }

    @Test
    public void testNonExistentFileReadALl() throws Exception {
        try {
            fm.getAllOrdersForDate(LocalDate.parse("2000-01-01"));
            fail("No file for this, must fail");
        } catch (Exception e) {

        }
    }

    @Test
    public void testNonExistentFile() throws Exception {

        try {
            fm.removeOrder(testDate, testOrder);
            fm.addOrder(testDate, testOrder);
            fail("Should not be able to reach here");
        } catch (Exception e) {

        }

    }

//    @Test
    public void testSetUpAFewFiles() throws Exception {
        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));

        Orders order1 = new Orders();
        order1.setOrderNumber(1);
        order1.setCustomerName("Andrews");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(LocalDate.now());
        fm.addOrder(LocalDate.now().plusDays(3), order1);

        Orders order2 = new Orders();
        order2.setOrderNumber(4);
        order2.setCustomerName("Chance");
        order2.setStateTax(tax1);
        order2.setProduct(product1);
        order2.setTotalSQFT(new BigDecimal("30.9"));
        order2.setOrderDate(LocalDate.now());
        fm.addOrder(LocalDate.now().minusWeeks(1), order2);

        Orders order3 = new Orders();
        order3.setOrderNumber(3);
        order3.setCustomerName("Beal");
        order3.setStateTax(tax1);
        order3.setProduct(product1);
        order3.setTotalSQFT(new BigDecimal("200"));
        order3.setOrderDate(LocalDate.now());
        fm.addOrder(LocalDate.now().plusDays(8), order3);
    }

}
