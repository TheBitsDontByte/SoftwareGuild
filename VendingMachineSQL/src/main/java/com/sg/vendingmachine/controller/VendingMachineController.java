/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.controller;

import com.sg.vendingmachine.model.Item;
import com.sg.vendingmachine.model.UserData;
import com.sg.vendingmachine.service.BankrollService;
import com.sg.vendingmachine.service.InsufficientFundsException;
import com.sg.vendingmachine.service.InventoryService;
import com.sg.vendingmachine.service.OutOfStockException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author chris
 */
@Controller()
public class VendingMachineController {

    InventoryService inventory;
    BankrollService bankroll;
    UserData user;

    @Inject
    public VendingMachineController(InventoryService inventory, BankrollService bankroll) {
        this.user = new UserData();
        this.inventory = inventory;
        this.bankroll = bankroll;
    }

    @RequestMapping(value = "/")
    public String home(Model model) {
        String currChange = bankroll.getChange(user.getCurrentChange());

        BigDecimal currMoney = user.getCurrentMoney();
        String message = user.getMessage();

        List<Item> stock = inventory.getInventory();

        Item currItem;
        try {
            currItem = user.getCurrentItem();
            model.addAttribute("currItemId", currItem.getItemId());
        } catch (Exception e) {
            model.addAttribute("currItemId", "");
//            message = e.getMessage();
        }

        model.addAttribute("currMoney", currMoney.compareTo(BigDecimal.ZERO) == 0 ? "" : currMoney);
        model.addAttribute("currChange", currChange);
        model.addAttribute("message", message);
        model.addAttribute("stock", stock);
        user.setMessage("");
        return "VendingMachine";
    }

    @RequestMapping(value = "addMoney")
    public String addMoney(HttpServletRequest request, Model model) {
        String value = request.getParameter("value");
        user.setCurrentMoney(bankroll.addMoney(value));
        return "redirect:/";
    }

    @RequestMapping(value = "selectItem")
    public String selectItem(HttpServletRequest request, Model model) {
        String param = request.getParameter("itemId");
        int itemId = Integer.parseInt(param);

        try {
            user.setCurrentItem(inventory.setCurrItem(itemId));

        } catch (OutOfStockException e) {
            user.setMessage(e.getMessage());
            user.setCurrentItem(null);
        }

        return "redirect:/";
    }

    @RequestMapping(value = "makePurchase")
    public String makePurchase() {
        try {
            BigDecimal currMoney = bankroll.getCurrentAmount();

            Item currItem = user.getCurrentItem();
            if (currItem == null) {
                user.setMessage("Make A Selection");
                return "redirect:/";
            }

            Map<String, Integer> change = bankroll.makeChange(currMoney, currItem.getItemCost());
            user.setCurrentChange(change);
            user.setCurrentMoney(BigDecimal.ZERO);
            inventory.vendItem(currItem);

            user.setMessage("Thank you !!");
            return "redirect:/";
        } catch (InsufficientFundsException e) {
            user.setMessage(e.getMessage());
            return "redirect:/";
        }

    }

    @RequestMapping("makeChange")
    public String makeChange() {
        BigDecimal currMoney = bankroll.getCurrentAmount();

        try {
            Map<String, Integer> change = bankroll.makeChange(currMoney, BigDecimal.ZERO);
            user.setCurrentChange(change);
        } catch (InsufficientFundsException e) {
            user.setMessage(e.getMessage());

        }
        user.setCurrentItem(null);
        return "redirect:/";
    }

}
