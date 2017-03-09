/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author chris
 */
public class FlooringMasteryAuditDaoFileImpl implements FlooringMasteryAuditDao {

    String FILE;

    FlooringMasteryAuditDaoFileImpl(String file) {
        FILE = file;
    }

    public void writeEntry(String audit) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE, true))) {
            out.println(audit);
            out.flush();
        } catch (IOException e) {
            System.err.println("Couldn't Write To Audit File");
        }
    }

}
