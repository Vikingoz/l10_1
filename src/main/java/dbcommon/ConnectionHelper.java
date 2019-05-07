package dbcommon;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    public static Connection getMyPostgresqlConnection() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:postgresql://" +  //db type
                "localhost:" +               //host name
                "5432/" +                    //port
                "postgres?" +                //db name
                "user=postgres&" +           //login
                //"user=postgres" +
                "password=sys";        //password
        return DriverManager.getConnection(url);
    }
}
