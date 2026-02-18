/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import Conectividad.ConectarseBD;
import Modelo.Profesor;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AndJe
 */
@WebServlet(name = "ServletGestionProfesores", urlPatterns = {"/ServletGestionProfesores"})
public class ServletGestionProfesores extends HttpServlet {

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
            out.println("<title>Servlet ServletGestionProfesores</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletGestionProfesores at " + request.getContextPath() + "</h1>");
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
            borrarProfesor(request, response, id);
        } else if (id != null && !id.isEmpty()) {
            editarProfesor(request, response, id);
            
        } else if ("new".equals(action)){
//            cargarListas(request);
            request.getRequestDispatcher("/crear_editar_profesores.jsp").forward(request, response);
        }else {
            listarProfesores(request, response);
        }
    }
    
    private void listarProfesores(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            List<Profesor> profesores = new ArrayList<>();

            try (Connection con = ConectarseBD.conectarse(null);
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id_profesor, nombre, apellidos, email, password, directiva FROM Profesor")) {

                while (rs.next()) {
                    Profesor profesor = new Profesor(
                            rs.getInt("id_profesor"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBoolean("directiva")
                    );

                    profesores.add(profesor);
                }

                // Ponemos la lista como atributo del request
                request.setAttribute("listaProfesores", profesores);

                // Redirigimos al JSP que muestra la lista
                request.getRequestDispatcher("/gestion_profesores.jsp").forward(request, response);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("error", "Error al listar los profesores: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }

        private void borrarProfesor(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        if (id == null || id.isEmpty()) {
            response.sendRedirect("ServletGestionProfesores");
            return;
        }

        try (Connection con = ConectarseBD.conectarse(null)) {
            String sqlDelete = "DELETE FROM Profesor WHERE id_profesor = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
                ps.setInt(1, Integer.parseInt(id));
                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Profesor con ID " + id + " borrado correctamente.");
                } else {
                    System.out.println("No se encontró profesor con ID " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al borrar el profesor: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionProfesores.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("ServletGestionProfesores");
    }
        
    private void editarProfesor(HttpServletRequest request, HttpServletResponse response, String id)
        throws ServletException, IOException {

        try (Connection con = ConectarseBD.conectarse(null)) {

            String sql = "SELECT id_profesor, nombre, apellidos, email, password, directiva " +
                         "FROM Profesor WHERE id_profesor = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Profesor profesor = new Profesor(
                        rs.getInt("id_profesor"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("directiva")
                );

                request.setAttribute("profesor", profesor);

                request.getRequestDispatcher("/crear_editar_profesores.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el profesor: " + e.getMessage());
            request.getRequestDispatcher("/crear_editar_profesores.jsp").forward(request, response);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String directivaParam = request.getParameter("directiva");
        boolean directiva = "on".equals(directivaParam);

        try (Connection con = ConectarseBD.conectarse(null)) {

            if (id == null || id.isEmpty()) {
                // INSERT: crear nuevo profesor
                String sqlInsert = "INSERT INTO Profesor (nombre, apellidos, email, password, directiva) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                    ps.setString(1, nombre);
                    ps.setString(2, apellidos);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    ps.setBoolean(5, directiva);
                    ps.executeUpdate();
                }
            } else {
                // UPDATE: editar profesor existente
                String sqlUpdate = "UPDATE Profesor SET nombre=?, apellidos=?, email=?, password=?, directiva=? WHERE id_profesor=?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                    ps.setString(1, nombre);
                    ps.setString(2, apellidos);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    ps.setBoolean(5, directiva);
                    ps.setInt(6, Integer.parseInt(id));
                    ps.executeUpdate();
                }
            }

            response.sendRedirect("ServletGestionProfesores");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar el profesor: " + e.getMessage());
            request.getRequestDispatcher("/crear_editar_profesor.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
