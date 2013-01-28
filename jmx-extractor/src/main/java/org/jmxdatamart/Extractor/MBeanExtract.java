/*
 * Copyright (c) 2012, Tripwire, Inc.
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MBeanExtract implements Extractable{

    MBeanData mbd;
    MBeanServerConnection mbsc;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MBeanExtract.class);
    ObjectName on;

    public MBeanExtract(MBeanData mbd, MBeanServerConnection mbsc) throws MalformedObjectNameException {
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
        Map<Attribute, Object> ret = new HashMap<Attribute, Object>();
        for (Attribute a : this.mbd.getAttributes()){
            try {
                ret.put(a, this.mbsc.getAttribute(on, a.getName()));
            } catch (Exception ex) {   // JDK 6 doesn't support multicatch, ARGGG
                logger.error("Error while extracting " 
                                + a.getName() + " from " 
                                + mbd.getName(), ex);
                throw ex;
            }
        }
        return ret;
    }
    
    
}
