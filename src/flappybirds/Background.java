package flappybirds;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {
	
	private BufferedImage background;
	
	public Background()
	{
		try {
			background = ImageIO.read(Background.class.getResource("/res/background.png"));
		} catch (IOException e) {}
	}
	
	public void Paint(Graphics2D g2d)
	{
		g2d.drawImage(background, 0, 0, FlappyBirds.MASTER_WIDTH, FlappyBirds.MASTER_HEIGHT, null);
	}

}
