/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
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
public class BankrollDAOTest {

    BankrollDAO dao;

    public BankrollDAOTest() {
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
        dao = ctx.getBean("bankRollDao", BankrollDAO.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addMoney method, of class BankrollDAO.
     */
    @Test
    public void testAddMoney() {
        dao.addMoney(BigDecimal.ONE);
        BigDecimal curr = dao.getCurrentMoney();
        assertTrue(curr.compareTo(new BigDecimal("1")) == 0);
        dao.addMoney(BigDecimal.TEN);
        curr = dao.getCurrentMoney();
        assertTrue(curr.compareTo(new BigDecimal("11")) == 0);
    }

    /**
     * Test of clearAllMoney method, of class BankrollDAO.
     */
    @Test
    public void testClearAllMoney() {
        dao.addMoney(BigDecimal.ONE);
        BigDecimal curr = dao.getCurrentMoney();
        assertTrue(curr.compareTo(new BigDecimal("1")) == 0);
        dao.addMoney(BigDecimal.TEN);
        curr = dao.getCurrentMoney();
        assertTrue(curr.compareTo(new BigDecimal("11")) == 0);
        dao.clearAllMoney();
        curr = dao.getCurrentMoney();
        assertTrue(curr.compareTo(BigDecimal.ZERO) == 0);

    }

    /**
     * Test of getCurrentMoney method, of class BankrollDAO.
     */
    @Test
    public void testGetCurrentMoney() {
    }

    public class BankrollDAOImpl implements BankrollDAO {

        public BigDecimal addMoney(BigDecimal amount) {
            return null;
        }

        public BigDecimal getCurrentMoney() {
            return null;
        }

        public void clearAllMoney() {
        }
    }

}
