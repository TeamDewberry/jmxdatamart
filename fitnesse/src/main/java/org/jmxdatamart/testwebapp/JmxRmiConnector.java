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

package org.jmxdatamart.testwebapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

/**
 * Starts up a JMX RMI Connector
 */
public class JmxRmiConnector {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public JmxRmiConnector() throws IOException, NamingException {
    System.setProperty("java.rmi.server.randomIDs", "true");

    int port = getPort();
    logger.info("Starting JMX RMI Connector on port {}", port);

    LocateRegistry.createRegistry(port);

    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

    HashMap<String,Object> env = new HashMap<String,Object>();
    JMXServiceURL url = new JMXServiceURL(String.format("service:jmx:rmi:///jndi/rmi://:%d/jmxrmi", port));
    JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
    cs.start();
  }

  private int getPort() throws NamingException {
    InitialContext initialContext = new InitialContext();
    Context context = (Context) initialContext.lookup("java:comp/env");
    return (Integer) context.lookup("jmxdatamart-testwebapp/jmx-rmi-port");
  }

}
