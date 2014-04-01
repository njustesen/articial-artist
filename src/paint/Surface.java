package paint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



class Surface extends JPanel {
	
	private BufferedImage image;
	private Graphics2D g;
	
    public Surface(int width, int height) {
		super();
		this.setPreferredSize(new Dimension(width, height));
		this.image = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );
		g = this.image.createGraphics();
		g.setColor( Color.white );
		g.fillRect(0, 0, width, height);
		//g.dispose();
	}
    
	public void drawLine(double x1, double y1, double x2, double y2, Color color, double brushSize) {
		g.setColor(color);
		g.setStroke(new BasicStroke((float) brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
		repaint();
	}

	@Override
	public void repaint(){
		if (image!=null){
			/*
			this.getGraphics().setColor(Color.white);
			this.getGraphics().fillRect(0, 0, this.getWidth(), this.getHeight());
			((Graphics2D) this.getGraphics()).setBackground(Color.white);
			*/
			this.getGraphics().drawImage(image, 0, 0, null);
		}
	}

	public BufferedImage getImage() {

		return image;
	    
	}
}