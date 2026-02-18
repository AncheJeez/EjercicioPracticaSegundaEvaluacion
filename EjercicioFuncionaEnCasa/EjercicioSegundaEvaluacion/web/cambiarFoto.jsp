<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>
    <c:choose>
        <c:when test="${not empty mensaje}">
            <h2 style="color:green">${mensaje}</h2>
            <img src="fotos/${nombreFichero}" alt="Foto subida" width="200"/>
        </c:when>
        <c:when test="${not empty error}">
            <h2 style="color:red">${error}</h2>
            <p>Intenta subir la imagen de nuevo.</p>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/SubirFoto" method="post" enctype="multipart/form-data">
                <input type="file" name="foto"/>
                <input type="hidden" name="nombreFichero" value=""/>
                <button type="submit">Subir</button>
            </form>
        </c:otherwise>
    </c:choose>
</body>
</html>

