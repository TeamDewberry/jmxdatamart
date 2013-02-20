/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jmxdatamart.common.DataType;
import org.slf4j.LoggerFactory;

/**
 * Counterpart of CSVWriterm CSVReader will read a csv format file.
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CSVReader {
    
    private final String filePath;
    private final Map<String, DataType> seen = new HashMap<String, DataType>();
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVReader.class);
    private final String name;
    
    public CSVReader(String filePath) {
        this.filePath = filePath;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            name = br.readLine();
            br.close();
        } catch (IOException ex) {
            logger.error("Can not open " + filePath, ex);
            throw new RuntimeException(ex);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public DataType getType(String columnName) {
        if (seen.get(columnName) != null) {
            return seen.get(columnName);
        } else {
            return null;
        }
    }
    
}
