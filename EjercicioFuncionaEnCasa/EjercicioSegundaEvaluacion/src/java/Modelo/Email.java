package Modelo;

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

    // Método para enviar el correo usando EmailSender (no depende de JavaMail)
    public void enviarEsteEmail() throws Exception {
        // use SMTP with STARTTLS on smtp.gmail.com:587
        EmailSender.send("smtp", "smtp.gmail.com", "587", true, from, password, to, subject, text);
    }
}
