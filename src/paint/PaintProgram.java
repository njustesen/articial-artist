package paint;

import jNeatCommon.EnvConstant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.Visibility;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import neat.Painter;

public class PaintProgram extends JPanel{
	
	private boolean visual = true;
	private Controller controller;
	private Surface surface;
	private JFrame frame;
	private int imgWidth;
	private int imgHeight;
	private Color color;
	private double brushSize;
	private double lifted;
	private int maxBrushSize;
	
	public PaintProgram(boolean visual, int imgWidth, int imgHeight) {
		super();
		this.visual = visual;
		this.controller = new Controller(new Vector2D(20, 20), new Vector2D(0, 0));
		this.surface = new Surface(imgWidth, imgHeight);
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.color = Color.white;
		this.brushSize = 1;
		this.maxBrushSize = imgWidth / 16;
	}

	public BufferedImage paintPicture(Painter painter, int paintTime){
		if (visual) {
			
			frame = new JFrame();
			//frame.setSize(1000,700);
			frame.setPreferredSize(new Dimension(imgWidth,imgHeight));
			frame.setTitle("Picture");
			frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
			
			this.surface = new Surface(imgWidth, imgHeight);
			
			frame.getContentPane().add(surface);  
			frame.setVisible(true);
			frame.pack();
			
		}
		this.controller = new Controller(new Vector2D(imgWidth/2, imgHeight/2), new Vector2D(0, 0));
		
		int time = 0;
		while(time++ < paintTime){
			// Old position
			int xFrom = (int) controller.getPos().getX();
			int yFrom = (int) controller.getPos().getY();
			
			// Input
			double[] in = new double[10]; // Remember to set number of inputs
			in[0] = downscale(controller.getPos().getX(), imgWidth);
			in[1] = downscale(controller.getPos().getY(), imgHeight);
			in[2] = controller.getMove().getX();
			in[3] = controller.getMove().getY();
			in[4] = (double)time / (double)paintTime;
			in[5] = downscale(brushSize, maxBrushSize);
			in[6] = downscale(color.getAlpha(),255);
			in[7] = downscale(color.getRed(),255);
			in[8] = downscale(color.getGreen(),255);
			in[9] = downscale(color.getBlue(),255);
			
			
			// Get output
			double[] out = painter.getOutput(in);
			
			// Extract and scale output
			double moveX = controller.getMove().getX() + scaleNegative(out[0]);
			double moveY = controller.getMove().getY() + scaleNegative(out[0]);
			int red = (int)upscale(out[2], 255);
			int green = (int)upscale(out[3], 255);
			int blue = (int)upscale(out[4], 255);
			int alpha = (int)upscale(out[5], 255);
			color = new Color(red, green, blue, alpha);
			brushSize = upscale(out[6], maxBrushSize);
			boolean randomRelocate = bool(out[7]);
			
			// Update
			controller.getMove().setX(moveX);
			controller.getMove().setY(moveY);
			controller.move(0, 0, imgWidth, imgHeight);
			//surface.drawLine(xFrom, yFrom, controller.getPos().getX(), controller.getPos().getY(), color, brushSize);
			surface.drawArc(xFrom, yFrom, controller.getPos().getX(), controller.getPos().getY(), 10, 10, color, brushSize);
			if (controller.getPos().getX() < 0 || controller.getPos().getX() >= imgWidth || 
					controller.getPos().getY() >= imgHeight || controller.getPos().getY() < 0){
				
				if (randomRelocate){
					controller.getPos().setX(Math.random()*imgWidth);
					controller.getPos().setY(Math.random()*imgHeight);	
				} else {
					controller.getMove().setX(moveX*(-1));
					controller.getMove().setY(moveY*(-1));
				}
			}
			
			if (!randomRelocate){
				controller.getPos().setX(Math.min(controller.getPos().getX(), imgWidth-1));
				controller.getPos().setY(Math.min(controller.getPos().getY(), imgHeight-1));
				controller.getPos().setX(Math.max(controller.getPos().getX(), 0));
				controller.getPos().setY(Math.max(controller.getPos().getY(), 0));
			}
			
		}
		if (visual){
			frame.setVisible(false);
		}
		
		return surface.getImage();
		
	}
	
	private boolean bool(double x) {
		if (x > 0.5)
			return true;
		return false;
	}

	private double bool10(double x) {
		if (x > 0.5)
			return 1.0;
		return 0.0;
	}

	private double scaleNegative(double x) {
		return (-1) + (x * 2);
	}

	private double upscale(double x, int scale) {
		
		return x * (double)scale;
		
	}

	private double downscale(double x, int scale) {
		
		return x / (double)scale;
		
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
