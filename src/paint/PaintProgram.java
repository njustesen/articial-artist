package paint;

import jNeatCommon.EnvConstant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.Visibility;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
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
	private double liftLimit;
	private double soundLevel;
	static TargetDataLine line;
	static byte[] data;
	
	public PaintProgram(boolean visual, int imgWidth, int imgHeight) {
		super();
		this.visual = visual;
		this.controller = new Controller(new Vector2D(20, 20), new Vector2D(0, 0));
		this.surface = new Surface(imgWidth, imgHeight);
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.color = Color.white;
		this.brushSize = 1;
		this.maxBrushSize = imgWidth / 10;
		this.liftLimit = 0.8;
		if (line==null)
			setupSoundInput();
	}

	private void setupSoundInput() {
		// TODO Auto-generated method stub
		AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, true);
	
		line = null;
	    DataLine.Info info = new DataLine.Info(TargetDataLine.class,
	            format); 
	    if (!AudioSystem.isLineSupported(info)) {
	
	    }
	
	    try {
	        line = (TargetDataLine) AudioSystem.getLine(info);
	        line.open(format);
	    } catch (LineUnavailableException ex) {
	        // Handle the error ...
	    }
	
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    int numBytesRead;
	    data = new byte[10];
	    
	}

	public BufferedImage paintPicture(Painter painter, int paintTime, boolean autoClose){
		//if (visual) {
		//setupSoundInput();
		line.start();
			
			frame = new JFrame();
			//frame.setSize(1000,700);
			frame.setPreferredSize(new Dimension(imgWidth+50,imgHeight+50));
			frame.setTitle("Picture");
			frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
			
			this.surface = new Surface(imgWidth, imgHeight);
			
			frame.getContentPane().add(surface);  
			frame.setVisible(visual);
			frame.pack();
			
		//}
		this.controller = new Controller(new Vector2D(imgWidth/2, imgHeight/2), new Vector2D(0, 0));
		
		int time = 0;
		while(time++ < paintTime){
			
			// Get sound level
			soundLevel = getSoundLevel();
			//System.out.println(soundLevel);
			
			// Old position
			int xFrom = (int) controller.getPos().getX();
			int yFrom = (int) controller.getPos().getY();
			
			// Input
			double[] in = new double[101]; // Remember to set number of inputs
			in[0] = downscale(controller.getPos().getX(), imgWidth);
			in[1] = downscale(controller.getPos().getY(), imgHeight);
			in[2] = controller.getMove().getX();
			in[3] = controller.getMove().getY();
			in[4] = (double)time / (double)paintTime;
			//in[5] = 0.5;
			//in[4] = 0.5;
			//in[5] = soundLevel;
			//in[4] = 1;
			in[5] = downscale(brushSize, maxBrushSize);
			in[6] = downscale(color.getAlpha(),255);
			in[7] = downscale(color.getRed(),255);
			in[8] = downscale(color.getGreen(),255);
			in[9] = downscale(color.getBlue(),255);
			//in[0] = soundLevel;
			/*
			for(double d : in)
				d = d * soundLevel;
			*/
			
			// Get output
			double[] out = painter.getOutput(in);

			if (time==1){
				
				double reposX = out[8];
				double reposY = out[9];
				controller.getPos().setX(reposX*imgWidth);
				controller.getPos().setY(reposY*imgHeight);	
				xFrom = (int) controller.getPos().getX();
				yFrom = (int) controller.getPos().getY();
			
			} else {

				// Extract and scale output
				double moveX = controller.getMove().getX() + upscale(scaleNegative(scaleTowardsHalf(out[0])),imgWidth/32);
				double moveY = controller.getMove().getY() + upscale(scaleNegative(scaleTowardsHalf(out[1])),imgHeight/32);
				int red = (int)upscale(scaleTowardsHalf(out[2]), 255);
				int green = (int)upscale(scaleTowardsHalf(out[3]), 255);
				int blue = (int)upscale(scaleTowardsHalf(out[4]), 255);
				int alpha = (int)upscale(scaleTowardsHalf(out[5]), 255);
				//alpha = 255;
				//alpha = (int) (soundLevel * 255);
				color = new Color(red, green, blue, alpha);
				brushSize = upscale(scaleTowardsHalf(out[6]), maxBrushSize);
				boolean lift = scaleTowardsHalf(out[7]) > liftLimit;
				double reposX = upscale(scaleTowardsHalf(out[8]), imgWidth);
				double reposY = upscale(scaleTowardsHalf(out[9]), imgHeight);
				//reposX = Math.random();
				//reposY = Math.random();
				
				// Update
				controller.getMove().setX(moveX);
				controller.getMove().setY(moveY);
				controller.move(0, 0, imgWidth, imgHeight);
/*
				if (!lift){
					surface.drawLine(xFrom, yFrom, controller.getPos().getX(), controller.getPos().getY(), color, brushSize);
				}
				//surface.drawArc(xFrom, yFrom, controller.getPos().getX()*0.1, controller.getPos().getY()*0.1, upscale(scaleNegative(reposX),imgWidth/10), upscale(scaleNegative(reposY),imgWidth/10), color, brushSize);
*/
				if (!lift){
					if (out[7] < 0.8)
						surface.drawLine(xFrom, yFrom, controller.getPos().getX(), controller.getPos().getY(), color, brushSize);
					else if (out[7] < 0.9)
						surface.drawRoundRect(xFrom, yFrom, controller.getPos().getX()*0.1, controller.getPos().getY()*0.1, upscale(scaleNegative(reposX),imgWidth/10), upscale(scaleNegative(reposY),imgWidth/10), color, brushSize);
					else
						surface.drawArc(xFrom, yFrom, controller.getPos().getX()*0.1, controller.getPos().getY()*0.1, upscale(scaleNegative(reposX),imgWidth/10), upscale(scaleNegative(reposY),imgWidth/10), color, brushSize);
				}
					
				if (lift || 
						(controller.getPos().getX() < 0 || controller.getPos().getX() >= imgWidth || 
						controller.getPos().getY() >= imgHeight || controller.getPos().getY() < 0)){
					//if (lift){
					controller.getPos().setX(reposX);
					controller.getPos().setY(reposY);	
					//System.out.println(reposX + " " + reposY);
					controller.getMove().setX(upscale(scaleNegative(scaleTowardsHalf(out[0])),3));
					controller.getMove().setY(upscale(scaleNegative(scaleTowardsHalf(out[1])),3));
					//System.out.println(controller.getPos().getX()+ " " + controller.getPos().getY());

				}
			}
			
		}
		
		//surface.printId(painter.getId(), imgWidth/2, imgHeight/10*7);
		
		//if (visual){
		if (autoClose) {
			frame.setVisible(visual);
			frame.dispose();
		}
		//}
		
			line.stop();
			
		return surface.getImage();
		
	}
	
	private double getSoundLevel() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int numBytesRead = line.read(data, 0, data.length);
        //System.out.println("Level: " + line.getLevel());
        //System.out.println(line.get);
        // Save this chunk of data.
        out.write(data, 0, numBytesRead);
        
        double v = 0;
        
        for(int i=0; i<numBytesRead; i+=1) {
            //System.out.println(Byte.toString(data[i]));
            int abs = Math.abs(data[i]);
            double val = (double)abs / (double)128;
            //System.out.println(val);
            v += val;
        }
        
        return v / numBytesRead;
        
	}

	private double scaleTowardsHalf(double value) {
		if (value >= 0.5)
			return value - (value - 0.5)/40;
			//return value;
		else
			return value + (0.5 - value)/40;
			//return value;
	}
	
	private int average(double[] out) {
		// TODO Auto-generated method stub
		return 0;
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
