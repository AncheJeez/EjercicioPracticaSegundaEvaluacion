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

import java.util.List;

import Modelo.PracticaDAO;
import Modelo.Alumno;
import Modelo.Empresa;
import Modelo.Practica;
import Modelo.Email;
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

        try {
            Alumno alumno = new Alumno();
            alumno.setIdAlumno(Integer.parseInt(alumnoId));
            Empresa empresa = new Empresa();
            empresa.setId_empresa(Integer.parseInt(empresaId));
            Practica practica = new Practica();
            practica.setAlumno(alumno);
            practica.setEmpresa(empresa);
            practica.setFecha_comienzo(java.sql.Date.valueOf(fechaInicio));
            practica.setFecha_finalizacion(java.sql.Date.valueOf(fechaFin));
            practica.setComentarios(comentarios);

            if (id == null || id.isEmpty()) {
                PracticaDAO.insert(practica);
            } else {
                practica.setId_practica(Integer.parseInt(id));
                PracticaDAO.update(practica);
            }

            if ("on".equals(enviarCorreo) && emailDestinatario != null && !emailDestinatario.isEmpty()) {
                String remitente = "sanchez.gonzalez.andres.jesus@iescamas.es";      // tu correo
                String password = "";             // contraseña o app password

                Email email = new Email(remitente, password, emailDestinatario, "Registro de Práctica", "Hola, se ha registrado tu práctica correctamente.");
                try {
                    email.enviarEsteEmail();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    request.setAttribute("errorCorreo", "No se pudo enviar el correo: " + ex.getMessage());
                }
            }

            response.sendRedirect("ServletGestionPracticas");
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al guardar la práctica: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            request.getRequestDispatcher("/crear_editar_practicas.jsp").forward(request, response);
        }
    }
    
    private void cargarListas(HttpServletRequest request) {
        try {
            request.setAttribute("listaAlumnos", PracticaDAO.listAlumnosForSelect());
            request.setAttribute("listaEmpresas", PracticaDAO.listEmpresasForSelect());
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al cargar listas auxiliares: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            // allow views to render; lists may be null
        }
    }

    
    private void listarPractica(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Practica> practicas = PracticaDAO.listAllWithDetails();
            request.setAttribute("practicas", practicas);
            cargarListas(request);
            request.getRequestDispatcher("/gestion_practicas.jsp").forward(request, response);
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al listar prácticas: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            request.getRequestDispatcher("/gestion_practicas.jsp").forward(request, response);
        }
    }

    private void editarPractica(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        try {
            Practica practica = PracticaDAO.findByIdWithDetails(Integer.parseInt(id));
            if (practica != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioFormateada = practica.getFecha_comienzo() != null ? sdf.format(practica.getFecha_comienzo()) : null;
                String fechaFinFormateada = practica.getFecha_finalizacion() != null ? sdf.format(practica.getFecha_finalizacion()) : null;
                request.setAttribute("practica", practica);
                request.setAttribute("fechaInicioFormateada", fechaInicioFormateada);
                request.setAttribute("fechaFinFormateada", fechaFinFormateada);
                cargarListas(request);
                request.getRequestDispatcher("/crear_editar_practicas.jsp").forward(request, response);
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al obtener/editar práctica: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            request.getRequestDispatcher("/crear_editar_practicas.jsp").forward(request, response);
        }
    }

    private void borrarPractica(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        try {
            PracticaDAO.delete(Integer.parseInt(id));
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al borrar práctica: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            request.getRequestDispatcher("/gestion_practicas.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("ServletGestionPracticas");
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
