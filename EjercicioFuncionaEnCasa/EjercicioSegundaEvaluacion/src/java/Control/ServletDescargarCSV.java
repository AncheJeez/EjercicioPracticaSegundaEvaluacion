package Control;

import Modelo.PracticaDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletDescargarCSV", urlPatterns = {"/ServletDescargarCSV"})
public class ServletDescargarCSV extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=alumnos_practicas.csv");

        try (PrintWriter writer = response.getWriter()) {
            // Delegate to PracticaDAO to write a two-section CSV (ALUMNOS then PRACTICAS)
            Modelo.PracticaDAO.writeCsv(writer);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error al generar el CSV: " + e.getMessage());
        }
    }

    // SACADO DE INTERNET
    // Método simple para escapear comas y comillas en CSV
    private String escapeCsv(String valor) {
        if (valor == null) return "";
        String v = valor.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v + "\"";
        } else {
            return v;
        }
    }
}
