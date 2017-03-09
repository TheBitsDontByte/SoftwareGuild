/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class FlooringMasteryIdDaoFileImpl implements FlooringMasteryIdDao {

    final String ID_FILE;

    FlooringMasteryIdDaoFileImpl(String idFile) {
        this.ID_FILE = idFile;
    }

    @Override
    public Integer getNewUniqueID() throws FMPersistenceException {
        Integer newUniqueID = readFile() + 1;

        return newUniqueID;
    }

    @Override
    public void setCurrentId(int id) throws FMPersistenceException {
        writeFile(id);
    }

    private Integer readFile() throws FMPersistenceException {
        File file = new File(ID_FILE);
        try {
            //If no file, create new file. Zero (0) will be returned, incremented
            //written (as one (1)) and then passed out of the class
            if (!file.isFile()) {
                file.createNewFile();
                return 0;
            }
        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't Instantiate New Unique ID. Contact System Admin");
        }
        Integer currID;
        try (Scanner scan = new Scanner(new BufferedReader(new FileReader(ID_FILE)))) {
            currID = scan.nextInt();
        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't Access New Unique ID. Contact System Admin");
        }
        return currID;
    }

    private void writeFile(Integer id) throws FMPersistenceException {
        try (PrintWriter out = new PrintWriter(new FileWriter(ID_FILE))) {
            out.println(id);
        } catch (IOException e) {
            throw new FMPersistenceException("Couldn't Persist UniqueID. Contact System Admin");
        }
    }

}
