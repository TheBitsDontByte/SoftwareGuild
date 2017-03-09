/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Products;
import java.math.BigDecimal;
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
public class FlooringMasteryProductDaoTest {

    FlooringMasteryProductDao fm;

    public FlooringMasteryProductDaoTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        fm = ctx.getBean("productsDao", FlooringMasteryProductDao.class);

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProductInfo method, of class FlooringMasteryProductDao.
     */
    @Test
    public void testGetProductInfo() throws Exception {
        List<Products> products = fm.getProductInfo();
        assertEquals(4, products.size());
        Products prod = null;
        for (Products product : products) {
            if (product.getProductName().equals("Wood")) {
                prod = product;
            }
        }
        assertEquals(new BigDecimal("5.15"), prod.getCostSQFT());

    }

}
