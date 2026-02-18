package Control;

import Conectividad.ConectarseBD;
import Modelo.Curso;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ServletGestionCurso", urlPatterns = {"/ServletGestionCurso"})
public class ServletGestionCurso extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletGestionCurso</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletGestionCurso at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreCurso = request.getParameter("nombre");
        String action = request.getParameter("action");

        try {
            if ("borrar".equals(action) && nombreCurso != null && !nombreCurso.isEmpty()) {
                borrarCurso(request, response, nombreCurso);

            } else if ("new".equals(action)) {
                request.getRequestDispatcher("/crear_editar_cursos.jsp").forward(request, response);

            } else if (nombreCurso != null && !nombreCurso.isEmpty()) {
                editarCurso(request, response, nombreCurso);

            } else {
                listarCursos(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ServletGestionCurso");
        }
    }

    
    private void listarCursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Curso> cursos = new ArrayList<>();

        try (Connection con = ConectarseBD.conectarse(null)) {

            String sql = "SELECT nombre FROM Curso ORDER BY nombre";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Curso curso = new Curso();
                    curso.setNombre(rs.getString("nombre"));
                    cursos.add(curso);
                }
            }

            request.setAttribute("cursos", cursos);

            request.getRequestDispatcher("/gestion_cursos.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al listar los cursos: " + e.getMessage());
            request.getRequestDispatcher("/gestion_cursos.jsp").forward(request, response);
        }
    }
    
    private void borrarCurso(HttpServletRequest request, HttpServletResponse response, String nombreCurso)
            throws ServletException, IOException {

        if (nombreCurso == null || nombreCurso.isEmpty()) {
            response.sendRedirect("ServletGestionCurso");
            return;
        }

        try (Connection con = ConectarseBD.conectarse(null)) {

            String sqlDelete = "DELETE FROM Curso WHERE nombre = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
                ps.setString(1, nombreCurso);
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    System.out.println("Curso '" + nombreCurso + "' borrado correctamente.");
                } else {
                    System.out.println("No se encontró el curso '" + nombreCurso + "'.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("ServletGestionCurso");
    }

    private void editarCurso(HttpServletRequest request, HttpServletResponse response, String nombre)
            throws ServletException, IOException {

        try (Connection con = ConectarseBD.conectarse(null)) {
            String sql = "SELECT nombre FROM Curso WHERE nombre = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Curso cursoObj = new Curso();
                        cursoObj.setNombre(rs.getString("nombre"));
                        request.setAttribute("curso", cursoObj);
                        request.getRequestDispatcher("/crear_editar_cursos.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("ServletGestionCurso"); // no encontrado
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ServletGestionCurso");
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreNuevo = request.getParameter("nombre");
        String nombreAntiguo = request.getParameter("nombreAntiguo"); // null si es creación

        if (nombreNuevo == null || nombreNuevo.trim().isEmpty()) {
            request.setAttribute("error", "El nombre del curso no puede estar vacío.");
            request.getRequestDispatcher("/crear_editar_cursos.jsp").forward(request, response);
            return;
        }

        try (Connection con = ConectarseBD.conectarse(null)) {
            if (nombreAntiguo == null || nombreAntiguo.isEmpty()) {
                // Crear
                String sqlInsert = "INSERT INTO Curso(nombre) VALUES(?)";
                try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                    ps.setString(1, nombreNuevo);
                    ps.executeUpdate();
                }
            } else {
                // Editar
                String sqlUpdate = "UPDATE Curso SET nombre = ? WHERE nombre = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                    ps.setString(1, nombreNuevo);
                    ps.setString(2, nombreAntiguo);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar el curso: " + e.getMessage());
            request.getRequestDispatcher("/crear_editar_cursos.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("ServletGestionCurso");
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
