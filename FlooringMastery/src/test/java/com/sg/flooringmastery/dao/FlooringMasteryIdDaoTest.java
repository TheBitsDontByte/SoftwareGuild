/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

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
public class FlooringMasteryIdDaoTest {

    //FlooringMasteryIdDao fm = new FlooringMasteryIdDaoFileImpl("testUniqueId.txt");
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    FlooringMasteryIdDao fm = ctx.getBean("uniqueIdDao", FlooringMasteryIdDao.class);

    public FlooringMasteryIdDaoTest() {
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
     * Test of getNewUniqueID method, of class FlooringMasteryIdDao.
     */
    @Test
    public void testGetNewUniqueID() throws Exception {
        int id = fm.getNewUniqueID();
        int id2 = fm.getNewUniqueID();
        assertEquals(id, id2);
        int id3 = fm.getNewUniqueID();
        fm.setCurrentId(id3);
        int id4 = fm.getNewUniqueID();
        assertEquals(id3 + 1, id4);

    }

    @Test
    public void testGetNewUniqueIDLots() throws Exception {
        int id = fm.getNewUniqueID();
        fm.setCurrentId(id);
        int id2 = 0;
        for (int i = 0; i < 10000; i++) {
            id2 = fm.getNewUniqueID();
            fm.setCurrentId(id2);
        }
        assertEquals(id2, id + 10000);

    }

}
