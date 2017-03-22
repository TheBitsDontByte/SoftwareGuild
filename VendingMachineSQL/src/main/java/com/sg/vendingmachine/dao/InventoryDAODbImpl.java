/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.model.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chris
 */
public class InventoryDAODbImpl implements InventoryDAO {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_ALL_INVENTORY
            = "select * from Inventory";

    private static final String SQL_GET_INVENTORY_BY_ID
            = "select * from Inventory where inventory_id = ?";

    private static final String SQL_UPDATE_ITEM_QUANTITY
            = "update Inventory set item_quantity = ? where inventory_id = ?";

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Item> loadInventory() throws VendingMachineOperationException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL_INVENTORY, new ItemMapper());
        } catch (Exception e) {
            throw new VendingMachineOperationException("Coudln't Load Inventory");
        }
    }

    @Override
    public List<Item> getInventory() {
        return jdbcTemplate.query(SQL_GET_ALL_INVENTORY, new ItemMapper());
    }

    @Override
    public Item getItem(int itemId) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_INVENTORY_BY_ID, new ItemMapper(), itemId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Item decreaseItemQuantity(int itemId) {
        Item item = getItem(itemId);
        int updateQuantity = item.getItemQuantity() - 1;
        item.setItemQuantity(updateQuantity);
        jdbcTemplate.update(SQL_UPDATE_ITEM_QUANTITY, updateQuantity, itemId);
        return item;
    }

    private static class ItemMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(ResultSet rs, int i) throws SQLException {
            Item item = new Item();
            item.setItemId(rs.getInt("inventory_id"));
            item.setItemCost(rs.getBigDecimal("item_cost"));
            item.setItemName(rs.getString("item_name"));
            item.setItemQuantity(rs.getInt("item_quantity"));
            return item;
        }

    }

}
