package Conectividad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarseBD {

    public ConectarseBD() {
    }

    public static Connection conectarse(Connection con) throws ClassNotFoundException, SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EjercicioSegundaEvaluacion", "root", "admin");
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return con;

    }
    
}
