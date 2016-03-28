import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.json.JSONObject;


public class GameUIClient extends JPanel implements Runnable {
	private BombPanel bombField[];
	private JPanel bombGrid;
	boolean myTurn;
	JSONObject exString;
	int maxMine;
	Player player;
	boolean isConnected;
	
//	ObjectOutputStream out;
//	ObjectInputStream in;
	PrintWriter out;
	BufferedReader in;
	Socket con;
	Thread outputThread;
	
	 
	
	public GameUIClient() throws IOException{
		super();
		isConnected = false;
		setGUI();
	
		
	}
	public void start(){
		try{
			con = new Socket("127.0.0.1",1256);
			in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
			out = new PrintWriter(con.getOutputStream(),true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error during initial connection");
			System.exit(1);
		}
		isConnected = true;
		outputThread = new Thread(this);
		outputThread.start();
	
	}
	private void setGUI(){
		
		this.setLayout(new BorderLayout());
		createBombGrid();
		add(bombGrid,BorderLayout.CENTER);
		JButton reset =new JButton("RESET");
		reset.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				bombGrid.setVisible(false);
				createBombGrid();
				add(bombGrid,BorderLayout.CENTER);
				
			}	
			
		});
		add(reset,BorderLayout.EAST);
		this.setVisible(true);
	}
	private void createNewGridPanel(){
		bombGrid = new JPanel();
		bombGrid.setLayout(new GridLayout(6,6));
	}
	private void createBombGrid(){
		createNewGridPanel();
		resetBombGrid(11);
		for(BombPanel panel:bombField){
			bombGrid.add(panel);
		}
		
	}
	private void resetBombGrid(int mine){
		int count = 0;
		bombField = new BombPanel[36];
		for(int i = 0; i<36 ;i++){
			bombField[i] = new BombPanel();
			bombField[i].setButtonListener(new BombListener(this,i));
			if(count == mine) {
				bombField[i].setBomb(false); continue;
			}
			if(bombField[i].checkBomb()) count++;
		}
		if(count < mine){
			resetBombGrid(mine);
		}
		if(isConnected)this.sendSameBombGrid();
		
	}
	private void setBombGrid(BombPanel grid[]){
		createNewGridPanel();
		for(int i = 0;i<this.bombField.length;i++){
			//bombField[i] = grid[i];
			
			bombGrid.add(bombField[i]);
		}
		this.add(bombGrid,BorderLayout.CENTER);
	}
	
	

	public void processBombGrid(int index){
		this.bombField[index].clickButton();
		 myTurn = true;
	}
	public void sendCurrentBombGrid(int index){
//		if(myTurn){
			out.println(index);
			System.out.println(index);
			myTurn = false;
		
//		}
	}
	private void sendSameBombGrid(){
		String bombIndex = "F";
		for(int i=0; i<this.bombField.length;i++){
			if(bombField[i].checkBomb()){
				bombIndex = bombIndex + i + " ";
			}
		}
		out.println(bombIndex);
	}
	private void setReceiveField(String indexString) {
		// TODO Auto-generated method stub
		bombGrid.setVisible(false);
		ArrayList<Integer> bomb = new ArrayList<Integer>();
		String temp = indexString.substring(1,indexString.length());
		for(int i =0; i<11;i++){
			if(temp.indexOf(" ") == -1) break;
			bomb.add(Integer.parseInt(temp.substring(0, temp.indexOf(" "))));
			temp = temp.substring(temp.indexOf(" ")+1);
		}
		for(int i =0;i<this.bombField.length;i++){
			bombField[i].setBomb(false);
		}
		for(int i =0;i < bomb.size();i++){
			bombField[bomb.get(i)].setBomb(true);
		}
		this.setBombGrid(this.bombField);
		
	}
	public void run() {

		// TODO Auto-generated method stub
		boolean stillOn = true;
		while(stillOn){
			try {
			
				String indexString = in.readLine();
				System.out.println(indexString);
				if(indexString.equals("Start")){
					sendSameBombGrid();
				} else if (indexString.startsWith("Waiting")){
					System.out.println("Wait for grid");
				} else if(indexString.startsWith("F")){
					setReceiveField(indexString);
				} else {
					
					int index = Integer.parseInt(indexString);
					System.out.println(index);
					//myTurn = true;
					processBombGrid(index);
				}
				
				
				
	        } catch (IOException e) {
	            System.err.println("Problem with Communication Server in Client");
	            stillOn = false;
	        }
        }
		
	}	
	
	
	
	
	
	public static void main(String [] args) throws IOException{
		JFrame frame = new JFrame();
		GameUIClient s = new GameUIClient();
		frame.add(s);
		frame.pack();
		frame.setSize(new Dimension(400,400)); frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.start();
		
	}
	
	
}


