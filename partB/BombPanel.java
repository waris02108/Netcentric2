package partB;
import java.net.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class BombPanel extends JPanel{
	private boolean isBomb;
	private BufferedImage setImage = null;
	private BufferedImage bombImg;
	private BufferedImage freeImg;
	private JLabel showImage;
	private JButton button;
	public BombPanel(){
		super();
		isBomb = false;
		try {
			bombImg = ImageIO.read(new File("bomb.gif"));
			freeImg = ImageIO.read(new File("free.gif"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		button = new JButton();
		Border thickBorder = new LineBorder(Color.WHITE, 1);
		button.setBorder(thickBorder);
		button.setPreferredSize(new Dimension(150,150));
		button.addActionListener(new ActionListener(){
	

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				button.setVisible(false);
				showImage.setVisible(true);
				System.out.println((isBomb? "bomb": "free" ) + " Score:" + getScore());
			}
		});
		this.add(button);
		this.randomBomb();
		
	}
	public void setBomb(boolean bomb){
		this.isBomb = bomb;
		this.resetShowImage();
	}
	public void resetShowImage(){
		if(isBomb)setImage = bombImg;
		else setImage = freeImg;
		showImage.setIcon(new ImageIcon(setImage));
		
	
	}
	public void randomBomb(){
		isBomb = (int)(Math.random()*1.55555)== 1? true:false ;
		if(isBomb)setImage = bombImg;
		else setImage = freeImg;
		//repaint();
		showImage = new JLabel( new ImageIcon(setImage));
		showImage.setPreferredSize(new Dimension(150,150));
		this.add(showImage);
		showImage.setVisible(false);
	}
	public boolean checkBomb(){
		return isBomb;
	}
	/*protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(setImage,0,0,null);
	}*/
	public int getScore(){
		return isBomb == true? 1:0 ;
	}
	

	
}
