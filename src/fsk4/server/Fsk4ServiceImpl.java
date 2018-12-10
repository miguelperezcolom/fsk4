package fsk4.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fsk.server.aux.BlenderWriter;
import fsk.server.aux.IGSHelper;
import fsk.server.aux.PDFHelper;
import fsk.server.aux.SVGWriter;
import fsk.server.modelo.Articulo;
import fsk.server.modelo.Disenyo;
import fsk.server.modelo.Envio;
import fsk.server.modelo.Fichero;
import fsk.server.modelo.Helper;
import fsk.server.modelo.Movimiento;
import fsk.server.modelo.Render;
import fsk.server.modelo.SobreescrituraTextura;
import fsk.server.modelo.Transaccion;
import fsk.server.modelo.Usuario;
import fsk4.client.Fsk4Service;
import fsk4.shared.GrupoTexturas;
import fsk4.shared.InfoArticulo;
import fsk4.shared.Modelo;
import fsk4.shared.Objeto;
import fsk4.shared.Plano;
import fsk4.shared.Planta;
import fsk4.shared.Textura;
import fsk4.shared.TexturaPlano;
import fsk4.shared.TexturasPlano;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class Fsk4ServiceImpl extends RemoteServiceServlet implements
		Fsk4Service {

	@Override
	public String preview(Plano plano) throws Exception {
		long t0 = new Date().getTime();

		String path = ((getServletConfig() != null)?getServletContext().getRealPath("/"):"/home/miguel") + "/tmp";
		
		//String fn = IGSHelper.render(plano, path, 2, false, 218, 163);
		String fn = BlenderWriter.render(plano, path, 2, true, 218, 163);
		//String fn = SVGWriter.render(plano, path, 2, false, 218, 163);
		
		System.out.println("Preview generada en " + (new Date().getTime() - t0) + "ms.");

		return "tmp/" + fn;
	}
	
	@Override
	public String previewGrande(Plano plano) throws Exception {
		long t0 = new Date().getTime();

		String path = ((getServletConfig() != null)?getServletContext().getRealPath("/"):"/home/miguel") + "/tmp";
		
		String fn = IGSHelper.render(System.getProperty("pathindigo", "/home/web/IndigoRenderer_x64_v3.4.18"), plano, path, 15, true, true, 400, 300, 2, false);
		//String fn = BlenderWriter.render(plano, path, 2, true, 400, 300);
		
		System.out.println("Preview (" + fn + ") generada en " + (new Date().getTime() - t0) + "ms.");

		return "tmp/" + fn;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new Fsk4ServiceImpl().preview(new Plano()));
	}

	@Override
	public String[][] sql(String sql) throws Exception {
		return Helper.sqlString(sql);
	}

	@Override
	public void login(final String email, final String password) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Usuario u = null;
				try {
					u = em.find(Usuario.class, email.toLowerCase().trim());
				} catch (Exception e) {
					
				}
				if (u == null) throw new Exception("No hay ningún usuario con el email " + email);
				else {
					boolean ok = false;
					String p = (password != null)?password.trim():"";
					if (password.equalsIgnoreCase(u.getPassword())) ok = true;
					else if (u.getNacimiento() != null && password.equalsIgnoreCase(new SimpleDateFormat("yyyy").format(u.getNacimiento()))) ok = true;
					else if (u.getNacimiento() != null && password.equalsIgnoreCase(new SimpleDateFormat("dd/MM/yyyy").format(u.getNacimiento()))) ok = true;
					else if (u.getNacimiento() != null && password.equalsIgnoreCase(new SimpleDateFormat("ddMMyyyy").format(u.getNacimiento()))) ok = true;
					else if (u.getNacimiento() != null && password.equalsIgnoreCase(new SimpleDateFormat("ddMM").format(u.getNacimiento()))) ok = true;
					else if (u.getNacimiento() != null && password.equalsIgnoreCase(new SimpleDateFormat("yyyyMMdd").format(u.getNacimiento()))) ok = true;
					if (!ok) throw new Exception("El password no es correcto"); 
					u.setUltimoAcceso(new Date());
				}
			}
		});
	}

	@Override
	public void registrar(final String email, String password, final String nombre,
			final String apellidos, final String nacimiento, final String cp) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Usuario u = null;
				try {
					u = em.find(Usuario.class, email.toLowerCase().trim());
				} catch (Exception e) {
					
				}
				if (u != null) throw new Exception("Ya hay ningún usuario con el email " + email);
				
				u = new Usuario();
				u.setLogin(email.toLowerCase().trim());
				u.setEmail(email.toLowerCase().trim());
				u.setNombre(nombre);
				u.setApelidos(apellidos);
				u.setCodigoPostal(cp);
				u.setCreado(new Date());
				u.setEstado(u.ESTADO_ACTIVO);
				u.setModificado(new Date());
				u.setNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(nacimiento));
				u.setUltimoAcceso(new Date());
				Movimiento m;
				u.getMovimientos().add(m= new Movimiento());
				m.setId(Helper.getUUID());
				m.setCreado(new Date());
				m.setCreditos(100);
				m.setEstado(m.ESTADO_CONFIRMADO);
				m.setTipo(m.TIPO_FREEALTA);
				m.setImporteEuros(0);
				m.setModificado(new Date());
				m.setUsuario(u);
				em.persist(m);
				em.persist(u);
			}
		});
		
		Helper.enviarEmail("pere@feskteva.com;xavier@feskteva.com;biel@feskteva.com;miguel@feskteva.com", "NUEVO USUARIO DE FESKTEVA", "" + nombre + " " + apellidos + " (" + email + " / " + cp + ") se ha registrado en feskteva.");
	}

	@Override
	public long guardar(final String email, final Plano plano) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Disenyo d = null;
				if (plano.getId() == -1) {
					d = new Disenyo();
					d.setId(Helper.getUUID());
					d.setUsuario(em.find(Usuario.class, email));
					d.getUsuario().getDisenyos().add(d);
					d.setCreado(new Date());
					em.persist(d);

					plano.setId(d.getId());
				} else {
					d = em.find(Disenyo.class, plano.getId());
				}
				
				d.setModificado(new Date());
				d.setNombre(plano.getNombre());
				d.setBytes(Helper.serializar(plano));
				
			}
		});
		return plano.getId();
	}

	@Override
	public Plano getPlano(final String id) throws Exception {
		final Plano[] p = new Plano[1];
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Disenyo d = em.find(Disenyo.class, Long.parseLong(id));
				
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(d.getBytes()));
				p[0] = (Plano) ois.readObject();
				
			}
		});
		return p[0];
	}

	@Override
	public long guardarComo(final String email, final Plano plano)
			throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Disenyo d = new Disenyo();
				d.setId(Helper.getUUID());
				d.setUsuario(em.find(Usuario.class, email));
				d.getUsuario().getDisenyos().add(d);
				d.setCreado(new Date());
				em.persist(d);
				
				plano.setId(d.getId());

				d.setModificado(new Date());
				d.setNombre(plano.getNombre());
				d.setBytes(Helper.serializar(plano));
				
			}
		});
		return plano.getId();
	}

	@Override
	public void foto(final String email, final Plano plano, final int segundosRender) throws Exception {
		
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Usuario u = em.find(Usuario.class, email);
				
				if (true || u.getCreditosRestantes() >= 1) {
					Render r = new Render();
					r.setId(Helper.getUUID());
					r.setCreado(new Date());			
					r.setSegundosRender(segundosRender);
					long segundosCola = 0;
					try { 
						segundosCola = Long.parseLong((String) Helper.sqlString("sql: select sum(ren_segundosrender) from fsk_render where ren_estado in ('" + Render.ESTADO_ENCOLA + "', '" + Render.ESTADO_RENDERIZANDO + "')")[0][0]);
					} catch (Exception e) {
						//e.printStackTrace();
					}
					r.setFinRenderPrevisto(new Date(new Date().getTime() + (r.getSegundosRender() + segundosCola) * 1000l));
					r.setEstado(r.ESTADO_ENCOLA);
					r.setPlano(Helper.serializar(plano));
					r.setUsuario(em.find(Usuario.class, email));
					r.getUsuario().getRenders().add(r);					
					em.persist(r);
				} else {
					throw new Exception("No quedan créditos");
				}
				
			}
		});

	}

	@Override
	public void cancelarRender(final String idRender) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Render r = em.find(Render.class, Long.parseLong(idRender));
				if (r.ESTADO_ENCOLA.equals(r.getEstado())) r.setEstado(r.ESTADO_CANCELADO);
				else throw new Exception("Lo siento. No se ha podido cancelar el renderizado");
			}
		});
	}

	@Override
	public Map<String, String> getDatosUsuario(final String email) throws Exception {
		final Map<String, String> datos = new HashMap<String, String>();
		
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Usuario u = null;
				try {
					u = em.find(Usuario.class, email.toLowerCase().trim());
				} catch (Exception e) {
					
				}
				if (u == null) throw new Exception("No hay ningún usuario con el email " + email);
				else {
					datos.put("login", u.getLogin());
					datos.put("email", u.getEmail());
					datos.put("nombre", u.getNombre());
					datos.put("apellidos", u.getApelidos());
					if (u.getNacimiento() != null) datos.put("nacimiento", new SimpleDateFormat("dd/MM/yyyy").format(u.getNacimiento()));					
					datos.put("tipo", (Helper.vacio(u.getTipo()))?u.TIPO_GRATUITO:u.getTipo());
					if (u.getLogo() != null) datos.put("logo", u.getLogo().toString());
					
					datos.put("creditos", "" + u.getCreditosRestantes());
				}
			}
		});
		
		return datos;
	}

	@Override
	public void grabarPerfil(final String login, final String email, String password, final String nombre,
			final String apellidos, final String nacimiento, final String idLogo) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Usuario u = null;
				try {
					u = em.find(Usuario.class, login.toLowerCase().trim());
				} catch (Exception e) {
					
				}
				if (u == null) throw new Exception("No hay ningún usuario con el login " + login);
				else {
					u.setEmail(email);
					u.setNombre(nombre);
					u.setApelidos(apellidos);
					u.setModificado(new Date());
					u.setNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(nacimiento));
					if (!Helper.vacio(idLogo)) {
						Fichero l = u.getLogo();
						if (l == null) {
							l = new Fichero();
							l.setCreado(new Date());
							l.setCreadoPor(u);
							l.setId(Helper.getUUID());
							em.persist(l);
							u.setLogo(l);
						}
						File f = UploadServlet.getFile(idLogo);
						l.setBytes(Helper.leerFichero(new FileInputStream(f)));
						l.setMimeType(UploadServlet.getContentType(idLogo));
						l.setNombre(f.getName());
						
						try {
							BufferedImage bimg = ImageIO.read(f);
							int width          = bimg.getWidth();
							int height         = bimg.getHeight();
							l.setAlto(height);
							l.setAncho(width);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	@Override
	public void enviarPorEmail(final String email, final String aEmail, final Plano plano)
			throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Envio r = new Envio();
				r.setId(Helper.getUUID());
				r.setCreado(new Date());
				r.setEstado(r.ESTADO_ENCOLA);
				r.setPlano(Helper.serializar(plano));
				r.setUsuario(em.find(Usuario.class, email));
				r.setAEmail(aEmail);
				em.persist(r);
				r.enviar(em);
			}
		});
	}

	@Override
	public Plano getPlanoEnvio(final String idEnvio) throws Exception {
		final Plano[] p = new Plano[1];
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Envio d = em.find(Envio.class, Long.parseLong(idEnvio));
				
				if (d == null) {
					Render r = em.find(Render.class, Long.parseLong(Helper.sqlString("sql: select ren_id from fsk_render where ren_render = " + idEnvio)[0][0]));
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(r.getPlano()));
					p[0] = (Plano) ois.readObject();
				} else {
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(d.getPlano()));
					p[0] = (Plano) ois.readObject();
				}
				
			}
		});
		return p[0];
	}

	@Override
	public Plano getPlanoRender(final String idFichero) throws Exception {
		final Plano[] p = new Plano[1];
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Render r = em.find(Render.class, Long.parseLong(Helper.sqlString("sql: select ren_id from fsk_render where ren_render = " + idFichero)[0][0]));
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(r.getPlano()));
				p[0] = (Plano) ois.readObject();
				
			}
		});
		return p[0];
	}

	@Override
	public String getHtmlFormularioTPV(final String email, final double importeEuros) throws Exception {
		
		final StringBuffer html = new StringBuffer();
		
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				DecimalFormat f = new DecimalFormat("####.00");

				Usuario u = em.find(Usuario.class, email);
				
				Movimiento m;
				u.getMovimientos().add(m= new Movimiento());
				m.setId(Helper.getNextVal("fsk_numerotransacciontpv"));
				m.setCreado(new Date());
				m.setCreditos(100);
				m.setEstado(m.ESTADO_PENDIENTE);
				m.setTipo(m.TIPO_COMPRATPV);
				m.setImporteEuros(importeEuros);
				m.setModificado(new Date());
				m.setUsuario(u);
				em.persist(m);
				
				String Merchant_Name = "PRUEBAS"; // ""
				String Merchant_Signature = "";
				String Merchant_Amount = f.format(m.getImporteEuros()).replaceAll("\\.", "").replaceAll("\\,", "");														//Valor 1 de la firma
				String Merchant_Order = "" + m.getId();													//Valor 2 de la firma
				String Merchant_Currency = "978"; 														//Valor 4 de la firma
				String Merchant_TransactionType = "0"; 													//Valor 5 de la firma
				String Merchant_ProductDescription = "TEST";
				String Merchant_Titular = u.getNombre() + " " + u.getApelidos();
				String Merchant_Merchant_UrlOK = "http://www.fescateva.com/pagadook.jsp";
				String Merchant_Merchant_UrlKO = "http://www.fescateva.com/pagadoko.jsp";

				String Merchant_Code = "22052526";  //"297487894";	//297375495													//Valor 3 de la firma
				String Merchant_MerchantURL = "http://www.fescateva.com/confirmarpagosermepa";			//Valor 6 de la firma
				String Merchant_Password = "qwertyasdf0123456789"; 												//Valor 7 de la firma
				String Merchant_Terminal = "001";

				byte bAmount[] = new byte[Merchant_Amount.length()];  
				byte bOrder[] = new byte[Merchant_Order.length()];  
				byte bCode[] = new byte[Merchant_Code.length()];  
				byte bCurrency[] = new byte[Merchant_Currency.length()];
				byte bTransactionType[] = new byte[Merchant_TransactionType.length()];  
				byte bMerchantURL[] = new byte[Merchant_MerchantURL.length()];  
				byte bMerchantURLOK[] = new byte[Merchant_Merchant_UrlOK.length()];  
				byte bMerchantURLKO[] = new byte[Merchant_Merchant_UrlKO.length()];  
				byte bPassword[] = new byte[Merchant_Password.length()];  

				bAmount = Merchant_Amount.getBytes();
				bOrder = Merchant_Order.getBytes();
				bCode = Merchant_Code.getBytes();
				bCurrency = Merchant_Currency.getBytes();
				bTransactionType = Merchant_TransactionType.getBytes();
				bMerchantURL = Merchant_MerchantURL.getBytes();
				bMerchantURLOK = Merchant_Merchant_UrlOK.getBytes();
				bMerchantURLKO = Merchant_Merchant_UrlKO.getBytes();
				bPassword = Merchant_Password.getBytes();

				MessageDigest sha = MessageDigest.getInstance("SHA-1");
				sha.update(bAmount);
				sha.update(bOrder);
				sha.update(bCode); 
				sha.update(bCurrency);
				sha.update(bTransactionType);
				sha.update(bMerchantURL);
				byte[] hash = sha.digest(bPassword);

				int h = 0;
				String s = new String();

				final int SHA1_DIGEST_LENGTH = 20;

				for(int i = 0; i < SHA1_DIGEST_LENGTH; i++) {         
					h = (int) hash[i];          // Convertir de byte a int
					if(h < 0) h += 256;  // Si son valores negativos, pueden haber problemas de conversi¢n.
					s = Integer.toHexString(h); // Devuelve el valor hexadecimal como un String        
					if (s.length() < 2) Merchant_Signature = Merchant_Signature.concat("0"); // A¤ade un 0 si es necesario
					Merchant_Signature = Merchant_Signature.concat(s); // A¤ade la conversi¢n a la cadena ya existente
				}
				Merchant_Signature = Merchant_Signature.toUpperCase(); // Convierte la cadena generada a Mayusculas.
				System.out.println("Merchant_Signature: " + Merchant_Signature);

				html.append("<form  name='compra' action='" + "https://sis-t.sermepa.es:25443/sis/realizarPago" + "' method='post'>" +
						"<input type=hidden name=Ds_Merchant_MerchantName value='" + Merchant_Name + "' />" +
						"<input type=hidden name=Ds_Merchant_MerchantCode value='" + Merchant_Code + "' />" +
						"<input type=hidden name=Ds_Merchant_ConsumerLanguage value='001' />" +
						"<input type=hidden name=Ds_Merchant_Terminal value='" + Merchant_Terminal + "' />" +
						"<input type=hidden name=Ds_Merchant_Order value='" + Merchant_Order + "' />" +
						"<input type=hidden name=Ds_Merchant_Amount value='" + Merchant_Amount + "' />" +
						"<input type=hidden name=Ds_Merchant_Currency value='" + Merchant_Currency + "' />" +
						"<input type=hidden name=Ds_Merchant_TransactionType value='" + Merchant_TransactionType + "'/>" +
						"<input type=hidden name=Ds_Merchant_MerchantURL value='" + Merchant_MerchantURL + "' />" +
						"<input type=hidden name=Ds_Merchant_MerchantSignature value='" + Merchant_Signature + "' />" +								
						"</form><script>document.compra.submit();</script>");
				
			}
		});
		
		return html.toString();
	}

	@Override
	public List<String> getConfiguracionesCamara() throws Exception {
		List<String> r = new ArrayList<String>();
		File d = new File(System.getProperty("pathindigo", "/home/web/IndigoRenderer_x64_v3.4.18") + "/data/camera_response_functions");
		for (File f : d.listFiles()) {
			if (f.getName().endsWith(".txt") && !f.getName().startsWith("source")) r.add(f.getName().substring(0, f.getName().indexOf(".txt")));
		}
		return r;
	}

	@Override
	public Map<String, Map<String, String>> getInfoPresupuesto(final Plano p)
			throws Exception {
		
		final Map<String, Map<String, String>> r = new HashMap<String, Map<String,String>>();
		
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {

				for (final Planta h : p.getPlantas()) {
					for (final Objeto o : h.getContenido()) {
						if (o instanceof Modelo) {
							Modelo m = (Modelo) o;
							HashMap<String, String> d;
							r.put(((Modelo) o).getId(), d = new HashMap<String, String>());
							
							Articulo a = em.find(Articulo.class, Long.parseLong(m.getId()));
							
							if (a != null) {
								d.put("nombre", a.getNombre());
								d.put("pvp", "" + a.getPvp());
								d.put("pvp", "" + 0);								
							}
							
						}
					}
				}

			}
		});
		
		
		return r;
	}

	@Override
	public String getPDFPresupuesto(String svg, Plano p) throws Exception {
		svg = new SVGWriter().toSVG(p, 800, 600);
		System.out.println("getPDFPresupuesto(" + svg + ")");
		return PDFHelper.crearPdfPresupuesto(svg, p);
	}


	@Override
	public boolean hacerFavorito(final String tipo, final String email, final String idArticulo) throws Exception {
		final boolean[] r = {false};
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Usuario u = em.find(Usuario.class, email);
				Articulo a = em.find(Articulo.class, Long.parseLong(idArticulo));
				if (!u.getArticulosFavoritos().contains(a)) {
					u.getArticulosFavoritos().add(a);
					r[0] = true;
				}
				else {
					while (u.getArticulosFavoritos().contains(a)) u.getArticulosFavoritos().remove(a);
					r[0] = false;
				}
			}
		});
		
		return r[0];
	}


	@Override
	public void panorama(final String email, final Plano plano, final int segundosRender) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				
				Usuario u = em.find(Usuario.class, email);
				
				if (u.getCreditosRestantes() >= 1) {
					Render r = new Render();
					r.setId(Helper.getUUID());
					r.setCreado(new Date());	
					r.setTipo(r.TIPO_PANORAMA360);
					r.setSegundosRender(segundosRender);
					long segundosCola = 0;
					try {
						segundosCola = Long.parseLong((String) Helper.sqlString("sql: select sum(ren_segundosrender) from fsk_render where ren_estado in ('" + Render.ESTADO_ENCOLA + "', '" + Render.ESTADO_RENDERIZANDO + "')")[0][0]);
					} catch (Exception e) {
						System.out.println("error al recuperar los segundos de render de la cola --> " + e.getClass().getName() + ":" + e.getMessage());
					}
					
					r.setFinRenderPrevisto(new Date(new Date().getTime() + (r.getSegundosRender() + segundosCola) * 1000l));
					r.setEstado(r.ESTADO_ENCOLA);
					r.setPlano(Helper.serializar(plano));
					r.setUsuario(em.find(Usuario.class, email));
					r.getUsuario().getRenders().add(r);					
					em.persist(r);
				} else {
					throw new Exception("No quedan créditos");
				}
				
			}
		});
	}

	@Override
	public InfoArticulo getInfoArticulo(final String idArticulo) throws Exception {
		final InfoArticulo i = new InfoArticulo();
		System.out.println("getInfoArticulo(" + idArticulo + ") ...");
		
		try {
			
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Articulo a = em.find(Articulo.class, Long.parseLong(idArticulo));
					i.setId(idArticulo);
					if (a.getImagenParaPlano() != null) i.setCenital("" + a.getImagenParaPlano().toString());
					i.setDescripcion(a.getDescripcion());
					i.setNombre(a.getNombre());
					i.setPvp(a.getPvp());
					i.setEstado(a.getEstado());
					if (a.getImagenVistaPrevia60x60() != null) i.setVistaPrevia("" + a.getImagenVistaPrevia60x60().toString());
					
					for (SobreescrituraTextura ma : a.getSobreescriturasTexturas()) if (ma.isPosible()) {
						Textura t;
						i.getTexturas().add(t = new Textura());
						if (ma.getTextura() != null) {
							if (ma.getArticulo().getImagenParaPlano() != null) t.setCenital("" + ma.getArticulo().getImagenParaPlano().toString());
							else t.setCenital("http://www.fescateva.com/img/bg-blanco-70.png");
							t.setIdMaterial("" + ma.getTextura().getId());
						} else {
							t.setCenital("http://www.fescateva.com/img/bg-blanco-70.png");
							t.setIdMaterial("1");
						}
						if (ma.getImagen() != null) {
							t.setImagen("" + ma.getImagen().toString());
						} else {
							t.setImagen("http://www.fescateva.com/img/bg-blanco-70.png");
						}
						t.setNombre(ma.getNombre());
					}
					
					System.out.println("getInfoArticulo(" + idArticulo + ") ok");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return i;
	}

	@Override
	public void logError(String s) throws Exception {
		String texto = "";
		StringWriter sw;
		texto += s;
		Helper.enviarEmail("mperez@viajesurbis.com", "ERROR EN FESKTEVA", texto);
	}
	
	public Throwable unwrap(Throwable t) {
		if (t instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) t;
			if (ue.getCauses().size() == 1) {
				return unwrap(ue.getCauses().iterator().next());
			}
		}
		return t;
	}

	@Override
	public void encuesta(String nombre, String email, int valoracion,
			String comentarios) throws Exception {
		String txt = "ENCUESTA:";
		txt += "\r\n<br/>Nombre: " + nombre;
		txt += "\r\n<br/>Email: " + email;
		txt += "\r\n<br/>Valoración: " + valoracion;
		txt += "\r\n<br/>Comentarios: " + comentarios;
		
		for (String a : new String[] {"cas.ferrer@yahoo.es", "carlosmulet@gmail.com", "mperez@viajesurbis.com"}) {
			try {
				Helper.enviarEmail(a, "ENCUESTA FESKTEVA RELLENADA", txt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public InfoArticulo rotarZ90(final String idArticulo) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Articulo a = em.find(Articulo.class, Long.parseLong(idArticulo));
				a.rotarObjZ90(em);
				a.centrarObj();
				a.actualizarDimensiones();
				a.actualizarContenidos(em);
				a.crearVistaPrevia(em);
				a.actualizarContenidos(em);
				a.ajustarVistaPrevia(em);
			}
		});
		return getInfoArticulo(idArticulo);
	}

	@Override
	public InfoArticulo rotarX90(final String idArticulo) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Articulo a = em.find(Articulo.class, Long.parseLong(idArticulo));
				a.rotarObjX90(em);
				a.centrarObj();
				a.actualizarDimensiones();
				a.actualizarContenidos(em);
				a.crearVistaPrevia(em);
				a.actualizarContenidos(em);
				a.ajustarVistaPrevia(em);
			}
		});
		return getInfoArticulo(idArticulo);
	}

	@Override
	public InfoArticulo modificarDatos(final String idArticulo, final String nombre, final String descripcion, final String pvp, final String estado) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Articulo a = em.find(Articulo.class, Long.parseLong(idArticulo));
				a.setNombre(nombre);
				a.setDescripcion(descripcion);
				a.setPvp(Double.parseDouble(pvp));
				a.setEstado(estado);
			}
		});
		return getInfoArticulo(idArticulo);
	}

	@Override
	public Map<String, String> comprobarSesion() throws Exception {
		HttpServletRequest request = getThreadLocalRequest();
		String email = (String) request.getSession().getAttribute("usuario");
		if (!Helper.vacio(email)) {
			return getDatosUsuario(email);
		} else {
			return null;
		}		
	}

	@Override
	public void logout() throws Exception {
		HttpServletRequest request = getThreadLocalRequest();
		request.getSession().removeAttribute("usuario");
	}

	@Override
	public void exportarABlend(final String email, final Plano plano) throws Exception {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				long t0 = new Date().getTime();

				String pathtmp = System.getProperty("pathtmp", "/home/miguel/tmp");
				if (!pathtmp.endsWith("/")) pathtmp += "/";

				String fn = new BlenderWriter().exportarABlend(plano, pathtmp, 0, false, 800, 600);
				
				System.out.println("Blend generado en " + (new Date().getTime() - t0) + "ms.");
				
				//Helper.enviarEmail(getUsuario().getEmail(), "Su foto 360", new String(Helper.leerFichero(this.getClass().getResourceAsStream("/fsk/server/aux/panorama.html"))).replaceAll("udhedhweibhdwiebdiwedwediuhediuwed", "http://www.fescateva.com/" + f.toString()));
				try {
					Helper.enviarEmail(email, "Su escena exportada", "<html><body><p>Aquí lo tienes:</p> <p><a href='/tmp" + fn.substring(fn.lastIndexOf("/")) + "'>" + fn.substring(fn.lastIndexOf("/") + 1) + "</a></p><p>Gracias por utilizar fescateva!</p><p>Exportado en " + (new Date().getTime() - t0) + "ms.</p></body></html>");
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
		
	}

	@Override
	public TexturasPlano getTexturas(final Plano p) throws Exception {
		final TexturasPlano ts = new TexturasPlano();
		
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {

				for (Planta px : p.getPlantas()) {
					GrupoTexturas g;
					ts.getGrupos().add(g = new GrupoTexturas());
					g.setId("planta" + p.getPlantas().indexOf(px));
					g.setNombre("Planta " + p.getPlantas().indexOf(px));
					
					GrupoTexturas s;
					g.getSubgrupos().add(s = new GrupoTexturas());
					s.setId("estructura");
					s.setNombre("Estructura");
					
					TexturaPlano t;
					s.getTexturas().add(t = new TexturaPlano());
					t.setId("suelo");
					t.setNombre("Suelo");
					t.setIdArticulo(px.getHabitaciones().get(0).getRevestimientoSuelo().getId());
					t.setUrlImagen(em.find(Articulo.class, Long.parseLong(px.getHabitaciones().get(0).getRevestimientoSuelo().getId())).getImagenParaPlano().getNombre());

					g.getSubgrupos().add(s = new GrupoTexturas());
					s.setId("contenido");
					s.setNombre("Contenido");

					for (Objeto o : px.getContenido()) {
						if (o instanceof Modelo) {
							Modelo m = (Modelo) o;
							for (Textura tx : m.getInfo().getTexturas()) {
								Articulo a = em.find(Articulo.class, Long.parseLong(tx.getIdMaterial()));
								s.getTexturas().add(t = new TexturaPlano());
								t.setId("contenido" + px.getContenido().indexOf(o) + "_" + m.getInfo().getTexturas().indexOf(tx));
								t.setNombre(a.getNombre());
								t.setIdArticulo(tx.getIdMaterial());
								t.setUrlImagen(a.getImagenVistaPrevia().getNombre());
							}
						}
					}
					
				}
			
			}
		});
		
 		
		return ts;
	}
	
}
