package fsk4.client;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

public class SVGBoton extends OMSVGGElement {
	
	private String texto;
	private Runnable accion;

	public SVGBoton(String texto, float f, float g, final Runnable accion) {
		this.texto = texto;
		this.accion = accion;
		
		final OMSVGRectElement r;
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, 100, 25, 5, 5));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#D9F5FF");
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.5");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		
		OMSVGTextElement t;
		appendChild(t = DibujoHelper.getDoc().createSVGTextElement(10, 20, OMSVGLength.SVG_LENGTHTYPE_PX, texto));
		t.setAttribute("pointer-events", "none");
		
		setAttribute("transform", "translate(" + f + ", " + g + ")");
		
		addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				accion.run();
			}
		});

		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				accion.run();
			}
		});
		
		addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#9CE0F7");
			}
		});
		
		addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#D9F5FF");
			}
		});
		
	}

}
