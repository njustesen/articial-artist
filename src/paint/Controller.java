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
		
		// Paint line on g between pos and newPos
		
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
