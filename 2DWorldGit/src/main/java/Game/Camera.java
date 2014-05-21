package Game;

import static Game.Main.tileSize;

import static org.lwjgl.opengl.GL11.*;

public class Camera{
	private double cameraPosX;
	private double cameraPosY;

	public Camera(double cameraPosX, double cameraPosY){
		this.cameraPosX = cameraPosX;
		this. cameraPosY = cameraPosY;
	}
		
	public void update(Entity entity){
		double centerX = tileSize * 14;
		double centerY = tileSize * 8;
		cameraPosX = entity.getX();
		cameraPosY = entity.getY();
		
		if(cameraPosX < tileSize * 9){
			cameraPosX = tileSize * 9;
		}
		if(cameraPosY < tileSize * 8){
			cameraPosY = tileSize * 8;
		}
		if(cameraPosX > tileSize * 48){
			cameraPosX = tileSize * 48;
		}
		if(cameraPosY > tileSize * 55.1){
			cameraPosY = tileSize * 55.1;
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(cameraPosX - centerX, 
				cameraPosX + 1920 - centerX, 
				cameraPosY + 1080 - centerY, 
				cameraPosY - centerY,				
				1,
				-1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public double getX(){
		return cameraPosX;		
	}
	
	public double getY(){
		return cameraPosY;
	}
}
