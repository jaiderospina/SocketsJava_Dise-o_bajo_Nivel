package ar.edu.uns.cs.tdp.sockets.juego;

import java.io.Serializable;
import java.util.Random;

public class Nave implements Serializable {
	
	private static final long serialVersionUID = 2840858292370613131L;
	public static final int COLOR_ROJO = 1;
	public static final int COLOR_MARRON = 2;
	public static final int COLOR_VERDE = 3;
	public static final int COLOR_VIOLETA = 4;
	
	private String jugador;
	private int posX;
	private int posY;
	private int color;

	
	public Nave(String nombre, int color) {
		this.jugador = nombre;
		this.color = color;
		Random r = new Random();
		posX = r.nextInt(700);
		posY = r.nextInt(400);
	}
	
	public String getJugador() {
		return jugador;
	}
	public int getColor() {
		return color;
	}
	public int getX() {
		return posX;
	}
	public int getY() {
		return posY;
	}
	public void setX(int x) {
		posX = x;
	}
	public void setY(int y) {
		posY = y;
	}
	
	public String toString() {
		String c = "";
		switch (color) {
			case COLOR_ROJO:
				c = "roja";
				break;
			case COLOR_MARRON:
				c = "marron";
				break;
			case COLOR_VERDE:
				c = "verde";
				break;
			case COLOR_VIOLETA:
				c = "violeta";
				break;
			default:
				break;
		}
	
		return jugador + " ("+c+") - Pos: ("+posX+","+posY+")";
	}
}
