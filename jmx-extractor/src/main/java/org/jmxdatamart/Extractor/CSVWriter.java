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
package org.jmxdatamart.Extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.jmxdatamart.common.CSVCommon;
import org.jmxdatamart.common.DataType;
import org.slf4j.LoggerFactory;

/**
 * Class to handle writing to csv file
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CSVWriter {

  //TODO: handle string
  private final Map<String, Map<String, DataType>> fileList;
  private final String filePath;
  private final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVWriter.class);

  /**
   * Initialize a CSVWriter
   *
   * @param folderLocation output folder location
   */
  public CSVWriter(String folderLocation) {
    fileList = new TreeMap<String, Map<String, DataType>>();
    this.filePath = folderLocation;

  }

  /**
   * Enclose a character sequence in double quote
   *
   * @param cs
   * @return
   */
  static StringBuilder enclose(CharSequence cs) {
    return (new StringBuilder()).
            append(CSVCommon.GENERAL_ENCLOSE).
            append(cs).
            append(CSVCommon.GENERAL_ENCLOSE);
  }

  /**
   * return epoc time as a stringbuilder, enclosed in double quote & append
   * comma at the end
   *
   * @return
   */
  static StringBuilder getEpocTime() {
    Long l = new Long(System.currentTimeMillis());
    return (new StringBuilder(enclose(l.toString())).append(CSVCommon.DELIMITER));
  }

  /**
   * Line up an alias, its datatype & its value in CSV format, the sequence DOES
   * NOT end with an delimiter
   *
   * @param alias alias of the attribute
   * @param dataType data type of the attribute
   * @param value actual value of the attribute
   * @return a character sequence represent all 3 in CSV format.
   */
  static StringBuilder lineUp(String alias, String dataType, String value) {
    StringBuilder sb = new StringBuilder();

    sb.append(enclose(alias))
            .append(CSVCommon.DELIMITER);

    sb.append(enclose(dataType))
            .append(CSVCommon.DELIMITER);

    sb.append(enclose(value));

    return sb;
  }

  /**
   * LineUp all entry in a result set.
   *
   * @param result the result of an extract
   * @return a character sequence represents a line
   */
  StringBuilder lineUpResult(Map<Attribute, Object> result, Map<String, DataType> seen) {

    StringBuilder line = new StringBuilder();

    for (Map.Entry<Attribute, Object> pair : result.entrySet()) {
      String dataType;
      if (!seen.containsKey(pair.getKey().getAlias())) {
        seen.put(pair.getKey().getAlias(), pair.getKey().getDataType());
        dataType = pair.getKey().getDataType().toString();
      } else {
        dataType = "";
      }
      line.append(
              lineUp(pair.getKey().getAlias(),
              dataType,
              pair.getValue().toString()))
              .append(CSVCommon.DELIMITER);
    }
    return line.deleteCharAt(line.length() - 1); // remove the last DELIMINATOR
  }

  /**
   * Write results in a result set to disk
   *
   * @param result result to be written
   */
  public void writeResult(String name, Map<Attribute, Object> result) {
    try {
      BufferedWriter bw;
      bw = new BufferedWriter(
              new FileWriter(
                filePath + name + ".csv",
                true));
      
      Map<String, DataType> seen;
      if (fileList.containsKey(name)) {
        seen = fileList.get(name);
      } else {
        seen = new TreeMap<String, DataType>();   // see the file for the first time
        fileList.put(name, seen);
        bw.close();
        
        File f = new File(filePath + name + ".csv");
        if (f.exists()) {
          f.delete();
        }
        
        bw = new BufferedWriter(
              new FileWriter(
                filePath + name + ".csv",
                true));
        bw.write(name);
        bw.newLine();
      }

      bw.write(
              getEpocTime().append(lineUpResult(result, seen)).toString());
      bw.newLine();
      bw.close();

    } catch (IOException ex) {
      logger.error("Can not open " + filePath, ex);
      throw new RuntimeException(ex);
    }
  }

  /**
   * @return the filePath
   */
  public String getFilePath() {
    return filePath;
  }
}
