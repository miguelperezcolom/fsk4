package fsk4.client.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPatternElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.events.HasGraphicalHandlers;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import fsk4.client.DibujoHelper;
import fsk4.client.PantallaHelper;
import fsk4.shared.Linea;
import fsk4.shared.Material;
import fsk4.shared.Pared;
import fsk4.shared.Planta;
import fsk4.shared.Punto;
import fsk4.shared.Vertice;

/**
 * Tiene variables para guardar las paredes colindantes por vértice de 1er y de segundo nivel
 * 
 * Intercepta el mouseDown para crear las paredes o añadir puntos a un volumen
 * 
 * @author miguel
 *
 */
public class SVGPlanoCenital extends SVGPlano {

	private OMSVGImageElement imgPatronSuelo;
	private OMSVGPatternElement patSuelo;
	private OMSVGPathSegList segsSuelo;
	private OMSVGPathElement pathSuelo;
	public Map<Vertice, List<SVGPared>> paredesPorVertice;
	public Map<Vertice, List<SVGPared>> paredesColindantesPorVertice;

	public SVGPlanoCenital(Planta planta) {
		super(planta);		
	}

	@Override
	public void asignarImagenFondo(double patwi, double pathe) {
		//if (getPlano().getHabitaciones().indexOf(getPlanta()) == 0) patExterior.appendChild(imgPatronExterior = SVGHelper.getDoc().createSVGImageElement(0f, 0f, new Float(patwi).floatValue(), new Float(pathe).floatValue(), "fileviewer/18/cesped3.jpg"));
	}

	@Override
	public void crearBook() {
		
		// SUELO
		
		Material rs = getPlanta().getHabitaciones().get(0).getRevestimientoSuelo();
		
		double patwi = ESCALA * rs.getAncho() / 100;  
		double pathe = ESCALA * rs.getAlto() / 100;  
		
		getDef().appendChild(patSuelo= SVGHelper.getDoc().createSVGPatternElement());
		patSuelo.setId("patx");
		
		patSuelo.setAttribute("patternUnits", "userSpaceOnUse");			
		patSuelo.setAttribute("width", "" + patwi);			
		patSuelo.setAttribute("height", "" + pathe);			
		patSuelo.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);			
		patSuelo.appendChild(imgPatronSuelo = SVGHelper.getDoc().createSVGImageElement(0f, 0f, new Float(patwi).floatValue(), new Float(pathe).floatValue(), rs.getTextura()));

		
		
		// PLANTA ABAJO
		if (getPlano().getPlantas().indexOf(getPlanta()) > 0) {
			final OMSVGGElement g = SVGHelper.getDoc().createSVGGElement();
			g.setAttribute("zzzzzz", "edueduehedhdhe");
			OMSVGPathElement path = SVGHelper.getDoc().createSVGPathElement();
            OMSVGPathSegList segs = path.getPathSegList();
            
            Planta p0 = getPlano().getPlantas().get(getPlano().getPlantas().indexOf(getPlanta()) - 1);
    		boolean primero = true;	    		
            for (Pared p : p0.getParedes()) {
            	Linea l1 = new Linea(p).paralela(-1 * p.getAncho());
            	Pared sp = p0.getSiguientePared(p);
            	Linea l2 = new Linea(sp).paralela(-1 * sp.getAncho());
            	Punto i = l1.intersection(l2, true);
            	if (primero) {
            		primero = false;
                    segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(i.getX()), new Float(i.getY())));
            	} else {
                    segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(i.getX()), new Float(i.getY())));
            	}
            }
            segs.appendItem(path.createSVGPathSegClosePath());	

            g.appendChild(path);
            
            path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
            path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1");
            path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_OPACITY_PROPERTY, "1");
            capaParedes.appendChild(g);
		}
		
		final OMSVGGElement g = SVGHelper.getDoc().createSVGGElement();
		pathSuelo = SVGHelper.getDoc().createSVGPathElement();
        segsSuelo = pathSuelo.getPathSegList();
        fillSegs(pathSuelo, segsSuelo, getPlanta());
        pathSuelo.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#patx)");
        pathSuelo.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1");
        //path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
        //path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#e6a0c5");
		
        g.appendChild(pathSuelo);
         
        
		((HasGraphicalHandlers)pathSuelo).addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				
				if (DibujoHelper.texturaEnDrag != null) {						
					double patwi = ESCALA * Double.parseDouble(DibujoHelper.texturaEnDrag.getAttribute("data-ancho")) / 100;  
					double pathe = ESCALA * Double.parseDouble(DibujoHelper.texturaEnDrag.getAttribute("data-alto")) / 100;  

					imgPatronSuelo.getHref().setBaseVal(DibujoHelper.texturaEnDrag.getAttribute("data-cenital"));
					imgPatronSuelo.setAttribute("width", "" + patwi);			
					imgPatronSuelo.setAttribute("height", "" + pathe);
					patSuelo.setAttribute("width", "" + patwi);			
					patSuelo.setAttribute("height", "" + pathe);			
					patSuelo.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);
					DibujoHelper.texturaDragginOn = getPlanta();
				}
				
			}
		});
		
		((HasGraphicalHandlers)pathSuelo).addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				
				if (DibujoHelper.texturaEnDrag != null) {						
					Material rs = getPlanta().getHabitaciones().get(0).getRevestimientoSuelo();
					
					double patwi = ESCALA * rs.getAncho() / 100;  
					double pathe = ESCALA * rs.getAlto() / 100;  
					
					imgPatronSuelo.getHref().setBaseVal(rs.getTextura());
					imgPatronSuelo.setAttribute("width", "" + patwi);
					imgPatronSuelo.setAttribute("height", "" + pathe);
					patSuelo.setAttribute("width", "" + patwi);			
					patSuelo.setAttribute("height", "" + pathe);			
					patSuelo.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);
					
					if (DibujoHelper.texturaDragginOn == getPlanta()) DibujoHelper.texturaDragginOn = null;
				}
				
			}
		});

        
		capaParedes.appendChild(g);
		
        
        // PAREDES
		
		paredesPorVertice = new HashMap<Vertice, List<SVGPared>>();
		paredesColindantesPorVertice = new HashMap<Vertice, List<SVGPared>>();

		
		for (List<Pared> pz : getPlanta().getTodasParedes()) {
			for (final Pared px : pz) {
				addSVGPared(pz, px);
			}
		}
		
	}

	public void repintarSuelo() {
		fillSegs(pathSuelo, segsSuelo, getPlanta());
	}

	private static void fillSegs(OMSVGPathElement path, OMSVGPathSegList segs, Planta h) {
		
		segs.clear();
		
		boolean primero = true;
        for (Pared p : h.getParedes()) {
        	if (primero) {
        		primero = false;
                segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(p.getDe().getX()), new Float(p.getDe().getY())));
        	} else {
                segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(p.getDe().getX()), new Float(p.getDe().getY())));
        	}
        }
        segs.appendItem(path.createSVGPathSegClosePath());	
    }

	@Override
	public void crearContenido() {
		
		SVGCamara s;
		capaCamara.appendChild(s = new SVGCamara(this, getPlano().getCamara()));
		payload.put(getPlano().getCamara(), s);
		
		// TODO Auto-generated method stub
		
	}

	public void addSVGVertice(Vertice nuevoVertice) {
		SVGVerticePared s;
		capaVertices.appendChild(s = new SVGVerticePared(this, nuevoVertice));
		payload.put(nuevoVertice, s);
	}

	public void addSVGPared(List<Pared> paredes, Pared pared) {
		// crea el svg y lo añade a la capa que contiene todas las paredes
		SVGPared s;
		capaParedes.appendChild(s = new SVGPared(this, paredes,pared));		
		payload.put(pared, s);
		
		if (paredesPorVertice == null) {
			SVGHelper.log("paredesPorVertice es null");
		} else {
			SVGHelper.log("paredesPorVertice NO es null");
		}
		
		// añade el svg a la lista de paredes que contienen cada vertice de la pared
		for (Vertice v : new Vertice[] { pared.getDe(), pared.getA()}) {
			List<SVGPared> l = paredesPorVertice.get(v);
			if (l == null) {
				l = new ArrayList<SVGPared>();
				paredesPorVertice.put(v, l);
			}
			if (!l.contains(s)) l.add(s);
		}

		//Pared anterior = getPlanta().getParedAnterior(pared);
		//Pared siguiente = getPlanta().getSiguientePared(pared);
		
		// añade el svg a la lista de paredes colindantes de 2º nivel con las paredes de contienen el vértice		
		for (Pared px : paredes) for (Vertice v : new Vertice[] {px.getDe(), px.getA()}) if (paredesPorVertice.get(v) != null) { // puede que todavía no hayamos añadido esta pared (por ejemplo cuando inicializamos el mapa)
			List<SVGPared> colindantes = new ArrayList<SVGPared>();
			for (SVGPared p : paredesPorVertice.get(v)) {
				if (!p.getPared().getDe().equals(v) && paredesPorVertice.get(p.getPared().getDe()) != null) colindantes.addAll(paredesPorVertice.get(p.getPared().getDe()));
				if (!p.getPared().getA().equals(v) && paredesPorVertice.get(p.getPared().getA()) != null) colindantes.addAll(paredesPorVertice.get(p.getPared().getA()));
			}		
			paredesColindantesPorVertice.put(v, colindantes);
		}
		
		// añade los svgs de las paredes
		for (Vertice v : new Vertice[] { pared.getDe(), pared.getA()}) if (!payload.containsKey(v)) {
			SVGVerticePared sv;
			capaVertices.appendChild(sv = new SVGVerticePared(this, v));
			payload.put(v, sv);
		}
	}

	public void eliminarDeSvg(Pared pared) {
		capaParedes.removeChild(payload.remove(pared));
		SVGHelper.eliminarPayload(pared);
	}

	public void eliminarDeSvg(Vertice v) {
		capaVertices.removeChild(payload.remove(v));
		SVGHelper.eliminarPayload(v);
	}

	public void arrastrarParedA(Pared pared, Punto punto) {
		((SVGPared)payload.get(pared)).arrastradoA(punto.getX(), punto.getY());
	}

	
	@Override
	public void md(int clientX, int clientY) {
		SVGHelper.log("md en SVGPlanoCenital");//TODO: añadir capa para controlar md y creación volúmen
		OMSVGPoint p = trasladar(clientX, clientY);
		if (SVGHelper.creandoParedes) {
			
			if (SVGHelper.nuevasParedes.size() > 1 && new Linea(SVGHelper.nuevasParedes.get(SVGHelper.nuevasParedes.size() - 1)).longitud() == 0) {
				SVGHelper.log("la última línea mide 0. --> fin crear paredes");
				SVGHelper.finCrearParedes();
			} else if (SVGHelper.nuevasParedes.size() > 1 && SVGHelper.nuevasParedes.get(SVGHelper.nuevasParedes.size() - 1).getA().mismaPosicion(SVGHelper.nuevasParedes.get(0).getDe())) {
				SVGHelper.log("md en un punto ya fijo, que es el primer vértice. --> fin crear paredes");
				// unir principio y final
				Pared primeraPared = SVGHelper.nuevasParedes.get(0);
				Pared ultimaPared = SVGHelper.nuevasParedes.get(SVGHelper.nuevasParedes.size() - 1);
				Vertice old = ultimaPared.getA();
				ultimaPared.setA(primeraPared.getDe());
				for (SVGPared px : paredesPorVertice.get(old)) if (!paredesPorVertice.get(ultimaPared.getA()).contains(px)) paredesPorVertice.get(ultimaPared.getA()).add(px);
				for (SVGPared px : paredesColindantesPorVertice.get(old)) if (!paredesColindantesPorVertice.get(ultimaPared.getA()).contains(px)) paredesColindantesPorVertice.get(ultimaPared.getA()).add(px);
				paredesColindantesPorVertice.get(ultimaPared.getDe()).add((SVGPared) payload.get(primeraPared));
				paredesColindantesPorVertice.get(primeraPared.getA()).add((SVGPared) payload.get(ultimaPared));
				((SVGPared) payload.get(primeraPared)).refrescar();
				((SVGPared) payload.get(ultimaPared)).refrescar();
				
				eliminarDeSvg(old);				
				
				SVGHelper.finCrearParedes();
			} else {
				
				imantar(p);
				
				Vertice de = null;
				if (SVGHelper.nuevasParedes.size() == 0) {
					de = new Vertice(p.getX(), p.getY());
				} else {
					de = SVGHelper.nuevasParedes.get(SVGHelper.nuevasParedes.size() - 1).getA();
				}
				Pared nuevaPared;
				Vertice ultimoVertice;
				SVGHelper.nuevasParedes.add(nuevaPared = new Pared(de, ultimoVertice = new Vertice(p.getX(), p.getY())));
				SVGHelper.addSVGPared(SVGHelper.nuevasParedes, nuevaPared);
				SVGHelper.svgArrastrado = (AbstractArrastable) SVGHelper.svgMarco.svgPlano.payload.get(ultimoVertice);
			}
			
		} else super.md(clientX, clientY);
	}
}
