package Modelo;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para operaciones relacionadas con Profesor.
 */
public class ProfesorDAO {

    public static Profesor authenticate(String email, String password) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT id_profesor, nombre, apellidos, email, password, directiva FROM Profesor WHERE email = ? AND password = ?")) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Profesor p = new Profesor();
                    p.setIdProfesor(rs.getInt("id_profesor"));
                    p.setNombre(rs.getString("nombre"));
                    p.setApellidos(rs.getString("apellidos"));
                    p.setEmail(rs.getString("email"));
                    p.setPassword(rs.getString("password"));
                    p.setDirectiva(rs.getBoolean("directiva"));
                    return p;
                }
            }
        }
        return null;
    }

    public static java.util.List<Profesor> listAll() throws Exception {
        java.util.List<Profesor> list = new java.util.ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_profesor, nombre, apellidos, email, password, directiva FROM Profesor")) {
            while (rs.next()) {
                list.add(new Profesor(rs.getInt("id_profesor"), rs.getString("nombre"), rs.getString("apellidos"), rs.getString("email"), rs.getString("password"), rs.getBoolean("directiva")));
            }
        }
        return list;
    }

    public static Profesor findById(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT id_profesor, nombre, apellidos, email, password, directiva FROM Profesor WHERE id_profesor = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Profesor(rs.getInt("id_profesor"), rs.getString("nombre"), rs.getString("apellidos"), rs.getString("email"), rs.getString("password"), rs.getBoolean("directiva"));
                }
            }
        }
        return null;
    }

    public static void delete(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Profesor WHERE id_profesor = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void insert(Profesor p) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("INSERT INTO Profesor (nombre, apellidos, email, password, directiva) VALUES (?,?,?,?,?)")) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getPassword());
            ps.setBoolean(5, p.getDirectiva());
            ps.executeUpdate();
        }
    }

    public static boolean existsByEmail(String email) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT email FROM Profesor WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void update(Profesor p) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("UPDATE Profesor SET nombre=?, apellidos=?, email=?, password=?, directiva=? WHERE id_profesor=?")) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getPassword());
            ps.setBoolean(5, p.getDirectiva());
            ps.setInt(6, p.getIdProfesor());
            ps.executeUpdate();
        }
    }
}
