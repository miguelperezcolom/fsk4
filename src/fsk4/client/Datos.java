package fsk4.client;

public class Datos {

	private static Datos d;
	
	private String email;
	
	
	public static Datos get() {
		if (d == null) d = new Datos();
		return d;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
