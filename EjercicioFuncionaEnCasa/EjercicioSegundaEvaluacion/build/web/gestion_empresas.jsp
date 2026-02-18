<%-- 
    Document   : gestion_empresas
    Created on : 17 feb 2026, 20:40:43
    Author     : AndJe
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
                            <a class="nav-link" href="/EjercicioSegundaEvaluacion">Principal</a>
                        </li>
                    </ul>
                    <ul class="navbar-nav ms-auto">
                        <c:choose>
                            <c:when test="${not empty sessionScope.nombre}">
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioSegundaEvaluacion/CerrarSesion">Cerrar Sesión</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioSegundaEvaluacion/Logearse">Logearse</a>
                                </li>
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioSegundaEvaluacion/Registrarse">Registrarse</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </nav>
        </header>
        <div class="container text-center mt-5">
            <h2>Listado de Empresas</h2>

            <c:if test="${empty empresas}">
                <div class="alert alert-warning" role="alert">
                    No hay empresas registradas en el sistema.
                </div>
            </c:if>

            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Nombre Completo</th>
                        <th>Email Tutor</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="empresa" items="${empresas}">
                        <tr>
                            <td>${empresa.nombre}</td>
                            <td>${empresa.descripcion}</td>
                            <td>${empresa.nombre_completo}</td>
                            <td>${empresa.email_tutor_laboral}</td>
                            <td>
                                <a href="ServletGestionEmpresas?id=${empresa.id_empresa}" class="btn btn-warning btn-sm">Editar</a>
                                <a href="ServletGestionEmpresas?action=borrar&id=${empresa.id_empresa}" 
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('¿Estás seguro de que deseas borrar esta empresa?');">
                                   Borrar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <a href="crear_editar_empresa.jsp" class="btn btn-primary btn-sm">Crear Empresa</a>
        </div>

    </body>
</html>
