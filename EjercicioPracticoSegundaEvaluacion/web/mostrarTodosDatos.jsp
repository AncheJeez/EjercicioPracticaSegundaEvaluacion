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
    </head>
    <body>
        Alumnos
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
