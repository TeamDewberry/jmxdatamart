/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jmxdatamart.common.DataType;
import org.slf4j.LoggerFactory;

/**
 * Counterpart of CSVWriterm CSVReader will read a csv format file.
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public final class CSVReader {

  private final File folder;
  private final List<File> fileList = new ArrayList<File>();
  private final Map<String, Map<String, DataType>> schema = new TreeMap<String, Map<String, DataType>>();
  private final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVReader.class);
  private BufferedReader br = null;
  private String currenTable = null;

  public CSVReader(String folder) {
    this.folder = new File(folder);
    for (File f : this.folder.listFiles()) {
      String fName = f.getName();
      if (f.isFile() && fName.endsWith(".csv")) {
        fileList.add(f);
      }
    }
    processSchema();
  }

  void processSchema() {
    logger.info("Start getting schema from " + this.folder);
    BufferedReader br;
    FileLoop:
    for (File f : fileList) {
      logger.info("Processing " + f.getName());
      try {
        br = new BufferedReader(new FileReader(f));
      } catch (FileNotFoundException ex) {
        logger.error("Can not open file", ex);
        continue FileLoop;
      }
      try {
        String line = br.readLine();
        if (!(line + ".csv").equals(f.getName())) {
          logger.error(f.getName() + " is malformed");
          continue FileLoop;
        }
        Map<String, DataType> table = new TreeMap<String, DataType>();
        table.put("TimeCollected", DataType.LONG); // BUGBUG: Should be timestamp
        schema.put(line, table);

        while ((line = br.readLine()) != null) {
          String[] fields = parseCSVLine(line);
          if (fields.length % 3 != 1) {
            logger.error(line + " in " + f.getName() + " malformed");
            continue FileLoop;
          }
          for (int i = 1; i < fields.length; i += 3) {
            if (fields[i + 1].equals("~")) {
              if (!table.containsKey(fields[i])) {
                logger.error(line + " in " + f.getName() + " malformed");
                continue FileLoop;
              }
            } else {
              DataType dt = string2DataType(fields[i + 1]);
              if (dt == DataType.UNKNOWN) {
                logger.error("Doesn't support " + fields[i + 1]);
                continue FileLoop;
              } else {
                table.put(fields[i], dt);
              }
            }
          }
        }
      } catch (IOException ex) {
        logger.error("Can not open file", ex);
        continue FileLoop;
      }
    }
  }

  DataType string2DataType(String dataType) {
    for (DataType dt : DataType.values()) {
      if (dt.toString().equals(dataType)) {
        return dt;
      }
    }
    return DataType.UNKNOWN;
  }

  String[] parseCSVLine(String line) {
    StringBuilder sb = new StringBuilder(line);
    sb.deleteCharAt(0).deleteCharAt(sb.length() - 1);
    return sb.toString().split("\",\"");
  }

  public String getFolder() {
    return folder.getPath();
  }

  public List<String> getTableNames() {
    ArrayList<String> tableNames = new ArrayList<String>(this.schema.size());
    for (String s : this.schema.keySet()) {
      tableNames.add(s);
    }
    return tableNames;
  }

  public void open(String table) {
    if (br != null) {
      logger.error("Someone else is reading " + this.currenTable);
      return;
    }
    if (!schema.containsKey(table)) {
      logger.error(table + " doesn't exist");
      return;
    }
    logger.info("Opening table " + table);
    this.currenTable = table;
    try {
      br = new BufferedReader(new FileReader(table + ".csv"));
    } catch (FileNotFoundException ex) {
      logger.error("Error while opening table", ex);
    }

    try {
      br.readLine();
    } catch (IOException ex) {
      logger.error("Error while reading table " + table, ex);
      this.close();
    }
  }

  public void close() {
    try {
      br.close();
    } catch (IOException ex) {
      logger.error("Error while closing file", ex);
      return;
    }
    br = null;
    logger.info(this.currenTable + " closed");
    currenTable = null;
  }

  public Map<String, String> readLine() {
    if (br == null) {
      return null;
    }

    String line;
    try {
      line = br.readLine();
    } catch (IOException ex) {
      logger.error("Error while reading " + currenTable, ex);
      this.close();
      return null;
    }

    if (line == null) {   // end of line
      return null;
    }
    String[] fields = parseCSVLine(line);
    if (fields.length % 3 != 1) {
      logger.error("Error while parsing " + line);
      this.close();
      return null;
    }
    Map<String, String> retVal = new TreeMap<String, String>();
    retVal.put("TimeCollected", fields[0]);
    for (int i = 1; i < fields.length; i += 3) {
      if (!schema.get(currenTable).containsKey(fields[i])) {
        logger.error("Can not recognize field " + fields[i]);
        this.close();
        return null;
      } else {
        retVal.put(fields[i], fields[i + 2]);
      }
    }
    return retVal;
  }

  /**
   * @return the schema
   */
  public Map<String, Map<String, DataType>> getSchema() {
    return schema;
  }

  public static void main(String[] args) throws IOException {
    CSVReader csvr = new CSVReader(System.getProperty("user.dir") + File.separator);
    Map<String, Map<String, DataType>> schema = csvr.getSchema();
    List<String> tables = csvr.getTableNames();
    for (String s : tables) {
      System.out.println(s);
      csvr.open(s);
      Map<String, String> line;
      while ((line = csvr.readLine()) != null) {
        for (Map.Entry<String, String> e : line.entrySet()) {
          System.out.println(e.getKey() + " -> " + e.getValue());
        }
        System.out.println("===***===");
      }
      csvr.close();
    }

    int s = System.in.read();
  }
}
