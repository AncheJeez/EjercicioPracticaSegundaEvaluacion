<%-- 
    Document   : estadisticas
    Created on : 18 feb 2026, 1:50:46
    Author     : AndJe
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
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
    <div class="p-3">
        <h2><fmt:message key="alumnosPorEmpresa"/></h2>
        <canvas id="chartEmpresa" width="400" height="200"></canvas>

        <h2><fmt:message key="alumnosPorCCurso"/></h2>
        <canvas id="chartCurso" width="400" height="200"></canvas>

            <script>
                const empresas = [<c:forEach var="e" items="${alumnosPorEmpresa}">'${e.nombre}',</c:forEach>];
                const numEmpresa = [<c:forEach var="e" items="${alumnosPorEmpresa}">${e.cantidad},</c:forEach>];

                const cursos = [<c:forEach var="c" items="${alumnosPorCurso}">'${c.nombre}',</c:forEach>];
                const numCurso = [<c:forEach var="c" items="${alumnosPorCurso}">${c.cantidad},</c:forEach>];

                new Chart(document.getElementById('chartEmpresa'), {
                    type: 'bar',
                    data: {
                        labels: empresas,
                        datasets: [{
                            label: 'Número de alumnos',
                            data: numEmpresa,
                            backgroundColor: 'rgba(54, 162, 235, 0.6)'
                        }]
                    }
                });

                new Chart(document.getElementById('chartCurso'), {
                    type: 'bar',
                    data: {
                        labels: cursos,
                        datasets: [{
                            label: 'Número de alumnos',
                            data: numCurso,
                            backgroundColor: 'rgba(255, 99, 132, 0.6)'
                        }]
                    }
                });
            </script>
    </div>
</body>
</html>
