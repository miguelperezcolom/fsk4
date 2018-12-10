package fsk4.client.svg;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPoint;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

import fsk4.client.DibujoHelper;
import fsk4.shared.Linea;
import fsk4.shared.Pared;
import fsk4.shared.Punto;
import fsk4.shared.Vertice;

public abstract class AbstractArrastable extends OMSVGGElement {
	
	public int arrastreX0;
	public int arrastreY0;
	protected float x0;
	protected float y0;
	public SVGPlano svgPlano;
	
	public AbstractArrastable() {
		super();
		init();
	}

	public void init() {
		
		addMouseDownHandler(new MouseDownHandler() {
			

			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (isInterceptaMD()) {
					event.stopPropagation();
					event.preventDefault();
					
					if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
						md(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
					}
				}
			}
		});		
		
		addTouchStartHandler(new TouchStartHandler() {
			
			@Override
			public void onTouchStart(TouchStartEvent event) {
				if (isInterceptaMD()) {
					event.stopPropagation();

					if (event.getTouches() != null && event.getTouches().length() > 0) {
						Touch t = event.getTouches().get(0);

						md(t.getClientX(), t.getClientY());

					}
				}
			}
		});


	}


	protected boolean isInterceptaMD() {
		return true;
	}

	public void md(int clientX, int clientY) {
		SVGHelper.svgArrastrado = AbstractArrastable.this;
		
		OMSVGPoint p = trasladar(clientX, clientY);
		x0 = getX();
		y0 = getY();
		arrastreX0 = new Float(p.getX()).intValue();					
		arrastreY0 = new Float(p.getY()).intValue();	
		
		if (AbstractArrastable.this instanceof AbstractRotable) {
			((AbstractRotable)AbstractArrastable.this).md(); 
		}

		if (AbstractArrastable.this instanceof SVGRotador) {
			((SVGRotador)AbstractArrastable.this).setPan0(((SVGRotador)AbstractArrastable.this).getRotado().getPan()); 
			if (((SVGRotador)AbstractArrastable.this).getRotado().getPan() % 90 == 0) SVGHelper.mostrarGuias(((SVGRotador)AbstractArrastable.this).getRotado().getX(), ((SVGRotador)AbstractArrastable.this).getRotado().getY());
		}
		
		if (AbstractArrastable.this instanceof AbstractSeleccionable) {						
			SVGHelper.marcarSeleccion();
			((AbstractSeleccionable)AbstractArrastable.this).seleccionar();
		}
	}

	public OMSVGPoint trasladar(float clientX, float clientY) {
		return SVGHelper.globalToPlano(clientX, clientY);
	}

	public OMSVGPoint trasladar(int clientX, int clientY) {
		return trasladar(new Float(clientX), new Float(clientY));
	}

	public void mmx(EventoMouse e) {
		if (e.isMouseDown()) {
			//SVGHelper.log("arrastrado.mmx(" + e.getX() + "," + e.getY() + ")");
			OMSVGPoint p0 = SVGHelper.getSvg().createSVGPoint(x0, y0);
			
			OMSVGPoint p1 = trasladar(x0 + e.getX() - arrastreX0, y0 + e.getY() - arrastreY0);
			
			if (!(AbstractArrastable.this instanceof SVGRotador)) if (!imantar(p1, p0)) imantar(p1);
			
			arrastradoA(p1.getX(), p1.getY());
			
			setAttribute("transform", getTransformString());
		}
	}
	
	public boolean imantar(OMSVGPoint p1, OMSVGPoint p0) {
		boolean imantado = false;
		if (DibujoHelper.getPlanta().getPlano().isIman()) {
			if (Math.abs(p1.getX() - p0.getX()) < 10) {
				p1.setX(p0.getX());
				imantado = true;
			}
			if (Math.abs(p1.getY() - p0.getY()) < 10) {
				p1.setY(p0.getY());
				imantado = true;
			}
		}
		//if (imantado) SVGHelper.log("imantado a p0 (" + p1.getX() + "," + p1.getY() + ")");
		return imantado;
	}

	public void imantar(OMSVGPoint p1) {
		if (DibujoHelper.getPlanta().getPlano().isIman()) {
			
			if (this instanceof SVGVerticePared) {
				
				// los vertices se imantan con los demás vértices
				for (List<Pared> pz : DibujoHelper.getPlanta().getTodasParedes(true)) for (Pared px : pz) {
					for (Vertice vx : new Vertice[] {px.getDe(), px.getA()}) if (!((SVGVerticePared)this).getVertice().equals(vx)) {
						imantar(p1, SVGHelper.getSvg().createSVGPoint(vx.getX(), vx.getY()));
					}
				} 
				
			} else {
				// encontrar la pared más cercana
				
				Punto pt2 = new Punto(p1.getX(), p1.getY());
				Linea paredMasCercana = null;
				double menorDistancia = 0;
				for (List<Pared> pz : DibujoHelper.getPlanta().getTodasParedes(true)) for (Pared px : pz) {
				//	if (paredes == null || paredes.size() == 0 || !px.equals(paredes.get(paredes.size() - 1))) {   // LA PARED QUE ESTAMOS CREANDO
					boolean ok = !(this instanceof SVGPared) || !((SVGPared)this).getPared().equals(px);
					ok &= !(this instanceof SVGVerticePared) || !(((SVGPlanoCenital)svgPlano).paredesPorVertice.get(((SVGVerticePared)this).getVertice()).contains(svgPlano.payload.get(px)));
					if (ok) {
						for (Linea l : new Linea[] { new Linea(px), new Linea(px).paralela(-1 * px.getAncho())}) {
							double d = l.distanciaDesde(pt2);
							if (d >= 0 && (paredMasCercana == null || d < menorDistancia)) {
								menorDistancia = d;
								paredMasCercana = l;
							}
						}
					}
					//}
				} 
				
				if (paredMasCercana != null) {
					
					//SVGHelper.log("imantado a " + paredMasCercana.getClass().getName());
					
					double d = paredMasCercana.distanciaDesde(pt2);
					// si cabe entonces colocar sobre la pared
					if (d < 10) {
						Punto proy = paredMasCercana.proyeccion(pt2);
						if (proy != null) {
							//SVGHelper.log("proyeccion ok. imantado a " + paredMasCercana + ", d=" + d);
							p1.setX(new Float(proy.getX()));
							p1.setY(new Float(proy.getY()));
						}
					}
				}
			}
			
			
		}
	}

	

	protected OMSVGPoint globalToParent(int x02, int y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(((OMSVGGElement)getParentNode()).getScreenCTM().inverse());
		return roomPoint;
	}
	
	public OMSVGPoint globalToParent(float x, float y) {
		return globalToParent(new Float(x).intValue(), new Float(y).intValue());
	}


	protected OMSVGPoint parentToGlobal(float x02, float y02) {
		OMSVGPoint pt = SVGHelper.getSvg().createSVGPoint();
		pt.setX(x02);
		pt.setY(y02);
		OMSVGPoint roomPoint = pt.matrixTransform(((OMSVGGElement)getParentNode()).getScreenCTM());
		return roomPoint;
	}

	public OMSVGPoint parentToGlobal(OMSVGPoint punto) {
		return parentToGlobal(punto.getX(), punto.getY());
	}

	public OMSVGPoint parentToGlobal(int x, int y) {
		return parentToGlobal(new Float(x), new Float(y));
	}

	
	public String getTransformString() {
		String auxs = "translate(" + getX() + "," + getY() + ")";
		return auxs;
	}
	
	public abstract void arrastradoA(float x, float y);

	public abstract  float getX();
	
	public abstract  float getY();

}
