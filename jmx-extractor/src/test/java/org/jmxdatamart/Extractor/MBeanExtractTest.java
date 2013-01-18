/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.JMXTestServer.TestBean;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MBeanExtractTest {
    
    public MBeanExtractTest() {
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
     * Test of extract method, of class MBeanExtract.
     */
    @Test
    public void testExtract() throws Exception {
        System.out.println("extract");
        
        // values
        int expected = 42;
        
        //Create new test MBean
        TestBean tb = new TestBean();
        tb.setA(new Integer(expected));
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String mbName = "org.jmxdatamart.JMXTestServer:type=TestBean";
        ObjectName mbeanName = new ObjectName(mbName);
        mbs.registerMBean(tb, mbeanName);
        
        //Create test MBean's MBeanData
        Attribute a = new Attribute("A", "Alpha", DataType.INT);
        MBeanData mbd = new MBeanData(mbName, "testMBean",
                                        Collections.singletonList(a), true);
        
        //Init MBeanExtract
        MBeanExtract instance = new MBeanExtract(mbd, mbs);
        Map result = instance.extract();
        assertEquals(1, result.size());
        assertTrue(result.keySet().contains(a));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
