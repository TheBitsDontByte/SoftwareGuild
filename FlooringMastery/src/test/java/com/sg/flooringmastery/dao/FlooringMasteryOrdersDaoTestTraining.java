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
public class FlooringMasteryOrdersDaoTestTraining {

    //FlooringMasteryOrdersDao fm = new FlooringMasteryOrdersDaoFileProductionImpl("testOrders");
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    FlooringMasteryOrdersDao fm = ctx.getBean("ordersTrainingDao", FlooringMasteryOrdersDao.class);

    Orders testOrder = new Orders();
    LocalDate testDate = LocalDate.parse("2018-12-12");

    public FlooringMasteryOrdersDaoTestTraining() {
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

        testOrder.setOrderNumber(10);
        testOrder.setCustomerName("Andrews");
        testOrder.setStateTax(tax1);
        testOrder.setProduct(product1);
        testOrder.setTotalSQFT(new BigDecimal("24.3"));
        testOrder.setOrderDate(testDate);
        try {
            List<Orders> orders = fm.getAllOrdersForDate(testDate);
            for (Orders order : orders) {
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
            fail("Should not throw an exception " + e.getMessage());
        }

    }

    @Test
    public void testAddRemoveOrder() throws Exception {
//        assertEquals(0, fm.getAllOrdersForDate(testDate).size());

        Orders returnOrder = fm.addOrder(testDate, testOrder);
        assertNull(returnOrder);

        Orders fromDao = fm.removeOrder(testDate, testOrder);
        assertEquals(fromDao, testOrder);

    }

    @Test
    public void testAddingDuplicateOrder() throws Exception {

        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));

        Orders order1 = new Orders();
        order1.setOrderNumber(10);
        order1.setCustomerName("Andrews");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(testDate);

        fm.addOrder(testDate, order1);

        try {
            fm.addOrder(testDate, order1);
            fail("Should not allow duplicate orders");
        } catch (Exception e) {

        }

    }

    @Test
    public void testGetAllOrders() throws Exception {

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
        order1.setOrderDate(testDate);

        fm.addOrder(testDate, order1);
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
        order2.setOrderDate(testDate);

        fm.addOrder(testDate, order2);
        assertEquals(2, fm.getAllOrdersForDate(testDate).size());
        List<Orders> orders = fm.getAllOrdersForDate(testDate);
//THis part wasnt relavent to what I was testing -- keep things as focused / clean as possible.
//        for (Orders order : orders) {
//            fm.removeOrder(testDate, order);
//        }
//        assertEquals(0, fm.getAllOrdersForDate(testDate).size());
    }

    @Test
    public void testEditOrder() throws Exception {
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
        order1.setOrderDate(testDate);
        fm.addOrder(testDate, order1);

        fm.removeOrder(testDate, order1);
        order1.setCustomerName("Chance");
        fm.updateOrder(testDate, order1);

        List<Orders> orders = fm.getAllOrdersForDate(testDate);
        Orders orders2 = orders.get(0);

        assertTrue(order1.equals(orders2));
        assertTrue(order1.getCustomerName().equals("Chance"));

    }

    @Test
    public void testEditFileDifferntDate() throws Exception {
        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));

        Orders order1 = new Orders();
        order1.setOrderNumber(2);
        order1.setCustomerName("Andrews");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(testDate);
        fm.addOrder(testDate, order1);

        Orders order2 = fm.removeOrder(testDate, order1);
        order2.setOrderDate(testDate.plusWeeks(1));
        fm.updateOrder(order2.getOrderDate(), order2);

        Orders fromRemove = fm.removeOrder(testDate.plusWeeks(1), order1);
        System.out.println(order1.getOrderDate());
//        assertFalse(fromRemove.equals(order1));
        assertTrue(fromRemove.equals(order2));
        List<Orders> list = fm.getAllOrdersForDate(testDate);
        assertEquals(0, list.size());
        list = fm.getAllOrdersForDate(testDate.plusWeeks(1));
        assertEquals(0, list.size());
    }

    @Test
    public void testNonExistentFileReadALl() throws Exception {
        try {
            fm.getAllOrdersForDate(testDate.minusDays(100));
            fail("No file for this, must fail");
        } catch (Exception e) {

        }
    }

    @Test
    public void testNonExistentFile() throws Exception {
        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));

        Orders order1 = new Orders();
        order1.setOrderNumber(2);
        order1.setCustomerName("Andrews");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(testDate);
        try {
            fm.removeOrder(testDate.minusDays(2), order1);
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
        order1.setOrderNumber(10);
        order1.setCustomerName("Andrews");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(testDate);
        fm.addOrder(testDate.plusDays(3), order1);

        Orders order2 = new Orders();
        order2.setOrderNumber(40);
        order2.setCustomerName("Chance");
        order2.setStateTax(tax1);
        order2.setProduct(product1);
        order2.setTotalSQFT(new BigDecimal("30.9"));
        order2.setOrderDate(testDate);
        fm.addOrder(testDate.minusWeeks(1), order2);

        Orders order3 = new Orders();
        order3.setOrderNumber(30);
        order3.setCustomerName("Beal");
        order3.setStateTax(tax1);
        order3.setProduct(product1);
        order3.setTotalSQFT(new BigDecimal("200"));
        order3.setOrderDate(testDate);
        fm.addOrder(testDate.plusDays(8), order3);

        Orders newOrder = fm.removeOrder(testDate.plusDays(8), order3);
        newOrder.setTotalSQFT(new BigDecimal("3400"));
        fm.addOrder(order3.getOrderDate(), order3);

        fm.addOrder(order3.getOrderDate(), order2);

        assertEquals(2, fm.getAllOrdersForDate(order3.getOrderDate()).size());

    }

}
