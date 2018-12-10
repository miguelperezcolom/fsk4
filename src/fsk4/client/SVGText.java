package fsk4.client;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGForeignObjectElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGSwitchElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

public class SVGText extends OMSVGGElement {

	private Element p;
	private OMElement ta;
	private OMSVGTextElement t;
	private int x;
	private int y;
	private int w;
	private int h;

	public SVGText(int x, int y, int w, int h, String fuente, String tamanyoFuente, String text) {
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		
		setText(text);
		
		setAttribute("transform", "translate(" + x + ", " + y + ")");

	}

	public void setText(String text) {
		List<OMNode> borrar = new ArrayList<OMNode>();
		for (int i = 0; i < getChildNodes().getLength(); i++) {
			borrar.add(getChildNodes().getItem(i));
		}
		for (OMNode n : borrar) {
			removeChild(n);
		}
		/*
		<switch>
	    <g requiredFeatures="http://www.w3.org/Graphics/SVG/feature/1.2/#TextFlow">
	      <textArea width="200" height="auto">
	       Text goes here
	      </textArea>
	    </g>
	    <foreignObject width="200" height="200" 
	     requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility">
	      <p xmlns="http://www.w3.org/1999/xhtml">Text goes here</p>
	    </foreignObject>
	    <text x="20" y="20">No automatic linewrapping.</text>
	  </switch>
	  */

		OMSVGSwitchElement s;
		appendChild(s = DibujoHelper.getDoc().createSVGSwitchElement());
		
		OMSVGGElement g;
		s.appendChild(g = DibujoHelper.getDoc().createSVGGElement());
		g.setAttribute("requiredFeatures", "http://www.w3.org/Graphics/SVG/feature/1.2/#TextFlow");
		g.appendChild(ta = DibujoHelper.getDoc().createElementNS("", "textArea"));
		ta.setAttribute("width", "" + w);
		ta.setAttribute("height", (h == 0)?"auto":"" + h);
		ta.appendChild(new OMText(text));
		

		OMSVGForeignObjectElement f;
		s.appendChild(f = DibujoHelper.getDoc().createSVGForeignObjectElement());
		f.setAttribute("requiredFeatures", "http://www.w3.org/TR/SVG11/feature#Extensibility");
		f.setAttribute("width", "" + w);
		f.setAttribute("height", "" + h);
		f.getElement().appendChild(p = Document.get().createElement("p"));
		p.setInnerText(text);
		//f.appendChild(p = DibujoHelper.getDoc().createElementNS("http://www.w3.org/1999/xhtml", "p"));
		//p.setNodeValue(text);
		//f.appendChild(new OMText("<p xmlns=\"http://www.w3.org/1999/xhtml\">" + text + "</p>"));
		
		s.appendChild(t = DibujoHelper.getDoc().createSVGTextElement(0, 0, OMSVGLength.SVG_LENGTHTYPE_PX, text));
		t.setAttribute("pointer-events", "none");
		
	}
	
}
