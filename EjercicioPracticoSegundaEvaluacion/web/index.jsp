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
        
        <div class="container text-center mt-5">

            <c:choose>
                <c:when test="${sessionScope.nombre != null && sessionScope.nombre != ''}">
                    <h2 class="fw-bold mb-4">
                        Bienvenid@ ${sessionScope.nombre} ${sessionScope.apellidos}
                    </h2>
                    <div class="d-flex justify-content-center flex-wrap mb-4 gap-3">
                        <button type="button" class="btn btn-primary btn-lg">Gestionar Empresas</button>
                        <button type="button" class="btn btn-primary btn-lg">Gestionar Alumnos</button>
                        <button type="button" class="btn btn-primary btn-lg">Gestionar Prácticas</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <h2 class="fw-bold mb-4">
                        Buenas! Inicie sesión para interactuar con la aplicación!
                    </h2>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${sessionScope.directiva}">
                    <p class="fw-bold mb-2">Es usted de la directiva</p>
                    <div class="d-flex justify-content-center flex-wrap mb-4 gap-3">
                        <button type="button" class="btn btn-info btn-lg">Gestionar profesores</button>
                        <button type="button" class="btn btn-info btn-lg">Gestionar cursos</button>
                        <button type="button" class="btn btn-info btn-lg">Mostrar estadísticas de prácticas</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${sessionScope.nombre != null && sessionScope.nombre != ''}">
                        <p class="fw-bold">Usted no es de la directiva</p>
                    </c:if>
                </c:otherwise>
            </c:choose>

        </div>

    </body>
</html>
