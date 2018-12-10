package fsk4.client.svg;

import fsk4.shared.Planta;

public class SVGPlanoAlzado extends SVGPlano {

	private int pan;

	public SVGPlanoAlzado(Planta planta, int pan) {
		super(planta);		
		this.setPan(pan);
	}

	@Override
	public void asignarImagenFondo(double patwi, double pathe) {
	}

	@Override
	public void crearBook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void crearContenido() {
		// TODO Auto-generated method stub
		
	}

	public int getPan() {
		return pan;
	}

	public void setPan(int pan) {
		this.pan = pan;
	}

}
