package fsk4.client.svg;

import fsk4.client.DibujoHelper;
import fsk4.shared.Volumen;

public class SVGVolumen extends AbstractRotable {
	
	private Volumen volumen;

	public SVGVolumen(Volumen volumen) {
		this.setVolumen(volumen);

		/*
		for (Vertice v : getVolumen().getVertices()) {
			appendChild(new SVGVerticePared(v));
		}
		*/
		
	}
	

	@Override
	public double getPan() {
		return getVolumen().getPan();
	}

	@Override
	public void setPan(double pan) {
		getVolumen().setPan((float) pan);
	}

	@Override
	public void seleccionado() {
		DibujoHelper.seleccionado = getVolumen();
	}

	@Override
	public String getIdTrozoInfo() {
		return "info-volumen";
	}

	@Override
	public void arrastradoA(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getX() {
		return getVolumen().getX();
	}

	@Override
	public float getY() {
		return getVolumen().getY();
	}


	public Volumen getVolumen() {
		return volumen;
	}


	public void setVolumen(Volumen volumen) {
		this.volumen = volumen;
	}

}
