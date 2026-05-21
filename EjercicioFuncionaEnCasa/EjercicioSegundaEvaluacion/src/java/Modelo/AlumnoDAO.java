package Modelo;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public static List<Alumno> listAll() throws Exception {
        List<Alumno> list = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac FROM Alumno")) {
            while (rs.next()) {
                list.add(new Alumno(
                    rs.getInt("id_alumno"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("email"),
                    rs.getString("curso_matriculado"),
                    rs.getDate("fecha_nac")
                ));
            }
        }
        return list;
    }

    public static Alumno findById(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac FROM Alumno WHERE id_alumno = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("curso_matriculado"),
                        rs.getDate("fecha_nac")
                    );
                }
            }
        }
        return null;
    }

    public static void delete(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Alumno WHERE id_alumno = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void insert(Alumno a) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("INSERT INTO Alumno (nombre, apellidos, email, curso_matriculado, fecha_nac) VALUES (?,?,?,?,?)")) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellidos());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getCursoMatriculado());
            ps.setDate(5, new java.sql.Date(a.getFechaNac().getTime()));
            ps.executeUpdate();
        }
    }

    public static void update(Alumno a) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("UPDATE Alumno SET nombre=?, apellidos=?, email=?, curso_matriculado=?, fecha_nac=? WHERE id_alumno=?")) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellidos());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getCursoMatriculado());
            ps.setDate(5, new java.sql.Date(a.getFechaNac().getTime()));
            ps.setInt(6, a.getIdAlumno());
            ps.executeUpdate();
        }
    }
}
