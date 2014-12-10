package ar.edu.uns.cs.tdp.sockets.pruebas;

//imports necesarios para sockets y envio/recepcion de datos
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class Cliente extends JFrame {

	private Socket miSocket;
	private PrintWriter out;

	
	private JPanel contentPane;
	private JTextArea textArea;
	private JTextField textFieldIP;
	private JTextField textFieldPort;
	private JScrollPane scrollPane;
	private JTextField textField;
	private JLabel lblEscribirYPresionar;

	
	public void conexionAlServer() {
		
		try {
			printGUI("*****Inicio cliente*****");
			int port = Integer.parseInt(textFieldPort.getText());
			String ip = textFieldIP.getText();
			printGUI("Creando socket para conectarse al servidor\n(IP: "+ip+" en el puerto "+port+")...\n");
			
			// Creo socket cliente, con el IP y port del servidor
			miSocket = new Socket(ip,port);
			
			printGUI("***Conectado al servidor***\n(IP "+miSocket.getInetAddress()+" Port: "+miSocket.getPort()+")\n");
		
			
			// Creo stream de datos de tipos simples con los InputStream y OutputStream del socket
			DataOutputStream bufferSalida = new DataOutputStream(miSocket.getOutputStream());
			DataInputStream bufferEntrada = new DataInputStream(miSocket.getInputStream());
			
			printGUI("Se crearon buffers de datos de entrada y salida (para tipos simples) con el servidor conectado");
			
			printGUI("\nProbando recepción de número real:");
			double d = bufferEntrada.readDouble();
			printGUI("Recibido "+d+"\n");
			
			// Envío y recepción de string
			charlaMonorriel(bufferEntrada,bufferSalida);
			
			Thread.sleep(5000);
			
			// Creo stream de entrada y salida para objetos
			ObjectOutputStream bufferSalidaObj = new ObjectOutputStream(miSocket.getOutputStream());
			ObjectInputStream bufferEntradaObj = new ObjectInputStream(miSocket.getInputStream());
			
			printGUI("\nSe crearon buffers de datos de entrada y salida (para objetos) con el servidor conectado");
			
			Thread.sleep(1000);
			
			// Envío y recepción de objetos
			irAlNutricionista(bufferEntradaObj,bufferSalidaObj);
			
			Thread.sleep(1000);
			
			// Enviar mensajes al servidor
			printGUI("\nEnviar mensajes al servidor...\nEscribiendo 'close' se cerrará el socket y terminará la conexión.");
			// Creo buffer salida
			out = new PrintWriter(miSocket.getOutputStream(),true);
			lblEscribirYPresionar.setEnabled(true);
			textField.setEnabled(true);
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			printGUI(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			printGUI(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			printGUI(e.toString());
		}
		
		
	}
	
	
	private void charlaMonorriel(DataInputStream bufferEntrada, DataOutputStream bufferSalida) throws IOException, InterruptedException {
		String env, rec;
		// Creo buffers de lectura y escritura
		//BufferedReader in = new BufferedReader(new InputStreamReader(bufferEntrada));
		//PrintWriter out = new PrintWriter(bufferSalida,true);
			
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("SERVER> \""+rec+"\"");
		
		Thread.sleep(1200);
		
		env = "Alo?";
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
		
		Thread.sleep(1200);
		
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("SERVER> \""+rec+"\"");
		
		Thread.sleep(1200);
		
		env = "Batman?";
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
		
		Thread.sleep(1200);
					
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("SERVER> \""+rec+"\"");
		
		Thread.sleep(1200);
		
		env = "Batman es un científico."; 
		bufferSalida.writeUTF(env);
		//out.println(env);
		printGUI("YO> \""+env+"\"");
		
		Thread.sleep(1200);		
		
		rec = bufferEntrada.readUTF();
		//rec = in.readLine();
		printGUI("SERVER> \""+rec+"\"");
		
	}
	
	private void irAlNutricionista(ObjectInputStream bufferEntradaObj, ObjectOutputStream bufferSalidaObj) throws IOException, InterruptedException, ClassNotFoundException {
		Persona p = new Persona("Barney","Gumble",39,27654231,132.87f);
		printGUI("Persona creada: \n"+p.toString());
		// Envío objeto
		bufferSalidaObj.writeObject(p);
		printGUI("\nPersona enviada al nutricionista...\n");
		Thread.sleep(3000);
		// Recibo objeto modificado
		p = (Persona)bufferEntradaObj.readObject();
		printGUI("Persona recibida:\n"+p.toString());
	}
	
	private void enviarMensaje() {
		String msj = textField.getText();
		out.println(msj);
		textField.setText("");
		printGUI("Enviado: "+msj);
		
		//Si el mensaje es exit, cierro el socket
		if(msj.equals("close")) {
			try {
				miSocket.close();
				out.close();
				printGUI("Socket cerrado");
				lblEscribirYPresionar.setEnabled(false);
				textField.setEnabled(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	public Cliente() {
		
		setTitle("CLIENTE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 557);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIp.setBounds(10, 433, 46, 14);
		contentPane.add(lblIp);
		
		textFieldIP = new JTextField();
		textFieldIP.setText("127.0.0.1");
		textFieldIP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldIP.setBounds(36, 426, 106, 30);
		contentPane.add(textFieldIP);
		textFieldIP.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPuerto.setBounds(152, 433, 53, 16);
		contentPane.add(lblPuerto);
		
		textFieldPort = new JTextField();
		textFieldPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldPort.setText("21412");
		textFieldPort.setBounds(204, 426, 68, 30);
		contentPane.add(textFieldPort);
		textFieldPort.setColumns(10);
		
		JButton btnConectar = new JButton("Conectarse al servidor");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread conexionThread = new Thread() {
					public void run() {
						conexionAlServer();
					}
			    };
			    conexionThread.start();				
			}
		});
		btnConectar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConectar.setBounds(457, 419, 203, 43);
		contentPane.add(btnConectar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 650, 397);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread mensajeThread = new Thread() {
					public void run() {
						enviarMensaje();
					}
			    };
			    mensajeThread.start();
			}
		});
		textField.setEnabled(false);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(281, 478, 379, 30);
		contentPane.add(textField);
		textField.setColumns(10);
		
		lblEscribirYPresionar = new JLabel("Escribir y presionar enter para enviar:");
		lblEscribirYPresionar.setEnabled(false);
		lblEscribirYPresionar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEscribirYPresionar.setBounds(28, 481, 244, 24);
		contentPane.add(lblEscribirYPresionar);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(miSocket!= null && !miSocket.isClosed()) {
					try {
						miSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
					Cliente frame = new Cliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
