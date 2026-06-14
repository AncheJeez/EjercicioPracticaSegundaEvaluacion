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
import javax.servlet.http.HttpSession;

import Modelo.Profesor;
import Modelo.ProfesorDAO;

/**
 *
 * @author usuario
 */
@WebServlet(name = "Registrarse", urlPatterns = {"/Registrarse","/registrarse"})
public class Registrarse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.removeAttribute("error");
        request.getRequestDispatcher("/Register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        boolean directiva = request.getParameter("directiva") != null;
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            if (Modelo.ProfesorDAO.existsByEmail(email)) {
                request.setAttribute("error", "Correo existe en la base de datos");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }
            Profesor p = new Profesor();
            p.setNombre(nombre);
            p.setApellidos(apellidos);
            p.setEmail(email);
            p.setPassword(password);
            p.setDirectiva(directiva);
            Modelo.ProfesorDAO.insert(p);
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("nombre",nombre);
            httpSession.setAttribute("apellidos",apellidos);
            httpSession.setAttribute("directiva", directiva);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Error interno: " + ex.getMessage());
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
