package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;

public class EmailSender {

    // Minimal SMTP client that does not require JavaMail. Supports SMTPS (implicit SSL) and SMTP with STARTTLS.
    public static void send(String protocol, String smtpHost, String smtpPort, boolean startTls, String user, String password, String to, String subject, String text) throws Exception {
        int port = smtpPort != null && !smtpPort.isEmpty() ? Integer.parseInt(smtpPort) : ("smtps".equalsIgnoreCase(protocol) ? 465 : 587);
        boolean useSsl = "smtps".equalsIgnoreCase(protocol);

        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            if (useSsl) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslSocket = (SSLSocket) factory.createSocket(smtpHost, port);
                socket = sslSocket;
            } else {
                socket = new Socket(smtpHost, port);
            }
            socket.setSoTimeout(20000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            // Read server banner
            String line = readResponse(reader);
            if (!line.startsWith("220")) throw new Exception("SMTP banner error: " + line);

            String localhost = "localhost";
            sendLine(writer, "EHLO " + localhost);
            readMultilineResponse(reader);

            if (!useSsl && startTls) {
                sendLine(writer, "STARTTLS");
                String resp = readResponse(reader);
                if (!resp.startsWith("220")) throw new Exception("STARTTLS not supported: " + resp);
                // upgrade to SSL
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslSocket = (SSLSocket) factory.createSocket(socket, smtpHost, port, true);
                socket = sslSocket;
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

                // EHLO again
                sendLine(writer, "EHLO " + localhost);
                readMultilineResponse(reader);
            }

            // AUTH LOGIN
            if (user != null && !user.isEmpty()) {
                sendLine(writer, "AUTH LOGIN");
                String resp = readResponse(reader);
                if (!resp.startsWith("334")) throw new Exception("AUTH LOGIN not accepted: " + resp);
                sendLine(writer, Base64.getEncoder().encodeToString(user.getBytes(StandardCharsets.UTF_8)));
                resp = readResponse(reader);
                if (!resp.startsWith("334")) throw new Exception("AUTH LOGIN username not accepted: " + resp);
                sendLine(writer, Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
                resp = readResponse(reader);
                if (!resp.startsWith("235")) throw new Exception("AUTH LOGIN failed: " + resp);
            }

            // MAIL FROM
            sendLine(writer, "MAIL FROM:<" + user + ">");
            String resp = readResponse(reader);
            if (!resp.startsWith("250")) throw new Exception("MAIL FROM failed: " + resp);

            // RCPT TO
            sendLine(writer, "RCPT TO:<" + to + ">");
            resp = readResponse(reader);
            if (!resp.startsWith("250") && !resp.startsWith("251")) throw new Exception("RCPT TO failed: " + resp);

            // DATA
            sendLine(writer, "DATA");
            resp = readResponse(reader);
            if (!resp.startsWith("354")) throw new Exception("DATA not accepted: " + resp);

            // Headers
            sendLine(writer, "From: " + user);
            sendLine(writer, "To: " + to);
            sendLine(writer, "Subject: " + (subject == null ? "" : subject));
            sendLine(writer, "Content-Type: text/plain; charset= UTF-8");
            sendLine(writer, "");
            // Body
            if (text != null && !text.isEmpty()) {
                for (String bodyLine : text.split("\r?\n")) {
                    // dot-stuffing
                    if (bodyLine.startsWith(".")) bodyLine = "." + bodyLine;
                    sendLine(writer, bodyLine);
                }
            }
            // End of data
            sendLine(writer, ".");
            resp = readResponse(reader);
            if (!resp.startsWith("250")) throw new Exception("Message not accepted: " + resp);

            // QUIT
            sendLine(writer, "QUIT");
            readResponse(reader);

        } finally {
            try { if (writer != null) writer.close(); } catch (Exception ex) {}
            try { if (reader != null) reader.close(); } catch (Exception ex) {}
            try { if (socket != null) socket.close(); } catch (Exception ex) {}
        }
    }

    // backward compatible - default to SMTPS (implicit SSL)
    public static void send(String smtpHost, String smtpPort, boolean startTls, String user, String password, String to, String subject, String text) throws Exception {
        send("smtps", smtpHost, smtpPort, startTls, user, password, to, subject, text);
    }

    private static void sendLine(BufferedWriter writer, String s) throws Exception {
        writer.write(s + "\r\n");
        writer.flush();
    }

    private static String readResponse(BufferedReader reader) throws Exception {
        String line = reader.readLine();
        if (line == null) throw new Exception("No response from server");
        return line;
    }

    private static void readMultilineResponse(BufferedReader reader) throws Exception {
        String line;
        do {
            line = reader.readLine();
            if (line == null) throw new Exception("No response from server");
        } while (line.length() >= 4 && line.charAt(3) == '-');
    }
}
