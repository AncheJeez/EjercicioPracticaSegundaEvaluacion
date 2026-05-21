package Modelo;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public static List<Curso> listAll() throws Exception {
        List<Curso> list = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT nombre FROM Curso ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Curso c = new Curso();
                c.setNombre(rs.getString("nombre"));
                list.add(c);
            }
        }
        return list;
    }

    public static void deleteByName(String nombre) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Curso WHERE nombre = ?")) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    public static Curso findByName(String nombre) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT nombre FROM Curso WHERE nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Curso c = new Curso();
                    c.setNombre(rs.getString("nombre"));
                    return c;
                }
            }
        }
        return null;
    }

    public static void insert(String nombre) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("INSERT INTO Curso(nombre) VALUES(?)")) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    public static void update(String nuevo, String antiguo) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("UPDATE Curso SET nombre = ? WHERE nombre = ?")) {
            ps.setString(1, nuevo);
            ps.setString(2, antiguo);
            ps.executeUpdate();
        }
    }
}
