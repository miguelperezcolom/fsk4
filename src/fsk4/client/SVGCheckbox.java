package fsk4.client;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

public class SVGCheckbox extends OMSVGGElement {

	private OMSVGRectElement r;
	private boolean valor;
	private ChangeListenerBoolean changeListener;
	private OMSVGPathElement p;
	private String texto;

	public SVGCheckbox() {
		
	}

	public SVGCheckbox(boolean valor, String texto, ChangeListenerBoolean changeListener) {
		this.valor = valor;
		this.texto = texto;
		this.changeListener = changeListener;
		
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, 15, 15, 0, 0));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		appendChild(p = DibujoHelper.getDoc().createSVGPathElement());
        p.getPathSegList().appendItem(p.createSVGPathSegMovetoAbs(-3, -1));
		p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(7, 9));
		p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(20, -5));
		//p.getPathSegList().appendItem(p.createSVGPathSegClosePath());
        p.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "none");
        p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "5");
        p.setAttribute("visibility", (valor)?"visible":"hidden");
        p.setAttribute("pointer-events", "none");
        
        OMSVGTextElement t;
		appendChild(t = DibujoHelper.getDoc().createSVGTextElement(20, 15, OMSVGLength.SVG_LENGTHTYPE_PX, texto));
		t.setAttribute("pointer-events", "none");
		
		r.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				SVGCheckbox.this.valor = !SVGCheckbox.this.valor;
		        p.setAttribute("visibility", (SVGCheckbox.this.valor)?"visible":"hidden");
				//SVGCheckbox.this.r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, (SVGCheckbox.this.valor)?"#ffffff":"#eaeaea");
				SVGCheckbox.this.changeListener.change(SVGCheckbox.this.valor);
			}
		});
		
	}
	
}
