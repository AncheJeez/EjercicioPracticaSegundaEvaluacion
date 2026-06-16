package Control;

import Modelo.EmailSender;
import Modelo.ProfesorDAO;
import Modelo.AlumnoDAO;
import Modelo.Profesor;
import Modelo.Alumno;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "EnviarCorreo", urlPatterns = {"/EnviarCorreo"})
public class EnviarCorreo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        Properties env = EnvConfig.readEnv(ctx);
        request.setAttribute("env", env);
        String action = request.getParameter("action");
        if ("edit_options".equals(action)) {
            request.getRequestDispatcher("/enviar_correo_opts.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/enviar_correo.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String smtpProtocol = request.getParameter("smtp_protocol"); // smtp or smtps
        String smtpHost = request.getParameter("smtp_host");
        String smtpPort = request.getParameter("smtp_port");
        String smtpUser = request.getParameter("smtp_user");
        String smtpPass = request.getParameter("smtp_pass");
        String useTls = request.getParameter("smtp_starttls");
        boolean startTls = "on".equalsIgnoreCase(useTls) || "true".equalsIgnoreCase(useTls);

        String action = request.getParameter("action");

        // Save env if requested or saving options
        String saveEnv = request.getParameter("save_env");
        ServletContext ctx = getServletContext();
        Properties env = new Properties();
        try {
            env = EnvConfig.readEnv(ctx);
        } catch (Exception ex) {
            // ignore
        }

        if (smtpProtocol != null) env.setProperty("SMTP_PROTOCOL", smtpProtocol);
        if (smtpHost != null) env.setProperty("SMTP_HOST", smtpHost);
        if (smtpPort != null) env.setProperty("SMTP_PORT", smtpPort);
        if (smtpUser != null) env.setProperty("SMTP_USER", smtpUser);
        if (smtpPass != null) env.setProperty("SMTP_PASS", smtpPass);
        env.setProperty("SMTP_STARTTLS", Boolean.toString(startTls));

        if ("save_options".equals(action) || (saveEnv != null && saveEnv.equals("on"))) {
            try {
                EnvConfig.writeEnv(ctx, env);
                request.setAttribute("env", env);
                request.setAttribute("message", "Opciones guardadas.");
                request.getRequestDispatcher("/enviar_correo.jsp").forward(request, response);
                return;
            } catch (Exception ex) {
                request.setAttribute("error", "No se pudo guardar .env: " + ex.getMessage());
                request.setAttribute("env", env);
                request.getRequestDispatcher("/enviar_correo_opts.jsp").forward(request, response);
                return;
            }
        }

        // Recipients selection
        String toProf = request.getParameter("to_profesores");
        String toAlum = request.getParameter("to_alumnos");
        String toCustom = request.getParameter("to_custom");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");

        // If action is 'edit_options' -> show options view
        if ("edit_options".equals(action)) {
            request.setAttribute("env", env);
            request.getRequestDispatcher("/enviar_correo_opts.jsp").forward(request, response);
            return;
        }

        Set<String> recipients = new LinkedHashSet<>();
        try {
            if (toProf != null && toProf.equals("on")) {
                List<Profesor> profs = ProfesorDAO.listAll();
                for (Profesor p : profs) if (p.getEmail() != null && !p.getEmail().isEmpty()) recipients.add(p.getEmail());
            }
        } catch (Exception ex) {
            request.setAttribute("error", "Error al obtener lista de profesores: " + ex.getMessage());
            request.setAttribute("env", env);
            request.getRequestDispatcher("/enviar_correo.jsp").forward(request, response);
            return;
        }

        try {
            if (toAlum != null && toAlum.equals("on")) {
                List<Alumno> al = AlumnoDAO.listAll();
                for (Alumno a : al) if (a.getEmail() != null && !a.getEmail().isEmpty()) recipients.add(a.getEmail());
            }
        } catch (Exception ex) {
            request.setAttribute("error", "Error al obtener lista de alumnos: " + ex.getMessage());
            request.setAttribute("env", env);
            request.getRequestDispatcher("/enviar_correo.jsp").forward(request, response);
            return;
        }

        if (toCustom != null && !toCustom.trim().isEmpty()) {
            String[] parts = toCustom.split("[,;\\s]+");
            for (String p : parts) {
                String t = p.trim();
                if (!t.isEmpty()) recipients.add(t);
            }
        }

        // Use env values if form left empty
        if ((smtpProtocol == null || smtpProtocol.isEmpty()) && env.getProperty("SMTP_PROTOCOL") != null) smtpProtocol = env.getProperty("SMTP_PROTOCOL");
        if ((smtpHost == null || smtpHost.isEmpty()) && env.getProperty("SMTP_HOST") != null) smtpHost = env.getProperty("SMTP_HOST");
        if ((smtpPort == null || smtpPort.isEmpty()) && env.getProperty("SMTP_PORT") != null) smtpPort = env.getProperty("SMTP_PORT");
        if ((smtpUser == null || smtpUser.isEmpty()) && env.getProperty("SMTP_USER") != null) smtpUser = env.getProperty("SMTP_USER");
        if ((smtpPass == null || smtpPass.isEmpty()) && env.getProperty("SMTP_PASS") != null) smtpPass = env.getProperty("SMTP_PASS");
        if (env.getProperty("SMTP_STARTTLS") != null) startTls = Boolean.parseBoolean(env.getProperty("SMTP_STARTTLS"));

        if (smtpHost == null || smtpHost.isEmpty() || smtpUser == null || smtpUser.isEmpty() || smtpPass == null) {
            request.setAttribute("error", "Debe proporcionar configuración SMTP (host, user, pass). Abra 'Opciones' para configurarlo.");
            request.setAttribute("env", env);
            request.getRequestDispatcher("/enviar_correo.jsp").forward(request, response);
            return;
        }

        List<String> failed = new ArrayList<>();
        int sent = 0;
        for (String dest : recipients) {
            try {
                String proto = smtpProtocol != null && !smtpProtocol.isEmpty() ? smtpProtocol : "smtps";
                String port = smtpPort != null && !smtpPort.isEmpty() ? smtpPort : ("smtps".equalsIgnoreCase(proto) ? "465" : "587");
                EmailSender.send(proto, smtpHost, port, startTls, smtpUser, smtpPass, dest, subject, body);
                sent++;
            } catch (Exception ex) {
                failed.add(dest + " -> " + ex.getMessage());
            }
        }

        request.setAttribute("env", env);
        request.setAttribute("sentCount", sent);
        request.setAttribute("failedList", failed);
        request.getRequestDispatcher("/enviar_correo.jsp").forward(request, response);
    }
}
