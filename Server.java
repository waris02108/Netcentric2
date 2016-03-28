import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Server extends JPanel{
	static ArrayList<PrintWriter> clientList;
	static ArrayList<Integer> clientID;
	static final int POOL_SIZE = 10;
	static Executor execs = Executors.newFixedThreadPool(POOL_SIZE);
	static int numClients = 0;
	static String inputLine;
	static JTextArea showUser;
	
	 static ServerSocket server;
	private ObjectInputStream in;
	private Socket conn;
	private String host;
	private int port;
	private ClientThread[] client;
	private Thread serverThread;
	public Server() throws IOException{
		setGUI();
		InetAddress addrhost = InetAddress.getByName("127.0.0.1");;
		server = new ServerSocket(1256);
		
		/*InetAddress addrhost;
		client = new ClientThread[2];
		try {
			addrhost = InetAddress.getByName("127.0.0.1");
			server = new ServerSocket(5000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//server = new ServerSocket(port,host);
	}
	public void setGUI(){
		showUser = new JTextArea();
		showUser.setPreferredSize(new Dimension(300,300));
		JScrollPane scroll = new JScrollPane(showUser);
		this.add(scroll);
		this.setVisible(true);
	}
	public void startPlay() throws IOException{
		while(true){
			System.out.println("Waiting for a Client ...");
			Socket con = server.accept();
		//	System.out.println(clientList.toString());
			numClients++;
			Runnable r = new TestRunnable(numClients,con);
			execs.execute(r);
		}
		
	}
	public static void main(String[] args) throws IOException{
		clientList = new ArrayList<PrintWriter>();
		clientID = new ArrayList<Integer>();
		JFrame mainFrame =  new JFrame();
		Server s = new Server();
		mainFrame.add(s);
		mainFrame.setSize(new Dimension(400,400));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		s.startPlay();
		
		
	}
	//server for each one to handle dataflow
	class TestRunnable implements Runnable{
		int id;
		Socket con;
		
		final int NUM_REPEAT = 5;
		public TestRunnable(int id,Socket con){
			this.id = id;
			this.con = con;
			System.out.println("Client #"+id+" is connected.");
			Server.clientID.add(id);
		}
		
		public String echo(String msg){
			return "Client #"+id+": "+msg;
		}
		
		public void broadcast(String msg){
	    	for(PrintWriter p:Server.clientList){
	    		p.println(msg);
	    	}
		}
		public void randomTurn(){
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean stillOn = true;
			
			while(stillOn){
				try {
		            PrintWriter out = new PrintWriter(con.getOutputStream(),true);
		            Server.clientList.add(out);
		    //        System.out.println(TestServer.clientList);
		            //broadcast("Client #"+id+" is connected.");
		          
		            if(Server.numClients <2){
		            	out.println("Waiting for Another Player");
		            } 
		            if (Server.numClients == 2){
		            	out.println("Start");
		            	
		            }
		           
		            Server.showUser.append("Client #"+id+" is connected.\n");
		            BufferedReader in = new BufferedReader(
		                    new InputStreamReader(con.getInputStream()));
		            
	            while ((Server.inputLine = in.readLine()) != null) {
	            	if(Server.inputLine.startsWith("F")){
	            		broadcast(Server.inputLine);
	            	} else {
		              Server.showUser.append("Client #"+id+": "+Server.inputLine+"\n");
		              broadcast(Server.inputLine);
	            	} 
		         }
				System.out.println("Client#"+id+" has left.");
		            out.close();
		            in.close();
		            con.close();
		        } catch (IOException e) {
		            System.err.println("Problem with Communication Server");
		            stillOn = false;
		        }
	        }
			System.out.println("Client #"+id+" has lost its connection.");
		//	System.out.println(id+" INT: "+TestServer.clientID.toString());
			for(int i=0;true;i++){
				if(Server.clientID.get(i) == id){
					Server.clientList.remove(i);
					Server.clientID.remove(i);
					broadcast("Client #"+id+" has left.");
					return;
				}
			}
		}	
	}

	
}
