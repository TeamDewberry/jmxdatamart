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

import org.jmxdatamart.common.DBException;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException, DBException {
        if (args.length!=2){
            printHelp();
        }
        else{
            if (lookForHelp(args)){
                printHelp();                       
            }
            else{
                String configFile = getConfig(args);
            
                for(File dbfile : getDbFileList(args)){
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
    public static void printHelp(){
        System.out.println("Loader Syntax:");
        System.out.println("Loader -h | h | ? | help , brings up this display");
        System.out.println("Loader config.ini \\dbfiledirpath  ");
        System.out.println("    Loader looks for hyperSQL files in dbfiledirpath");
        System.out.println("Example: Loader loaderConfig.ini C:\\Extracted");
    }
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
    private static Boolean lookForHelp(String[] argArray){
        for(int i = 0; i < argArray.length; i++){
          if (argArray[i].equals("-h") | argArray[i].equals("h") | argArray[i].equals("?") | argArray[i].equals("help")){
              return true;
          }
        }
        return false;
    }
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
}
