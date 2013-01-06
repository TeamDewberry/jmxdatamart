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

package org.jmxdatamart.fixtures;

import fit.ColumnFixture;
import org.jmxdatamart.Version;

import java.io.IOException;

public class VersionInformationForClass extends ColumnFixture {

  String className;

  private Version version;

  private Class<?> loadClass(String className) throws ClassNotFoundException {
    return this.getClass().getClassLoader().loadClass(className);
  }

  private Version getVersion() throws ClassNotFoundException, IOException {
    if (version == null) {
      this.version = new Version(loadClass(className));
    }

    return version;
  }

  public String builtBy() throws IOException, ClassNotFoundException {
    return getVersion().getBuiltBy();
  }

  public boolean hasSomeInformation() throws ClassNotFoundException, IOException {
    return builtBy() != null;
  }

  public String buildTime() throws IOException, ClassNotFoundException {
    return getVersion().getBuildTime();
  }

  public String buildNumber() throws IOException, ClassNotFoundException {
    return getVersion().getBuildNumber();
  }

  public String sourceRevision() throws IOException, ClassNotFoundException {
    return getVersion().getSourceRevision();
  }

  public String implementationVersion() throws IOException, ClassNotFoundException {
    return getVersion().getImplementationVersion();
  }
}
