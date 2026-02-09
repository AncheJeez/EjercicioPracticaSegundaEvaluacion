package Conectividad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
/**
 *
 * @author usuario
 */
public class ConectarseBD {

    public ConectarseBD() {
    }

    public static Connection conectarse(Connection con) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/ejerciciosegundaevaluacion", "root", "admin");
    }
    
}
