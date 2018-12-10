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

import fsk4.shared.Material;

public class CampoTextura extends VerticalPanel {

	private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();
	
	private String id;
	private double ancho;
	private double alto;	
	private String tipo;
	private String color;
	private String textura;
	private boolean favorito;
	private double escala;

	private String idCampo;

	public CampoTextura(String idCampo) {
			this.idCampo = idCampo;
			actualizar();
	}

	public void addChangeHandler(ChangeHandler changeHandler) {
		changeHandlers.add(changeHandler);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTextura() {
		return textura;
	}

	public void setTextura(String textura) {
		this.textura = textura;
	}
	
	public void setValue(String tipo, String color, String textura, String id, double ancho, double alto) {
		setTipo(tipo);
		setColor(color);
		setTextura(textura);
		setId(id);
		setAncho(ancho);
		setAlto(alto);
		actualizar();
	}

	private void actualizar() {
		clear();
		Widget w;
		if (getTextura() != null && !"".equals(getTextura())) {
			/*
<input id="text" class="span2" type="text" rel="tooltip" data-original-title="Text" data-placement="left" value="">
			 */
			add(w = new Image(getTextura(), 0, 0, 60, 60));
			w.addStyleName("span");
			w.addStyleName("span2");
			w.getElement().setAttribute("rel", "tooltip");
			w.getElement().setAttribute("data-original-title", "Haz click para elegir otra textura");
			w.getElement().setAttribute("data-placement", "left");
			w.addStyleName("boton-textura");
		} else {
			add(w = new SimplePanel());
			w.getElement().setAttribute("style", "background-color:" + getColor() + ";");
			w.addStyleName("boton-textura");
		}
		$(w).click(new Function() {
			@Override
			public void f() {
				seleccionarTextura();
			}
		});
		for (ChangeHandler h : changeHandlers) {
			h.onChange(new ChangeEvent() {
				
			});
		}
		$("#" + idCampo).change();
	}

	protected void seleccionarTextura() {
		final String idContenedor = "contenedor-texturas-" + new Date().getTime();
		PantallaHelper.abrirSelectorArticulo(new HTML("<div id='" + idContenedor + "'></div>"), new Runnable() {
			
			@Override
			public void run() {				
				
				PantallaHelper.crearSelectorArticulos(idContenedor, new Seleccionado() {
					
					@Override
					public void seleccionado(String tipo, String id, String preview, String cenital, double ancho, double alto, boolean favorito, double escala) {
						PantallaHelper.cerrarSelectorArticulo();
						setId(id);
						setTextura(cenital);
						setAncho(ancho);
						setAlto(alto);
						setFavorito(favorito);
						actualizar();
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

	public void setMaterial(Material m) {
		if (m != null) {
			setId(m.getId());
			setTipo(m.getTipo());
			setColor(m.getColor());
			setTextura(m.getTextura());
			setAncho(m.getAncho());
			setAlto(m.getAlto());
		} else {
			setId("");
			setTipo("");
			setColor("");
			setTextura("");
			setAncho(100);
			setAlto(100);
		}
		actualizar();
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
