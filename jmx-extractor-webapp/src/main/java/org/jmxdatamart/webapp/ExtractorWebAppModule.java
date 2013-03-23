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

package org.jmxdatamart.webapp;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jmxdatamart.Extractor.Extractor;
import org.jmxdatamart.Extractor.ExtractorSettings;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ExtractorWebAppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SettingsFileProvider.class).to(SettingsFileFromJndi.class);
    bind(Extractor.class).asEagerSingleton();
  }

  @Provides
  public MBeanServerConnection provideMBeanServerConnection(ExtractorSettings settings) throws IOException {
    String jmxUrl = settings.getUrl();
    if (jmxUrl == null) {
      return ManagementFactory.getPlatformMBeanServer();

    } else {
      return connectToRemoteMBeanServer(jmxUrl);
    }
  }

  private MBeanServerConnection connectToRemoteMBeanServer(String jmxUrl) throws IOException {
    JMXServiceURL url = new JMXServiceURL(jmxUrl);
    return JMXConnectorFactory.connect(url).getMBeanServerConnection();
  }

  @Provides
  public ExtractorSettings provideSettings(SettingsFileProvider settingsFile) throws FileNotFoundException {
    return ExtractorSettings.fromXML(new FileInputStream(settingsFile.get()));
  }

}
