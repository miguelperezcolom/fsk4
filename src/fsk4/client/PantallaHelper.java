package fsk4.client;

import static com.google.gwt.query.client.GQuery.$;
import static gwtquery.plugins.draggable.client.Draggable.Draggable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import fsk4.client.svg.EventoMouse;
import fsk4.client.svg.SVGHelper;
import fsk4.shared.Camara;
import fsk4.shared.FuenteLuz;
import fsk4.shared.Material;
import fsk4.shared.Modelo;
import fsk4.shared.Pared;
import fsk4.shared.Plano;
import fsk4.shared.Planta;
import fsk4.shared.Puerta;
import fsk4.shared.Ventana;
import gwtquery.plugins.draggable.client.DraggableHandler;
import gwtquery.plugins.draggable.client.DraggableOptions;
import gwtquery.plugins.draggable.client.DraggableOptions.DragFunction;
import gwtquery.plugins.draggable.client.DraggableOptions.HelperType;
import gwtquery.plugins.draggable.client.events.DragContext;

public class PantallaHelper {
	
	public static final int NIVEL_TODO = 0;
	public static final int NIVEL_CATALOGO = 1;
	public static final int NIVEL_FABRICANTE = 2;
	public static final int NIVEL_FAMILIA = 3;
	public static final int NIVEL_SUBFAMILIA = 4;
	
	private static Map<String, CampoTextura> camposTextura = new HashMap<String, CampoTextura>();
	private static Map<String, CampoArticulo> camposArticulo = new HashMap<String, CampoArticulo>();
	private static int segundosEspera;
	private static RepeatingCommand rpEspera;

	public static CampoTextura getCampoTextura(String id) {
		return camposTextura.get(id);
	}
	
	public static CampoArticulo getCampoArticulo(String id) {
		return camposArticulo.get(id);
	}

	public static void crearSelectorArticulos(final String idContenedor, final Seleccionado seleccionado) {
		$("#"+ idContenedor).html($("#divarticulos").html().replaceAll("uhwsuqwhsqwhscabecera", (idContenedor.toLowerCase().contains("textura"))?"Texturas":"Mobiliario").replaceAll("uhwsuqwhsqwhs", idContenedor));
		
		$("#q-" + idContenedor).keyup(new Function() {
			@Override
			public void f(Element e) {
				buscar(idContenedor, seleccionado);
			}
		});
		
		$("#buscar-" + idContenedor).click(new Function() {
			@Override
			public void f(Element e) {
				buscar(idContenedor, seleccionado);
			}
		});
		
		$("#marcas-arbol-" + idContenedor + " a").click(new Function() {
			@Override
			public void f(Element e) {
				$(e).parent().nextAll().remove();
				cargarCatalogos("marcas", idContenedor, $(e).attr("data-id"), Integer.parseInt($(e).attr("data-nivel")), seleccionado);
			}
		});

		if (false) $("#familias-arbol-" + idContenedor + " a").click(new Function() {
			@Override
			public void f(Element e) {
				$(e).parent().nextAll().remove();
				cargarCatalogos("familias", idContenedor, $(e).attr("data-id"), Integer.parseInt($(e).attr("data-nivel")), seleccionado);
			}
		});

		$("#tabfavoritos-" + idContenedor).click(new Function() {
			@Override
			public void f(Element e) {
				cargarFavoritos(idContenedor, seleccionado);
			}
		});

		cargarCatalogos("marcas", idContenedor, "", 0, seleccionado);
		if (false) cargarCatalogos("familias", idContenedor, "", 0, seleccionado);
		
	}
	
	private static void cargarFavoritos(final String idContenedor, final Seleccionado seleccionado) {
		$("#favoritos-" + idContenedor).html("");
		$("#favoritos-" + idContenedor).html("<img src='img/comecocos.gif' style='position:absolute; left: 150px; top: 200px;''>");
		
		boolean estructura = idContenedor.contains("estructura");
		boolean mobiliario = idContenedor.contains("mobiliario");
		boolean texturas = idContenedor.contains("textura");
		
		String sql = "";
		if (texturas) {
			//                              0         1                       2                  3        4            5              6              7                          8          9                        10             11				12
			sql = "sql: select mat_id, mat_nombre, fab_nombre, mat_id, mat_ancho, mat_alto, vp.fic_id, vp.fic_nombre, pp.fic_id, pp.fic_nombre, '', f_material, 0.01, 0, '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''  " +
					" from fsk_material " +
					" inner join fsk_subfamilia on sub_id = mat_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" inner join fsk_fabricante on fab_id = mat_fabricante " +
					" left outer join fsk_fichero vp on  vp.fic_id =mat_imagenvistaprevia60x60  " +
					" left outer join fsk_fichero pp on  pp.fic_id = mat_imagenparaplano  " +
					" inner join fsk_usuario_materialfavorito on f_usuario = '" + Datos.get().getEmail() + "' and f_material = mat_id " +
					" where mat_activo and mat_imagenparaplano is not null and not mat_indigo and not mat_texturamodelo ";
			sql += "order by mat_nombre, fab_nombre";
		} else {
			//                              0         1                       2                  3        4            5              6              7                          8          9                        10        11		12                     13	     14				15				16				17						18			19						20				21			22					23				24
			sql = "sql: select art_id, art_nombre, fab_nombre, art_id, art_ancho, art_alto, vp.fic_id, vp.fic_nombre, pp.fic_id, pp.fic_nombre, art_tipo, f_articulo, art_escala, art_altura, v0.fic_id, v0.fic_nombre, v90.fic_id, v90.fic_nombre, v180.fic_id, v180.fic_nombre, v270.fic_id, v270.fic_nombre, art_cota, art_cotaarriba  " +
					" from fsk_articulo " +
					" inner join fsk_subfamilia on sub_id = art_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" inner join fsk_fabricante on fab_id = art_fabricante " +
					" left outer join fsk_fichero m on m.fic_id = art_modelo " +
					" left outer join fsk_fichero vp on  vp.fic_id =art_imagenvistaprevia60x60  " +
					" left outer join fsk_fichero pp on  pp.fic_id = art_imagenparaplano  " +
					" left outer join fsk_fichero v0 on  v0.fic_id = art_imagenvista0  " +
					" left outer join fsk_fichero v90 on  v90.fic_id = art_imagenvista90  " +
					" left outer join fsk_fichero v180 on  v180.fic_id = art_imagenvista180  " +
					" left outer join fsk_fichero v270 on  v270.fic_id = art_imagenvista270  " +
					" inner join fsk_usuario_articulofavorito on f_usuario = '" + Datos.get().getEmail() + "' and f_articulo = art_id " +
					" where art_activo and art_estado = 'OK' ";
			if (estructura) sql += " and art_estructura ";
			if (mobiliario) sql += " and art_tipo in ('objeto', 'puerta', 'ventana', 'foto') ";
			sql += " and art_imagenparaplano is not null ";
		}
		
		if (idContenedor.contains("textura")) {
			//                             0         1                       2                  3                   4            5              6              7                          8          9                    10		11	    12   13
			sql = "sql: select mat_id, mat_nombre, fab_nombre, mat_id, mat_ancho, mat_alto, vp.fic_id, vp.fic_nombre, pp.fic_id, pp.fic_nombre, '' , f_material, 0.01, 0  " +
					" from fsk_material inner join fsk_fabricante on fab_id = mat_fabricante " +
					" left outer join fsk_fichero vp on  vp.fic_id =mat_imagenvistaprevia60x60  " +
					" left outer join fsk_fichero pp on  pp.fic_id = mat_imagenparaplano  " +
					" inner join fsk_usuario_materialfavorito on f_usuario = '" + Datos.get().getEmail() + "' and f_material = mat_id " +
					" where mat_activo and mat_imagenparaplano is not null and not mat_indigo and not mat_texturamodelo ";
			sql += "order by mat_nombre, fab_nombre";
		} else {			
		//                             0         1                       2                  3        4            5              6              7                          8          9                    10					11			12               13
		sql = "sql: select art_id, art_nombre, fab_nombre, art_id, art_ancho, art_alto, vp.fic_id, vp.fic_nombre, pp.fic_id, pp.fic_nombre, art_tipo, f_articulo, art_escala, art_altura  " +
				" from fsk_articulo inner join fsk_fabricante on fab_id = art_fabricante " +
				" left outer join fsk_fichero vp on  vp.fic_id =art_imagenvistaprevia60x60  " +
				" left outer join fsk_fichero pp on  pp.fic_id = art_imagenparaplano  " +
				" inner join fsk_usuario_articulofavorito on f_usuario = '" + Datos.get().getEmail() + "' and f_articulo = art_id " +
				" where art_activo and art_estado = 'OK' ";
			if (idContenedor.contains("estructura")) sql += " and art_estructura ";		
			if (idContenedor.contains("mobiliario")) sql += " and art_tipo in ('objeto', 'puerta', 'ventana', 'foto') ";
			sql += " and art_imagenparaplano is not null ";
			sql += "order by art_nombre, fab_nombre";
		}
		
		Fsk4.getServicio().sql(sql, new AsyncCallback<String[][]>() {
			
			@Override
			public void onSuccess(String[][] result) {
				rellenar(result, "favoritos-" + idContenedor , seleccionado);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
			}
		});
	}

	protected static void actualizarArticulo(String id, Modelo m) {
		
		if (m.getId() != null && !m.getId().equals(m.getId())) {
			CampoArticulo c = PantallaHelper.getCampoArticulo(id);
			m.setAlto(m.getAlto());
			m.setAncho(m.getAncho());
			m.setId(m.getId());
			m.setImagenParaPlano(m.getImagenParaPlano());
			m.setImagenVistaPrevia(m.getImagenVistaPrevia());

			DibujoHelper.actualizaModelo();
		}

	}

	protected static void actualizarMaterial(String id, Material m) {
		CampoTextura c = PantallaHelper.getCampoTextura(id);
		m.setId(c.getId());
		m.setAlto(c.getAlto());
		m.setAncho(c.getAncho());
		m.setColor(c.getColor());
		m.setTextura(c.getTextura());
		m.setTipo(c.getTipo());
		
		if ("habitacion_revestimientosuelo".equals(id)) DibujoHelper.actualizaSuelo();
	}

	/*
<div	id="catalogo-arbol-uhwsuqwhsqwhs">
									<div style="height: 20px; padding-left: 0px;"><i class="icon-folder-open"></i>Todo</div>
									<div style="height: 20px; padding-left: 20px;"><i class="icon-folder-open"></i> Baño</div>
									<div style="height: 20px; padding-left: 40px;"><i class="icon-folder-open"></i> Roca</div>
									<div style="height: 20px; padding-left: 60px;"><i class="icon-folder-open"></i> Accesorios</div>								
							</div>	 
	 * */

	
	public static void cargarCatalogos(final String pestanya, final String idContenedor, String id, final int nivel, final Seleccionado seleccionado) {
		$("#" + pestanya + "-lista-" + idContenedor).css("top", (30 + nivel * 20) + "px");
		if (nivel == NIVEL_SUBFAMILIA) {
			$("#" + pestanya + "-lista-" + idContenedor).css("left", "0px");
			$("#" + pestanya + "-lista-" + idContenedor).addClass("catalogo-lista-articulos");
		} else {
			$("#" + pestanya + "-lista-" + idContenedor).css("left", (30 + nivel * 20) + "px");
			$("#" + pestanya + "-lista-" + idContenedor).removeClass("catalogo-lista-articulos");
		}
		$("#" + pestanya + "-lista-" + idContenedor).html("");
		$("#" + pestanya + "-lista-" + idContenedor).html("<img src='img/comecocos.gif' style='position:absolute; left: 150px; top: 200px; width: 60px; height: 60px;'>");
		
		boolean texturas = idContenedor.contains("textura");
		
		String sql = "";
		if (nivel == NIVEL_TODO) {
			sql = "sql: select distinct cat_id, cat_nombre  " +
					" from fsk_articulo " +
					" inner join fsk_subfamilia on sub_id = art_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" where art_activo and art_imagenparaplano is not null and art_estado = 'OK' ";
			if (texturas) {
				//                                          0         1
				sql += " and art_tipo in ('materialindigo') ";
			} else {
				//                                          0         1
				sql += " and art_tipo in ('objeto', 'foto', 'puerta', 'ventana') ";
			}
			sql += "order by cat_nombre";
		} else if (nivel == NIVEL_CATALOGO) {
			//                                          0         1
			sql = "sql: select distinct fab_id, fab_nombre  " +
					" from fsk_articulo " +
					" inner join fsk_fabricante on fab_id = art_fabricante " +
					" inner join fsk_subfamilia on sub_id = art_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" where art_activo and art_imagenparaplano is not null and art_estado = 'OK' ";
			if (texturas) {
				//                                          0         1
				sql += " and art_tipo in ('materialindigo') ";
			} else {
				//                                          0         1
				sql += " and art_tipo in ('objeto', 'foto', 'puerta', 'ventana') ";
			}
			sql += " and cat_id = " + id + " ";
			sql += " order by fab_nombre";			
		} else if (nivel == NIVEL_FABRICANTE) {
			//                                          0         1
			sql = "sql: select distinct fam_id, fam_nombre  " +
					" from fsk_articulo " +
					" inner join fsk_fabricante on fab_id = art_fabricante " +
					" inner join fsk_subfamilia on sub_id = art_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" where art_activo and art_imagenparaplano is not null and art_estado = 'OK' ";
			if (texturas) {
				//                                          0         1
				sql += " and art_tipo in ('materialindigo') ";
			} else {
				//                                          0         1
				sql += " and art_tipo in ('objeto', 'foto', 'puerta', 'ventana') ";
			}
			sql += " and cat_id = " + $("#" + pestanya + "-arbol-" + idContenedor + " a[data-nivel = '" + NIVEL_CATALOGO + "']").attr("data-id") + " ";
			sql += " and fab_id = " + id + " ";
			sql += " order by fam_nombre";			

		} else if (nivel == NIVEL_FAMILIA) {
			//                                          0         1
			sql = "sql: select distinct sub_id, sub_nombre  " +
					" from fsk_articulo " +
					" inner join fsk_fabricante on fab_id = art_fabricante " +
					" inner join fsk_subfamilia on sub_id = art_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" where art_activo and art_imagenparaplano is not null and art_estado = 'OK' ";
			if (texturas) {
				//                                          0         1
				sql += " and art_tipo in ('materialindigo') ";
			} else {
				//                                          0         1
				sql += " and art_tipo in ('objeto', 'foto', 'puerta', 'ventana') ";
			}
			sql += " and cat_id = " + $("#" + pestanya + "-arbol-" + idContenedor + " a[data-nivel = '" + NIVEL_CATALOGO + "']").attr("data-id") + " ";
			sql += " and fab_id = " + $("#" + pestanya + "-arbol-" + idContenedor + " a[data-nivel = '" + NIVEL_FABRICANTE + "']").attr("data-id") + " ";
			sql += " and fam_id = " + id + " ";
			sql += "order by sub_nombre";
		} else if (nivel == NIVEL_SUBFAMILIA) {
			sql = "sql: select art_id, art_nombre, fab_nombre, art_id, art_ancho, art_alto, vp.fic_id, vp.fic_nombre, pp.fic_id, pp.fic_nombre, art_tipo, f_articulo, art_escala, art_altura, v0.fic_id, v0.fic_nombre, v90.fic_id, v90.fic_nombre, v180.fic_id, v180.fic_nombre, v270.fic_id, v270.fic_nombre, art_cota, art_cotaarriba  " +
					" from fsk_articulo " +
					" inner join fsk_subfamilia on sub_id = art_subfamilia " +
					" inner join fsk_familia on fam_id = sub_familia " +
					" inner join fsk_catalogo on cat_id = fam_catalogo " +
					" inner join fsk_fabricante on fab_id = art_fabricante " +
					" left outer join fsk_fichero m on m.fic_id = art_modelo " +
					" left outer join fsk_fichero vp on  vp.fic_id =art_imagenvistaprevia60x60  " +
					" left outer join fsk_fichero pp on  pp.fic_id = art_imagenparaplano  " +
					" left outer join fsk_fichero v0 on  v0.fic_id = art_imagenvista0  " +
					" left outer join fsk_fichero v90 on  v90.fic_id = art_imagenvista90  " +
					" left outer join fsk_fichero v180 on  v180.fic_id = art_imagenvista180  " +
					" left outer join fsk_fichero v270 on  v270.fic_id = art_imagenvista270  " +
					" left outer join fsk_usuario_articulofavorito on f_usuario = '" + Datos.get().getEmail() + "' and f_articulo = art_id " +
					" where art_activo and art_imagenparaplano is not null and art_estado = 'OK' ";
			if (texturas) {
				//                                          0         1
				sql += " and art_tipo in ('materialindigo') ";
			} else {
				//                                          0         1
				sql += " and art_tipo in ('objeto', 'foto', 'puerta', 'ventana') ";
			}
			sql += " and fab_id = " + $("#" + pestanya + "-arbol-" + idContenedor + " a[data-nivel = '" + NIVEL_FABRICANTE + "']").attr("data-id") + " ";
			sql += " and sub_id = " + id + " ";
			sql += "order by art_nombre";
		}
		
		Fsk4.getServicio().sql(sql, new AsyncCallback<String[][]>() {

			@Override
			public void onFailure(Throwable caught) {
				Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
			}

			@Override
			public void onSuccess(String[][] result) {				
				if (nivel == NIVEL_SUBFAMILIA) {
					rellenar(result, "" + pestanya + "-lista-" + idContenedor, seleccionado);
				} else {
					StringBuffer h = new StringBuffer();
					
					h.append("<table class='table table-hover'>");
					if (result != null) for (final String[] f : result) {
						h.append("<tr><td><a href='#' class='catalogo-contenido-nivel-" + idContenedor + "' data-id='" + f[0] + "' data-nombre='" + f[1].replaceAll("'", "&apos;") + "' data-nivel='"+ (nivel + 1) + "'><i class='icon-folder-close'></i>"+ f[1] +"</a></td></tr>");
					}
					h.append("</table>");

					$("#" + pestanya + "-lista-" + idContenedor).html("");
					$("#" + pestanya + "-lista-" + idContenedor).html(h.toString());
					
					if (result.length == 1) {
						
						String[] f = result[0];
						
						$("#" + pestanya + "-arbol-" + idContenedor).append("<div style='height: 20px; padding-left: " + ((nivel + 1) * 20) + "px;'><a href='#' class='catalogo-nivel' data-id='" + f[0] + "' data-nombre='" + f[1].replaceAll("'", "&apos;") + "' data-nivel='"+ (nivel + 1) + "'><i class='icon-folder-open'></i>" + f[1] + "</a></div>");
						
						cargarCatalogos(pestanya, idContenedor, f[0], (nivel + 1), seleccionado);
						
						$("#" + pestanya + "-arbol-" + idContenedor + " a").last().click(new Function() {
							@Override
							public void f(Element e) {
								$(e).parent().nextAll().remove();
								cargarCatalogos(pestanya, idContenedor, $(e).attr("data-id"), Integer.parseInt($(e).attr("data-nivel")), seleccionado);
							}
						});
					}

					$("#" + pestanya + "-lista-" + idContenedor + " a").click(new Function() {
						public void f(Element e) {
							
							$("#" + pestanya + "-arbol-" + idContenedor).append("<div style='height: 20px; padding-left: " + ((nivel + 1) * 20) + "px;'><a href='#' class='catalogo-nivel' data-id='" + $(e).attr("data-id") + "' data-nombre='" + $(e).attr("data-nombre").replaceAll("'", "&apos;") + "' data-nivel='"+ $(e).attr("data-nivel") + "'><i class='icon-folder-open'></i>" + $(e).attr("data-nombre") + "</a></div>");
							
							cargarCatalogos(pestanya, idContenedor, $(e).attr("data-id"), Integer.parseInt($(e).attr("data-nivel")), seleccionado);
							
							$("#" + pestanya + "-arbol-" + idContenedor + " a").last().click(new Function() {
								@Override
								public void f(Element e) {
									$(e).parent().nextAll().remove();
									cargarCatalogos(pestanya, idContenedor, $(e).attr("data-id"), Integer.parseInt($(e).attr("data-nivel")), seleccionado);
								}
							});
						};
					});
					
				}
			}
		});
	}
	
	
	
	protected static void buscar(final String idContenedor, final Seleccionado seleccionado) {
		String q = $("#q-" + idContenedor).val();
		if (q.length() > 1) {
			$("#encontrados-" + idContenedor).html("");
			$("#encontrados-" + idContenedor).html("<img src='img/comecocos.gif' style='position:absolute; left: 150px; top: 200px;''>");
			boolean texturas = idContenedor.contains("textura");
			String sql = "";
				//                              0         1                       2                  3        4            5              6              7                          8          9                        10        11		12                     13	     14				15				16				17						18			19						20				21			22					23				24
				sql = "sql: select art_id, art_nombre, fab_nombre, art_id, art_ancho, art_alto, vp.fic_id, vp.fic_nombre, pp.fic_id, pp.fic_nombre, art_tipo, f_articulo, art_escala, art_altura, v0.fic_id, v0.fic_nombre, v90.fic_id, v90.fic_nombre, v180.fic_id, v180.fic_nombre, v270.fic_id, v270.fic_nombre, art_cota, art_cotaarriba  " +
						" from fsk_articulo " +
						" inner join fsk_subfamilia on sub_id = art_subfamilia " +
						" inner join fsk_familia on fam_id = sub_familia " +
						" inner join fsk_catalogo on cat_id = fam_catalogo " +
						" inner join fsk_fabricante on fab_id = art_fabricante " +
						" left outer join fsk_fichero m on m.fic_id = art_modelo " +
						" left outer join fsk_fichero vp on  vp.fic_id =art_imagenvistaprevia60x60  " +
						" left outer join fsk_fichero pp on  pp.fic_id = art_imagenparaplano  " +
						" left outer join fsk_fichero v0 on  v0.fic_id = art_imagenvista0  " +
						" left outer join fsk_fichero v90 on  v90.fic_id = art_imagenvista90  " +
						" left outer join fsk_fichero v180 on  v180.fic_id = art_imagenvista180  " +
						" left outer join fsk_fichero v270 on  v270.fic_id = art_imagenvista270  " +
						" left outer join fsk_usuario_articulofavorito on f_usuario = '" + Datos.get().getEmail() + "' and f_articulo = art_id " +
						" where art_activo and art_estado = 'OK' ";
				if (texturas) {
					//                                          0         1
					sql += " and art_tipo in ('materialindigo') ";
				} else {
					//                                          0         1
					sql += " and art_tipo in ('objeto', 'foto', 'puerta', 'ventana') ";
				}
				sql += " and art_imagenparaplano is not null ";
			
				if (q != null && !"".equals(q)) {
					String cond = "";
					for (String t : q.split(" ")) {
						if (!t.trim().equals("")) {
							if (!"".equals(cond)) cond += " and ";
							cond += " lower(art_nombre) like '%" + t.toLowerCase().replaceAll("'", "''") + "%' ";
						}
					}
					sql += " and (" + cond + ") ";
				}
				sql += " order by fab_nombre, art_nombre";

				Fsk4.getServicio().sql(sql, new AsyncCallback<String[][]>() {

					@Override
					public void onSuccess(String[][] result) {
						rellenar(result, "encontrados-" + idContenedor , seleccionado);
					}

					@Override
					public void onFailure(Throwable caught) {
						Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
					}
				});			
		}
	};

	protected static void rellenar(String[][] result, final String idContenedor, final Seleccionado seleccionado) {
		
		StringBuffer h = new StringBuffer();
		h.append("<table class='table table-hover'>");
		int pos = 0;
		for (final String[] f : result) {
			//h.append("<tr><td><a class='hint--bottom' data-hint='Arrastrar para añadir' ><img class='boton-modelo " + idContenedor + "' data-id='" + f[0]  + "' data-favorito='" + (("null".equals(f[11]))?"false":"·true")  + "' data-tipo='" + f[10]  + "' data-escala='" + f[12]  + "' data-altura='" + f[13]  + "' data-ancho='" + f[4]  + "' data-alto='" + f[5]  + "' data-preview='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[6]  + "/" + f[7]  + "' data-cenital='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[8]  + "/" + f[9] + "' data-vista0='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[14]  + "/" + f[15] + "'' data-vista90='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[16]  + "/" + f[17] + "'' data-vista180='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[18]  + "/" + f[19] + "'' data-vista270='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[20]  + "/" + f[21] + "'' src='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[6]  + "/" + f[7]  + "' style='width: 60px; height: 60px;max-width: none;'></a></td><td><b>" + f[1]  + "</b><br><img class='boton-favorito' data-id='" + f[0]  + "' src='img/" + ((f[11] == null || "".equals(f[11]))?"glyphicons_019_heart_empty.png":"glyphicons_012_heart.png") + "'><b>" + f[2] + "</b><br/><a class='hint--bottom' data-hint='Disponible en' href='" + ((idContenedor.contains("textura"))?"http://www.el58depicasso.es/":"http://www.sociasyrossello.es/") + "' target='_blank'><img src='publico/images/" + ((idContenedor.contains("textura"))?"el58depicasso":"socias") + ".png' width='100px' height='30px'></a></td></tr>");
			double cota = 0;
			try {
				cota = DibujoHelper.ESCALA * Double.parseDouble(f[22]);
				if (f[23].contains("t")) cota = cota - Double.parseDouble(f[13]);  
			} catch (Exception e) {
				
			}
			//h.append("<tr><td><a class='hint--bottom' data-hint='Arrastrar para añadir' ><img class='boton-modelo " + idContenedor + "' data-id='" + f[0]  + "' data-favorito='" + (("null".equals(f[11]))?"false":"·true")  + "' data-tipo='" + f[10]  + "' data-test='a' data-cota='" + cota  + "' data-escala='" + f[12]  + "' data-altura='" + f[13]  + "' data-ancho='" + f[4]  + "' data-alto='" + f[5]  + "' data-preview='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[6]  + "/" + f[7]  + "' data-cenital='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[8]  + "/" + f[9] + "' data-vista0='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[14]  + "/" + f[15] + "'' data-vista90='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[16]  + "/" + f[17] + "'' data-vista180='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[18]  + "/" + f[19] + "'' data-vista270='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[20]  + "/" + f[21] + "'' src='" + System.getProperty("urlfeskteva", "http://www.fescateva.com") + "/fileviewer/" + f[6]  + "/" + f[7]  + "' style='width: 60px; height: 60px;max-width: none;'></a></td><td><b>" + f[1]  + "</b><br><img class='boton-favorito' data-id='" + f[0]  + "' src='img/" + ((f[11] == null || "".equals(f[11]))?"glyphicons_019_heart_empty.png":"glyphicons_012_heart.png") + "'><b>" + f[2] + "</b><br/><a class='hint--bottom' data-hint='Disponible en' href='http://www.feskteva.com' target='_blank'><img src='publico/images/watermark.png' width='100px' height='30px'></a></td></tr>");
			if (pos % 2 == 0) {
				if (pos > 0) h.append("</tr>");
				h.append("<tr>");
			}
			h.append("<td><a class='hint--bottom' data-hint='" + f[1]  + "' ><img class='boton-modelo " + idContenedor + "' data-id='" + f[0]  + "' data-favorito='" + (("null".equals(f[11]))?"false":"·true")  + "' data-tipo='" + f[10]  + "' data-test='a' data-cota='" + cota  + "' data-escala='1' data-altura='" + f[13]  + "' data-ancho='" + f[4]  + "' data-alto='" + f[5]  + "' data-preview='fileviewer/" + f[6]  + "/" + f[7]  + "' data-cenital='fileviewer/" + f[8]  + "/" + f[9] + "' data-vista0='fileviewer/" + f[14]  + "/" + f[15] + "'' data-vista90='fileviewer/" + f[16]  + "/" + f[17] + "'' data-vista180='fileviewer/" + f[18]  + "/" + f[19] + "'' data-vista270='fileviewer/" + f[20]  + "/" + f[21] + "'' src='fileviewer/" + f[6]  + "/" + f[7]  + "' style='width: 100px; height: 100px;max-width: none;'></a></td>");
			pos++;
		}
		if (pos > 0) h.append("</tr>");
		h.append("</table>");
		$("#" + idContenedor).html(h.toString());

		if (seleccionado != null) {
			$(".boton-modelo." + idContenedor + "").click(new Function() {
				@Override
				public void f(Element e) {
					seleccionado.seleccionado($(e).attr("data-tipo"), $(e).attr("data-id"), $(e).attr("data-preview"), $(e).attr("data-cenital"), Double.parseDouble($(e).attr("data-ancho")), Double.parseDouble($(e).attr("data-alto")), "true".equals($(e).attr("data-favorito")), Double.parseDouble($(e).attr("data-escala")));
				}
			});
		} else {
			final DraggableOptions options = new DraggableOptions();
			//options.setCursor(Cursor.MOVE);
		    options.setHelper(HelperType.CLONE);
		    //options.setHelper($("<div style='width: 150px;height: 150px;background-color: orange;'>hola</div>"));
		    options.setCursor(Cursor.MOVE);
		    if (Fsk4.isTableta())  options.setAppendTo("body");
		    else options.setAppendTo("#toolbar-mobiliario");
			//set the options you want... See next paragraph
		    
			options.setOnDragStart(new DragFunction() {
				
				@Override
				public void f(DragContext context) {
					GQuery e = $(context.getDraggable());
					
					if (idContenedor.contains("textura")) {
						double patwi = DibujoHelper.ESCALA * Double.parseDouble(e.attr("data-ancho")) / 100;  
						double pathe = DibujoHelper.ESCALA * Double.parseDouble(e.attr("data-alto")) / 100;  
						DibujoHelper.startDragOnTextura(e.attr("data-id"), e.attr("data-preview"), e.attr("data-cenital"), patwi, pathe);
					} else {
						if (!DibujoHelper.enAlzado) {
							Modelo m = DibujoHelper.anadirModelo(e.attr("data-tipo"), e.attr("data-id"), e.attr("data-preview"), e.attr("data-cenital"), Float.parseFloat(e.attr("data-ancho")), Float.parseFloat(e.attr("data-alto")), context.getHelperPosition().left, context.getHelperPosition().top, Float.parseFloat(e.attr("data-cota")), 0f, "true".equals(e.attr("data-favorito")), Float.parseFloat(e.attr("data-escala")), Float.parseFloat(e.attr("data-altura")), e.attr("data-vista0"), e.attr("data-vista90"), e.attr("data-vista180"), e.attr("data-vista270"));
							//DibujoHelper.md(DibujoHelper.payload.get(m), m, context.get.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
							DibujoHelper.startDragOn(m);
						} else {
							DraggableHandler.getInstance(context.getDraggable()).revertToOriginalPosition(new Function() {
								@Override
								public void f(Element e) {
									System.out.println("xxxx");
								}
							});
							Window.alert("No se pueden añadir objetos mientras estamos editando el alzado de una pared");
						}
					}
					
					
				}
			});
			options.setOnDragStop(new DragFunction() {
				
				@Override
				public void f(DragContext context) {
					System.out.println("drag stop!!!!!!!!!!!!!!");
					if (Fsk4.isTableta()) {
						Fsk4.info("drag stop!!!!!!!!!!!!!!");
					}
					//if (DibujoHelper.texturaEnDrag == null) DibujoHelper.mu(context.getHelperPosition().left, context.getHelperPosition().top);
					if (DibujoHelper.startDragPendiente != null) {
						if (Fsk4.isTableta()) {
							Fsk4.info("DibujoHelper.startDragPendiente != null (" + context.getHelperPosition().left + "," + context.getHelperPosition().top + ")");
							SVGHelper.mm(new EventoMouse(context.getHelperPosition().left, context.getHelperPosition().top, true));
						} else {
							DibujoHelper.startDragPendiente = null;
							DibujoHelper.gplano.setAttribute("cursor", "auto");	
						}
					}
				}
			});
			//call the plug-in with your options 
			$(".boton-modelo." + idContenedor + "").as(Draggable).draggable(options); 
			$(".boton-favorito").click(new Function() {
				@Override
				public void f(final Element e) {
					AccionHelper.autenticar(new Runnable() {
						
						@Override
						public void run() {
							System.out.println("pasando!!!");
							Fsk4.getServicio().hacerFavorito(((idContenedor.contains("textura"))?"material":"articulo"), Datos.get().getEmail(), $(e).attr("data-id"), new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									$(e).attr("src", "img/" + ((!result)?"glyphicons_019_heart_empty.png":"glyphicons_012_heart.png"));
								}
							});
						}
					});
				}
			});

		}
		
	}

	public static void dinamizar() {
		
		$(".preview-agrandar").click(new Function() {
			@Override
			public void f(Element e) {
				AccionHelper.agrandarPreview();
			}
		});

		
		/*
		PantallaHelper.crearSelectorArticulos("toolbar-estructura", new Seleccionado() {
			
			@Override
			public void seleccionado(String tipo, String id, String preview,
					String cenital, double ancho, double alto, boolean favorito) {
				DibujoHelper.anadirModelo(tipo, id, preview, cenital, ancho, alto, 100d, 100d, 0d, 0d, favorito);
			}
		});
		*/

		PantallaHelper.crearSelectorArticulos("toolbar-mobiliario", null);
		
		PantallaHelper.crearSelectorArticulos("toolbar-textura", null);

		
		Fsk4.getServicio().getConfiguracionesCamara(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Fsk4.alert("" + caught.getClass().getName() + ":" + caught.getMessage());
			}

			@Override
			public void onSuccess(List<String> result) {
				for (String f : result) {
					$("#plano_tmcamera_funcionrespuesta").append("<option value='" + f + "'>" + f + "</option>");
				}
			}
		});
		
		
		$(".textura").each(new Function() {
			@Override
			public Object f(Element e, int i) {
				
				$(e).hide().after("<div id='divcontenedor-" + $(e).attr("id") + "'></div>");
				
				CampoTextura c;
				camposTextura.put($(e).attr("id"), c = new CampoTextura($(e).attr("id")));
				RootPanel.get("divcontenedor-" + $(e).attr("id")).add(c);
				
				return null;
			}
		});
		
		$(".articulo").each(new Function() {
			@Override
			public Object f(Element e, int i) {
				
				$(e).hide().after("<div id='divcontenedor-" + $(e).attr("id") + "'></div>");
				
				CampoArticulo  c;
				camposArticulo.put($(e).attr("id"), c = new CampoArticulo($(e).attr("id")));
				RootPanel.get("divcontenedor-" + $(e).attr("id")).add(c);
				
				return null;
			}
		});
 
		
		$(".parametro-configuracion").change(new Function() {
			@Override
			public void f(Element e) {
				
				if (DibujoHelper.isRefrescoActivo()) {
					
					boolean refrescarPreview = !"plano_zoom".equals($(e).attr("id"));
					
					Object seleccionado = DibujoHelper.getSeleccionado();
					
					if (seleccionado instanceof Camara) {
						((Camara) seleccionado).setZ(Float.parseFloat($("#camara_altura").val()) * DibujoHelper.ESCALA);
						((Camara) seleccionado).setTilt(Float.parseFloat($("#camara_tilt").val()));
						((Camara) seleccionado).setRadioApertura(Float.parseFloat($("#camara_radioapertura").val()));
						((Camara) seleccionado).setAnchoSensor(Float.parseFloat($("#camara_anchosensor").val()));
						((Camara) seleccionado).setDistanciaSensor(Float.parseFloat($("#camara_distanciasensor").val()));
						((Camara) seleccionado).setTiempoExposicion(Float.parseFloat($("#camara_tiempoexposicion").val()));
						DibujoHelper.actualizarFOV();
					} else if (seleccionado instanceof FuenteLuz) {
						((FuenteLuz) seleccionado).setZ(Float.parseFloat($("#bombilla_altura").val()) * DibujoHelper.ESCALA);
						((FuenteLuz) seleccionado).setLumens(Integer.parseInt($("#bombilla_lumens").val()));
					} else if (seleccionado instanceof Planta) {		
						/*
						actualizarMaterial("habitacion_revestimientosuelo", ((Habitacion) seleccionado).getRevestimientoSuelo());
						actualizarMaterial("habitacion_revestimientoparedes", ((Habitacion) seleccionado).getRevestimientoParedes());
						actualizarMaterial("habitacion_cenefa", ((Habitacion) seleccionado).getCenefa());
						((Habitacion) seleccionado).setConCenefa($("#habitacion_tienecenefa").is(":checked"));					
						((Habitacion) seleccionado).setAlturaCenefa(Float.parseFloat($("#habitacion_alturacenefa").val()) * DibujoHelper.ESCALA);					
						((Habitacion) seleccionado).setAlturaParedes(Float.parseFloat($("#habitacion_alturaparedes").val()) * DibujoHelper.ESCALA);					
						actualizarMaterial("habitacion_revestimientosobrecenefa", ((Habitacion) seleccionado).getRevestimientoParedesSobreCenefa());
						*/
					} else if (seleccionado instanceof Ventana) {
						((Ventana) seleccionado).setZ(Float.parseFloat($("#ventana_altura").val()) * DibujoHelper.ESCALA);
						((Ventana) seleccionado).setAlto(Float.parseFloat($("#ventana_alto").val()) * DibujoHelper.ESCALA);
						((Ventana) seleccionado).setAncho(Float.parseFloat($("#ventana_ancho").val()) * DibujoHelper.ESCALA);
						((Ventana) seleccionado).setDistanciaDesdeDe(Float.parseFloat($("#ventana_distanciaparedizda").val()) * DibujoHelper.ESCALA);
						((Ventana) seleccionado).setPomoALaIzquierda($("#ventana_pomoalaizda").is(":checked"));
						((Ventana) seleccionado).setHojas(Integer.parseInt($("#ventana_numerohojas").val()));
						DibujoHelper.actualizaPuerta((Puerta)seleccionado);
					} else if (seleccionado instanceof Puerta) {
						((Puerta) seleccionado).setAlto(Float.parseFloat($("#puerta_alto").val()) * DibujoHelper.ESCALA); 
						((Puerta) seleccionado).setAncho(Float.parseFloat($("#puerta_ancho").val()) * DibujoHelper.ESCALA);
						((Puerta) seleccionado).setDistanciaDesdeDe(Float.parseFloat($("#puerta_distanciaparedizda").val()) * DibujoHelper.ESCALA);
						((Puerta) seleccionado).setPomoALaIzquierda($("#puerta_pomoalaizda").is(":checked"));
						((Puerta) seleccionado).setHojas(Integer.parseInt($("#puerta_numerohojas").val()));
						DibujoHelper.actualizaPuerta((Puerta)seleccionado);
					} else if (seleccionado instanceof Pared) {
						DibujoHelper.setLongitudPared((Pared) seleccionado, Float.parseFloat($("#pared_longitud").val()));
					} else if (seleccionado instanceof Plano) {
						((Plano) seleccionado).setZoom(Float.parseFloat($("#plano_zoom").val()));
						SVGHelper.zoom();
						
						((Plano) seleccionado).setPintarTecho($("#plano_pintartecho").is(":checked"));
						((Plano) seleccionado).setLuzSolar($("#plano_luzsolar").is(":checked"));
						((Plano) seleccionado).setLuzAmbiente($("#plano_luzambiente").is(":checked"));
						((Plano) seleccionado).setSundir($("#plano_direccionsol").val());
						((Plano) seleccionado).setTurbidity(Float.parseFloat($("#plano_turbidity").val()));
						((Plano) seleccionado).setExtraAtmospheric($("#plano_extraatmosferico").is(":checked"));
						((Plano) seleccionado).setSkylightModel($("#plano_modeloluzsolar").val());
						
						((Plano) seleccionado).setTonemapping($("#plano_tonemapping").val());
						((Plano) seleccionado).setToneMappingCameraEVAdjust(Float.parseFloat($("#plano_tmcamera_evadjust").val()));
						((Plano) seleccionado).setToneMappingCameraFilmISO(Float.parseFloat($("#plano_tmcamera_filmiso").val()));
						((Plano) seleccionado).setToneMappingCameraResponseFunctionPath($("#plano_tmcamera_funcionrespuesta").val());
						((Plano) seleccionado).setToneMappingLinearScale(Float.parseFloat($("#plano_tmlinear_scale").val()));
						((Plano) seleccionado).setToneMappingReinhardBurn(Float.parseFloat($("#plano_tmreinhard_burn").val()));
						((Plano) seleccionado).setToneMappingReinhardPostScale(Float.parseFloat($("#plano_tmreinhard_postscale").val()));
						((Plano) seleccionado).setToneMappingReinhardPreScale(Float.parseFloat($("#plano_tmreinhard_prescale").val()));
					} else if (seleccionado instanceof Modelo) {
						((Modelo) seleccionado).setZ(Float.parseFloat($("#modelo_altura").val()) * DibujoHelper.ESCALA);
						actualizarArticulo("modelo_articulo", ((Modelo) seleccionado));
					}
					
					
					if (refrescarPreview) DibujoHelper.refrescaPreview();
					
				}
				
			}
		});
	}

	public static void abrirDialogo(String idDiv, int ancho, int alto, Runnable r) {
			abrirDialogo(idDiv, ancho, alto, r, true);
	}
	
	public static void abrirDialogo(String idDiv, int ancho, int alto, Runnable r, boolean sePuedeCerrar) {

		// get the screen height and width  
		int maskHeight = Window.getClientHeight();  
		int maskWidth = Window.getClientWidth();
		
//		$("#dialogo-box").height(height);
//		$("#dialogo-box").width(width);
		
		// calculate the values for center alignment
		int dialogTop =  (maskHeight - alto) / 2;  
		int dialogLeft = (maskWidth - ancho) /2; 
		
		// assign values to the overlay and dialog box
		$("#modal-background").height(maskHeight).width(maskWidth).show();
		$("#modal-contenedor").css("height", "" + alto + "px!important").css("width", "" + ancho + "px!important").css("top", "" + dialogTop).css("left", "" + dialogLeft).show();
		$("#modal-contenido").width(ancho);
		
		$("#modal-contenido").html($("#" + idDiv).html());
		
		if (sePuedeCerrar) {
			$("#modal-cerrar").show();
		} else {
			$("#modal-cerrar").hide();
		}
		
		//RootPanel.get("modal-contenido").clear();
		//RootPanel.get("modal-contenido").add(new HTML($("#" + idDiv).html()));
		
		r.run();
	}
	
	public static void abrirDialogo(Widget w, int ancho, int alto, Runnable r) {
		// get the screen height and width  
		int maskHeight = Window.getClientHeight();  
		int maskWidth = Window.getClientWidth();
		
//		$("#dialogo-box").height(height);
//		$("#dialogo-box").width(width);
		
		// calculate the values for center alignment
		int dialogTop =  (maskHeight - alto) / 2;  
		int dialogLeft = (maskWidth - ancho) /2; 
		
		// assign values to the overlay and dialog box
		$("#modal-background").height(maskHeight).width(maskWidth).show();
		$("#modal-contenedor").css("top", "" + dialogTop).css("left", "" + dialogLeft).show();

		//$("#modal-contenido").html($("#" + idDiv).html());

		$("#modal-contenido").add($(w));
		
		r.run();
		
	}
	

	public static void cerrarDialogo() {
		$("#modal-background").hide();
		$("#modal-contenedor").hide();
	}

	public static void abrirSelectorArticulo(Widget w, Runnable r) {
		$("#seleccionarticulo-contenedor").show();

		
		RootPanel.get("seleccionarticulo-contenido").clear();
		RootPanel.get("seleccionarticulo-contenido").add(w);
		
		r.run();		
	}
	
	public static void cerrarSelectorArticulo() {
		$("#seleccionarticulo-background").hide();
		$("#seleccionarticulo-contenedor").hide();
	}

	protected static boolean esperando;

	public static void espera(String msg, int segundos) {
		int maskHeight = Window.getClientHeight();  
		int maskWidth = Window.getClientWidth();
		
		int dialogTop =  (maskHeight/2) - 50;  
		int dialogLeft = (maskWidth/2) - ($("#popin-content-holder").width()/2); 
		
		// assign values to the overlay and dialog box
		$("#espera-background").height(maskHeight).width(maskWidth).show();
		$("#espera-contenedor").css("top", "" + dialogTop).css("left", "" + dialogLeft).show();

		segundosEspera = segundos;
		esperando = true;
		
		$("#espera-mensaje").html(msg);
		$("#espera-progreso").html("Faltan " + segundos + " segundos.");
		
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
			
			@Override
			public boolean execute() {
				$("#espera-progreso").html("Faltan " + segundosEspera-- + " segundos.");
				return esperando;
			}
		}, 1000);
	}

	public static void finEspera() {
		esperando = false;
		$("#espera-background").hide();
		$("#espera-contenedor").hide();
	}


}
