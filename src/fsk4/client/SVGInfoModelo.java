package fsk4.client;

import java.util.Date;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fsk4.shared.InfoArticulo;
import fsk4.shared.Modelo;

public class SVGInfoModelo extends OMSVGGElement {

	private Modelo m;
	private OMSVGImageElement i;
	private SVGText n;
	private SVGText p;
	private SVGText d;

	public SVGInfoModelo(Modelo m) {
		this.m = m;
		
		OMSVGRectElement r;
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, 217f, 170f, 5, 5));
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ffffff");
		r.getStyle().setSVGProperty(SVGConstants.CSS_OPACITY_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#000000");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		
		appendChild(i = DibujoHelper.getDoc().createSVGImageElement(5, 5, 60, 60, "http://www.fescateva.com/img/loading_indicator_big.gif"));
		appendChild(n = new SVGText(70, 5, 100, 100, null, null, ""));
		n.setAttribute("pointer-events", "none");
		appendChild(d = new SVGText(5, 70, 200, 20, null, null, ""));
		d.setAttribute("pointer-events", "none");
		appendChild(p = new SVGText(5, 90, 200, 20, null, null, ""));
		p.setAttribute("pointer-events", "none");
		
		
		SVGText de;
		appendChild(de = new SVGText(5, 110, 100, 100, null, null, "Disponible en"));
		OMSVGImageElement ix;
		appendChild(ix = DibujoHelper.getDoc().createSVGImageElement(5, 130, 100, 30, "publico/images/watermark.png"));
		ix.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//Window.open("http://www.mediamarkt.es/", "mediamarket", "");
				Window.open("/", "xxx", "");
			}
		});
		
		
		if (m.getInfo() == null) {
			Fsk4.getServicio().getInfoArticulo(m.getId(), new AsyncCallback<InfoArticulo>() {
				
				@Override
				public void onSuccess(InfoArticulo result) {
					SVGInfoModelo.this.m.setInfo(result);
					updateInfo();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Fsk4.alert("" + caught.getClass().getName() + ": " + caught.getMessage());
				}
			});
		} else {
			i.getHref().setBaseVal(m.getInfo().getVistaPrevia() + "?cuando=" + new Date().getTime());
			n.setText((m.getInfo().getNombre() != null)?m.getInfo().getNombre():"");
			p.setText("EUR " + m.getInfo().getPvp());
			updateDimensiones();
		}
			
		
		
	}
	
	protected void updateInfo() {
		i.getHref().setBaseVal("http://www.fescateva.com/img/loading_indicator_big.gif");
		i.getHref().setBaseVal(m.getInfo().getVistaPrevia() + "?cuando=" + new Date().getTime());
		n.setText((m.getInfo().getNombre() != null)?m.getInfo().getNombre():"");
		p.setText("EUR " + m.getInfo().getPvp());
		updateDimensiones();
	}

	public void updateDimensiones() {
		d.setText("" + (Math.round(100d * m.getAncho()) / 100d) + " x " + (Math.round(100d * m.getAlto()) / 100d) + " x " + (Math.round(100d * m.getAltura()) / 100d) + " cm.");
		
	}
	
	

}
