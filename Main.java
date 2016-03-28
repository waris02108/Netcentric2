import javax.swing.JFrame;


public class Main {
	static JFrame mainframe = new JFrame();
	boolean isSever;
	Thread thread;
	Player player1;
	Player player2;
	MyMineWelcome welcome;
	GameState currentState;
	public static enum GameState {
		WELCOME,
		SELECT_UI,
		GAME_LOBBY,
		GAME_PLAYING,
		END_GAME
	}
	public void init(){
		//createSound();
		//fade = new fade(); *****
	}
	public void update(GameState state){
		currentState = state;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
