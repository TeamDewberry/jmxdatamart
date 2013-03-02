package org.jmxdatamart.Loader;/*
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

import java.io.File;
import org.jmxdatamart.common.DBException;

import java.sql.SQLException;
import java.util.ArrayList;


public class Main {
    
    /**
* Command line main method. Loads all hypersql files in the directory passed in args
* according to the configuration in the .ini file also passed in args.
* @param args command line arguments
*/
    public static void main(String[] args) throws SQLException, DBException {
        if (args.length!=3){
            printHelp();
        }
        else{
            if (lookForHelp(args)){
                printHelp();
            }
            else{
                Integer dFormat = lookForDataFormat(args);
                if (dFormat == -1){
                    printHelp();                
                }
                else{
                    String configFile = getConfig(args);
                    ArrayList<File> dbFiles = new ArrayList<File>();
                    if (dFormat == 0){
                        //db files are csv directories
                        dbFiles = getCSVDirList(args);
                        
                        for(File dbfile : dbFiles){
                        
                            System.out.println(dbfile.getPath());
                            
                            
                        }
                    }
                    if (dFormat == 1){
                        // db files are HyperSQL files
                        dbFiles = getDbFileList(args);
                        
                        for(File dbfile : dbFiles){
                            String dbName = dbfile.getPath().substring(0,dbfile.getPath().length()-7);
                            System.out.println(dbfile.getPath().substring(0,dbfile.getPath().length()-7));

                            DB2DB d2d = new DB2DB();
                            LoaderSetting s = d2d.readProperties(configFile);

                            LoaderSetting.DBInfo fileInfo = s.getSource();

                            fileInfo.setDatabaseName(dbName);
                            fileInfo.setJdbcUrl("jdbc:hsqldb:file:" + dbName);

                            s.setSource(fileInfo);                     

                            d2d.loadSetting(s);
                            d2d.copySchema();
                            d2d.importData();
                            d2d.disConnect();
                        }
                    } 
                }
            }
        }
    }
    
    /**
* Prints to System.out the Loader syntax for the command line.
*/
    public static void printHelp(){
        System.out.println("Loader Syntax:");
        System.out.println("Loader -h | h | ? | help , brings up this display");
        System.out.println("Loader config.ini \\dbfiledirpath dataformat");
        System.out.println(" Loader looks for files/dirs in dbfiledirpath");
        System.out.println(" dataformat - (csv | CSV | hsql | HSQL)");
        System.out.println("Example: Loader loaderConfig.ini C:\\Extracted");
    }
    
    /**
* Iterates through the command line arguments looking for .ini file name/path
* @param argArray command line arguments
* @return String file name
*/
    private static String getConfig(String[] argArray){
      String extension;
      for(int i = 0; i<argArray.length; i++){
          extension = argArray[i].substring(argArray[i].length()-3);
          if (extension.equals("ini")){
              return argArray[i];
          }
      }
      return "";
    }
    
    /**
* Iterates through the command line arguments looking for -h | h | ? | help
* @param argArray command line arguments
* @return Boolean True if found in argArray
*/
    private static Boolean lookForHelp(String[] argArray){
        for(int i = 0; i < argArray.length; i++){
          if (argArray[i].equals("-h") | argArray[i].equals("h") | argArray[i].equals("?") | argArray[i].equals("help")){
              return true;
          }
        }
        return false;
    }
    
    /**
     * 
     * @param argArray
     * @return Integer 0 if data format is HyperSQL, 1 if CSV directories
     */
    private static Integer lookForDataFormat(String[] argArray){
        // dataFormat should be replaced in the future with an enum of supported
        // datatypes, searching through args could also be cleaned up.
        Integer dataFormat = -1;
        for(int i = 0; i < argArray.length; i++){
          if (argArray[i].equals("csv") | argArray[i].equals("CSV")){
              dataFormat = 0;
          }
          if (argArray[i].equals("hsql") | argArray[i].equals("HQSL")){
              dataFormat = 1;
          }
        }
        return dataFormat;
    }
    
    /**
* Iterates through the command line arguments looking for a directory
* The method then iterates through all files in that directory looking for .script files
* @param argArray command line arguments
* @return ArrayList<File> list of hypersql db files
*/
    private static ArrayList<File> getDbFileList(String[] argArray) {
        ArrayList<File> dbList = new ArrayList<File>();
        for (int i = 0; i < argArray.length; i++) {
            //find db directory argument
            File folder = new File(argArray[i]);
            if (folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                for (int j = 0; j < listOfFiles.length; j++) {
                    //find db files files in the directory
                    if (listOfFiles[j].isFile() && listOfFiles[j].getName().endsWith(".script")) {
                        if (!dbList.contains(listOfFiles[j])) {
                            dbList.add(listOfFiles[j]);
                        }
                    }
                }
            }
        }
        return dbList;
    }
    
    /**
     * Iterates through the command line arguments looking for directory
     * The method then iterates through all files/directories in that directory looking 
     * for directories that start with "Extractor"
     * @param argArray
     * @return ArrayList<File> list of CSV directories
     */
    private static ArrayList<File> getCSVDirList(String[] argArray) {
        ArrayList<File> dbList = new ArrayList<File>();
        for (int i = 0; i < argArray.length; i++) {
            //find db directory argument
            File folder = new File(argArray[i]);
            if (folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                for (int j = 0; j < listOfFiles.length; j++) {
                    //find db files files in the directory
                    if (listOfFiles[j].isDirectory() && listOfFiles[j].getName().startsWith("Extractor")) {
                        if (!dbList.contains(listOfFiles[j])) {
                            dbList.add(listOfFiles[j]);
                        }
                    }
                }
            }
        }
        return dbList;
    }
}


//public class Main {
//    public static void main(String[] args) throws SQLException, DBException {
//        if (args.length!=1){
//            System.err.println("Must have one argument");
//            //System.exit(0);
//        }
//        //read file ...
//
//        String arg1 = "jmx-loader/src/main/java/org/jmxdatamart/Loader/loaderconfig.ini";
//
//        DB2DB d2d = new DB2DB();
//        LoaderSetting s =d2d.readProperties(arg1);
//        d2d.loadSetting(s);
//        d2d.copySchema();
//        d2d.importData();
//        d2d.disConnect();
//        System.out.println("done");
//    }
//
//
//}
