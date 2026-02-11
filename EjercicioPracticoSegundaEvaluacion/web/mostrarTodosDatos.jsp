<%-- 
    Document   : mostrarTodosDatos
    Created on : 6 feb 2026, 13:51:35
    Author     : usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            header .nav-link {
                transition: all 0.2s ease;
                padding: 0.5rem 0;
                position: relative;
            }

            header .nav-link:hover {
                color: blue !important; 
                transform: translateY(-1px);
            }

            header .nav-link::after {
                content: '';
                position: absolute;
                width: 0;
                height: 2px;
                bottom: -4px;
                left: 0;
                background-color: blue;
                transition: width 0.3s ease;
            }

            header .nav-link:hover::after {
                width: 100%;
            }

            header .btn-outline-light:hover {
                background-color: rgba(255,0,255,0.15);
            }
        </style>
    </head>
    <body>
        <header class="container-fluid bg-primary text-white shadow-sm py-3">
            <nav class="navbar navbar-expand-lg navbar-light bg-light mx-3 px-3 rounded">
                <a class="navbar-brand" href="#">
                    <img src="./assets/Apache_Tomcat_logo.svg.png" width="40" height="40" class="d-inline-block align-top" alt="">
                    Practica Segunda Evaluacion
                    <img src="./assets/Java_programming_language_logo.svg.png" width="40" height="40" class="d-inline-block align-top" alt="">
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" 
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion">Principal</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/ServletMostrarTodosLosDatos">Mostrar Datos</a>
                        </li>
                    </ul>
                    <ul class="navbar-nav ms-auto">
                        <c:choose>
                            <c:when test="${not empty sessionScope.nombre}">
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/CerrarSesion">Cerrar Sesión</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/Logearse">Logearse</a>
                                </li>
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/Registrarse">Registrarse</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </nav>
        </header>
        <h1>Alumnos</h1>
        <ul>
            <c:forEach var="alumno" items="${alumnos}">
                <li>${alumno.idAlumno} - ${alumno.nombre} ${alumno.apellidos} ${alumno.email} ${alumno.curso_matriculado} ${alumno.fecha_nac}</li>
            </c:forEach>
        </ul>
        Profesores
        <ul>
            <c:forEach var="profesor" items="${profesores}">
                <li>${profesor.idProfesor} - ${profesor.nombre} ${profesor.apellidos} ${profesor.email} ${profesor.password} ${profesor.directiva}</li>
            </c:forEach> 
        </ul>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    </body>
</html>
