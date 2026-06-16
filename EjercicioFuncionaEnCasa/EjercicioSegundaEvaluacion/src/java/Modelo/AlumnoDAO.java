package Modelo;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public static void deleteAll() throws Exception {
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("DELETE FROM Alumno")) {
            ps.executeUpdate();
        }
    }

    public static Alumno findByEmail(String email) throws Exception {
        if (email == null) return null;
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac, grupo FROM Alumno WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("curso_matriculado"),
                        rs.getDate("fecha_nac"),
                        rs.getString("grupo")
                    );
                }
            }
        }
        return null;
    }

    public static Integer findIdByEmail(String email) throws Exception {
        Alumno a = findByEmail(email);
        return a != null ? a.getIdAlumno() : null;
    }

    public static List<String> listCursos() throws Exception {
        List<String> cursos = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT DISTINCT curso_matriculado FROM Alumno WHERE curso_matriculado IS NOT NULL AND curso_matriculado <> ''");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cursos.add(rs.getString(1));
            }
        }
        return cursos;
    }

    private static boolean hasColumn(Connection con, String table, String column) throws Exception {
        try (ResultSet rs = con.getMetaData().getColumns(null, null, table, column)) {
            return rs.next();
        }
    }

    public static List<Alumno> listAll() throws Exception {
        return listByCurso(null);
    }

    public static List<Alumno> listByCurso(String curso) throws Exception {
        List<Alumno> list = new ArrayList<>();
        try (Connection con = ConectarseBD.conectarse(null)) {
            boolean hasGrupo = hasColumn(con, "Alumno", "grupo");
            String sql;
            if (curso == null || curso.trim().isEmpty()) {
                sql = "SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac" + (hasGrupo ? ", grupo" : "") + " FROM Alumno";
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String grupo = hasGrupo ? rs.getString("grupo") : null;
                        list.add(new Alumno(
                            rs.getInt("id_alumno"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("curso_matriculado"),
                            rs.getDate("fecha_nac"),
                            grupo
                        ));
                    }
                }
            } else {
                sql = "SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac" + (hasGrupo ? ", grupo" : "") + " FROM Alumno WHERE curso_matriculado = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, curso);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String grupo = hasGrupo ? rs.getString("grupo") : null;
                            list.add(new Alumno(
                                rs.getInt("id_alumno"),
                                rs.getString("nombre"),
                                rs.getString("apellidos"),
                                rs.getString("email"),
                                rs.getString("curso_matriculado"),
                                rs.getDate("fecha_nac"),
                                grupo
                            ));
                        }
                    }
                }
            }
        }
        return list;
    }

    public static Alumno findById(int id) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null)) {
            boolean hasGrupo = hasColumn(con, "Alumno", "grupo");
            String sql = "SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac" + (hasGrupo ? ", grupo" : "") + " FROM Alumno WHERE id_alumno = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String grupo = hasGrupo ? rs.getString("grupo") : null;
                        return new Alumno(
                            rs.getInt("id_alumno"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("curso_matriculado"),
                            rs.getDate("fecha_nac"),
                            grupo
                        );
                    }
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
        try (Connection con = ConectarseBD.conectarse(null)) {
            boolean hasGrupo = hasColumn(con, "Alumno", "grupo");
            String sql = hasGrupo ? "INSERT INTO Alumno (nombre, apellidos, email, curso_matriculado, fecha_nac, grupo) VALUES (?,?,?,?,?,?)" : "INSERT INTO Alumno (nombre, apellidos, email, curso_matriculado, fecha_nac) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, a.getNombre());
                ps.setString(2, a.getApellidos());
                ps.setString(3, a.getEmail());
                ps.setString(4, a.getCursoMatriculado());
                if (a.getFechaNac() != null) {
                    ps.setDate(5, new java.sql.Date(a.getFechaNac().getTime()));
                } else {
                    ps.setDate(5, null);
                }
                if (hasGrupo) {
                    ps.setString(6, a.getGrupo());
                }
                ps.executeUpdate();
            }
        }
    }

    public static void update(Alumno a) throws Exception {
        try (Connection con = ConectarseBD.conectarse(null)) {
            boolean hasGrupo = hasColumn(con, "Alumno", "grupo");
            String sql = hasGrupo ? "UPDATE Alumno SET nombre=?, apellidos=?, email=?, curso_matriculado=?, fecha_nac=?, grupo=? WHERE id_alumno=?" : "UPDATE Alumno SET nombre=?, apellidos=?, email=?, curso_matriculado=?, fecha_nac=? WHERE id_alumno=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, a.getNombre());
                ps.setString(2, a.getApellidos());
                ps.setString(3, a.getEmail());
                ps.setString(4, a.getCursoMatriculado());
                if (a.getFechaNac() != null) {
                    ps.setDate(5, new java.sql.Date(a.getFechaNac().getTime()));
                } else {
                    ps.setDate(5, null);
                }
                if (hasGrupo) {
                    ps.setString(6, a.getGrupo());
                    ps.setInt(7, a.getIdAlumno());
                } else {
                    ps.setInt(6, a.getIdAlumno());
                }
                ps.executeUpdate();
            }
        }
    }
}
