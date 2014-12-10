package ar.edu.uns.cs.tdp.sockets.pruebas;

import java.io.Serializable;


public class Persona implements Serializable {

	private static final long serialVersionUID = -3324607016464494814L;
	private String nombre;
	private String apellido;
	private int edad;
	private int DNI;
	private float peso;
	
	
	public Persona(String n, String a, int e, int d, float p) {
		nombre = n;
		apellido = a;
		edad = e;
		DNI = d;
		peso = p;
	}
	
	public String toString() {
		return "NOMBRE: "+ nombre+ " APELLIDO: "+apellido+
				"\nDNI: "+DNI+" EDAD: "+edad+" PESO: "+peso;
	}
	
	public void adelgazar() {
		peso-=25;
	}
}
