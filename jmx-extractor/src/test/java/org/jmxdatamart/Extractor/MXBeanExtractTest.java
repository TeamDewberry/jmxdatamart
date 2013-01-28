/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.JMXTestServer.CarBean;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MXBeanExtractTest {
    
    public MXBeanExtractTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of extract method, of class MXBeanExtract.
     */
    @Test
    public void testExtract() throws Exception {
        System.out.println("extract");
        ObjectName on = new ObjectName("org.jmxdatamart.JMXTestServer:type=CarBean");
        Object cb = new CarBean();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(cb, on);
        
        List<Attribute> attributes = new ArrayList<Attribute>();
        Attribute a1 = new Attribute("Car.name", "Full name", DataType.STRING);
        Attribute a2 = new Attribute("Car.year","Year unveiled", DataType.STRING);
        Attribute a3 = new Attribute("Car.engine", "Engine type", DataType.INT);
        Attribute a4 = new Attribute("Car.power", "Total power", DataType.INT);
        attributes.add(a1);
        attributes.add(a2);
        attributes.add(a3);
        attributes.add(a4);
        
        MBeanData mbd = new MBeanData(on.getCanonicalName(), "Dream Car", attributes, true);
        
        MXBeanExtract mxbe = new MXBeanExtract(mbd, mbs);
        
        Map<Attribute, Object> result = mxbe.extract();
        
        assertEquals(result.get(a1), CarBean.name);
        assertEquals(result.get(a2), CarBean.year);
        assertEquals(result.get(a3), CarBean.engine);
        assertEquals(result.get(a4), CarBean.power);
        assertEquals(result.size(), 4);
        
        //MXBeanExtract instance = new MXBeanExtract();
        //Map expResult = null;
        //Map result = instance.extract();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
