/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor.MXBean;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.Extractor.Setting.Attribute;
import org.jmxdatamart.common.DataType;
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
public class MultiLayeredAttributeTest {
    
    @Before
    public void setUp() {
        // force GC -- will use a of memory and will crash if your jvm doesn't
        // have enough memory - oopsy!
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int i = 0; i < 10000000; ++i) {
            arr.add(i);
        }
        arr.clear();
        for (int i = 0; i < 10000000; ++i) {
            arr.add(10000000 - i);
        }
        arr.clear();
    }
    
    @Test
    public void testMultiLayeredAttribute() throws Exception{
        // note: test PS MarkSweep
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mS = new ObjectName("java.lang:name=PS MarkSweep,type=GarbageCollector");
        
//        Attribute a = new Attribute("LastGcInfo.GcThreadCount", "tc", null);
        MultiLayeredAttribute mla = new MultiLayeredAttribute("LastGcInfo.GcThreadCount", mbs, mS);
        Map<Attribute, Object> result = mla.getAll();
        assertTrue(result.size() == 1);
        for (Map.Entry<Attribute, Object> entry : result.entrySet()) {
            
        }
    }
}