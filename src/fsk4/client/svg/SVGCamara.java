package fsk4.client.svg;

import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.utils.SVGConstants;

import fsk4.client.DibujoHelper;
import fsk4.shared.Camara;
import fsk4.shared.Punto;

public class SVGCamara extends AbstractRotable {

	private Camara camara;

	public SVGCamara(SVGPlanoCenital svgPlano, Camara camara) {
		this.setCamara(camara);
		this.svgPlano = svgPlano;
		
		init();
		
		
		/*
		List<OMNode> zz = new ArrayList<OMNode>();
		for (OMNode z : gojo.getChildNodes()) zz.add(z);
		for (OMNode z : zz) gojo.removeChild(z);
*/
		
		// calculamos el ángulo
		double anguloApertura = Math.atan2(camara.getDistanciaSensor(), camara.getAnchoSensor() / 2);
		
		// calculamos los 2 puntos que formarán el triángulo, junto con el (0,0)
		Punto p0 = new Punto(-1 * 45 * Math.cos(anguloApertura), -1 * 45 * Math.sin(anguloApertura));
		Punto p1 = new Punto(1 * 45 * Math.cos(anguloApertura), -1 * 45 * Math.sin(anguloApertura));

		// la línea izquierda
		OMSVGLineElement l;
		appendChild(l = SVGHelper.getDoc().createSVGLineElement(0, 0, new Float(p0.getX()), new Float(p0.getY())));
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		
		// la línea derecha
		appendChild(l = SVGHelper.getDoc().createSVGLineElement(0, 0, new Float(p1.getX()), new Float(p1.getY())));
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		l.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2");
		
		// ahora un poco más adentro, para el blanco del ojo (el arco)
		p0 = new Punto(-1 * 30 * Math.cos(anguloApertura), -1 * 30 * Math.sin(anguloApertura));
		p1 = new Punto(1 * 30 * Math.cos(anguloApertura), -1 * 30 * Math.sin(anguloApertura));

		// el arco
		OMSVGPathElement p;
		appendChild(p = SVGHelper.getDoc().createSVGPathElement());
		OMSVGPathSegList segs = p.getPathSegList();
		segs.appendItem(p.createSVGPathSegMovetoAbs(0, 0));
		segs.appendItem(p.createSVGPathSegLinetoAbs(new Float(p0.getX()), new Float(p0.getY())));
        segs.appendItem(p.createSVGPathSegArcAbs(new Float(p1.getX()), new Float(p1.getY()), 30, 30, new Float(anguloApertura), false, true));
        segs.appendItem(p.createSVGPathSegClosePath());
		p.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");

		// ahora un poco más adentro para el iris
		p0 = new Punto(-1 * 20 * Math.cos(anguloApertura), -1 * 20 * Math.sin(anguloApertura));
		p1 = new Punto(1 * 20 * Math.cos(anguloApertura), -1 * 20 * Math.sin(anguloApertura));

		// el iris
		appendChild(p = SVGHelper.getDoc().createSVGPathElement());
		segs = p.getPathSegList();
		segs.appendItem(p.createSVGPathSegMovetoAbs(0, 0));
		segs.appendItem(p.createSVGPathSegLinetoAbs(new Float(p0.getX()), new Float(p0.getY())));
        segs.appendItem(p.createSVGPathSegArcAbs(new Float(p1.getX()), new Float(p1.getY()), 20, 20, new Float(anguloApertura), false, true));
        segs.appendItem(p.createSVGPathSegClosePath());
		p.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#000000");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		p.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");	

	       
		actualizar();
	}

	private void actualizar() {
		setAttribute("transform", getTransformString());
	}

	@Override
	public double getPan() {
		return getCamara().getPan();
	}

	@Override
	public void setPan(double pan) {
		getCamara().setPan((float) pan);
	}

	@Override
	public void seleccionado() {
		DibujoHelper.seleccionado = getCamara();
	}

	@Override
	public String getIdTrozoInfo() {
		return "info-camara";
	}

	@Override
	public void arrastradoA(float x, float y) {
		getCamara().setX(x);
		getCamara().setY(y);
	}

	@Override
	public float getX() {
		return getCamara().getX();
	}

	@Override
	public float getY() {
		return getCamara().getY();
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

}
