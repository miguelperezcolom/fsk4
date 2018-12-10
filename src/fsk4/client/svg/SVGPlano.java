package fsk4.client.svg;

import java.util.HashMap;
import java.util.Map;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDefsElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPatternElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.utils.SVGConstants;

import fsk4.client.DibujoHelper;
import fsk4.shared.Plano;
import fsk4.shared.Planta;
import fsk4.shared.Vertice;

/**
 * base para la clase SVGPlanoCenital y SVGPlanoAlzado
 * 
 * Tiene una variable <b>payload</b> para poder recuperar el SVG desde el objeto del plano
 * 
 * Tiene también varias capas para las paredes, vértices, contenido, luces y cámara
 * 
 * 
 * 
 * 
 * @author miguel
 *
 */
public abstract class SVGPlano extends AbstractSeleccionable {
	
	public static final double ESCALA = 100;

	private Plano plano;
	private Planta planta;

	private OMSVGDefsElement def;
	private OMSVGPatternElement patInteriorParedes;
	protected OMSVGPatternElement patExterior;
	protected OMSVGImageElement imgPatronExterior;
	
	protected OMSVGGElement capaParedes;
	protected OMSVGGElement capaVertices;
	protected OMSVGGElement capaContenido;
	protected OMSVGGElement capaLuces;
	protected OMSVGGElement capaCamara;
	
	public Map<Object, OMSVGGElement> payload = new HashMap<Object, OMSVGGElement>();

	private OMSVGGElement capaGuias;

	private OMSVGLineElement guiaHorizontal;

	private OMSVGLineElement guiaVertical;

	public SVGPlano(Planta planta) {
		
		super();

		
		this.planta = planta;
		this.plano = planta.getPlano();
		
		
		setDef(SVGHelper.getDoc().createSVGDefsElement());
		appendChild(getDef());

		/*
<pattern id="diagonalHatch" patternUnits="userSpaceOnUse" width="4" height="4">
  <path d="M-1,1 l2,-2
           M0,4 l4,-4
           M3,5 l2,-2" />
</pattern>
		 */

		getDef().appendChild(patInteriorParedes= SVGHelper.getDoc().createSVGPatternElement());
		patInteriorParedes.setId("patinteriorparedes");

		patInteriorParedes.setAttribute("patternUnits", "userSpaceOnUse");			
		patInteriorParedes.setAttribute("width", "4");			
		patInteriorParedes.setAttribute("height", "4");			
		//patExterior.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);			
		OMSVGPathElement aux;
		patInteriorParedes.appendChild(aux = SVGHelper.getDoc().createSVGPathElement());
		aux.setAttribute("d", "M-1,1 l2,-2 M0,4 l4,-4 M3,5 l2,-2");
		aux.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");

		getDef().appendChild(patExterior= SVGHelper.getDoc().createSVGPatternElement());
		patExterior.setId("patexterior");

		double patwi = ESCALA * 100 / 100;  
		double pathe = ESCALA * 100 / 100;  
		
		patExterior.setAttribute("patternUnits", "userSpaceOnUse");			
		patExterior.setAttribute("width", "" + patwi);			
		patExterior.setAttribute("height", "" + pathe);			
		patExterior.setAttribute("viewbox", "0 0 " + patwi + " " + pathe);
		
		asignarImagenFondo(patwi, pathe);
		
		OMSVGLineElement aux2;
		patExterior.appendChild(aux2 = SVGHelper.getDoc().createSVGLineElement(50, 0, 50, 100));
		aux2.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#cdcdcd");
		patExterior.appendChild(aux2 = SVGHelper.getDoc().createSVGLineElement(0, 50, 100, 50));
		aux2.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#cdcdcd");

		
        OMSVGCircleElement r;
		appendChild(r = SVGHelper.getDoc().createSVGCircleElement(0, 0, 10000));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#patexterior)");

		
		
		// CREAMOS LAS CAPAS. TODAS SE ESCALAN IGUAL QUE EL PLANO
		appendChild(capaParedes = SVGHelper.getDoc().createSVGGElement());
		capaParedes.setId("capaParedes");
		appendChild(capaVertices = SVGHelper.getDoc().createSVGGElement());
		capaVertices.setId("capaVertices");
		appendChild(capaContenido = SVGHelper.getDoc().createSVGGElement());
		capaContenido.setId("capaContenido");
		appendChild(capaLuces = SVGHelper.getDoc().createSVGGElement());
		capaLuces.setId("capaLuces");
		appendChild(capaCamara = SVGHelper.getDoc().createSVGGElement());
		capaCamara.setId("capaCamara");
		appendChild(capaGuias = SVGHelper.getDoc().createSVGGElement());
		capaGuias.setId("capaGuias");
		
		capaGuias.appendChild(guiaHorizontal = SVGHelper.getDoc().createSVGLineElement(-5000, 0, 5000, 0));
		guiaHorizontal.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
		guiaHorizontal.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        guiaHorizontal.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        guiaHorizontal.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_DASHARRAY_PROPERTY, "20,10,5,5,5,10");
		capaGuias.appendChild(guiaVertical = SVGHelper.getDoc().createSVGLineElement(0, -5000, 0, 5000));
		guiaVertical.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
		guiaVertical.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        guiaVertical.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        guiaVertical.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_DASHARRAY_PROPERTY, "20,10,5,5,5,10");

		
		crearBook();		
		
		crearContenido();
		
		setAttribute("cursor", "auto");
		//setAttribute("cursor", "crosshair");
		
		setAttribute("transform", getTransformString());
	}
	
	public abstract void crearContenido();

	public abstract void crearBook();

	public abstract void asignarImagenFondo(double patwi, double pathe);

	@Override
	public void arrastradoA(float x, float y) {
		getPlano().setX(x);
		getPlano().setY(y);
	}

	@Override
	public float getX() {
		return getPlano().getX();
	}

	@Override
	public float getY() {
		return getPlano().getY();
	}

	@Override
	public void seleccionado() {
		DibujoHelper.seleccionado = getPlano();
	}

	@Override
	public String getTransformString() {
		return "translate(" + getPlano().getX() + "," + getPlano().getY() + "), scale(" + getPlano().getZoom() + ")";
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}
	
	@Override
	public String getIdTrozoInfo() {
		return "info-plano";
	}

	public void zoom() {
		setAttribute("transform", getTransformString());
	}

	public OMSVGDefsElement getDef() {
		return def;
	}

	public void setDef(OMSVGDefsElement def) {
		this.def = def;
	}
	
	
	public OMSVGPoint globalToPlano(int x02, int y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(getScreenCTM().inverse());
		return roomPoint;
	}

	public OMSVGPoint planoToGlobal(float x02, float y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(getScreenCTM());
		return roomPoint;
	}

	public OMSVGPoint planoToGlobal(OMSVGPoint punto) {
		return planoToGlobal(punto.getX(), punto.getY());
	}

	public OMSVGPoint planoToGlobal(int x, int y) {
		return planoToGlobal(new Float(x), new Float(y));
	}

	public OMSVGGElement[] getCapas() {
		return new OMSVGGElement[] {
				capaCamara, capaContenido, capaLuces, capaParedes, capaVertices
		};
	}

	public void mostrarGuias(float x, float y) {
		guiaHorizontal.setAttribute("transform", "translate(0," + y + ")");
		guiaHorizontal.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "block");
		guiaVertical.setAttribute("transform", "translate(" + x + ",0)");
		guiaVertical.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "block");
	}

	public void ocultarGuias() {
		guiaHorizontal.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
		guiaVertical.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
	}

	
	@Override
	public void md(int clientX, int clientY) {
		// TODO Auto-generated method stub
		super.md(clientX, clientY);
	}

	@Override
	public OMSVGPoint trasladar(int clientX, int clientY) {
		return SVGHelper.globalToPlano(clientX, clientY);
		//return SVGHelper.getSvg().createSVGPoint(clientX, clientY);
	}
}
