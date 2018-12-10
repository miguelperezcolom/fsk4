package fsk4.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

import fsk4.shared.FuenteLuz;
import fsk4.shared.Planta;
import fsk4.shared.Modelo;
import fsk4.shared.Objeto;
import fsk4.shared.Plano;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;
import gwtupload.client.Uploader;

public class AccionHelper { 
	 
	private static Map<String, Modelo> contenidoPresupuesto = new HashMap<String, Modelo>();
	
	public static void nuevo(boolean pedirConfirmacion) {
		if (!pedirConfirmacion || Window.confirm("Vas a perder el plano actual. Estás seguro?")) {
			
			PantallaHelper.abrirDialogo("divnuevodisenyo", 570, 292, new Runnable() {
				public void run() {
					$("#babrir").click(new Function() {
						@Override
						public void f(Element e) {
							PantallaHelper.cerrarDialogo();
							AccionHelper.abrir();
						}
					});
					$(".boton-seleccionpatron").click(new Function() {
						@Override
						public void f(Element e) {
							PantallaHelper.cerrarDialogo();
							DibujoHelper.resetPlano(Integer.parseInt(e.getAttribute("data-id")));
						}
					});
				}
			}, false);
			
		}	
	}
		
	public static void abrir() {
		autenticar(new Runnable() {
			public void run() {
				if (Window.confirm("Vas a perder el plano actual. Estás seguro?")) {

					PantallaHelper.abrirDialogo("divabrir", 500, 300, new Runnable() {
						public void run() {
							$("abrir-resultado").html("Buscando...");
							
							Fsk4.getServicio().sql("sql: select dis_id, dis_nombre, to_char(dis_creado, 'dd/MM/yyyy HH24:MI'), to_char(dis_modificado, 'dd/MM/yyyy HH24:MI') from fsk_diseno where dis_usuario = '" + Datos.get().getEmail().replaceAll("'", "''") + "' order by dis_modificado desc", new AsyncCallback<String[][]>() {
								
								@Override
								public void onSuccess(String[][] result) {
									
									if (result == null || result.length == 0) $("#abrir-resultado").html("No hay diseños.");
									else {
										StringBuffer h = new StringBuffer("<table>");
										h.append("<tr>");
										h.append("<td style='bottom-border: 1px solid #000000;'><b>Nombre</b></td>");
										h.append("<td style='bottom-border: 1px solid #000000;'><b>Creado</b></td>");
										h.append("<td style='bottom-border: 1px solid #000000;'><b>Modificado</b></td>");
										h.append("<td style='bottom-border: 1px solid #000000;'>&nbsp;</td>");
										h.append("</tr>");
										
										for (String[] l : result) {
											h.append("<tr>");
											h.append("<td style='bottom-border: 1px solid #000000;'>" + l[1] + "</td>");
											h.append("<td style='bottom-border: 1px solid #000000;'>" + l[2] + "</td>");
											h.append("<td style='bottom-border: 1px solid #000000;'>" + l[3] + "</td>");
											h.append("<td style='bottom-border: 1px solid #000000;'><input type='button' class='boton-abrirdisenyo' data-id='" + l[0] + "' value='Abrir'></td>");
											h.append("</tr>");
										}
										h.append("</table>");
										
										$("#abrir-resultado").html(h.toString());
										
										$(".boton-abrirdisenyo").click(new Function() {
											public void f(com.google.gwt.user.client.Element e) {
												PantallaHelper.cerrarDialogo();
												Fsk4.getServicio().getPlano(e.getAttribute("data-id"), new AsyncCallback<Plano>() {

													@Override
													public void onFailure(Throwable caught) {
														Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
													}

													@Override
													public void onSuccess(Plano result) {
														DibujoHelper.resetPlano(result);
													}
												});
											}
										});

									}

								}
								
								@Override
								public void onFailure(Throwable caught) {
									Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
								}
							});
							
						}
					});
					
				}
			}
		});		
	}

	public static void autenticar(final Runnable accion) {
		if (Datos.get().getEmail() == null) {
			//new DialogoLogin(accion).center();
			PantallaHelper.abrirDialogo("divlogin", 700, 300, new Runnable() {
				
				@Override
				public void run() {
					$("#login-botonlogin").bind(Event.ONCLICK, new Function() {
						@Override
						public boolean f(Event e) {
							
							final TextBox email = (TextBox) (($("#login-login").widget() != null)?$("#login-login").widget():TextBox.wrap($("#login-login").elements()[0]));
							PasswordTextBox password = (PasswordTextBox) (($("#login-password").widget() != null)?$("#login-password").widget():PasswordTextBox.wrap($("#login-password").elements()[0]));
							
							if (email.getValue() == null || "".equalsIgnoreCase(email.getValue().trim()) || password.getValue() == null || "".equalsIgnoreCase(password.getValue().trim())) {
								Window.alert("Debes rellenar el email y el password");
							} else Fsk4.getServicio().login(email.getValue(), password.getValue(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
								}

								@Override
								public void onSuccess(Void result) {
									Datos.get().setEmail(email.getValue().toLowerCase().trim());
									PantallaHelper.cerrarDialogo();
									
									$("#seccion_invitado").hide();
									$("#seccion_usuario").show();
									
									accion.run();
								}
							});
								

							return true;
						}
					});
						$("#login-botonregistro").bind(Event.ONCLICK, new Function() {
						@Override
						public boolean f(Event e) {
							
							registrar(accion);
							
							return true;
						}
					});
				}
			});
		}
		else accion.run();
	}
	
	protected static void registrar(final Runnable accion) {
		PantallaHelper.abrirDialogo("divregistro", 500, 350, new Runnable() {
			
			@Override
			public void run() {
				$("#registro-botonregistro").bind(Event.ONCLICK, new Function() {
					@Override
					public boolean f(Event e) {
						
						final TextBox email = TextBox.wrap($("#registro-email").elements()[0]);
						final TextBox nombre = TextBox.wrap($("#registro-nombre").elements()[0]);
						final TextBox apellidos = TextBox.wrap($("#registro-apellidos").elements()[0]);
						final TextBox nacimiento = TextBox.wrap($("#registro-nacimiento").elements()[0]);
						final TextBox cp = TextBox.wrap($("#registro-cp").elements()[0]);
						
						if (email.getValue() == null || "".equalsIgnoreCase(email.getValue().trim()) 
								|| nombre.getValue() == null || "".equalsIgnoreCase(nombre.getValue().trim())
								|| apellidos.getValue() == null || "".equalsIgnoreCase(apellidos.getValue().trim())
								|| nacimiento.getValue() == null || "".equalsIgnoreCase(nacimiento.getValue().trim())
								|| cp.getValue() == null || "".equalsIgnoreCase(cp.getValue().trim())
								) {
							Window.alert("Debes rellenar todos los campos");
						} else Fsk4.getServicio().registrar(email.getValue(), nacimiento.getValue(), nombre.getValue(), apellidos.getValue(), nacimiento.getValue(), cp.getValue(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
							}

							@Override
							public void onSuccess(Void result) {
								Datos.get().setEmail(email.getValue().toLowerCase().trim());
								PantallaHelper.cerrarDialogo();
								
								$("#seccion_invitado").hide();
								$("#seccion_usuario").show();
								
								accion.run();
							}
						});
							
						return true;
					}
					
				});
		}
		});
	}

	public static void guardar() {
		autenticar(new Runnable() {

			@Override
			public void run() {
				if (DibujoHelper.getPlano().getId() <= 0) {
					DibujoHelper.getPlano().setNombre(Window.prompt("Nombre del plano", ""));
					if (DibujoHelper.getPlano().getNombre() == null || "".equals(DibujoHelper.getPlano().getNombre().trim())) Window.alert("El nombre del plano es obligatorio"); 
				}
				if (DibujoHelper.getPlano().getId() > 0 || (DibujoHelper.getPlano().getNombre() != null && !"".equals(DibujoHelper.getPlano().getNombre().trim()))) Fsk4.getServicio().guardar(Datos.get().getEmail(), DibujoHelper.getPlano(), new AsyncCallback<Long>() {
					
					@Override
					public void onSuccess(Long result) {
						Fsk4.success("Plano guardado.");
						DibujoHelper.getPlano().setId(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}
				});			
			}
		});	}

	public static void guardarComo() {
		autenticar(new Runnable() {
			
			@Override
			public void run() {
				String n = Window.prompt("Nombre del plano", "Copia de " + DibujoHelper.getPlano().getNombre());
				if (n == null || "".equals(n.trim())) Window.alert("El nombre del plano es obligatorio"); 
				else {
					DibujoHelper.getPlano().setNombre(n);
					Fsk4.getServicio().guardarComo(Datos.get().getEmail(), DibujoHelper.getPlano(), new AsyncCallback<Long>() {

						@Override
						public void onSuccess(Long result) {
							Fsk4.success("Plano guardado.");
							DibujoHelper.getPlano().setId(result);
						}

						@Override
						public void onFailure(Throwable caught) {
							Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
						}
					});

				}
			}
		});
	}

	public static void foto() {
		autenticar(new Runnable() {
			public void run() {
				
				int segundosRender = 120*60;
				Fsk4.getServicio().foto(Datos.get().getEmail(), DibujoHelper.getPlano(), segundosRender, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Window.alert("Foto HD programada. En unos minutos recibirás la foto en tu email.");
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught.getMessage() != null && caught.getMessage().contains("No quedan créditos")) {
							if (Window.confirm(caught.getMessage() + ". Quieres cambiar tu perfil para poder hacer más fotos?")) {
								comprarCreditos();
							} else {
								Window.alert("No te peocupes. Podrás hacer más fotos HD mañana.");
							}
						} else {
							Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
						}
					}
				});
			}
		});
	}

	public static void panorama() {
		autenticar(new Runnable() {
			public void run() {

				int segundosRender = 1800;
				Fsk4.getServicio().panorama(Datos.get().getEmail(), DibujoHelper.getPlano(), segundosRender, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Window.alert("Foto HD programada. En unos minutos recibirás la foto en tu email.");
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught.getMessage() != null && caught.getMessage().contains("No quedan créditos")) {
							if (Window.confirm(caught.getMessage() + ". Quieres comprar más créditos?")) {
								comprarCreditos();
							} else {
								Window.alert("Podrás hacer más fotos HD cuando compres más créditos. Lo siento.");
							}
						} else {
							Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
						}
					}
				});
			}
		});
	}

	public static void exportarABlend() {
		autenticar(new Runnable() {
			public void run() {

				Fsk4.getServicio().exportarABlend(Datos.get().getEmail(), DibujoHelper.getPlano(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Window.alert("Exportación a Blender programada. En unos minutos recibirás el archivo en tu email.");
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught.getMessage() != null && caught.getMessage().contains("No quedan créditos")) {
							if (Window.confirm(caught.getMessage() + ". Quieres comprar más créditos?")) {
								comprarCreditos();
							} else {
								Window.alert("Podrás hacer más fotos HD cuando compres más créditos. Lo siento.");
							}
						} else {
							Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
						}
					}
				});
			}
		});
	}

	public static void compartir() {
		autenticar(new Runnable() {
			
			@Override
			public void run() {
				String n = Window.prompt("Enviar este plano al email", "");
				if (n == null || "".equals(n.trim())) Window.alert("El email es obligatorio"); 
				else {
					Fsk4.getServicio().enviarPorEmail(Datos.get().getEmail(), n, DibujoHelper.getPlano(), new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							Window.alert("Email enviado.");
						}

						@Override
						public void onFailure(Throwable caught) {
							Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
						}
					});

				}
			}
		});
	}
	
	public static void comprarCreditos() {
		
		Window.open("publico/cambiarperfil.jsp?login=" + Datos.get().getEmail(), "", "");
		
		if (false) PantallaHelper.abrirDialogo("divcompra", 500, 200, new Runnable() {
			
			@Override
			public void run() {
				
				$("#compra-botoncomprar").click(new Function() {
					@Override
					public void f() {
						PantallaHelper.cerrarDialogo();
						
						Fsk4.alert("Lo siento. Esta opción no está disponible de momento.");
						
						if (false) {
							
							double euros = Double.parseDouble($("#compra-euros").val());
							Frame f = new Frame("/pasarela.jsp?email=" + Datos.get().getEmail() + "&euros=" + euros);
							f.setWidth("800px");
							f.setHeight("600px");
							PantallaHelper.abrirDialogo(f, 700, 500, new Runnable() {
								public void run() {										
								}
							});

						}
						
					}
				});
			}
		});
	}
	
	
	public static void actualizarCarrito() {
		$("#presupuesto-lineas").html("");
		$("#presupuesto-lineas").html("<img src='img/comecocos.gif' style='position:absolute; left: 150px; top: 200px;''>");
		Fsk4.getServicio().getInfoPresupuesto(DibujoHelper.getPlano(), new AsyncCallback<Map<String,Map<String,String>>>() {

			@Override
			public void onFailure(Throwable caught) {
				Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());

			}

			@Override
			public void onSuccess(Map<String, Map<String, String>> result) {
				$("#presupuesto-lineas").html("");
				
				StringBuffer h = new StringBuffer();
				h.append("<table class='table table-hover'>");
				
				double total = 0;
				int pos = 0;
				for (final Planta hab : DibujoHelper.getPlano().getPlantas()) {
					for (final Objeto o : hab.getContenido()) {
						if (o instanceof Modelo) {

							if (((Modelo) o).getId() != null && !"".equals(((Modelo) o).getId()) && !(o instanceof FuenteLuz)) {
								Map<String, String> d = result.get(((Modelo) o).getId());
								
								
								h.append("<tr><td>" +
										"<img class='presupuesto-boton-modelo' data-id='" +((Modelo) o).getId()  + "'' style='width: 60px; height: 60px; max-width: none;' data-pos='" + pos + "' src='" + ((Modelo) o).getImagenVistaPrevia() + "' ></td>" +
										"<td><b>" + d.get("nombre") + "</b></td><td><a href='#' class='button hint--left presupuesto-boton-ocultar' data-hint='Ocultar' data-pos='" + pos + "'><i class='icon-eye-" + ((o.isVisible())?"open":"close") + "'></i></a></td>" +
										"<td><a href='#' class='button hint--left presupuesto-boton-eliminar' data-hint='Eliminar' data-pos='" + pos + "'><i class='icon-remove'></i></a></td>" +
												"<td><div class='presupuesto-total-linea hint--left' data-hint='Click para inlcuir / excluir del precio final' style='" + ((o.isIncluirEnPresupuesto())?"":"text-decoration:line-through;") + "' data-pos='" + pos + "'>" + d.get("pvp") + "</div></td></tr>");
								
								contenidoPresupuesto.put("" + pos++, (Modelo) o);

								if (d.get("pvp") != null && !"".equals(d.get("pvp"))) total += Double.parseDouble(d.get("pvp"));
							}
							
						}
					}
				}			
				
				h.append("</table>");
				
				$("#totalpresupuesto").html("<h3>Total..... " + (Math.round(100d * total) / 100d) + " Euros</h3>");
				
				$("#presupuesto-lineas").html(h.toString());
				
				$(".presupuesto-boton-ocultar").click(new Function() {
					@Override
					public void f(Element e) {
						Modelo o = contenidoPresupuesto.get($(e).attr("data-pos"));
						
						DibujoHelper.ocultar(o);
												
						$(e).children().attr("class", o.isVisible()?"icon-eye-open":"icon-eye-close");
					}
				});
				
				$(".presupuesto-boton-eliminar").click(new Function() {
					@Override
					public void f(Element e) {
						Modelo o = contenidoPresupuesto.get($(e).attr("data-pos"));
						
						DibujoHelper.eliminar(o);						
						
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							
							@Override
							public void execute() {
								actualizarCarrito();
							}
						});
					}
				});

				$(".presupuesto-total-linea").click(new Function() {
					@Override
					public void f(Element e) {
						Modelo o = contenidoPresupuesto.get($(e).attr("data-pos"));
						
						o.setIncluirEnPresupuesto(!o.isIncluirEnPresupuesto());
												
						actualizarCarrito();

					}
				});

				
			}
		});
	}

	public static void imprimir3d() {
		Fsk4.alert("Disponible en breve");
	}

	public static void cola() {
		PantallaHelper.abrirDialogo("divcolarenders", 700, 400, new Runnable() {
			public void run() {
			
				refrescarColaRenders();
				
			}

		});
	}

	protected static void refrescarColaRenders() {
		$("cola-resultado").html("Buscando...");
		
		Fsk4.getServicio().sql("sql: select ren_id, ren_usuario, to_char(ren_creado, 'HH24:MI'), ren_estado, to_char(ren_finrenderprevisto, 'HH24:MI') from fsk_render where ren_estado in ('En cola', 'Renderizando') order by ren_creado desc", new AsyncCallback<String[][]>() {
			
			@Override
			public void onSuccess(String[][] result) {
				
				if (result == null || result.length == 0) $("#cola-resultado").html("No hay renders en marcha. <input type='button' id='botonrefrescarrenders' value='Actualizar'/>");
				else {
					StringBuffer h = new StringBuffer("<table>");
					h.append("<tr>");
					h.append("<td style='bottom-border: 1px solid #000000;'><b>Usuario</b></td>");
					h.append("<td style='bottom-border: 1px solid #000000;'><b>Creado</b></td>");
					h.append("<td style='bottom-border: 1px solid #000000;'><b>Estado</b></td>");
					h.append("<td style='bottom-border: 1px solid #000000;'><b>Previsión</b></td>");
					h.append("<td style='bottom-border: 1px solid #000000;'><input type='button' id='botonrefrescarrenders' value='Actualizar'/></td>");
					h.append("</tr>");
					
					for (String[] l : result) {
						h.append("<tr>");
						h.append("<td style='bottom-border: 1px solid #000000;'>" + l[1] + "</td>");
						h.append("<td style='bottom-border: 1px solid #000000;'>" + l[2] + "</td>");
						h.append("<td style='bottom-border: 1px solid #000000;'>" + l[3] + "</td>");
						h.append("<td style='bottom-border: 1px solid #000000;'>" + l[4] + "</td>");
						if (l[1].equalsIgnoreCase(Datos.get().getEmail()) && !"Renderizando".equalsIgnoreCase(l[3])) h.append("<td style='bottom-border: 1px solid #000000;'><input type='button' class='boton-cancelarrender' data-id='" + l[0] + "' value='Cancelar'></td>");
						else h.append("<td style='bottom-border: 1px solid #000000;'>&nbsp;</td>");
						h.append("</tr>");
					}
					h.append("</table>");
					
					$("#cola-resultado").html(h.toString());
					
					$(".boton-cancelarrender").click(new Function() {
						public void f(com.google.gwt.user.client.Element e) {
							PantallaHelper.cerrarDialogo();
							Fsk4.getServicio().cancelarRender(e.getAttribute("data-id"), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
								}

								@Override
								public void onSuccess(Void result) {
									Window.alert("Render cancelado");
								}
							});
						}
					});
					
				}

				$("#botonrefrescarrenders").click(new Function() {
					public void f() {
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							
							@Override
							public void execute() {
								refrescarColaRenders();
							}
						});
					};
				});

			}
			
			@Override
			public void onFailure(Throwable caught) {
				Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
			}
		});
		
	}
	
	  // Load the image in the document and in the case of success attach it to the viewer
	  private static IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {

	        new PreloadedImage(uploader.fileUrl(), showImage);
	        
	        // The server sends useful information to the client by default
	        UploadedInfo info = uploader.getServerInfo();
	        System.out.println("File name " + info.name);
	        System.out.println("File content-type " + info.ctype);
	        System.out.println("File size " + info.size);

	        // You can send any customized message and parse it 
	        System.out.println("Server message " + info.message);
	      }
	    }
	  };

	  // Attach an image to the pictures viewer
	  private static OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
	    public void onLoad(PreloadedImage image) {
	      image.setWidth("75px");
	      //panelImages.add(image);
	      final Image imagen = Image.wrap($("#perfil-imagen").elements()[0]);
	      imagen.setUrl(image.getUrl());
	      //$("#perfil-imagenuploader").append($(image));
	    }
	  };
	  
	public static void perfil() {
		PantallaHelper.abrirDialogo("divperfil", 572, 400, new Runnable() {
			
			@Override
			public void run() {
				
				Fsk4.getServicio().getDatosUsuario(Datos.get().getEmail(), new AsyncCallback<Map<String,String>>() {

					@Override
					public void onFailure(Throwable caught) {
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}

					@Override
					public void onSuccess(Map<String, String> result) {
						final TextBox login = TextBox.wrap($("#perfil-login").elements()[0]);
						final TextBox email = TextBox.wrap($("#perfil-email").elements()[0]);
						final TextBox nombre = TextBox.wrap($("#perfil-nombre").elements()[0]);
						final TextBox apellidos = TextBox.wrap($("#perfil-apellidos").elements()[0]);
						final TextBox nacimiento = TextBox.wrap($("#perfil-nacimiento").elements()[0]);
						final Image imagen = Image.wrap($("#perfil-imagen").elements()[0]);
						
						// Create a new uploader panel and attach it to the document
					    //MultiUploader defaultUploader = new MultiUploader();
						final SingleUploader defaultUploader = new SingleUploader();
						defaultUploader.setAutoSubmit(true);
					    RootPanel.get("perfil-imagenuploader").add(defaultUploader);
						//$("#perfil-imagenuploader").append($(defaultUploader));
						
						// Add a finish handler which will load the image once the upload finishes
					    defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
						
						login.setValue(result.get("login"));
						email.setValue(result.get("email"));
						nombre.setValue(result.get("nombre"));
						apellidos.setValue(result.get("apellidos"));
						nacimiento.setValue(result.get("nacimiento"));
						if (result.get("logo") != null && !"".equals(result.get("logo"))) imagen.setUrl(result.get("logo"));
						
						$("#perfil-creditos").html(result.get("creditos"));
						
						
						$("#perfil-botoncomprar").bind(Event.ONCLICK, new Function() {
							@Override
							public boolean f(Event e) {
								PantallaHelper.cerrarDialogo();
								comprarCreditos();
								return true;
							}
						});
						
						
						$("#perfil-botongrabar").bind(Event.ONCLICK, new Function() {
							@Override
							public boolean f(Event e) {
								
								if (email.getValue() == null || "".equalsIgnoreCase(email.getValue().trim()) 
										|| nombre.getValue() == null || "".equalsIgnoreCase(nombre.getValue().trim())
										|| apellidos.getValue() == null || "".equalsIgnoreCase(apellidos.getValue().trim())
										|| nacimiento.getValue() == null || "".equalsIgnoreCase(nacimiento.getValue().trim())
										) {
									Window.alert("Debes rellenar todos los campos");
								} else Fsk4.getServicio().grabarPerfil(login.getValue(), email.getValue(), nacimiento.getValue(), nombre.getValue(), apellidos.getValue(), nacimiento.getValue(), (defaultUploader.getServerInfo() != null)?defaultUploader.getServerInfo().field:null, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										PantallaHelper.cerrarDialogo();
										Datos.get().setEmail(email.getValue().toLowerCase().trim());
										PantallaHelper.cerrarDialogo();
										
										$("seccion_invitado").hide();
										$("seccion_usuario").show();
									}
								});
									
								return true;
							}
							
						});
						
					}
				});
				

		}
		});
	}

	public static void login() {
		autenticar(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public static void registro() {
		registrar(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public static void logout() {
		Datos.get().setEmail(null);
		$("#seccion_usuario").hide();
		$("#seccion_invitado").show();
		Fsk4.getServicio().logout(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
			}
		});
	}

	public static void agrandarPreview() {
		PantallaHelper.abrirDialogo("divagrandarpreview", 400, 300, new Runnable() {
			
			@Override
			public void run() {
				Fsk4.getServicio().previewGrande(DibujoHelper.getPlano(), new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						PantallaHelper.cerrarDialogo();
						Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
					}

					@Override
					public void onSuccess(final String result) {
						if ($("#previewAgrandada").isVisible()) {
							$("#previewAgrandada").html("<img src='" + result + "'>");

							/*
							final int[] cont = {0};
							
							Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

								@Override
								public boolean execute() {
									$("#previewAgrandada").html("<img src='" + result + "?a=" + new Date().getTime() + "'>");
									cont[0]++;
									return cont[0] < 4 && $("#previewAgrandada").isVisible();
								}
								
							}, 3000);
							*/
						}
						
					}
				});
			}
		});
	}

	public static void abrirVistaPrevia() {
		$(".preview").show();
		//$("#bfoto").show();
		//$("#b360").show();
	}

	public static void cerrarVistaPrevia() {
		$(".preview").hide();
		//$("#bfoto").hide();
		//$("#b360").hide();
	}

	public static void encuesta() {
		PantallaHelper.abrirDialogo("divencuesta", 300, 500, new Runnable() {
			
			@Override
			public void run() {
				$("#enc_submit").click(new Function() {
					@Override
					public void f() {
						PantallaHelper.cerrarDialogo();
						String nombre = $("#enc_nombre").val();
						String email = $("#enc_email").val();
						String comentarios = $("#enc_comentarios").val();
						int valoracion = 5;
						for (int i = 0; i <= 10; i++) {
							if ($("#enc_valoracion" + i).is(":checked")) valoracion = i;
						}
						
						Fsk4.getServicio().encuesta(nombre, email, valoracion, comentarios, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
							}

							@Override
							public void onSuccess(Void result) {
								Window.alert("Gracias por tu ayuda!");
							}
						});
					}
				});
			}
		});
	}
}
