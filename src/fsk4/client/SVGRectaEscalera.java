package fsk4.client;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

import fsk4.shared.AnguloEscalera;
import fsk4.shared.Escalera;
import fsk4.shared.Linea;
import fsk4.shared.Punto;
import fsk4.shared.TramoEscalera;

public class SVGRectaEscalera extends OMSVGGElement {
	
	private Escalera escalera;
	private TramoEscalera tramo;
	private SVGEscalera svgEscalera;
	protected double x0;
	protected double y0;
	private AnguloEscalera de;
	private AnguloEscalera a;
	private Linea linea;
	private AnguloEscalera amas1;
	private AnguloEscalera demenos1;
	private Linea lineaantes;
	private Linea lineadespues;

	public SVGRectaEscalera(Escalera escalera, TramoEscalera tramo, SVGEscalera svgEscalera) {
		this.setEscalera(escalera);
		this.setTramo(tramo);
		this.svgEscalera = svgEscalera;
		
		de = tramo.getDe();
		a = tramo.getA();
		
		if (getEscalera().getAngulos().indexOf(de) > 0) demenos1 = getEscalera().getAngulos().get(getEscalera().getAngulos().indexOf(de) - 1);
		else demenos1 = getEscalera().getAngulos().get(getEscalera().getAngulos().size() - 1);

		if (getEscalera().getAngulos().indexOf(a) < getEscalera().getAngulos().size() - 1) amas1 = getEscalera().getAngulos().get(getEscalera().getAngulos().indexOf(a) + 1);
		else amas1 = getEscalera().getAngulos().get(0);

		linea= new Linea(new Punto(de), new Punto(a));

		OMSVGRectElement c;
		appendChild(c = DibujoHelper.getDoc().createSVGRectElement(-4, -4, 8, 8, 0, 0));
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
					x0 = SVGRectaEscalera.this.getEscalera().getX();
					y0 = SVGRectaEscalera.this.getEscalera().getY();
					DibujoHelper.setSvgRectaEscaleraArrastrada(SVGRectaEscalera.this);
					DibujoHelper.setClientX0(event.getNativeEvent().getClientX());
					DibujoHelper.setClientY0(event.getNativeEvent().getClientY());
				}
			}
		});	

        
	}

	public void arrastrado(int clientX0, int clientY0, float clientX, float clientY) {
		OMSVGPoint p0 = globalToParent(clientX0, clientY0);
		OMSVGPoint p1 = globalToParent(new Float(clientX).intValue(), new Float(clientY).intValue());
		
		Linea paralela = linea.paralelaQuePasaPor(new Punto(p1.getX(), p1.getY()));
		
		lineaantes= new Linea(new Punto(demenos1), new Punto(de));
		lineadespues= new Linea(new Punto(a), new Punto(amas1));
		
		Punto nde = paralela.intersection(lineaantes, true);
		Punto na = paralela.intersection(lineadespues, true);
		
		if (nde != null && na != null) {
			de.setX(nde.getX());
			de.setY(nde.getY());
			a.setX(na.getX());
			a.setY(na.getY());
		}
        
        svgEscalera.fillSegs(true);
	}
	
	public void actualizar() {
        setAttribute("transform", "translate(" + ((de.getX() + a.getX()) / 2) + "," + ((de.getY() + a.getY()) / 2) + ")");
	}
	
	protected OMSVGPoint globalToParent(int x02, int y02) {
		OMSVGPoint pt = DibujoHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(svgEscalera.getScreenCTM().inverse());
		return roomPoint;
	}

	public Escalera getEscalera() {
		return escalera;
	}

	public void setEscalera(Escalera escalera) {
		this.escalera = escalera;
	}

	public TramoEscalera getTramo() {
		return tramo;
	}

	public void setTramo(TramoEscalera tramo) {
		this.tramo = tramo;
	}


}
