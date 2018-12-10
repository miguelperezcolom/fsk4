package fsk4.client;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import fsk4.shared.Linea;
import fsk4.shared.Material;
import fsk4.shared.Pared;
import fsk4.shared.Puerta;

public class SVGAlzado extends OMSVGGElement {

	private Pared p;

	public SVGAlzado(Pared p) {
		this.setP(p);
		
		OMSVGRectElement r;
		
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(-5, new Float(p.getAlto() * DibujoHelper.ESCALA), new Float(new Linea(p).longitud() + 10), 5, 0, 0));
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#000000");

		
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, new Float(new Linea(p).longitud()), new Float(p.getAlto() * DibujoHelper.ESCALA), 0, 0));
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#" + DibujoHelper.pats.get(p).getId() + ")");
		
		r.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (DibujoHelper.texturaEnDrag != null) {
					double patwi = DibujoHelper.ESCALA * Double.parseDouble(DibujoHelper.texturaEnDrag.getAttribute("data-ancho")) / 100;  
					double pathe = DibujoHelper.ESCALA * Double.parseDouble(DibujoHelper.texturaEnDrag.getAttribute("data-alto")) / 100;  
					
					DibujoHelper.imgs.get(SVGAlzado.this.getP()).setAttribute("href", DibujoHelper.texturaEnDrag.getAttribute("data-cenital"));
					DibujoHelper.imgs.get(SVGAlzado.this.getP()).setAttribute("width", "" + patwi);
					DibujoHelper.imgs.get(SVGAlzado.this.getP()).setAttribute("height", "" + pathe);
					DibujoHelper.pats.get(SVGAlzado.this.getP()).setAttribute("width", "" + patwi);			
					DibujoHelper.pats.get(SVGAlzado.this.getP()).setAttribute("height", "" + pathe);			
					DibujoHelper.pats.get(SVGAlzado.this.getP()).setAttribute("viewbox", "0 0 " + patwi + " " + pathe);
					DibujoHelper.texturaDragginOn = SVGAlzado.this;
				}
				
			}
		});
		
		r.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (DibujoHelper.texturaEnDrag != null) {						
					Material rs = SVGAlzado.this.p.getRevestimientoInterior().getRevestimiento1();
					
					double patwi = DibujoHelper.ESCALA * rs.getAncho() / 100;  
					double pathe = DibujoHelper.ESCALA * rs.getAlto() / 100;  
					
					DibujoHelper.imgs.get(SVGAlzado.this.getP()).setAttribute("href", rs.getTextura());
					DibujoHelper.imgs.get(SVGAlzado.this.getP()).setAttribute("width", "" + patwi);
					DibujoHelper.imgs.get(SVGAlzado.this.getP()).setAttribute("height", "" + pathe);
					DibujoHelper.pats.get(SVGAlzado.this.getP()).setAttribute("width", "" + patwi);			
					DibujoHelper.pats.get(SVGAlzado.this.getP()).setAttribute("height", "" + pathe);			
					DibujoHelper.pats.get(SVGAlzado.this.getP()).setAttribute("viewbox", "0 0 " + patwi + " " + pathe);

					if (DibujoHelper.texturaDragginOn == SVGAlzado.this) DibujoHelper.texturaDragginOn = null;
				}
				
			}
		});
		
		
		for (Puerta pta : p.getPuertas()) {
			appendChild(r = DibujoHelper.getDoc().createSVGRectElement(new Float(pta.getDistanciaDesdeDe()), new Float(pta.getZ()), new Float(pta.getAncho()), new Float(pta.getAlto()), 0, 0));
			r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
			r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
			r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
			r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#9a9a9a");
		}
		


	}

	public Pared getP() {
		return p;
	}

	public void setP(Pared p) {
		this.p = p;
	}
	
	

}
