package fsk4.client;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLineElement;

import fsk4.shared.FuenteLuz;
import fsk4.shared.IPosicionable;
import fsk4.shared.Linea;
import fsk4.shared.Modelo;
import fsk4.shared.Objeto;
import fsk4.shared.Pared;
import fsk4.shared.Puerta;
import fsk4.shared.Punto;

public class SVGDistancias extends OMSVGGElement {

	private Object seleccionado;

	public SVGDistancias(Object seleccionado) {
		this.seleccionado = seleccionado;
	}

	public void actualizar() {
		// vaciamos
		List<OMNode> zz = new ArrayList<OMNode>();
		for (OMNode z : getChildNodes()) zz.add(z);
		for (OMNode z : zz) removeChild(z);

		// dibujamos líneas
		
		if (seleccionado instanceof IPosicionable) {
			
			List<Linea> lineasOtrosObjetos = new ArrayList<Linea>();
			
			for (List<Pared> pz : DibujoHelper.getPlanta().getTodasParedes(true)) for (Pared px : pz) {
				for (Linea l2 : new Linea[] { new Linea(px), new Linea(px).paralela(-1 * px.getAncho())}) {
					lineasOtrosObjetos.add(l2);
				}
			}
			for (Objeto o : DibujoHelper.getPlanta().getContenido()) {
				if (o instanceof Modelo && !(o instanceof Puerta) && !(o instanceof FuenteLuz)) if (!o.equals(seleccionado)) {
					List<Linea> ol = DibujoHelper.getLineasContorno(o);
					lineasOtrosObjetos.addAll(ol);
				}
			}
			
			IPosicionable aux = (IPosicionable) seleccionado;
			
			Punto p = new Punto(aux.getX(), aux.getY());
			for (Linea l : lineasOtrosObjetos) {
				Punto i = l.proyeccion(p);
				OMSVGLineElement lx;
				if (i != null) appendChild(lx = new OMSVGLineElement(new Float(p.getX()), new Float(p.getY()), new Float(i.getX()), new Float(i.getY())));
			}
			
		}
		
	}
	
}
