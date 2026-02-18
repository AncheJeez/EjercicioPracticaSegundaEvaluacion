<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gestionar Empresa</title>
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
        <div class="container mt-5">
            <h2>${practica != null ? 'Editar' : 'Crear'} Práctica</h2>
            <form action="ServletGestionPracticas" method="post">

                <c:if test="${not empty practica}">
                    <input type="hidden" name="id" value="${practica.id_practica}">
                </c:if>

                <div class="mb-3">
                    <label for="alumno" class="form-label">Alumno</label>
                    <select class="form-select" id="alumno" name="alumno" required>
                        <option value="">Seleccione un alumno</option>
                        <c:forEach var="a" items="${listaAlumnos}">
                            <option value="${a.idAlumno}" 
                                <c:if test="${practica != null && practica.alumno.idAlumno == a.idAlumno}">selected</c:if>>
                                ${a.nombre} ${a.apellidos}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="empresa" class="form-label">Empresa</label>
                    <select class="form-select" id="empresa" name="empresa" required>
                        <option value="">Seleccione una empresa</option>
                        <c:forEach var="e" items="${listaEmpresas}">
                            <option value="${e.id_empresa}"
                                <c:if test="${practica != null && practica.empresa.id_empresa == e.id_empresa}">selected</c:if>>
                                ${e.nombre}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="fecha_comienzo" class="form-label">Fecha de Comienzo</label>
                    <input type="date" class="form-control" id="fecha_comienzo" name="fecha_comienzo" 
                        value="${practica != null ? fechaInicioFormateada : ''}" required>
                </div>

                <div class="mb-3">
                    <label for="fecha_finalizacion" class="form-label">Fecha de Finalización</label>
                    <input type="date" class="form-control" id="fecha_finalizacion" name="fecha_finalizacion" 
                        value="${practica != null ? fechaFinFormateada : ''}" required>
                </div>

                <div class="mb-3">
                    <label for="comentarios" class="form-label">Comentarios</label>
                    <textarea class="form-control" id="comentarios" name="comentarios" rows="3" required>
                        ${practica != null ? practica.comentarios : ''}
                    </textarea>
                </div>
                    
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="enviarCorreo" name="enviarCorreo" 
                           <c:if test="${param.enviarCorreo eq 'on'}">checked</c:if>>
                    <label class="form-check-label" for="enviarCorreo">Enviar correo electrónico</label>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Correo electrónico</label>
                    <input type="email" class="form-control" id="email" name="email" 
                           value="${practica != null ? practica.alumno.email : ''}" 
                           placeholder="Introduce el correo" >
                </div>

                <button type="submit" class="btn btn-primary">Guardar</button>
            </form>
        </div>
    </body>
</html>

