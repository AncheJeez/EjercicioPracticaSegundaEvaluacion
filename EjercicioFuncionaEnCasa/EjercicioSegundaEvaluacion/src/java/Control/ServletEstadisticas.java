/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import Conectividad.ConectarseBD;
import Modelo.Estadistica;
import Modelo.Profesor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

/**
 *
 * @author AndJe
 */
@WebServlet(name = "ServletEstadisticas", urlPatterns = {"/ServletEstadisticas"})
public class ServletEstadisticas extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletEstadisticas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletEstadisticas at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        List<Estadistica> alumnosPorEmpresa = new ArrayList<>();
        List<Estadistica> alumnosPorCurso = new ArrayList<>();

        try (Connection con = ConectarseBD.conectarse(null)) {
            String sqlEmpresa = "SELECT e.nombre AS empresa, COUNT(p.id_practica) AS num_alumnos "
                              + "FROM Empresa e LEFT JOIN Practica p ON p.empresa_id = e.id_empresa "
                              + "GROUP BY e.nombre";
            try (PreparedStatement ps = con.prepareStatement(sqlEmpresa);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alumnosPorEmpresa.add(new Estadistica(
                        rs.getString("empresa"),
                        rs.getInt("num_alumnos")
                    ));
                }
            }

            String sqlCurso = "SELECT a.curso_matriculado AS curso, COUNT(*) AS num_alumnos "
                            + "FROM Alumno a LEFT JOIN Practica p ON p.alumno_id = a.id_alumno "
                            + "GROUP BY a.curso_matriculado";
            try (PreparedStatement ps = con.prepareStatement(sqlCurso);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alumnosPorCurso.add(new Estadistica(
                        rs.getString("curso"),
                        rs.getInt("num_alumnos")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("alumnosPorEmpresa", alumnosPorEmpresa);
        request.setAttribute("alumnosPorCurso", alumnosPorCurso);

        request.getRequestDispatcher("/estadisticas.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
