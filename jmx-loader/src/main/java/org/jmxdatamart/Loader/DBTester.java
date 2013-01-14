package org.jmxdatamart.Loader;


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

        try
        {
            Properties props = new Properties();
            props.put("user", "user1");
            props.put("password", "user1");

            conn = derby.connectDB(dbName, props);
            st = conn.createStatement();

            conn.setAutoCommit(false);
            if (!derby.tblExists("players", conn))
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

            derby.disconnectDB(rs,st,ps,conn);
            derby.shutdownDB(dbName); //we should shut down a embedded Derby DB after using it
        }

    }
}
