package fsk4.client.recursos;

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Recursos extends ClientBundle {

	public Recursos INSTANCIA = GWT.create(Recursos.class);
	
	@Source("camara2.svg")
	SVGResource svgCamara();
	
	@Source("bombilla.svg")
	SVGResource svgBombilla();

	@Source("sol.svg")
	SVGResource svgSol();

	@Source("rotador.svg")
	SVGResource svgRotador();
	
	@Source("patron_-.jpg")
	ImageResource patron_i2();

	@Source("patron_cuadrado.jpg")
	ImageResource patron_cuadrado();

	@Source("patron_i.jpg")
	ImageResource patron_i();

	@Source("patron_l.jpg")
	ImageResource patron_l();

	@Source("patron_l2.jpg")
	ImageResource patron_l2();

	@Source("patron_r.jpg")
	ImageResource patron_r();

	@Source("patron_r2.jpg")
	ImageResource patron_r2();

	@Source("patron_t.jpg")
	ImageResource patron_t();
	
	@Source("hidden.png")
	ImageResource hidden();
	
	@Source("draw.png")
	ImageResource draw();

	@Source("delete.png")
	ImageResource delete();
}
