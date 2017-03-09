/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryOrdersDao;
import com.sg.flooringmastery.view.MenuChoices;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author chris
 */
public interface FlooringMasteryBeanSelector {

    public FlooringMasteryOrdersDao getBean(MenuChoices choice, ApplicationContext ctx);
}
