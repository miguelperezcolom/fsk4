package fsk4.client;

import static com.google.gwt.query.client.GQuery.$;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vectomatic.dom.svg.utils.DOMHelper;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fsk4.client.svg.SVGHelper;
import fsk4.shared.Plano;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Fsk4 implements EntryPoint {
	private static final Fsk4ServiceAsync fsk4Service = GWT.create(Fsk4Service.class);
	
	public static Fsk4ServiceAsync getServicio() {
		return fsk4Service;
	}
	
	private static native void firebugWarn(String message) /*-{
	 console.warn(message);
	}-*/;
	
	public static void log(String msg) {
		firebugWarn(msg);
	}
	
	// for lineEnding, use "<br>" for HTML, "\n" for text
	public static final String getStackTrace(Throwable t, String lineEnding) {
	    Object[] stackTrace = t.getStackTrace();
	    if (stackTrace != null) {
	        StringBuilder output = new StringBuilder();
	        for (Object line : stackTrace) {
	            output.append(line);
	            output.append(lineEnding);
	        }
	        return output.toString();
	    } else {
	        return "[stack unavailable]";
	    }
	}

	private static boolean tableta;
	private static int contbotonlose;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void onUncaughtException(Throwable e) {
				alert("" + e.getClass().getName() + ":" + e.getMessage());
				e.printStackTrace();
				Fsk4.getServicio().logError(getStackTrace(e, "\n"), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Fsk4.alert("Error reportado al equipo informático de fesKteva.");
					}
				});
			}
		});
		
		String ua = Window.Navigator.getUserAgent();
		String uaver = Window.Navigator.getAppVersion();
		
		setTableta(ua.toLowerCase().contains("android") || ua.toLowerCase().contains("ipad"));
		
		boolean soportaSVG = DOMHelper.hasFeature( SVGConstants.SVG_FEATURE_SHAPE);
		
		boolean navegadorOk = true;
				
		if (ua.contains("firefox") || ua.contains("chrome") || ua.contains("safari")) {
			navegadorOk = true;
		} else if (ua.toLowerCase().contains("msie")) {
			navegadorOk = (uaver.toUpperCase().contains("MSIE 9") || uaver.toUpperCase().contains("MSIE 1"));
		} else if (soportaSVG) {			
			navegadorOk = true;
		} else {
			navegadorOk = false;
		}
		
		if (navegadorOk) {
			PantallaHelper.abrirDialogo("divsplash", 400, 300, new Runnable() {
				
				@Override
				public void run() {
					$("#botonentrar").click(new Function() {
						@Override
						public void f() {
							entrar();
						}
					});
				}
			}, false);
			
		} else {

			int maskHeight = Window.getClientHeight();  
			int maskWidth = Window.getClientWidth();
			// calculate the values for center alignment
			int alto = $("#divnavegadornosoportado").height();
			int ancho = $("#divnavegadornosoportado").width();

			int dialogTop =  (maskHeight - alto) / 2;  
			int dialogLeft = (maskWidth - ancho) /2; 
			
			// assign values to the overlay and dialog box
			$("#modal-background").height(maskHeight).width(maskWidth).show();
			$("#divnavegadornosoportado").css("height", "" + alto + "px!important").css("width", "" + ancho + "px!important").css("top", "" + dialogTop).css("left", "" + dialogLeft).show();
			
		}
		
	}

	protected void entrar() {
		PantallaHelper.abrirDialogo("divterminosycondiciones", 400, 300, new Runnable() {
			
			@Override
			public void run() {
				$("#leidoyacepto").click(new Function() {
					@Override
					public void f() {
						PantallaHelper.cerrarDialogo();
						entrarLeidoYAceptado();
					}
				});
			}
		}, false);
	}
	
	protected void entrarLeidoYAceptado() {
		Fsk4.getServicio().comprobarSesion(new AsyncCallback<Map<String,String>>() {

			@Override
			public void onFailure(Throwable caught) {				
				entrarSesionComprobada();
				Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				if (result != null) {
					Datos.get().setEmail(result.get("email").toLowerCase().trim());
					
					$("#seccion_invitado").hide();
					$("#seccion_usuario").show();
				}

				entrarSesionComprobada();
			}
		});
	}	
	
	protected void entrarSesionComprobada() {
		// esto realmente hace falta??????????????????????
		/*
		$("a").click(new Function() {
			public boolean f(Event e) {
				e.preventDefault();
				return true;
			};
		});
		*/

		$(GQuery.document).mouseup(new Function() {
			@Override
			public boolean f(Event e) {
				SVGHelper.mu(e.getClientX(), e.getClientY());
				return true;
			}
		});
		
		$(GQuery.document).bind("touchend", new Function() {
			@Override
			public boolean f(Event e) {
				info("document.touchend " + e.getClientX() + "," + e.getClientY());
				SVGHelper.mu(e.getClientX(), e.getClientY());
				return true;
			}
		});

		ClickHelper.addClicks();
		
		ClickHelper.addKeys();

		PantallaHelper.dinamizar();
	
		// carga inicial del plano
		if (Window.Location.getParameter("id") != null) {
			Fsk4.getServicio().getPlano(Window.Location.getParameter("id"), new AsyncCallback<Plano>() {

				@Override
				public void onFailure(Throwable caught) {
					Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
				}

				@Override
				public void onSuccess(Plano result) {
					DibujoHelper.resetPlano(result);
				}
			});
		} else if (Window.Location.getParameter("idenvio") != null) {
			String idEnvio = Window.Location.getParameter("idenvio");
			getServicio().getPlanoEnvio(idEnvio, new AsyncCallback<Plano>() {

				@Override
				public void onFailure(Throwable caught) {
					Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
				}

				@Override
				public void onSuccess(Plano result) {
					DibujoHelper.resetPlano(result);
				}
			});
		} else if (Window.Location.getParameter("idficherorender") != null) {
			String idFicheroRender = Window.Location.getParameter("idficherorender");
			getServicio().getPlanoRender(idFicheroRender, new AsyncCallback<Plano>() {

				@Override
				public void onFailure(Throwable caught) {
					Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
				}

				@Override
				public void onSuccess(Plano result) {
					DibujoHelper.resetPlano(result);
				}
			});
		} else {
			AccionHelper.nuevo(false);
			/*
			DibujoHelper.getPlano().setX(Window.getClientWidth() / 2);
			DibujoHelper.getPlano().setY(Window.getClientHeight() / 2);
			DibujoHelper.resetPlano(DibujoHelper.getPlano());
			*/
		}

		
		//$(".toolbar-boton.estructura").click();
		//$(".preview-boton.mostrar").click();
		
		$("#botontuopinion").click(new Function() {
			@Override
			public void f() {
				AccionHelper.encuesta();
			}
		});
		$("#botontuopinion").show();
		
	}

	protected static void alert(String msg) {
		alert(msg, true);
	}
	
	public static void alert(String msg, boolean autoDesaparecer) {
		alert("alert-error", msg, autoDesaparecer);
	}
	
	protected static void success(String msg) {
		success(msg, true);
	}
	
	public static void success(String msg, boolean autoDesaparecer) {
		alert("alert-success", msg, autoDesaparecer);
	}

	protected static void info(String msg) {
		info(msg, true);
	}
	
	public static void info(String msg, boolean autoDesaparecer) {
		alert("alert-info", msg, autoDesaparecer);
	}

	public static void alert(String tipo, String msg, boolean autoDesaparecer) {
		final int idbotonclose = contbotonlose++;
		String html = "<div class='alert " + tipo + " fade in' id='msg' style='position: absolute; left: 400px; top: 60px; right: 400px; '><button type='button' class='close' data-dismiss='alert' id='msg-close" + idbotonclose + "'>&times;</button>" + msg + "</div>";
		$("body").append(html);
		if (autoDesaparecer) {
			Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
				
				@Override
				public boolean execute() {
					$("#msg-close" + idbotonclose).click();
					return false;
				}
			}, 2000);
			
		}
	}

	public static boolean isTableta() {
		return tableta;
	}

	public static void setTableta(boolean tableta) {
		Fsk4.tableta = tableta;
	}

}
