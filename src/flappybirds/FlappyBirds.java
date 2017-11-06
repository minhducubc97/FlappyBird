package flappybirds;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import pkg2dgamesframework.AFrameOnImage;
import pkg2dgamesframework.Animation;
import pkg2dgamesframework.GameScreen;

public class FlappyBirds extends GameScreen {
	
	private BufferedImage birds; // BufferedImage is a special type used for image
	private Animation bird_anim;
	
	public static float g = 0.3f; // acceleration
	
	private Bird bird;
	private Ground ground;
	private ChimneyGroup chimneygroup;
	private Background background;
	
	private int Score = 0;
	private static int HighScore;
	
	private int BEGIN_SCREEN = 0;
	private int GAMEPLAY_SCREEN = 1;
	private int GAMEOVER_SCREEN = 2;
	
	private int CurrentScreen = BEGIN_SCREEN;
	
	public static ArrayList<User> users; // data structure: arraylist to save data
	public boolean ask = true; // to open the dialog update data only once
	
	public FlappyBirds()
	{
		super(800,600);
		
		users = new ArrayList<User>(); // initialize ArrayList
		ReadData(); // read the old data
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				UpdateData(); // when closing the window => update data
			}
		});
		
		try {
			birds = ImageIO.read(FlappyBirds.class.getResource("/res/bird_sprite.png"));
		} catch (IOException e) {}
		
		bird_anim = new Animation(50);
		AFrameOnImage f;
		
		f = new AFrameOnImage(0,0,60,60);
		bird_anim.AddFrame(f);
		f = new AFrameOnImage(60,0,60,60);
		bird_anim.AddFrame(f);
		f = new AFrameOnImage(120,0,60,60);
		bird_anim.AddFrame(f);
		f = new AFrameOnImage(60,0,60,60);
		bird_anim.AddFrame(f);
		
		bird = new Bird (350,250,50,50);
		ground = new Ground ();
		chimneygroup = new ChimneyGroup ();
		background = new Background();
		
		BeginGame();
	}
	
	public static void main (String [] args)
	{
		new FlappyBirds();
	}
	
	private void resetGame()
	{
		bird.setPos(350, 250);
		bird.setSpeed(0);
		bird.setLife(true);
		Score = 0;
		chimneygroup.reset_chimneys();
		ask = true;
	}
	
	public static void UpdateData() // update data into data.txt
	{
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter("data/data.txt");
			bw = new BufferedWriter(fw);
			
			for (User u : users)
			{
				bw.write(u.getName() + " " + u.getScore());
				bw.newLine(); // next line
			}
		} catch (IOException e) {}
		finally
		{
			try {
				bw.close();
			} catch (IOException e) {}
		}
	}
	
	public static void ReadData()
	{
		HighScore = 0;
		boolean firsttime = true;
		FileReader fr;
		try {
			fr = new FileReader("data/data.txt");
			BufferedReader br = new BufferedReader (fr);
			String line = null; // read each line
			while ((line = br.readLine()) != null) // if the line is not null
			{
				String [] str = line.split(" ");
				users.add(new User(str[0], str[1]));
				if (firsttime == true)
				{
					HighScore = Integer.parseInt(str[1]);
					firsttime = false;
				}
			}
			br.close();
		} catch (IOException e) {}
	}
	
	@Override
	public void GAME_UPDATE(long deltaTime) {
		// TODO Auto-generated method stub
		
		if (CurrentScreen == BEGIN_SCREEN)
		{
			resetGame();
		}
		
		else if (CurrentScreen == GAMEPLAY_SCREEN)
		{
			if (bird.getLife())
			{
				bird_anim.Update_Me(deltaTime);
			}
			bird.update_bird(deltaTime);
			ground.update_ground();
			chimneygroup.update_chimneys();
			
			for (int i = 0; i < ChimneyGroup.SIZE; i++) // ChimneyGroup, not chimneygroup because SIZE is static
			{
				if (bird.getRect().intersects(chimneygroup.getChimney(i).getRect())
						|| bird.getPosY() + bird.getH() > ground.getYGround())
				{
					if (bird.getLife()) bird.bupSound.play(); // the condition prevents the sound from keep on playing
					bird.setLife(false);
				}
			}
			
			for (int i = 0; i < ChimneyGroup.SIZE; i++)
			{
				if ((bird.getPosX() > chimneygroup.getChimney(i).getPosX()) 
						&& !chimneygroup.getChimney(i).getIsBehindBird()
						&& i%2==0)
				// second clause to prevent the score keep adding from previous chimney
				{
					Score++;
					bird.getMoneySound.play();
					chimneygroup.getChimney(i).setIsBehindBird(true);
				}
				
			}
	
			if (bird.getPosY() + bird.getH() > ground.getYGround() + 150)
			{
				CurrentScreen = GAMEOVER_SCREEN;
			}
		}
		
		else if (CurrentScreen == GAMEOVER_SCREEN) 
		{
			if (ask == true)
			{
				ask = false;
				String name = JOptionPane.showInputDialog("Please enter your name: ");
				if (Score > HighScore)
				{
					users.add(0, new User(name, String.valueOf(Score)));
					HighScore = Score;
				}
				else if (Score <= HighScore)
				{
					FlappyBirds.users.add(new User(name, String.valueOf(Score)));
				}
			}
		}
	}
 
	@Override
	public void GAME_PAINT(Graphics2D g2) {
		// TODO Auto-generated method stub
//		g2.setColor(Color.decode("#b8daef"));
//		g2.fillRect(0, 0, this.MASTER_WIDTH, this.MASTER_HEIGHT);

		background.Paint(g2);
		chimneygroup.Paint(g2);;
		ground.Paint(g2);
		
		if (bird.getIsFlying())
		{
			bird_anim.PaintAnims((int)bird.getPosX(), (int)bird.getPosY(), birds, g2, 0, -1);
		}
		else
		{
			bird_anim.PaintAnims((int)bird.getPosX(), (int)bird.getPosY(), birds, g2, 0, 0);
		}
		
		
		if (CurrentScreen == BEGIN_SCREEN)
		{
			g2.setColor(Color.RED);
			g2.setFont(new Font ("Courier",Font.BOLD,22));
			g2.drawString("Press space to play game", 250, 350);
		}
		
		if (CurrentScreen == GAMEOVER_SCREEN)
		{
			g2.setColor(Color.RED);
			g2.setFont(new Font ("Courier",Font.BOLD,22));
			g2.drawString("Press space to play again", 250, 350);
		}
		
		g2.setColor(Color.RED);
		g2.setFont(new Font ("Courier",Font.BOLD,22));
		g2.drawString("SCORE: " + Score, 24, 30);
		
		g2.setColor(Color.YELLOW);
		g2.drawString("HIGH SCORE: "+users.get(0).toString(), 24, 50);
	}

	@Override
	public void KEY_ACTION(KeyEvent e, int Event) {
		// TODO Auto-generated method stub
		if (Event == KEY_PRESSED)
		{
			if (CurrentScreen == BEGIN_SCREEN)
			{
				CurrentScreen = GAMEPLAY_SCREEN;
			}
			
			else if (CurrentScreen == GAMEPLAY_SCREEN)
			{
				if (bird.getLife())
				{
					bird.fly();
				}
			}
			
			else if (CurrentScreen == GAMEOVER_SCREEN) 
			{
				CurrentScreen = BEGIN_SCREEN;
			}
		}
	}

}
