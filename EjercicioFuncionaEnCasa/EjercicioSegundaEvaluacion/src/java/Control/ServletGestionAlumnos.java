/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

import Modelo.Alumno;
import Modelo.AlumnoDAO;
import Modelo.Practica;
import Modelo.PracticaDAO;
import Modelo.Empresa;
import Modelo.EmpresaDAO;
import Modelo.CursoDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AndJe
 */
@WebServlet(name = "ServletGestionAlumnos", urlPatterns = {"/ServletGestionAlumnos"})
@MultipartConfig
public class ServletGestionAlumnos extends HttpServlet {

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
            out.println("<title>Servlet ServletGestionAlumnos</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletGestionAlumnos at " + request.getContextPath() + "</h1>");
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
            borrarAlumno(request, response, id);
        } else if ("new".equals(action)) {
            prepararNuevoAlumno(request, response);
        } else if (id != null && !id.isEmpty()) {
            editarAlumno(request, response, id);
        } else {
            listarAlumnos(request, response);
        }
    }
    
    private void borrarAlumno(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("error", "ID de alumno ausente");
            listarAlumnos(request, response);
            return;
        }

        try {
            Modelo.AlumnoDAO.delete(Integer.parseInt(id));
            // Prefer redirect after successful delete
            response.sendRedirect("ServletGestionAlumnos");
            return;
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("error", "ID de alumno inválido: " + id);
            listarAlumnos(request, response);
            return;
        } catch (Exception e) {
            // capture stacktrace and show the view with error details
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al borrar alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            listarAlumnos(request, response);
            return;
        }
    }
    
    private void listarAlumnos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cursoFilter = request.getParameter("curso");
            List<Alumno> alumnos = Modelo.AlumnoDAO.listByCurso(cursoFilter);
            request.setAttribute("alumnos", alumnos);
            // also provide courses list for the filter UI
            List<String> cursos = Modelo.AlumnoDAO.listCursos();
            request.setAttribute("cursos", cursos);
            request.setAttribute("cursoSeleccionado", cursoFilter);
            // successful list
            response.setStatus(HttpServletResponse.SC_OK);
            request.getRequestDispatcher("/gestion_alumnos.jsp").forward(request, response);
        } catch (Exception e) {
            // capture stacktrace and show the gestion view with details
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al listar alumnos: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.getRequestDispatcher("/gestion_alumnos.jsp").forward(request, response);
        }
    }

    private void prepararNuevoAlumno(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // provide empty alumno and cursos list
            request.setAttribute("alumno", null);
            try {
                List<Modelo.Curso> cursos = Modelo.CursoDAO.listAll();
                request.setAttribute("cursos", cursos);
            } catch (Exception ex) {
                // ignore
            }
            response.setStatus(HttpServletResponse.SC_OK);
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error preparando creación de alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.getRequestDispatcher("/gestion_alumnos.jsp").forward(request, response);
        }
    }
    
    private void editarAlumno(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("error", "ID de alumno ausente para editar");
            listarAlumnos(request, response);
            return;
        }

        try {
            Alumno alumno = Modelo.AlumnoDAO.findById(Integer.parseInt(id));
            if (alumno == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("error", "Alumno no encontrado");
                listarAlumnos(request, response);
                return;
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateada = alumno.getFechaNac() != null ? sdf.format(alumno.getFechaNac()) : "";
            request.setAttribute("alumno", alumno);
            request.setAttribute("fechaFormateada", fechaFormateada);
            // provide cursos list for select
            try {
                List<Modelo.Curso> cursos = Modelo.CursoDAO.listAll();
                request.setAttribute("cursos", cursos);
            } catch (Exception ex) {
                // ignore — view will handle empty cursos
            }
            response.setStatus(HttpServletResponse.SC_OK);
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("error", "ID de alumno inválido: " + id);
            listarAlumnos(request, response);
        } catch (Exception e) {
            // capture stacktrace and forward to edit view with details
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al obtener/editar alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Handle CSV upload if present
        String action = request.getParameter("action");
        Part filePart = null;
        try {
            filePart = request.getPart("csvfile");
        } catch (IllegalStateException | IOException | ServletException ex) {
            // not a multipart request or no part
            filePart = null;
        }

        if ((action != null && action.equals("upload")) || filePart != null) {
            // process CSV upload
            if (filePart == null || filePart.getSize() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("error", "No se ha seleccionado ningún fichero CSV.");
                listarAlumnos(request, response);
                return;
            }

            int inserted = 0;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8))) {
                String first = reader.readLine();
                if (first == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    request.setAttribute("error", "CSV vacío");
                    listarAlumnos(request, response);
                    return;
                }

                if (first.trim().equalsIgnoreCase("#ALUMNOS")) {
                    // New two-section format
                    // Delete practicas first (FK), then alumnos
                    Modelo.PracticaDAO.deleteAll();
                    Modelo.AlumnoDAO.deleteAll();

                    // read alumnos header
                    String alumnosHeader = reader.readLine();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().equalsIgnoreCase("#PRACTICAS")) break;
                        if (line.trim().isEmpty()) continue;
                        List<String> cols = parseCsvLine(line);
                        // alumno columns: id_alumno,Nombre,Apellidos,Email,FechaNacimiento,Curso,Grupo
                        String nombre = cols.size() > 1 ? cols.get(1) : "";
                        String apellidos = cols.size() > 2 ? cols.get(2) : "";
                        String email = cols.size() > 3 ? cols.get(3) : "";
                        String fechaStr = cols.size() > 4 ? cols.get(4) : null;
                        String curso = cols.size() > 5 ? cols.get(5) : null;
                        String grupo = cols.size() > 6 ? cols.get(6) : null;

                        java.sql.Date fecha = null;
                        if (fechaStr != null && !fechaStr.isEmpty() && !fechaStr.equalsIgnoreCase("null")) {
                            try { fecha = java.sql.Date.valueOf(fechaStr); } catch (Exception ex) {}
                        }

                        try {
                            Alumno a = new Alumno(0, nombre, apellidos, email, curso, fecha, grupo);
                            Modelo.AlumnoDAO.insert(a);
                            inserted++;
                        } catch (Exception ex) {
                            // skip bad rows but continue
                        }
                    }

                    // now read practicas header
                    String practicasHeader = reader.readLine(); // header line after #PRACTICAS
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        List<String> cols = parseCsvLine(line);
                        // columns: id_practica,alumno_email,empresa_id,empresa_nombre,fecha_comienzo,fecha_finalizacion,comentarios
                        String alumnoEmail = cols.size() > 1 ? cols.get(1) : null;
                        String empresaIdStr = cols.size() > 2 ? cols.get(2) : null;
                        String empresaNombre = cols.size() > 3 ? cols.get(3) : null;
                        String fechaComStr = cols.size() > 4 ? cols.get(4) : null;
                        String fechaFinStr = cols.size() > 5 ? cols.get(5) : null;
                        String comentarios = cols.size() > 6 ? cols.get(6) : null;

                        try {
                            Integer alumnoId = alumnoEmail != null ? Modelo.AlumnoDAO.findIdByEmail(alumnoEmail) : null;
                            if (alumnoId == null) continue; // cannot attach practice

                            // ensure empresa exists
                            if (empresaNombre != null && !empresaNombre.trim().isEmpty()) {
                                Modelo.EmpresaDAO.insertIfNotExistsByName(empresaNombre);
                            }
                            Modelo.Empresa empresa = null;
                            if (empresaNombre != null && !empresaNombre.trim().isEmpty()) {
                                empresa = Modelo.EmpresaDAO.findByName(empresaNombre);
                            }
                            if (empresa == null) continue; // skip practice if no empresa

                            java.sql.Date fechaCom = null;
                            java.sql.Date fechaFin = null;
                            if (fechaComStr != null && !fechaComStr.isEmpty() && !fechaComStr.equalsIgnoreCase("null")) {
                                try { fechaCom = java.sql.Date.valueOf(fechaComStr); } catch (Exception ex) {}
                            }
                            if (fechaFinStr != null && !fechaFinStr.isEmpty() && !fechaFinStr.equalsIgnoreCase("null")) {
                                try { fechaFin = java.sql.Date.valueOf(fechaFinStr); } catch (Exception ex) {}
                            }

                            Alumno aRef = new Alumno();
                            aRef.setIdAlumno(alumnoId);
                            Practica p = new Practica(0, aRef, empresa, fechaCom, fechaFin, comentarios);
                            Modelo.PracticaDAO.insert(p);
                        } catch (Exception ex) {
                            // skip bad practice rows
                        }
                    }

                    request.setAttribute("message", "CSV importado. Alumnos insertados: " + inserted);
                    response.setStatus(HttpServletResponse.SC_OK);
                    listarAlumnos(request, response);
                    return;

                } else {
                    // Fallback: old single-row-per-practice CSV (handle by inserting unique emails)
                    Modelo.PracticaDAO.deleteAll();
                    Modelo.AlumnoDAO.deleteAll();
                    java.util.Set<String> seen = new java.util.HashSet<>();
                    String line = first; // first line was header previously; treat it as header so skip
                    // skip header already read, now continue
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        List<String> cols = parseCsvLine(line);
                        String nombre = cols.size() > 1 ? cols.get(1) : "";
                        String apellidos = cols.size() > 2 ? cols.get(2) : "";
                        String email = cols.size() > 3 ? cols.get(3) : "";
                        String fechaStr = cols.size() > 4 ? cols.get(4) : null;
                        String curso = cols.size() > 5 ? cols.get(5) : null;
                        String grupo = null;

                        if (email == null || email.trim().isEmpty()) continue;
                        if (seen.contains(email)) continue;

                        java.sql.Date fecha = null;
                        if (fechaStr != null && !fechaStr.isEmpty() && !fechaStr.equalsIgnoreCase("null")) {
                            try { fecha = java.sql.Date.valueOf(fechaStr); } catch (Exception ex) {}
                        }

                        try {
                            Alumno a = new Alumno(0, nombre, apellidos, email, curso, fecha, grupo);
                            Modelo.AlumnoDAO.insert(a);
                            seen.add(email);
                            inserted++;
                        } catch (Exception ex) {}
                    }

                    request.setAttribute("message", "CSV importado (formato legacy). Alumnos insertados: " + inserted);
                    response.setStatus(HttpServletResponse.SC_OK);
                    listarAlumnos(request, response);
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error al procesar CSV: " + e.getMessage());
                java.io.StringWriter sw = new java.io.StringWriter(); e.printStackTrace(new java.io.PrintWriter(sw)); request.setAttribute("exceptionStack", sw.toString());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                listarAlumnos(request, response);
                return;
            }
        }

        // existing single-alumno form handling
        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String curso = request.getParameter("curso_matriculado");
        String fechaStr = request.getParameter("fecha_nac");
        String grupo = request.getParameter("grupo");

        try {
            java.sql.Date fecha = fechaStr != null && !fechaStr.isEmpty() ? java.sql.Date.valueOf(fechaStr) : null;

            // Ensure curso exists to satisfy FK constraint. If not, create it.
            if (curso != null) curso = curso.trim();
            if (curso != null && !curso.isEmpty()) {
                try {
                    Modelo.CursoDAO.findByName(curso);
                } catch (Exception ignored) {
                    // findByName throws on DB issues; fallback handled below
                }
                // If curso not present, insert it
                try {
                    if (Modelo.CursoDAO.findByName(curso) == null) {
                        Modelo.CursoDAO.insert(curso);
                    }
                } catch (Exception eCurso) {
                    // If cannot create curso, propagate as validation error
                    throw new Exception("No se pudo asegurar el curso '" + curso + "': " + eCurso.getMessage(), eCurso);
                }
            } else {
                // If curso empty, set to NULL to avoid FK violation if schema allows null
                curso = null;
            }

            if (id != null && !id.isEmpty()) {
                Alumno alumno = new Alumno(Integer.parseInt(id), nombre, apellidos, email, curso, fecha, grupo);
                Modelo.AlumnoDAO.update(alumno);
            } else {
                Alumno alumno = new Alumno(0, nombre, apellidos, email, curso, fecha, grupo);
                Modelo.AlumnoDAO.insert(alumno);
            }

        } catch (Exception e) {
            // capture stacktrace and forward back to create/edit view with submitted data
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            request.setAttribute("error", "Error al guardar alumno: " + e.getMessage());
            request.setAttribute("exceptionStack", sw.toString());
            // repopular campos en el formulario
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellidos", apellidos);
            request.setAttribute("email", email);
            request.setAttribute("curso_matriculado", curso);
            request.setAttribute("fecha_nac", fechaStr);
            request.setAttribute("grupo", grupo);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.getRequestDispatcher("/crear_editar_alumnos.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("ServletGestionAlumnos");
    }

    // Simple CSV parser that respects quoted fields
    private List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        if (line == null) return out;
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        out.add(cur.toString());
        return out;
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
