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
	
	private boolean visual = true;
	private Controller controller;
	
	public PaintProgram(boolean visual) {
		super();
		this.visual = visual;
	}

	public BufferedImage paintPicture(Painter painter, int paintTime){
		int time = 0;
		while(time++ < paintTime){
			double[] in = new double[2];
			in[0] = controller.getPos().getX();
			in[1] = controller.getPos().getY();
			double[] out = painter.getOutput(in);
			controller.getMove().setX(out[0]);
			controller.getMove().setY(out[1]);
			//controller.moveAndPaint(g);	// Call with graphics object!
		}
		
		// Create image from graphics object
		
		return randomImage();
		
	}

	/**
	 * Just for testing.
	 * @return
	 */
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
