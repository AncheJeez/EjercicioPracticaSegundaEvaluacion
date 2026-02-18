/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import Conectividad.ConectarseBD;
import Modelo.Empresa;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
/**
 *
 * @author AndJe
 */
@WebServlet(name = "ServletGestionEmpresas", urlPatterns = {"/ServletGestionEmpresas"})
public class ServletGestionEmpresas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String action = request.getParameter("action");

        if ("borrar".equals(action)) {
            borrarEmpresa(request, response, id);
        } else if (id != null && !id.isEmpty()) {
            editarEmpresa(request, response, id);
        } else {
            listarEmpresas(request, response);
        }
    }

    private void borrarEmpresa(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        try (Connection con = ConectarseBD.conectarse(null)) {
            String sqlDelete = "DELETE FROM Empresa WHERE id_empresa = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
                ps.setInt(1, Integer.parseInt(id));
                ps.executeUpdate();
                System.out.println("Empresa con ID " + id + " borrada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("ServletGestionEmpresas");
    }


    private void listarEmpresas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Empresa> empresas = new ArrayList<>();

        // Obtener datos de la base de datos
        try (Connection con = ConectarseBD.conectarse(null);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_empresa, nombre, descripcion, nombre_completo, email_tutor_laboral FROM Empresa")) {

            while (rs.next()) {
                Empresa empresa = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("nombre_completo"),
                        rs.getString("email_tutor_laboral")
                );
                empresas.add(empresa);
            }

            // Pasar la lista de empresas al JSP
            request.setAttribute("empresas", empresas);
            request.getRequestDispatcher("/gestion_empresas.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void editarEmpresa(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        Empresa empresa = null;

        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Empresa WHERE id_empresa = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                empresa = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("nombre_completo"),
                        rs.getString("email_tutor_laboral")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Pasar la empresa al JSP para rellenar los campos del formulario
        request.setAttribute("empresa", empresa);
        request.getRequestDispatcher("/crear_editar_empresa.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String nombre_completo = request.getParameter("nombre_completo");
        String email_tutor_laboral = request.getParameter("email_tutor_laboral");
        String id = request.getParameter("id");

        try (Connection con = ConectarseBD.conectarse(null)) {
            if (id != null && !id.isEmpty()) {
                String sqlUpdate = "UPDATE Empresa SET nombre = ?, descripcion = ?, nombre_completo = ?, email_tutor_laboral = ? WHERE id_empresa = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                    ps.setString(1, nombre);
                    ps.setString(2, descripcion);
                    ps.setString(3, nombre_completo);
                    ps.setString(4, email_tutor_laboral);
                    ps.setInt(5, Integer.parseInt(id));
                    ps.executeUpdate();
                }
            } else {
                String sqlInsert = "INSERT INTO Empresa (nombre, descripcion, nombre_completo, email_tutor_laboral) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                    ps.setString(1, nombre);
                    ps.setString(2, descripcion);
                    ps.setString(3, nombre_completo);
                    ps.setString(4, email_tutor_laboral);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("ServletGestionEmpresas");
    }

    @Override
    public String getServletInfo() {
        return "Servlet de gestión de empresas";
    }
}
