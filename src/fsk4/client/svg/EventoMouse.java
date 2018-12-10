package fsk4.client.svg;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPoint;

public class EventoMouse {
	
	private int x;
	private int y;
	private boolean mouseDown;
	
	
	public EventoMouse(int clientX, int clientY, boolean mouseDown) {
		super();

		/*
		OMSVGPoint puntoLocal = globalToLocal(DibujoHelper.perspectivaHelper.svgPlano, clientX, clientY);
		
		this.x = new Float(puntoLocal.getX()).intValue();
		this.y = new Float(puntoLocal.getY()).intValue();
		*/
		
		this.x = clientX;
		this.y = clientY;
		this.mouseDown = mouseDown;
	}
	
	public static OMSVGPoint localToGlobal(OMSVGGElement g, float x02, float y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(g.getScreenCTM());
		return roomPoint;
	}
	
	public static OMSVGPoint globalToLocal(OMSVGGElement g, float x02, float y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(g.getScreenCTM().inverse());
		return roomPoint;
	}



	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public boolean isMouseDown() {
		return mouseDown;
	}


	public void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}
	
	

}
