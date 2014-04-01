package paint;

import java.awt.Graphics2D;


public class Controller {

	private Vector2D pos;
	private Vector2D move;
	
	public Controller(Vector2D pos, Vector2D move) {
		super();
		this.pos = pos;
		this.move = move;
	}
	
	public void moveAndPaint(Graphics2D g) {
		double newX = pos.getX() + move.getX();
		double newY = pos.getY() + move.getY();
		Vector2D newPos = new Vector2D(newX, newY);
		
		//Draw stuff
		g.drawLine((int) pos.getX(), (int) pos.getY(), (int) newX, (int) newY);
		
		pos.setX(newX);
		pos.setY(newY);
	}
	

	public void move(int i, int j, double imgWidth, double imgHeight) {
		double newX = pos.getX() + move.getX();
		double newY = pos.getY() + move.getY();

		//newX = pos.getX() + move.getX();
		//newY = pos.getY() + move.getY();
		
		//System.out.println(newX + " : " + newY);
		
//		newX = Math.min(newX, imgWidth);
//		newX = Math.max(0, newX);
//		newY = Math.min(newY, imgHeight);
//		newY = Math.max(0, newY);
		pos.setX(newX);
		pos.setY(newY);
	}

	public Vector2D getPos() {
		return pos;
	}

	public void setPos(Vector2D pos) {
		this.pos = pos;
	}

	public Vector2D getMove() {
		return move;
	}

	public void setMove(Vector2D move) {
		this.move = move;
	}

}
