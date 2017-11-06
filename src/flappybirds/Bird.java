package flappybirds;

import java.awt.Rectangle;
import java.io.File;

import pkg2dgamesframework.Objects;
import pkg2dgamesframework.SoundPlayer;

public class Bird extends Objects{
	
	private float speed = 0;
	
	private boolean isFlying = false;
	
	private Rectangle rect; // helps to check collision
	
	private boolean isAlive = true;
	
	public SoundPlayer flapSound, bupSound, getMoneySound;
	
	public Bird (int x, int y, int w, int h)
	{
		super(x,y,w,h);
		rect = new Rectangle(x,y,w,h);
		
		flapSound = new SoundPlayer(new File("sound/fap.wav"));
		bupSound = new SoundPlayer(new File ("sound/fall.wav"));
		getMoneySound = new SoundPlayer(new File ("sound/getpoint.wav"));
	}
	
	public void setLife (boolean b)
	{
		isAlive = b;
	}
	
	public boolean getLife ()
	{
		return isAlive;
	}
	
	public Rectangle getRect()
	{
		return rect;
	}
	
	public void update_bird (long deltaTime)
	{
		speed += FlappyBirds.g;
		
		this.setPosY(this.getPosY() + speed);
		this.rect.setLocation((int) this.getPosX(),(int) this.getPosY()); // necessary to update bird position
		
		if (speed < 0)
		{
			isFlying = true;
		}
		else
		{
			isFlying = false;
		}
	}
	
	public void fly()
	{
		speed = -7; 
		flapSound.play();
	}
	
	public boolean getIsFlying()
	{
		return isFlying;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
}
