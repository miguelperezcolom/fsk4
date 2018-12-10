package fsk4.client;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

public class SVGEsquinaEscalado extends OMSVGGElement {

	public float x0;
	public float y0;
	private SVGMarcaSeleccion marca;

	public SVGEsquinaEscalado(SVGMarcaSeleccion marca, Float x, Float y) {
		this.marca = marca;
		final OMSVGCircleElement c;
		appendChild(c = DibujoHelper.getDoc().createSVGCircleElement(0, 0, 5));
		c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.5");
		c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#0000ff");
		
		setAttribute("transform", "translate(" + x + "," + y + ")");
		x0 = x;
		y0 = y;
		
		c.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation();
				event.preventDefault();
				
				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
					DibujoHelper.arrastradoX0 = x0;
					DibujoHelper.arrastradoY0 = y0;
					DibujoHelper.svggarrastrado = SVGEsquinaEscalado.this;
					DibujoHelper.setClientX0(event.getNativeEvent().getClientX());					
					DibujoHelper.setClientY0(event.getNativeEvent().getClientY());
				}
			}
		});		
	}

	public void arrastrado(float dx, float dy) {
		float x = DibujoHelper.arrastradoX0 + dx;
		float y = DibujoHelper.arrastradoY0 + dy;
		
		marca.escalarA(x0, y0, x, y);
	}
	
}
