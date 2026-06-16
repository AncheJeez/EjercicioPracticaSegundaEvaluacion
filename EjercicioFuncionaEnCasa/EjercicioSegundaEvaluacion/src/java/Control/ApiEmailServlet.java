package Control;

import Modelo.EmailSender;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ApiEmailServlet", urlPatterns = {"/api/email"})
public class ApiEmailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Accept either application/json or form-encoded posts. For simplicity, parse parameters.
        request.setCharacterEncoding("UTF-8");
        String smtpProtocol = request.getParameter("smtp_protocol");
        String smtpHost = request.getParameter("smtp_host");
        String smtpPort = request.getParameter("smtp_port");
        String smtpUser = request.getParameter("smtp_user");
        String smtpPass = request.getParameter("smtp_pass");
        String startTlsParam = request.getParameter("smtp_starttls");
        boolean startTls = "true".equalsIgnoreCase(startTlsParam) || "on".equalsIgnoreCase(startTlsParam);

        String to = request.getParameter("to"); // comma/; separated
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");

        // Basic validation
        if (smtpHost == null || smtpHost.isEmpty() || smtpUser == null || smtpUser.isEmpty() || smtpPass == null || smtpPass.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Missing SMTP configuration (smtp_host, smtp_user, smtp_pass)\"}");
            return;
        }

        Set<String> recipients = new LinkedHashSet<>();
        if (to != null) {
            String[] parts = to.split("[,;\\s]+");
            for (String p : parts) {
                String t = p.trim();
                if (!t.isEmpty()) recipients.add(t);
            }
        }

        if (recipients.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"No recipients specified\"}");
            return;
        }

        String proto = smtpProtocol != null && !smtpProtocol.isEmpty() ? smtpProtocol : "smtps";
        String port = smtpPort != null && !smtpPort.isEmpty() ? smtpPort : ("smtps".equalsIgnoreCase(proto) ? "465" : "587");

        List<String> failed = new ArrayList<>();
        int sent = 0;
        for (String dest : recipients) {
            try {
                EmailSender.send(proto, smtpHost, port, startTls, smtpUser, smtpPass, dest, subject, body);
                sent++;
            } catch (Exception ex) {
                failed.add(dest + " -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        StringBuilder json = new StringBuilder();
        json.append('{');
        json.append("\"sent\":").append(sent).append(',');
        json.append("\"failed\":").append('[');
        for (int i = 0; i < failed.size(); i++) {
            json.append('"').append(escapeJson(failed.get(i))).append('"');
            if (i + 1 < failed.size()) json.append(',');
        }
        json.append(']');
        json.append('}');
        response.getWriter().write(json.toString());
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
