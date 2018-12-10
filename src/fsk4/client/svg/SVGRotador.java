package fsk4.client.svg;

import java.util.Date;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import fsk4.client.DibujoHelper;
import fsk4.client.recursos.Recursos;

public class SVGRotador extends AbstractArrastable {
	
	private AbstractRotable rotado;
	private double pan0;

	public SVGRotador(AbstractRotable rotado) {
		this.setRotado(rotado);

		setId("grot" + new Date().getTime());
		
		for (double pan : new double[] {0, 90, 180, 270}) {
			
			OMNode svgb = Recursos.INSTANCIA.svgRotador().getSvg();
			
			Object o = svgb;
			System.out.println(o.getClass().getName());
			
			OMSVGGElement cx = null;
			for (OMNode xx : ((OMSVGSVGElement) o).getChildNodes()) {
				if (xx instanceof OMSVGGElement) cx = (OMSVGGElement) xx;
			}
			
			appendChild(cx);
			cx.setAttribute("transform", "rotate(" + pan + ")");
		}
        
        setAttribute("visibility", "hidden");

	}

	@Override
	public void arrastradoA(float x, float y) {
		double a0 = 0;
		double a1 = 0;
		
		a0 = 180 * Math.atan2(arrastreY0 - getRotado().getY(), arrastreX0 - getRotado().getX()) / Math.PI;
		a1 = 180 * Math.atan2((arrastreY0 + y) - getRotado().getY(), (arrastreX0 + x) - getRotado().getX()) / Math.PI;

		double ax = a1 - a0;
		getRotado().setPan(new Float(getPan0() + ax));
		
		SVGHelper.log("" + arrastreX0 + "," + arrastreY0 + "..." + x + "," + y + "..." + a0 + "," + a1 + "..." + getPan0() + "-->" + getRotado().getPan());
		
		if (DibujoHelper.getPlanta().getPlano().isIman() && (Math.abs(getRotado().getPan() % 90) < 3 || Math.abs(getRotado().getPan() % 90) > 87)) {
			getRotado().setPan(new Float(90 * (Math.signum(getRotado().getPan()) * (Math.floor(Math.abs(getRotado().getPan()) / 90d) + ((Math.abs(getRotado().getPan() % 90) < 3)?0:1)))));
			SVGHelper.mostrarGuias(getRotado().getX(), getRotado().getY());
		} else {
			SVGHelper.ocultarGuias();
		}
		getRotado().setAttribute("transform", getRotado().getTransformString());
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}

	public AbstractRotable getRotado() {
		return rotado;
	}

	public void setRotado(AbstractRotable rotado) {
		this.rotado = rotado;
	}

	public double getPan0() {
		return pan0;
	}

	public void setPan0(double pan0) {
		this.pan0 = pan0;
	}

}
