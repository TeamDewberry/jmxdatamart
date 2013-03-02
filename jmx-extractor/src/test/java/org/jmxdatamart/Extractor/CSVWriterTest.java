/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.jmxdatamart.common.CSVCommon;
import org.jmxdatamart.common.DataType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CSVWriterTest {
  
     private static String[] names = {"Name1", "Name2", "Name3", "Name4"};
   
     @Test
     public void testEnclose() {
         String s = "Hello, World";
         assertEquals(  CSVCommon.STRING_ENCLOSE + "Hello, World" + CSVCommon.STRING_ENCLOSE,
                        CSVWriter.enclose(s).toString());
     }
     
     @Test
     public void testDifferentFiles() {
         Map<Attribute, Object> result;
         final String alias = "MBEAN";
         CSVWriter csvw = new CSVWriter(System.getProperty("user.dir") + File.separator);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("A", "Alpha", DataType.INT), new Integer(7));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Hello World");
         csvw.writeResult(names[0],result);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("A", "Alpha", DataType.INT), new Integer(42));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Cruel World");
         csvw.writeResult(names[1], result);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("C", "Sigma", DataType.INT), new Integer(-1));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Bye World");
         csvw.writeResult(names[2], result);
                 
//         File f = new File(csvw.getFilePath());
//         assertTrue(f.exists());
//         f.delete();
//         assertFalse(f.exists());
     }
     
     @Test
     public void testSameFile() {
         Map<Attribute, Object> result;
         final String alias = "MBEAN";
         CSVWriter csvw = new CSVWriter(System.getProperty("user.dir") + File.separator);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("A", "Alpha", DataType.INT), new Integer(7));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Hello World");
         csvw.writeResult(names[3],result);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("A", "Alpha", DataType.INT), new Integer(42));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Cruel World");
         csvw.writeResult(names[3], result);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("C", "Sigma", DataType.INT), new Integer(-1));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Bye World");
         csvw.writeResult(names[3], result);
                 
//         File f = new File(csvw.getFilePath());
//         assertTrue(f.exists());
//         f.delete();
//         assertFalse(f.exists());
     }
}
