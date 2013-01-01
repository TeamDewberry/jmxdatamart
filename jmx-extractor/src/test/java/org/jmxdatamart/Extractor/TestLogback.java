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

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class TestLogback extends TestCase {


  private PrintStream originalStandardOut;
  private ByteArrayOutputStream standardOutputBytes;

  @Override
  protected void setUp() throws Exception {
    originalStandardOut = System.out;

    standardOutputBytes = new ByteArrayOutputStream();
    PrintStream newOut = new PrintStream(standardOutputBytes);
    System.setOut(newOut);
  }

  @Override
  protected void tearDown() throws Exception {
    if (originalStandardOut != null) {
      System.setOut(originalStandardOut);
    }
  }

  private String getStandardOutput() {
    return standardOutputBytes.toString();
  }

  public void testThatLogbackMessagesAreLoggedWithTestConfiguration() {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    String errorLevelMessage = "Expect error-level to be written";
    logger.error(errorLevelMessage);

    String infoLevelMessage = "Expect info-level to be written";
    logger.info(infoLevelMessage);

    String debugLevelMessage = "Expect debug-level to be written";
    logger.debug(debugLevelMessage);

    String traceLevelMessage = "Did not expect trace-level to be written";
    logger.trace(traceLevelMessage);

    String out = getStandardOutput();
    assertThat(out, containsString(errorLevelMessage));
    assertThat(out, containsString(infoLevelMessage));
    assertThat(out, containsString(debugLevelMessage));
    assertThat(out, not(containsString(traceLevelMessage)));
  }

}
