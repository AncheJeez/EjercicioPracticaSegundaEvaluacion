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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Conectividad.ConectarseBD;
import Modelo.Alumno;
import Modelo.Email;
import Modelo.Empresa;
import Modelo.Practica;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ServletGestionPracticas", urlPatterns = {"/ServletGestionPracticas"})
public class ServletGestionPracticas extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletGestionPracticas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletGestionPracticas at " + request.getContextPath() + "</h1>");
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
            borrarPractica(request, response, id);
        } else if (id != null && !id.isEmpty()) {
            editarPractica(request, response, id);
            
        } else if ("new".equals(action)){
            cargarListas(request);
            request.getRequestDispatcher("/crear_editar_practicas.jsp").forward(request, response);
        }else {
            listarPractica(request, response);
        }
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
        String id = request.getParameter("id");
        String alumnoId = request.getParameter("alumno");
        String empresaId = request.getParameter("empresa");
        String fechaInicio = request.getParameter("fecha_comienzo");
        String fechaFin = request.getParameter("fecha_finalizacion");
        String comentarios = request.getParameter("comentarios");
        
        String enviarCorreo = request.getParameter("enviarCorreo"); // checkbox
        String emailDestinatario = request.getParameter("email");    // input email

        try (Connection con = ConectarseBD.conectarse(null)) {
            if (id == null || id.isEmpty()) {
                // INSERT
                String sqlInsert = "INSERT INTO Practica (alumno_id, empresa_id, fecha_comienzo, fecha_finalizacion, comentarios) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                    ps.setInt(1, Integer.parseInt(alumnoId));
                    ps.setInt(2, Integer.parseInt(empresaId));
                    ps.setDate(3, java.sql.Date.valueOf(fechaInicio));
                    ps.setDate(4, java.sql.Date.valueOf(fechaFin));
                    ps.setString(5, comentarios);
                    ps.executeUpdate();
                }
            } else {
                // UPDATE
                String sqlUpdate = "UPDATE Practica SET alumno_id=?, empresa_id=?, fecha_comienzo=?, fecha_finalizacion=?, comentarios=? WHERE id_practica=?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                    ps.setInt(1, Integer.parseInt(alumnoId));
                    ps.setInt(2, Integer.parseInt(empresaId));
                    ps.setDate(3, java.sql.Date.valueOf(fechaInicio));
                    ps.setDate(4, java.sql.Date.valueOf(fechaFin));
                    ps.setString(5, comentarios);
                    ps.setInt(6, Integer.parseInt(id));
                    ps.executeUpdate();
                }
            }
            
            if ("on".equals(enviarCorreo) && emailDestinatario != null && !emailDestinatario.isEmpty()) {

                String remitente = "sanchez.gonzalez.andres.jesus@iescamas.es";      // tu correo
                String password = "";             // contraseña o app password

                Email email = new Email(
                        remitente,
                        password,
                        emailDestinatario,
                        "Registro de Práctica",
                        "Hola, se ha registrado tu práctica correctamente."
                );

                try {
                    email.enviarEsteEmail();
                    System.out.println("Correo enviado a " + emailDestinatario);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    request.setAttribute("errorCorreo", "No se pudo enviar el correo: " + ex.getMessage());
                }
            }
            
            response.sendRedirect("ServletGestionPracticas");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar la práctica: " + e.getMessage());
            request.getRequestDispatcher("/crear_editar_practicas.jsp").forward(request, response);
        }
    }
    
    private void cargarListas(HttpServletRequest request) {
        try (Connection con = ConectarseBD.conectarse(null)) {

            List<Alumno> listaAlumnos = new ArrayList<>();
            ResultSet rsAlumnos = con.createStatement().executeQuery(
                "SELECT id_alumno, nombre, apellidos FROM Alumno"
            );
            while (rsAlumnos.next()) {
                listaAlumnos.add(new Alumno(
                    rsAlumnos.getInt("id_alumno"),
                    rsAlumnos.getString("nombre"),
                    rsAlumnos.getString("apellidos"),
                    "", "", null
                ));
            }
            request.setAttribute("listaAlumnos", listaAlumnos);

            List<Empresa> listaEmpresas = new ArrayList<>();
            ResultSet rsEmpresas = con.createStatement().executeQuery(
                "SELECT id_empresa, nombre AS nombre, descripcion, nombre_completo, email_tutor_laboral FROM Empresa"
            );
            while (rsEmpresas.next()) {
                listaEmpresas.add(new Empresa(
                    rsEmpresas.getInt("id_empresa"),
                    rsEmpresas.getString("nombre"),
                    rsEmpresas.getString("descripcion"),
                    rsEmpresas.getString("nombre_completo"),
                    rsEmpresas.getString("email_tutor_laboral")
                ));
            }
            request.setAttribute("listaEmpresas", listaEmpresas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void listarPractica(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Practica> practicas = new ArrayList<>();
        
        // Obtener datos de la base de datos
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT p.id_practica, a.nombre AS alumno_nombre, a.apellidos AS alumno_apellidos, e.id_empresa, e.nombre AS empresa_nombre, p.fecha_comienzo, p.fecha_finalizacion, p.comentarios FROM Practica p JOIN Alumno a ON p.alumno_id = a.id_alumno JOIN Empresa e ON p.empresa_id = e.id_empresa")) {

            while (rs.next()) {
                Alumno alumno = new Alumno(
                        0,
                        rs.getString("alumno_nombre"),
                        rs.getString("alumno_apellidos"),
                        "",
                        "",
                        null
                );
                
                Empresa empresa = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("empresa_nombre"), 
                        "",
                        "",
                        "" 
                );
                
                Practica practica = new Practica(
                        rs.getInt("id_practica"),
                        alumno, 
                        empresa, 
                        rs.getDate("fecha_comienzo"),
                        rs.getDate("fecha_finalizacion"),
                        rs.getString("comentarios")
                );
                
                practicas.add(practica);
            }

            request.setAttribute("practicas", practicas);
            cargarListas(request);
            request.getRequestDispatcher("/gestion_practicas.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionPracticas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void editarPractica(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        try (Connection con = ConectarseBD.conectarse(null)) {

            String sql = "SELECT p.id_practica, p.fecha_comienzo, p.fecha_finalizacion, p.comentarios, " +
                         "a.id_alumno, a.nombre, a.apellidos, a.email, a.curso_matriculado, a.fecha_nac, " +
                         "e.id_empresa, e.nombre, e.descripcion,  e.nombre_completo, e.email_tutor_laboral " +
                         "FROM Practica p " +
                         "JOIN Alumno a ON p.alumno_id = a.id_alumno " +
                         "JOIN Empresa e ON p.empresa_id = e.id_empresa " +
                         "WHERE p.id_practica = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // Crear Alumno
                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("curso_matriculado"),
                        rs.getDate("fecha_nac")
                );

                Empresa empresa = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("nombre_completo"),
                        rs.getString("email_tutor_laboral")
                );

                Practica practica = new Practica(
                        rs.getInt("id_practica"),
                        alumno,
                        empresa,
                        rs.getDate("fecha_comienzo"),
                        rs.getDate("fecha_finalizacion"),
                        rs.getString("comentarios")
                );

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

                String fechaInicioFormateada = sdf.format(practica.getFecha_comienzo());
                String fechaFinFormateada = sdf.format(practica.getFecha_finalizacion());

                request.setAttribute("practica", practica);
                request.setAttribute("fechaInicioFormateada", fechaInicioFormateada);
                request.setAttribute("fechaFinFormateada", fechaFinFormateada);

                cargarListas(request);
                request.getRequestDispatcher("/crear_editar_practicas.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void borrarPractica(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        try (Connection con = ConectarseBD.conectarse(null)) {
            String sqlDelete = "DELETE FROM Practica WHERE id_practica = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
                ps.setInt(1, Integer.parseInt(id));
                ps.executeUpdate();
                System.out.println("Practica con ID " + id + " borrado correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionPracticas.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("ServletGestionPracticas");
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
