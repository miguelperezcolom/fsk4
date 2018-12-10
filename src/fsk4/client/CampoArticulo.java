package fsk4.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fsk4.shared.Modelo;

public class CampoArticulo extends VerticalPanel {

	private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();
	
	private String id;
	private double ancho;
	private double alto;	
	private String imagenParaPlano;
	private String imagenVistaPrevia;
	private boolean favorito;
	private double escala;

	private String idCampo;

	public CampoArticulo(String idCampo) {
			this.idCampo = idCampo;
			actualizar();
	}

	public void addChangeHandler(ChangeHandler changeHandler) {
		changeHandlers.add(changeHandler);
	}


	public void setValue(String imagenParaPlano, String imagenVistaPrevia, String id, double ancho, double alto) {
		setImagenParaPlano(imagenParaPlano);
		setImagenVistaPrevia(imagenVistaPrevia);
		setId(id);
		setAncho(ancho);
		setAlto(alto);
		actualizar();
	}

	private void actualizar() {
		clear();
		Widget w;
		if (getImagenVistaPrevia() != null && !"".equals(getImagenVistaPrevia())) {
			add(w = new Image(getImagenVistaPrevia(), 0, 0, 60, 60));
			w.addStyleName("boton-textura");
		} else {
			add(w = new SimplePanel());
			w.getElement().setAttribute("style", "background-color:" + "grey" + ";");
			w.addStyleName("boton-textura");
		}
		$(w).click(new Function() {
			@Override
			public void f() {
				seleccionarArticulo();
			}
		});	
		$("#" + idCampo).change();
	}

	protected void seleccionarArticulo() {
		final String idContenedor = "contenedor-mobiliario-" + new Date().getTime();
		PantallaHelper.abrirSelectorArticulo(new HTML("<div id='" + idContenedor + "'></div>"), new Runnable() {
			
			@Override
			public void run() {				
				
				PantallaHelper.crearSelectorArticulos(idContenedor, new Seleccionado() {
					
					@Override
					public void seleccionado(String tipo, String id, String preview, String cenital, double ancho, double alto, boolean favorito, double escala) {
						PantallaHelper.cerrarSelectorArticulo();
						setId(id);
						setImagenParaPlano(cenital);
						setImagenVistaPrevia(preview);
						setAncho(ancho);
						setAlto(alto);
						setFavorito(favorito);
						setEscala(escala);
						actualizar();	
						
						for (ChangeHandler h : changeHandlers) {
							h.onChange(new ChangeEvent() {
								
							});
						}
					}
				});
				
			}
		});
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getAncho() {
		return ancho;
	}

	public void setAncho(double ancho) {
		this.ancho = ancho;
	}

	public double getAlto() {
		return alto;
	}

	public void setAlto(double alto) {
		this.alto = alto;
	}

	public void setModelo(Modelo m) {
		if (m != null) {
			setId(m.getId());
			setImagenParaPlano(m.getImagenParaPlano());
			setImagenVistaPrevia(m.getImagenVistaPrevia());
			setAncho(m.getAncho());
			setAlto(m.getAlto());
			setFavorito(m.isFavorito());
		} else {
			setId("");
			setImagenParaPlano("");
			setImagenVistaPrevia("");
			setAncho(0);
			setAlto(0);
			setFavorito(false);
		}
		actualizar();
	}

	public String getImagenParaPlano() {
		return imagenParaPlano;
	}

	public void setImagenParaPlano(String imagenParaPlano) {
		this.imagenParaPlano = imagenParaPlano;
	}

	public String getImagenVistaPrevia() {
		return imagenVistaPrevia;
	}

	public void setImagenVistaPrevia(String imagenVistaPrevia) {
		this.imagenVistaPrevia = imagenVistaPrevia;
	}

	public boolean isFavorito() {
		return favorito;
	}

	public void setFavorito(boolean favorito) {
		this.favorito = favorito;
	}

	public double getEscala() {
		return escala;
	}

	public void setEscala(double escala) {
		this.escala = escala;
	}
	
}
