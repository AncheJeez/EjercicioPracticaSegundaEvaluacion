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

import Modelo.Alumno;
import Modelo.AlumnoDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        try {
            Modelo.AlumnoDAO.delete(Integer.parseInt(id));
            response.sendRedirect("ServletGestionAlumnos");
            return;
        } catch (Exception e) {
            // capture stacktrace and show the view with error details
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al borrar alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            // fallthrough to listar para mostrar la vista con el error
        }
        listarAlumnos(request, response);
    }
    
    private void listarAlumnos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Alumno> alumnos = Modelo.AlumnoDAO.listAll();
            request.setAttribute("alumnos", alumnos);
            request.getRequestDispatcher("/gestion_alumnos.jsp").forward(request, response);
        } catch (Exception e) {
            // capture stacktrace and show the gestion view with details
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al listar alumnos: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            request.getRequestDispatcher("/gestion_alumnos.jsp").forward(request, response);
        }
    }
    
    private void editarAlumno(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        try {
            Alumno alumno = Modelo.AlumnoDAO.findById(Integer.parseInt(id));
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateada = alumno.getFechaNac() != null ? sdf.format(alumno.getFechaNac()) : "";
            request.setAttribute("alumno", alumno);
            request.setAttribute("fechaFormateada", fechaFormateada);
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
        } catch (Exception e) {
            // capture stacktrace and forward to edit view with details
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al obtener/editar alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
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
        String grupo = request.getParameter("grupo");

        try {
            java.sql.Date fecha = fechaStr != null && !fechaStr.isEmpty() ? java.sql.Date.valueOf(fechaStr) : null;

            if (id != null && !id.isEmpty()) {
                Alumno alumno = new Alumno(Integer.parseInt(id), nombre, apellidos, email, curso, fecha, grupo);
                Modelo.AlumnoDAO.update(alumno);
            } else {
                Alumno alumno = new Alumno(0, nombre, apellidos, email, curso, fecha, grupo);
                Modelo.AlumnoDAO.insert(alumno);
            }

        } catch (Exception e) {
            // capture stacktrace and forward back to create/edit view with submitted data
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al guardar alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            // repopular campos en el formulario
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellidos", apellidos);
            request.setAttribute("email", email);
            request.setAttribute("curso_matriculado", curso);
            request.setAttribute("fecha_nac", fechaStr);
            request.setAttribute("grupo", grupo);
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
            return;
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
