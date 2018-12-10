package fsk4.client;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import fsk4.shared.Modelo;
import fsk4.shared.Punto;

public class SVGMarcaSeleccion extends OMSVGGElement {
	
	private OMSVGRectElement r;
	private List<SVGEsquinaEscalado> esquinasEscalado = new ArrayList<SVGEsquinaEscalado>();
	private float x0;
	private float y0;
	private float w0;
	private float h0;
	private float gx0;
	private float gy0;
	private OMSVGMatrix ctm;


	public SVGMarcaSeleccion(Modelo seleccionado) {
		
		ctm = ((OMSVGGElement)DibujoHelper.payload.get(DibujoHelper.seleccionado)).getScreenCTM();
		
		r = DibujoHelper.getDoc().createSVGRectElement(new Float(-1 * DibujoHelper.ESCALA * seleccionado.getAncho() / (2 * 100d)), new Float(-1 * DibujoHelper.ESCALA * seleccionado.getAlto() / (2 * 100d)), new Float(seleccionado.getAncho() * DibujoHelper.ESCALA / 100d), new Float(seleccionado.getAlto() * DibujoHelper.ESCALA / 100d), 0, 0);
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#0000ff");
		r.setAttribute("pointer-events", "none");		
		
		x0 = seleccionado.getX();
		y0 = seleccionado.getY();
		w0 = seleccionado.getAncho();
		h0 = seleccionado.getAlto();
		
		appendChild(r);
		
		anadirEsquinaEscalado(new Float(-1 * DibujoHelper.ESCALA * seleccionado.getAncho() / (2 * 100d)), new Float(-1 * DibujoHelper.ESCALA * seleccionado.getAlto() / (2 * 100d)));
		anadirEsquinaEscalado(new Float(1 * DibujoHelper.ESCALA * seleccionado.getAncho() / (2 * 100d)), new Float(-1 * DibujoHelper.ESCALA * seleccionado.getAlto() / (2 * 100d)));
		anadirEsquinaEscalado(new Float(1 * DibujoHelper.ESCALA * seleccionado.getAncho() / (2 * 100d)), new Float(1 * DibujoHelper.ESCALA * seleccionado.getAlto() / (2 * 100d)));
		anadirEsquinaEscalado(new Float(-1 * DibujoHelper.ESCALA * seleccionado.getAncho() / (2 * 100d)), new Float(1 * DibujoHelper.ESCALA * seleccionado.getAlto() / (2 * 100d)));

		
	}
	

	private void anadirEsquinaEscalado(Float x, Float y) {
		SVGEsquinaEscalado e;
		appendChild(e = new SVGEsquinaEscalado(this, x, y));
		esquinasEscalado.add(e);
	}


	public void escalarA(float x0, float y0, float x, float y) {
		
		if (gx0 == 0) {
			OMSVGPoint p = DibujoHelper.localToGlobal(this, 0f, 0f);
			gx0 = p.getX();
			gy0 = p.getY();
		}
		
		float minx = 564564654646464f;
		float miny = 564564654646464f;
		float maxx = -564564654646464f;
		float maxy = -564564654646464f;
		
		Modelo m = (Modelo) DibujoHelper.seleccionado;
		OMSVGPoint p = DibujoHelper.roomToGlobal(m);

		for (SVGEsquinaEscalado e : esquinasEscalado) {
			float xx = e.x0;
			float yx = e.y0;
			if (e.x0 == x0) xx = x;
			if (e.y0 == y0) yx = y; 
			
			if (xx < minx) minx = xx;
			if (xx > maxx) maxx = xx;
			if (yx < miny) miny = yx;
			if (yx > maxy) maxy = yx;
		}		
		
		float w = maxx - minx; 
		float h = maxy - miny; 		

		if (w > 0 && h > 0) {
			
			m.setAncho(w);
			m.setAlto(h);		
			
			OMSVGPoint pt = DibujoHelper.getSvg().createSVGPoint();
			pt.setX(new Float(((maxx + minx) / 2)));
			pt.setY(new Float(((maxy + miny) / 2)));
			
			OMSVGPoint pa = pt.matrixTransform(ctm);
			OMSVGPoint px = DibujoHelper.globalToRoom(pa);
			m.setX(px.getX());
			m.setY(px.getY());

			r.setAttribute("x", "" + (-1 * m.getAncho() * DibujoHelper.ESCALA * 0.01d / 2d));
			r.setAttribute("y", "" + (-1 * m.getAlto() * DibujoHelper.ESCALA * 0.01d / 2d));
			r.setAttribute("width", "" + (m.getAncho() * DibujoHelper.ESCALA * 0.01d));
			r.setAttribute("height", "" + (m.getAlto() * DibujoHelper.ESCALA * 0.01d));
			DibujoHelper.imagenesModelos.get(m).setAttribute("transform", "scale(" + (m.getAncho() / m.getAncho0()) + ", " + (m.getAlto() / m.getAlto0()) + ")");
			

			String auxs = "translate(" + m.getX() + "," + m.getY() + ")";
			auxs += " rotate(" + m.getPan() + " 0 0)";
			//if (!(v instanceof Volumen)) auxs += " scale(" + (v.getEscala() / 0.001d) + ")";					
			((OMSVGGElement)DibujoHelper.payload.get(DibujoHelper.seleccionado)).setAttribute("transform", auxs);
			
			for (SVGEsquinaEscalado e : esquinasEscalado) {
				x = ((e.x0 > 0)?1:-1) *  w / 2f;
				y = ((e.y0 > 0)?1:-1) *  h / 2f;
				e.setAttribute("transform", "translate(" + x + "," + y + ")");
			}

			if (DibujoHelper.i != null) DibujoHelper.i.updateDimensiones();
		}
		
	}

}
