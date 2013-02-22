/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import org.jmxdatamart.Extractor.Setting.Attribute;
import org.jmxdatamart.Extractor.Setting.MBeanData;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.jmxdatamart.common.CVSCommon;
import org.jmxdatamart.common.DataType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CSVWriterTest {
   
     @Test
     public void testEnclose() {
         String s = "Hello, World";
         assertEquals(  CVSCommon.GENERAL_ENCLOSE + "Hello, World" + CVSCommon.GENERAL_ENCLOSE,
                        CSVWriter.enclose(s).toString());
     }
     
     @Test
     public void testLineUpResult() {
         Map<Attribute, Object> result;
         final String alias = "MBEAN";
         CSVWriter csvw = new CSVWriter(
                 new MBeanData("aBean", alias, null, true), 
                 System.getProperty("user.dir"));
         
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
                 
         File f = new File(csvw.getFilePath());
         assertTrue(f.exists());
         f.delete();
         assertFalse(f.exists());
     }
}
