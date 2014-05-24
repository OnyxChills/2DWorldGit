package Game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Tile implements ITile{
	private double x, y, width, height, dx, dy;	
	@SuppressWarnings("unused")
	private double speed = 1;
	private boolean doLog = false;
	protected Rectangle hitbox = new Rectangle();
	protected Texture texture = null;
	protected Type type;
	protected Logger log;
	private int fpsCount = 0;
	
	public Tile(double x, double y, double width, double height, Type type, Logger log){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.doLog = true;
		this.log = log;
			
		dx = 0;	
		dy = 0;
			
		try{
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.location)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
			log.writeToLog("CRITICAL ERROR: ");
			e.printStackTrace(this.log.p);
			log.endLogging();
		}catch(IOException e){
			e.printStackTrace();
			log.writeToLog("CRITICAL ERROR: ");
			e.printStackTrace(this.log.p);
			log.endLogging();
		}
	}
	
	public Tile(double x, double y, double width, double height, Type type){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
			
		dx = 0;	
		dy = 0;
			
		try{
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.location)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
			log.writeToLog("CRITICAL ERROR: ");
			e.printStackTrace(log.p);
			log.endLogging();
		}catch(IOException e){
			e.printStackTrace();
			log.writeToLog("CRITICAL ERROR: ");
			e.printStackTrace(log.p);
			log.endLogging();
		}
	}
	
	/**
	 * Sets the '<b>x</b>' value of the <b>Tile</b>.
	 */
	public void setX(double x){
		this.x = x;
	}

	/**
	 * Sets the '<b>y</b>' value of the <b>Tile</b>.
	 */
	public void setY(double y){
		this.y = y;
	}

	/** 
	 * @return Returns the '<b>x</b> value of the <b>Tile</b>.
	 */
	public double getX(){
		return this.x;
	}

	/** 
	 * @return Returns the '<b>y</b> value of the <b>Tile</b>.
	 */
	public double getY(){
		return this.y;
	}

	/**
	 * Sets the '<b>dx</b>' value of the <b>Tile</b>.
	 */
	public void setDX(double dx){
		this.dx = dx;
	}

	/**
	 * Sets the '<b>dy</b>' value of the <b>Tile</b>.
	 */
	public void setDY(double dy){
		this.dy = dy;
	}


	/** 
	 * @return Returns the '<b>dx</b> value of the <b>Tile</b>.
	 */
	public double getDX(){
		return this.dx;
	}


	/** 
	 * @return Returns the '<b>dy</b> value of the <b>Tile</b>.
	 */
	public double getDY(){
		return this.dy;
	}
	
	/**
	 * Draws the quad of the <b>Tile</b> on the current <b>Display</b>.
	 */	
	public void draw(){
		/*
		glBegin(GL_QUADS);
			glColor3f(0F, 1F, 0F);
			glVertex2d(x, y); 						// Bottom-left
			glVertex2d(x, y + height); 				// Top-Left
			glVertex2d(x + width, y + height); 		// Top-right
			glVertex2d(x + width, y); 				// Bottom-right
		glEnd();
		*/
		long now, then, delta;
		then = System.nanoTime();
		
		this.texture.bind();
		glLoadIdentity();
		glTranslated(this.x, this.y, 0);
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2d(0, 0);							//Bottom Left
			
			glTexCoord2f(0, 1);
			glVertex2d(0, this.height);							//Top Left
			
			glTexCoord2f(1, 1);
			glVertex2d(this.width, this.height);							//Bottom Right
			
			glTexCoord2f(1, 0);
			glVertex2d(this.width, 0);							//Top Right
		glEnd();
		glLoadIdentity();
		
		now = System.nanoTime();
		delta = now - then;	
				
		if(doLog && (fpsCount == 0 || fpsCount == 4 * Math.pow(Main.tileSize, 2) / Math.pow(Main.tileSize, 2))){
			this.log.writeToLog(this.log.getCallerCallerClassName() + " Loading and drawing textures for tiles, " + delta + " ns, " + (delta/1000000)/60 + " ms.");
			fpsCount = 1;
		}
		fpsCount++;
	}
	
	/**
	 * Updates the <b>Tile</b>'s position based on the <b>Delta</b>.
	 */
	
	public void update(double delta){
		this.x += delta * dx;
		this.y += delta * dy;
	}
	
	/**
	 * Moves <b>Tile</b> up based on the <b>Delta</b>.
	 */	
	public void moveUp(double speed){
		setDY(speed);
	}

	/**
	 * Moves <b>Tile</b> down based on the <b>Delta</b>.
	 */	
	public void moveDown(double speed){
		setDY(-speed);
	}

	/**
	 * Moves <b>Tile</b> left based on the <b>Delta</b>.
	 */	
	public void moveLeft(double speed){
		setDX(-speed);
	}

	/**
	 * Moves <b>Tile</b> right based on the <b>Delta</b>.
	 */
	public void moveRight(double speed){
		setDX(speed);
	}
	
	/**
	 * Sets the <b>Tile</b> width.
	 */
	public void setWidth(double width){
		this.width = width;		
	}
	
	/**
	 * Sets the <b>Tile</b> height.
	 */
	public void setHeight(double height){
		this.height = height;		
	}
	
	/**
	 * Gets the <b>Tile</b> width.
	 */
	public double getWidth(){
		return this.width;
	}
	
	/**
	 * Gets the <b>Tile</b> height.
	 */
	public double getHeight(){
		return this.height;
	}
	
	/**
	 * Gets the <b>Tile</b> type.
	 */
	public Type getType(){
		return type;
	}
	
	/**
	 * Sets the <b>Tile</b> type.
	 */
	public void setType(Type type){
		long now, then, delta;
		then = System.nanoTime();
		
		this.type = type;
		try{
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.location)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
			if(doLog){
				log.writeToLog("CRITICAL ERROR: ");
				e.printStackTrace(log.p);
				log.endLogging();
			}
		}catch(IOException e){
			e.printStackTrace();
			if(doLog){
				log.writeToLog("CRITICAL ERROR: ");
				e.printStackTrace(log.p);
				log.endLogging();
			}
		}
		
		now = System.nanoTime();
		delta = now - then;
		System.out.println(doLog);
		if(doLog)
			this.log.writeToLog("Changing tile types, " + delta + " ns, " + (delta / 1000000) + " ms.");
	}
	
	/**
	 * Sets the <b>Tile</b> type.
	 */
	public void setPos(double x, double y){
		this.x = x;
		this.y = y;
	}
}
