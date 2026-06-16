<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Enviar correo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"/>
    </head>
    <body class="p-3">
        <div class="container">
            <h3>Enviar correo</h3>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:if test="${not empty sentCount}">
                <div class="alert alert-success">Correos enviados: ${sentCount}</div>
            </c:if>

            <c:if test="${not empty failedList}">
                <div class="alert alert-warning">
                    <strong>Fallos:</strong>
                    <ul>
                        <c:forEach var="f" items="${failedList}">
                            <li>${f}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <form method="post" action="EnviarCorreo">
                <div class="row">
                    <div class="col-md-6">
                        <h5>Configuración SMTP</h5>
                        <div class="mb-2">
                            <label class="form-label">Host</label>
                            <input name="smtp_host" class="form-control" value="${env.SMTP_HOST}">
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Port</label>
                            <input name="smtp_port" class="form-control" value="${env.SMTP_PORT}">
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Usuario (email)</label>
                            <input name="smtp_user" class="form-control" value="${env.SMTP_USER}">
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Password / Key</label>
                            <input name="smtp_pass" type="password" class="form-control" value="${env.SMTP_PASS}">
                        </div>
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="checkbox" id="smtp_starttls" name="smtp_starttls" <c:if test="${env.SMTP_STARTTLS == 'true'}">checked</c:if>>
                            <label class="form-check-label" for="smtp_starttls">Enable STARTTLS</label>
                        </div>
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="checkbox" id="save_env" name="save_env">
                            <label class="form-check-label" for="save_env">Guardar configuración en .env</label>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <h5>Destinatarios y mensaje</h5>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="to_profesores" name="to_profesores">
                            <label class="form-check-label" for="to_profesores">Profesores (todos los registrados)</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="to_alumnos" name="to_alumnos">
                            <label class="form-check-label" for="to_alumnos">Alumnos (todos los registrados)</label>
                        </div>
                        <div class="mb-2 mt-2">
                                    <label class="form-label">Emails adicionales (separados por coma, punto y coma o nueva línea)</label>
                                    <textarea name="to_custom" class="form-control" rows="3"></textarea>
                        </div>

                        <div class="mb-2">
                            <label class="form-label">Asunto</label>
                            <input name="subject" class="form-control">
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Texto</label>
                            <textarea name="body" class="form-control" rows="6"></textarea>
                        </div>

                                <div class="mt-2">
                                    <a href="EnviarCorreo?action=edit_options" class="btn btn-link">Opciones SMTP</a>
                                </div>
                            </div>
                </div>

                <div class="mt-3">
                    <button class="btn btn-primary" type="submit">Enviar</button>
                    <a href="/EjercicioSegundaEvaluacion" class="btn btn-secondary">Volver</a>
                </div>
            </form>

        </div>
    </body>
</html>