/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart;

import java.io.File;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.jmxdatamart.Loader.CSVReader;
import org.jmxdatamart.common.DataType;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CSVReaderTest extends TestCase {
  
  public CSVReaderTest(String testName) {
    super(testName);
  }
  
  public void testCSVReader() {
    CSVReader csvr = new CSVReader(System.getProperty("user.dir") + File.separator);
    Map<String, Map<String, DataType>> schema = csvr.getSchema();
    List<String> tables = csvr.getTableNames();
    for (String s : tables) {
      csvr.open(s);
      Map<String, String> line;
      while ((line = csvr.readLine()) != null){
        for (Map.Entry<String, String> e : line.entrySet()) {
          System.out.println(e.getKey() + " -> " + e.getValue());
        }
        System.out.println("===***===");
      }
    }
  }
}
