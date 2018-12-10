package fsk4.client;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

import fsk4.shared.AnguloEscalera;

public class SVGAnguloEscalera extends OMSVGGElement {

	private AnguloEscalera angulo;
	
	protected float x0;
	protected float y0;
	
	private SVGEscalera svgEscalera;
	private OMSVGCircleElement c;

	
	public SVGAnguloEscalera(AnguloEscalera angulo, SVGEscalera svgEscalera) {
		this.setAngulo(angulo);
		this.setSvgEscalera(svgEscalera);
		
		appendChild(c = DibujoHelper.getDoc().createSVGCircleElement(0, 0, 4));
        c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#00ff00");
        c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
        c.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.5");

       actualizar();
        
		addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation();
				event.preventDefault();
				
				//event.getDataTransfer().setDragImage(, x, y)
				
				//event.getDataTransfer().setData("text/plain", "bzzz");			
				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
					x0 = SVGAnguloEscalera.this.getAngulo().getX();
					y0 = SVGAnguloEscalera.this.getAngulo().getY();
					//DibujoHelper.seleccionar(getAngulo());
					DibujoHelper.setSvgAnguloEscaleraArrastrado(SVGAnguloEscalera.this);
					DibujoHelper.setClientX0(event.getNativeEvent().getClientX());
					DibujoHelper.setClientY0(event.getNativeEvent().getClientY());
				}
			}
		});	

	}
	
	public void arrastrado(int clientX0, int clientY0, float clientX, float clientY) {
		OMSVGPoint p0 = globalToParent(clientX0, clientY0);
		OMSVGPoint p1 = globalToParent(new Float(clientX).intValue(), new Float(clientY).intValue());
		getAngulo().setX(x0 + p1.getX() - p0.getX());
		getAngulo().setY(y0 + p1.getY() - p0.getY());
        getSvgEscalera().fillSegs(true);
	}
	
	public void actualizar() {
		 setAttribute("transform", "translate(" + getAngulo().getX() + "," + getAngulo().getY() + ")");
	}
	
	protected OMSVGPoint globalToParent(int x02, int y02) {
		OMSVGPoint pt = DibujoHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(getSvgEscalera().getScreenCTM().inverse());
		return roomPoint;
	}

	protected OMSVGPoint parentToGlobal(float x02, float y02) {
		OMSVGPoint pt = DibujoHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(getSvgEscalera().getScreenCTM());
		return roomPoint;
	}

	public OMSVGPoint getPuntoVerticeAnterior() {
		AnguloEscalera vx = null;
		if (getAngulo().getEscalera().getAngulos().indexOf(angulo) > 0) vx = getAngulo().getEscalera().getAngulos().get(getAngulo().getEscalera().getAngulos().indexOf(angulo) - 1);
		else vx = getAngulo().getEscalera().getAngulos().get(getAngulo().getEscalera().getAngulos().size() - 1);
		
		OMSVGPoint pt = DibujoHelper.getSvg().createSVGPoint();
		pt.setX(new Float(vx.getX()));
		pt.setY(new Float(vx.getY()));
		return pt;
	}

	public OMSVGPoint getPuntoVerticePosterior() {
		AnguloEscalera vx = null;
		if (getAngulo().getEscalera().getAngulos().indexOf(angulo) < getAngulo().getEscalera().getAngulos().size() - 1) vx = getAngulo().getEscalera().getAngulos().get(getAngulo().getEscalera().getAngulos().indexOf(angulo) + 1);
		else vx = getAngulo().getEscalera().getAngulos().get(0);
		
		OMSVGPoint pt = DibujoHelper.getSvg().createSVGPoint();
		pt.setX(new Float(vx.getX()));
		pt.setY(new Float(vx.getY()));
		return pt;
	}

	public OMSVGPoint parentToGlobal(OMSVGPoint puntoVerticeAnterior) {
		return parentToGlobal(puntoVerticeAnterior.getX(), puntoVerticeAnterior.getY());
	}

	public OMSVGPoint parentToGlobal(int x, int y) {
		return parentToGlobal(new Float(x), new Float(y));
	}

	public AnguloEscalera getAngulo() {
		return angulo;
	}

	public void setAngulo(AnguloEscalera angulo) {
		this.angulo = angulo;
	}

	public SVGEscalera getSvgEscalera() {
		return svgEscalera;
	}

	public void setSvgEscalera(SVGEscalera svgEscalera) {
		this.svgEscalera = svgEscalera;
	}

	public void marcar() {
		c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#ff0000");
	}
	
	public void desmarcar() {
		c.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#00ff00");
	}

}
