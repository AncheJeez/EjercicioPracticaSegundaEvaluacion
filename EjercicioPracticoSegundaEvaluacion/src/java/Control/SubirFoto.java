package Control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author usuario
 */
@MultipartConfig
@WebServlet(name = "SubirFoto", urlPatterns = {"/SubirFoto","/subirfoto","/subirFoto"})
public class SubirFoto extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            String error=null;
            String mensaje = null;
            String nombre ="";
            String extension="";
            BufferedReader lectorNombre = new BufferedReader(new InputStreamReader(request.getPart("nombreFichero").getInputStream()));
            nombre = lectorNombre.readLine();
            lectorNombre.close();
            Part datosSubidos = request.getPart("foto");
            if (datosSubidos == null){
                error = "No se ha recibido la imagen";
            }else{
                if(datosSubidos.getSize() > 100*1024){
                    error = "No se permiten ficheros superiores a 100kb";
                }else{
                    String tipoContenido = datosSubidos.getContentType();
                    int posicion = tipoContenido.indexOf("/");
                    extension = tipoContenido.substring(posicion + 1);
                }
                if(error == null){
                    nombre = nombre + "." +extension;
                    String rutaFichero = request.getServletContext().getRealPath("fotos");
                    File carpetaFotos = new File(rutaFichero);

                    if (!carpetaFotos.exists()) {
                        carpetaFotos.mkdirs();
                    }
                    File ficheroDestino = new File(carpetaFotos, nombre);
                    FileOutputStream fichero = new FileOutputStream(ficheroDestino);
                    InputStream contenido = datosSubidos.getInputStream();
                    byte[] bytes = new byte[2048];
                    while(contenido.available()>0){
                        int longitud = contenido.read(bytes);
                        fichero.write(bytes, 0, longitud);
                    }
                    mensaje = "Se ha subido el fichero correctamente";
                    fichero.close();
                    contenido.close();
                }
//                String parametros = error.isEmpty() ? "Mensaje: "+mensaje: "Error: "+error;
                request.setAttribute("nombreFichero", nombre);
                request.setAttribute("mensaje", mensaje);
                request.setAttribute("error", error);
//                request.getRequestDispatcher(response.encodeURL("cambiarFoto.jsp"+parametros)).forward(request,response);
                request.getRequestDispatcher("cambiarFoto.jsp").forward(request, response);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        request.getRequestDispatcher(response.encodeURL("cambiarFoto.jsp")).forward(request,response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
