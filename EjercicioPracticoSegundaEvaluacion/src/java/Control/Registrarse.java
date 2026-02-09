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

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author usuario
 */
@WebServlet(name = "Registrarse", urlPatterns = {"/Registrarse","/registrarse"})
public class Registrarse extends HttpServlet {

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
            out.println("<title>Servlet Registrarse</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Registrarse at " + request.getContextPath() + "</h1>");
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
//        processRequest(request, response);
        request.removeAttribute("error");
        request.getRequestDispatcher("/Register.jsp").forward(request, response);
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
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rsSelect = null;
        
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        boolean directiva = request.getParameter("directiva") != null;
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
//        System.out.println(nombre+" "+apellidos+" "+directiva+" "+email+" "+password);
        try{
            con = ConectarseBD.conectarse(con);
            
            String sqlSelect = "SELECT email FROM Profesor WHERE email = ?";
            ps = con.prepareStatement(sqlSelect);
            ps.setString(1,email);
            rsSelect = ps.executeQuery();
            
            if(rsSelect.next()){
                System.out.println("Correo existe en la base de datos");
                request.setAttribute("error", "Correo existe en la base de datos");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
            }else{
                ps = con.prepareStatement("INSERT INTO Profesor ()");
            }
            
            
        } catch (ClassNotFoundException ex) {
            System.getLogger(ServletMostrarTodosLosDatos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ServletMostrarTodosLosDatos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } finally {
            try { if(ps != null) ps.close(); } catch(Exception e) {}
            try { if(con != null) con.close(); } catch(Exception e) {}
        }
        
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
