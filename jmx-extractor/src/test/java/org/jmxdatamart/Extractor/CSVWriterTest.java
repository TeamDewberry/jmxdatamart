/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import java.util.Map;
import java.util.HashMap;
import org.jmxdatamart.common.DataType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CSVWriterTest {
    
     @Test
     public void testEnclose() {
         String s = "Hello, World";
         assertEquals(  CSVWriter.DOUBLE_QUOTE + "Hello, World" + CSVWriter.DOUBLE_QUOTE,
                        CSVWriter.enclose(s).toString());
     }
     
     @Test
     public void testLineUpResult() {
         Map<Attribute, Object> result;
         CSVWriter csvw = new CSVWriter(
                 new MBeanData("aBean", "MBEAN!!11!", null, true), 
                 "/home/ankel/jmx1");
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("A", "Alpha", DataType.INT), new Integer(7));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Hello World");
         csvw.writeResult(result);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("A", "Alpha", DataType.INT), new Integer(42));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Cruel World");
         csvw.writeResult(result);
         
         result = new HashMap<Attribute, Object>();
         result.put(new Attribute("C", "Sigma", DataType.INT), new Integer(-1));
         result.put(new Attribute("B", "Beta", DataType.STRING), "Bye World");
         csvw.writeResult(result);
                 
     }
}
