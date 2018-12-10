package fsk4.client.svg;

import java.util.List;

import org.vectomatic.dom.svg.OMSVGGElement;

import fsk4.shared.Pared;
import fsk4.shared.PerspectivaCenital;
import fsk4.shared.PerspectivaDerecha;
import fsk4.shared.PerspectivaDetras;
import fsk4.shared.PerspectivaFrente;
import fsk4.shared.PerspectivaIzquierda;
import fsk4.shared.Planta;
import fsk4.shared.Punto;
import fsk4.shared.Vertice;


/**
 * contenedor del plano. Lo utilizamos para gestionar las perspectivas y los elementos comunes a todas ellas (zoom, guia) 
 * 
 * Está referenciado desde SVGHelper, y se crea cada vez que cargamos una planta
 * 
 * @author miguel
 *
 */
public class SVGMarco extends OMSVGGElement {
	
	private OMSVGGElement capaPlano;
	private OMSVGGElement capaHerramientas;
	public SVGPlano svgPlano;

	public SVGMarco() {
		appendChild(capaPlano = new OMSVGGElement());
		capaPlano.setId("capaPlano");
//		capaPlano.setAttribute("transform", "translate(400,400)");
		appendChild(capaHerramientas = new OMSVGGElement());
		capaHerramientas.setId("capaHerramientas");
	}
	
	
	/**
	 * básicamente, instancia el SVGPlano correspondiente según la perspectiva seleccionada 
	 * 
	 * @param planta
	 */
	public void setPlanta(Planta planta) {
		SVGHelper.log("SVGMarco.setPlanta()");
		if (planta.getPlano().getPerspectiva() instanceof PerspectivaCenital) capaPlano.appendChild(svgPlano = new SVGPlanoCenital(planta));
		else if (planta.getPlano().getPerspectiva() instanceof PerspectivaFrente) capaPlano.appendChild(svgPlano = new SVGPlanoAlzado(planta, 0));
		else if (planta.getPlano().getPerspectiva() instanceof PerspectivaDerecha) capaPlano.appendChild(svgPlano = new SVGPlanoAlzado(planta, 90));
		else if (planta.getPlano().getPerspectiva() instanceof PerspectivaDetras) capaPlano.appendChild(svgPlano = new SVGPlanoAlzado(planta, 180));
		else if (planta.getPlano().getPerspectiva() instanceof PerspectivaIzquierda) capaPlano.appendChild(svgPlano = new SVGPlanoAlzado(planta, 270));
	}

	public void zoom() {
		svgPlano.zoom();
	}


	public void mostrarGuias(float x, float y) {
		svgPlano.mostrarGuias(x, y);
	}


	public void ocultarGuias() {
		if (svgPlano != null) svgPlano.ocultarGuias();
	}


	public void addSVGPared(List<Pared> paredes, Pared pared) {
		if (svgPlano != null && svgPlano instanceof SVGPlanoCenital) ((SVGPlanoCenital)svgPlano).addSVGPared(paredes, pared); 
	}


	public void arrastrarParedA(Pared pared, Punto punto) {
		if (svgPlano != null && svgPlano instanceof SVGPlanoCenital) ((SVGPlanoCenital)svgPlano).arrastrarParedA(pared, punto); 
	}
	
}
