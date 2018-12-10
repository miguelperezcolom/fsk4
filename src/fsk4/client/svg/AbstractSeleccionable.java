package fsk4.client.svg;

import static com.google.gwt.query.client.GQuery.$;
import fsk4.client.DibujoHelper;


public abstract class AbstractSeleccionable extends AbstractArrastable {

	public void seleccionar() {
		seleccionado();
		DibujoHelper.actualizarInfo();
		$(".trozo-info").hide();
		$("#"+ getIdTrozoInfo()).show();
		$(".toolbar-boton.info").click();
	}

	public abstract void seleccionado();
	
	public abstract String getIdTrozoInfo();

	/*
	limpiarSeleccion();
	if (o instanceof Planta) o = getPlano();
	seleccionado = o;
	if (o instanceof Camara) actualizarFOV();
	
	if (!(o instanceof FuenteLuz) && !(o instanceof Foto)) {
		rotadorActivo = rotadores.get(payload.get(o));
		if (rotadorActivo != null) rotadorActivo.setAttribute("visibility", "visible");
	}
	
	marcarSeleccion();
	visualizarBotonera();
	visualizarSlidersEInfo();
	//actualizarInfo();
	//$(".toolbar-boton.info").click();

*/


}
