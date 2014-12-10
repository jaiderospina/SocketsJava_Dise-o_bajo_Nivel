package ar.edu.uns.cs.tdp.sockets.juego.client;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;

import javax.swing.ImageIcon;

import ar.edu.uns.cs.tdp.sockets.juego.Nave;

/**
 * Clase heredada de JPanel, que dibuja cada 50ms todas las naves del juego y posee el oyente de las teclas
 * 
 * @author Diego Schwindt
 *
 */

@SuppressWarnings("serial")
public class GUIJuego extends JPanel implements ActionListener {
	
	protected Timer timer;  //timer usado para repintar el panel
	
	protected LogicaCliente juego;
	
	public static final String NAVE_ROJA = "img/nave_roja.png";
	public static final String NAVE_MARRON = "img/nave_marron.png";
	public static final String NAVE_VERDE = "img/nave_verde.png";
	public static final String NAVE_VIOLETA = "img/nave_violeta.png";
	private ImageIcon naveRoja, naveVerde, naveMarron,naveVioleta; 
	
	/**
	 * Create the panel.
	 */
	public GUIJuego(LogicaCliente juego) {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				moverJugador(arg0);
			}
		});
		
		this.juego = juego;
		setLayout(null);
		this.setFocusable(true);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(GUIJuego.class.getResource("img/background.jpg")));
		label.setBounds(0, 0, 760, 450);
		add(label);
		
		// Creo los ImageIcon para acceder una sola vez a los archivos
		naveRoja = new ImageIcon(getClass().getResource(NAVE_ROJA));
		naveMarron = new ImageIcon(getClass().getResource(NAVE_MARRON));
		naveVerde = new ImageIcon(getClass().getResource(NAVE_VERDE));
		naveVioleta = new ImageIcon(getClass().getResource(NAVE_VIOLETA));
		
		timer = new Timer(50, this);  //se actualiza cada 50ms
        timer.start();	

	}
	
	/*
	 * Este método se llama cada 50ms (por el timer)
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		repaint(); // Invoca a paint  
	}
	
	/*
	 * Método invocado por repaint()
	 * Pide la lista de naves la Logica y las dibuja en la posicion indicada, con la imagen adecuada 
	 */
	public void paint(Graphics g) {
		// llamo el paint de la clase padre (JPanel)
		super.paint(g);
		
		// Seteo color a usar para los strings
		g.setColor(Color.WHITE);

		// Recorro lista de naves enemigas y las dibujo con la imagen que corresponda
		for (Nave nav : juego.getEnemigos()) {
			g.drawImage(selectImageNave(nav.getColor()),nav.getX(),nav.getY(),this);
			g.drawString(nav.getJugador(), nav.getX(), nav.getY()+60);
		}
		// Dibujo mi nave	
		if(juego.getPlayer() != null) {
			Nave nav = juego.getPlayer();
			g.drawImage(selectImageNave(nav.getColor()),nav.getX(),nav.getY(),this);
			g.drawString(nav.getJugador(), nav.getX(), nav.getY()+60);
		}
		
	}
	
	private Image selectImageNave(int n) {
		switch (n) {
			case Nave.COLOR_ROJO:
				return naveRoja.getImage();
			case Nave.COLOR_MARRON:
				return naveMarron.getImage();
			case Nave.COLOR_VERDE:
				return naveVerde.getImage();
			case Nave.COLOR_VIOLETA:
				return naveVioleta.getImage();
	
			default:
				return null;
		}
	}
	
	private void moverJugador(KeyEvent k) {
		int key = k.getKeyCode();
		juego.moverPlayer(key == KeyEvent.VK_UP, key == KeyEvent.VK_DOWN, key == KeyEvent.VK_LEFT, key == KeyEvent.VK_RIGHT);
	//	System.out.println(key);
	}
	

	

}
