package ar.edu.uns.cs.tdp.sockets.juego.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ar.edu.uns.cs.tdp.sockets.juego.Nave;

/**
 * Comunicación de servidor con un cliente
 * Implementa Runnable para poder ser lanzada en un hilo aparte
 * 
 * @author Diego
 *
 */

public class ThreadConexion implements Runnable {
	
	private Socket clienteSocket;
	private boolean conectado;
	private ServidorJuego serv; 
	private ObjectOutputStream bufferSalidaObj;
	private ObjectInputStream bufferEntradaObj;

	private Nave player; 
	private ThreadConexionOutput threadOutput;
	
	public ThreadConexion(Socket cli,ServidorJuego serv) {
		clienteSocket = cli;
		conectado = true;
		this.serv = serv;
		
		
		// Creo stream de entrada y salida para objetos
		try {
			bufferSalidaObj = new ObjectOutputStream(cli.getOutputStream());
			bufferEntradaObj = new ObjectInputStream(cli.getInputStream());
						
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		threadOutput = new ThreadConexionOutput(bufferSalidaObj, serv);
		threadOutput.start();
		
		try {
			while(conectado) {
			
				// Recibo su nave
				Nave nav = (Nave)bufferEntradaObj.readObject();
				serv.printGUI("Recibo de "+clienteSocket.getInetAddress()+": "+nav.toString());
				// Sincronizo: para que no se solape esta llamada con otros threads,
				// y no actualizar la lista mientras otro la está leyendo
				synchronized (serv.getNaves()) {
					recibirNave(nav);
				}
				/*
				  Escritura: la hago en el otro thread
				 
					if(conectado) //controlo que no haya recibido mensaje de finalización
					{
						
						// Sincronizo: para que no se solape esta llamada con otros threads,
						// y no enviar la lista mientras se está actualizando
						synchronized(serv.getNaves()) {
							// Envío lista de naves
							// Hago reset primero para que se envíen los nuevos valores
							bufferSalidaObj.reset();
							bufferSalidaObj.writeObject(serv.getNaves());
							serv.printGUI(serv.getNaves().toString());
						}
						//Thread.sleep(30);
					}
				*/	
			}
			
			// Si conectado es false - Quito nave de la lista y Cierro socket 
			
			// Sincronizo para que no se solape con otros threads
			synchronized(serv.getNaves()) {
				quitarNave(player);
				serv.printGUI("Quito nave. "+serv.getNaves().toString());
			}
			
			desconectar();
		
		//} catch (InterruptedException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Recibo nave del cliente: si esta en la lista la actualizo. Si no la inserto
	 * Este método es llamado dentro de un bloque synchronized
	 */
	private void recibirNave(Nave nave){
		// Mensaje especial: desconecta
		if (nave.getJugador().equals("EXIT") && nave.getColor()==-1) {
			conectado = false;
			return;
		}
				
		boolean flag = true;
		Nave n;
		for (int i=0; i<serv.getNaves().size() && flag;i++) {
			n = serv.getNaves().get(i);
			if(n.getJugador().equals(nave.getJugador())) {
				serv.getNaves().set(i, nave);
				flag = false;
			}
		}
		//Si no está
		if(flag) {
			serv.getNaves().add(nave);
		}
		
		player = nave;
	}
	
	/*
	 * Este método es llamado dentro de un bloque synchronized
	 */
	private void quitarNave(Nave nav) {
		boolean flag = true;
		Nave n; int index = 0;
		for (int i=0; i<serv.getNaves().size() && flag;i++) {
			n = serv.getNaves().get(i);
			if(n.getJugador().equals(nav.getJugador())) {
				index = i;
				flag = false;
			}
		}
		if(!flag) {
			serv.getNaves().remove(index);
		}
	}
	
	
	public void desconectar() {
		try {
			threadOutput.desconectar();
			Thread.sleep(30);
			bufferSalidaObj.close();
			bufferEntradaObj.close();
			clienteSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public Socket getClienteSocket() {
		return clienteSocket;
	}
}
