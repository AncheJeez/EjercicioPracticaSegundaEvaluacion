<%-- 
    Document   : index.jsp
    Created on : 9 feb 2026, 9:55:27
    Author     : usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${not empty param.lang}">
    <c:set var="lang" value="${param.lang}" scope="session"/>
</c:if>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>
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
                        <fmt:message key="tituloNav"/>
                    <img src="./assets/Java_programming_language_logo.svg.png" width="40" height="40" class="d-inline-block align-top" alt="">
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" 
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="/EjercicioSegundaEvaluacion">
                                <fmt:message key="principalNav"/>
                            </a>
                        </li>
                    </ul>
                    <div class="language-selector">
                        <a href="?lang=es" class="btn btn-sm btn-secondary">ES</a>
                        <a href="?lang=en" class="btn btn-sm btn-secondary">EN</a>
                    </div>
                    <ul class="navbar-nav ms-auto">
                        <c:choose>
                            <c:when test="${not empty sessionScope.nombre}">
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioSegundaEvaluacion/CerrarSesion">
                                        <fmt:message key="closeNav"/>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioSegundaEvaluacion/Logearse">
                                        <fmt:message key="loginNav"/>
                                    </a>
                                </li>
                                <li class="nav-item active">
                                    <a class="nav-link" href="/EjercicioSegundaEvaluacion/Registrarse">
                                        <fmt:message key="registerNav"/>
                                    </a>
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
                        <fmt:message key="bienvenida"/> ${sessionScope.nombre} ${sessionScope.apellidos}
                    </h2>
                    <div class="d-flex justify-content-center flex-wrap mb-4 gap-3">
                        <a href="/EjercicioSegundaEvaluacion/ServletGestionEmpresas" class="btn btn-primary btn-lg">
                            <fmt:message key="gestionEmpresa"/>
                        </a>
                        <a href="/EjercicioSegundaEvaluacion/ServletGestionAlumnos" class="btn btn-primary btn-lg">
                            <fmt:message key="gestionAlumno"/>
                        </a>
                        <a href="/EjercicioSegundaEvaluacion/ServletGestionPracticas" class="btn btn-primary btn-lg">
                            <fmt:message key="gestionPractica"/>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <h2 class="fw-bold mb-4">
                        <fmt:message key="pleaseLogIn"/>
                    </h2>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${sessionScope.directiva}">
                    <p class="fw-bold mb-2"><fmt:message key="udDirectiva"/></p>
                    <div class="d-flex justify-content-center flex-wrap mb-4 gap-3">
                        <a href="/EjercicioSegundaEvaluacion/ServletGestionProfesores" class="btn btn-info btn-lg">
                            <fmt:message key="gestionProfesores"/>
                        </a>
                        <a href="/EjercicioSegundaEvaluacion/ServletGestionCurso" type="button" class="btn btn-info btn-lg">
                            <fmt:message key="gestionarCursos"/>
                        </a>
                        <a href="/EjercicioSegundaEvaluacion/ServletEstadisticas" type="button" class="btn btn-info btn-lg">
                            <fmt:message key="estadisticas"/>
                        </a>
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
