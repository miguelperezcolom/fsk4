package fsk4.client.svg;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;

import fsk4.client.DibujoHelper;
import fsk4.client.Fsk4;
import fsk4.shared.Linea;
import fsk4.shared.Pared;
import fsk4.shared.Planta;
import fsk4.shared.Punto;
import fsk4.shared.Vertice;

public class SVGHelper {
	

	private static Queue<Date> rs = new LinkedList<Date>();	
	private static RepeatingCommand srp;
	private static Queue<EventoMouse> mms = new LinkedList<EventoMouse>();
	private static RepeatingCommand smm;

	
	private static OMSVGDocument doc;
	private static OMSVGSVGElement svg;

	public static SVGMarco svgMarco;

	protected static AbstractArrastable svgArrastrado;

	
	public static boolean creandoParedes;
	public static List<Pared> nuevasParedes;

	
	public static void crearSvg() {
		setDoc(OMSVGParser.currentDocument());
		setSvg(getDoc().createSVGSVGElement());
		getSvg().setWidth(OMSVGLength.SVG_LENGTHTYPE_PX, 3000);
		getSvg().setHeight(OMSVGLength.SVG_LENGTHTYPE_PX, 2000);

		/*
		OMSVGCircleElement circle = doc.createSVGCircleElement(150, 150, 100);
		circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "blue");
		svg.appendChild(circle);
		 */
		$("#canvas").html("");		
		$("#canvas").elements()[0].appendChild(getSvg().getElement());		


		getSvg().addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				event.preventDefault();
				event.stopPropagation();

				mm(new EventoMouse(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY(), event.getNativeButton() == NativeEvent.BUTTON_LEFT));
			}
		});

		getSvg().addTouchMoveHandler(new TouchMoveHandler() {
			
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				event.preventDefault();
				event.stopPropagation();
				
				if (event.getTouches() != null && event.getTouches().length() > 0) {
					Touch t = event.getTouches().get(0);
					mm(new EventoMouse(t.getClientX(), t.getClientY(), true));
				}

			}
		});

		
		
		if (smm == null) Scheduler.get().scheduleFixedDelay(smm = new Scheduler.RepeatingCommand() {
			
			@Override
			public boolean execute() {
				try {
					if (mms.size() > 0) {
						EventoMouse nxy;
						EventoMouse lxy = null;
						while ((nxy = mms.poll()) != null) lxy = nxy;
						if (lxy != null) {
							mmx(lxy);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		}, 20);
		
		
	}

	public static void mm(EventoMouse e) {
		mms .add(e);
	}

	protected static void mmx(EventoMouse e) {
		//SVGHelper.log("mmx(" + e.getX() + "," + e.getY() + ")");
		if (svgArrastrado != null) {			
			svgArrastrado.mmx(e);
		}
		//ghelp.setAttribute("transform", "translate(" + (clientX + 10) + "," + (clientY + 10) + ")");
	}

	
	
	
	
	public static void log(String text) {
		$("#log").html(text + "<br/>" + $("#log").html());
	}

	public static OMSVGDocument getDoc() {
		return doc;
	}

	public static void setDoc(OMSVGDocument doc) {
		SVGHelper.doc = doc;
	}

	public static OMSVGSVGElement getSvg() {
		return svg;
	}

	public static void setSvg(OMSVGSVGElement svg) {
		SVGHelper.svg = svg;
	}

	public static void dibujar(Planta planta) {
		getSvg().appendChild(svgMarco = new SVGMarco());
		svgMarco.setPlanta(planta);
	}

	public static void mu(int clientX, int clientY) {
		if (svgArrastrado != null && !creandoParedes) {
			svgArrastrado = null;
			log("fin arrastre");			
		}
		SVGHelper.ocultarGuias();
		DibujoHelper.refrescaPreview();
	}

	public static void zoom() {
		svgMarco.zoom();
	}

	
	public static OMSVGPoint globalToPlano(int x02, int y02) {
		return globalToPlano(new Float(x02), new Float(y02));
	}
	
	public static OMSVGPoint globalToPlano(float x02, float y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(svgMarco.svgPlano.getScreenCTM().inverse());
		return roomPoint;
	}

	public OMSVGPoint planoToGlobal(float x02, float y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(svgMarco.svgPlano.getScreenCTM());
		return roomPoint;
	}

	public OMSVGPoint planoToGlobal(OMSVGPoint punto) {
		return planoToGlobal(punto.getX(), punto.getY());
	}

	public OMSVGPoint planoToGlobal(int x, int y) {
		return planoToGlobal(new Float(x), new Float(y));
	}

	public static void vaciar(OMSVGGElement g) {
		List<OMNode> zz = new ArrayList<OMNode>();
		for (OMNode z : g.getChildNodes()) zz.add(z);
		for (OMNode z : zz) g.removeChild(z);
	}

	public static void marcarSeleccion() {
		for (OMSVGGElement g : svgMarco.svgPlano.getCapas()) for (OMNode x : g.getChildNodes()) {
			if (x instanceof OMSVGGElement) {
				//SVGHelper.log("marcando " + x.getClass().getName());
				OMSVGGElement gx = (OMSVGGElement) x;
				if (svgArrastrado != null && !(svgArrastrado instanceof SVGPlano) && gx != svgArrastrado) {
					try {
						if (gx.getAttribute("old-opacity") == null && gx.getStyle().getSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY)!= null) gx.setAttribute("old-opacity", gx.getStyle().getSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY));
						gx.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.1");
					} catch (Exception e) {
						SVGHelper.log("ERROR: " + e.getClass().getName() + ":" + e.getMessage());
					}
				} else {
					gx.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, (gx.getAttribute("old-opacity") != null)?gx.getAttribute("old-opacity"):"1");
				}
				//SVGHelper.log("fin marcado " + x.getClass().getName());
			}
		}
	}
	
	public static void desmarcarSeleccion() {
		svgArrastrado = null;
		marcarSeleccion();
	}


	public static void mostrarGuias(float x, float y) {
		svgMarco.mostrarGuias(x, y);
	}

	public static void ocultarGuias() {
		if (svgMarco != null) svgMarco.ocultarGuias();
	}

	public static void addSVGPared(List<Pared> paredes, Pared pared) {
		svgMarco.addSVGPared(paredes, pared);
	}

	public static void arrastrarParedA(Pared pared, Punto punto) {
		svgMarco.arrastrarParedA(pared, punto);
	}

	public static void inicioCrearParedes() {
		//finCrearVolumen();
		//finCrearParedes();
		//finCrearEscalera();
		
		SVGHelper.creandoParedes = true;		
		SVGHelper.nuevasParedes = new ArrayList<Pared>();
		getPlanta().getParedesInteriores().add(nuevasParedes);

		svgMarco.svgPlano.setAttribute("cursor", "crosshair");
		
		//ghelp.setText("Ir haciendo click ara crear paredes. Click de nuevo sobre alguno de los vértices para terminar");
	}

	public static Planta getPlanta() {
		return DibujoHelper.getPlanta();
	}

	public static void finCrearParedes() {

		if (creandoParedes) {
			
			List<Pared> borrar = new ArrayList<Pared>();
			for (Pared p : nuevasParedes) {
				if (new Linea(p).longitud() == 0) {
					borrar.add(p);
				}
			}
			if (borrar.size() > 0) {
				List<Object> borrarPayloads = new ArrayList<Object>();
				for (Pared x : borrar) {
					for (Pared p : nuevasParedes) if (x.equals(p)) {
						boolean cerrado = nuevasParedes.get(0).getDe().equals(nuevasParedes.get(nuevasParedes.size() - 1).getA());
						Pared anterior = getAnterior(nuevasParedes, cerrado, p);
						Pared siguiente = getSiguiente(nuevasParedes, cerrado, p);
						if (anterior == null && siguiente == null) {
							// eliminar a, b, pared
							borrarPayloads.add(p.getDe());
							borrarPayloads.add(p.getA());
							borrarPayloads.add(p);							
						} else if (anterior != null && siguiente != null) {
							// siguiente.de = anterior.a
							siguiente.setDe(anterior.getA());
							// eliminar b, pared
							borrarPayloads.add(p.getA());
							borrarPayloads.add(p);							
						} else if (anterior != null && siguiente == null) {
							// eliminar b, pared
							borrarPayloads.add(p.getA());
							borrarPayloads.add(p);							
						} else if (anterior == null && siguiente != null) {
							// eliminar a, pared
							borrarPayloads.add(p.getDe());
							borrarPayloads.add(p);							
						}
					}
				}
				for (Object x : borrarPayloads) {
					eliminar(x);
				}
			}
			nuevasParedes.removeAll(borrar);
			
		}
		
		creandoParedes = false;
		nuevasParedes = null;		
		svgArrastrado = null;
		$("#botonparedes").removeClass("active");
		
		svgMarco.svgPlano.setAttribute("cursor", "auto");

		DibujoHelper.refrescaPreview();
	}

	
	public static void eliminar(Object x) {
		SVGPlano p = svgMarco.svgPlano;
		if (p instanceof SVGPlanoCenital) {
			if (x instanceof Vertice) {
				((SVGPlanoCenital)p).eliminarDeSvg((Vertice)x);
			} else if (x instanceof Pared) {
				((SVGPlanoCenital)p).eliminarDeSvg((Pared)x);
			}
		}

	}
	
	public static void eliminarPayload(Object x) {
		SVGPlano p = svgMarco.svgPlano;
		if (x instanceof Vertice) {
			//p.capaVertices.removeChild(p.payload.get(x));
			if (p instanceof SVGPlanoCenital) {
				((SVGPlanoCenital)p).paredesPorVertice.remove(x);
				((SVGPlanoCenital)p).paredesColindantesPorVertice.remove(x);
			}
		} else if (x instanceof Pared) {
			//p.capaParedes.removeChild(p.payload.get(x));
			if (p instanceof SVGPlanoCenital) {
				for(List<SVGPared> l : ((SVGPlanoCenital)p).paredesPorVertice.values()) l.remove(x);
				for (List<SVGPared> l : ((SVGPlanoCenital)p).paredesColindantesPorVertice.values()) l.remove(x);
			}
		}

	}


	private static Pared getSiguiente(List<Pared> paredes, boolean cerrado, Pared p) {		
		Pared s = null;
		if (paredes.indexOf(p) < paredes.size() - 1) {
			s = paredes.get(paredes.indexOf(p) + 1);
		} else if (cerrado) {
			s = paredes.get(0);
		}
		return s;
	}

	private static Pared getAnterior(List<Pared> paredes, boolean cerrado, Pared p) {
		Pared a = null;
		if (paredes.indexOf(p) > 0) {
			a = paredes.get(paredes.indexOf(p) - 1);
		} else if (cerrado) {
			a = paredes.get(paredes.size() - 1);
		}
		return a;
	}

	public static void inicioCrearVolumen() {
		// TODO Auto-generated method stub
		
	}

	public static void finCrearVolumen() {
		// TODO Auto-generated method stub
		
	}
	

}
