/*
 * Copyright (c) 2012, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Extractor {

    private final Settings configData;
    private final MBeanServerConnection mbsc;
    private final Logger logger = LoggerFactory.getLogger(Extractor.class);

    @Inject
    public Extractor(Settings configData) throws IOException {
        this.configData = configData;
        if (configData.getUrl() == null) {
            mbsc = ManagementFactory.getPlatformMBeanServer();
        } else {
            JMXServiceURL url = new JMXServiceURL(configData.getUrl());
            mbsc = JMXConnectorFactory.connect(url).getMBeanServerConnection();
        }

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

    String extract() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        StringBuilder outputStuff = new StringBuilder();
        for (MBeanData bd : this.configData.getBeans()) {
            ObjectName on = new ObjectName(bd.getName());
            for (Attribute a : bd.getAttributes()) {
                outputStuff.append(bd.getAlias()).append(", ")
                        .append(a.getAlias()).append(", ")
                        .append(a.getDataType()).append(", ")
                        .append(this.mbsc.getAttribute(on, a.getName()).toString())
                        .append("\n");
            }
        }
        return outputStuff.toString();
    }

    private class Extract extends TimerTask {

        @Override
        public void run() {
            try {
                //Temporarily disabled by Xiao since it causes the Extrator and JMXTestServer out-of-sync.
                //System.out.print(extract());
            } catch (Exception e) {
                logger.debug("While extracting MBeans", e);
            }
        }
    }
}