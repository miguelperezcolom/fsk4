package fsk4.client.svg;

import java.util.List;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import fsk4.client.DibujoHelper;
import fsk4.shared.Linea;
import fsk4.shared.Pared;
import fsk4.shared.Vertice;

public class SVGVerticePared extends AbstractSeleccionable {

	private Vertice vertice;

	public SVGVerticePared(SVGPlanoCenital svgPlano, Vertice v) {
		this.setVertice(v);
		this.svgPlano = svgPlano;
				
		final OMSVGCircleElement cx = DibujoHelper.getDoc().createSVGCircleElement(0f, 0f, 15f);
        cx.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#00ff00");
        cx.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
        cx.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        cx.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "0.5");

        appendChild(cx);

       init();
       
       actualizar();
	}

	public void actualizar() {
		setAttribute("transform", getTransformString());
        if (SVGHelper.svgMarco.svgPlano instanceof SVGPlanoCenital) for (SVGPared p : ((SVGPlanoCenital)SVGHelper.svgMarco.svgPlano).paredesColindantesPorVertice.get(vertice)) p.refrescar();
        ((SVGPlanoCenital)svgPlano).repintarSuelo();
	}

	@Override
	public void arrastradoA(float x, float y) {
		getVertice().setX(x);
		getVertice().setY(y);
		actualizar();
	}

	@Override
	public float getX() {
		return getVertice().getX();
	}

	@Override
	public float getY() {
		return getVertice().getY();
	}

	@Override
	public void seleccionado() {
		DibujoHelper.seleccionado = getVertice();
	}

	@Override
	public String getIdTrozoInfo() {
		return "info-vertice";
	}

	public Vertice getVertice() {
		return vertice;
	}

	public void setVertice(Vertice vertice) {
		this.vertice = vertice;
	}
	
	@Override
	protected boolean isInterceptaMD() {
		return !SVGHelper.creandoParedes;
	}
}
