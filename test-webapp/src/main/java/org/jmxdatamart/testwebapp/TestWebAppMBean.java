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

package org.jmxdatamart.testwebapp;

import java.util.Random;

public class TestWebAppMBean implements TestWebAppMXBean {

  private final long creationTime = System.currentTimeMillis();
  private final Random random = new Random();
  private int numberOfCalls;

  @Override
  public long getAge() {
    return System.currentTimeMillis() - creationTime;
  }

  @Override
  public int getNumberOfInvocations() {
    return numberOfCalls++;
  }

  @Override
  public float getRandomFloat() {
    return random.nextFloat();
  }

  @Override
  public String getAsciiString() {
    return "Where is the library?";
  }

  @Override
  public String getLatin1String() {
    return "\\u00BFD\\u00F3nde est\\u00E1 la biblioteca?";
  }

  @Override
  public String getUnicodeString() {
    return "Th\\u01B0 vi\\u1EC7n \\u1EDF \\u0111\\u00E2u?";
  }

  @Override
  public String getAnotherUnicodeString() {
    return "\u56F3\u66F8\u9928\u306F\u3069\u3053\u3067\u3059\u304B\uFF1F";
  }

}
