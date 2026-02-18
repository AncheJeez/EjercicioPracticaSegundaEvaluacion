<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Log In</title>
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
        <h1 class="ms-3">Inicie sesión</h1>
        
        <form action="Logearse" method="post" class="ms-3 me-3">
            <c:if test="${error != null and error != ''}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>
            <div class="row mb-3">
              <label for="email" class="col-sm-2 col-form-label">Correo electrónico:</label>
              <div class="col-sm-10">
                <input type="email" class="form-control" id="email" name="email" placeholder="Introduzca su correo electrónico">
              </div>
            </div>
            <div class="row mb-3">
              <label for="pwd" class="col-sm-2 col-form-label">Contraseña:</label>
              <div class="col-sm-10">
                <input type="password" class="form-control" id="pwd" name="password" placeholder="Introduzca contraseña">
              </div>
            </div>
            <div class="row mb-3">
                <div class="col-sm-10 offset-sm-2">
                    <a href="/EjercicioSegundaEvaluacion/Registrarse">Quiero registrarme</a>
                </div>
            </div>
            <div class="row mb-3">
              <div class="col-sm-10 offset-sm-2">
                <button type="submit" class="btn btn-primary">Submit</button>
              </div>
            </div>
         </form>

    </body>
</html>
