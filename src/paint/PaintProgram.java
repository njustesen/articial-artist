package paint;

import jNeatCommon.EnvConstant;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import neat.Painter;

public class PaintProgram extends JPanel{
	
	private boolean visual = false;
	
	public PaintProgram(boolean visual) {
		super();
		this.visual = visual;
	}

	public BufferedImage paintPicture(Painter painter, int paintTime){
		
		return randomImage();
		
	}

	private BufferedImage randomImage() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(EnvConstant.JNEAT_DIR + EnvConstant.OS_FILE_SEP + "testimage.jpg"));
		} catch (IOException e) {
		}
		/*
		Graphics2D gfx = (Graphics2D) image.getGraphics();
		gfx.setBackground(Color.BLUE);
		gfx.setColor(Color.RED);
		gfx.drawOval(20, 20, 20, 20);
		gfx.drawImage(image, null, null);
		*/
		return image;
	}
	
}
