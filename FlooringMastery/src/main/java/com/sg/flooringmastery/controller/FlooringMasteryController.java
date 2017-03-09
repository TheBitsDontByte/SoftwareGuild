/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FMDuplicateOrderNumberException;
import com.sg.flooringmastery.dao.FMInvalidDataException;
import com.sg.flooringmastery.dao.FMOrderNotFoundException;
import com.sg.flooringmastery.dao.FMPersistenceException;
import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import com.sg.flooringmastery.service.FlooringMasteryBeanSelectorServiceImpl;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.view.FlooringMasteryView;
import com.sg.flooringmastery.view.MenuChoices;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author chris
 */
public class FlooringMasteryController {

    FlooringMasteryView view;
    FlooringMasteryServiceLayer service;
    FlooringMasteryBeanSelectorServiceImpl beanService;
    ApplicationContext ctx;

    FlooringMasteryController(
            FlooringMasteryView view,
            FlooringMasteryServiceLayer service,
            FlooringMasteryBeanSelectorServiceImpl beanService) {
        this.view = view;
        this.service = service;
        this.beanService = beanService;
    }

    public void run(ApplicationContext ctx) {
        this.ctx = ctx;
        boolean keepRunning = true;
        int menuChoice = 0;

        while (keepRunning) {

            boolean inTrainingMode = service.getMode();
            view.displayMainMenu(inTrainingMode);
            menuChoice = view.getMenuChoice(1, 7);
            view.displayBlankLine();
            try {
                switch (menuChoice) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        saveWork();
                        break;
                    case 6:
                        switchModes();
                        break;
                    case 7:
                        quit();
                        keepRunning = false;
                        break;
                    default:

                }

                view.displayBlankLine();

            } catch (FMInvalidDataException
                    | FMDuplicateOrderNumberException
                    | FMOrderNotFoundException e) {
                displayErrors(e.getMessage());
            } catch (FMPersistenceException e) {
                displayErrors("  **ERROR** " + e.getMessage());
            }

        }
    }

    private void displayOrders() throws FMPersistenceException, FMInvalidDataException {
        view.displayDisplayOrdersBanner();
        view.displayBlankLine();

        LocalDate date = view.getDate();
        //"Q" returns a null, exit on it
        if (date == null) {
            view.displayDisplayOrdersFailBanner();
            return;
        }

        //getAllOrders will throw if no record/file exists
        List<Orders> orders = service.getAllOrdersForDate(date);

        view.displayBlankLine();
        view.displayListOfOrders(orders);
        view.displayBlankLine();
        view.displayDisplayOrdersSuccessBanner();
    }

    private void addOrder() throws FMPersistenceException, FMInvalidDataException, FMDuplicateOrderNumberException {
        view.displayAddOrderBanner(service.getMode());
        List<Products> prodList = service.getAllProducts();
        List<Taxes> taxList = service.getAllTaxInfo();
        Orders currOrder = view.getNewOrder(prodList, taxList);
        if (currOrder == null) {
            view.displayOrderNotAddedBanner();
            return;
        }
        view.displayBlankLine();
        currOrder.setOrderNumber(service.getUniqueID());
        boolean createOrder = view.displayOrderGetConfirmation(currOrder, service.getMode());
        view.displayBlankLine();
        if (!createOrder) {
            view.displayOrderNotAddedBanner();
        } else {
            service.addNewOrder(currOrder);
            view.displayAddOrderSuccessBanner();
        }

    }

    private void displayErrors(String message) {
        view.displayBlankLine();
        view.displayErrorMessage(message);
        view.displayBlankLine();
    }

    private void removeOrder() throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException {
        view.displayRemoveOrderBanner(service.getMode());
        view.displayBlankLine();
        LocalDate date = view.getDate();
        if (date == null) {
            view.displayReturningToMainMenu();
            return;
        }
        view.displayBlankLine();
        List<Orders> orderList = service.getAllOrdersForDate(date);
        view.displayDisplayOrdersBanner();
        view.displayBlankLine();
        view.displayListOfOrders(orderList);
        Orders orderToRemove = view.getOrderToEdit(orderList);
        view.displayBlankLine();
        if (orderToRemove == null) {
            view.displayOrderNotRemovedBanner();
            return;
        }
        if (orderToRemove == null) {
            view.displayReturningToMainMenu();
        }

        boolean removeOrder = view.displayOrderGetConfirmation(orderToRemove, service.getMode());
        view.displayBlankLine();
        if (!removeOrder) {
            view.displayOrderNotRemovedBanner();
        } else {
            service.removeOrder(orderToRemove);
            view.displayRemoveOrderSuccessBanner();
        }
    }

    private void switchModes() throws FMPersistenceException {
        view.displaySwitchModesBanner();
        boolean inTraining = service.getMode();
        MenuChoices choice = view.getModeChoice(inTraining);
        //From beanService, get the correct bean based on menu choice, sets it in ServiceLayer
        service.setMode(beanService.getBean(choice, ctx));
        view.displayBlankLine();
        view.displaySwitchModeSuccessBanner();
    }

    private void saveWork() {
        view.displayWorkSavedBanner(service.getMode());
    }

    private void editOrder() throws FMPersistenceException, FMInvalidDataException, FMOrderNotFoundException, FMDuplicateOrderNumberException {
        view.displayEditOrderBanner(service.getMode());
        view.displayBlankLine();
        LocalDate date = view.getDate();
        if (date == null) {
            view.displayReturningToMainMenu();
            return;
        }
        List<Orders> orderList = service.getAllOrdersForDate(date);

        view.displayBlankLine();
        view.displayListOfOrders(orderList);
        Orders originalOrder = view.getOrderToRemove(orderList);

        view.displayBlankLine();
        if (originalOrder == null) {
            view.displayReturningToMainMenu();
            return;
        }

        List<Products> prodList = service.getAllProducts();
        List<Taxes> taxList = service.getAllTaxInfo();
        Orders editedOrder = view.editOrder(originalOrder, prodList, taxList);
        editedOrder.setOrderNumber(originalOrder.getOrderNumber());
        view.displayBlankLine();
        boolean editOrder = view.displayOrderGetConfirmation(editedOrder, service.getMode());
        if (!editOrder) {
            view.displayBlankLine();
            view.displayEditOrderFailureBanner();
            return;
        } else {

            service.updateRemoveOrder(originalOrder);
            service.updateAddOrder(editedOrder, originalOrder);

            view.displayBlankLine();
            view.displayEditOrderSuccessBanner();
        }

    }

    private void quit() {
        view.displayQuitBanner();
    }

}
