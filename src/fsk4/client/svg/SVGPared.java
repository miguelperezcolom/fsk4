package fsk4.client.svg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.vecmath.Vector2d;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPatternElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import fsk4.client.DibujoHelper;
import fsk4.shared.Linea;
import fsk4.shared.Material;
import fsk4.shared.Pared;
import fsk4.shared.Planta;
import fsk4.shared.Puerta;
import fsk4.shared.Punto;
import fsk4.shared.Vertice;

public class SVGPared extends AbstractSeleccionable {

	private Pared pared;
	private List<Pared> paredes;
	private OMSVGPatternElement pat;
	private OMSVGImageElement img;
	private OMSVGPathElement path;
	private OMSVGPathSegList segs;
	private OMSVGGElement capaBase;
	private OMSVGGElement capaCotas;

	public SVGPared(SVGPlanoCenital svgPlano, List<Pared> paredes, Pared pared) {
		
		super();
		
		this.setPared(pared);
		this.paredes = paredes;
		this.svgPlano = svgPlano;
		
		setId("pared_" + new Date().getTime());
		
		Material r = pared.getRevestimientoInterior().getRevestimiento1();
		
		double patwi = SVGPlano.ESCALA * r.getAncho() / 100;  
		double pathe = SVGPlano.ESCALA * r.getAlto() / 100; 

		pat = SVGHelper.getDoc().createSVGPatternElement();
		svgPlano.getDef().appendChild(pat);
		pat.setId("patp_" + getId());

		
		pat.setAttribute("patternUnits", "userSpaceOnUse");			
		pat.setAttribute("width", "" + patwi);			
		pat.setAttribute("height", "" + pathe);			
		pat.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);
		pat.appendChild(img = SVGHelper.getDoc().createSVGImageElement(0f, 0f, new Float(patwi).floatValue(), new Float(pathe).floatValue(), r.getTextura()));

		path = SVGHelper.getDoc().createSVGPathElement();
        segs = path.getPathSegList();

        //path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#FFF67D");
        //path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1");
        path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#121212");
        path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        //if (Fsk4.isTableta()) path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#BD6500");
        path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#" + pat.getId() + ")");
        path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1");
        //path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#patinteriorparedes)");        
        
        //g.appendChild(path);
        
        
        
        appendChild(capaBase = new OMSVGGElement());
        appendChild(capaCotas = new OMSVGGElement());

		capaBase.appendChild(path);
		
		refrescar();
		 
		path.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (DibujoHelper.texturaEnDrag != null) {
					double patwi = SVGPlano.ESCALA * Double.parseDouble(DibujoHelper.texturaEnDrag.getAttribute("data-ancho")) / 100;  
					double pathe = SVGPlano.ESCALA * Double.parseDouble(DibujoHelper.texturaEnDrag.getAttribute("data-alto")) / 100;  
					
					img.getHref().setBaseVal(DibujoHelper.texturaEnDrag.getAttribute("data-cenital"));
					img.setAttribute("width", "" + patwi);
					img.setAttribute("height", "" + pathe);
					pat.setAttribute("width", "" + patwi);			
					pat.setAttribute("height", "" + pathe);			
					pat.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);
					DibujoHelper.texturaDragginOn = SVGPared.this;
				}
				
			}
		});
		
		path.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (DibujoHelper.texturaEnDrag != null) {						
					Material rs = getPared().getRevestimientoInterior().getRevestimiento1();
					
					double patwi = SVGPlano.ESCALA * rs.getAncho() / 100;  
					double pathe = SVGPlano.ESCALA * rs.getAlto() / 100;  
					
					img.setAttribute("href", rs.getTextura());
					img.setAttribute("width", "" + patwi);
					img.setAttribute("height", "" + pathe);
					img.setAttribute("width", "" + patwi);			
					img.setAttribute("height", "" + pathe);			
					img.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);

					if (DibujoHelper.texturaDragginOn == SVGPared.this) DibujoHelper.texturaDragginOn = null;
				}
				
			}
		});
		
	}
	
	private void fillSegs(OMSVGPathElement path, OMSVGPathSegList segs, Pared antes, Pared despues) {
		
		if (pared.getDe().mismaPosicion(pared.getA())) {
			setAttribute("visibility", "hidden");
		} else {
			setAttribute("visibility", "visible");
			double ancho = pared.getAncho();
			Linea a = (antes != null)?DibujoHelper.getParalela(antes.getDe(), antes.getA(), ancho):null;
			Linea b = DibujoHelper.getParalela(pared.getDe(), pared.getA(), ancho);
			Linea c = (despues != null)?DibujoHelper.getParalela(despues.getDe(), despues.getA(), ancho):null;
			
			List<Punto> puntos = new ArrayList<Punto>();
			puntos.add(new Punto(pared.getDe().getX(), pared.getDe().getY()));
			double ab = b.direction().angle(new Vector2d(1,0));
			double aa = (a != null)?a.direction().angle(new Vector2d(1,0)):ab;
			double ac = (c != null)?c.direction().angle(new Vector2d(1,0)):ab;
			if (Math.abs(aa - ab) > 0.1 && Math.abs(aa + ab) > 0.1) {
				Punto xxx = a.intersection(b, false);
				if (xxx != null) puntos.add(a.intersection(b, false));
			} else {
				puntos.add(DibujoHelper.getPuntoPerpendicular(pared.getDe(), pared.getDe(), pared.getA(), ancho));
			}
			if (Math.abs(ab - ac) > 0.1 && Math.abs(ab + ac) > 0.1) {
				Punto xxx = b.intersection(c, false);
				if (xxx != null)  puntos.add(b.intersection(c, false));
			} else {
				puntos.add(DibujoHelper.getPuntoPerpendicular(pared.getA(), pared.getDe(), pared.getA(), ancho));
			}
			puntos.add(new Punto(pared.getA().getX(), pared.getA().getY()));
			puntos.add(new Punto(pared.getDe().getX(), pared.getDe().getY()));
			
			segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(puntos.get(0).getX()), new Float(puntos.get(0).getY())));
			for (int pos = 0; pos < puntos.size() - 1; pos++) {
		        segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(puntos.get(pos + 1).getX()), new Float(puntos.get(pos + 1).getY())));
			}
	         
	         segs.appendItem(path.createSVGPathSegClosePath());	
		}
   }
	
	@Override
	public void arrastradoA(float x, float y) {
			Pared siguientePared = getSiguientePared();
			Pared paredAnterior = getParedAnterior();
			
			Linea lanterior = null;
			Linea lsiguiente = null;
			
			//antes creamos un nuevo vertice y una nueva pared si hacen falta
			if (paredAnterior != null) {
				desdoblarSiNecesario(pared, paredAnterior);
				lanterior = new Linea(paredAnterior);
			} else {
				lanterior = new Linea(pared).perpendicularQuePasaPor(new Punto(pared.getDe()));
			}

			if (siguientePared != null) {
				desdoblarSiNecesario(pared, siguientePared);
				lsiguiente = new Linea(siguientePared);
			} else {
				lsiguiente = new Linea(pared).perpendicularQuePasaPor(new Punto(pared.getA()));
			}
			
			Linea l0 = new Linea(new Punto(pared.getDe()), new Punto(pared.getA()));
			Linea l = l0.paralelaQuePasaPor(new Punto(x, y));
			Punto nde = l.intersection(lanterior, true);
			Punto na = l.intersection(lsiguiente, true);
			
			pared.getDe().setX(nde.getX());
			pared.getDe().setY(nde.getY());
			pared.getA().setX(na.getX());
			pared.getA().setY(na.getY());
			
			SVGVerticePared sde = (SVGVerticePared) svgPlano.payload.get(pared.getDe());
			sde.actualizar();
			SVGVerticePared sa = (SVGVerticePared) svgPlano.payload.get(pared.getA());
			sa.actualizar();								
			
			if (siguientePared != null) unirSiPosible(pared, siguientePared);
			if (paredAnterior != null) unirSiPosible(pared, paredAnterior);

			((SVGPlanoCenital)svgPlano).repintarSuelo();
			
	}
	
	

	private Pared getParedAnterior() {
		boolean cerrado = false;
		if (paredes.size() > 2) {
			Vertice v0 = paredes.get(0).getDe();
			Vertice v1 = paredes.get(paredes.size() - 1).getA();
			cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
		}
        int pos = paredes.indexOf(pared);
		return (pos == 0)?((cerrado)?paredes.get(paredes.size() - 1):null):paredes.get(pos - 1);
	}

	private Pared getSiguientePared() {
		boolean cerrado = false;
		if (paredes.size() > 2) {
			Vertice v0 = paredes.get(0).getDe();
			Vertice v1 = paredes.get(paredes.size() - 1).getA();
			cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
		}
        int pos = paredes.indexOf(pared);
		return (pos == paredes.size() - 1)?((cerrado)?paredes.get(0):null):paredes.get(pos + 1);
	}

	private void desdoblarSiNecesario(Pared p0, Pared p1) {
		Linea l0 = new Linea(p0);
		Linea l1 = new Linea(p1);
		if (l0.esParalela(l1)) {
			// son paralelas --> son la misma línea
			if (l0.getA().mismaPosicion(l1.getDe())) {
				dividirPared(p1, new Vertice(p1.getDe()));
			} else if (l0.getDe().mismaPosicion(l1.getA())) {
				dividirPared(p1, new Vertice(p1.getA()));
			}
		}
	}
	
	public void dividirPared(Pared p, Vertice nuevoVertice) {
		
		nuevoVertice.setZ((p.getDe().getZ() + p.getA().getZ()) / 2f);
		
		Material m = new Material(p.getRevestimientoInterior().getRevestimiento1().getId(), p.getRevestimientoInterior().getRevestimiento1().getTipo(), p.getRevestimientoInterior().getRevestimiento1().getColor(), p.getRevestimientoInterior().getRevestimiento1().getTextura(), p.getRevestimientoInterior().getRevestimiento1().getAncho(), p.getRevestimientoInterior().getRevestimiento1().getAlto());
		Pared p2;
		
		SVGPlanoCenital pl = (SVGPlanoCenital)svgPlano;
		
		pl.paredesPorVertice.get(p.getA()).remove(this);
		if (!pl.paredesColindantesPorVertice.get(p.getA()).contains(this)) pl.paredesColindantesPorVertice.get(p.getA()).add(this);

		paredes.add(paredes.indexOf(p) + 1, p2 = new Pared(nuevoVertice, p.getA()));
		p2.setRevestimientoInterior(p.getRevestimientoInterior().clone());
		p2.setRevestimientoExterior(p.getRevestimientoExterior().clone());
		p2.setAncho(p.getAncho());
		p.setA(nuevoVertice);
		
		List<SVGPared> l = pl.paredesPorVertice.get(p.getA());
		if (l == null) {
			l = new ArrayList<SVGPared>();
			pl.paredesPorVertice.put(p.getA(), l);
		}
		if (!l.contains(this)) l.add(this);
		
		
		((SVGPlanoCenital)svgPlano).addSVGPared(paredes, p2);
		
		
		refrescar();
	}


	private void unirSiPosible(Pared p0, Pared p1) {
		if (p0 != null && p1 != null) {
			Linea l0 = new Linea(p0);
			Linea l1 = new Linea(p1);
			if (l0.esParalela(l1)) {
				// son paralelas --> son la misma línea
				unirPared(p0, p1);
			}
		}
	}


	private void unirPared(Pared p0, Pared p1) {
		if (paredes.indexOf(p0) < paredes.indexOf(p1)) {
			p0.setA(p1.getA());			
		} else {
			// p1 va antes que p0
			p0.setDe(p1.getDe());
		}
		paredes.remove(p1);
		((SVGPlanoCenital)svgPlano).eliminarDeSvg(p1);
		p0.getPuertas().addAll(p1.getPuertas());
		for (Puerta px : p1.getPuertas()) px.setPared(p0);
		((SVGPlanoCenital)svgPlano).eliminarDeSvg(p1.getDe());
	}



	@Override
	public float getX() {
		return pared.getDe().getX();
	}

	@Override
	public float getY() {
		return pared.getDe().getY();
	}

	public void refrescar() {
		boolean cerrado = false;
		if (paredes.size() > 2) {
			Vertice v0 = paredes.get(0).getDe();
			Vertice v1 = paredes.get(paredes.size() - 1).getA();
			cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
		}
		
		
        int pos = paredes.indexOf(pared);
		Pared panterior = (pos == 0)?((cerrado)?paredes.get(paredes.size() - 1):null):paredes.get(pos - 1);
		Pared psiguiente = (pos == paredes.size() - 1)?((cerrado)?paredes.get(0):null):paredes.get(pos + 1);
		OMSVGPathSegList segs = path.getPathSegList();
		segs.clear();
		
        fillSegs(path, segs, panterior, psiguiente);

        //getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, (pared.isVisible() && !pared.getDe().mismaPosicion(pared.getA()))?"1":"0.5");
        
        actualizarCotas(pared);
	}

	public Pared getPared() {
		return pared;
	}

	public void setPared(Pared pared) {
		this.pared = pared;
	}

	@Override
	public void seleccionado() {
		DibujoHelper.seleccionado = getPared();
	}

	@Override
	public String getIdTrozoInfo() {
		return "info-pared";
	}

	
	@Override
	public String getTransformString() {
		String auxs = "translate(0,0)";
		return auxs;
	}
	
	private void actualizarCotas(Pared px) {
		if (px.getDe().mismaPosicion(px.getA())) {
			capaCotas.setAttribute("visibility", "hidden");
		} else {
			capaCotas.setAttribute("visibility", "visible");
			
			SVGHelper.vaciar(capaCotas);
			
			
			Punto p0 = DibujoHelper.getPuntoPerpendicular(px.getDe(), px.getDe(), px.getA(), 15 + px.getAncho());
			Punto p1 = DibujoHelper.getPuntoPerpendicular(px.getA(), px.getDe(), px.getA(), 15 + px.getAncho());

			OMSVGLineElement l;
			capaCotas.appendChild(l = SVGHelper.getDoc().createSVGLineElement(new Float(px.getDe().getX()).floatValue(), new Float(px.getDe().getY()).floatValue(), new Float(p0.getX()).floatValue(), new Float(p0.getY()).floatValue()));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
	        capaCotas.appendChild(l = SVGHelper.getDoc().createSVGLineElement(new Float(px.getA().getX()).floatValue(), new Float(px.getA().getY()).floatValue(), new Float(p1.getX()).floatValue(), new Float(p1.getY()).floatValue()));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			
			p0 = DibujoHelper.getPuntoPerpendicular(px.getDe(), px.getDe(), px.getA(), 10 + px.getAncho());
			p1 = DibujoHelper.getPuntoPerpendicular(px.getA(), px.getDe(), px.getA(), 10 + px.getAncho());

			capaCotas.appendChild(l = SVGHelper.getDoc().createSVGLineElement(new Float(p0.getX()).floatValue(), new Float(p0.getY()).floatValue(), new Float(p1.getX()).floatValue(), new Float(p1.getY()).floatValue()));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			
			p0 = DibujoHelper.getPuntoPerpendicular(px.getDe(), px.getDe(), px.getA(), 12 + px.getAncho());
			p1 = DibujoHelper.getPuntoPerpendicular(px.getA(), px.getDe(), px.getA(), 12 + px.getAncho());

			OMSVGTextElement t = SVGHelper.getDoc().createSVGTextElement(0f, 0f, OMSVGLength.SVG_LENGTHTYPE_PX, 
					"" + (Math.round(100d * Math.sqrt(((px.getA().getX() - px.getDe().getX()) * (px.getA().getX() - px.getDe().getX())) + ((px.getA().getY() - px.getDe().getY()) * (px.getA().getY() - px.getDe().getY()))) / DibujoHelper.ESCALA) / 100d) + "m."); // + new Linea(px).angulo() );
			t.setAttribute("pointer-events", "none");
			//t.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "0");
			
			capaCotas.appendChild(t);

			p0 = DibujoHelper.getPuntoPerpendicular(new Vertice((px.getA().getX() + px.getDe().getX()) / 2 , (px.getA().getY() + px.getDe().getY()) / 2), px.getDe(), px.getA(), 15 + px.getAncho());
			
			float a = new Linea(px).angulo();
			if (a < 0 && a >= -180) p0.setX(p0.getX() - t.getComputedTextLength());
			if (a < -90 && a >= -180 || a > 90 && a <= 180) p0.setY(p0.getY() + 20);
			
			String auxs = "translate(" + p0.getX() + "," + p0.getY() + ")";
			//auxs += " rotate(" + ((IRotable)p).getPan() + " 0 0)";
			auxs += " rotate(0 0 0)";
			t.setAttribute("transform", auxs);
		}

	}
}
