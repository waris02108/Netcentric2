package p2p;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JPanel implements Runnable {
	static JFrame mainFrame = new JFrame();
	boolean isSever;
	Thread thread;
	static GameUIClient gameController;
	static Player player1;
	static Player player2;
	static MyMineWelcome welcome;
	static GameState currentState = GameState.WELCOME;
	static int mineCount = 0;
	///IP + PORT
	static String ip = "";
	static String port ="";
	public static enum GameState {
		WELCOME,
		//SELECT_UI,
		GAME_PLAYING_SERVER,
		GAME_PLAYING_READY,
		GAME_PLAYING_CLIENT
	}
	public void init(){
		//createSound();
		welcome = new MyMineWelcome();
		
		this.add(welcome);
	}
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	public void update(){
		//currentState = state;
		if(currentState == GameState.WELCOME){
			revalidate();
			add(welcome);
			repaint();
		} else if (currentState == GameState.GAME_PLAYING_SERVER){
			removeAll();
			revalidate();
			try {
				gameController = new GameUIClient(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(gameController);
			//gameController.setServer(true);
			gameController.tempPromptName();
			gameController.start();
			currentState = GameState.GAME_PLAYING_READY;
		} else if(currentState == GameState.GAME_PLAYING_CLIENT){
			removeAll();
			revalidate();
			try {
				gameController = new GameUIClient(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(gameController);
			//gameController.setServer(false);
			gameController.tempPromptName();
			gameController.start();
			currentState = GameState.GAME_PLAYING_READY;
		}
		
	}
	
	public static void main(String[] args) {
		//mainInterface = new MainInterface();
		mainFrame = new JFrame();
		Main main = new Main();
		mainFrame.add(main);
		mainFrame.setSize(1200,1000);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		main.start();
		//mainInterface.setVisible(false);
		//mainInterface
		//main.requestFocus();
		
		//mainInterface.dispose();

	}
	@Override
	public void run() {
		
		init();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (true) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				// update();
				update();
				// repaint();
				updates++;
				delta--;
			}

			// repaint();
			frames++;

			// update();

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}

		}
		
	}
	public void insertBGM(String sound) {
		File soundFile = new File(sound);
		AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip2 = AudioSystem.getClip();
			clip2.open(audioIn);
			
				clip2.start();
				
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
//	public void createSound() {
//		File soundFile1 = new File("sounds/login2.wav");
//		File soundFile2 = new File("sounds/menu.wav");
//		File soundFile3 = new File("sounds/battle.wav");
//		File soundFile4 = new File("sounds/deck.wav");
//		File soundFile5 = new File("sounds/battle2.wav");
//		AudioInputStream audioIn = null;
//		AudioInputStream audioIn2 = null;
//		AudioInputStream audioIn3 = null;
//		AudioInputStream audioIn4 = null;
//		AudioInputStream audioIn5 = null;
//		try {
//			audioIn = AudioSystem.getAudioInputStream(soundFile1);
//			loginClip = AudioSystem.getClip();
//			loginClip.open(audioIn);
//			
//			audioIn2 = AudioSystem.getAudioInputStream(soundFile2);
//			MenuClip = AudioSystem.getClip();
//			MenuClip.open(audioIn2);
//			
//			audioIn3 = AudioSystem.getAudioInputStream(soundFile3);
//			BattleClip = AudioSystem.getClip();
//			BattleClip.open(audioIn3);
//			
//			audioIn4 = AudioSystem.getAudioInputStream(soundFile4);
//			DeckClip = AudioSystem.getClip();
//			DeckClip.open(audioIn4);
//			10.202.241.190
//			audioIn5 = AudioSystem.getAudioInputStream(soundFile5);
//			ChooseClip = AudioSystem.getClip();
//			ChooseClip.open(audioIn5);
//			
//			
//		} catch (UnsupportedAudioFileException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (LineUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
