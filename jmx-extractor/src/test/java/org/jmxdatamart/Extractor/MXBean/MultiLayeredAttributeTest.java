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
import org.jmxdatamart.JMXTestServer.CarBean;
import org.jmxdatamart.common.DataType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MultiLayeredAttributeTest {
    
    @Test
    public void testMultiLayeredAttribute() throws Exception{
        // note: test PS MarkSweep
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mS = new ObjectName("java.lang:name=CarBean");
        mbs.registerMBean(new CarBean(), mS);
        
        MultiLayeredAttribute mla = new MultiLayeredAttribute("Car.*", mbs, mS);
        Map<Attribute, Object> result = mla.getAll();
        
        assertTrue ( result.size() == 4);
        
        for (Map.Entry<Attribute, Object> entry : result.entrySet()) {
            if (entry.getKey().getAlias().equals("Car_name")) {
                assertThat(entry.getValue().toString(), equalTo(CarBean.NAME));
            } else if (entry.getKey().getAlias().equals("Car_year")) {
                assertThat(entry.getValue().toString(), equalTo(CarBean.YEAR));
            } else if (entry.getKey().getAlias().equals("Car_engine")) {
                assertThat(entry.getValue().toString(), 
                        equalTo((new Integer(CarBean.ENGINE)).toString()));
            } else if (entry.getKey().getAlias().equals("Car_power")) {
                assertThat(entry.getValue().toString(),
                        equalTo((new Integer(CarBean.POWER)).toString()));
            } else {
                fail("Unknown attribute " + entry.getKey().toString());
            }
        }
        
        mla = new MultiLayeredAttribute("Map.*.*", mbs, mS);
        result = mla.getAll();
        
        assertEquals(4, result.size());
        for (Map.Entry<Attribute, Object> entry : result.entrySet()) {
            if (entry.getKey().getAlias().equals("Map_Car_name")) {
                assertThat(entry.getValue().toString(), equalTo(CarBean.NAME));
            } else if (entry.getKey().getAlias().equals("Map_Car_year")) {
                assertThat(entry.getValue().toString(), equalTo(CarBean.YEAR));
            } else if (entry.getKey().getAlias().equals("Map_Car_engine")) {
                assertThat(entry.getValue().toString(), 
                        equalTo((new Integer(CarBean.ENGINE)).toString()));
            } else if (entry.getKey().getAlias().equals("Map_Car_power")) {
                assertThat(entry.getValue().toString(),
                        equalTo((new Integer(CarBean.POWER)).toString()));
            } else {
                fail("Unknown attribute " + entry.getKey().toString());
            }
        }
    }
}