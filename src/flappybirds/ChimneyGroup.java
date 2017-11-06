package flappybirds;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import pkg2dgamesframework.QueueList;

public class ChimneyGroup {
	
	private QueueList<Chimney> chimneys;
	
	private BufferedImage chimney_img;
	private BufferedImage chimney_img2;
	
	public static int SIZE = 6;
	
	private int topChimneyY = -350;
	private int bottomChimneyY = 220;
	
	public Chimney getChimney(int i)
	{
		return chimneys.get(i);
	}
	
	public int getRandomY()
	{
		Random random = new Random(); // initialize random 
		int a; 
		a = random.nextInt(10);
		
		return a*25 ;
	}
	
	public ChimneyGroup()
	{
		try {
			chimney_img = ImageIO.read(ChimneyGroup.class.getResource("/res/chimney.png"));
			chimney_img2 = ImageIO.read(ChimneyGroup.class.getResource("/res/chimney_.png"));
		} catch (IOException e) {}
		
		chimneys = new QueueList<Chimney>();
		
		Chimney cn;
		
		for (int i = 0; i < SIZE/2; i++) // 3 pairs of chimneys
		{
			int deltaY = getRandomY();
			
			cn = new Chimney(830 + i*300,bottomChimneyY+deltaY,74,400);
			chimneys.push(cn);
			
			cn = new Chimney(830 + i*300,topChimneyY+deltaY,74,400);
			chimneys.push(cn);
		}
	}
	
	public void reset_chimneys()
	{
		chimneys = new QueueList<Chimney>();

		Chimney cn;

		for (int i = 0; i < SIZE/2; i++) // 3 pairs of chimneys
		{
			int deltaY = getRandomY();
			
			cn = new Chimney(830 + i*300,bottomChimneyY+deltaY,74,400);
			chimneys.push(cn);

			cn = new Chimney(830 + i*300,topChimneyY+deltaY,74,400);
			chimneys.push(cn);
		}
	}
	
	public void update_chimneys()
	{
		for (int i = 0; i < SIZE; i++)
		{
			chimneys.get(i).update_chimney(); // get the chimney moving
		}
		
		for (int i = 0; i < SIZE; i++)
		{
			if (chimneys.get(0).getPosX() < -74) // place chimney in the back
			{
				int deltaY = getRandomY();
				
				Chimney cn;
				cn = chimneys.pop();
				cn.setPosX(chimneys.get(4).getPosX() + 300);
				cn.setPosY(bottomChimneyY+deltaY);
				cn.setIsBehindBird(false); //!!! prevent adding score confusion
				chimneys.push(cn);
				
				cn = chimneys.pop();
				cn.setPosX(chimneys.get(4).getPosX());
				cn.setPosY(topChimneyY+deltaY);
				cn.setIsBehindBird(false); //!!! 
				chimneys.push(cn);
			}
		}
	}
	
	public void Paint(Graphics2D g2d)
	{
		for (int i = 0; i < SIZE; i++)
		{
			if (i%2 == 0)
			{
				g2d.drawImage(chimney_img, (int)chimneys.get(i).getPosX(), (int)chimneys.get(i).getPosY(), null);
			}
			else
			{
				g2d.drawImage(chimney_img2, (int)chimneys.get(i).getPosX(), (int)chimneys.get(i).getPosY(), null);
			}
		}
	}
}
