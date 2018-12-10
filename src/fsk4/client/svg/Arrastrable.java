package fsk4.client.svg;

import fsk4.shared.IPosicionable;

public class Arrastrable extends AbstractArrastable {
	
	private IPosicionable objeto;

	public Arrastrable(IPosicionable objeto) {
		this.objeto = objeto;
	}

	@Override
	public void arrastradoA(float x, float y) {
		objeto.setX(x);
		objeto.setY(x);
	}

	@Override
	public float getX() {
		return objeto.getX();
	}

	@Override
	public float getY() {
		return objeto.getY();
	}

}
