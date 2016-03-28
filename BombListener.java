import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class BombListener extends MouseAdapter {
	private GameUIClient game;
	private int panel;
	
	public BombListener(GameUIClient g, int p){
		this.game = g;
		this.panel = p;
	}
	public void mouseReleased(MouseEvent e){
		System.out.println("block"+panel);
		game.sendCurrentBombGrid(panel);
		game.processBombGrid(panel);
	}
}
