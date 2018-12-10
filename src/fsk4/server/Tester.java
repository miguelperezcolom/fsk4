package fsk4.server;

import java.util.ArrayList;
import java.util.List;

import fsk.server.aux.IGSHelper;
import fsk.server.aux.RIBHelper;
import fsk4.shared.Camara;
import fsk4.shared.Planta;
import fsk4.shared.Material;
import fsk4.shared.Pared;
import fsk4.shared.Plano;
import fsk4.shared.Vertice;

public class Tester {
	
	public static void main(String[] args) throws Exception {
		
		test1();
		
		//test2();
	}

	private static void test2() throws Exception {
		System.out.println(IGSHelper.render(crearPlano(), "/home/miguel/disco"));
	}

	private static void test1() throws Exception {
		System.out.println(RIBHelper.render(crearPlano(), "/home/miguel"));
	}
	
	private static Plano crearPlano() {
		Plano p = new Plano();
		Camara c;
		p.setCamara(c = new Camara());
		c.setX(300);
		c.setY(300);
		c.setPan(150);
		c.setTilt(0);

		Planta h;
		p.getPlantas().add(h = new Planta(p));
		
		List<Vertice> vs = new ArrayList<Vertice>();
		vs.add(new Vertice(0, 0));
		vs.add(new Vertice(10, 0));
		vs.add(new Vertice(10, 10));
		vs.add(new Vertice(0, 10));
		
		Material m = new Material();
		m.setColor("#e6a5e7");
		m.setTextura("");
		m.setTipo("");
		h.getHabitaciones().get(0).setRevestimientoSuelo(m);
		
		m = new Material();
		m.setColor("#a6a5e7");
		m.setTextura("");
		m.setTipo("");
		
		vs.addAll(vs);		
		int posv = 0;
		h.getParedes().add(new Pared(vs.get(posv++ % vs.size()), vs.get(posv++ % vs.size())));
		h.getParedes().add(new Pared(vs.get(posv++ % vs.size()), vs.get(posv++ % vs.size())));
		h.getParedes().add(new Pared(vs.get(posv++ % vs.size()), vs.get(posv++ % vs.size())));
		h.getParedes().add(new Pared(vs.get(posv++ % vs.size()), vs.get(posv++ % vs.size())));
		
		return p;
	}

}
