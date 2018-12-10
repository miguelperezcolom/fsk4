package fsk4.server;

import java.util.Date;

import javax.persistence.EntityManager;

import fsk.server.modelo.Helper;
import fsk.server.modelo.Render;
import fsk.server.modelo.Transaccion;

public class Renderizador {
	
	public static boolean enMarcha = true;
	
	public static void main(String[] args) {
		bucle();
	}

	public static void bucle() {
		while (enMarcha) {
			
			try {
				recogerYRenderizar();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void recogerYRenderizar() throws Exception {
		String[][] r = Helper.sqlString("sql: select ren_id from fsk_render where ren_estado in ('" + Render.ESTADO_ENCOLA + "', '" + Render.ESTADO_RENDERIZANDO + "') order by ren_creado asc");
		if (r.length > 0) {
			render(r[0][0]);
		}
	}

	private static void render(final String id) throws Exception {
		Helper.transact(new Transaccion() {
			
			@Override
			public void ejecutar(EntityManager em) throws Exception {
				Render r = em.find(Render.class, Long.parseLong(id));
				r.setCoimenzoRender(new Date());
				r.setEstado(r.ESTADO_RENDERIZANDO);
			}
		});
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Render r = em.find(Render.class, Long.parseLong(id));
					System.out.println("" + new Date() + ": renderizando foto hd " + id + " " + r.getUsuario().getEmail() + " " + r.getCreado());
					r.renderYEnviar(em, System.getProperty("pathtmp", "/home/miguel/disco/wsx/miguel/fsk3/war/tmp"));
				}
			});
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Render r = em.find(Render.class, Long.parseLong(id));
					r.setFinRender(new Date());
					r.setEstado(r.ESTADO_HECHO);
				}
			});
		} catch (Exception e) {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Render r = em.find(Render.class, Long.parseLong(id));
					r.setFinRender(new Date());
					r.setEstado(r.ESTADO_ERROR);
				}
			});
		}
	}

}
