package neat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputListener;

public class EvolutionPanel extends JPanel implements MouseInputListener {

	List<BufferedImage> pictures;
	List<BufferedImage> selected;
	int shiftSelected = -1;
	JScrollPane scrollPane;
	int width;
	int height;
	int top = 0;
    int space = 10;
    int border = 4;
	
	public EvolutionPanel() {
		super();
		this.pictures = new ArrayList<BufferedImage>();
		this.selected = new ArrayList<BufferedImage>();
		JFrame f = new JFrame();
		this.width = 1300;
		this.height = 700;
        f.setSize(width, height);
        f.setPreferredSize(new Dimension(width, height));
        f.setTitle("Evolution Panel");
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(this);   
        f.addMouseListener(this);
        f.pack();
        f.setVisible(true);
	}

	public void clearPictures() {
		pictures.clear();
		selected.clear();
		//scrollPane.removeAll();
	}

	public void addPictures(List<BufferedImage> pics) {
		this.pictures.addAll(pics);
		/*
		for(BufferedImage image : pictures){
			PictureComponent pic = new PictureComponent(image);
			//scrollPane.add(pic);
			this.add(pic);
		}
		//scrollPane.add(comp);
		*/ 
		repaint();
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, width, height);
        int x = space;
        int y = top + space;
        for(BufferedImage picture : pictures){
        	if (x >= this.getSize().width-picture.getWidth()+space){
        		x = space;
        		y += space + picture.getHeight(); 
        	}
        	
        	if(selected.contains(picture)){
        		g.setColor(Color.GREEN);
        		//g.drawRect(x-border, y-border, picture.getWidth()+border*2, picture.getHeight()+border*2);
        		g.fillRect(x-border, y-border, picture.getWidth()+border*2, picture.getHeight()+border*2);
        	}
        	g.drawImage(picture, x, y, picture.getWidth(), picture.getHeight(), null);
        	x += space + picture.getWidth();
        	
        	y += 0;
        }
        
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		
		if (pictures.isEmpty())
			return;
		
		int w = pictures.get(0).getWidth();
		int h = pictures.get(0).getHeight();
		
		int nX = (x/(w+space));
		int nY = ((y-top)/(h+space));
		int perLine = width / (w+space);
		int n = nY * perLine + nX;
		
		if (pictures.size() < n)
			return;
		
		if (arg0.isShiftDown()) {
			shiftSelected = n;
		}
		
		else {
			if (selected.contains(pictures.get(n)))
				selected.remove(pictures.get(n));
			else
				selected.add(pictures.get(n));
		}				
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public List<BufferedImage> getSelected() {
		return selected;
	}

	public void setSelected(List<BufferedImage> selected) {
		this.selected = selected;
	}

	public List<BufferedImage> getPictures() {
		return pictures;
	}

	public void setPictures(List<BufferedImage> pictures) {
		this.pictures = pictures;
	}
	
}
