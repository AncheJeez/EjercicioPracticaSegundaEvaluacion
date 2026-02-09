<%-- 
    Document   : index.jsp
    Created on : 9 feb 2026, 9:55:27
    Author     : usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Página principal</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" href="#">
                <img src="./assets/Apache_Tomcat_logo.svg.png" width="40" height="40" class="d-inline-block align-top" alt="">
                Practica Segunda Evaluacion
                <img src="./assets/Java_programming_language_logo.svg.png" width="40" height="40" class="d-inline-block align-top" alt="">
            </a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/Logearse">Logearse</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/Registrarse">Registrarse</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/ServletMostrarTodosLosDatos">Mostrar Datos</a>
                    </li>
                </ul>
            </div>
        </nav>
        
        <%
        String nombre = (String)session.getAttribute("nombre");
        String apellidos = (String)session.getAttribute("apellidos");
        %>
        Welcome <%= nombre %> <%= apellidos %>
    </body>
</html>
