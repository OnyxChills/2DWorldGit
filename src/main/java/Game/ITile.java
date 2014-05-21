package Game;

public interface ITile{
	public void setX(double x);
	public void setY(double y);
	public double getX();
	public double getY();
	public void setWidth(double width);
	public void setHeight(double height);
	public double getWidth();
	public double getHeight();
	public void setDX(double dx);
	public void setDY(double dy);
	public double getDX();
	public double getDY(); 
	public void draw();
	public void update(double delta);
	public void moveUp(double speed);
	public void moveDown(double speed);
	public void moveLeft(double speed);
	public void moveRight(double speed);
}
