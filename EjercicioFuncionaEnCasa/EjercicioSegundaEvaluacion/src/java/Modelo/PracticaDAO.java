package Modelo;

import Conectividad.ConectarseBD;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PracticaDAO {

    public static List<Practica> listAllWithDetails() throws Exception {
        List<Practica> practicas = new ArrayList<>();
        String sql = "SELECT p.id_practica, a.id_alumno, a.nombre AS alumno_nombre, a.apellidos AS alumno_apellidos, e.id_empresa, e.nombre AS empresa_nombre, p.fecha_comienzo, p.fecha_finalizacion, p.comentarios FROM Practica p JOIN Alumno a ON p.alumno_id = a.id_alumno JOIN Empresa e ON p.empresa_id = e.id_empresa";
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Alumno alumno = new Alumno(0, rs.getString("alumno_nombre"), rs.getString("alumno_apellidos"), "", "", null, null);
                Empresa empresa = new Empresa(rs.getInt("id_empresa"), rs.getString("empresa_nombre"), "", "", "");
                Practica practica = new Practica(rs.getInt("id_practica"), alumno, empresa, rs.getDate("fecha_comienzo"), rs.getDate("fecha_finalizacion"), rs.getString("comentarios"));
                practicas.add(practica);
            }
        }
        return practicas;
    }

    public static Practica findByIdWithDetails(int id) throws Exception {
        String sql = "SELECT p.id_practica, p.fecha_comienzo, p.fecha_finalizacion, p.comentarios, " +
                             "a.id_alumno, a.nombre, a.apellidos, a.email, a.curso_matriculado, a.fecha_nac, " +
                     "e.id_empresa, e.nombre, e.descripcion,  e.nombre_completo, e.email_tutor_laboral " +
                     "FROM Practica p " +
                     "JOIN Alumno a ON p.alumno_id = a.id_alumno " +
                     "JOIN Empresa e ON p.empresa_id = e.id_empresa " +
                     "WHERE p.id_practica = ?";
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                                    Alumno alumno = new Alumno(rs.getInt("id_alumno"), rs.getString("nombre"), rs.getString("apellidos"), rs.getString("email"), rs.getString("curso_matriculado"), rs.getDate("fecha_nac"), null);
                    Empresa empresa = new Empresa(rs.getInt("id_empresa"), rs.getString("nombre"), rs.getString("descripcion"), rs.getString("nombre_completo"), rs.getString("email_tutor_laboral"));
                    return new Practica(rs.getInt("id_practica"), alumno, empresa, rs.getDate("fecha_comienzo"), rs.getDate("fecha_finalizacion"), rs.getString("comentarios"));
                }
            }
        }
        return null;
    }

    public static void delete(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Practica WHERE id_practica = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void deleteAll() throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Practica")) {
            ps.executeUpdate();
        }
    }

    public static void insert(Practica p) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("INSERT INTO Practica (alumno_id, empresa_id, fecha_comienzo, fecha_finalizacion, comentarios) VALUES (?, ?, ?, ?, ?)") ) {
            ps.setInt(1, p.getAlumno().getIdAlumno());
            ps.setInt(2, p.getEmpresa().getId_empresa());
            ps.setDate(3, p.getFecha_comienzo() != null ? new java.sql.Date(p.getFecha_comienzo().getTime()) : null);
            ps.setDate(4, p.getFecha_finalizacion() != null ? new java.sql.Date(p.getFecha_finalizacion().getTime()) : null);
            ps.setString(5, p.getComentarios());
            ps.executeUpdate();
        }
    }

    public static void update(Practica p) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("UPDATE Practica SET alumno_id=?, empresa_id=?, fecha_comienzo=?, fecha_finalizacion=?, comentarios=? WHERE id_practica=?")) {
            ps.setInt(1, p.getAlumno().getIdAlumno());
            ps.setInt(2, p.getEmpresa().getId_empresa());
            ps.setDate(3, p.getFecha_comienzo() != null ? new java.sql.Date(p.getFecha_comienzo().getTime()) : null);
            ps.setDate(4, p.getFecha_finalizacion() != null ? new java.sql.Date(p.getFecha_finalizacion().getTime()) : null);
            ps.setString(5, p.getComentarios());
            ps.setInt(6, p.getId_practica());
            ps.executeUpdate();
        }
    }

    public static List<Alumno> listAlumnosForSelect() throws Exception {
        List<Alumno> listaAlumnos = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             ResultSet rs = con.createStatement().executeQuery("SELECT id_alumno, nombre, apellidos FROM Alumno")) {
            while (rs.next()) {
                listaAlumnos.add(new Alumno(rs.getInt("id_alumno"), rs.getString("nombre"), rs.getString("apellidos"), "", "", null, null));
            }
        }
        return listaAlumnos;
    }

    public static List<Empresa> listEmpresasForSelect() throws Exception {
        List<Empresa> listaEmpresas = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             ResultSet rs = con.createStatement().executeQuery("SELECT id_empresa, nombre AS nombre, descripcion, nombre_completo, email_tutor_laboral FROM Empresa")) {
            while (rs.next()) {
                listaEmpresas.add(new Empresa(rs.getInt("id_empresa"), rs.getString("nombre"), rs.getString("descripcion"), rs.getString("nombre_completo"), rs.getString("email_tutor_laboral")));
            }
        }
        return listaEmpresas;
    }

    public static void writeCsv(PrintWriter writer) throws Exception {
        // Two-section CSV: first ALUMNOS then PRACTICAS
        writer.println("#ALUMNOS");
        writer.println("id_alumno,Nombre,Apellidos,Email,FechaNacimiento,Curso,Grupo");
        String sqlAl = "SELECT id_alumno, nombre, apellidos, email, fecha_nac, curso_matriculado, grupo FROM Alumno ORDER BY id_alumno";
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement(sqlAl);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String linea = rs.getInt("id_alumno") + "," +
                               escapeCsv(rs.getString("nombre")) + "," +
                               escapeCsv(rs.getString("apellidos")) + "," +
                               escapeCsv(rs.getString("email")) + "," +
                               rs.getDate("fecha_nac") + "," +
                               escapeCsv(rs.getString("curso_matriculado")) + "," +
                               escapeCsv(rs.getString("grupo"));
                writer.println(linea);
            }
        }

        writer.println("#PRACTICAS");
        writer.println("id_practica,alumno_email,empresa_id,empresa_nombre,fecha_comienzo,fecha_finalizacion,comentarios");
        String sqlPr = "SELECT p.id_practica, a.email AS alumno_email, p.empresa_id, e.nombre AS empresa_nombre, p.fecha_comienzo, p.fecha_finalizacion, p.comentarios " +
                       "FROM Practica p LEFT JOIN Alumno a ON p.alumno_id = a.id_alumno LEFT JOIN Empresa e ON p.empresa_id = e.id_empresa";
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement(sqlPr);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String linea = rs.getInt("id_practica") + "," +
                               escapeCsv(rs.getString("alumno_email")) + "," +
                               rs.getString("empresa_id") + "," +
                               escapeCsv(rs.getString("empresa_nombre")) + "," +
                               rs.getDate("fecha_comienzo") + "," +
                               rs.getDate("fecha_finalizacion") + "," +
                               escapeCsv(rs.getString("comentarios"));
                writer.println(linea);
            }
        }
    }

    private static String escapeCsv(String valor) {
        if (valor == null) return "";
        String v = valor.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v + "\"";
        } else {
            return v;
        }
    }
}
