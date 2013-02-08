package org.jmxdatamart.common;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class HypersqlHandler extends DBHandler {
    private final String driver = "org.hsqldb.jdbcDriver";
    private final String protocol = "jdbc:hsqldb:";
    private final String timeType = "timestamp";
    public String getTimeType() {
		return timeType;
	}

	private DatabaseMetaData metadata =null;

    public void shutdownDatabase(Connection conn) throws SQLException{
        conn.createStatement().execute("SHUTDOWN");
    }

    public Connection connectDatabase(String databaseName,java.util.Properties p) throws SQLException{
            return  DriverManager.getConnection(protocol+databaseName, p);
    }

    public boolean columnExists(String columnName, String tableName, Connection conn) throws  SQLException{
        metadata = conn.getMetaData();
        ResultSet columnNames = metadata.getColumns(null, null, tableName.toUpperCase(), columnName.toUpperCase());
        //System.out.println("****" + tableName + ": " + columnName+ "*****");
        while (columnNames.next()){
            String col = columnNames.getString("COLUMN_NAME");
            //System.out.println("======" + col);
            if (col.equalsIgnoreCase(columnName)){
                columnNames.close();
                return true;
            }
        }
        columnNames.close();
        return false;
    }

    public boolean tableExists(String tableName, Connection conn)  throws SQLException{
        metadata = conn.getMetaData();
        String[] names = { "TABLE"};
        ResultSet tableNames = metadata.getTables( null, null, null, names);

        while( tableNames.next())
        {
            String tab = tableNames.getString( "TABLE_NAME");
            if (tab.equalsIgnoreCase(tableName)){
                tableNames.close();
                return true;
            }
        }
        tableNames.close();
        return false;
    }

    public boolean databaseExists(String databaseName,java.util.Properties p){
        //Maybe it is a dummy way to check if a db exits, need to improve
        try {
            DriverManager.getConnection(protocol+databaseName+";ifexists=true;create=false", p.getProperty("username"),p.getProperty("password"));
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Map<String, Map> getDatabaseSchema(Connection conn) throws SQLException{
        Map<String, Map> databaseSchema =  new HashMap<String,Map>();

        String[] names = { "TABLE"};
        ResultSet tables = conn.getMetaData().getTables(null, null, null, names), columns =null;

        while( tables.next())
        {
            String tab = tables.getString( "TABLE_NAME");
            String schem = tables.getString("table_schem");
            if (!schem.equalsIgnoreCase("public")) continue;


            columns = conn.getMetaData().getColumns(null, null, tab.toUpperCase(), null);
            Map<String, FieldAttribute> fields = new HashMap<String, FieldAttribute>();
            while (columns.next()){
                String col = columns.getString("COLUMN_NAME");
                String typename = "varchar";
                int type = columns.getInt("DATA_TYPE");
                int size = columns.getInt("COLUMN_SIZE") ;
                DataType myType ;
                switch (type){
                    case Types.VARCHAR:
                        myType = DataType.STRING;
                        typename ="varchar";
                        break;
                    case Types.INTEGER:
                        myType = DataType.INT;
                        typename = "integer";
                        break;
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    	typename = "float";
                        myType = DataType.FLOAT; //Ms sqlserver doesn't have type name of "double"
                        break;
                    default:
                        myType = DataType.STRING;
                        typename = "varchar";
                        break;
                }
                FieldAttribute fieldinfo = new FieldAttribute(myType,typename,size);
                fields.put(col,fieldinfo);
            }
            databaseSchema.put(tab,fields);
        }

        if (columns!=null) columns.close();
        if (tables!=null) tables.close();

        return databaseSchema;
    }



    public String getProtocol() {
        return protocol;
    }

    public String getDriver() {
        return driver;
    }
}
