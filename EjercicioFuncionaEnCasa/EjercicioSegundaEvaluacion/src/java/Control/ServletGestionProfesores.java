/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import Modelo.Profesor;
import Modelo.ProfesorDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
            try {
                List<Profesor> profesores = ProfesorDAO.listAll();
                request.setAttribute("listaProfesores", profesores);
                request.getRequestDispatcher("/gestion_profesores.jsp").forward(request, response);
            } catch (Exception e) {
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

            try {
                ProfesorDAO.delete(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error al borrar el profesor: " + e.getMessage());
                Logger.getLogger(ServletGestionProfesores.class.getName()).log(Level.SEVERE, null, e);
            }

            response.sendRedirect("ServletGestionProfesores");
        }
        
    private void editarProfesor(HttpServletRequest request, HttpServletResponse response, String id)
        throws ServletException, IOException {

        try {
            if (id != null && !id.isEmpty()) {
                Profesor profesor = ProfesorDAO.findById(Integer.parseInt(id));
                request.setAttribute("profesor", profesor);
            }
            request.getRequestDispatcher("/crear_editar_profesores.jsp").forward(request, response);
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

        try {
            Profesor p = new Profesor();
            p.setNombre(nombre);
            p.setApellidos(apellidos);
            p.setEmail(email);
            p.setPassword(password);
            p.setDirectiva(directiva);

            if (id == null || id.isEmpty()) {
                ProfesorDAO.insert(p);
            } else {
                p.setIdProfesor(Integer.parseInt(id));
                ProfesorDAO.update(p);
            }

            response.sendRedirect("ServletGestionProfesores");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar el profesor: " + e.getMessage());
            request.getRequestDispatcher("/crear_editar_profesores.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
