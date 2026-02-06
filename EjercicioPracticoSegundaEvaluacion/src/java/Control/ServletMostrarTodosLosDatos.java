/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import Modelo.Alumno;
import Modelo.Profesor;
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

/**
 *
 * @author usuario
 */
@WebServlet(name = "ServletMostrarTodosLosDatos", urlPatterns = {"/ServletMostrarTodosLosDatos","/servletmostrartodoslosdatos"})
public class ServletMostrarTodosLosDatos extends HttpServlet {

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
        
        List<Alumno> alumnos = (List<Alumno>) request.getAttribute("alumnos");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletMostrarTodosLosDatos</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletMostrarTodosLosDatos at " + request.getContextPath() + "</h1>");
            out.println("<h1>Alumnos</h1>");
            out.println("<ul>");
            for (Alumno a : alumnos) {
                out.println("<li>" + a.getIdAlumno() + " - " + a.getNombre() + " - " + a.getApellidos()+ " - " + a.getEmail() + " - " + a.getCurso_matriculado() + " - "+ a.getFecha_nac() + "</li>");
            }
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            response.setContentType("text/html;charset=UTF-8");
        
//        try (PrintWriter out = response.getWriter()) {
        try{
            
            String url = "jdbc:mysql://localhost:3306/ejerciciosegundaevaluacion";
            String user = "root";
            String password = "admin";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            
            Statement stmt = con.createStatement();
            
//            ResultSet rs = stmt.executeQuery("SELECT nombre, apellidos FROM Alumno");
            ResultSet rsAlumnos = stmt.executeQuery(
                "SELECT id_alumno, nombre, apellidos, email, curso_matriculado, fecha_nac FROM Alumno"
            );

            List<Alumno> alumnos = new ArrayList<>();
            while (rsAlumnos.next()) {
                alumnos.add(new Alumno(
                    rsAlumnos.getInt("id_alumno"),
                    rsAlumnos.getString("nombre"),
                    rsAlumnos.getString("apellidos"),
                    rsAlumnos.getString("email"),
                    rsAlumnos.getString("curso_matriculado"),
                    rsAlumnos.getDate("fecha_nac")
                ));
            }
            
            request.setAttribute("alumnos", alumnos);
            
            ResultSet rsProf = stmt.executeQuery(
                "SELECT id_profesor, nombre, apellidos, email, password, directiva FROM Profesor"
            );

            List<Profesor> profesores = new ArrayList<>();
            while (rsProf.next()) {
                profesores.add(new Profesor(
                    rsProf.getInt("id_profesor"),
                    rsProf.getString("nombre"),
                    rsProf.getString("apellidos"),
                    rsProf.getString("email"),
                    rsProf.getString("password"),
                    rsProf.getBoolean("directiva")
                ));
            }
            
            request.setAttribute("profesores", profesores);
            
            
            
            request.getRequestDispatcher("/mostrarTodosDatos.jsp").forward(request, response);
            
            
//            out.println("<html><body>");
//            out.println("<h1>Alumnos</h1>");
//            out.println("<ul>");
//            while (rs.next()) {
//                out.println("<li>" + rs.getString("nombre") + " " + rs.getString("apellidos") + "</li>");
//            }
//            out.println("</ul>");
//            out.println("</body></html>");
            
        } catch (ClassNotFoundException ex) {
            System.getLogger(ServletMostrarTodosLosDatos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ServletMostrarTodosLosDatos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
//            processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
