package Control;

import Modelo.Alumno;
import Modelo.AlumnoDAO;
import Modelo.CursoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ApiAlumnosServlet", urlPatterns = {"/api/alumnos", "/api/alumnos/*"})
public class ApiAlumnosServlet extends HttpServlet {

    private String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
        }
        return sb.toString().trim();
    }

    private String extractJsonString(String json, String key) {
        if (json == null || json.isEmpty()) return null;
        // Corregido: \\s* y el escape de la comilla dentro de los corchetes [^\"]
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) return m.group(1);

        // Corregido: \\s* para buscar el valor null
        p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(null)", Pattern.CASE_INSENSITIVE);
        m = p.matcher(json);
        if (m.find()) return null;
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo(); // may be null or "/{id}"
        try (PrintWriter out = resp.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/") ) {
                // list all, optional ?curso
                String cursoFilter = req.getParameter("curso");
                List<Alumno> list = AlumnoDAO.listByCurso(cursoFilter);
                resp.setContentType("application/json;charset=UTF-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("[");
                for (int i = 0; i < list.size(); i++) {
                    Alumno a = list.get(i);
                    out.print(alumnoToJson(a));
                    if (i + 1 < list.size()) out.print(",");
                }
                out.print("]");
            } else {
                // get by id
                String idStr = pathInfo.substring(1);
                int id = Integer.parseInt(idStr);
                Alumno a = AlumnoDAO.findById(id);
                if (a == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.setContentType("application/json;charset=UTF-8");
                    out.print("{\"error\":\"Alumno no encontrado\"}");
                    return;
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json;charset=UTF-8");
                out.print(alumnoToJson(a));
            }
        } catch (NumberFormatException nfe) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print("{\"error\":\"ID inválido\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // create
        resp.setCharacterEncoding("UTF-8");
        try {
            String nombre = req.getParameter("nombre");
            String apellidos = req.getParameter("apellidos");
            String email = req.getParameter("email");
            String curso = req.getParameter("curso_matriculado");
            String fecha_nac = req.getParameter("fecha_nac");
            String grupo = req.getParameter("grupo");

            String body = null;
            String contentType = req.getContentType();
            if ((nombre == null || apellidos == null || email == null) && contentType != null && contentType.toLowerCase().contains("application/json")) {
                body = readBody(req);
                if (nombre == null) nombre = extractJsonString(body, "nombre");
                if (apellidos == null) apellidos = extractJsonString(body, "apellidos");
                if (email == null) email = extractJsonString(body, "email");
                if (curso == null) curso = extractJsonString(body, "curso_matriculado");
                if (fecha_nac == null) fecha_nac = extractJsonString(body, "fecha_nac");
                if (grupo == null) grupo = extractJsonString(body, "grupo");
            }

            // basic validation
            if (nombre == null || nombre.isEmpty() || apellidos == null || apellidos.isEmpty() || email == null || email.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().print("{\"error\":\"Campos obligatorios: nombre, apellidos, email\"}");
                return;
            }

            // ensure curso exists (create if missing)
            if (curso != null) curso = curso.trim();
            if (curso != null && !curso.isEmpty()) {
                if (CursoDAO.findByName(curso) == null) {
                    CursoDAO.insert(curso);
                }
            } else {
                curso = null;
            }

            java.util.Date d = null;
            if (fecha_nac != null && !fecha_nac.isEmpty()) {
                try { d = java.sql.Date.valueOf(fecha_nac); } catch (Exception ex) { /* ignore parse */ }
            }

            Alumno a = new Alumno(0, nombre, apellidos, email, curso, d, grupo);
            int newId = AlumnoDAO.insertReturnId(a);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json;charset=UTF-8");
            resp.setHeader("Location", req.getContextPath() + "/api/alumnos/" + (newId > 0 ? newId : ""));
            resp.getWriter().print("{\"id\":" + newId + ",\"message\":\"Alumno creado\"}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // update /api/alumnos/{id}
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\":\"Falta ID en URL\"}");
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));

            // read params or json
            String nombre = req.getParameter("nombre");
            String apellidos = req.getParameter("apellidos");
            String email = req.getParameter("email");
            String curso = req.getParameter("curso_matriculado");
            String fecha_nac = req.getParameter("fecha_nac");
            String grupo = req.getParameter("grupo");

            String contentType = req.getContentType();
            if (contentType != null && contentType.toLowerCase().contains("application/json")) {
                String body = readBody(req);
                if (nombre == null) nombre = extractJsonString(body, "nombre");
                if (apellidos == null) apellidos = extractJsonString(body, "apellidos");
                if (email == null) email = extractJsonString(body, "email");
                if (curso == null) curso = extractJsonString(body, "curso_matriculado");
                if (fecha_nac == null) fecha_nac = extractJsonString(body, "fecha_nac");
                if (grupo == null) grupo = extractJsonString(body, "grupo");
            }

            Alumno existing = AlumnoDAO.findById(id);
            if (existing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().print("{\"error\":\"Alumno no encontrado\"}");
                return;
            }

            // update fields if provided, else keep existing
            if (nombre == null) nombre = existing.getNombre();
            if (apellidos == null) apellidos = existing.getApellidos();
            if (email == null) email = existing.getEmail();
            if (curso == null) curso = existing.getCursoMatriculado();
            if (grupo == null) grupo = existing.getGrupo();

            if (curso != null) curso = curso.trim();
            if (curso != null && !curso.isEmpty()) {
                if (CursoDAO.findByName(curso) == null) {
                    CursoDAO.insert(curso);
                }
            } else {
                curso = null;
            }

            java.util.Date d = existing.getFechaNac();
            if (fecha_nac != null && !fecha_nac.isEmpty()) {
                try { d = java.sql.Date.valueOf(fecha_nac); } catch (Exception ex) { /* ignore parse */ }
            }

            Alumno updated = new Alumno(id, nombre, apellidos, email, curso, d, grupo);
            AlumnoDAO.update(updated);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print("{\"message\":\"Alumno actualizado\"}");

        } catch (NumberFormatException nfe) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print("{\"error\":\"ID inválido\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\":\"Falta ID en URL\"}");
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            AlumnoDAO.delete(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException nfe) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\":\"ID inválido\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private String alumnoToJson(Alumno a) {
        if (a == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"id\":").append(a.getIdAlumno()).append(',');
        sb.append("\"nombre\":\"").append(escapeJson(a.getNombre())).append("\",");
        sb.append("\"apellidos\":\"").append(escapeJson(a.getApellidos())).append("\",");
        sb.append("\"email\":\"").append(escapeJson(a.getEmail())).append("\",");
        sb.append("\"curso_matriculado\":");
        if (a.getCursoMatriculado() == null) sb.append("null"); else sb.append('\"').append(escapeJson(a.getCursoMatriculado())).append('\"');
        sb.append(',');
        sb.append("\"fecha_nac\":");
        if (a.getFechaNac() == null) sb.append("null"); else sb.append('\"').append(a.getFechaNac().toString()).append('\"');
        sb.append(',');
        sb.append("\"grupo\":");
        if (a.getGrupo() == null) sb.append("null"); else sb.append('\"').append(escapeJson(a.getGrupo())).append('\"');
        sb.append('}');
        return sb.toString();
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
