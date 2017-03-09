/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 *
 * @author chris
 */
public class FlooringMasteryView {

    UserIO io;
    final String SPACE = "   ";

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public void displayTrainingModeBanner(boolean inTrainingMode) {
        if (inTrainingMode) {
            io.println(SPACE + "<< ## TRAINING MODE -- NO DATA PERSISTENCE ## >>");
        }
    }

    public void displayMainMenu(boolean inTrainingMode) {
        io.println("<<<<Point Of Sale - Floor Masters>>>>");
        displayTrainingModeBanner(inTrainingMode);
        io.println(SPACE + "1. Display Orders");
        io.println(SPACE + "2. Add An Order");
        io.println(SPACE + "3. Edit An Order");
        io.println(SPACE + "4. Remove An Order");
        io.println(SPACE + "5. Save Current Work");
        io.println(SPACE + "6. Switch Modes");
        io.println(SPACE + "7. Quit");

    }

    public int getMenuChoice(int min, int max) {
        return io.readInt(SPACE + ": ", min, max);
    }

    public void displayBlankLine() {
        io.println("");
    }

    public void displayDisplayOrdersBanner() {
        io.println(SPACE + "<<<<Displaying Orders>>>>");
    }

    public LocalDate getDate() {
        String stringDate;
        while (true) {
            stringDate = io.readString(SPACE + "Date(MM/DD/YYYY) or 'Q' to return to main menu: ");
            if (stringDate.equals("Q")) {
                io.println("");
                return null;
            }
            try {
                LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("MM/dd/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT));
                if (date.isAfter(LocalDate.now().plusYears(3))) {
                    io.println(SPACE + "Order Date Must Be Within 3 Years. Try Again\n");
                    continue;
                }
                return date;
            } catch (Exception e) {
                io.println("");
                io.println(SPACE + "Invalid Date Format. Try Again.");
                io.println("");
            }
        }
    }

    public void displayNoResultsFoundBanner(LocalDate date) {
        io.println(SPACE + "No results found for " + date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

    }

    public void displayListOfOrders(List<Orders> orders) {
        for (int i = 0; i < orders.size(); i++) {
            String custDispAndName = String.format(" -- Customer Name: %-10.10s", orders.get(i).getCustomerName());
            String prodDispAndProd = String.format(" || Product: %-9.9s", orders.get(i).getProduct().getProductName());

            io.println(SPACE + "#" + (i + 1)
                    + custDispAndName
                    + prodDispAndProd
                    + " || State: " + orders.get(i).getStateTax().getState()
                    + " || Total Cost: $" + orders.get(i).getTotalCost());
        }
    }

    public void displayDisplayOrdersSuccessBanner() {
        io.println(SPACE + "<<<<All Orders Displayed>>>>");
    }

    public void displayErrorMessage(String message) {
        io.println(SPACE + "<<<<" + message + ">>>>");
    }

    public void displayAddOrderBanner(boolean inTrainingMode) {
        io.println(SPACE + "<<<<<Add An Order>>>>");
        displayTrainingModeBanner(inTrainingMode);

    }

    public Orders getNewOrder(List<Products> prodList, List<Taxes> taxList) {

        //Todo: Think about creating a "validation" layer as mentioned by sara
        Orders order = new Orders();

        displayBlankLine();
        String customerName = getCustomerName();

        displayBlankLine();
        Products prodChoice = getProductChoice(prodList);

        displayBlankLine();
        Taxes taxChoice = getTaxChoice(taxList);

        displayBlankLine();
        BigDecimal sqft = getSquareFeet();

        displayBlankLine();
        LocalDate date = null;
        while (true) {
            date = getDate();
            if (date == null) {
                return null;
            } else if (date.isBefore(LocalDate.now())) {
                io.println(SPACE + "Dates must be in the future, try again.\n");
            } else {
                break;
            }
        }

        order.setCustomerName(customerName);
        order.setStateTax(taxChoice);
        order.setProduct(prodChoice);
        order.setTotalSQFT(sqft);
        order.setOrderDate(date);

        return order;

    }

    public boolean displayOrderGetConfirmation(Orders currOrder, boolean inTrainingMode) {
        io.println(SPACE + "<<<<Order Details>>>>");
        displayTrainingModeBanner(inTrainingMode);
        io.println("");
        io.println(SPACE + "Order ID: " + currOrder.getOrderNumber());
        io.println(SPACE + "Customer: " + currOrder.getCustomerName());
        io.println(SPACE + "Product: " + currOrder.getProduct().getProductName());
        io.println(SPACE + "Order Date: " + currOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        io.println(SPACE + "State: " + currOrder.getStateTax().getState());
        io.println(SPACE + "Square Feet: " + currOrder.getTotalSQFT());
        io.println(SPACE + "Total Labor: $" + currOrder.getTotalLaborCost());
        io.println(SPACE + "Total Material: $" + currOrder.getTotalMaterialCost());
        io.println(SPACE + "Tax: $" + currOrder.getTotalTax());
        io.println(SPACE + "");
        io.println(SPACE + "Grand Total: $" + currOrder.getTotalCost());
        io.println("");
        while (true) {
            String keepOrder = io.readString(SPACE + "Continue? 'Y'es / 'N'o :");
            if (keepOrder.equals("Y")) {
                return true;
            } else if (keepOrder.equals("N")) {
                return false;
            } else {
                io.println("Invalid answer, try again.\n");
            }
        }
    }

    public void displayOrderNotAddedBanner() {
        io.println(SPACE + "<<<<Order Not Added. Returning To Main Menu>>>>");
    }

    public void displayAddOrderSuccessBanner() {
        io.println(SPACE + "<<<<Order Successfully Added. Returning To Main Menu>>>>");
    }

    public void displayDisplayOrdersFailBanner() {
        io.println(SPACE + "<<<<Orders Not Displayed, Returning To Main Menu.>>>>");
    }

    public Orders getOrderToRemove(List<Orders> orders) {
        //-1 to compensate for 0 indexing of arrays
        int choice = io.readInt(SPACE + "Order number to remove or 0 to return to main menu: ", 0, orders.size()) - 1;
        if (choice == -1) {
            return null;
        }
        return orders.get(choice);
    }

    public Orders getOrderToEdit(List<Orders> orders) {
        //-1 to compensate for 0 indexing of arrays
        int choice = io.readInt(SPACE + "Order number to remove, 0 to return to main menu: ", 0, orders.size()) - 1;
        if (choice == -1) {
            return null;
        }
        return orders.get(choice);
    }

    public void displayRemoveOrderBanner(boolean inTrainingMode) {
        io.println(SPACE + "<<<<Remove An Order>>>>");
        displayTrainingModeBanner(inTrainingMode);
    }

    public void displayOrderNotRemovedBanner() {
        io.println(SPACE + "<<<<Order Will Not Be Removed. Returning To Main Menu>>>>");
    }

    public void displayRemoveOrderSuccessBanner() {
        io.println(SPACE + "<<<<Order Successfully Removed. Returning To Main Menu>>>>");
    }

    public void displaySwitchModesBanner() {
        io.println(SPACE + "<<<<Switch Modes>>>>");
    }

    public MenuChoices getModeChoice(boolean inTraining) {
        if (inTraining) {
            io.println(SPACE + "Currently in training mode");
        } else {
            io.println(SPACE + "Currently in production mode");
        }
        io.println(SPACE + "Choose mode:");
        io.println(SPACE + "1. Production Mode");
        io.println(SPACE + "2. Training Mode");

        int choice = io.readInt(SPACE + ": ", 1, 2);
        if (choice == 1) {
            return MenuChoices.productionMode;
        } else {
            return MenuChoices.trainingMode;
        }

    }

    public void displaySwitchModeSuccessBanner() {
        io.println(SPACE + "<<<<Modes Switched. Returning To Main Menu>>>>");
    }

    public void displayWorkSavedBanner(boolean inTrainingMode) {
        if (inTrainingMode) {
            displayTrainingModeBanner(inTrainingMode);

        } else {
            io.println(SPACE + "<<<<All Work Saved. Returning To Main Menu>>>>");
        }
    }

    public void displayEditOrderBanner(boolean inTrainingMode) {
        io.println(SPACE + "<<<<Edit An Order>>>>");
        displayTrainingModeBanner(inTrainingMode);
    }

    public Orders editOrder(Orders origOrder, List<Products> prodList, List<Taxes> taxList) {

        Orders order = new Orders();
        io.println(SPACE + "<<<<Order Details>>>>>");
        this.displayBlankLine();
        order.setCustomerName(editCustomerName(origOrder.getCustomerName()));

        this.displayBlankLine();
        order.setProduct(editProductChoice(prodList, origOrder.getProduct()));

        this.displayBlankLine();
        order.setTotalSQFT(editSquareFeet(origOrder.getTotalSQFT()));

        this.displayBlankLine();
        order.setStateTax(editTaxChoice(taxList, origOrder.getStateTax()));

        this.displayBlankLine();
        order.setOrderDate(editDate(origOrder.getOrderDate()));

        return order;
    }

    private Products getProductChoice(List<Products> prodList) {
        int prodSelection = 0;
        io.println(SPACE + "Products");
        for (int i = 0; i < prodList.size(); i++) {
            io.println(SPACE + SPACE + (i + 1) + " - " + prodList.get(i).getProductName());
        }
        //-1 to account for array indexing
        prodSelection = io.readInt(SPACE + "Choice: ", 1, prodList.size()) - 1;
        return prodList.get(prodSelection);
    }

    private Products editProductChoice(List<Products> prodList, Products prod) {

        io.println(SPACE + "Current Product: " + prod.getProductName());
        io.println(SPACE + "Products");
        for (int i = 0; i < prodList.size(); i++) {
            io.println(SPACE + SPACE + (i + 1) + " - " + prodList.get(i).getProductName());
        }

        while (true) {
            String stringSelection = io.readTrimmedString(SPACE + "Enter to skip or edit to: ");
            if (stringSelection.trim().length() < 1) {
                return prod;
            }
            try {
                Integer intSelection = Integer.parseInt(stringSelection);
                if (intSelection < 1 || intSelection > prodList.size()) {
                    throw new Exception();
                } else {
                    //-1 for intSelection to account for array
                    return prodList.get(intSelection - 1);
                }
            } catch (Exception e) {
                io.println(SPACE + "Invalid number entered, try again. ");
                displayBlankLine();
            }
        }
    }

    private Taxes getTaxChoice(List<Taxes> taxList) {
        int stateTaxSelection = 0;
        io.println(SPACE + "States");
        for (int i = 0; i < taxList.size(); i++) {
            io.println(SPACE + SPACE + (i + 1) + " - " + taxList.get(i).getState());
        }
        //-1 to account for array indexing
        stateTaxSelection = io.readInt(SPACE + "Choice: ", 1, taxList.size()) - 1;
        return taxList.get(stateTaxSelection);
    }

    private Taxes editTaxChoice(List<Taxes> taxList, Taxes tax) {

        io.println(SPACE + "Current State: " + tax.getState());
        io.println(SPACE + "States");
        for (int i = 0; i < taxList.size(); i++) {
            io.println(SPACE + SPACE + (i + 1) + " - " + taxList.get(i).getState());
        }

        while (true) {
            String stringSelection = io.readTrimmedString(SPACE + "Enter to skip or edit to: ");
            if (stringSelection.trim().length() < 1) {
                return tax;
            }
            try {
                Integer intSelection = Integer.parseInt(stringSelection);
                if (intSelection < 1 || intSelection > taxList.size()) {
                    throw new Exception();
                } else {
                    //-1 for intSelection to account for array
                    return taxList.get(intSelection - 1);
                }
            } catch (Exception e) {
                io.println(SPACE + "Invalid number entered, try again. ");
                displayBlankLine();
            }
        }
    }

    private String getCustomerName() {
        return io.readString(SPACE + "Customer Name: ");
    }

    private String editCustomerName(String origName) {
        io.println(SPACE + "Current Name: " + origName);
        String customerName = io.readTrimmedString(SPACE + "Enter to skip or edit to: ");
        return customerName.trim().isEmpty() ? origName : customerName;

    }

    private BigDecimal getSquareFeet() {
        BigDecimal sqft = null;
        while (true) {
            try {
                sqft = new BigDecimal(io.readString(SPACE + "SquareFeet: "));
                if (sqft.compareTo(BigDecimal.ZERO) < 1) {
                    io.println(SPACE + "Must be a positive number, try again.\n");
                } else {
                    return sqft;
                }
            } catch (Exception e) {
                io.println(SPACE + "Invalid number entered, try again.\n");
            }
        }
    }

    private BigDecimal editSquareFeet(BigDecimal sqft) {
        io.println(SPACE + "Current Area: " + sqft + " sqft");

        BigDecimal newSqft;
        while (true) {
            try {
                String stringSqft = io.readTrimmedString(SPACE + "Enter to skip or edit to: ");
                if (stringSqft.trim().length() < 1) {
                    return sqft;
                }
                newSqft = new BigDecimal(stringSqft);
                if (newSqft.compareTo(BigDecimal.ZERO) < 1) {
                    io.println(SPACE + "Must be a positive number, try again.\n");
                } else {
                    return newSqft;
                }
            } catch (Exception e) {
                io.println(SPACE + "Invalid number entered, try again.\n");
            }
        }
    }

    public LocalDate editDate(LocalDate origDate) {
        io.println(SPACE + "Current Date: " + origDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        String stringDate;
        while (true) {
            stringDate = io.readTrimmedString(SPACE + "Enter to skip or edit to (mm/dd/yyyy): ");
            if (stringDate.length() < 1) {
                return origDate;
            }
            try {
                LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if (date.isAfter(LocalDate.now().plusYears(3))) {
                    io.println(SPACE + "Order Date Must Be Within 3 Years. Try Again\n");
                    continue;
                }
                return date;
            } catch (Exception e) {
                io.println("");
                io.println(SPACE + "Invalid Date Format. Try Again.");
                io.println("");
            }
        }
    }

    public void displayEditOrderFailureBanner() {
        io.println(SPACE + "<<<<Order Not Edited. Returning To Main Menu>>>>");
    }

    public void displayEditOrderSuccessBanner() {
        io.println(SPACE + "<<<<Order Successfully Edited. Returning To Main Menu>>>>");
    }

    public void displayReturningToMainMenu() {
        io.println(SPACE + "<<<<Returning To Main Menu>>>>");
    }

    public void displayQuitBanner() {
        io.println("<<<<Shutting Down>>>>");
    }
}
