/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chris
 */
public class App {

    public static void main(String[] args) {
        FlooringMasteryController controller;
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        controller = ctx.getBean("controller", FlooringMasteryController.class);
        controller.run(ctx);
    }
}
