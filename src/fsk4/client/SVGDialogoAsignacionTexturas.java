package fsk4.client;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;

import fsk4.shared.Modelo;
import fsk4.shared.Textura;

public abstract class SVGDialogoAsignacionTexturas extends SVGDialogo {

	private OMSVGGElement tex;
	private Modelo m;

	public SVGDialogoAsignacionTexturas(final OMSVGGElement tex, Modelo m) {
		super();
		this.tex = tex;
		this.m = m;
		
		for (final Textura t : m.getInfo().getTexturas()) {
			
			final OMSVGImageElement i = DibujoHelper.getDoc().createSVGImageElement(50, 15 + m.getInfo().getTexturas().indexOf(t) * 30, 25, 25, t.getCenital());
			
			SVGCheckbox c;
			appendChild(c = new SVGCheckbox(false, "", new ChangeListenerBoolean() {
				
				@Override
				public void change(boolean nuevoValor) {
					t.setIdMaterial(tex.getAttribute("data-id"));
					t.setCenital(tex.getAttribute("data-cenital"));
					t.setNombre(tex.getAttribute("data-nombre"));
					i.setAttribute("href", t.getCenital());
				}
			}));
			c.setAttribute("transform", "translate(15, " + (15 + m.getInfo().getTexturas().indexOf(t) * 30) + ")");
			
			appendChild(i);
			
			appendChild(DibujoHelper.getDoc().createSVGImageElement(80, 15 + m.getInfo().getTexturas().indexOf(t) * 30, 25, 25, t.getImagen()));

			appendChild(new SVGBoton("Ok", getAncho() - 120, getAlto() - 30, new Runnable() {
				
				@Override
				public void run() {
					cerrar();
					Fsk4.success("Textura asignada");
					DibujoHelper.refrescaPreview();
				}
			}));
			
		}
		
	}

	@Override
	public float getAncho() {
		return 150;
	}
	
	

}
