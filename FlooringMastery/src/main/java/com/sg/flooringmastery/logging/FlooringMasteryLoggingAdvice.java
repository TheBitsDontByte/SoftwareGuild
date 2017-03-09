/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.logging;

import com.sg.flooringmastery.dao.FlooringMasteryAuditDaoFileImpl;
import com.sg.flooringmastery.dto.Orders;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.aspectj.lang.JoinPoint;

/**
 *
 * @author chris
 */
public class FlooringMasteryLoggingAdvice {

    FlooringMasteryAuditDaoFileImpl dao;
    boolean inTrainingMode = false;

    public FlooringMasteryLoggingAdvice(FlooringMasteryAuditDaoFileImpl dao) {
        this.dao = dao;
    }

    public void writeAddOrderAudit(JoinPoint jp) {
        //1 Parameter: Orders
        if (inTrainingMode) {
            return;
        }
        Orders order = (Orders) jp.getArgs()[0];

        String audit = LocalDateTime.now().toString();
        audit += ": Order Added -- OrderID: " + order.getOrderNumber();
        audit += " || Customer Name: " + order.getCustomerName();
        audit += " || Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        audit += " || State: " + order.getStateTax().getState();
        audit += " || Product: " + order.getProduct().getProductName();
        audit += " || Total: $" + order.getTotalCost();

        dao.writeEntry(audit);
    }

    public void writeSwitchModesAudit(JoinPoint jp, boolean trainingMode) {
        String audit = LocalDateTime.now().toString();
        if (inTrainingMode != trainingMode) {
            audit += trainingMode ? ": Switched to training mode" : ": Switched to production mode";
            inTrainingMode = trainingMode;
            dao.writeEntry(audit);
        }

    }

    public void writeRemoveOrderAudit(JoinPoint jp) {
        if (inTrainingMode) {
            return;
        }
        Orders order = (Orders) jp.getArgs()[0];

        String audit = LocalDateTime.now().toString();
        audit += ": Order Removed -- OrderID: " + order.getOrderNumber();
        audit += " || Customer Name: " + order.getCustomerName();
        audit += " || Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        audit += " || State: " + order.getStateTax().getState();
        audit += " || Product: " + order.getProduct().getProductName();
        audit += " || Total: $" + order.getTotalCost();

        dao.writeEntry(audit);

    }

    public void writeEditOrderAudit(JoinPoint jp) {
        if (inTrainingMode) {
            return;
        }

        Orders originalOrder = (Orders) jp.getArgs()[1];
        Orders editedOrder = (Orders) jp.getArgs()[0];

        String originalAudit = "\tOriginal -- Customer Name: " + originalOrder.getCustomerName();
        originalAudit += " || Date: " + originalOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        originalAudit += " || State: " + originalOrder.getStateTax().getState();
        originalAudit += " || Product: " + originalOrder.getProduct().getProductName();
        originalAudit += " || Total: $" + originalOrder.getTotalCost();

        String editAudit = "\t  Edit   -- Customer Name: " + editedOrder.getCustomerName();
        editAudit += " || Date: " + editedOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        editAudit += " || State: " + editedOrder.getStateTax().getState();
        editAudit += " || Product: " + editedOrder.getProduct().getProductName();
        editAudit += " || Total: $" + editedOrder.getTotalCost();

        String audit = LocalDateTime.now().toString() + ": Order " + originalOrder.getOrderNumber() + " Edited: \n";
        audit += originalAudit + "\n";
        audit += editAudit;

        dao.writeEntry(audit);

    }

}
