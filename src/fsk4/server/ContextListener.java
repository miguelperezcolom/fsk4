package fsk4.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener  implements ServletContextListener {

			@Override
			public void contextDestroyed(ServletContextEvent event) {
				Renderizador.enMarcha = false;
			}

			@Override
			public void contextInitialized(ServletContextEvent event) {
				System.out.println("contexte " + event.getServletContext().getServletContextName() + " inicialitzat");	
				
				if (!"true".equalsIgnoreCase(System.getProperty("test"))) new Thread(new Runnable() {
					
					@Override
					public void run() {
						Renderizador.bucle();
					}
				}).start();
				
			}

}
