/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import Modelo.Empresa;
import Modelo.EmpresaDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            if (id != null && !id.isEmpty()) {
                Modelo.EmpresaDAO.delete(Integer.parseInt(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, e);
            request.setAttribute("error", "Error al borrar empresa: " + e.getMessage());
        }

        response.sendRedirect("ServletGestionEmpresas");
    }


    private void listarEmpresas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Empresa> empresas = EmpresaDAO.listAll();
            request.setAttribute("empresas", empresas);
            request.getRequestDispatcher("/gestion_empresas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al listar empresas: " + e.getMessage());
            request.getRequestDispatcher("/gestion_empresas.jsp").forward(request, response);
        }
    }

    private void editarEmpresa(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        Empresa empresa = null;
        try {
            if (id != null && !id.isEmpty()) {
                empresa = EmpresaDAO.findById(Integer.parseInt(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, e);
            request.setAttribute("error", "Error al recuperar empresa: " + e.getMessage());
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

        try {
            Empresa e = new Empresa();
            e.setNombre(nombre);
            e.setDescripcion(descripcion);
            e.setNombre_completo(nombre_completo);
            e.setEmail_tutor_laboral(email_tutor_laboral);

            if (id != null && !id.isEmpty()) {
                e.setId_empresa(Integer.parseInt(id));
                EmpresaDAO.update(e);
            } else {
                EmpresaDAO.insert(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ServletGestionEmpresas.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "Error al guardar empresa: " + ex.getMessage());
            request.getRequestDispatcher("/crear_editar_empresa.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("ServletGestionEmpresas");
    }

    @Override
    public String getServletInfo() {
        return "Servlet de gestión de empresas";
    }
}
