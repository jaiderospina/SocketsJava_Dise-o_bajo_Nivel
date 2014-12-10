package ar.edu.uns.cs.tdp.sockets.pruebas;

//imports necesarios para sockets y envio/recepcion de datos
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.InetAddress;


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

import javax.swing.JScrollPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class Server extends JFrame {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnIniciarServidor;
	private JTextField textField;
	private JButton btnEsperarCliente;


	/**
	 * Creación y escucha (recepción y envío de mensajes) del Socket server
	 */
	
	public void crearSocketServer() {
		try {
			printGUI("*****Inicio servidor*****");
			int port = Integer.parseInt(textField.getText());
			printGUI("Intentando abrir socket servidor, en el puerto "+port+"...\n");
			
			// Creo socket servidor en el puerto indicado
			serverSocket = new ServerSocket(port);
			
			printGUI("***Socket creado***\nIP server: "+InetAddress.getLocalHost().getHostAddress() + " - Puerto: "+ serverSocket.getLocalPort());
			printGUI("Presione el botón para esperar aceptar la conexión de un cliente...\n");
			btnEsperarCliente.setEnabled(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			printGUI("ERROR: "+e.toString());
		}
	
		
	}
	
	public void conexionCliente() {
		
		try {
			printGUI("Esperando conexión de cliente...");
			
			// Se espera aceptar conexión de un cliente (este método bloquea la ejecución del programa hasta que haya conexión)
			clientSocket = serverSocket.accept();
			
			//btnEsperarCliente.setEnabled(false);
			printGUI("\n***Conectado con cliente, IP: "+clientSocket.getInetAddress()+"***");
			

			// Creo stream de datos de tipos simples con los InputStream y OutputStream del cliente
			DataInputStream bufferEntrada = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream bufferSalida = new DataOutputStream(clientSocket.getOutputStream());
			
			printGUI("Se crearon buffers de datos de entrada y salida (para tipos simples) con el cliente conectado");
			
			// Envío número real
			printGUI("\nProbando envío de número real:");
			double d = 21.7;
			bufferSalida.writeDouble(d);
			printGUI("Enviado "+d+"\n");
			
			// Envío y recepción de string
			charlaMonorriel(bufferEntrada,bufferSalida);
					
			Thread.sleep(5000);
			
			// Creo stream de entrada y salida para objetos con el cliente conectado
			ObjectInputStream bufferEntradaObj = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream bufferSalidaObj = new ObjectOutputStream(clientSocket.getOutputStream());
			printGUI("\nSe crearon buffers de datos de entrada y salida (para objetos) con el cliente conectado");
			
			Thread.sleep(1000);
			
			// Envío y recepción de objetos
			hacerDieta(bufferEntradaObj,bufferSalidaObj);
			
			Thread.sleep(1000);
			
			// Recibo mensajes del cliente hasta que envíe exit, donde cierro el socket
			recibirMensajes(clientSocket);
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			printGUI("ERROR: "+e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			printGUI("ERROR: "+e.toString());
		}
		
	}
	
	private void charlaMonorriel(DataInputStream bufferEntrada, DataOutputStream bufferSalida) throws IOException, InterruptedException {
		// Creo buffers de lectura y escritura
		//BufferedReader in = new BufferedReader(new InputStreamReader(bufferEntrada));
		//PrintWriter out = new PrintWriter(bufferSalida,true);
		
		String env, rec;
		
		env = "Homero? Homero?!!";
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
		
		Thread.sleep(1000);
		
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("CLIENT> \""+rec+"\"");
		
		Thread.sleep(1000);
		
		env = "Homero, hay un hombre que puede ayudarte";
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
		
		Thread.sleep(1000);
		
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("CLIENT> \""+rec+"\"");
		
		Thread.sleep(1000);
		
		env = "No, es un científico.";
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
		
		Thread.sleep(1000);
		
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("CLIENT> \""+rec+"\"");
		
		Thread.sleep(1000);
		
		env = "Que no es Batman!";
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
			
	}
	
	private void hacerDieta(ObjectInputStream bufferEntradaObj, ObjectOutputStream bufferSalidaObj) throws ClassNotFoundException, IOException, InterruptedException {
		// Recibo objeto (debo hacer casting)
		Persona p = (Persona) bufferEntradaObj.readObject();
		printGUI("\nPersona recibida:\n"+p.toString());
		printGUI("\nPersona hace dieta...\n");
		p.adelgazar();
		Thread.sleep(3000);
		// Envío objeto modificado
		bufferSalidaObj.writeObject(p);
		printGUI("Persona enviada:\n"+p.toString());
		
	}
	
	private void recibirMensajes(Socket clientSocket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		printGUI("\nRecibo mensajes del cliente...\n");
		
		boolean fin = false;
		String msj;
		// Recibo mensajes hasta que el mensaje sea close
		while(!fin) {
			msj = in.readLine();
			printGUI("CLIENT> \""+msj+ "\"");
			if (msj.equals("close")) {
				fin = true;
			}
		}
		
		if(fin) {
			printGUI("Cierro socket cliente");
			clientSocket.close();
			//serverSocket.close();
			in.close();
		}
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
	
	
	/**
	 * Create the frame.
	 */
	public Server() {
		
		setTitle("SERVIDOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnIniciarServidor = new JButton("Iniciar Servidor");
		btnIniciarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				crearSocketServer();
				//btnIniciarServidor.setEnabled(false);
			}
		});
		btnIniciarServidor.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnIniciarServidor.setBounds(266, 511, 165, 45);
		contentPane.add(btnIniciarServidor);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPuerto.setBounds(22, 521, 78, 26);
		contentPane.add(lblPuerto);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setText("21412");
		textField.setBounds(110, 521, 69, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnEsperarCliente = new JButton("Aceptar cliente");
		btnEsperarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread conexionThread = new Thread() {
					public void run() {
						conexionCliente();
					}
			    };
			    conexionThread.start();
			}
		});
		btnEsperarCliente.setEnabled(false);
		btnEsperarCliente.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEsperarCliente.setBounds(487, 512, 173, 45);
		contentPane.add(btnEsperarCliente);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 650, 473);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(textArea);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(serverSocket != null) { 
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (clientSocket!= null && !clientSocket.isClosed()) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
