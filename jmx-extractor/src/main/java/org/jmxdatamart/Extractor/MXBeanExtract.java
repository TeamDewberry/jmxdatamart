/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MXBeanExtract implements Extractable{

    BeanData mbd;
    MBeanServerConnection mbsc;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MBeanExtract.class);
    ObjectName on;

    public MXBeanExtract(BeanData mbd, MBeanServerConnection mbsc) throws MalformedObjectNameException {
        this.mbd = mbd;
        this.mbsc = mbsc;
        try {
            on = new ObjectName(mbd.getName());
        } catch (MalformedObjectNameException ex) {
            logger.error("Error while creating ObjectName from MBeanData", ex);
            throw ex;
        }
    }
    
    
    @Override
    public Map<Attribute, Object> extract() throws Exception {
        Map<Attribute, Object> retVal = new HashMap<Attribute, Object>();
        
        for (Attribute a : this.mbd.getAttributes()) {
            try{
                String aName = a.getName();
                if (!aName.contains(".")) {
                    retVal.put(a, this.mbsc.getAttribute(on, aName));
                } else {
                    String[] mxAttribute = aName.split("\\.");
                    if (mxAttribute.length != 2) {
                        throw new Exception("MXBean attribute malformed " + aName);
                    }
                    CompositeData cd = (CompositeData)mbsc.getAttribute(on, mxAttribute[0]);
                    Object value = cd.get(mxAttribute[1]);
                    retVal.put(a, value);
                }
            } catch (Exception ex) {
                logger.error("Error while extracting " 
                                + a.getName() + " from " 
                                + mbd.getName(), ex);
                throw ex;
            }
        }
        
        return retVal;
    }
    
}
