package fsk4.client.svg;

import java.util.Date;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.events.HasGraphicalHandlers;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

import fsk4.client.Fsk4;
import fsk4.client.recursos.Recursos;


public abstract class AbstractRotable extends AbstractSeleccionable {
	
	private SVGRotador rotador;


	@Override
	public void init() {
		
		// circulo para centrado rotación
		OMSVGCircleElement cxx = SVGHelper.getDoc().createSVGCircleElement(0f, 0f, new Float(1000));
		cxx.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffff00");
		cxx.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0");
		cxx.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_OPACITY_PROPERTY, "0"); 
		cxx.setAttribute("pointer-events", "none");
        appendChild(cxx);
        
        
        appendChild(rotador = new SVGRotador(this));

		super.init();
		
		//appendChild(new SVGPuntoArrastreRotacion(this));

	}
	

	@Override
	public String getTransformString() {
		String auxs = super.getTransformString();
		auxs += " rotate(" + getPan() + " 0 0)";
		return auxs;
	}


	public abstract double getPan();


	public abstract void setPan(double pan);


	public void md() {
		rotador.setAttribute("visibility", "visible");
	}


	
}
