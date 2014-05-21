package Game;

import static Game.Main.tileSize;

public class Entity extends Tile{	
	public Entity(double x, double y, double width, double height, Type type, Logger log){
		super(x, y, width, height, type, log);	
	}
	
	public void moveUp(double speed, Map map){
		if(getY() > 0)
			if(checkCollisionUp(map))
				setY(getY() - speed);
	}

	public void moveDown(double speed, Map map){
		if(getY() < 63 * tileSize)
			if(checkCollisionDown(map))
				setY(getY() + speed);
	}

	public void moveLeft(double speed, Map map){
		if(getX() > 0)
			if(checkCollisionLeft(map))
			setX(getX() - speed);
	}

	public void moveRight(double speed, Map map){
		if(getX() < 63 * tileSize)
			if(checkCollisionRight(map))
			setX(getX() + speed);
	}
	
	public boolean checkCollisionLeft(Map map){
		if(getX() > map.getAt((int)((getX() - tileSize) / tileSize), (int)(getY() / tileSize)).getX()
					&& !map.getAt((int)((getX() - tileSize) / tileSize), (int)(getY() / tileSize)).getType().canPassThrough)
			return true;
		else
			return false;
	}
	
	public boolean checkCollisionRight(Map map){
		if(getX() < map.getAt((int)((getX() + tileSize) / tileSize), (int)(getY() / tileSize)).getX()
			&& !map.getAt((int)((getX() + tileSize) / tileSize), (int)(getY() / tileSize)).getType().canPassThrough)
			return true;
		else 
			return  false;
	}
	
	public boolean checkCollisionDown(Map map){
		if(getY() < map.getAt((int)(getX() / tileSize), (int)((getY() + tileSize) / tileSize)).getY()
				&& !map.getAt((int)(getX() / tileSize), (int)((getY() + tileSize) / tileSize)).getType().canPassThrough)
			return true;
		else 
			return false;
	}
	
	public boolean checkCollisionUp(Map map){
		if(getY() > map.getAt((int)(getX() / tileSize), (int)((getY() - tileSize) / tileSize)).getY()
				&& !map.getAt((int)(getX() / tileSize), (int)((getY() - tileSize) / tileSize)).getType().canPassThrough)
			return true;
		else 
			return false;
	}
}
