/*
 * Copyright (c) 2013, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jmxdatamart.Extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MBeanExtract {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MBeanExtract.class);
    
    
    public static Map<Attribute, Object> extract(MBeanData mbd, MBeanServerConnection mbsc) {
        ObjectName on = null;
        try {
            on = new ObjectName(mbd.getName());
        } catch (MalformedObjectNameException ex) {
            logger.error("Error while trying to attach to " + mbd.getName(), ex);
            System.exit(1);
        }
    
        Map<Attribute, Object> retVal = new HashMap<Attribute, Object>();
        
        for (Attribute a : mbd.getAttributes()) {
            try{
                String aName = a.getName();
                if (!aName.contains(".")) {
                    retVal.put(a, mbsc.getAttribute(on, aName));
                } else {
                    String[] mxAttribute = aName.split("\\.");
                    if (mxAttribute.length != 2) {
                        logger.error("MXBean attribute malformed " + aName);
			System.exit(1);
                    }
                    CompositeData cd = (CompositeData)mbsc.getAttribute(on, mxAttribute[0]);
                    Object value = cd.get(mxAttribute[1]);
                    if (value.getClass().getCanonicalName().equals(a.getDataType().getJavaType()))
                    	retVal.put(a, value);
                    else
                    	logger.error("Error while extracting " + a.getAlias() + " from " + on + ": Mismatched data type\n");
                }
            } catch (Exception ex) {
                logger.error("Error while extracting " 
                                + a.getName() + " from " 
                                + mbd.getName(), ex);
            }
        }
        
        return retVal;
    }
    
}
