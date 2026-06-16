<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Opciones SMTP</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"/>
    </head>
    <body class="p-3">
        <div class="container">
            <h3>Opciones SMTP</h3>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>

            <form method="post" action="EnviarCorreo">
                <input type="hidden" name="action" value="save_options" />
                <div class="mb-2">
                    <label class="form-label">Protocolo</label>
                    <select name="smtp_protocol" class="form-select">
                        <option value="smtp" <c:if test="${env.SMTP_PROTOCOL == 'smtp'}">selected</c:if>>SMTP (STARTTLS)</option>
                        <option value="smtps" <c:if test="${env.SMTP_PROTOCOL == 'smtps'}">selected</c:if>>SMTPS (SSL, puerto 465)</option>
                    </select>
                </div>
                <div class="mb-2">
                    <label class="form-label">Host</label>
                    <input name="smtp_host" class="form-control" value="${env.SMTP_HOST}">
                </div>
                <div class="mb-2">
                    <label class="form-label">Port (opcional)</label>
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
                    <label class="form-check-label" for="smtp_starttls">Enable STARTTLS (si usas SMTP)</label>
                </div>
                <div class="form-check mb-2">
                    <input class="form-check-input" type="checkbox" id="save_env" name="save_env">
                    <label class="form-check-label" for="save_env">Guardar configuración en .env</label>
                </div>

                <div class="mt-3">
                    <button class="btn btn-primary" type="submit">Guardar</button>
                    <a href="EnviarCorreo" class="btn btn-secondary">Volver</a>
                </div>
            </form>
        </div>
    </body>
</html>