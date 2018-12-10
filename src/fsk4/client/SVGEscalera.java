package fsk4.client;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPatternElement;
import org.vectomatic.dom.svg.impl.SVGGElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import fsk4.shared.AnguloEscalera;
import fsk4.shared.Escalera;
import fsk4.shared.Linea;
import fsk4.shared.Punto;
import fsk4.shared.TramoEscalera;
import fsk4.shared.Vertice;
import fsk4.shared.Volumen;

public class SVGEscalera extends OMSVGGElement {

	private Escalera escalera;
	private OMSVGPathElement path;
	private OMSVGPathSegList segs;
	private List<SVGAnguloEscalera> vertices = new ArrayList<SVGAnguloEscalera>();
	private List<SVGRectaEscalera> rectas = new ArrayList<SVGRectaEscalera>();
	
	public SVGEscalera(Escalera escalera) {
		this.setEscalera(escalera);
		
		path = DibujoHelper.getDoc().createSVGPathElement();
        segs = path.getPathSegList();
        fillSegs(false);
        path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#fcfcfc"); //"url(#patesc" + getEscalera().getId() + ")");
        path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.1");

        appendChild(path);
        
        for (AnguloEscalera v : escalera.getAngulos()) {
        	SVGAnguloEscalera vx;
			appendChild(vx = new SVGAnguloEscalera(v, this));			
			vertices.add(vx);
			DibujoHelper.payload.put(v, vx);
			DibujoHelper.payload.put(vx, v);
        }
        for (TramoEscalera tramo : getEscalera().getTramos()) {
        	SVGRectaEscalera rv;
			appendChild(rv = new SVGRectaEscalera(escalera, tramo, this));
			rectas.add(rv);
        }
        
	}

	public void fillSegs(boolean clear) {
		if (clear) segs.clear();
		
		 for (int k = 0; k < getEscalera().getTramos().size(); k++) {
			 TramoEscalera t = getEscalera().getTramos().get(k);
			Linea l = new Linea(new Punto(t.getDe()), new Punto(t.getA()));
			
			Punto i0 = null;
			Punto i1 = null;
			Punto i2 = null;
			Punto i3 = null;
			
			Linea l1 = l.paralela(-1 * t.getAncho() / 2);
			Linea l2 = l.paralela(1 * t.getAncho() / 2);
			if (k <  getEscalera().getTramos().size() - 1) {
				TramoEscalera t2 = getEscalera().getTramos().get(k + 1); 
				Linea lx = new Linea(new Punto(t2.getDe()), new Punto(t2.getA()));
				Linea l3 = lx.paralela(-1 * t2.getAncho() / 2);
				Linea l4 = lx.paralela(1 * t2.getAncho() / 2);
				i2 = l1.intersection(l3, true);
				i3 = l2.intersection(l4, true);
			}
			if (i2 == null || i3 == null) {
				Linea l4 = l.perpendicularQuePasaPor(l.getPunto(l.longitud() + t.getAncho() / 2));
				i2 = l1.intersection(l4, true);
				i3 = l2.intersection(l4, true);
			}
			if (k > 0) {
				TramoEscalera t2 = getEscalera().getTramos().get(k - 1); 
				Linea lx = new Linea(new Punto(t2.getDe()), new Punto(t2.getA()));
				Linea l3 = lx.paralela(-1 * t2.getAncho() / 2);
				Linea l4 = lx.paralela(1 * t2.getAncho() / 2);
				i1 = l1.intersection(l3, true);
				i0 = l2.intersection(l4, true);
			}
			if (i0 == null || i1 == null) {
				Linea l3 = l.perpendicularQuePasaPor(l.getPunto(-1 * t.getAncho() / 2));
				i1 = l1.intersection(l3, true);
				i0 = l2.intersection(l3, true);
			}
            if (k == 0) {
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i0.getX()), new Float(i0.getY())));
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(i1.getX()), new Float(i1.getY())));
            }
			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i1.getX()), new Float(i1.getY())));
			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(i2.getX()), new Float(i2.getY())));
			
			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i3.getX()), new Float(i3.getY())));
			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(i0.getX()), new Float(i0.getY())));
			
            if (k == escalera.getTramos().size() - 1) {
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i2.getX()), new Float(i2.getY())));
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(i3.getX()), new Float(i3.getY())));
            }
            
            Punto p0 = l.proyeccion(i0, true);
            Punto p1 = l.proyeccion(i1, true);
            Punto p2 = l.proyeccion(i2, true);
            Punto p3 = l.proyeccion(i3, true);
			
            double d0 = l.getA().distanciaA(p0);
            double d1 = l.getA().distanciaA(p1);
            double d2 = l.getDe().distanciaA(p2);
            double d3 = l.getDe().distanciaA(p3);
            
            if (d0 < d1) {
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i0.getX()), new Float(i0.getY())));
    			Punto p = l1.proyeccion(i0, true);
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(p.getX()), new Float(p.getY())));
            } else {
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i1.getX()), new Float(i1.getY())));
    			Punto p = l2.proyeccion(i1, true);
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(p.getX()), new Float(p.getY())));
            }
            	
            if (d2 < d3) {
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i2.getX()), new Float(i2.getY())));
    			Punto p = l2.proyeccion(i2, true);
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(p.getX()), new Float(p.getY())));
            } else {
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i3.getX()), new Float(i3.getY())));
    			Punto p = l1.proyeccion(i3, true);
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(p.getX()), new Float(p.getY())));
            }
            
            double desde = 0;
            if (d0 < d1) desde = l.getDe().distanciaA(l.getA()) - l.getA().distanciaA(p0);
            else desde = l.getDe().distanciaA(l.getA()) - l.getA().distanciaA(p1);
            
            double hasta = Math.min(d2,  d3);
            
            int numEscalones = new Double((hasta - desde) / 30).intValue();

            double anchoEscalon = (hasta - desde) / numEscalones;
            
            for (int i = 0; i < numEscalones; i++) {
            	Punto p = l.getPunto(desde + i * anchoEscalon);
            	Punto px0 = l1.proyeccion(p, true);
            	Punto px1 = l2.proyeccion(p, true);
    			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(px0.getX()), new Float(px0.getY())));
    			segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(px1.getX()), new Float(px1.getY())));
            }
            
		}
		
		 /*
		boolean primero = true;		
        for (AnguloEscalera v : getEscalera().getAngulos()) {
        	if (primero) {
        		primero = false;
                segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(v.getX()), new Float(v.getY())));
        	} else {
                segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(v.getX()), new Float(v.getY())));
        	}
        }
        //segs.appendItem(path.createSVGPathSegClosePath());	
        */
		 
        if (clear) {
            for (SVGRectaEscalera r : rectas) {
            	r.actualizar();
            }
            for (SVGAnguloEscalera r : vertices) {
            	r.actualizar();
            }
        }
	}

	public void marcar() {
		path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#0000ff");
	}
	
	public void desmarcar() {
		path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#121212");
	}

	public Escalera getEscalera() {
		return escalera;
	}

	public void setEscalera(Escalera escalera) {
		this.escalera = escalera;
	}

	
}
