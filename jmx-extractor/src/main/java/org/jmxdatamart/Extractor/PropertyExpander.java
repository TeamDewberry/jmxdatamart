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

import java.util.Properties;

public class PropertyExpander {
  private final Properties properties;

  public PropertyExpander(Properties properties) {
    this.properties = properties;

  }

  public String expand(String before) {
    return (new Expander()).expandPropertiesIn(before);
  }

  private class Expander {
    private boolean maybeInPropertyName = false;
    private boolean inPropertyName = false;

    private StringBuilder expanded = new StringBuilder();
    private StringBuilder propertyName = new StringBuilder();

    public String expandPropertiesIn(String before) {
      for (char c : before.toCharArray()) {
        if (c == '$' && maybeInPropertyName) {
          // Two dollar signs together
          appendToExpandedOrPropertyName('$');

        } else if (c == '$' && !inPropertyName && !maybeInPropertyName) {
          maybeInPropertyName = true;

        } else if (c == '{' && maybeInPropertyName && !inPropertyName) {
          inPropertyName = true;

        } else if (c == '}' && inPropertyName) {
          appendPropertyValueToExpanded();
          maybeInPropertyName = false;
          inPropertyName = false;
          propertyName = new StringBuilder();

        } else {
          if (maybeInPropertyName && !inPropertyName) {
            // The previous character was a $, but the current character is nothing special
            appendToExpandedOrPropertyName('$');
            maybeInPropertyName = false;
          }
          appendToExpandedOrPropertyName(c);
        }

      }

      return expanded.toString();
    }

    private void appendPropertyValueToExpanded() {
      String propertyValue = properties.getProperty(propertyName.toString());
      if (propertyValue == null) {
        // Just leave the property as-is, I guess
        expanded.append("${");
        expanded.append(propertyName.toString());
        expanded.append('}');

      } else {
        expanded.append(propertyValue);
      }
    }

    private void appendToExpandedOrPropertyName(char c) {
      if (inPropertyName) {
        propertyName.append(c);

      } else {
        expanded.append(c);
      }

    }
  }
}
