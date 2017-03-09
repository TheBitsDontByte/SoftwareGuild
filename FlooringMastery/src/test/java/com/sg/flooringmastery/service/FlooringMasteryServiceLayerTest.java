/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryOrdersDao;
import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
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
public class FlooringMasteryServiceLayerTest {

    FlooringMasteryServiceLayer service;
    ApplicationContext ctx;
    Orders testOrder = new Orders();
    LocalDate testDate = LocalDate.parse("2018-01-01");

    public FlooringMasteryServiceLayerTest() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", FlooringMasteryServiceLayer.class);

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
        testOrder.setCustomerName("TesterMcTesty");
        testOrder.setStateTax(tax1);
        testOrder.setProduct(product1);
        testOrder.setTotalSQFT(new BigDecimal("24.3"));
        testOrder.setOrderDate(testDate);

        try {
            for (Orders order : service.getAllOrdersForDate(testDate)) {
                service.removeOrder(order);
            }
        } catch (Exception e) {
            System.out.println("Error in setup. " + e.getMessage());
        }

    }

    @After
    public void tearDown() {

    }

    //Tests Below \/  \/  \/
    @Test
    public void testSwitchMode() throws Exception {
        assertFalse(service.getMode());
        service.setMode(ctx.getBean("ordersTrainingDaoSTUB", FlooringMasteryOrdersDao.class));
        assertTrue(service.getMode());
    }

    @Test
    public void testGetAllOrders() throws Exception {

        service.addNewOrder(testOrder);

        List<Orders> list = service.getAllOrdersForDate(testDate);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetProductInfo() throws Exception {
        List<Products> list = service.getAllProducts();
        for (Products products : list) {
            if (products.getProductName().equals("Wood")) {
                assertEquals(new BigDecimal("5.15"), products.getCostSQFT());
            }
        }
        int lineCount = 0;
        try (Scanner scan = new Scanner(new BufferedReader(new FileReader("AppData/Products.txt")))) {
            while (scan.hasNextLine()) {
                scan.nextLine();
                lineCount++;
            }
        } catch (Exception e) {
            System.out.println("COULDNT READ FILE, CHECK LOCATION");
        }

        //lineCount -1 because of the header.
        assertEquals(lineCount - 1, list.size());
    }

    @Test
    public void testGetTaxInfo() throws Exception {
        List<Taxes> list = service.getAllTaxInfo();
        for (Taxes taxes : list) {
            if (taxes.getState().equals("OH")) {
                assertEquals(new BigDecimal("6.25"), taxes.getTaxes());
            }
        }
        assertEquals(4, list.size());
    }

    @Test
    public void testGetUniqueID() throws Exception {
        int id = service.getUniqueID();
        assertTrue(id > 0);
        int id2 = service.getUniqueID();
        assertEquals(id + 1, id2);
    }

    @Test
    public void testAddInProductionMode() throws Exception {
        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));
        Orders order1 = new Orders();
        order1.setOrderNumber(1);
        order1.setCustomerName("TesterMcTesty");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(LocalDate.parse("2017-03-03"));

        service.addNewOrder(order1);
        //visual check, file was created and has correct info
        try {
            service.addNewOrder(order1);
            fail("Should not be able to get here");
        } catch (Exception e) {

        }
        service.removeOrder(order1);
    }

    @Test
    public void testInvalidOrders() throws Exception {
        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));

        Orders order1 = new Orders();
        order1.setOrderNumber(1);
        try {
            service.addNewOrder(order1);
            fail("Incomplete order, must not get here");
        } catch (Exception e) {

        }

        order1.setCustomerName("TesterMcTesty");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        try {
            service.addNewOrder(order1);
            fail("Incomplete order, must not get here");
        } catch (Exception e) {

        }
        order1.setOrderDate(LocalDate.parse("2017-03-03"));
        assertNull(service.addNewOrder(order1));
        service.removeOrder(order1);
    }

    @Test
    public void testAddRemoveInTrainingMode() throws Exception {
        service.setMode(ctx.getBean("ordersTrainingDao", FlooringMasteryOrdersDao.class));
        Products product1 = new Products();
        product1.setCostSQFT(new BigDecimal("1.25"));
        product1.setLaborCostSQFT(new BigDecimal("1.25"));
        product1.setProductName("Wood");

        Taxes tax1 = new Taxes();
        tax1.setState("TX");
        tax1.setTaxes(new BigDecimal("8.25"));
        Orders order1 = new Orders();
        order1.setOrderNumber(1);
        order1.setCustomerName("TesterMcTesty");
        order1.setStateTax(tax1);
        order1.setProduct(product1);
        order1.setTotalSQFT(new BigDecimal("24.3"));
        order1.setOrderDate(LocalDate.parse("2017-03-03"));

        service.addNewOrder(order1);
        //visual check, file was created and has correct info
        try {
            service.addNewOrder(order1);
            fail("Should not be able to get here");
        } catch (Exception e) {

        }
//        service.removeOrder(order1);

    }

}
