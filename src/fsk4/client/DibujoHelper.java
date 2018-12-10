package fsk4.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.vecmath.Vector2d;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDefsElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPatternElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.events.HasGraphicalHandlers;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fsk.server.modelo.Columna;
import fsk4.client.recursos.Recursos;
import fsk4.client.svg.SVGHelper;
import fsk4.shared.AnguloEscalera;
import fsk4.shared.Camara;
import fsk4.shared.Escalera;
import fsk4.shared.Foto;
import fsk4.shared.FuenteLuz;
import fsk4.shared.Hueco;
import fsk4.shared.IPosicionable;
import fsk4.shared.IRotable;
import fsk4.shared.InfoArticulo;
import fsk4.shared.Linea;
import fsk4.shared.Material;
import fsk4.shared.Modelo;
import fsk4.shared.Objeto;
import fsk4.shared.Pared;
import fsk4.shared.Plano;
import fsk4.shared.Planta;
import fsk4.shared.Puerta;
import fsk4.shared.Punto;
import fsk4.shared.RevestimientoPared;
import fsk4.shared.Sol;
import fsk4.shared.Textura;
import fsk4.shared.TramoEscalera;
import fsk4.shared.Ventana;
import fsk4.shared.Vertice;
import fsk4.shared.Volumen;
import fsk4.shared.ZoomCamara;

public class DibujoHelper {
	 
	public static final float ESCALA = 100;

	private static Queue<Date> rs = new LinkedList<Date>();	
	private static RepeatingCommand srp;
	private static Queue<int[]> mms = new LinkedList<int[]>();
	private static RepeatingCommand smm;
	 
	static List<HandlerRegistration> bmuhs = new ArrayList<HandlerRegistration>();

	private static OMSVGDocument doc;
	private static OMSVGSVGElement svg;
	static OMSVGGElement svggarrastrado; 
	
	private static OMSVGElement gparedes;
	private static OMSVGElement gcotas;
	private static OMSVGElement gvertices;
	static OMSVGGElement gplano;

	
	private static Map<OMSVGGElement, OMSVGElement> rotadores = new HashMap<OMSVGGElement, OMSVGElement>();
	private static Map<OMSVGElement, OMSVGGElement> rotados = new HashMap<OMSVGElement, OMSVGGElement>();
	public static Object seleccionado;
	
	static Map<Object, Object> payload;
	public static Map<Object, Object> payloadAlzado;
	static Map<Escalera, SVGEscalera> svgsEscaleras;
	private static SVGAnguloEscalera svgAnguloEscaleraArrastrado;
	private static SVGRectaEscalera svgRectaEscaleraArrastrada;
	
	private static Plano plano = Plano.crearPlanoInicial(Plano.PLANO_CUADRADO);
	private static Planta planta;
	
	private static Map<Object, Object> paths;
	private static Map<Object, Object> cotas;
	
	private static int clientX0;
	private static int clientY0;
	protected static float arrastradoX0;
	protected static float arrastradoY0;
	protected static float arrastradoX01;
	protected static float arrastradoY01;	
	protected static double camaraPan0;
	private static OMSVGElement rotadorActivo;
	
	private static OMSVGGElement botonera;
	private static OMSVGLineElement lh;
	private static OMSVGLineElement lv;
	
	private static OMSVGImageElement imgPatronSuelo;

	private static boolean refrescoActivo = true;

	private static OMSVGPathElement fov;
	private static OMSVGPathElement fovl0;
	private static OMSVGPathElement fovl1;
	
	
	public static Modelo anadirModelo(String tipo, String id, String preview,
			String cenital, float ancho, float alto, float x, float y, float z,
			float pan, boolean favorito, float escala, float altura, String vista0, String vista90, String vista180, String vista270) {
		Modelo m;
		if (Modelo.TIPO_FUENTELUZ.equalsIgnoreCase(tipo)) {
			m = new FuenteLuz();
			m.setId(id);
			m.setImagenVistaPrevia(preview);
			m.setImagenParaPlano(cenital);
		} else if (Modelo.TIPO_VENTANA.equalsIgnoreCase(tipo)) {
			m = new Ventana();
			m.setId(id);
			m.setImagenVistaPrevia(preview);
			m.setImagenParaPlano(cenital);
		} else if (Modelo.TIPO_PUERTA.equalsIgnoreCase(tipo)) {
			m = new Puerta();
			m.setId(id);
			m.setImagenVistaPrevia(preview);
			m.setImagenParaPlano(cenital);
		} else if (Modelo.TIPO_FOTO.equalsIgnoreCase(tipo)) {
			m = new Foto();
			m.setId(id);
			m.setImagenVistaPrevia(preview);
			m.setImagenParaPlano(cenital);
		} else {
			m = new Modelo(100, 100, 0, 0, id, preview, cenital);
		}
		OMSVGPoint px = globalToRoom(new Float(x), new Float(y));
		m.setX(px.getX());
		m.setY(px.getY());
		m.setZ(z);
		m.setAltura(altura);
		m.setEscala(escala);
		m.setPan(pan);
		m.setAncho(ancho);
		m.setAlto(alto);
		m.setFavorito(favorito);
		m.setVista0(vista0);
		m.setVista90(vista90);
		m.setVista180(vista180);
		m.setVista270(vista270);
		if (Modelo.TIPO_VENTANA.equalsIgnoreCase(tipo)) {
			m.setZ(1 * DibujoHelper.ESCALA);
			m.setAltura(1 * DibujoHelper.ESCALA);			
			m.setAncho(2 * DibujoHelper.ESCALA);
			m.setAlto(1 * DibujoHelper.ESCALA);
		} else if (Modelo.TIPO_PUERTA.equalsIgnoreCase(tipo)) {
			m.setZ(0 * DibujoHelper.ESCALA);
			m.setAltura(0 * DibujoHelper.ESCALA);
			m.setAncho(1 * DibujoHelper.ESCALA);
			m.setAlto(2 * DibujoHelper.ESCALA);
		}
		m.setAncho0(m.getAncho());
		m.setAlto0(m.getAlto());
		m.setAltura0(m.getAltura());
		return m;
	}

	private static void addContenido(final Objeto o, Objeto p) {		
		//if (!(o instanceof FuenteLuz)) {
			OMSVGElement cx = null;
			if (o instanceof Puerta) {
				cx = getDoc().createSVGGElement();
				cx.setId("gv" + new Date().getTime());
				cx.appendChild(dibujaVentana((Puerta) o));				
			} else if (o instanceof FuenteLuz) {			
				cx = dibujar((FuenteLuz) o);
			} else if (o instanceof Foto) {			
				cx = dibujar((Foto) o);
			} else if (o instanceof Modelo) {
				if ("".equals(((Modelo) o).getImagenParaPlano())) {
					cx= getDoc().createSVGImageElement(new Float(-1 * ESCALA * o.getAncho0() * 0.01d / 2d), new Float(-1 * ESCALA * o.getAlto0() * 0.01d / 2d), new Float(o.getAncho0() * ESCALA * 0.01d), new Float(o.getAlto0() * ESCALA * 0.01d), ((Modelo) o).getImagenParaPlano());
		            cx.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#00ff00");
		            cx.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		            cx.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
				} else {
					cx = getDoc().createSVGImageElement(new Float(-1 * ESCALA * o.getAncho0() * 0.01d / 2d), new Float(-1 * ESCALA * o.getAlto0() * 0.01d / 2d), new Float(o.getAncho0() * ESCALA * 0.01d), new Float(o.getAlto0() * ESCALA * 0.01d), ((Modelo) o).getImagenParaPlano());
				}
	            cx.setAttribute("transform", "scale(" + (o.getAncho() / o.getAncho0()) + ", " + (o.getAlto() / o.getAlto0()) + ")");
	            imagenesModelos.put((Modelo) o, (OMSVGImageElement) cx);
			}
	//	}
	}

	private static OMSVGElement dibujar(Foto f) {
		OMSVGGElement g = getDoc().createSVGGElement();

		OMSVGLineElement l;
		g.appendChild(l = getDoc().createSVGLineElement(new Float(-1 * f.getAncho() / 2), 0f, new Float(f.getAncho() / 2), 0f));
        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "3");

		return g;
	}

	private static OMSVGElement dibujar(FuenteLuz o) {
		OMSVGGElement g = getDoc().createSVGGElement();
		OMSVGCircleElement c;
		g.appendChild(c = getDoc().createSVGCircleElement(0, 0, 15));
        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#FFFF47");
        c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        
        
        OMSVGLineElement l;
		g.appendChild(l = getDoc().createSVGLineElement(-15, -15, 15, 15));
        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		g.appendChild(l = getDoc().createSVGLineElement(-15, 15, 15, -15));
        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
			/*
			OMNode svgb = Recursos.INSTANCIA.svgBombilla().getSvg();
			
			Object o = svgb;
			System.out.println(o.getClass().getName());
			
			OMSVGGElement auxg = null;
			for (OMNode xx : ((OMSVGSVGElement) o).getChildNodes()) {
				if (xx instanceof OMSVGGElement) auxg = (OMSVGGElement) xx;
			}
			*/
			
			
			//OMSVGGElement gx = habilitar(b, auxg, gcapaluces);
		return g;
	}

	private static OMNode dibujaVentana(Puerta o) {
		OMSVGGElement g = getDoc().createSVGGElement();
		g.setId("gpx" + new Date().getTime());
		g.setAttribute("overflow", "visible");
		
		OMSVGGElement gp = getDoc().createSVGGElement();
		gp.setId("gp" + new Date().getTime());
		gp.setAttribute("overflow", "visible");

		rellenarVentana(gp, o);
		
		OMSVGGElement gcv = getDoc().createSVGGElement();
		gcv.setId("gcv" + new Date().getTime());
		gcv.setAttribute("overflow", "visible");
        
		g.appendChild(gcv);
		g.appendChild(gp);
		
		return g;
	}
	
	private static void rellenarVentana(OMSVGGElement g, Puerta o) {
		
		OMSVGRectElement r = getDoc().createSVGRectElement(0, new Float(-1f * ((o.getPared() != null)? o.getPared().getAncho():25)), new Float(o.getAncho()), new Float((o.getPared() != null)? o.getPared().getAncho():25), 0, 0);
        r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ff0000");
        r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        g.appendChild(r);
        
        /*
        if (o.getHojas() == 1) {        	
    		if (o.isPomoALaIzquierda()) r = getDoc().createSVGRectElement(new Float(o.getAncho() - 3), 0, 3, new Float(o.getAncho()), 0, 0);
    		else r = getDoc().createSVGRectElement(0, 0, 3, new Float(o.getAncho()), 0, 0);
            r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#000000");
            r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            g.appendChild(r);
            
            OMSVGPathElement a = getDoc().createSVGPathElement();
            if (o.isPomoALaIzquierda()) {
                a.getPathSegList().appendItem(a.createSVGPathSegMovetoAbs(new Float(o.getAncho()), 0));
        		a.getPathSegList().appendItem(a.createSVGPathSegLinetoAbs(0, 0));
        		a.getPathSegList().appendItem(a.createSVGPathSegArcAbs(new Float(o.getAncho()), new Float(o.getAncho()), new Float(o.getAncho()), new Float(o.getAncho()), -90, false, false));
        		a.getPathSegList().appendItem(a.createSVGPathSegClosePath());
            } else {
                a.getPathSegList().appendItem(a.createSVGPathSegMovetoAbs(0,  0));
        		a.getPathSegList().appendItem(a.createSVGPathSegLinetoAbs(new Float(o.getAncho()), 0));
        		a.getPathSegList().appendItem(a.createSVGPathSegArcAbs(0, new Float(o.getAncho()), new Float(o.getAncho()), new Float(o.getAncho()), 90, false, true));
        		a.getPathSegList().appendItem(a.createSVGPathSegClosePath());
            }
            a.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
            a.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_OPACITY_PROPERTY, "0.5");
            g.appendChild(a);
        } else if (o.getHojas() == 2) {
    		r = getDoc().createSVGRectElement(new Float(o.getAncho() - 3), 0, 3, new Float(o.getAncho() / 2), 0, 0);
            r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#000000");
            r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            g.appendChild(r);
            
            OMSVGPathElement a = getDoc().createSVGPathElement();
            a.getPathSegList().appendItem(a.createSVGPathSegMovetoAbs(new Float(o.getAncho()), 0));
            a.getPathSegList().appendItem(a.createSVGPathSegLinetoAbs(new Float(o.getAncho() / 2), 0));
            a.getPathSegList().appendItem(a.createSVGPathSegArcAbs(new Float(o.getAncho()), new Float(o.getAncho() / 2), new Float(o.getAncho() / 2), new Float(o.getAncho() / 2), -90, false, false));
            a.getPathSegList().appendItem(a.createSVGPathSegClosePath());
            a.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
            a.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_OPACITY_PROPERTY, "0.5");
            g.appendChild(a);
        	
    		r = getDoc().createSVGRectElement(0, 0, 3, new Float(o.getAncho() / 2), 0, 0);
            r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#000000");
            r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            g.appendChild(r);

            a = getDoc().createSVGPathElement();
            a.getPathSegList().appendItem(a.createSVGPathSegMovetoAbs(0,  0));
            a.getPathSegList().appendItem(a.createSVGPathSegLinetoAbs(new Float(o.getAncho() / 2), 0));
            a.getPathSegList().appendItem(a.createSVGPathSegArcAbs(0, new Float(o.getAncho() / 2), new Float(o.getAncho() / 2), new Float(o.getAncho() / 2), 90, false, true));
            a.getPathSegList().appendItem(a.createSVGPathSegClosePath());
            a.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
            a.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            a.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_OPACITY_PROPERTY, "0.5");
            g.appendChild(a);

        }
        */
        
	}

	public static Object texturaDragginOn;

	private static int volumenX0;

	private static int volumenY0;

	private static boolean mouseDown;


	private static OMSVGRectElement rectanguloArrastreCreacionVolumen;

	private static OMSVGGElement gCreacionVolumen;

	private static OMSVGLineElement lineaCreacionVolumen;
	

	private static Escalera escalera;

	private static boolean creandoEscalera;

	private static OMSVGGElement gCreacionEscalera;

	private static OMSVGLineElement lineaCreacionEscalera;

	private static OMSVGRectElement rectanguloArrastreCreacionEscalera;
	

	private static OMSVGPoint puntoEnObj0;

	private static SVGMarcaSeleccion marcaSeleccion;

	private static SVGDistancias gdistancias;
	



	private static void moo(final OMSVGPathElement l) {
		((HasGraphicalHandlers)l).addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
	    		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#ff0000");
			}
		});

		((HasGraphicalHandlers)l).addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
	    		if (svggarrastrado != null || !(payload.get(svggarrastrado) instanceof ZoomCamara)) l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#ffffff");
			}
		});

	}


	static void actualizarFOV() {
		fov.getPathSegList().clear();
		fov.getPathSegList().appendItem(fov.createSVGPathSegMovetoAbs(0, 0));
		float a = new Float(getPlano().getCamara().getAnchoSensor() * 400 / getPlano().getCamara().getDistanciaSensor());
		fov.getPathSegList().appendItem(fov.createSVGPathSegLinetoAbs(-1 * a / 2, -400));
		fov.getPathSegList().appendItem(fov.createSVGPathSegLinetoAbs(a / 2, -400));
		//fov.getPathSegList().appendItem(fov.createSVGPathSegArcAbs(new Float(o.getAncho()), new Float(o.getAncho()), new Float(o.getAncho()), new Float(o.getAncho()), -90, false, false));
		fov.getPathSegList().appendItem(fov.createSVGPathSegClosePath());
		fov.setAttribute("visibility", "visible");
		
		fovl0.getPathSegList().clear();
		fovl0.getPathSegList().appendItem(fov.createSVGPathSegMovetoAbs(0, 0));
		fovl0.getPathSegList().appendItem(fov.createSVGPathSegLinetoAbs(-1 * a / 2, -400));
		fovl0.getPathSegList().appendItem(fov.createSVGPathSegClosePath());
		fovl0.setAttribute("visibility", "visible");

		fovl1.getPathSegList().clear();
		fovl1.getPathSegList().appendItem(fov.createSVGPathSegMovetoAbs(0, 0));
		fovl1.getPathSegList().appendItem(fov.createSVGPathSegLinetoAbs(a / 2, -400));
		fovl1.getPathSegList().appendItem(fov.createSVGPathSegClosePath());
		fovl1.setAttribute("visibility", "visible");

		actualizarOjo();
	}

	static void limpiarSeleccion() {
		desmarcarSeleccion();
		ocultarBotonera();
		ocultarSlidersEInfo();
		ocultarFOV();
		if (rotadorActivo != null) {
			rotadorActivo.setAttribute("visibility", "hidden");
			rotadorActivo = null;
		}
		seleccionado = null;		
	}

	private static void ocultarBotonera() {
		if (botonera != null) {
			try {
				getSvg().removeChild(botonera);
			} catch (Exception e) {
				e.printStackTrace();
			}
			botonera = null;
		}
	}

	private static void ocultarSlidersEInfo() {
		if (gsliders != null) {
			try {
				getSvg().removeChild(gsliders);
			} catch (Exception e) {
				e.printStackTrace();
			}
			gsliders = null;
		}
		$("#toolbar-herramientas-privado").hide();
	}

	private static void ocultarFOV() {
		fov.setAttribute("visibility", "hidden");
		fovl0.setAttribute("visibility", "hidden");
		fovl1.setAttribute("visibility", "hidden");
		fovl0.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#ffffff");
		fovl1.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#ffffff");

	}

	protected static void mdrotador(OMSVGGElement gx, OMSVGGElement svgbx, int clientX, int clientY) {
		
		if (botonera != null) botonera.setAttribute("visibility", "hidden");
		
		svggarrastrado = svgbx;
		if (rotadorActivo != null && !rotadorActivo.equals(svgbx)) {
			rotadorActivo.setAttribute("visibility", "hidden");
		}
		rotadorActivo = svgbx;
		
		setClientX0(clientX);
		setClientY0(clientY);
		camaraPan0 = ((IRotable)payload.get(rotados.get(svggarrastrado))).getPan();
	}

	public static void resetPlano(int tipoHabitacion) {
		if (botonera != null) {
			getSvg().removeChild(botonera);
			botonera = null;
		}
		Plano p = Plano.crearPlanoInicial(tipoHabitacion);
		p.setX(Window.getClientWidth() / 2);
		p.setY(Window.getClientHeight() / 2);
		
		resetPlano(p);
	}

	public static void refrescaPreview() {
		refrescaPreview(true);
	}
	
	public static void refrescaPreview(boolean hacer) {
//		if (false || (!GWT.getModuleBaseURL().contains("127.0.0.1") && !GWT.getModuleBaseURL().contains("localhost"))) if ("0px".equals($(".preview").css("bottom"))) {
		if (hacer) {
			
			/*
			if (rs.size() == 0) {
				rs.add(new Date());
				cargaPreview();
			} else {
				rs.add(new Date());
			}
			*/
			
			if (isRefrescoActivo() && getPlano().getCamara().isVisible()) {
				rs.add(new Date());
				if (srp == null) Scheduler.get().scheduleFixedDelay(srp = new Scheduler.RepeatingCommand() {
					
					@Override
					public boolean execute() {
						srp = null;
						Date dz = null;
						Date dx;
						while ((dx = rs.poll()) != null) dz = dx; 
						if (dz != null) {
							$("#previewmsg").html("Cargando...");
							$("#previewmsg").show();
							Fsk4.getServicio().preview(getPlano(), new AsyncCallback<String>() {
								
								@Override
								public void onSuccess(String result) {
									$("#previewmsg").hide();
									$("#previewimg").elements()[0].setAttribute("src", result);
								}
								
								@Override
								public void onFailure(Throwable caught) {
									$("#previewmsg").hide();
									Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
								}
							});
						}
						return false;
					}
				}, 700);
			}
		}
	}

	private static void cargaPreview() {
		rs.clear();
		$("#previewmsg").html("Cargando...");
		$("#previewmsg").show();
		Fsk4.getServicio().preview(getPlano(), new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				$("#previewmsg").hide();
				$("#previewimg").elements()[0].setAttribute("src", result);
				if (rs.size() > 0) cargaPreview();
			}

			@Override
			public void onFailure(Throwable caught) {
				$("#previewmsg").hide();
				Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
				if (rs.size() > 0) cargaPreview();
			}
		});
	}

	static Plano getPlano() {
		return plano;
	}

	private static void setPlano(Plano p) {
		plano = p;
		
		if (getPlano().getCamara().isVisible()) $("#botoncamara").addClass("active"); else $("#botoncamara").removeClass("active");
		if (getPlano().isLuzSolar()) $("#botonsol").addClass("active"); else $("#botonsol").removeClass("active");
		if (getPlano().isLuzCamara()) $("#botonflash").addClass("active"); else $("#botonflash").removeClass("active");
		if (getPlano().isParedesBloqueadas()) $("#botoncandado").addClass("active"); else $("#botoncandado").removeClass("active");
		if (getPlano().isIman()) $("#botoniman").addClass("active"); else $("#botoniman").removeClass("active");
		
	}


	protected static void log(DomEvent event) {
		if (false) log(event.getClass().getName().substring(event.getClass().getName().lastIndexOf(".")) + "-->" + event.getSource());		
	}

	protected static void log(String m) {
		//$("#log").val(m + "\n" + (($("#log").val().indexOf("\n", 30) >= 0)?$("#log").val().substring(0, $("#log").val().indexOf("\n", 30)):$("#log").val()));		
		if (true) $("#divlog").html(m + "<br/>" + (($("#divlog").html().indexOf("<br/>", 30) >= 0)?$("#divlog").html().substring(0, $("#divlog").html().indexOf("<br/>", 30)):$("#divlog").html()));		
		System.out.println(m);
	}
	
	private static void log(OMSVGTextElement l, String s) {
		for (int i = l.getChildNodes().getLength() - 1; i >=0; i--) l.removeChild(l.getChildNodes().getItem(i)); 
		l.appendChild(new OMText(s));
	}

	
	private static void asignarTexturas(final OMSVGGElement tex, final Pared p) {
		
		PantallaHelper.abrirDialogo("divasignartexurapared", 600, 300, new Runnable() {
			
			@Override
			public void run() {
				
				Fsk4.getServicio().sql("sql:select mat_id, mat_nombre, eti_nombre from fsk_material inner join fsk_material_etiqueta on x_material = mat_id inner join fsk_etiqueta on eti_id = x_etiqueta where eti_nombre in ('pared', 'cenefa') order by eti_nombre, mat_nombre", new AsyncCallback<String[][]>() {

					@Override
					public void onFailure(Throwable caught) {
						Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
					}

					@Override
					public void onSuccess(String[][] result) {
						Map<String, StringBuffer> hs = new HashMap<String, StringBuffer>();
						hs.put("pared", new StringBuffer());
						hs.put("cenefa", new StringBuffer());
						
						for (String[] l : result) {
							hs.get(l[2]).append("<option value='" + l[0] + "'>" + l[1] + "</option>");
						}
						
						for (String k : hs.keySet()) $(".selectmaterial." + k).html(hs.get(k).toString());
						
						inicializarFormulario("int", p.getRevestimientoInterior());
						inicializarFormulario("ext", p.getRevestimientoExterior());
						
					}

					private void inicializarFormulario(final String k, RevestimientoPared r) {

						$("#atp_" + k + "_tipo_" + r.getTipo()).attr("checked", true);
						//$("#atp_" + k + "_tipo").val(r.getTipo());

						$("#atp_" + k + "_rev1").val(r.getRevestimiento1().getId());
						$("#atp_" + k + "_cen1").val(r.getCenefa1().getId());
						$("#atp_" + k + "_rev2").val(r.getRevestimiento2().getId());
						$("#atp_" + k + "_cen2").val(r.getCenefa2().getId());
						$("#atp_" + k + "_rev3").val(r.getRevestimiento3().getId());

						$("#atp_" + k + "_alto_cen1").val("" + r.getAlturaCenefa1());
						$("#atp_" + k + "_ancho_cen1").val("" + r.getAnchoCenefa1());
						$("#atp_" + k + "_alto_cen2").val("" + r.getAlturaCenefa2());
						$("#atp_" + k + "_ancho_cen2").val("" + r.getAnchoCenefa2());

						$("#atp_" + k + "_aplicar_esta").click(new Function() {
							@Override
							public void f(Element e) {

								List<Pared> paredes = new ArrayList<Pared>();
								paredes.add(p);
								aplicar(k, paredes);
								
								DibujoHelper.refrescaPreview();
								PantallaHelper.cerrarDialogo();
							}

						});

						$("#atp_" + k + "_aplicar_todas").click(new Function() {
							@Override
							public void f(Element e) {

								List<Pared> paredes = new ArrayList<Pared>();
								for (List<Pared> pz : getPlanta().getTodasParedes()) if (pz.contains(p)) {
									paredes.addAll(pz);
								}
								aplicar(k, paredes);
								
								DibujoHelper.refrescaPreview();
								PantallaHelper.cerrarDialogo();
							}

						});

						//$("#habitacion_tienecenefa").attr("checked"						
						//boolean checked = aux.is(":checked");
						
					}
					
					private void aplicar(String k, List<Pared> paredes) {
						for (Pared p: paredes) {
							RevestimientoPared r = null;
							if ("int".equals(k)) {
								r = p.getRevestimientoInterior();
							} else {
								r = p.getRevestimientoExterior();
							}
							
							if ($("#atp_" + k + "_tipo_sincenefa").is(":checked")) r.setTipo("sincenefa");
							else if ($("#atp_" + k + "_tipo_1cenefa").is(":checked")) r.setTipo("1cenefa");
							else if ($("#atp_" + k + "_tipo_2cenefas").is(":checked")) r.setTipo("2cenefas");
							
							r.getRevestimiento1().setId($("#atp_" + k + "_rev1").val());
							r.getCenefa1().setId($("#atp_" + k + "_cen1").val());
							r.getRevestimiento2().setId($("#atp_" + k + "_rev2").val());
							r.getCenefa2().setId($("#atp_" + k + "_cen2").val());
							r.getRevestimiento3().setId($("#atp_" + k + "_rev3").val());

							r.setAlturaCenefa1(Double.parseDouble($("#atp_" + k + "_alto_cen1").val()));
							r.setAnchoCenefa1(Double.parseDouble($("#atp_" + k + "_ancho_cen1").val()));
							r.setAlturaCenefa2(Double.parseDouble($("#atp_" + k + "_alto_cen2").val()));
							r.setAnchoCenefa2(Double.parseDouble($("#atp_" + k + "_ancho_cen2").val()));

						}
					}					

				}); 
				
				
				//$("#astex_leyenda").html("Asignación texturas " + m.getInfo().getNombre());
				
				/*
				
				StringBuffer h = new StringBuffer();
				h.append("<table>");
				
				int pos = 0;
				for (final Textura t : m.getInfo().getTexturas()) {
					h.append("<tr><td><img class='xxx' src='" + t.getImagen() + "' width=100 height=100></td>");
					h.append("<td><input type='checkbox' class='checkasignartextura' data-pos='" + pos + "' data-idmaterial0='" + t.getIdMaterial() + "'  data-cenital0='" + t.getCenital() + "' ></td>");
					h.append("<td><img id='cenitaltextura_" + pos  + "' class='xxx' src='" + t.getCenital() + "' width=100 height=100></td>");
					h.append("</tr>");
					pos++;
				}
				h.append("<table>");
				
				$("#divtexturas").html(h.toString());

				$(".checkasignartextura").click(new Function() {
					@Override
					public void f(Element e) {
						//Fsk4.alert("clic en " + $(e).attr("data-id") + "," + $(e).attr("data-cenital") + "," + $(e).attr("data-nombre"));
						GQuery aux = $(e);
						int pos = Integer.parseInt(aux.attr("data-pos"));
						
						boolean checked = aux.is(":checked");
						
						Textura t = m.getInfo().getTexturas().get(pos);
						
						t.setCenital((checked)?tex.getAttribute("data-cenital"):aux.attr("data-cenital0"));
						t.setIdMaterial((checked)?tex.getAttribute("data-id"):aux.attr("data-idmaterial0"));
						
						$("#cenitaltextura_" + pos).attr("src", t.getCenital());
						
						Fsk4.success("Textura asignada");
						DibujoHelper.refrescaPreview();
					}					
				});
				*/
			}
		});
		
		
		/*
		
		if (false) abrirDialogo(new SVGDialogoAsignacionTexturas(tex, m) {

			@Override
			public float getAlto() {
				return 30 + 30 + m.getInfo().getTexturas().size() * 30;
			}

			@Override
			public String getTitulo() {
				return "Asignación texturas " + m.getInfo().getNombre();
			}
			
			
			
		});
		*/
	}
	
	

	private static void asignarTexturas(final OMSVGGElement tex, final Modelo m) {
		
		PantallaHelper.abrirDialogo("divasignartextura", 400, 300, new Runnable() {
			
			@Override
			public void run() {
				$("#astex_leyenda").html("Asignación texturas " + m.getInfo().getNombre());
				
				StringBuffer h = new StringBuffer();
				h.append("<table>");
				
				int pos = 0;
				for (final Textura t : m.getInfo().getTexturas()) {
					h.append("<tr><td><img class='xxx' src='" + t.getImagen() + "' width=100 height=100></td>");
					h.append("<td><input type='checkbox' class='checkasignartextura' data-pos='" + pos + "' data-idmaterial0='" + t.getIdMaterial() + "'  data-cenital0='" + t.getCenital() + "' ></td>");
					h.append("<td><img id='cenitaltextura_" + pos  + "' class='xxx' src='" + t.getCenital() + "' width=100 height=100></td>");
					h.append("</tr>");
					pos++;
				}
				h.append("<table>");
				
				$("#divtexturas").html(h.toString());

				$(".checkasignartextura").click(new Function() {
					@Override
					public void f(Element e) {
						//Fsk4.alert("clic en " + $(e).attr("data-id") + "," + $(e).attr("data-cenital") + "," + $(e).attr("data-nombre"));
						GQuery aux = $(e);
						int pos = Integer.parseInt(aux.attr("data-pos"));
						
						boolean checked = aux.is(":checked");
						
						Textura t = m.getInfo().getTexturas().get(pos);
						
						t.setCenital((checked)?tex.getAttribute("data-cenital"):aux.attr("data-cenital0"));
						t.setIdMaterial((checked)?tex.getAttribute("data-id"):aux.attr("data-idmaterial0"));
						
						$("#cenitaltextura_" + pos).attr("src", t.getCenital());
						
						Fsk4.success("Textura asignada");
						DibujoHelper.refrescaPreview();
					}
				});
			}
		});
		
		
		if (false) abrirDialogo(new SVGDialogoAsignacionTexturas(tex, m) {

			@Override
			public float getAlto() {
				return 30 + 30 + m.getInfo().getTexturas().size() * 30;
			}

			@Override
			public String getTitulo() {
				return "Asignación texturas " + m.getInfo().getNombre();
			}
			
			
			
		});
	}

	static void abrirDialogo(SVGDialogo d) {
		ocultarBotonera();		
		getBackgroundDialogo().setAttribute("visibility", "visible");
		getSvg().appendChild(d);		
	}

	private static void marcarSeleccion() {
		if (seleccionado != null) {
			 if (seleccionado instanceof AnguloEscalera) {
				((SVGAnguloEscalera)DibujoHelper.payload.get(seleccionado)).marcar();
			} else if (seleccionado instanceof Modelo && !(seleccionado instanceof Puerta) && !(seleccionado instanceof FuenteLuz) && !(seleccionado instanceof Sol)) {
				Modelo m = (Modelo) seleccionado;
				
				if (marcaSeleccion != null && payload.get(seleccionado) != null) ((OMSVGGElement)payload.get(seleccionado)).removeChild(marcaSeleccion);
				marcaSeleccion = null;

				if (marcaSeleccion == null) {
					
					marcaSeleccion = new SVGMarcaSeleccion(m);
					
					((OMSVGGElement)DibujoHelper.payload.get(seleccionado)).appendChild(marcaSeleccion);					
					
				}
			}
			if (rotadorActivo != null) rotadorActivo.setAttribute("visibility", "visible");
			OMSVGGElement g = (OMSVGGElement) DibujoHelper.payload.get(seleccionado);
			if (g != null) {
				g.appendChild(gdistancias = new SVGDistancias(seleccionado));
			}
		}
	}


	public static List<Linea> getLineasContorno(Objeto o) {
		List<Linea> ls = new ArrayList<Linea>();
		{
			
				if (o instanceof Volumen) {
					Volumen vol = (Volumen) o;
					/*
					SVGVolumen svol = svgsVolumenes.get(vol);
					for (int i = 0; i < vol.getVertices().size(); i++) {
						int iSiguiente = i + 1;
						if (iSiguiente == vol.getVertices().size()) iSiguiente = 0;
						OMSVGPoint de = globalToRoom(localToGlobal(svol, new Float(vol.getVertices().get(i).getX()), new Float(vol.getVertices().get(i).getY())));
						OMSVGPoint a = globalToRoom(localToGlobal(svol, new Float(vol.getVertices().get(iSiguiente).getX()), new Float(vol.getVertices().get(iSiguiente).getY()))); 
						ls.add(new Linea(new Punto(de.getX(), de.getY()), new Punto(a.getX(), a.getY())));
					}
					*/
				} else {
					OMSVGGElement g = (OMSVGGElement) payload.get(o);
					
					double ancho = o.getAncho() * ESCALA / 100d;
					double alto = o.getAlto() * ESCALA / 100d;

					List<Vertice> vs = new ArrayList<Vertice>();
					vs.add(new Vertice(new Float(-1 * ancho / 2), new Float(-1 * alto / 2)));
					vs.add(new Vertice(new Float(1 * ancho / 2), new Float(-1 * alto / 2)));
					vs.add(new Vertice(new Float(1 * ancho / 2), new Float(1 * alto / 2)));
					vs.add(new Vertice(new Float(-1 * ancho / 2), new Float(1 * alto / 2)));
					
					for (int i = 0; i < vs.size(); i++) {
						int iSiguiente = i + 1;
						if (iSiguiente == vs.size()) iSiguiente = 0;
						OMSVGPoint de = globalToRoom(localToGlobal(g, new Float(vs.get(i).getX()), new Float(vs.get(i).getY())));
						OMSVGPoint a = globalToRoom(localToGlobal(g, new Float(vs.get(iSiguiente).getX()), new Float(vs.get(iSiguiente).getY()))); 
						ls.add(new Linea(new Punto(de.getX(), de.getY()), new Punto(a.getX(), a.getY())));
					}
			}
			
		}
		return ls;
	}

	private static void actualizarCotasPuerta(Puerta o) {
		
		OMSVGGElement gpta = (OMSVGGElement) payload.get(o);
		
		OMSVGGElement gv = null;
		for (OMNode z : gpta.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gv")) gv = (OMSVGGElement) z;
		OMSVGGElement gpx = null;
		for (OMNode z : gv.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gpx")) gpx = (OMSVGGElement) z;
		OMSVGGElement gcv = null;
		for (OMNode z : gpx.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gcv")) gcv = (OMSVGGElement) z;
		OMSVGGElement gp = null;
		for (OMNode z : gpx.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gp")) gp = (OMSVGGElement) z;
		
		List<OMNode> zz = new ArrayList<OMNode>();
		for (OMNode z : gcv.getChildNodes()) zz.add(z);
		for (OMNode z : zz) gcv.removeChild(z);
		
		if (o.getPared() != null) {
			
			zz = new ArrayList<OMNode>();
			for (OMNode z : gp.getChildNodes()) zz.add(z);
			for (OMNode z : zz) gp.removeChild(z);

			rellenarVentana(gp, o);
			
			
			OMSVGLineElement l;
			gcv.appendChild(l = getDoc().createSVGLineElement(new Float(-1 * o.getDistanciaDesdeDe()).floatValue(), -7, 0, -7));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			
			OMSVGTextElement t = getDoc().createSVGTextElement(0f, 0f, OMSVGLength.SVG_LENGTHTYPE_PX, "" + (Math.round(100d * o.getDistanciaDesdeDe() / ESCALA) / 100d) + "m.");
			t.setAttribute("pointer-events", "none");
			//t.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "0");
			String auxs = "translate(" + (0 + (-1 * o.getDistanciaDesdeDe()) / 2) + ", -3)";
			t.setAttribute("transform", auxs);
			gcv.appendChild(t);
			
			double lon = new Linea(o.getPared()).longitud();
			
			gcv.appendChild(l = getDoc().createSVGLineElement(new Float(o.getDistanciaDesdeA() + o.getAncho()), -7, new Float(lon - o.getDistanciaDesdeDe()), -7));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			
			t = getDoc().createSVGTextElement(0f, 0f, OMSVGLength.SVG_LENGTHTYPE_PX, "" + (Math.round(100d * (lon - o.getDistanciaDesdeDe() - o.getAncho()) / ESCALA) / 100d) + "m.");
			t.setAttribute("pointer-events", "none");
			//t.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "0");
			auxs = "translate(" + (o.getAncho() + (lon - o.getDistanciaDesdeDe() - o.getAncho()) / 2) + ", -3)";
			t.setAttribute("transform", auxs);
			gcv.appendChild(t);
		
		}
	}

	private static void imantar(OMSVGPoint p1, OMSVGPoint p0) {
		if (getPlano().isIman()) {
			if (Math.abs(p1.getX() - p0.getX()) < 10) p1.setX(p0.getX());
			if (Math.abs(p1.getY() - p0.getY()) < 10) p1.setY(p0.getY());
		}
				
	}

	private static void desmarcarSeleccion() {
		if (seleccionado != null) {
			if (seleccionado instanceof AnguloEscalera) {
				((SVGAnguloEscalera)DibujoHelper.payload.get(seleccionado)).desmarcar();
			} else if (seleccionado instanceof Modelo) {
				if (marcaSeleccion != null && payload.get(seleccionado) != null) ((OMSVGGElement)payload.get(seleccionado)).removeChild(marcaSeleccion);
				marcaSeleccion = null;
			}
			if (rotadorActivo != null) {
				rotadorActivo.setAttribute("visibility", "hidden");
			}
			OMSVGGElement g = (OMSVGGElement) DibujoHelper.payload.get(seleccionado);
			if (g != null) {
				if (gdistancias != null && gdistancias.getParentNode().equals(g)) g.removeChild(gdistancias);
				gdistancias = null;
			}
		}
	}

	protected static double camaraTilt0;
	protected static boolean tilteandoCamara;

	private static OMSVGGElement gcam;

	protected static boolean slideandoCamara;

	protected static float camaraZ0;

	private static OMSVGGElement gzoom;

	static SVGInfoModelo i;

	public static boolean enAlzado;

	private static OMSVGGElement gsliders;

	public static SVGSlider sldrAncho;

	public static SVGSlider sldrProfundo;

	
	private static void visualizarSlidersEInfo() {
		try {
			if (gsliders != null) {
				try {
					getSvg().removeChild(gsliders);
				} catch (Exception e) {
					e.printStackTrace();
				}
				gsliders = null;
			}
			
			if (true) {
				if (!(seleccionado instanceof Plano)&& !(seleccionado instanceof Planta)) {
					if (getDoc() != null) {
						gsliders = getDoc().createSVGGElement();
					}
				
				int w = Window.getClientWidth();
				int h = Window.getClientHeight();
				
				int top = 324;
				if (("miguelperezcolom@gmail.com".equalsIgnoreCase(Datos.get().getEmail())
						|| "carlosmulet@gmail.com".equalsIgnoreCase(Datos.get().getEmail())
						|| "jaumet@graficmail.com".equalsIgnoreCase(Datos.get().getEmail())
						|| "cas.ferrer@yahoo.es".equalsIgnoreCase(Datos.get().getEmail())
						|| "bielcr@msn.com".equalsIgnoreCase(Datos.get().getEmail())
						) 
						&& seleccionado instanceof Modelo &&  !(seleccionado instanceof Puerta) && !(seleccionado instanceof Sol) && !(seleccionado instanceof FuenteLuz)) {
					$("#toolbar-herramientas-privado").show();
					top += 35;
				} else {
					$("#toolbar-herramientas-privado").hide();
				}

				gsliders.setAttribute("transform", "translate(" + (w - 238) + ", " + (top) + ")");
					
					if (seleccionado instanceof Camara) {

			    		OMSVGRectElement r;
			    		gsliders.appendChild(r = getDoc().createSVGRectElement(0f, 0f, 100f, 120f, 10f, 10f));
			    		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
			    		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
			    		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
			    		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "0.1");
			    		r.setAttribute("pointer-events", "none");

			    		OMSVGLineElement lin;
			    		gsliders.appendChild(lin = getDoc().createSVGLineElement(20, 10, 20, 110));
			    		lin.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
			    		lin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
			    		lin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
			    		
			    		
			    		gcam = getDoc().createSVGGElement();
			    		OMSVGRectElement cam;
			    		gcam.appendChild(cam = getDoc().createSVGRectElement(-5, -5, 10, 10, 0, 0));
			    		cam.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
			    		cam.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
			    		cam.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
			    		cam.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");

			    		cam.addMouseDownHandler(new MouseDownHandler() {

			    			@Override
			    			public void onMouseDown(MouseDownEvent event) {
			    				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
			    					event.preventDefault();
			    					event.stopPropagation();

			    					slideandoCamara = true;
			    					setClientX0(event.getNativeEvent().getClientX());
			    					setClientY0(event.getNativeEvent().getClientY());
			    					camaraZ0 = getPlano().getCamara().getZ();
			    				}


			    			}
			    		});

			    		OMSVGCircleElement rotcam;
			    		gcam.appendChild(rotcam = getDoc().createSVGCircleElement(30, 0, 5));
			    		rotcam.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ff0000");
			    		rotcam.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
			    		rotcam.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
			    		rotcam.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");

			    		rotcam.addMouseDownHandler(new MouseDownHandler() {

			    			@Override
			    			public void onMouseDown(MouseDownEvent event) {
			    				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
			    					event.preventDefault();
			    					event.stopPropagation();

			    					tilteandoCamara = true;
			    					setClientX0(event.getNativeEvent().getClientX());
			    					setClientY0(event.getNativeEvent().getClientY());
			    					camaraTilt0 = getPlano().getCamara().getTilt();
			    				}


			    			}
			    		});
			    		
						gcam.setAttribute("transform", "translate(20, " + (110 - ((getPlano().getCamara().getZ() / ESCALA) * 100 / 2.5)) + "), rotate(" + (-1 * getPlano().getCamara().getTilt()) + ")");

			    		gsliders.appendChild(gcam);			
						
					} else {
						if (seleccionado instanceof Modelo) {
							if (!(seleccionado instanceof Puerta) && !(seleccionado instanceof Sol) && !(seleccionado instanceof FuenteLuz) && !(seleccionado instanceof Foto)) {
								marcarSeleccion();
							}
						}
						
						if (seleccionado instanceof Modelo || seleccionado instanceof Pared || seleccionado instanceof Vertice || seleccionado instanceof AnguloEscalera) {
							List<OMSVGGElement> sldrs = new ArrayList<OMSVGGElement>();							

							if (seleccionado instanceof AnguloEscalera) {
								sldrs.add(new SVGSlider(((AnguloEscalera) seleccionado).getZ(), 0, 5, "Dist. al suelo", new ChangeListener() {

									@Override
									public void change(float nuevoValor) {
										((AnguloEscalera) seleccionado).setZ(nuevoValor);
										DibujoHelper.refrescaPreview();
									}
									
								}));
								
								sldrs.add(new SVGSlider(((AnguloEscalera) seleccionado).getEscalones(), 0, 3, "Escalones", new ChangeListener() {

									@Override
									public void change(float nuevoValor) {
										((AnguloEscalera) seleccionado).setEscalones(new Double(nuevoValor).intValue());
										((SVGEscalera)DibujoHelper.payload.get(((AnguloEscalera) seleccionado).getEscalera())).fillSegs(true);
										DibujoHelper.refrescaPreview();
										//gets.setValor(nuevoValor);
									}
									
								}));
							}


							if (seleccionado instanceof Vertice) {
								sldrs.add(new SVGSlider(((Vertice) seleccionado).getZ(), 0, 5, "Dist. al suelo", new ChangeListener() {

									@Override
									public void change(float nuevoValor) {
										((Vertice) seleccionado).setZ(nuevoValor);
										DibujoHelper.refrescaPreview();
									}
									
								}));

							} else if (seleccionado instanceof Vertice) {
								sldrs.add(new SVGSlider(((Vertice) seleccionado).getZ(), 0, 5, "Dist. al suelo", new ChangeListener() {

									@Override
									public void change(float nuevoValor) {
										((Vertice) seleccionado).setZ(nuevoValor);
										DibujoHelper.refrescaPreview();
									}
									
								}));

							} else if (seleccionado instanceof Modelo) {
								
								if (seleccionado instanceof FuenteLuz) {
									sldrs.add(new SVGSlider(((FuenteLuz) seleccionado).getLumens(), 0, 1000, "Lumens", new ChangeListener() {

										@Override
										public void change(float nuevoValor) {
											((FuenteLuz) seleccionado).setLumens(new Double(nuevoValor).intValue());
											DibujoHelper.refrescaPreview();
										}
										
									}));
								}
								
								if (false && seleccionado instanceof Sol) {
									sldrs.add(new SVGCheckbox(((Sol) seleccionado).isActivo(), "Activo", new ChangeListenerBoolean() {

										@Override
										public void change(boolean nuevoValor) {
											((Sol) seleccionado).setActivo(nuevoValor);
											((OMSVGGElement)payload.get(seleccionado)).getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, ((Sol) seleccionado).isActivo()?"1":"0.5");
											Linea l = new Linea(new Punto(((Sol) seleccionado).getX(), ((Sol) seleccionado).getY()), getPlano().getCentroHabitacion(getPlanta()));
											gflechassol.setAttribute("transform", "rotate(" + l.angulo() + ")");
											DibujoHelper.refrescaPreview();
										}
										
									}));
								}
								
								if (!(seleccionado instanceof Puerta) && !(seleccionado instanceof Sol) && !(seleccionado instanceof FuenteLuz) && !(seleccionado instanceof Foto)) {

									boolean anchoYAlto = true;
									
									if (anchoYAlto) sldrs.add(sldrAncho = new SVGSlider(((Modelo) seleccionado).getAncho() / ESCALA, 0, 2.5f, "Ancho", new ChangeListener() {

										@Override
										public void change(float nuevoValor) {
											((Modelo) seleccionado).setAncho(nuevoValor * ESCALA);
											DibujoHelper.refrescarModelo((Modelo) seleccionado);
											DibujoHelper.refrescaPreview();
										}

									}));

									if (anchoYAlto) sldrs.add(sldrProfundo = new SVGSlider(((Modelo) seleccionado).getAlto() / ESCALA, 0, 2.5f, "Profundo", new ChangeListener() {

										@Override
										public void change(float nuevoValor) {
											((Modelo) seleccionado).setAlto(nuevoValor * ESCALA);
											DibujoHelper.refrescarModelo((Modelo) seleccionado);
											DibujoHelper.refrescaPreview();
										}

									}));


									sldrs.add(new SVGSlider(((Modelo) seleccionado).getAltura() / ESCALA, 0, 2.7f, "Altura", new ChangeListener() {

										@Override
										public void change(float nuevoValor) {
											((Modelo) seleccionado).setAltura(nuevoValor * ESCALA);
											DibujoHelper.refrescaPreview();
										}
										
									}));
								}

								if (seleccionado instanceof Puerta) {
									sldrs.add(new SVGSlider(((Puerta) seleccionado).getAncho() / ESCALA, 0, 6, "Ancho", new ChangeListener() {

										@Override
										public void change(float nuevoValor) {
											((Puerta) seleccionado).setAncho(nuevoValor * ESCALA);
											actualizaPuerta((Puerta) seleccionado);
											DibujoHelper.refrescaPreview();
										}
										
									}));

									sldrs.add(new SVGSlider(((Puerta) seleccionado).getAlto() / ESCALA, 0, 2.7f, "Alto", new ChangeListener() {

										@Override
										public void change(float nuevoValor) {
											((Puerta) seleccionado).setAlto(nuevoValor * ESCALA);
											actualizaPuerta((Puerta) seleccionado);
											DibujoHelper.refrescaPreview();
										}
										
									}));
								}
							}
							

							if (seleccionado instanceof Pared) {
								sldrs.add(new SVGSlider(((Pared) seleccionado).getAlto(), 0, 5, "Altura", new ChangeListener() {

									@Override
									public void change(float nuevoValor) {
										((Pared) seleccionado).setAlto(nuevoValor);
										((Pared) seleccionado).getDe().setZ(nuevoValor);
										((Pared) seleccionado).getA().setZ(nuevoValor);
										DibujoHelper.refrescaPreview();
									}
									
								}));

								sldrs.add(new SVGSlider(((Pared) seleccionado).getAncho(), 0, 100, "Ancho", new ChangeListener() {

									@Override
									public void change(float nuevoValor) {
										((Pared) seleccionado).setAncho(nuevoValor);
										for (List<Pared> paredes : getPlanta().getTodasParedes()) if (paredes.contains(seleccionado)) {
											refrescarParedes(paredes);
										}
										DibujoHelper.refrescaPreview();
									}
									
								}));

							}
							
							
				    		OMSVGRectElement r;
							gsliders.appendChild(r = getDoc().createSVGRectElement(0f, 0f, 217f, (10 + 22 * sldrs.size()), 5f, 5f));
				    		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
				    		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
				    		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
				    		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "0.1");
				    		r.setAttribute("pointer-events", "none");

							
							for (OMSVGGElement sldr : sldrs) {
								gsliders.appendChild(sldr); 						
								sldr.setAttribute("transform", "translate(10, " + (5 + 22 * sldrs.indexOf(sldr)) + ")");
							}

							if (!(seleccionado instanceof Vertice) && !(seleccionado instanceof Pared) && !(seleccionado instanceof Volumen) && !(seleccionado instanceof AnguloEscalera) && !(seleccionado instanceof FuenteLuz) && ((Modelo)seleccionado).getId() != null && !"".equals(((Modelo)seleccionado).getId().trim())) {
								gsliders.appendChild(i = new SVGInfoModelo((Modelo)seleccionado));
								i.setAttribute("transform", "translate(0, " + (10 + 22 * sldrs.size() + 10) + ")");
							}
							
						}

					}
					
					getSvg().appendChild(gsliders);		
				}
			}
	} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected static void refrescarModelo(Modelo m) {
		{
			DibujoHelper.imagenesModelos.get(m).setAttribute("transform", "scale(" + (m.getAncho() / m.getAncho0()) + ", " + (m.getAlto() / m.getAlto0()) + ")");

			String auxs = "translate(" + m.getX() + "," + m.getY() + ")";
			auxs += " rotate(" + m.getPan() + " 0 0)";
			//if (!(v instanceof Volumen)) auxs += " scale(" + (v.getEscala() / 0.001d) + ")";					
			((OMSVGGElement)DibujoHelper.payload.get(DibujoHelper.seleccionado)).setAttribute("transform", auxs);
		}
	}



	protected static void cambiar(Puerta pta) {

		
		pta.setPomoALaIzquierda(!pta.isPomoALaIzquierda());

		/*
		if (pta.isPomoALaIzquierda() && pta.getHojas() == 1) {
			pta.setPomoALaIzquierda(false);
		} else if (pta.getHojas() == 1) {
			pta.setHojas(2);
		} else {
			pta.setPomoALaIzquierda(true);
			pta.setHojas(1);
		}
		 */
		
		actualizaPuerta(pta);
		
	}

	protected static void alejar(Modelo m) {
		Planta h = getPlanta();
		if (h.getContenido().indexOf(m) > 0) {
			int newpos = h.getContenido().indexOf(m) - 1;
			h.getContenido().remove(m);
			h.getContenido().add(newpos, m);
			
			OMNode g = ((OMSVGGElement)payload.get(m)).getParentNode();
			
			OMNode ref = null;
			OMNode aux = null;
			for (int i = 0; i < g.getChildNodes().getLength(); i++) {
				if (payload.get(g.getChildNodes().getItem(i)) != null && !payload.get(g.getChildNodes().getItem(i)).equals(m)) aux = g.getChildNodes().getItem(i);
				if (g.getChildNodes().getItem(i).equals(payload.get(m))) ref = aux;
			}
			if (ref != null && !ref.equals(payload.get(m))) {
				g.removeChild((OMNode) payload.get(m));
				g.insertBefore((OMNode) payload.get(m), ref);		
			}
		}
	}

	
	protected static void duplicar(Modelo m) {
		Planta h = getPlanta();
		Modelo m2 = null;
		/*
		if (m instanceof Volumen) {
			m2 = new Volumen();
			Volumen v = (Volumen) m;
			Volumen v2 = (Volumen) m2;
			
			for (VerticeVolumen vx : v.getVertices()) {
				v2.getVertices().add(new VerticeVolumen(vx));
			}
			Material mat2;
			v2.setRevestimiento(mat2 = new Material());
			Material mat = v.getRevestimiento();
			mat2.setAlto(mat.getAlto());
			mat2.setAncho(mat.getAncho());
			mat2.setColor(mat.getColor());
			mat2.setId(mat.getId());
			mat2.setTextura(mat.getTextura());
			mat2.setTipo(mat.getTipo());
			v2.setAltura(v.getAltura());
			for (Hueco hu : v.getHuecos()) {
				Hueco hu2;
				v2.getHuecos().add(hu2 = new Hueco());
				hu2.setPan(hu.getPan());
				for (Vertice vx : hu.getVertices()) {
					hu2.getVertices().add(new Vertice(vx));
				}
				hu2.setX(hu.getX());
				hu2.setY(hu.getY());
			}
		} else */ if (m instanceof FuenteLuz) {
			m2 = new FuenteLuz();
		} else if (m instanceof Foto) {
			m2 = new Foto();
		} else {
			m2 = new Modelo();
		}
		
		m2.setAlto(m.getAlto());
		m2.setAltura(m.getAltura());
		m2.setAncho(m.getAncho());
		m2.setAlto0(m.getAlto0());
		m2.setAncho0(m.getAncho0());
		m2.setAltura0(m.getAltura0());
		m2.setEscala(m.getEscala());
		m2.setFavorito(m.isFavorito());
		m2.setId(m.getId());
		m2.setImagenParaPlano(m.getImagenParaPlano());
		m2.setImagenVistaPrevia(m.getImagenVistaPrevia());
		m2.setIncluirEnPresupuesto(m.isIncluirEnPresupuesto());
		if (m.getInfo() != null) {
			InfoArticulo i = m.getInfo();
			InfoArticulo i2;
			m2.setInfo(i2 = new InfoArticulo());
			i2.setCenital(i.getCenital());
			i2.setDescripcion(i.getDescripcion());
			i2.setId(i.getId());
			i2.setNombre(i.getNombre());
			i2.setPvp(i.getPvp());
			for (Textura t : i.getTexturas()) {
				Textura t2;
				i2.getTexturas().add(t2 = new Textura());
				t2.setCenital(t.getCenital());
				t2.setIdMaterial(t.getIdMaterial());
				t2.setImagen(t.getImagen());
				t2.setNombre(t.getNombre());
			}
			i2.setVistaPrevia(i.getVistaPrevia());
		}
		m2.setPadre(m.getPadre());
		if (m2.getPadre() != null) m2.getPadre().getContenido().add(m2);
		m2.setPan(m.getPan());
		m2.setVisible(m.isVisible());
		m2.setVista0(m.getVista0());
		m2.setVista180(m.getVista180());
		m2.setVista270(m.getVista270());
		m2.setVista90(m.getVista90());
		m2.setX(m.getX() + 10);
		m2.setY(m.getY() + 10);
		m2.setZ(m.getZ());
		
		h.getContenido().add(m2);
		
		addContenido(m2, h);
		//seleccionar(m2);
		
		Fsk4.info("Objeto duplicado");
	}
	
	protected static void acercar(Modelo m) {
		Planta h = getPlanta();
		if (h.getContenido().indexOf(m) < h.getContenido().size() - 1) {
			int newpos = h.getContenido().indexOf(m) + 1;
			h.getContenido().remove(m);
			h.getContenido().add(newpos, m);
			
			OMNode g = ((OMSVGGElement)payload.get(m)).getParentNode();
			
			OMNode ref = null;
			OMNode aux = null;
			OMNode auxmas1 = null;
			for (int i = g.getChildNodes().getLength() - 1; i > 0; i--) {				
				if (payload.get(g.getChildNodes().getItem(i)) != null && !payload.get(g.getChildNodes().getItem(i)).equals(m)) {
					if (aux != null) auxmas1 = aux;
					aux = g.getChildNodes().getItem(i);
				}
				if (g.getChildNodes().getItem(i).equals(payload.get(m))) ref = auxmas1;
			}
			g.removeChild((OMNode) payload.get(m));
			if (ref != null) g.insertBefore((OMNode) payload.get(m), ref);
			else g.appendChild((OMNode) payload.get(m));
		}
	}

	protected static HasGraphicalHandlers ultimoMouseDownBoton;

	private static void clickable(final HasGraphicalHandlers bb, final Runnable runnable) {
		
		bb.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
				log(event);
				
				System.out.println("mouse down en boton");
				
				ultimoMouseDownBoton = bb;
				
			}
		});

		bb.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				event.preventDefault();
				log(event);
				//mu(event.getClientX(), event.getClientY());
				
				if (bb.equals(ultimoMouseDownBoton)) runnable.run();				
			}
		});
		
		bb.addTouchStartHandler(new TouchStartHandler() {
			
			@Override
			public void onTouchStart(TouchStartEvent event) {
				event.preventDefault();
				log(event);
								
				ultimoMouseDownBoton = bb;

			}
		});
		
		bb.addTouchEndHandler(new TouchEndHandler() {
			
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				event.preventDefault();
				log(event);

				Fsk4.info("touchend");
				
				if (bb.equals(ultimoMouseDownBoton)) runnable.run();				
			}
		});

		/*

		bb.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				event.preventDefault();
				log(event);
				
				mu(event.getClientX(), event.getClientY());
				
				runnable.run();				
			}
		});

		bb.addTouchEndHandler(new TouchEndHandler() {
			
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				event.preventDefault();
				log(event);
				
				Fsk4.info("touchend");
				
				if (event.getTouches() != null && event.getTouches().length() > 0) {
					Touch t = event.getTouches().get(0);
					mu(t.getClientX(), t.getClientY());
				}				
				
				runnable.run();				
			}
		});

	*/
	}


	protected static void clickEnFavorito() {
		if (seleccionado instanceof Modelo) {
			AccionHelper.autenticar(new Runnable() {
				
				@Override
				public void run() {
					Fsk4.getServicio().hacerFavorito("articulo", Datos.get().getEmail(), ((Modelo) seleccionado).getId(), new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							((Modelo) seleccionado).setFavorito(result);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
						}
					});
				}
			});
		}
	}

	
	protected static void clickEnEliminar() {
		
		if (seleccionado != null && !(seleccionado instanceof Planta)  && !(seleccionado instanceof Plano)) {
			
			boolean ok = true;
			if (seleccionado instanceof FuenteLuz) {
				int numFuentesLuz = 0;
				for (Objeto o : getPlanta().getContenido()) {
					if (o instanceof FuenteLuz) numFuentesLuz++;
				}
				if (numFuentesLuz < 3) {
					Fsk4.alert("Debe haber un mínimo de 2 fuentes de luz en la habitación.");
					ok = false;
				}
			}
			
			if (ok) {
				Object eliminable = seleccionado;
				
				limpiarSeleccion();
				
				if (eliminable instanceof Vertice || eliminable instanceof Pared) {
					Planta h = getPlanta();
						if (eliminable instanceof Vertice) {
							List<Pared> paredes = null;
							Pared px = null;
							Pared py = null;
							for (Pared p : h.getParedes()) {
								if (p.getA().equals(eliminable)) {
									px = p;
									paredes = h.getParedes();
									py = h.getSiguientePared(px);
								}
							}
							if (px != null && h.getParedes().size() <= 3) {
								Fsk4.alert("La habitación debe tener al menos 3 paredes.");
							} else {
								if (px == null) {
									for (List<Pared> pz : h.getParedesInteriores()) {
										for (Pared p : pz) {
											if (p.getA().equals(eliminable)) {
												px = p;
												paredes = pz;

												boolean cerrado = false;
												if (paredes.size() > 2) {
													Vertice v0 = paredes.get(0).getDe();
													Vertice v1 = paredes.get(paredes.size() - 1).getA();
													cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
												}

												int pos = paredes.indexOf(p);
												py = (pos == paredes.size() - 1)?((cerrado)?paredes.get(0):null):paredes.get(pos + 1);

												break;
											}
										}
									}
								}
								if (px == null) {
									for (List<Pared> pz : h.getParedesInteriores()) {
										for (Pared p : pz) {
											if (p.getDe().equals(eliminable)) {
												px = p;
												paredes = pz;
												break;
											} 
										}
									}
								}
								if (py != null) {
									unirPared(px, py);
								} else {
									if (paredes.size() == 1) h.getParedesInteriores().remove(paredes);
									if (paredes.size() == 1 || px.getDe().equals(eliminable)) {
										eliminarDeSvg(px.getDe());
									}
									if (paredes.size() == 1 || px.getA().equals(eliminable)) {
										eliminarDeSvg(px.getA());
									}
									paredes.remove(px);
									eliminarDeSvg(px);
									((OMSVGGElement)cotas.get(px)).getParentNode().removeChild((OMNode) cotas.get(px));
									cotas.remove(px);
									for (Puerta p : px.getPuertas()) eliminarDeSvg(p); 
								}
							}
						} else if (eliminable instanceof Pared) {
							List<Pared> paredes = null;
							Pared px = null;
							Pared py = null;
							for (Pared p : h.getParedes()) {
								if (p.getA().equals(eliminable)) {
									px = p;
									paredes = h.getParedes();
									py = h.getSiguientePared(px);
								}
							}
							if (px != null && h.getParedes().size() <= 3) {
								Fsk4.alert("La habitación debe tener al menos 3 paredes.");
							} else {
								for (List<Pared> pz : h.getParedesInteriores()) {
									for (Pared p : pz) {
										if (p.equals(eliminable)) {
											px = p;
											paredes = pz;
											
											boolean cerrado = false;
											if (paredes.size() > 2) {
												Vertice v0 = paredes.get(0).getDe();
												Vertice v1 = paredes.get(paredes.size() - 1).getA();
												cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
											}

											int pos = paredes.indexOf(p);
											py = (pos == paredes.size() - 1)?((cerrado)?paredes.get(0):null):paredes.get(pos + 1);
												
											break;
										}
									}
								}
								if (px != null) {				
									if (py != null) {
										unirPared(px, py);
									} else {
										if (paredes.size() == 1) h.getParedesInteriores().remove(paredes);
										if (paredes.size() == 1 || px.getDe().equals(eliminable)) {
											eliminarDeSvg(px.getDe());
										}
										if (paredes.size() == 1 || px.getA().equals(eliminable)) {
											eliminarDeSvg(px.getA());
										}
										paredes.remove(px);
										eliminarDeSvg(px);
										((OMSVGGElement)cotas.get(px)).getParentNode().removeChild((OMNode) cotas.get(px));
										cotas.remove(px);
										for (Puerta p : px.getPuertas()) eliminarDeSvg(p); 
									}
								}
							}
						}
				} else { 
					
					getPlanta().getContenido().remove(eliminable);
					if (eliminable instanceof Puerta) ((Puerta) eliminable).getPared().getPuertas().remove(eliminable);
					if (eliminable instanceof Escalera) getPlanta().getEscaleras().remove(eliminable);
					
					eliminarDeSvg(eliminable);
				}

				if (seleccionado instanceof Modelo) {
					imagenesModelos.remove(seleccionado);
				}
				
				if (payload.get(seleccionado) != null) {
					payload.remove(payload.get(seleccionado));
					payload.remove(seleccionado);
				}
				
				refrescar(getPlanta());
				refrescaPreview();				
			}

		}
		
	}



	private static void eliminarDeSvg(Object s) {
		OMSVGGElement g = (OMSVGGElement) payload.get(s);
		payload.remove(s);
		payload.remove(g);
		g.getParentNode().removeChild(g);
		if (payloadAlzado != null && payloadAlzado.get(s) != null) {
			g = (OMSVGGElement)payloadAlzado.get(s);
			payloadAlzado.remove(s);
			g.getParentNode().removeChild(g);			
		}
		if (s instanceof Escalera) {
			for (AnguloEscalera v : ((Escalera)s).getAngulos()) {
				DibujoHelper.payload.remove(DibujoHelper.payload.remove(v));
			}
		}
	}


	public static void actualizarInfo() {
		
		SVGHelper.log("actualizarinfo");
		
		if (true) {
			
			setRefrescoActivo(false);
			
			$("#info-camara").hide();
			$("#info-bombilla").hide();
			$("#info-habitacion").hide();
			$("#info-columna").hide();
			$("#info-puerta").hide();
			$("#info-ventana").hide();
			$("#info-pared").hide();
			$("#info-plano").hide();
			$("#info-modelo").hide();
			
			if (seleccionado instanceof Camara) {
				$("#camara_altura").val("" + (Math.round(100d * ((Camara) seleccionado).getZ() / ESCALA) / 100d));			
				$("#camara_tilt").val("" + ((Camara) seleccionado).getTilt()).change();			
				$("#camara_radioapertura").val("" + ((Camara) seleccionado).getRadioApertura()).change();
				$("#camara_anchosensor").val("" + ((Camara) seleccionado).getAnchoSensor()).change();
				$("#camara_distanciasensor").val("" + ((Camara) seleccionado).getDistanciaSensor()).change();
				$("#camara_tiempoexposicion").val("" + ((Camara) seleccionado).getTiempoExposicion()).change();
				$("#info-camara").show();
			} else if (seleccionado instanceof FuenteLuz) {
				$("#bombilla_altura").val("" + (Math.round(100d * ((FuenteLuz) seleccionado).getZ() / ESCALA) / 100d));
				$("#bombilla_lumens").val("" + ((FuenteLuz) seleccionado).getLumens());
				$("#info-bombilla").show();
			} else if (seleccionado instanceof Planta) {			
				/*
				actualizarInfo("habitacion_revestimientosuelo", ((Habitacion) seleccionado).getRevestimientoSuelo());
				actualizarInfo("habitacion_revestimientoparedes", ((Habitacion) seleccionado).getRevestimientoParedes());
				actualizarInfo("habitacion_cenefa", ((Habitacion) seleccionado).getCenefa());
				$("#habitacion_tienecenefa").attr("checked", ((Habitacion) seleccionado).isConCenefa());
				$("#habitacion_alturacenefa").val("" + (Math.round(100d * ((Habitacion) seleccionado).getAlturaCenefa() / ESCALA) / 100d));
				$("#habitacion_alturaparedes").val("" + (Math.round(100d * ((Habitacion) seleccionado).getAlturaParedes() / ESCALA) / 100d));
				actualizarInfo("habitacion_revestimientosobrecenefa", ((Habitacion) seleccionado).getRevestimientoParedesSobreCenefa());
				
				if (((Habitacion) seleccionado).isConCenefa()) $(".cenefa").show();
				else $(".cenefa").hide();
				*/
				$("#info-habitacion").show();
			} else if (seleccionado instanceof Ventana) {
				$("#ventana_altura").val("" + (Math.round(100d * ((Ventana) seleccionado).getZ() / ESCALA) / 100d));
				$("#ventana_alto").val("" + (Math.round(100d * ((Ventana) seleccionado).getAlto() / ESCALA) / 100d));
				$("#ventana_ancho").val("" + (Math.round(100d * ((Ventana) seleccionado).getAncho() / ESCALA) / 100d));
				$("#ventana_distanciaparedizda").val("" + (Math.round(100d * ((Ventana) seleccionado).getDistanciaDesdeDe() / ESCALA) / 100d));
				$("#ventana_pomoalaizda").attr("checked", ((Puerta) seleccionado).isPomoALaIzquierda());
				$("#ventana_numerohojas").val("" + ((Ventana)seleccionado).getHojas());
				$("#info-ventana").show();
			} else if (seleccionado instanceof Puerta) {
				$("#puerta_alto").val("" + (Math.round(100d * ((Puerta) seleccionado).getAlto() / ESCALA) / 100d));
				$("#puerta_ancho").val("" + (Math.round(100d * ((Puerta) seleccionado).getAncho() / ESCALA) / 100d));
				$("#puerta_distanciaparedizda").val("" + (Math.round(100d * ((Puerta) seleccionado).getDistanciaDesdeDe() / ESCALA) / 100d));
				$("#puerta_pomoalaizda").attr("checked", ((Puerta) seleccionado).isPomoALaIzquierda());
				$("#puerta_numerohojas").val("" + ((Puerta)seleccionado).getHojas());
				$("#info-puerta").show();
			} else if (seleccionado instanceof Pared) {
				Pared p = ((Pared) seleccionado);
				$("#pared_longitud").val("" + (Math.round(100d * new Vector2d(p.getA().getX() - p.getDe().getX(), p.getA().getY() - p.getDe().getY()).length() / ESCALA) / 100d));
				$("#info-pared").show();
			} else if (seleccionado instanceof Plano) {
				
				SVGHelper.log("actualizarinfo.plano");
				
				
				$("#plano_zoom").val("" + ((Plano)seleccionado).getZoom()).change();

				$("#plano_luzsolar").attr("checked", ((Plano)seleccionado).isLuzSolar());

				$("#plano_luzambiente").attr("checked", ((Plano)seleccionado).isLuzAmbiente());

				$("#plano_pintartecho").attr("checked", ((Plano)seleccionado).isPintarTecho());

				$("#plano_direccionsol").val(((Plano)seleccionado).getSundir());
				$("#plano_turbidity").val("" + ((Plano)seleccionado).getTurbidity()).change();
				$("#plano_extraatmosferico").attr("checked", ((Plano)seleccionado).isExtraAtmospheric());
				$("#plano_modeloluzsolar").val(((Plano)seleccionado).getSkylightModel());
				
				$("#plano_tonemapping").val(((Plano)seleccionado).getTonemapping());
				$("#plano_tmcamera_evadjust").val("" + ((Plano)seleccionado).getToneMappingCameraEVAdjust()).change();
				$("#plano_tmcamera_filmiso").val("" + ((Plano)seleccionado).getToneMappingCameraFilmISO()).change();
				$("#plano_tmcamera_funcionrespuesta").val(((Plano)seleccionado).getToneMappingCameraResponseFunctionPath());
				$("#plano_tmlinear_scale").val("" + ((Plano)seleccionado).getToneMappingLinearScale()).change();
				$("#plano_tmreinhard_burn").val("" + ((Plano)seleccionado).getToneMappingReinhardBurn()).change();
				$("#plano_tmreinhard_postscale").val("" + ((Plano)seleccionado).getToneMappingReinhardPostScale()).change();
				$("#plano_tmreinhard_prescale").val("" + ((Plano)seleccionado).getToneMappingReinhardPreScale()).change();

				$(".info-tonemapping").hide();
				$(".info-tonemapping." + ((Plano)seleccionado).getTonemapping()).show();

				$("#info-plano").show();
			} else if (seleccionado instanceof Modelo) {
				actualizarInfo("modelo_articulo", ((Modelo) seleccionado));
				$("#modelo_altura").val("" + (Math.round(100d * ((Objeto) seleccionado).getZ() / ESCALA) / 100d));
				$("#info-modelo").show();
			}
			
			setRefrescoActivo(true);

		}

	}

	private static void actualizarInfo(String id, Modelo m) {
		PantallaHelper.getCampoArticulo(id).setModelo(m);
	}

	private static void actualizarInfo(String id, Material m) {
		PantallaHelper.getCampoTextura(id).setMaterial(m);
	}

	protected static double zoom0;
	protected static boolean zoomeando;

	private static OMSVGRectElement gzoomslider;

	private static OMSVGGElement gcamara;

	static Modelo startDragPendiente;

	private static SVGSlider sliderActivo;

	private static OMSVGImageElement imglock;

	public static OMSVGGElement texturaEnDrag;

	private static OMSVGPatternElement patSuelo;

	private static OMSVGPatternElement patParedes;

	private static OMSVGImageElement imgPatronParedes;

	static Map<Pared, OMSVGPatternElement> pats;

	static Map<Pared, OMSVGImageElement> imgs;
	
	static Map<Modelo, OMSVGImageElement> imagenesModelos;

	private static OMSVGImageElement imgvolumen;

	public static boolean creandoVolumen;

	
	protected static Vertice ultimoVerticeParedes;

	private static OMSVGDefsElement def;

	private static OMSVGGElement gcapacontenido;

	private static OMSVGGElement gcapaluces;

	private static OMSVGGElement gcapacamara;

	private static OMSVGGElement gcapahabitaciones;

	private static OMSVGImageElement help1;

	private static OMSVGImageElement imgvigas;

	private static OMSVGGElement gflechassol;

	private static OMSVGPatternElement patExterior;

	private static OMSVGImageElement imgPatronExterior;

	private static OMSVGRectElement backgroundDialogo;

	private static OMSVGImageElement imgexterior;

	private static OMSVGGElement gtoolbar;

	static SVGHelp ghelp;

	private static boolean primeraVez = true;

	private static OMSVGGElement gojo;

	private static OMSVGPatternElement patInteriorParedes;

	private static OMSVGElement dibujar(Camara camara) {
		gojo = getDoc().createSVGGElement();

		actualizarOjo();
		
		return gojo;
	}

	private static void actualizarOjo() {
		
		List<OMNode> zz = new ArrayList<OMNode>();
		for (OMNode z : gojo.getChildNodes()) zz.add(z);
		for (OMNode z : zz) gojo.removeChild(z);

		
		double angulo = Math.atan2(getPlano().getCamara().getDistanciaSensor(), getPlano().getCamara().getAnchoSensor() / 2);
		
		Punto p0 = new Punto(-1 * 45 * Math.cos(angulo), -1 * 45 * Math.sin(angulo));
		Punto p1 = new Punto(1 * 45 * Math.cos(angulo), -1 * 45 * Math.sin(angulo));

		OMSVGLineElement l;
		gojo.appendChild(l = getDoc().createSVGLineElement(0, 0, new Float(p0.getX()), new Float(p0.getY())));
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		
		gojo.appendChild(l = getDoc().createSVGLineElement(0, 0, new Float(p1.getX()), new Float(p1.getY())));
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		
		p0 = new Punto(-1 * 30 * Math.cos(angulo), -1 * 30 * Math.sin(angulo));
		p1 = new Punto(1 * 30 * Math.cos(angulo), -1 * 30 * Math.sin(angulo));

		OMSVGPathElement p;
		gojo.appendChild(p = getDoc().createSVGPathElement());
		OMSVGPathSegList segs = p.getPathSegList();
		segs.appendItem(p.createSVGPathSegMovetoAbs(0, 0));
		segs.appendItem(p.createSVGPathSegLinetoAbs(new Float(p0.getX()), new Float(p0.getY())));
        segs.appendItem(p.createSVGPathSegArcAbs(new Float(p1.getX()), new Float(p1.getY()), 30, 30, new Float(angulo), false, true));
        segs.appendItem(p.createSVGPathSegClosePath());
		p.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");

		p0 = new Punto(-1 * 20 * Math.cos(angulo), -1 * 20 * Math.sin(angulo));
		p1 = new Punto(1 * 20 * Math.cos(angulo), -1 * 20 * Math.sin(angulo));

		gojo.appendChild(p = getDoc().createSVGPathElement());
		segs = p.getPathSegList();
		segs.appendItem(p.createSVGPathSegMovetoAbs(0, 0));
		segs.appendItem(p.createSVGPathSegLinetoAbs(new Float(p0.getX()), new Float(p0.getY())));
        segs.appendItem(p.createSVGPathSegArcAbs(new Float(p1.getX()), new Float(p1.getY()), 20, 20, new Float(angulo), false, true));
        segs.appendItem(p.createSVGPathSegClosePath());
		p.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#000000");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");	
	}

	private static OMSVGGElement crearZoom() {
		OMSVGGElement g = getDoc().createSVGGElement();
		
		OMSVGRectElement r;
		g.appendChild(r = getDoc().createSVGRectElement(0,  0, 200, 40, 20, 20));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#FCFCFC");
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#C2C2C2");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		
		OMSVGImageElement im;
		g.appendChild(im = getDoc().createSVGImageElement(30, 21, 18, 18, "img/glyphicons_halflings_015_zoom-out@2x.png"));
		
		clickable(im, new Runnable() {
			public void run() {
				getPlano().setZoom(getPlano().getZoom() * 0.9f);
				DibujoHelper.zoom();
				gzoomslider.setAttribute("transform", "translate(" + (35 + (getPlano().getZoom() * 120 / 3)) + ", 30)");
			}
		});
		
		g.appendChild(im = getDoc().createSVGImageElement(160, 21, 18, 18, "img/glyphicons_halflings_014_zoom-in@2x.png"));

		clickable(im, new Runnable() {
			public void run() {
				getPlano().setZoom(getPlano().getZoom() * 1.1f);
				DibujoHelper.zoom();
				gzoomslider.setAttribute("transform", "translate(" + (35 + (getPlano().getZoom() * 120 / 3)) + ", 30)");
			}
		});

		OMSVGLineElement lin;
		g.appendChild(lin = getDoc().createSVGLineElement(45, 30, 155, 30));
		lin.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");
		lin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		lin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		
		g.appendChild(gzoomslider = getDoc().createSVGRectElement(-5, -5, 10, 10, 0, 0));
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
					
					limpiarSeleccion();

					zoomeando = true;
					setClientX0(event.getNativeEvent().getClientX());
					setClientY0(event.getNativeEvent().getClientY());
					zoom0 = getPlano().getZoom();
				}


			}
		});
		
		gzoomslider.setAttribute("transform", "translate(" + (35 + (getPlano().getZoom() * 120 / 3)) + ", 30)");
		
		return g;
	}

	private static OMSVGGElement crearBotonToolbar(final String urlimgtrue, final String urlimgfalse, final Check check, String txt, final Runnable r) {		
		OMSVGGElement g = getDoc().createSVGGElement();
		
		final OMSVGRectElement rect;
		g.appendChild(rect = getDoc().createSVGRectElement(0, 2.5f, 150, 25, 12.5f, 12.5f));
        rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.5");
        rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
 		
		OMSVGTextElement t;
		g.appendChild(t = getDoc().createSVGTextElement(40, 20, OMSVGLength.SVG_LENGTHTYPE_PX, txt));
		t.setAttribute("pointer-events", "none");
		
		final OMSVGCircleElement c;
		g.appendChild(c = getDoc().createSVGCircleElement(16, 16, 16));
        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1");
        c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		
        final OMSVGImageElement img;
		g.appendChild(img = doc.createSVGImageElement(6, 6, 20, 20, (check.esCierto())?urlimgtrue:urlimgfalse));
		
		g.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
		        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ff0000");
		        rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1");
			}
		});

		g.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
		        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		        rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.5");
			}
		});

		g.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				r.run();
				img.setAttribute("href", (check.esCierto())?urlimgtrue:urlimgfalse);
			}
		});
		return g;
	}

	public static OMSVGPoint globalToPlano(float x, float y) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x);
		pt.setY(y);
		//System.out.print(pt.getDescription());
		OMSVGPoint planoPoint = pt.matrixTransform(((OMSVGGElement)payload.get(getPlano())).getScreenCTM().inverse());
		//System.out.println("-->" + pt.getDescription());
		/*
		System.out.print(roomPoint.getDescription());
		roomPoint = roomPoint.scale(new Float(1 / getPlano().getZoom()));
		System.out.println("-->" + roomPoint.getDescription());
*/
		return planoPoint;
	}

	protected static OMSVGPoint globalToPlano(int x, int y) {
		return globalToPlano(new Float(x), new Float(y));
	}


	protected static OMSVGPoint globalToSvg(int x02, int y02) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint planoPoint = pt.matrixTransform(getSvg().getScreenCTM().inverse());
		return planoPoint;
	}

	protected static OMSVGPoint globalToCameraTilter(int x, int y) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x);
		pt.setY(y);
		OMSVGPoint roomPoint = pt.matrixTransform(gcam.getScreenCTM().inverse());
		return roomPoint;
	}

	protected static OMSVGPoint globalToCamera(int x, int y) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x);
		pt.setY(y);
		//System.out.print(pt.getDescription());
		OMSVGPoint roomPoint = pt.matrixTransform(((OMSVGGElement)payload.get(getPlano().getCamara())).getScreenCTM().inverse());
		//System.out.println("-->" + pt.getDescription());
		/*
		System.out.print(roomPoint.getDescription());
		roomPoint = roomPoint.scale(new Float(1 / getPlano().getZoom()));
		System.out.println("-->" + roomPoint.getDescription());
*/
		return roomPoint; 
	}

	protected static OMSVGPoint globalToRoom(int x02, int y02) {
		return globalToRoom(new Float(x02), new Float(y02));
	}
	protected static OMSVGPoint globalToRoom(float x02, float y02) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		//System.out.print(pt.getDescription());
		OMSVGPoint roomPoint = pt.matrixTransform(((OMSVGGElement)payload.get(getPlanta())).getScreenCTM().inverse());
		//System.out.println("-->" + pt.getDescription());
		/*
		System.out.print(roomPoint.getDescription());
		roomPoint = roomPoint.scale(new Float(1 / getPlano().getZoom()));
		System.out.println("-->" + roomPoint.getDescription());
*/
		return roomPoint;
	}
	
	protected static OMSVGPoint globalToRoom(OMSVGPoint p) {
		return globalToRoom(p.getX(), p.getY());
	}

	protected static OMSVGPoint localToGlobal(OMSVGGElement g, float x02, float y02) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(g.getScreenCTM());
		return roomPoint;
	}
	
	protected static OMSVGPoint globalToLocal(OMSVGGElement g, float x02, float y02) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(g.getScreenCTM().inverse());
		return roomPoint;
	}

	protected static OMSVGPoint roomToGlobal(int x02, int y02) {
		OMSVGPoint pt = getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint globalPoint = pt.matrixTransform(((OMSVGGElement)payload.get(getPlanta())).getScreenCTM());

		return globalPoint;
	}
	
	protected static void refrescar(Planta h) {
		OMSVGPathElement path = (OMSVGPathElement) paths.get(h);
		OMSVGPathSegList segs = path.getPathSegList();
		segs.clear();
        fillSegs(path, segs, h);	

        refrescarParedes(h.getParedes());
        
        for (List<Pared> paredes : h.getParedesInteriores()) refrescarParedes(paredes);

	}
	
	protected static void refrescarParedes(List<Pared> paredes) {
		boolean cerrado = false;
		if (paredes.size() > 2) {
			Vertice v0 = paredes.get(0).getDe();
			Vertice v1 = paredes.get(paredes.size() - 1).getA();
			cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
		}
        int pos = 0;
        for (Pared px : paredes) {
        	OMSVGPathElement  path = (OMSVGPathElement) paths.get(px);
    		if (px.getDe().mismaPosicion(px.getA())) {
    			path.setAttribute("visibility", "hidden");
    		} else {
    			path.setAttribute("visibility", "visible");

    			OMSVGPathSegList segs = path.getPathSegList();
        		segs.clear();
        		
                fillSegs(path, segs, px, (pos == 0)?((cerrado)?paredes.get(paredes.size() - 1):null):paredes.get(pos - 1), (pos == paredes.size() - 1)?((cerrado)?paredes.get(0):null):paredes.get(pos + 1));
    		}
            
            actualizarCotas(px);
            
            pos++;
        }
        
	}
	
	private static void fillSegs(OMSVGPathElement path, OMSVGPathSegList segs, Pared p, Pared antes, Pared despues) {
		double ancho = p.getAncho();
		
		Linea a = (antes != null)?getParalela(antes.getDe(), antes.getA(), ancho):null;
		Linea b = getParalela(p.getDe(), p.getA(), ancho);
		Linea c = (despues != null)?getParalela(despues.getDe(), despues.getA(), ancho):null;
		
		List<Punto> puntos = new ArrayList<Punto>();
		puntos.add(new Punto(p.getDe().getX(), p.getDe().getY()));
		double ab = b.direction().angle(new Vector2d(1,0));
		double aa = (a != null)?a.direction().angle(new Vector2d(1,0)):ab;
		double ac = (c != null)?c.direction().angle(new Vector2d(1,0)):ab;
		if (Math.abs(aa - ab) > 0.1 && Math.abs(aa + ab) > 0.1) {
			Punto xxx = a.intersection(b, false);
			if (xxx != null) puntos.add(a.intersection(b, false));
		} else {
			puntos.add(getPuntoPerpendicular(p.getDe(), p.getDe(), p.getA(), ancho));
		}
		if (Math.abs(ab - ac) > 0.1 && Math.abs(ab + ac) > 0.1) {
			Punto xxx = b.intersection(c, false);
			if (xxx != null)  puntos.add(b.intersection(c, false));
		} else {
			puntos.add(getPuntoPerpendicular(p.getA(), p.getDe(), p.getA(), ancho));
		}
		puntos.add(new Punto(p.getA().getX(), p.getA().getY()));
		puntos.add(new Punto(p.getDe().getX(), p.getDe().getY()));
		
		segs.appendItem(path.createSVGPathSegMovetoAbs(new Float(puntos.get(0).getX()), new Float(puntos.get(0).getY())));
		for (int pos = 0; pos < puntos.size() - 1; pos++) {
	        segs.appendItem(path.createSVGPathSegLinetoAbs(new Float(puntos.get(pos + 1).getX()), new Float(puntos.get(pos + 1).getY())));
		}
         
         segs.appendItem(path.createSVGPathSegClosePath());	
   }


	private static void fillSegs(OMSVGPathElement path, OMSVGPathSegList segs,
			Planta h) {
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



	
	public static Punto getPuntoPerpendicular(Vertice xx, Vertice de, Vertice a, double ancho) {
		Vector2d v0 = new Vector2d(a.getX() - de.getX(), a.getY() - de.getY());
		v0.normalize();
		Vector2d v0p = new Vector2d(-1 * v0.y, v0.x);
		Punto p01 = new Punto(xx.getX() - ancho * v0p.x, xx.getY() - ancho * v0p.y);
		return p01;
	}



	public static Linea getParalela(Vertice de, Vertice a, double ancho) {
		Vector2d v0 = new Vector2d(a.getX() - de.getX(), a.getY() - de.getY());
		v0.normalize();
		Vector2d v0p = new Vector2d(-1 * v0.y, v0.x);
		Punto p01 = new Punto(de.getX() - ancho * v0p.x, de.getY() - ancho * v0p.y);
		Punto p02 = new Punto(p01.getX() - 2000 * v0.x, p01.getY() - 2000 * v0.y);
		Punto p03 = new Punto(p01.getX() + 2000 * v0.x, p01.getY() + 2000 * v0.y);
		Linea l0 = new Linea(p02, p03);
		return l0;
	}
	
	protected static void arrastrarParedA(Pared pared, Punto punto) {
		SVGHelper.arrastrarParedA(pared, punto);
	}


	private static Pared getParedAnterior(List<Pared> paredes, Pared px) {
		boolean cerrado = false;
		if (paredes.size() > 2) {
			Vertice v0 = paredes.get(0).getDe();
			Vertice v1 = paredes.get(paredes.size() - 1).getA();
			cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
		}
        int pos = paredes.indexOf(px);
		return (pos == 0)?((cerrado)?paredes.get(paredes.size() - 1):null):paredes.get(pos - 1);
	}

	private static Pared getSiguientePared(List<Pared> paredes, Pared px) {
		boolean cerrado = false;
		if (paredes.size() > 2) {
			Vertice v0 = paredes.get(0).getDe();
			Vertice v1 = paredes.get(paredes.size() - 1).getA();
			cerrado = v0.getX() == v1.getX() && v0.getY() == v1.getY();
		}
        int pos = paredes.indexOf(px);
		return (pos == paredes.size() - 1)?((cerrado)?paredes.get(0):null):paredes.get(pos + 1);
	}

	private static void unirSiPosible(Pared p0, Pared p1) {
		if (p0 != null && p1 != null) {
			Linea l0 = new Linea(p0);
			Linea l1 = new Linea(p1);
			if (l0.esParalela(l1)) {
				// son paralelas --> son la misma línea
				unirPared(p0, p1);
			}
		}
	}


	private static void unirPared(Pared p0, Pared p1) {
		Planta h = getPlanta();
		
		List<Pared> paredes = null;
		if (h.getParedes().contains(p0)) paredes = h.getParedes();
		else {
			for (List<Pared> pz : h.getParedesInteriores()) {
				if (pz.contains(p0)) {
					paredes = pz;
					break;
				}
			}
		}
		
		if (paredes.indexOf(p0) < paredes.indexOf(p1)) {
			p0.setA(p1.getA());			
		} else {
			// p1 va antes que p0
			p0.setDe(p1.getDe());
		}
		paredes.remove(p1);
		eliminarDeSvg(p1);
		((OMSVGGElement)cotas.get(p1)).getParentNode().removeChild((OMNode) cotas.get(p1));
		cotas.remove(p1);
		p0.getPuertas().addAll(p1.getPuertas());
		for (Puerta px : p1.getPuertas()) px.setPared(p0);
		eliminarDeSvg(p1.getDe());
	}

	public static OMSVGPoint roomToGlobal(IPosicionable p) {
		return roomToGlobal(new Double(p.getX()).intValue(), new Double(p.getY()).intValue());
	}



	public static OMSVGPoint roomToGlobal(Punto p) {
		return roomToGlobal(new Double(p.getX()).intValue(), new Double(p.getY()).intValue());
	}

	private static void actualizarCotas(Pared px) {
		OMSVGGElement g = (OMSVGGElement) cotas.get(px);
		
		if (px.getDe().mismaPosicion(px.getA())) {
			g.setAttribute("visibility", "hidden");
		} else {
			g.setAttribute("visibility", "visible");
			
			List<OMNode> zz = new ArrayList<OMNode>();
			for (OMNode z : g.getChildNodes()) zz.add(z);
			for (OMNode z : zz) g.removeChild(z);
			
			Punto p0 = getPuntoPerpendicular(px.getDe(), px.getDe(), px.getA(), 35);
			Punto p1 = getPuntoPerpendicular(px.getA(), px.getDe(), px.getA(), 35);

			OMSVGLineElement l;
			g.appendChild(l = getDoc().createSVGLineElement(new Float(px.getDe().getX()).floatValue(), new Float(px.getDe().getY()).floatValue(), new Float(p0.getX()).floatValue(), new Float(p0.getY()).floatValue()));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			g.appendChild(l = getDoc().createSVGLineElement(new Float(px.getA().getX()).floatValue(), new Float(px.getA().getY()).floatValue(), new Float(p1.getX()).floatValue(), new Float(p1.getY()).floatValue()));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			
			p0 = getPuntoPerpendicular(px.getDe(), px.getDe(), px.getA(), 30);
			p1 = getPuntoPerpendicular(px.getA(), px.getDe(), px.getA(), 30);

			g.appendChild(l = getDoc().createSVGLineElement(new Float(p0.getX()).floatValue(), new Float(p0.getY()).floatValue(), new Float(p1.getX()).floatValue(), new Float(p1.getY()).floatValue()));
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	        l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
			
			p0 = getPuntoPerpendicular(px.getDe(), px.getDe(), px.getA(), 32);
			p1 = getPuntoPerpendicular(px.getA(), px.getDe(), px.getA(), 32);

			OMSVGTextElement t = getDoc().createSVGTextElement(0f, 0f, OMSVGLength.SVG_LENGTHTYPE_PX, 
					"" + (Math.round(100d * Math.sqrt(((px.getA().getX() - px.getDe().getX()) * (px.getA().getX() - px.getDe().getX())) + ((px.getA().getY() - px.getDe().getY()) * (px.getA().getY() - px.getDe().getY()))) / ESCALA) / 100d) + "m.");
			t.setAttribute("pointer-events", "none");
			//t.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "0");
			String auxs = "translate(" + (0 + (p1.getX() + p0.getX()) / 2) + "," + (0 + (p1.getY() + p0.getY()) / 2) + ")";
			//auxs += " rotate(" + ((IRotable)p).getPan() + " 0 0)";
			auxs += " rotate(0 0 0)";
			t.setAttribute("transform", auxs);
			g.appendChild(t);
		}

	}

	
	private static void dibujar(Sol s) {
		OMNode svgb = Recursos.INSTANCIA.svgSol().getSvg();
		
		Object o = svgb;
		System.out.println(o.getClass().getName());
		
		OMSVGGElement auxg = null;
		for (OMNode xx : ((OMSVGSVGElement) o).getChildNodes()) {
			if (xx instanceof OMSVGGElement) auxg = (OMSVGGElement) xx;
		}
		auxg.setAttribute("transform", "translate(-50, -50)");
		
		gflechassol = doc.createSVGGElement();
		for (int i = 0; i < 3; i++) {			
			OMSVGPathElement p;
			gflechassol.appendChild(p = doc.createSVGPathElement());
			p.getPathSegList().appendItem(p.createSVGPathSegMovetoAbs(60, i * 20));
			p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(130, i * 20));
			p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(110, (i * 20) - 5));
			p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(110, (i * 20) + 5));
			p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(130, (i * 20)));
    		p.getPathSegList().appendItem(p.createSVGPathSegClosePath());
    		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
    		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");

			if (i > 0) {
				gflechassol.appendChild(p = doc.createSVGPathElement());
				p.getPathSegList().appendItem(p.createSVGPathSegMovetoAbs(60, i * -20));
				p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(130, i * -20));
				p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(110, (i * -20) - 5));
				p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(110, (i * -20) + 5));
				p.getPathSegList().appendItem(p.createSVGPathSegLinetoAbs(130, (i * -20)));
	    		p.getPathSegList().appendItem(p.createSVGPathSegClosePath());
	    		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
	    		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
			}
		}
		

		OMSVGGElement g = doc.createSVGGElement();
		
		// circulo para centrado rotación
		OMSVGCircleElement cxx = getDoc().createSVGCircleElement(0f, 0f, new Float(500));
		cxx.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffff00");
		cxx.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0");
		cxx.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_OPACITY_PROPERTY, "0");
		cxx.setAttribute("pointer-events", "none");
        g.appendChild(cxx);
		
		g.appendChild(gflechassol);
		g.appendChild(auxg);
		
		
		if (!getPlano().getSol().isActivo()) auxg.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		Linea l = new Linea(new Punto(s.getX(), s.getY()), getPlano().getCentroHabitacion(getPlanta()));
		gflechassol.setAttribute("transform", "rotate(" + l.angulo() + ")");
		
		//OMSVGGElement gx = habilitar(s, g, gcapaluces);		
		
		actualizarSol();
	}
	

	public static void resetPlano(Plano p) {
		setPlano(p);
		seleccionarPlanta(p.getPlantas().get(0));		
	}

	private static void refrescarMenuPlantas() {
		StringBuffer m = new StringBuffer();
		for (int i = 0; i < getPlano().getPlantas().size(); i++) {
			m.append("<li><a href='#' class='selectorplanta' data-id='" + i + "'>Planta " + i + "</a></li>");
		}
		m.append("<li><a href=''#' class='anadirplanta'>Añadir planta</a></li><li><a href=''#' class='eliminarplanta'>Eliminar última planta</a></li>");
		$("#menuplantas").html(m.toString());
		$("#etiquetaplantas").html("Planta " + getPlano().getPlantas().indexOf(getPlanta()));
		$(".anadirplanta").click(new Function() {
			@Override
			public boolean f(Event e) {
				Planta np;
				getPlano().getPlantas().add(np = getPlano().getPlantas().get(getPlano().getPlantas().size() - 1).clone());
				//np.getContenido().add(new FuenteLuz(-30, 30, 130, 0, "1367379351780", "http://www.fescateva.com/fileviewer/1367379351781/bombilla.jpg", null));
				//np.getContenido().add(new FuenteLuz(30, 30, 30, 0, "1367379351780", "http://www.fescateva.com/fileviewer/1367379351781/bombilla.jpg", null));
				seleccionarPlanta(np);
				refrescarMenuPlantas();
				ClickHelper.cerrarMenu();
				return false;
			}
		});
		$(".eliminarplanta").click(new Function() {
			@Override
			public boolean f(Event e) {
				if (getPlano().getPlantas().size() == 1) {
					Window.alert("Tiene que haber al menos una planta.");
				} else if (Window.confirm("Seguro que quieres eliminar la última planta?")) {
					Planta px = getPlanta();
					Planta pz = getPlano().getPlantas().remove(getPlano().getPlantas().size() - 1);
					if (pz.equals(px)) {
						seleccionarPlanta(getPlano().getPlantas().get(getPlano().getPlantas().size() - 1));
					}
					refrescarMenuPlantas();
				}
				ClickHelper.cerrarMenu();
				return false;
			}
		});
		$(".selectorplanta").click(new Function() {
			@Override
			public boolean f(Event e) {
				seleccionarPlanta(getPlano().getPlantas().get(Integer.parseInt($(e.getEventTarget()).attr("data-id"))));
				ClickHelper.cerrarMenu();
				return false;
			}
		});
	}

	protected static void seleccionarPlanta(Planta planta) {
		SVGHelper.crearSvg();

		if (primeraVez) {
			$(".toolbar-boton.mobiliario").click();
			//$("#botoncamara").click();
			primeraVez = false;
		}

		setPlanta(planta);
		getPlano().setCamaraEnPlanta(getPlano().getPlantas().indexOf(planta));
		
		
		SVGHelper.dibujar(getPlanta());
		actualizarCamara();
		refrescaPreview();
		refrescarMenuPlantas();
	}

	public static void ocultar(Modelo o) {
		if (o.isVisible()) {
			((OMSVGGElement)payload.get(o)).getStyle().setDisplay(Display.NONE);
			if (payloadAlzado != null && payloadAlzado.get(o) != null) ((OMSVGGElement)payloadAlzado.get(o)).getStyle().setDisplay(Display.NONE);
		}
		else {
			((OMSVGGElement)payload.get(o)).getStyle().setDisplay(Display.BLOCK);
			if (payloadAlzado != null && payloadAlzado.get(o) != null) ((OMSVGGElement)payloadAlzado.get(o)).getStyle().setDisplay(Display.BLOCK);
		}
		
		o.setVisible(!o.isVisible());
	}

	public static void eliminar(Modelo o) {
		for (Planta h : getPlano().getPlantas()) {
			h.getContenido().remove(o);
		}
		eliminarDeSvg(o);
	}

	public static void setLongitudPared(Pared p, double l) {
		l = l * ESCALA;
		List<Pared> pz = DibujoHelper.getPlanta().getListaParedes(p);
		Pared siguientePared = null;
		int i = pz.indexOf(p);
		if (i == pz.size() - 1) siguientePared = pz.get(0);
		else siguientePared = pz.get(i + 1);
		arrastrarParedA(siguientePared, new Linea((Pared)p).getPunto(l));	
	}

	public static void zoom() {
		gplano.setAttribute("transform", "translate(" + getPlano().getX() + ", " + getPlano().getY() + "), scale(" + getPlano().getZoom() + ")");
	}

	public static void actualizaPuerta(Puerta pta) {
		OMSVGGElement g = (OMSVGGElement) payload.get(pta);
		
		Linea l = new Linea(pta.getPared());
		Punto p = l.getPunto(pta.getDistanciaDesdeDe());
		String auxs = "translate(" + p.getX() + "," + p.getY() + ")";
		auxs += " rotate(" + l.angulo() + " 0 0)";
		g.setAttribute("transform", auxs);

		OMSVGGElement gv = null;
		for (OMNode z : g.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gv")) gv = (OMSVGGElement) z;
		OMSVGGElement gpx = null;
		for (OMNode z : gv.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gpx")) gpx = (OMSVGGElement) z;
		OMSVGGElement gp = null;
		for (OMNode z : gpx.getChildNodes()) if (z instanceof OMSVGGElement && ((OMSVGGElement)z).getId().startsWith("gp")) gp = (OMSVGGElement) z;
		
		List<OMNode> zz = new ArrayList<OMNode>();
		for (OMNode z : gp.getChildNodes()) zz.add(z);
		for (OMNode z : zz) gp.removeChild(z);

		rellenarVentana(gp, pta);
		actualizarCotasPuerta(pta);
	}

	public static Object getSeleccionado() {
		return seleccionado;
	}

	public static boolean isRefrescoActivo() {
		return refrescoActivo;
	}

	public static void setRefrescoActivo(boolean refrescoActivo) {
		DibujoHelper.refrescoActivo = refrescoActivo;
	}

	public static void actualizaModelo() {
		getPlanta().getContenido().remove(seleccionado);
		eliminarDeSvg(seleccionado);
		Modelo m = (Modelo) seleccionado;
		seleccionado = anadirModelo("objeto", m.getId(), m.getImagenVistaPrevia(), m.getImagenParaPlano(), m.getAncho(), m.getAlto(), ((IPosicionable)seleccionado).getX(), ((IPosicionable)seleccionado).getY(), ((Modelo)seleccionado).getZ(), ((IRotable)seleccionado).getPan(), m.isFavorito(), m.getEscala(), m.getAltura(), m.getVista0(), m.getVista90(), m.getVista180(), m.getVista270());
		getPlanta().getContenido().add((Objeto)seleccionado);
		addContenido(m, getPlanta());						
		clientX0 = new Double(((IPosicionable)seleccionado).getX()).intValue();
		clientY0 = new Double(((IPosicionable)seleccionado).getY()).intValue();
		arrastradoX0 = clientX0;
		arrastradoY0 = clientY0;
		refrescaPreview();
	}

	public static void actualizaSuelo() {
		Material r = getPlanta().getHabitaciones().get(0).getRevestimientoSuelo();
		imgPatronSuelo.setAttribute("href", r.getTextura());
		imgPatronSuelo.setAttribute("width", "" + (r.getAncho() * ESCALA / 100));
		imgPatronSuelo.setAttribute("height", "" + (r.getAlto() * ESCALA / 100));
	}

	public static void actualizarCamara() {
//        gcapacamara.setAttribute("visibility", (getPlano().getCamara().isVisible())?"visible":"hidden");
//        gcapaluces.setAttribute("visibility", (getPlano().getCamara().isVisible())?"visible":"hidden");
		if (!getPlano().getCamara().isVisible()) {
			//limpiarSeleccion();
			AccionHelper.cerrarVistaPrevia();
			$("#bvistaprevia i").attr("class", "icon-eye-open");
		} else {
			//seleccionar(getPlano().getCamara());			
			AccionHelper.abrirVistaPrevia();
			$("#bvistaprevia i").attr("class", "icon-eye-close");			
		}
	}

	public static void actualizarSol() {
		((OMSVGGElement)payload.get(getPlano().getSol())).setAttribute("visibility", getPlano().isLuzSolar()?"visible":"hidden");
	}

	public static void startDragOn(Modelo m) {
		startDragPendiente = m;
	}

	public static OMSVGDocument getDoc() {
		return doc;
	}

	public static void setDoc(OMSVGDocument doc) {
		DibujoHelper.doc = doc;
	}

	public static void setSliderActivo(SVGSlider slider) {
		sliderActivo = slider;
	}

	public static void startDragOnTextura(String idArticulo, String preview, String cenital, double ancho, double alto) {
		OMSVGGElement g = getDoc().createSVGGElement();
		OMSVGImageElement i;
		//g.appendChild(i = getDoc().createSVGImageElement(0, 0, new Float(ancho), new Float(alto), cenital));
		g.appendChild(i = getDoc().createSVGImageElement(0, 0, 60, 60, cenital));
		g.setAttribute("data-id", idArticulo);
		g.setAttribute("data-preview", preview);
		g.setAttribute("data-cenital", cenital);
		g.setAttribute("data-ancho", "" + ancho);
		g.setAttribute("data-alto", "" + alto);
		g.setAttribute("pointer-events", "none");
		texturaEnDrag = g;
		getSvg().appendChild(g);
	}

	public static int getClientX0() {
		return clientX0;
	}

	public static void setClientX0(int clientX0) {
		DibujoHelper.clientX0 = clientX0;
	}

	public static int getClientY0() {
		return clientY0;
	}

	public static void setClientY0(int clientY0) {
		DibujoHelper.clientY0 = clientY0;
	}

	public static OMSVGSVGElement getSvg() {
		return svg;
	}

	public static void setSvg(OMSVGSVGElement svg) {
		DibujoHelper.svg = svg;
	}

	public static OMSVGRectElement getBackgroundDialogo() {
		return backgroundDialogo;
	}

	public static void setBackgroundDialogo(OMSVGRectElement backgroundDialogo) {
		DibujoHelper.backgroundDialogo = backgroundDialogo;
		if (backgroundDialogo != null) backgroundDialogo.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		});
	}

	public static void reset() {
		ghelp.hide();

		if (rectanguloArrastreCreacionVolumen != null) {
			((OMSVGGElement)payload.get(getPlanta())).removeChild(rectanguloArrastreCreacionVolumen);
			rectanguloArrastreCreacionVolumen = null;
		}
		if (gCreacionVolumen != null) {
			((OMSVGGElement)payload.get(getPlanta())).removeChild(gCreacionVolumen);
			gCreacionVolumen = null;
		}

		SVGHelper.creandoParedes = false;
		SVGHelper.nuevasParedes = null;		
		creandoVolumen = false;
		gplano.setAttribute("cursor", "auto");
		gCreacionVolumen = null;
		lineaCreacionVolumen = null;
		rectanguloArrastreCreacionVolumen = null;
		
		
		tilteandoCamara = false;
		slideandoCamara = false;
		zoomeando = false;
		svggarrastrado = null;
		sliderActivo = null;

		
		if (lh != null) lh.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
		if (lv != null) lv.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
		if (texturaEnDrag != null) {
			getSvg().removeChild(texturaEnDrag);
			texturaEnDrag = null;
			texturaDragginOn = null;
		}

	}

	public static Planta getPlanta() {
		return planta;
	}

	public static void setPlanta(Planta planta) {
		DibujoHelper.planta = planta;
	}

	public static void inicioCrearEscalera() {
		//finCrearVolumen();
		//finCrearParedes();
		finCrearEscalera();
		
		creandoEscalera = true;
		gplano.setAttribute("cursor", "crosshair");
		escalera = new Escalera();
		gCreacionEscalera = null;
		lineaCreacionEscalera = null;
		rectanguloArrastreCreacionEscalera = null;
		
		ghelp.setText("arrastrar para crear un rectángulo o ir haciendo click para marcar los puntos");
	}

	public static void finCrearEscalera() {
		boolean rectangulo = false;
		if (rectanguloArrastreCreacionEscalera != null) {
			rectangulo = true;
			((OMSVGGElement)payload.get(getPlanta())).removeChild(rectanguloArrastreCreacionEscalera);
			rectanguloArrastreCreacionEscalera = null;
		}
		if (gCreacionEscalera != null) {
			((OMSVGGElement)payload.get(getPlanta())).removeChild(gCreacionEscalera);
			gCreacionEscalera = null;
		}
		gplano.setAttribute("cursor", "auto");	

		if (creandoEscalera) { 
			if ((rectangulo && escalera.getAngulos().size() > 1) || escalera.getAngulos().size() > 1) {
				
				getPlanta().getEscaleras().add(escalera);
				
				for (int i = 0; i < escalera.getAngulos().size() - 1; i++) {
					TramoEscalera t;
					escalera.getTramos().add(t = new TramoEscalera());
					t.setDe(escalera.getAngulos().get(i));
					t.setA(escalera.getAngulos().get(i + 1));					
				}
				
				
				//OMSVGGElement gx = habilitar(escalera, new SVGEscalera(escalera), gcapacontenido);
				
				escalera.recentrar();			
				svgsEscaleras.get(escalera).fillSegs(true);
				//gx.setAttribute("transform", "translate(" + escalera.getX() + ", " + escalera.getY() + "), rotate(" + escalera.getPan() + ")");
			}
			
			$("#botonescalera").removeClass("active");
		}
		
		creandoEscalera = false;
		escalera = null;
		ghelp.hide();

		refrescaPreview();
	}

	public static SVGAnguloEscalera getSvgAnguloEscaleraArrastrado() {
		return svgAnguloEscaleraArrastrado;
	} 

	public static void setSvgAnguloEscaleraArrastrado(
			SVGAnguloEscalera svgAnguloEscaleraArrastrado) {
		DibujoHelper.svgAnguloEscaleraArrastrado = svgAnguloEscaleraArrastrado;
	}

	public static SVGRectaEscalera getSvgRectaEscaleraArrastrada() {
		return svgRectaEscaleraArrastrada;
	}

	public static void setSvgRectaEscaleraArrastrada(
			SVGRectaEscalera svgRectaEscaleraArrastrada) {
		DibujoHelper.svgRectaEscaleraArrastrada = svgRectaEscaleraArrastrada;
	}


}
