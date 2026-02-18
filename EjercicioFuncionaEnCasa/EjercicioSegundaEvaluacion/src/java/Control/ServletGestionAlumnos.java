/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Conectividad.ConectarseBD;
import Modelo.Alumno;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AndJe
 */
@WebServlet(name = "ServletGestionAlumnos", urlPatterns = {"/ServletGestionAlumnos"})
public class ServletGestionAlumnos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletGestionAlumnos</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletGestionAlumnos at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");

        if ("borrar".equals(action)) {
            borrarAlumno(request, response, id);
        } else if (id != null && !id.isEmpty()) {
            editarAlumno(request, response, id);
        } else {
            listarAlumnos(request, response);
        }
    }
    
    private void borrarAlumno(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        try (Connection con = ConectarseBD.conectarse(null)) {
            String sqlDelete = "DELETE FROM Alumno WHERE id_alumno = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
                ps.setInt(1, Integer.parseInt(id));
                ps.executeUpdate();
                System.out.println("Alumno con ID " + id + " borrado correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("ServletGestionAlumnos");
    }
    
    private void listarAlumnos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       List<Alumno> alumnos = new ArrayList<>();
        
        // Obtener datos de la base de datos
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac FROM Alumno")) {

            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("curso_matriculado"),
                        rs.getDate("fecha_nac")
                );
                alumnos.add(alumno);
            }
            request.setAttribute("alumnos", alumnos);
            request.getRequestDispatcher("/gestion_alumnos.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void editarAlumno(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        try (Connection con = ConectarseBD.conectarse(null)) {

            String sql = "SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac FROM Alumno WHERE id_alumno = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("curso_matriculado"),
                        rs.getDate("fecha_nac")
                );

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = sdf.format(alumno.getFechaNac());

                request.setAttribute("alumno", alumno);
                request.setAttribute("fechaFormateada", fechaFormateada);

                request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String curso = request.getParameter("curso_matriculado");
        String fechaStr = request.getParameter("fecha_nac");

        try (Connection con = ConectarseBD.conectarse(null)) {

            java.sql.Date fecha = java.sql.Date.valueOf(fechaStr);

            if (id != null && !id.isEmpty()) {
                String sqlUpdate = "UPDATE Alumno SET nombre=?, apellidos=?, email=?, curso_matriculado=?, fecha_nac=? WHERE id_alumno=?";
                PreparedStatement ps = con.prepareStatement(sqlUpdate);

                ps.setString(1, nombre);
                ps.setString(2, apellidos);
                ps.setString(3, email);
                ps.setString(4, curso);
                ps.setDate(5, fecha);
                ps.setInt(6, Integer.parseInt(id));

                ps.executeUpdate();

            } else {
                String sqlInsert = "INSERT INTO Alumno (nombre, apellidos, email, curso_matriculado, fecha_nac) VALUES (?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sqlInsert);

                ps.setString(1, nombre);
                ps.setString(2, apellidos);
                ps.setString(3, email);
                ps.setString(4, curso);
                ps.setDate(5, fecha);

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("ServletGestionAlumnos");
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
