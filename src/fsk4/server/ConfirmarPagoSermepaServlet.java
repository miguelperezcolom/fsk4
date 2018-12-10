package fsk4.server;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fsk.server.modelo.Helper;
import fsk.server.modelo.Movimiento;
import fsk.server.modelo.Transaccion;


public class ConfirmarPagoSermepaServlet extends HttpServlet {

	@Override
	protected void service(final HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			Helper.transact(new Transaccion() {
				
				@Override
				public void ejecutar(EntityManager em) throws Exception {
					Movimiento m = em.find(Movimiento.class, Long.parseLong(request.getParameter("Ds_Order")));
					m.setObservaciones(request.getParameter("Ds_Response"));
					m.setEstado(m.ESTADO_ERROR);
					try {
						if (Integer.parseInt(request.getParameter("Ds_Response").trim(), 10) < 101) {
							m.setEstado(m.ESTADO_CONFIRMADO);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		response.getWriter().print("ok");
	}
}
