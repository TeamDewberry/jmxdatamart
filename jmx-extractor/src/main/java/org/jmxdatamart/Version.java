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

package org.jmxdatamart;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Version {
  private final String builtBy;
  private final String buildTime;
  private final String buildNumber;
  private final String sourceRevision;
  private final String implementationVersion;

  /**
   * Computes version information about a class from the Manifest in its jar file
   */
  public Version(Class<?> aClass) throws IOException {
    Manifest manifest = getManifestForMyJarFile(aClass);
    Attributes mainAttributes = manifest.getMainAttributes();
    builtBy = mainAttributes.getValue("Built-By");
    buildTime = mainAttributes.getValue("Build-Time");
    buildNumber = mainAttributes.getValue("Build-Number");
    sourceRevision = mainAttributes.getValue("Source-Revision");
    implementationVersion = mainAttributes.getValue("Implementation-Version");
  }

  private Manifest getManifestForMyJarFile(Class<?> aClass) throws IOException {
    URL url = aClass.getProtectionDomain().getCodeSource().getLocation();
    URLClassLoader cl = new URLClassLoader(new URL[]{url});
    URL manifestUrl = cl.findResource("META-INF/MANIFEST.MF");
    if (manifestUrl == null) {
      return new Manifest();
    } else {
      return new Manifest(manifestUrl.openStream());
    }
  }

  public String getBuiltBy() {
    return builtBy;
  }

  public String getBuildTime() {
    return buildTime;
  }

  public String getBuildNumber() {
    return buildNumber;
  }

  public String getSourceRevision() {
    return sourceRevision;
  }

  public String getImplementationVersion() {
    return implementationVersion;
  }
}
