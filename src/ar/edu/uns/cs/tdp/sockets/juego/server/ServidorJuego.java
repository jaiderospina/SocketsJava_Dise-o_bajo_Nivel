package ar.edu.uns.cs.tdp.sockets.juego.server;

//imports necesarios para sockets y envio/recepcion de datos
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.InetAddress;


import java.util.ArrayList;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;

import ar.edu.uns.cs.tdp.sockets.juego.Nave;

@SuppressWarnings("serial")
public class ServidorJuego extends JFrame {

	private ServerSocket serverSocket;
	
	private ArrayList<Nave> naves;
	private boolean listening;
	
		
	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnIniciarServidor;
	private JTextField textField;


	/**
	 * Creación y escucha (recepción y envío de mensajes) del Socket server
	 */
	
	public void loopServer() {
		try {
			printGUI("*****Inicio servidor*****");
			int port = Integer.parseInt(textField.getText());
			printGUI("Intentando abrir socket servidor, en el puerto "+port+"...\n");
			
			// Creo socket servidor en el puerto indicado
			serverSocket = new ServerSocket(port);
			listening = true;
			printGUI("***Socket creado***\nIP server: "+InetAddress.getLocalHost().getHostAddress() + " - Puerto: "+ serverSocket.getLocalPort());
			
			iniciarThreadInfo(); // Creo un thread para mostrar informacion en la consola
			
			while(listening) {
			
				printGUI("Esperando conexión de cliente...");
				// Se espera aceptar conexión de un cliente (este método bloquea la ejecución del programa hasta que haya conexión)
				Socket clientSocket = serverSocket.accept();
				
				printGUI("\n***Conectado con cliente, IP: "+clientSocket.getInetAddress()+"***\nInicio thread comunicación");
				
				// Creo hilo
				Runnable threadConexion = new ThreadConexion(clientSocket,this);
				Thread hilo = new Thread(threadConexion);
				// Inicio hilo
				hilo.start();
			}
						
			
		} catch (Exception e) {
			e.printStackTrace();
			printGUI("ERROR: "+e.toString());
		}
	
		
	}
	
	public ArrayList<Nave> getNaves() {
		return naves;
	}
	
	
	public void printGUI(final String msj) {
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					textArea.append(msj + "\n" );
				}
			}
		);
	}
	
	/*
	 * Método que crea y arranca un thread
	 * que cada 2 segundos imprime la lista de naves 
	 */
	private void iniciarThreadInfo() {
		Thread threadInfo = new Thread() {
			public void run() {
				while(listening) {
					synchronized (naves) {
						if(!naves.isEmpty())
							printGUI("Lista de naves: "+naves.toString());
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	    };
	    threadInfo.start();	
	}
	
	private void cerrarSockets() {
		try {
			if(serverSocket!= null && !serverSocket.isClosed())
				serverSocket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// También habría que cerrar los sockets de los clientes y matar los threads.
		// Para eso, cada vez que se crea un threadConexion, guardarlo en una lista.
		// En este metodo recorrer la lista de threads y llamar al metodo desconectar()
	}
	/**
	 * Create the frame.
	 */
	public ServidorJuego() {
		naves = new ArrayList<Nave>();
		
		setTitle("SERVIDOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnIniciarServidor = new JButton("Iniciar Servidor");
		btnIniciarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread serverThread = new Thread() {
					public void run() {
						loopServer();
						btnIniciarServidor.setEnabled(false);
					}
			    };
			    serverThread.start();
			
			}
		});
		btnIniciarServidor.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnIniciarServidor.setBounds(373, 386, 165, 45);
		contentPane.add(btnIniciarServidor);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPuerto.setBounds(58, 396, 78, 26);
		contentPane.add(lblPuerto);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setText("21412");
		textField.setBounds(146, 396, 69, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 564, 350);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(textArea);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				listening = false;
				cerrarSockets();
			}
		});
		
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServidorJuego frame = new ServidorJuego();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
