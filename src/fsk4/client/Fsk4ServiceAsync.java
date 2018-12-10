package fsk4.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fsk4.shared.InfoArticulo;
import fsk4.shared.Modelo;
import fsk4.shared.Plano;
import fsk4.shared.TexturasPlano;

public interface Fsk4ServiceAsync {

	void preview(Plano plano, AsyncCallback<String> callback);

	void sql(String sql, AsyncCallback<String[][]> callback);

	void login(String email, String password, AsyncCallback<Void> callback);

	void registrar(String email, String password, String nombre,
			String apellidos, String nacimiento, String cp, AsyncCallback<Void> callback);

	void getPlano(String id, AsyncCallback<Plano> callback);

	void guardar(String email, Plano plano, AsyncCallback<Long> callback);

	void guardarComo(String email, Plano plano,
			AsyncCallback<Long> callback);

	void foto(String email, Plano plano, int segundosRender, AsyncCallback<Void> callback);

	void cancelarRender(String idRender, AsyncCallback<Void> callback);

	void getDatosUsuario(String email,
			AsyncCallback<Map<String, String>> callback);

	void grabarPerfil(String login, String email, String password,
			String nombre, String apellidos, String nacimiento, String idLogo,
			AsyncCallback<Void> callback);

	void enviarPorEmail(String email, String aemail, Plano plano,
			AsyncCallback<Void> callback);

	void getPlanoEnvio(String idEnvio, AsyncCallback<Plano> callback);

	void getHtmlFormularioTPV(String email, double importeEuros,
			AsyncCallback<String> callback);

	void getConfiguracionesCamara(AsyncCallback<List<String>> callback);

	void getInfoPresupuesto(Plano p,
			AsyncCallback<Map<String, Map<String, String>>> callback);

	void getPDFPresupuesto(String svg, Plano p, AsyncCallback<String> callback);

	void hacerFavorito(String tipo, String email, String idArticulo, AsyncCallback<Boolean> callback);

	void panorama(String email, Plano plano, int segundosRender, AsyncCallback<Void> callback);

	void previewGrande(Plano plano, AsyncCallback<String> callback);

	void getInfoArticulo(String idArticulo, AsyncCallback<InfoArticulo> callback);

	void logError(String s, AsyncCallback<Void> callback);

	void encuesta(String nombre, String email, int valoracion,
			String comentarios, AsyncCallback<Void> callback);

	void rotarZ90(String idArticulo, AsyncCallback<InfoArticulo> callback);

	void rotarX90(String idArticulo, AsyncCallback<InfoArticulo> callback);

	void modificarDatos(String idArticulo, String nombre, String descripcion, String pvp, String estado, AsyncCallback<InfoArticulo> callback);

	void comprobarSesion(AsyncCallback<Map<String, String>> callback);

	void logout(AsyncCallback<Void> callback);

	void getPlanoRender(String idFichero, AsyncCallback<Plano> callback);

	void exportarABlend(String email, Plano plano, AsyncCallback<Void> callback);

	void getTexturas(Plano p, AsyncCallback<TexturasPlano> callback);

}
