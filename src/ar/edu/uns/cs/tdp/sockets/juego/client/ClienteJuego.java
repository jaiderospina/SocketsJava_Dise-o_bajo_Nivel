package ar.edu.uns.cs.tdp.sockets.juego.client;

import java.awt.EventQueue;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;

import ar.edu.uns.cs.tdp.sockets.juego.Nave;

@SuppressWarnings("serial")
public class ClienteJuego extends JFrame {
	

	private LogicaCliente juego;
	private GUIJuego panel;
	private JPanel contentPane;
	private JTextField textFieldJugador;
	private JTextField textFieldIP;
	private JTextField textFieldPort;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNaveRoja;
	private JRadioButton rdbtnNaveMarrn;
	private JRadioButton rdbtnNaveVerde;
	private JRadioButton rdbtnNaveVioleta;
	private JButton buttonConect;
	private JButton btnCrearNave;
	private AbstractButton btnSalir;
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteJuego frame = new ClienteJuego();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*
	 * Crea nave jugador con el color y nombre indicado y lo setea en la logica 
	 */
	private void crearNave() {
		int botonSelected = 0;
		
		if(rdbtnNaveRoja.isSelected()) {
			botonSelected = Nave.COLOR_ROJO;
		}
		else if (rdbtnNaveMarrn.isSelected()) {
			botonSelected = Nave.COLOR_MARRON;
		}
		else if (rdbtnNaveVerde.isSelected()) {
			botonSelected = Nave.COLOR_VERDE;
		}
		else if (rdbtnNaveVioleta.isSelected()) {
			botonSelected = Nave.COLOR_VIOLETA;
		}
		
		Nave player = new Nave(textFieldJugador.getText(),botonSelected);
		juego.setPlayer(player);
				
		activarCreacion(false);
		activarConexion(true);
		panel.setFocusable(true);
				
	}
	
	/*
	 * Invoca a la logica para realiza la conexion con el servidor en un thread aparte 
	 */
	private void conectar() {
		activarConexion(false);
		btnSalir.setEnabled(true);
		panel.setFocusable(true);
		// La conexión con el server se hace en otro thread
		Thread conexionThread = new Thread() {
			public void run() {
				int port = Integer.parseInt(textFieldPort.getText());
				String ip = textFieldIP.getText();
				juego.conexion(ip,port);
			}
	    };
	    conexionThread.start();	
	    
	}
	
	/*
	 *  Avisa a la logica desconectarse del servidor
	 */
	
	private void salir() {
		btnSalir.setEnabled(false);
		activarCreacion(true);
		juego.salir();
	}

	
	private void activarCreacion(boolean b) {
		textFieldJugador.setEnabled(b);
		rdbtnNaveMarrn.setEnabled(b);
		rdbtnNaveRoja.setEnabled(b);
		rdbtnNaveVerde.setEnabled(b);
		rdbtnNaveVioleta.setEnabled(b);
		btnCrearNave.setEnabled(b);
	}
	private void activarConexion(boolean b) {
		buttonConect.setEnabled(b);
		textFieldIP.setEnabled(b);
		textFieldPort.setEnabled(b);
		
	}
	
	
	/**
	 * Create the frame.
	 */
	public ClienteJuego() {

		juego = new LogicaCliente();
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Panel que dibujará las naves
		panel = new GUIJuego(juego);
		panel.setBounds(10, 10, 760, 440);
		contentPane.add(panel);
		
		
		rdbtnNaveRoja = new JRadioButton("Nave roja");
		rdbtnNaveRoja.setSelected(true);
		buttonGroup.add(rdbtnNaveRoja);
		rdbtnNaveRoja.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnNaveRoja.setBounds(278, 458, 117, 23);
		contentPane.add(rdbtnNaveRoja);
		
		rdbtnNaveMarrn = new JRadioButton("Nave marr\u00F3n");
		buttonGroup.add(rdbtnNaveMarrn);
		rdbtnNaveMarrn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnNaveMarrn.setBounds(278, 484, 117, 23);
		contentPane.add(rdbtnNaveMarrn);
		
		JLabel lblMiNombre = new JLabel("Mi nombre:");
		lblMiNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMiNombre.setBounds(20, 471, 83, 29);
		contentPane.add(lblMiNombre);
		
		textFieldJugador = new JTextField();
		textFieldJugador.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldJugador.setBounds(113, 471, 136, 29);
		contentPane.add(textFieldJugador);
		textFieldJugador.setColumns(10);
		
		rdbtnNaveVerde = new JRadioButton("Nave verde");
		buttonGroup.add(rdbtnNaveVerde);
		rdbtnNaveVerde.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnNaveVerde.setBounds(412, 458, 109, 23);
		contentPane.add(rdbtnNaveVerde);
		
		rdbtnNaveVioleta = new JRadioButton("Nave violeta");
		buttonGroup.add(rdbtnNaveVioleta);
		rdbtnNaveVioleta.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnNaveVioleta.setBounds(412, 484, 109, 23);
		contentPane.add(rdbtnNaveVioleta);
		
		JLabel lblIpServidor = new JLabel("IP servidor:");
		lblIpServidor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIpServidor.setBounds(20, 524, 83, 14);
		contentPane.add(lblIpServidor);
		
		textFieldIP = new JTextField();
		textFieldIP.setText("127.0.0.1");
		textFieldIP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldIP.setColumns(10);
		textFieldIP.setBounds(114, 517, 106, 30);
		contentPane.add(textFieldIP);
		
		JLabel label_1 = new JLabel("Puerto:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(243, 523, 53, 16);
		contentPane.add(label_1);
		
		textFieldPort = new JTextField();
		textFieldPort.setText("21412");
		textFieldPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldPort.setColumns(10);
		textFieldPort.setBounds(317, 517, 68, 30);
		contentPane.add(textFieldPort);
		
		buttonConect = new JButton("Conectarse al servidor");
		buttonConect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectar();
			}
		});
		buttonConect.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonConect.setBounds(422, 510, 203, 43);
		buttonConect.setFocusable(false);
		contentPane.add(buttonConect);
		
		btnCrearNave = new JButton("CREAR NAVE");
		btnCrearNave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearNave();
			}
		});
		btnCrearNave.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCrearNave.setBounds(556, 459, 174, 48);
		contentPane.add(btnCrearNave);
		
		btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salir();
			}
		});
		btnSalir.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSalir.setBounds(641, 510, 134, 43);
		btnSalir.setFocusable(false);
		contentPane.add(btnSalir);
		
		activarCreacion(true);
		activarConexion(false);
		btnSalir.setEnabled(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				juego.salir();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);	
			}
		});
	
	}
}
