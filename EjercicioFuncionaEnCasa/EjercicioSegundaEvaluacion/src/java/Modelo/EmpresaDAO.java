package Modelo;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public static List<Empresa> listAll() throws Exception {
        List<Empresa> empresas = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_empresa, nombre, descripcion, nombre_completo, email_tutor_laboral FROM Empresa")) {
            while (rs.next()) {
                empresas.add(new Empresa(
                    rs.getInt("id_empresa"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("nombre_completo"),
                    rs.getString("email_tutor_laboral")
                ));
            }
        }
        return empresas;
    }

    public static Empresa findById(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Empresa WHERE id_empresa = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("nombre_completo"),
                        rs.getString("email_tutor_laboral")
                    );
                }
            }
        }
        return null;
    }

    public static Empresa findByName(String name) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Empresa WHERE nombre = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("nombre_completo"),
                        rs.getString("email_tutor_laboral")
                    );
                }
            }
        }
        return null;
    }

    public static void insertIfNotExistsByName(String name) throws Exception {
        // Minimal insert that only sets the name if it doesn't exist
        if (name == null || name.trim().isEmpty()) return;
        Empresa existing = findByName(name);
        if (existing != null) return;
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("INSERT INTO Empresa (nombre) VALUES (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Empresa WHERE id_empresa = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void insert(Empresa e) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("INSERT INTO Empresa (nombre, descripcion, nombre_completo, email_tutor_laboral) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getNombre_completo());
            ps.setString(4, e.getEmail_tutor_laboral());
            ps.executeUpdate();
        }
    }

    public static void update(Empresa e) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("UPDATE Empresa SET nombre = ?, descripcion = ?, nombre_completo = ?, email_tutor_laboral = ? WHERE id_empresa = ?")) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getNombre_completo());
            ps.setString(4, e.getEmail_tutor_laboral());
            ps.setInt(5, e.getId_empresa());
            ps.executeUpdate();
        }
    }
}
