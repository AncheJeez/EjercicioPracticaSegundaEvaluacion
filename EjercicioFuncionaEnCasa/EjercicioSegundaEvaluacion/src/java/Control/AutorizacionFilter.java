package Control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter para controlar autorización basada en rol (atributo "directiva" en sesión).
 */
@WebFilter("/*")
public class AutorizacionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String context = req.getContextPath();

        // Rutas públicas (login, recursos estáticos, páginas públicas)
        if (uri.startsWith(context + "/Logearse") || uri.endsWith("LogIn.jsp") || uri.startsWith(context + "/resources/")
                || uri.equals(context + "/") || uri.endsWith("index.jsp") ) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        // Si no hay sesión, redirigir al login
        if (session == null || session.getAttribute("nombre") == null) {
            res.sendRedirect(context + "/Logearse");
            return;
        }

        // Determinar si la ruta requiere rol de directiva (administración)
        String[] adminPaths = new String[] {
            "/ServletGestionProfesores",
            "/ServletGestionPracticas",
            "/ServletGestionEmpresas",
            "/ServletGestionCurso",
            "/ServletGestionAlumnos"
        };

        boolean requiresAdmin = false;
        for (String p : adminPaths) {
            if (uri.contains(p)) {
                requiresAdmin = true;
                break;
            }
        }

        if (requiresAdmin) {
            Object directivaObj = session.getAttribute("directiva");
            boolean directiva = false;
            if (directivaObj instanceof Boolean) directiva = (Boolean) directivaObj;

            if (!directiva) {
                // Usuario no autorizado
                // Redirigir a página principal con mensaje o mostrar 403
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para acceder a esta página.");
                return;
            }
        }

        // Si todo está bien, continuar
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No-op
    }
}
