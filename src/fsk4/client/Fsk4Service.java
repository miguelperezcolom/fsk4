package fsk4.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fsk4.shared.InfoArticulo;
import fsk4.shared.Modelo;
import fsk4.shared.Plano;
import fsk4.shared.TexturasPlano;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("fsk4")
public interface Fsk4Service extends RemoteService {
	
	String preview(Plano plano) throws Exception;
	String previewGrande(Plano plano) throws Exception;
	String[][] sql(String sql) throws Exception;
	
	
	void login(String email, String password) throws Exception;
	void logout() throws Exception;
	void registrar(String email, String password, String nombre, String apellidos, String nacimiento, String cp) throws Exception;
	
	long guardar(String email, Plano plano) throws Exception;
	long guardarComo(String email, Plano plano) throws Exception;
	Plano getPlano(String id) throws Exception;
	Plano getPlanoEnvio(String idEnvio) throws Exception;
	Plano getPlanoRender(String idFichero) throws Exception;
	void foto(String email, Plano plano, int segundosRender) throws Exception;
	void panorama(String email, Plano plano, int segundosRender) throws Exception;
	void exportarABlend(String email, Plano plano) throws Exception;
	void cancelarRender(String idRender) throws Exception;
	Map<String, String> getDatosUsuario(String email) throws Exception;
	Map<String, String> comprobarSesion() throws Exception;
	void grabarPerfil(String login, String email, String password, String nombre, String apellidos, String nacimiento, String idLogo) throws Exception;
	void enviarPorEmail(String email, String aemail, Plano plano) throws Exception;
	String getHtmlFormularioTPV(String email, double importeEuros) throws Exception;
	List<String> getConfiguracionesCamara() throws Exception;
	Map<String, Map<String, String>> getInfoPresupuesto(Plano p) throws Exception;
	String getPDFPresupuesto(String svg, Plano p) throws Exception;
	boolean hacerFavorito(String tipo, String email, String idArticulo) throws Exception;
	InfoArticulo getInfoArticulo(String idArticulo) throws Exception;
	
	void logError(String s) throws Exception;

	void encuesta(String nombre, String email, int valoracion, String comentarios) throws Exception;
	
	InfoArticulo rotarZ90(String idArticulo) throws Exception;
	InfoArticulo rotarX90(String idArticulo) throws Exception;
	InfoArticulo modificarDatos(String idArticulo, String nombre, String descripcion, String pvp, String estado) throws Exception;
	
	public TexturasPlano getTexturas(Plano p) throws Exception;
	
}
