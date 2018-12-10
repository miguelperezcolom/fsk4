package fsk4.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fsk.server.aux.NuevoImporter;
import fsk.server.modelo.Helper;
import fsk.server.modelo.Movimiento;
import fsk.server.modelo.Render;
import fsk.server.modelo.Transaccion;
import fsk.server.modelo.Usuario;

public class Web {


	private static Thread hiloActualizarCatalogo;

	private static Thread hiloCrearExcelCatalogo;

	public static List<String> getValores(Map m, String k) {
		List<String> l = new ArrayList<String>();
		Object o = m.get(k);
		if (o != null) {
			if (o instanceof String) {
				l.add((String) o);
			} else if (o instanceof String[]) {
				String[] s = (String[]) o;
				for (String v : s) l.add(v);
			}
		}
		return l;
	}

	public static String getValor(Map m, String k) {
		String v = null;
		Object o = m.get(k);
		if (o != null) {
			if (o instanceof String) {
				v = (String) o;
			} else if (o instanceof String[]) {
				String[] s = (String[]) o;
				if (s.length > 0) v = s[0];
			}
		}
		return v;
	}


	public static void registrar(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		final StringBuffer errores = new StringBuffer("");

		if ("".equals(errores.toString()) && Helper.vacio(request.getParameter("email"))) errores.append("El email es obligatorio");
		if ("".equals(errores.toString()) && Helper.vacio(request.getParameter("nombre"))) errores.append("El nombre es obligatorio");
		if ("".equals(errores.toString()) && Helper.vacio(request.getParameter("apellidos"))) errores.append("Los apellidos son obligatorios");
		if ("".equals(errores.toString()) && Helper.vacio(request.getParameter("nacimiento"))) errores.append("La fecha de nacimiento es obligatoria");
		Date n = null;
		try {
			n = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("nacimiento"));
		} catch (Exception e) {			
		}
		if ("".equals(errores.toString()) && n == null) errores.append("La fecha de nacimiento debe ser en formato dd/MM/yyyy (Ejemplo: 30/01/1969)");
		if ("".equals(errores.toString()) && Helper.vacio(request.getParameter("cp"))) errores.append("El codigo postal es obligatorio");

		if ("".equals(errores.toString())) {

			try {
				Helper.transact(new Transaccion() {

					@Override
					public void ejecutar(EntityManager em) throws Exception {
						Usuario u = em.find(Usuario.class, request.getParameter("email").trim().toLowerCase());
						if (u != null && "miguelperezcolom@gmail.com".equalsIgnoreCase(u.getEmail())) {
							
						} else {
							if (u != null && (u.getMovimientos().size() > 0 || (u.getCreado() != null && u.getCreado().getTime() < new Date().getTime() - 24l * 60l * 60l * 1000l))) errores.append("Ya existe un usuario con el email " + request.getParameter("email")); 
							else if (u == null) {
								u = new Usuario();
								u.setLogin(request.getParameter("email").trim().toLowerCase());
								u.setCreado(new Date());
								u.setTipo(u.TIPO_GRATUITO);
								em.persist(u);
							}
							if ( "".equals(errores.toString())) {
								u.setApelidos(request.getParameter("apellidos"));
								u.setCodigoPostal(request.getParameter("cp").trim());
								u.setEmail(request.getParameter("email").trim().toLowerCase());
								u.setModificado(new Date());
								u.setNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("nacimiento")));
								u.setNombre(request.getParameter("nombre"));
							}
						}
						
						if (u != null && "".equals(errores.toString())) request.getSession().setAttribute("usuario", u.getLogin());

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				errores.append("" + e.getClass().getName() + ":" + e.getMessage());
			}
		}

		System.out.println("Web.registrar(rq,rs) --> errores:" + errores);
		
		if ("".equals(errores.toString())) {
			response.getWriter().print("ok");
		} else {
			System.out.println("Web.registrar(rq,rs) -->  devolvemos 700/" + errores.toString());
			response.sendError(700, errores.toString());
		}
	}

	public static void pro1ok(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Usuario u = em.find(Usuario.class, (String) request.getSession().getAttribute("usuario"));
					u.setTipo(u.TIPO_PRO);
					Movimiento m;
					u.getMovimientos().add(m = new Movimiento());
					m.setCreado(new Date());
					m.setCreditos(1000);
					m.setEstado(m.ESTADO_CONFIRMADO);
					m.setId(Helper.getUUID());
					m.setImporteEuros(1);
					m.setObservaciones(logString(request));
					m.setUsuario(u);
					m.setTipo(m.TIPO_SUSCRIPCIONPAYPAL);
					em.persist(m);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Fsk4.html");
	}

	public static void pro8ok(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Usuario u = em.find(Usuario.class, (String) request.getSession().getAttribute("usuario"));
					u.setTipo(u.TIPO_PRO);
					Movimiento m;
					u.getMovimientos().add(m = new Movimiento());
					m.setCreado(new Date());
					m.setCreditos(1000);
					m.setEstado(m.ESTADO_CONFIRMADO);
					m.setId(Helper.getUUID());
					m.setImporteEuros(8);
					m.setObservaciones(logString(request));
					m.setUsuario(u);
					m.setTipo(m.TIPO_SUSCRIPCIONPAYPAL);
					em.persist(m);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Fsk4.html");
	}

	public static void tienda25ok(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Usuario u = em.find(Usuario.class, (String) request.getSession().getAttribute("usuario"));
					u.setTipo(u.TIPO_TIENDA);
					Movimiento m;
					u.getMovimientos().add(m = new Movimiento());
					m.setCreado(new Date());
					m.setCreditos(1000);
					m.setEstado(m.ESTADO_CONFIRMADO);
					m.setId(Helper.getUUID());
					m.setImporteEuros(25);
					m.setObservaciones(logString(request));
					m.setUsuario(u);
					m.setTipo(m.TIPO_SUSCRIPCIONPAYPAL);
					em.persist(m);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Fsk4.html");
	}

	public static void pro1ko(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		response.sendRedirect("/Fsk4.html");
	}

	public static void pro8ko(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		response.sendRedirect("/Fsk4.html");
	}

	public static void tienda25ko(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		response.sendRedirect("/Fsk4.html");
	}

	private static void log(HttpServletRequest request) {
		System.out.println("*********************************************************************************************");
		System.out.println(request.getRequestURI());
		System.out.println("*********************************************************************************************");
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
			String k = (String) e.nextElement();
			String[] v = request.getParameterValues(k);
			for (int i = 0; i < v.length; i++) {
				System.out.println("" + k + "=" + v[i]);
			}
		}
		System.out.println("*********************************************************************************************");
		System.out.println("*********************************************************************************************");
	}

	private static String logString(HttpServletRequest request) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("*********************************************************************************************");
		pw.println(request.getRequestURI());
		pw.println("*********************************************************************************************");
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
			String k = (String) e.nextElement();
			String[] v = request.getParameterValues(k);
			for (int i = 0; i < v.length; i++) {
				pw.println("" + k + "=" + v[i]);
			}
		}
		pw.println("*********************************************************************************************");
		pw.println("*********************************************************************************************");
		return sw.toString();
	}

	
	public static void pro8upok(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Usuario u = em.find(Usuario.class, (String) request.getSession().getAttribute("usuario"));
					u.setTipo(u.TIPO_PRO);
					Movimiento m;
					u.getMovimientos().add(m = new Movimiento());
					m.setCreado(new Date());
					m.setCreditos(1000);
					m.setEstado(m.ESTADO_CONFIRMADO);
					m.setId(Helper.getUUID());
					m.setImporteEuros(8);
					m.setObservaciones(logString(request));
					m.setUsuario(u);
					m.setTipo(m.TIPO_SUSCRIPCIONPAYPAL);
					em.persist(m);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Fsk4.html");
	}

	public static void tienda25upok(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		log(request);
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Usuario u = em.find(Usuario.class, (String) request.getSession().getAttribute("usuario"));
					u.setTipo(u.TIPO_TIENDA);
					Movimiento m;
					u.getMovimientos().add(m = new Movimiento());
					m.setCreado(new Date());
					m.setCreditos(1000);
					m.setEstado(m.ESTADO_CONFIRMADO);
					m.setId(Helper.getUUID());
					m.setImporteEuros(25);
					m.setObservaciones(logString(request));
					m.setUsuario(u);
					m.setTipo(m.TIPO_SUSCRIPCIONPAYPAL);
					em.persist(m);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Fsk4.html");
	}

	public static void miniatura(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				String[][] x = Helper.sqlString("sql: select ren_id from fsk_render where ren_render = " + request.getParameter("idfichero"));
				
				Render r = em.find(Render.class, Long.parseLong(x[0][0]));
				
				response.sendRedirect("/" + r.getMiniatura200x150().toString());
				
			}
		});
	}

	public static void grande(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				String[][] x = Helper.sqlString("sql: select ren_id from fsk_render where ren_render = " + request.getParameter("idfichero"));
				
				Render r = em.find(Render.class, Long.parseLong(x[0][0]));
				
				response.sendRedirect("/" + r.getRender().toString());
				
			}
		});
	}

	public static String render(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		
		final StringBuffer h = new StringBuffer();

		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				/*

	<div class="row">
	
	<div class="4u">
		<div class="thumbnail first">
			<a href="http://www.feskteva.com/Fsk4.html?idficherorender=1379086109318"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=1379086109318" alt="" width="200" height="150" /></a>
		</div>
	</div>
	<div class="3u">
		<div class="thumbnail first">
			<a href="http://www.feskteva.com/Fsk4.html?idficherorender=1379005536820"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=1379005536820" alt="" width="200" height="150" /></a>
		</div>
	</div>
	<div class="3u">
		<div class="thumbnail first">
			<a href="http://www.feskteva.com/Fsk4.html?idficherorender=1378957080063"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=1378957080063" alt="" width="200" height="150" /></a>
		</div>
	</div>
	<div class="3u">
		<div class="thumbnail first">
			<a href="#"><img src="images/watermark.png" alt="" /></a>
		</div>
	</div>
</div>


<div class="row">
	<div class="12u">
		<div class="divider"></div>
	</div>
</div>


				 */
				
				String[][] x = Helper.sqlString("sql: select ren_id from fsk_render where ren_id = " + request.getParameter("idrender"));
				
				Render r = em.find(Render.class, Long.parseLong(x[0][0]));
				
				h.append("	<div class='12u'><div class='thumbnail first'><a href='" + System.getProperty("urlfeskteva", "http://www.feskteva.com") +"/Fsk4.html?idficherorender=" + r.getRender().getId() + "'><img src='" + System.getProperty("urlfeskteva", "http://www.feskteva.com") + "/" + r.getRender().toString() + "'  data-toggle='tooltip' title='Haz clic para abrir este plano'   /></a></div></div>");

			}
		});
		return h.toString();
	}

	public static String galeria(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final StringBuffer h = new StringBuffer();

		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				/*

	<div class="row">
	
	<div class="4u">
		<div class="thumbnail first">
			<a href="http://www.feskteva.com/Fsk4.html?idficherorender=1379086109318"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=1379086109318" alt="" width="200" height="150" /></a>
		</div>
	</div>
	<div class="3u">
		<div class="thumbnail first">
			<a href="http://www.feskteva.com/Fsk4.html?idficherorender=1379005536820"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=1379005536820" alt="" width="200" height="150" /></a>
		</div>
	</div>
	<div class="3u">
		<div class="thumbnail first">
			<a href="http://www.feskteva.com/Fsk4.html?idficherorender=1378957080063"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=1378957080063" alt="" width="200" height="150" /></a>
		</div>
	</div>
	<div class="3u">
		<div class="thumbnail first">
			<a href="#"><img src="images/watermark.png" alt="" /></a>
		</div>
	</div>
</div>


<div class="row">
	<div class="12u">
		<div class="divider"></div>
	</div>
</div>


				 */
				
				
				
				String[][] x = Helper.sqlString("sql: select ren_id from fsk_render where ren_estado = 'Hecho' and ren_render is not null order by ren_id desc limit 30");
				
				int pos = 0;
				for (final String[] y : x) {
					if (pos > 0 && pos % 3 == 0) h.append("</div><div class='row'><div class='12u'><div class='divider'></div></div></div>");
					if (pos % 3 == 0) h.append("<div class='row'>");
					try {
						Helper.transact(new Transaccion() {
							
							@Override
							public void ejecutar(EntityManager em) throws Exception {
								
								Render r = em.find(Render.class, Long.parseLong(y[0]));
								
								h.append("	<div class='4u'><div class='thumbnail first'><a href='render.jsp?idrender=" + r.getId() + "'><img src='" + System.getProperty("urlfeskteva", "http://www.feskteva.com") +"/miniatura.jsp?idfichero=" + r.getRender().getId() + "'  data-toggle='tooltip' title='Haz clic para abrir este plano'  width='200' height='150' /></a></div></div>");
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					pos++;
				}
				
				if (pos > 0) {
					h.append("</div>");
				}
				
				
				
			}
		});
		return h.toString();
	}
	
	public static void actualizarCatalogo(HttpServletRequest request, HttpServletResponse response) {
		if (hiloActualizarCatalogo == null) {
			hiloActualizarCatalogo = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						String pathDropbox = System.getProperty("pathdropbox");						
						if (!pathDropbox.endsWith("/")) pathDropbox += "/";
						NuevoImporter.importar(pathDropbox + "feskteva/CatàlegBETA");
					} catch (Exception e) {
						e.printStackTrace();
					}
					hiloActualizarCatalogo = null;
				}
			});
			hiloActualizarCatalogo.start();
		}
	}

	public static void crearExcelCatalogo(final HttpServletRequest request, HttpServletResponse response) {
		if (hiloCrearExcelCatalogo == null) {
			hiloCrearExcelCatalogo = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						NuevoImporter.exportar(System.getProperty("pathtmp") + "/catalogo.xls");
					} catch (Exception e) {
						e.printStackTrace();
					}
					hiloCrearExcelCatalogo = null;
				}
			});
			hiloCrearExcelCatalogo.start();
		}
	}

}
