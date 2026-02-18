<%-- 
    Document   : gestion_empresas
    Created on : 17 feb 2026, 20:40:43
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
            <h2><fmt:message key="listadoEmpresas"/></h2>

            <c:if test="${empty empresas}">
                <div class="alert alert-warning" role="alert">
                    <fmt:message key="noHayEmpresasRegistradas"/>
                </div>
            </c:if>

            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th><fmt:message key="gesEmpNombre"/></th>
                        <th><fmt:message key="gesEmpDescripcion"/></th>
                        <th><fmt:message key="gesEmpNombreCompleto"/></th>
                        <th><fmt:message key="gesEmpEmailTutor"/></th>
                        <th><fmt:message key="gesEmpeAcciones"/></th>
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
                                <a href="ServletGestionEmpresas?id=${empresa.id_empresa}" class="btn btn-warning btn-sm">
                                    <fmt:message key="accionEditar"/>
                                </a>
                                <a href="ServletGestionEmpresas?action=borrar&id=${empresa.id_empresa}" 
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('¿Estás seguro de que deseas borrar esta empresa?');">
                                   <fmt:message key="accionBorrar"/>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <a href="crear_editar_empresa.jsp" class="btn btn-primary btn-sm"><fmt:message key="accionCrear"/></a>
        </div>

    </body>
</html>
