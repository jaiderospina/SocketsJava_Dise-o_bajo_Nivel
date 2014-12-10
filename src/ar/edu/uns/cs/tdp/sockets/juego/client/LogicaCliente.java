package ar.edu.uns.cs.tdp.sockets.juego.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.uns.cs.tdp.sockets.juego.Nave;


/**
 * Clase que mantiene la lógica del juego en el cliente 
 *  - Contiene la nave del jugador y lista de naves enemigos
 *  - Se encarga de la comunicación con el servidor para indicar posicion de nave local y recibir las enemigas
 * 
 * @author Diego Schwindt
 *
 */
public class LogicaCliente {

	protected Nave player;
	protected List<Nave> enemigos;
	private Socket miSocket;
	
	protected boolean conectado;
	protected boolean movimiento;
	
	public LogicaCliente() {
		enemigos = new ArrayList<Nave>();
		movimiento = false;
	}
	
	public void setPlayer(Nave p) {
		player = p;
	}
	
	public Nave getPlayer() {
		return player;
	}
	public List<Nave> getEnemigos() {
		return enemigos;
	}
	
	
	public void moverPlayer(boolean up, boolean down, boolean left, boolean right) {
		if(left)
			player.setX(player.getX()-2);
		if(right)
			player.setX(player.getX()+2);
		if(up)
			player.setY(player.getY()-2);
		if(down)
			player.setY(player.getY()+2);
		
		movimiento = true; // Seteo esta variable para enviar al servidor la nueva posicion
	}
	
	/*
	 * Conexión con el servidor (es ejecutado en un thread aparte al ser invocado por la GUI)
	 */
	public void conexion(String ip, int port) {
		
		try {
			// Creo socket para conectarme con el server
			miSocket = new Socket(ip,port);
			conectado = true;
			movimiento = true; // Inicialmente es true para enviar la primera posicion al servidor
			ArrayList<Nave> lista;
			// Creo stream de entrada y salida para objetos
			ObjectOutputStream bufferSalidaObj = new ObjectOutputStream(miSocket.getOutputStream());
			ObjectInputStream bufferEntradaObj = new ObjectInputStream(miSocket.getInputStream());
			
		
			while(conectado) {
				// Si hubo movimiento, Envío mi nave
				// Uso writeunshared para que se actualicen los valores de objeto a enviar
				if(movimiento) {
					bufferSalidaObj.writeUnshared(player);
					System.out.println("Enviado: "+player.toString());
					movimiento = false;
				}
				
				// recibo todas las naves del server
				lista = (ArrayList<Nave>) bufferEntradaObj.readObject();
				recibirLista(lista); 
				System.out.println("Enemigos: "+enemigos.toString());
				//Thread.sleep(30);
			}
			
			// Conectado false: Sali, envio nave especial para cerrar
			Nave n = new Nave("EXIT", -1);
			bufferSalidaObj.reset();
			bufferSalidaObj.writeObject(n);
			System.out.println("Enviado mensaje finalizacion");		
			
			Thread.sleep(250);	// Espero un instante para que llegue bien el mensaje antes de cerrar

			bufferSalidaObj.close();
		
			miSocket.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		
	}
	
	/*
	 * Método que separa la nave del jugador del resto de las enemigas
	 */
	public void recibirLista(ArrayList<Nave> lista) {
		System.out.println("Recibido: "+lista.toString());
		boolean flag = true;
		Nave n; int index = 0;
		for (int i=0; i<lista.size() && flag;i++) {
			n = lista.get(i);
			if(n.getJugador().equals(player.getJugador())) {
				index = i;
				flag = false;
			}
		}
		if(!flag) {
			lista.remove(index);
		}
		enemigos = lista;
		
	}
	
	public boolean getConectad() {
		return conectado;
	}
	
	public void salir() {
		conectado = false;
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player = null;
		enemigos = new ArrayList<Nave>();
	
	}
	
	
}
