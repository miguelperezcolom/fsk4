package fsk4.client;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.utils.SVGConstants;

public class SVGHelp extends OMSVGGElement {

	private OMSVGRectElement r;
	private OMSVGTextElement t;

	public SVGHelp() {
		
		appendChild(r = DibujoHelper.getDoc().createSVGRectElement(0, 0, 200, 20, 5, 5));
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
		r.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#FFFFB8");
		r.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "#FFB452");

		appendChild(t = DibujoHelper.getDoc().createSVGTextElement(0, 18, OMSVGLength.SVG_LENGTHTYPE_PX, ""));
		t.setAttribute("pointer-events", "none");
		
		setAttribute("visibility", "hidden");		
	}
	
	public void setText(String text) {
		for (int i = 0; i < t.getChildNodes().getLength(); i++) {
			OMNode n = t.getChildNodes().getItem(i);
			if (n instanceof OMText) n.setNodeValue("" + text); 
		}
		r.setAttribute("width", "" + (t.getBBox().getWidth() + 20));
		setAttribute("visibility", "visible");		
	}
	
	public void hide() {
		setAttribute("visibility", "hidden");		
	}
	
}
