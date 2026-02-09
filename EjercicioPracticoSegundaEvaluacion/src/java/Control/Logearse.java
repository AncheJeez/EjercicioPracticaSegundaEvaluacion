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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author usuario
 */
@WebServlet(name = "Logearse", urlPatterns = {"/Logearse","/logearse"})
public class Logearse extends HttpServlet {

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
            out.println("<title>Servlet Logearse</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Logearse at " + request.getContextPath() + "</h1>");
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

        request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
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
//        processRequest(request, response);
        String email = request.getParameter("email");
        String passwordUser = request.getParameter("password");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rsUsuario = null;
        try{
            String url = "jdbc:mysql://localhost:3306/ejerciciosegundaevaluacion";
            String user = "root";
            String password = "admin";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            
            String sql = "SELECT nombre, apellidos, email, password FROM Profesor WHERE email = ? AND password = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, passwordUser);
            rsUsuario = ps.executeQuery();
            
            if(rsUsuario.next()){
                String nombre = rsUsuario.getString("nombre");
                String apellidos = rsUsuario.getString("apellidos");
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("nombre",nombre);
                httpSession.setAttribute("apellidos",apellidos);
                
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }else{
                System.out.println("Usuario no encontrado en la base de datos");
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("LogIn.jsp").forward(request, response);
            }
            
        } catch (ClassNotFoundException ex) {
            System.getLogger(ServletMostrarTodosLosDatos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ServletMostrarTodosLosDatos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } finally {
            try { if(rsUsuario != null) rsUsuario.close(); } catch(Exception e) {}
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
