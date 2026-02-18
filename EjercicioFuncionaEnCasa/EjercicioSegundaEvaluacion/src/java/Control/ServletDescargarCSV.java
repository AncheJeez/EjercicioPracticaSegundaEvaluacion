package Control;

import Conectividad.ConectarseBD;
import Modelo.Alumno;
import Modelo.Practica;
import Modelo.Curso;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletDescargarCSV", urlPatterns = {"/ServletDescargarCSV"})
public class ServletDescargarCSV extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=alumnos_practicas.csv");

        try (PrintWriter writer = response.getWriter();
             Connection con = ConectarseBD.conectarse(null)) {

            writer.println("AlumnoID,Nombre,Apellidos,Email,FechaNacimiento,Curso,PracticaID,Empresa,FechaComienzo,FechaFin,Comentarios");

            String sql = "SELECT a.id_alumno, a.nombre AS alumno_nombre, a.apellidos, a.email AS alumno_email, " +
                         "a.fecha_nac, a.curso_matriculado, " +
                         "p.id_practica, p.fecha_comienzo, p.fecha_finalizacion, p.comentarios, " +
                         "e.nombre AS empresa_nombre " +
                         "FROM Alumno a " +
                         "LEFT JOIN Practica p ON p.alumno_id = a.id_alumno " +
                         "LEFT JOIN Empresa e ON p.empresa_id = e.id_empresa " +
                         "ORDER BY a.id_alumno";

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String linea = rs.getInt("id_alumno") + "," +
                                   escapeCsv(rs.getString("alumno_nombre")) + "," +
                                   escapeCsv(rs.getString("apellidos")) + "," +
                                   escapeCsv(rs.getString("alumno_email")) + "," +
                                   rs.getDate("fecha_nac") + "," +
                                   escapeCsv(rs.getString("curso_matriculado")) + "," +
                                   rs.getString("id_practica") + "," +
                                   escapeCsv(rs.getString("empresa_nombre")) + "," +
                                   rs.getDate("fecha_comienzo") + "," +
                                   rs.getDate("fecha_finalizacion") + "," +
                                   escapeCsv(rs.getString("comentarios"));

                    writer.println(linea);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error al generar el CSV: " + e.getMessage());
        }
    }

    // SACADO DE INTERNET
    // Método simple para escapear comas y comillas en CSV
    private String escapeCsv(String valor) {
        if (valor == null) return "";
        String v = valor.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v + "\"";
        } else {
            return v;
        }
    }
}
