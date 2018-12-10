package fsk4.client.svg;

import org.vectomatic.dom.svg.OMSVGImageElement;

public abstract class AbstractClickable extends OMSVGImageElement {

	public AbstractClickable(float x, float y, float width, float height, String href) {
		super(x, y, width, height, href);
	}

	public abstract void click();
}
