package fsk4.client;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;

public class SVGSlider extends OMSVGGElement {
	
	private float valorInicial;
	private float min;
	private float max;
	private ChangeListener changeListener;
	private OMSVGRectElement gzoomslider;
	private float valor;
	private OMSVGTextElement text;
	private String texto;

	public SVGSlider(float valorInicial, float min, float max, String texto, ChangeListener changeListener) {
		this.valorInicial = valorInicial;
		this.min = min;
		this.max = max;
		this.changeListener = changeListener;
		this.valor = valorInicial;
		this.texto = texto;
		
		OMSVGLineElement lin;
		appendChild(lin = DibujoHelper.getDoc().createSVGLineElement(0, 10, 110, 10));
		lin.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
		lin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		lin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		
		appendChild(gzoomslider = DibujoHelper.getDoc().createSVGRectElement(-5, -5, 10, 10, 0, 0));
		gzoomslider.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		gzoomslider.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
		gzoomslider.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		gzoomslider.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		
		gzoomslider.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
					event.preventDefault();
					event.stopPropagation();
					
					DibujoHelper.setSliderActivo(SVGSlider.this);

					SVGSlider.this.valorInicial = valor;
					
					DibujoHelper.setClientX0(event.getNativeEvent().getClientX());
					DibujoHelper.setClientY0(event.getNativeEvent().getClientY());
				}
			}
		});
		
		OMSVGRectElement r;
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(120, 0, 30, 20, 5, 5));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#BEFAD8");
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		
		r.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				String s = Window.prompt("Valor", "" + valor);
				try {
					float aux = Float.parseFloat(s);
					if (aux < SVGSlider.this.min) aux = SVGSlider.this.min;
					if (aux > SVGSlider.this.max) aux = SVGSlider.this.max;
					valor = aux;
					actualizarSlider();
					SVGSlider.this.changeListener.change(valor);		
				} catch (Exception e) {
					
				}
			}
		});
		
		appendChild(text = DibujoHelper.getDoc().createSVGTextElement(122, 15, OMSVGLength.SVG_LENGTHTYPE_PX, ""));
		text.setAttribute("pointer-events", "none");
		
		OMSVGTextElement t;
		appendChild(t = DibujoHelper.getDoc().createSVGTextElement(160, 15, OMSVGLength.SVG_LENGTHTYPE_PX, texto));
		t.setAttribute("pointer-events", "none");
		
		actualizarSlider();		
	}

	public void arrastrado(int clientX0, int clientY0, int clientX, int clientY) {
		float aux = valorInicial + (clientX - clientX0) * (max - min) / 110;
		if (aux < min) aux = min;
		if (aux > max) aux = max;
		valor = aux;
		actualizarSlider();
		changeListener.change(valor);		
	}
	
	public void setValor(float nuevoValor) {
		valor = nuevoValor;
		actualizarSlider();		
	}

	private void actualizarSlider() {
		gzoomslider.setAttribute("transform", "translate(" + (0 + ((valor - min) * 110 / (max - min))) + ", 10)");		
		for (int i = 0; i < text.getChildNodes().getLength(); i++) {
			OMNode n = text.getChildNodes().getItem(i);
			System.out.println(n.getClass().getName());
			if (n instanceof OMText) n.setNodeValue("" + (Math.round(100d * valor) / 100d)); 
		}
	}
}
