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
                    <c:if test="${not empty sessionScope.nombre}">
                        <div class="ms-2 me-2">
                            <span class="badge bg-secondary">
                                <c:choose>
                                    <c:when test="${sessionScope.directiva}">Directiva</c:when>
                                    <c:otherwise>Profesor</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </c:if>
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

<div class="container mt-5">

    <h2>${alumno != null ? 'Editar' : 'Crear'} Alumno</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            <strong>Error:</strong> ${error}
        </div>
        <c:if test="${not empty exceptionStack}">
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">Detalles de la excepción</h5>
                    <pre style="white-space: pre-wrap; font-size: 0.9rem;">${exceptionStack}</pre>
                </div>
            </div>
        </c:if>
    </c:if>

    <form action="ServletGestionAlumnos" method="post">

        <c:if test="${not empty alumno}">
            <input type="hidden" name="id" value="${alumno.idAlumno}">
        </c:if>

        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre</label>
            <input type="text" 
                   class="form-control" 
                   id="nombre" 
                   name="nombre"
                   value="${not empty alumno ? alumno.nombre : ''}" 
                   required>
        </div>

        <div class="mb-3">
            <label for="apellidos" class="form-label">Apellidos</label>
            <input type="text" 
                   class="form-control" 
                   id="apellidos" 
                   name="apellidos"
                   value="${not empty alumno ? alumno.apellidos : ''}" 
                   required>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" 
                   class="form-control" 
                   id="email" 
                   name="email"
                   value="${not empty alumno ? alumno.email : ''}" 
                   required>
        </div>

        <div class="mb-3">
            <label for="curso_matriculado" class="form-label">Curso Matriculado</label>
            <c:choose>
                <c:when test="${not empty cursos}">
                    <select id="curso_matriculado" name="curso_matriculado" class="form-select" required>
                        <option value="">-- Seleccionar curso --</option>
                        <c:forEach var="c" items="${cursos}">
                            <!-- c may be a Curso object or String; try properties accordingly -->
                            <c:choose>
                                <c:when test="${c.nombre != null}">
                                    <option value="${c.nombre}" <c:if test="${not empty alumno && alumno.cursoMatriculado == c.nombre}">selected</c:if>>${c.nombre}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${c}" <c:if test="${not empty alumno && alumno.cursoMatriculado == c}">selected</c:if>>${c}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </c:when>
                <c:otherwise>
                    <input type="text" 
                           class="form-control" 
                           id="curso_matriculado" 
                           name="curso_matriculado"
                           value="${not empty alumno ? alumno.cursoMatriculado : ''}" 
                           required>
                    <div class="form-text">No hay cursos disponibles — se creará uno nuevo con el nombre introducido.</div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="mb-3">
            <label for="fecha_nac" class="form-label">Fecha de Nacimiento</label>
            <input type="date" 
                   class="form-control" 
                   id="fecha_nac" 
                   name="fecha_nac"
                   value="${not empty alumno ? fechaFormateada : ''}"
                   required>
        </div>

        <div class="mb-3">
            <label for="grupo" class="form-label">Grupo</label>
            <select id="grupo" name="grupo" class="form-select" required>
                <option value="A" <c:if test="${alumno != null && alumno.grupo == 'A'}">selected="selected"</c:if>>A</option>
                <option value="B" <c:if test="${alumno != null && alumno.grupo == 'B'}">selected="selected"</c:if>>B</option>
                <option value="C" <c:if test="${alumno != null && alumno.grupo == 'C'}">selected="selected"</c:if>>C</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>

    </form>
</div>

</body>
</html>
