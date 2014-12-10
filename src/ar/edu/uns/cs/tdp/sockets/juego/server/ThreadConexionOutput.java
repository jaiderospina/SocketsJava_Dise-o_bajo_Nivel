package ar.edu.uns.cs.tdp.sockets.juego.server;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Comunicación de servidor con un cliente
 * Extiende thread para poder ser lanzada en un hilo aparte
 * 
 * @author Diego
 *
 */

public class ThreadConexionOutput extends Thread {
	
	private boolean conectado;
	private ServidorJuego serv; 
	private ObjectOutputStream bufferSalidaObj;

//	private Nave player; 
	
	public ThreadConexionOutput(ObjectOutputStream output,ServidorJuego serv) {
		conectado = true;
		this.serv = serv;
		bufferSalidaObj = output;
			
	}
	
	@Override
	public void run() {

		try {
			while(conectado) { 
			
				// Sincronizo: para que no se solape esta llamada con otros threads,
				// y no enviar la lista mientras se está actualizando
				synchronized(serv.getNaves()) {
					// Envío lista de naves
					// Uso writeUnshared para que se envíen los nuevos valores
					bufferSalidaObj.writeUnshared(serv.getNaves());
				//	serv.printGUI(serv.getNaves().toString());
				}
				Thread.sleep(30);
				
			}
			
		
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void desconectar() {
		conectado = false;
	}
	

}