/*
 * Copyright (c) 2013, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   o  Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *
 *   o  Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
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

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestPropertyExpander {

  @Test
  public void stringWithNoPropertiesIsStillTheSameAfterExpansion() {
    String before = "This is a string with no system property";

    PropertyExpander expander = new PropertyExpander(System.getProperties());
    assertThat(expander.expand(before), equalTo(before));
  }

  @Test
  public void onePropertyAtStartOfStringIsExpanded() {
    String before = "${name} begins a sentence";

    PropertyExpander expander = expanderWithProperties("name", "The first word");

    assertThat(expander.expand(before), equalTo("The first word begins a sentence"));
  }

  private PropertyExpander expanderWithProperties(String... keysAndValues) {
    if (keysAndValues.length % 2 != 0) {
      String m = "Expected an even number of keys and values, not " + keysAndValues.length;
      throw new IllegalStateException(m);
    }

    Properties props = new Properties();
    for (int i = 0; i < keysAndValues.length / 2; i++) {
      int index = i * 2;
      props.setProperty(keysAndValues[index], keysAndValues[index + 1]);
    }
    return new PropertyExpander(props);

  }

  @Test
  public void onePropertyAtEndOfStringIsExpanded() {
    String before = "The first word begins ${name}";

    PropertyExpander expander = expanderWithProperties("name", "a sentence");

    assertThat(expander.expand(before), equalTo("The first word begins a sentence"));
  }

  @Test
  public void onePropertyInMiddleOfStringIsExpanded() {
    String before = "The first ${name} a sentence";

    PropertyExpander expander = expanderWithProperties("name", "word begins");

    assertThat(expander.expand(before), equalTo("The first word begins a sentence"));
  }

  @Test
  public void twoDollarsSignsTogetherExpandsToADollarAndPropertyValue() {
    String before = "The first $${name} a sentence";

    PropertyExpander expander = expanderWithProperties("name", "word begins");

    assertThat(expander.expand(before), equalTo("The first $word begins a sentence"));
  }

  @Test
  public void twoLeftBracesAfterDollarIsLeftBraceInPropertyName() {
    String before = "The first ${{name} a sentence";

    PropertyExpander expander = expanderWithProperties("{name", "word begins");

    assertThat(expander.expand(before), equalTo("The first word begins a sentence"));
  }

  @Test
  public void multiplePropertiesAreExpanded() {
    String before = "${one} ${two} ${three}";

    PropertyExpander expander = expanderWithProperties("one", "The first", "two", "word begins", "three", "a sentence");

    assertThat(expander.expand(before), equalTo("The first word begins a sentence"));
  }

  @Test
  public void unsetPropertyIsNotExpanded() {
    String before = "The first ${name} a sentence";

    PropertyExpander expander = expanderWithProperties();

    assertThat(expander.expand(before), equalTo(before));
  }

  @Test
  public void someDollarsAndBracesNotTogetherIsNotExpanded() {
    String before = "This sho{uld $ no}t be ex{pande}d";

    PropertyExpander expander = expanderWithProperties("pande", "nope");

    assertThat(expander.expand(before), equalTo(before));
  }

  @Test
  public void dollarSignInPropertyame() {
    String before = "The first ${$name} a sentence";

    PropertyExpander expander = expanderWithProperties("$name", "word begins");

    assertThat(expander.expand(before), equalTo("The first word begins a sentence"));
  }

}
