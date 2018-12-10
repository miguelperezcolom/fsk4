package fsk4.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import org.vectomatic.dom.svg.utils.DOMHelper;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fsk4.client.svg.SVGHelper;
import fsk4.client.svg.SVGPared;
import fsk4.shared.InfoArticulo;
import fsk4.shared.Modelo;
import fsk4.shared.Pared;
import fsk4.shared.PerspectivaCenital;
import fsk4.shared.PerspectivaFrente;
import fsk4.shared.Vertice;

public class ClickHelper {
	
	public static void addClicks() {
		
		

		$("#botoncamara").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botoncamara").toggleClass("active");
				DibujoHelper.getPlano().getCamara().setVisible($("#botoncamara").hasClass("active"));
				if (DibujoHelper.getPlano().getCamara().isVisible()) DibujoHelper.refrescaPreview(true);
				DibujoHelper.actualizarCamara();  				
				return true;
			}
		});

		$("#botoncandado").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botoncandado").toggleClass("active");
				DibujoHelper.getPlano().setParedesBloqueadas($("#botoncandado").hasClass("active"));
				return true;
			}
		});

		$("#botoniman").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botoniman").toggleClass("active");
				DibujoHelper.getPlano().setIman($("#botoniman").hasClass("active"));
				return true;
			}
		});

		$("#botonvolumen").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botonvolumen").toggleClass("active");
				if ($("#botonvolumen").hasClass("active")) {
					DibujoHelper.limpiarSeleccion();
					SVGHelper.inicioCrearVolumen();
				} else {
					SVGHelper.finCrearVolumen();
				}
				return true;
			}
		});

		$("#botonparedes").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botonparedes").toggleClass("active");
				if ($("#botonparedes").hasClass("active")) {
					SVGHelper.marcarSeleccion();
					SVGHelper.inicioCrearParedes();
				} else {
					SVGHelper.finCrearParedes();
				}
				return true;
			}
		});
		
		$("#botonescalera").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botonescalera").toggleClass("active");
				if ($("#botonescalera").hasClass("active")) {
					DibujoHelper.limpiarSeleccion();
					DibujoHelper.inicioCrearEscalera();
				} else {
					DibujoHelper.finCrearEscalera();
				}
				return true;
			}
		});


		$("#botonsol").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botonsol").toggleClass("active");
				DibujoHelper.getPlano().setLuzSolar($("#botonsol").hasClass("active"));
				DibujoHelper.getPlano().getSol().setActivo($("#botonsol").hasClass("active"));
				if (DibujoHelper.getPlano().getCamara().isVisible()) DibujoHelper.refrescaPreview(true);
				DibujoHelper.actualizarSol();  				
				return true;
			}
		});

		$("#botonflash").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botonflash").toggleClass("active");
				DibujoHelper.getPlano().setLuzCamara($("#botonflash").hasClass("active"));
				if (DibujoHelper.getPlano().isLuzCamara()) DibujoHelper.refrescaPreview(true);
				return true;
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		$("#botoncenital").click(new Function() {
			@Override
			public boolean f(Event e) {
				$(".btnperspectiva").removeClass("active");
				$("#botoncenital").toggleClass("active");
				
				DibujoHelper.getPlano().setPerspectiva(new PerspectivaCenital());
				DibujoHelper.resetPlano(DibujoHelper.getPlano());

				return true;
			}
		});

		$("#botonarriba").click(new Function() {
			@Override
			public boolean f(Event e) {
				$(".btnperspectiva").removeClass("active");
				$("#botonarriba").toggleClass("active");

				DibujoHelper.getPlano().setPerspectiva(new PerspectivaFrente());
				DibujoHelper.resetPlano(DibujoHelper.getPlano());

				return true;
			}
		});
		
		
		
		
		

		$("#botonrotarz").click(new Function() {
			@Override
			public boolean f(Event e) {
				PantallaHelper.espera("Actualizando vista previa...(tarda aprox. 10 segundos)", 10);
				
				Fsk4.getServicio().rotarZ90(((Modelo) DibujoHelper.getSeleccionado()).getId(), new AsyncCallback<InfoArticulo>() {
					
					@Override
					public void onSuccess(InfoArticulo result) {
						PantallaHelper.finEspera();
						Fsk4.success("Ok. Datos grabados.");
						((Modelo) DibujoHelper.getSeleccionado()).setInfo(result);
						DibujoHelper.i.updateInfo();
						DibujoHelper.refrescaPreview(true);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						PantallaHelper.finEspera();
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}
				});
				return true;
			}
		});
		
		$("#botonrotarx").click(new Function() {
			@Override
			public boolean f(Event e) {
				PantallaHelper.espera("Actualizando vista previa...(tarda aprox. 10 segundos)", 10);
				
				Fsk4.getServicio().rotarX90(((Modelo) DibujoHelper.getSeleccionado()).getId(), new AsyncCallback<InfoArticulo>() {
					
					@Override
					public void onSuccess(InfoArticulo result) {
						PantallaHelper.finEspera();
						Fsk4.success("Ok. Datos grabados.");
						((Modelo) DibujoHelper.getSeleccionado()).setInfo(result);
						DibujoHelper.i.updateInfo();
						DibujoHelper.refrescaPreview(true);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						PantallaHelper.finEspera();
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}
				});
				return true;
			}
		});
		
		

		$("#botoneditar").click(new Function() {
			@Override
			public boolean f(Event e) {
				PantallaHelper.abrirDialogo("divmodificardatosarticulo", 400, 600, new Runnable() {
					
					@Override
					public void run() {
						Modelo m = (Modelo) DibujoHelper.getSeleccionado();
						$("#mod_nombre").val(m.getInfo().getNombre());
						$("#mod_descripcion").val(m.getInfo().getDescripcion());
						$("#mod_pvp").val("" + m.getInfo().getPvp());
						if ("ok".equalsIgnoreCase(m.getInfo().getEstado())) {
							$("#mod_estadook").attr("checked", true);
						} else if ("aparcado".equalsIgnoreCase(m.getInfo().getEstado())) {
							$("#mod_estadoaparcado").attr("checked", true);
						} else if ("rechazado".equalsIgnoreCase(m.getInfo().getEstado())) {
							$("#mod_estadorechazado").attr("checked", true);
						} else {
							$("#mod_estadopendiente").attr("checked", true);
						}
						
						$("#mod_submit").click(new Function() {
							@Override
							public void f() {
								PantallaHelper.espera("Actualizando datos", 3);
								String estado = "PENDIENTE";
								if ($("#mod_estadook").is(":checked")) estado = "OK";
								else if ($("#mod_estadoaparcado").is(":checked")) estado = "APARCADO";
								else if ($("#mod_estadorechazado").is(":checked")) estado = "RECHAZADO";
								Fsk4.getServicio().modificarDatos(((Modelo) DibujoHelper.getSeleccionado()).getId(), $("#mod_nombre").val(), $("#mod_descripcion").val(), $("#mod_pvp").val(), estado, new AsyncCallback<InfoArticulo>() {
									
									@Override
									public void onSuccess(InfoArticulo result) {
										PantallaHelper.finEspera();
										Fsk4.success("Ok. Datos grabados.");
										((Modelo) DibujoHelper.getSeleccionado()).setInfo(result);
										DibujoHelper.i.updateInfo();
										PantallaHelper.cerrarDialogo();
										DibujoHelper.refrescaPreview(true);
									}
									
									@Override
									public void onFailure(Throwable caught) {
										PantallaHelper.finEspera();
										Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
									}
								});
							}
						});
						
					}
				});
				return true;
			}
		});


		/*
		$("#botonexterior").click(new Function() {
			@Override
			public boolean f(Event e) {
				$("#botonexterior").toggleClass("active");
				DibujoHelper.getPlano().setPintarTecho($("#botonexterior").hasClass("active"));
				return true;
			}
		});
		*/
		
		/*
		$("#botonpreview").click(new Function() {
			@Override
			public boolean f(Event e) {
				DibujoHelper.refrescaPreview(true);
				return true;
			}
		});
		*/

		$("#botonpreviewhd").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.foto();
				return true;
			}
		});

		$("#mnuevo").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.nuevo(true);
				cerrarMenu();
				return true;
			}
		});

		$("#mabrir").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.abrir();
				cerrarMenu();
				return true;
			}
		});
		
		$("#mguardar").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.guardar();
				cerrarMenu();
				return true;
			}
		});

		$("#mguardarcomo").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.guardarComo();
				cerrarMenu();
				return true;
			}
		});
		
		$("#mfoto").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.foto();
				cerrarMenu();
				return true;
			}
		});

		$("#m360").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.panorama();
				cerrarMenu();
				return true;
			}
		});

		$("#mblend").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.exportarABlend();
				cerrarMenu();
				return true;
			}
		});

		$("#mcompartir").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.compartir();
				cerrarMenu();
				return true;
			}
		});
		
		$("#msugerencias").click(new Function() {
			@Override
			public boolean f(Event e) {
				Window.open("publico/ayuda.pdf", "Sugerencias", "");
				cerrarMenu();
				return true;
			}
		});

		
		$("#mcola").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.cola();
				cerrarMenu();
				return true;
			}
		});
		
		$("#mversion").click(new Function() {
			@Override
			public boolean f(Event e) {

				String ua = Window.Navigator.getUserAgent();
				boolean soportaSVG = DOMHelper.hasFeature( SVGConstants.SVG_FEATURE_SHAPE);
				
				Window.alert("useragent=" + ua + ", soportasvg=" + soportaSVG);
				
				cerrarMenu();
				return true;
			}
		});

		/*
		$("#bfoto").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.foto();
				return true;
			}
		});
		*/

		/*
		$("#b360").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.panorama();
				return true;
			}
		});
		*/
		
		$("#bprint3d").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.imprimir3d();
				cerrarMenu();
				return true;
			}
		});
		

		$("#mlogin").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.login();
				return true;
			}
		});

		$("#mregistro").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.registro();
				return true;
			}
		});

		$("#mperfil").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.perfil();
				return true;
			}
		});

		$("#mlogout").click(new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.logout();
				return true;
			}
		});
		
		$("#boton-presupuestopdf").click(new Function() {
			@Override
			public boolean f(Event e) {
				Fsk4.getServicio().getPDFPresupuesto($("#canvas").html(), DibujoHelper.getPlano(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						Window.open(result, "Presupuesto", "");						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}
				});
				return true;
			}
		});

		habilitarPestanyas();
	
		habilitarToolbar();
		
		
		habilitarBotonesInfo();
		
		
		$(".modal-cerrar").bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				$("#modal-background").hide();
				$("#modal-contenedor").hide();
				return true;
			}
		});
		
		$(".seleccionarticulo-cerrar").bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				$("#seleccionarticulo-background").hide();
				$("#seleccionarticulo-contenedor").hide();
				return true;
			}
		});

		$("#plano_tonemapping").change(new Function() {
			@Override
			public void f(Element e) {
				$(".info-tonemapping").hide();
				$(".info-tonemapping." + $(e).val()).show();
			}
		});
		
		$("#habitacion_tienecenefa").change(new Function() {
			@Override
			public void f(Element e) {
				if ("on".equals($(e).val())) $(".cenefa").show();
				else $(".cenefa").hide();
			}
		});
		
	}

	private static void habilitarBotonesInfo() {
		$("#botondividirpared").click(new Function() {
			@Override
			public boolean f(Event e) {
				
				if (DibujoHelper.seleccionado != null && DibujoHelper.seleccionado instanceof Pared) {
					
					Pared p = (Pared)DibujoHelper.seleccionado;
					
					((SVGPared)SVGHelper.svgMarco.svgPlano.payload.get(DibujoHelper.seleccionado)).dividirPared(p, new Vertice((p.getDe().getX() + p.getA().getX()) / 2f, (p.getDe().getY() + p.getA().getY()) / 2f));
					
					DibujoHelper.refrescaPreview();
				}
				
				return true;
			}
		});
	}

	protected static void cerrarMenu() {
		//$(".dropdown .dropdown-menu")
	}

	private static void habilitarToolbar() {

		
		$(".toolbar-boton.presupuesto").bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				AccionHelper.actualizarCarrito();
				return true;
			}
		});
		
		
	}

	private static void habilitarPestanyas() {
		
		$(".preview-boton").click(new Function() {
			@Override
			public boolean f(Event e) {
				DibujoHelper.getPlano().getCamara().setVisible(true);
				DibujoHelper.actualizarCamara();  				
				
				return true;
			}
		});

		$("#preview-boton-ocultar").click(new Function() {
			@Override
			public boolean f(Event e) {
				DibujoHelper.getPlano().getCamara().setVisible(false);
				DibujoHelper.actualizarCamara();  				
					
				return true;
			}
		});
		

		$(".toolbar-boton").click(new Function() {
			@Override
			public void f(Element e) {
				
				$(".contenido-toolbar").hide();
				$(".toolbar-boton").removeClass("seleccionado");
				
				$(e).addClass("seleccionado");

				$("#" + $(e).attr("data-toolbar")).show();
				
				$(".toolbar").animate("left: 0px;", 300, Easing.LINEAR, new Function() {
					@Override
					public void f() {						
						$("#tollbar-boton-ocultar").show();
					}
				});
			}
		});

		$("#tollbar-boton-ocultar").click(new Function() {
			@Override
			public boolean f(Event e) {
				
				$(".toolbar").animate("left: -280px;", 300, Easing.LINEAR, new Function() {
					@Override
					public void f() {
						$("#tollbar-boton-ocultar").hide();
					}
				});
				
				return true;
			}
		});	
		
	}

	public static void addKeys() {
		$("html").keyup(new Function() {
			@Override
			public boolean f(Event e) {
				
				boolean nuestro = false;
				if (e.getKeyCode() == KeyCodes.KEY_DELETE || e.getKeyCode() == KeyCodes.KEY_BACKSPACE) {
					//nuestro = true;
					//DibujoHelper.clickEnEliminar();
				} else if (e.getKeyCode() == KeyCodes.KEY_UP) {
					nuestro = true;
				} else if (e.getKeyCode() == KeyCodes.KEY_DOWN) {
					nuestro = true;
				} else if (e.getKeyCode() == KeyCodes.KEY_LEFT) {
					nuestro = true;
				} else if (e.getKeyCode() == KeyCodes.KEY_RIGHT) {
					nuestro = true;
				} else if (e.getKeyCode() == KeyCodes.KEY_ESCAPE) {
					DibujoHelper.reset();
					nuestro = true;
				}
				
				if (nuestro) {
					e.stopPropagation();
					e.preventDefault();
				}
				
				return true;
			}
		});
	}

}
