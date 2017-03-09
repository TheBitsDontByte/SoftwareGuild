/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Taxes;
import java.math.BigDecimal;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chris
 */
public class FlooringMasteryTaxDaoTest {

    FlooringMasteryTaxDao fm;

    public FlooringMasteryTaxDaoTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        fm = ctx.getBean("taxesDao", FlooringMasteryTaxDao.class);
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
     * Test of getTaxInfo method, of class FlooringMasteryTaxDao.
     */
    @Test
    public void testGetTaxInfo() throws Exception {
        //4 entries in the file. One "State,TaxRate" template at top;
        List<Taxes> taxes = fm.getTaxInfo();
        assertEquals(4, taxes.size());
        Taxes ohTax = null;
        for (Taxes tax : taxes) {
            if (tax.getState().equals("OH")) {
                ohTax = tax;
            }
        }
        assertNotNull(ohTax);
        assertEquals(new BigDecimal("6.25"), ohTax.getTaxes());

    }

}
