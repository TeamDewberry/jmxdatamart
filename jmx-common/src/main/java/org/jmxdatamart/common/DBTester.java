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

package org.jmxdatamart.common;


import java.sql.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class DBTester {
    public static void main(String[] args){

        Connection conn = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;

        DerbyHandler derby = new DerbyHandler();
        derby.loadDriver(derby.getDriver());

        String dbName = "derbyDB";
        //test commit
        try
        {
            Properties props = new Properties();
            props.put("user", "user1");
            props.put("password", "user1");

            conn = derby.connectDatabase( dbName, props);
            st = conn.createStatement();

            conn.setAutoCommit(false);
            if (!derby.tableExists("players", conn))
                st.execute("create table players(playerid int NOT NULL GENERATED " +
                        "ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), name varchar(40), primary key(playerid))");
            System.out.println("Created table PLAYERS");

            ps = conn.prepareStatement("insert into players(name) values (?)");

            ps.setString(1, "Jordon");
            ps.executeUpdate();

            ps.setString(1, "James");
            ps.executeUpdate();

            conn.commit();

            ps = conn.prepareStatement("update players set name=? where name=?");
            ps.setString(1,"Malone");
            ps.setString(2,"Jordon");
            ps.executeUpdate();

            conn.rollback(); //won't replace Jordon with Malone

            conn.setAutoCommit(true);

            ps = conn.prepareStatement("update players set name=? where name=?");
            ps.setString(1,"Kobe");
            ps.setString(2,"James");
            ps.executeUpdate(); //replace James with Kobe

            st.execute("ALTER TABLE players ADD comment varchar(200)");
            ps = conn.prepareStatement("insert into players(name,comment) values (?,?)");
            ps.setString(1, "David");
            ps.setString(2, "Rookie");
            ps.executeUpdate();

            rs = st.executeQuery("SELECT * FROM players ORDER BY playerid");
            int i=0;
            while(rs.next()){
                System.out.println(rs.getInt(1)+ "\t\t" + rs.getString(2) + "\t\t" +  (rs.getString(3)==null?"":rs.getString(3)));
                i++;
            }
            System.out.println("Total records: " + i);

            st.execute("drop table players");


        }
        catch (Exception e){
            System.err.print(e.getMessage());
        }
        finally {

            derby.disconnectDatabase(rs,st,ps,conn) ;
            derby.shutdownDatabase(dbName); //we should shut down a embedded Derby DB after using it
        }

    }
}
