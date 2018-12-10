package fsk4.client;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;

public abstract class SVGDialogo extends OMSVGGElement {
	
	public SVGDialogo() {
		// get the screen height and width  
		int maskHeight = Window.getClientHeight();  
		int maskWidth = Window.getClientWidth();
		// calculate the values for center alignment
		int dialogTop =  new Double((maskHeight - getAlto()) / 2).intValue();  
		int dialogLeft = new Double((maskWidth - getAncho()) /2).intValue(); 

		OMSVGRectElement r;
		
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, getAncho(), 20, 2, 2));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ff0000");
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		r.setAttribute("transform", "translate(0,-20)");
		
		OMSVGTextElement t;
		appendChild(t = DibujoHelper.getDoc().createSVGTextElement(5f, -5f, OMSVGLength.SVG_LENGTHTYPE_PX, getTitulo()));
		t.setAttribute("pointer-events", "none");
		t.getStyle().setSVGProperty(SVGConstants.CSS_FONT_WEIGHT_PROPERTY, "bold");
		t.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		t.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#ffffff");

		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, getAncho(), getAlto(), 2, 2));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		
		

		setAttribute("transform", "translate(" + dialogLeft + "," + dialogTop + ")");
		
		addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		});
		
	}
	
	public abstract float getAncho();
	public abstract float getAlto();
	public abstract String getTitulo();
	
	public void cerrar() {
		DibujoHelper.getBackgroundDialogo().setAttribute("visibility", "hidden");
		getParentNode().removeChild(this);
	}

}
