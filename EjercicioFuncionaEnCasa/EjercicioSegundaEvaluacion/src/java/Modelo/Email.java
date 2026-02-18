package Modelo;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {

    private final String from;
    private final String password;
    private final String to;
    private final String subject;
    private final String text;

    // Constructor
    public Email(String from, String password, String to, String subject, String text) {
        this.from = from;
        this.password = password;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    // Método para enviar el correo
    public void enviarEsteEmail() throws MessagingException {

        // Configuración SMTP para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");        // servidor SMTP
        props.put("mail.smtp.port", "587");                   // puerto TLS
        props.put("mail.smtp.auth", "true");                  // requiere autenticación
        props.put("mail.smtp.starttls.enable", "true");       // habilitar TLS

        // Crear sesión con autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        session.setDebug(false); // true para ver debug del envío

        // Crear mensaje
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(text);

        // Enviar correo
        Transport.send(message);
    }
}
