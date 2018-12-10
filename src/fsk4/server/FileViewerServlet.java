package fsk4.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fsk.server.modelo.Fichero;
import fsk.server.modelo.Helper;
import fsk.server.modelo.Transaccion;


public class FileViewerServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, final HttpServletResponse resp) throws IOException {
	    // Get the absolute path of the image
	    ServletContext sc = getServletContext();
	    
	    String aux = req.getPathInfo();
	    if (aux.startsWith("/")) aux = aux.substring(1);
	    
	    final String id = aux.substring(0, aux.indexOf("/"));
	    String fn = aux.substring(aux.lastIndexOf("/") + 1);	  

	    System.out.println("sirviendo imagen " + id + "----" + fn);
	    
	    String pathcache = System.getProperty("cachedir", "");
		System.out.println("pathcache=" + pathcache);
	    if (!pathcache.endsWith("/")) pathcache += "/";
	    
	    final File fc = new File(pathcache + id + "/" + fn);
	    if (!fc.exists()) {
	    	try {
				Helper.transact(new Transaccion() {
					
					@Override
					public void ejecutar(EntityManager em) throws Exception {
						Fichero f = em.find(Fichero.class, Long.parseLong(id));
						if (f != null) System.out.println("f(" + f.getId() + ")=" + f.getNombre());
						else System.out.println("f(" + id + ")=null");
						Helper.escribirFichero(fc.getAbsolutePath(), f.getBytes());					
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    resp.sendRedirect("/cache/" + id + "/" + URLEncoder.encode(fn, "UTF-8").replaceAll("\\+", "%20"));
	    
	    if (false) {
		    // Get the MIME type of the image
		    String mimeType = sc.getMimeType(fn);
		    /*
		    if (mimeType == null) {
		        sc.log("Could not get MIME type of "+fn);
		        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		        return;
		    }
		    */

		    // Set content type
		    resp.setContentType(mimeType);
		    
		    if (fn.toLowerCase().endsWith("svgz")) {
			    resp.setContentType("image/svg+xml");
			    resp.setHeader("Content-Encoding", "gzip");
		    }


		    try {
				
		    	Helper.transact(new Transaccion() {
					
					@Override
					public void ejecutar(EntityManager em) throws Exception {
						Fichero f = em.find(Fichero.class, Long.parseLong(id));
						
						byte[] bytes = f.getBytes();
						
					    // Set content size
					    resp.setContentLength(bytes.length);

					    // Open the file and output streams
					    OutputStream out = resp.getOutputStream();

					    // Copy the contents of the file to the output stream
					    out.write(bytes);
					    out.close();
						
					}
					
				});
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
	    }

	}
	
}
