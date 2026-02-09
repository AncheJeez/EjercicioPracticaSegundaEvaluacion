<%-- 
    Document   : Register
    Created on : 9 feb 2026, 11:00:34
    Author     : usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
                        <a class="nav-link" href="/EjercicioPracticoSegundaEvaluacion/ServletMostrarTodosLosDatos">Mostrar Datos</a>
                    </li>
                </ul>
            </div>
        </nav>
        <h1>Registrese</h1>
        
        <form action="Registrarse" method="post">
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>
            <div class="row mb-3">
              <label for="nombre" class="col-sm-2 col-form-label">Nombre:</label>
              <div class="col-sm-10">
                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Introduzca su nombre">
              </div>
            </div>
            <div class="row mb-3">
              <label for="apellidos" class="col-sm-2 col-form-label">Apellidos:</label>
              <div class="col-sm-10">
                <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Introduzca sus apellidos">
              </div>
            </div>
            <div class="row mb-3">
                <div class="col-sm-10 offset-sm-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="directiva">
                        <label class="form-check-label" for="remember">Directiva</label>
                    </div>
                </div>
            </div>
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
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="remember">
                      <label class="form-check-label" for="remember">Remember me</label>
                    </div>
                </div>
                <div class="col-sm-10 offset-sm-2">
                    <a href="/EjercicioPracticoSegundaEvaluacion/Logearse">Quiero logearme</a>
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
