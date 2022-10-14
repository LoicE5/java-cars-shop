import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private final String protocol;
    private final String host;
    private final int port;
    private final String db_name;
    private final String username;
    private final String password;

    public Database(String protocol, String host, int port, String db_name, String username, String password){
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.db_name = db_name;
        this.username = username;
        this.password = password;
    }

    public ArrayList<HashMap<String, String>> query(String sql){
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:"+this.protocol+"://"+this.host+":"+this.port+"/"+this.db_name
                    ,this.username,this.password);

            Statement stmt=con.createStatement();

            if(sql.substring(0,10).contains("insert") || sql.substring(0,10).contains("update") || sql.substring(0,10).contains("delete")){
                stmt.executeUpdate(sql);
                return new ArrayList<HashMap<String, String>>();
            }

            ResultSet rs=stmt.executeQuery(sql);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            return rsToDict(rs, rsMetaData);

        } catch(Exception e){
            System.out.println(e);
            return new ArrayList<HashMap<String, String>>();
        }
    }

    private ArrayList<HashMap<String, String>> rsToDict(ResultSet rs, ResultSetMetaData metaData){
        ArrayList result = new ArrayList();

        try {

            while(rs.next()){
                int i = 1;
                HashMap itemList = new HashMap();

                while(true){
                    try {
                        itemList.put(metaData.getColumnName(i),rs.getString(i++));
                    } catch(Exception e){
                        break;
                    }
                }

                result.add(itemList);
            }

        } catch (SQLException e){
            System.out.println(e);
        }

        return result;
    }

}