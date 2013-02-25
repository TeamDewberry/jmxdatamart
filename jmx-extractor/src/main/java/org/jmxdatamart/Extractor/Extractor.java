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

import com.google.inject.Inject;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.jmxdatamart.common.HypersqlHandler;

public class Extractor {

    private final Settings configData;
    private final MBeanServerConnection mbsc;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(Extractor.class);
    private final Bean2DB bd = new Bean2DB();
    private final String dbname;
    private final HypersqlHandler hsql;
    private Connection conn;
    private final Lock connLock = new ReentrantLock();

    @Inject
    public Extractor(Settings configData) throws IOException, SQLException {
        this.configData = configData;
        if (configData.getUrl() == null || configData.getUrl().isEmpty()) {
            mbsc = ManagementFactory.getPlatformMBeanServer();
        } else {
            JMXServiceURL url = new JMXServiceURL(configData.getUrl());
            mbsc = JMXConnectorFactory.connect(url).getMBeanServerConnection();
        }
               
        hsql = new HypersqlHandler();
        dbname = bd.generateMBeanDB(configData);

        if (shouldPeriodicallyExtract()) {
            periodicallyExtract();
        }
    }

    private void periodicallyExtract() {
        boolean isDaemon = true;
        Timer timer = new Timer("JMX Statistics Extractor", isDaemon);
        long rate = configData.getPollingRate() * 1000;
        int delay = 0;
        timer.scheduleAtFixedRate(new Extract(), delay, rate);
    }

    private boolean shouldPeriodicallyExtract() {
        return this.configData.getPollingRate() > 0;
    }

    void extract() throws Exception {
        Properties props = new Properties();
        props.put("username", "sa");
        props.put("password", "whatever");
        
        connLock.lock();
        try{
            conn= hsql.connectDatabase(dbname,props);

            for (BeanData bdata : this.configData.getBeans()) {
                MBeanExtract mbe = new MBeanExtract(bdata, mbsc);
                Map<Attribute, Object> result = mbe.extract();
                bd.export2DB(conn, bdata, result);
            }

            hsql.shutdownDatabase(conn);
            hsql.disconnectDatabase(null,null,null,conn);
            conn = null;
        } finally {
            connLock.unlock();
        }
        System.out.println("Extracted");
    }

    private class Extract extends TimerTask {

        public Extract(){
            super();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connLock.lock();
                        if (conn != null && !conn.isClosed()) {
                            hsql.shutdownDatabase(conn);
                            hsql.disconnectDatabase(null,null,null,conn);
                        }
                    } catch (SQLException ex) {
                            LoggerFactory.getLogger(Extractor.class)
                                    .error("Error while closing conn durring JVM shutdown", ex);
                    } finally {
                        connLock.unlock();
                    }
                }
            }));
        }
        
        @Override
        public void run() {
            try {
                extract();
            } catch (Exception e) {
                logger.debug("While extracting MBeans", e);
            }
        }
    }
}