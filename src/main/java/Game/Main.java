package Game;

import static Game.Type.BRAIN_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main{

	public static double playerX;
	public static double playerY;
	public static final double screenWidth = 1920;
	public static final double screenHeight = 1080;
	public static final String mapFolder = "src/main/maps/";
	public static final String textureFolder = "src/main/resources/";
	public static final String logFolder = "src/main/logs/";
	
	public static Font debugFont = new Font("Times New Roman", Font.BOLD, 22); //18 is default

	public static final double tileSize = 64;
	public static int columns = (int) (screenWidth / tileSize);
	public static int rows = (int) (screenHeight / tileSize);
	public static double HUDSize = 4;
	public boolean consoleInput = false;

	private Entity player;
	public static Map map;
	private Camera cam;
	private GameGUI debugGUI;
	private GameGUI consoleGUI;
	private Logger debugLog = new Logger(logFolder+"debuglog"/* + " - " + new SimpleDateFormat("HH-mm").format(new Date()).toString()*/);
	private Console console;
	private Inventory inv;
	private InputManager inputMan;
	
	public Main() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		initDisplay();
		initObjects();		
		initGL();
		// Main Loop
		mainLoop();
	}

	private void input() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Keyboard.enableRepeatEvents(true);
		while (Keyboard.next()) {
			// Movement block
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_LEFT),
					Keyboard.getEventKeyState(),
					!consoleInput,
					!inv.isOpen()
					}))
				player.moveLeft(tileSize, map);
			
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_UP), 
					Keyboard.getEventKeyState(), 
					!consoleInput, 
					!inv.isOpen()
					}))
				player.moveUp(tileSize, map);
			
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_RIGHT), 
					Keyboard.getEventKeyState(), 
					!consoleInput, 
					!inv.isOpen()
					}))
				player.moveRight(tileSize, map);
			
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_DOWN), 
					Keyboard.getEventKeyState(), 
					!consoleInput, 
					!inv.isOpen()
					}))
				player.moveDown(tileSize, map);
			
			// Movement block

			if (inputMan.isKeyDown(Keyboard.KEY_ESCAPE)){
				debugLog.endLogging();
				map.mapLog.endLogging();
				Display.destroy();
				System.exit(0);
			}
			
			if(inputMan.keyArgs(new boolean[]{inputMan.isKeyDown(Keyboard.KEY_L), Keyboard.getEventKeyState(), !consoleInput})){
				inv.setItemAt(Type.STONE_WALL, 1, 2, 1);
			}
			
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_T), 
					Keyboard.getEventKeyState(), 
					!consoleInput, 
					!inv.isOpen()
					})){
				inv.addItemAt(1, 2, 1);
			}

			if(inputMan.keyArgs(new boolean[]{inputMan.isKeyDown(Keyboard.KEY_I), Keyboard.getEventKeyState()})){
				inv.toggleOpen();
			}
			
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_G), 
					Keyboard.getEventKeyState(), 
					!consoleInput, 
					!inv.isOpen()
					})){
				inv.subtractItemAt(1, 2, 1);
			}

			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_P), 
					Keyboard.getEventKeyState(), 
					!consoleInput, 
					!inv.isOpen()
					})){
				map.setAt((int) (player.getX() / tileSize), (int) (player.getY() / tileSize), Type.STONE_WALL);
				debugLog.writeToLog("Put \"" + Type.STONE_WALL.name() + "\" type block at point (" + player.getX() + ", " + player.getX() + ")");
			}
			
			if(inputMan.keyArgs(new boolean[]{
					inputMan.isKeyDown(Keyboard.KEY_F), 
					Keyboard.getEventKeyState(), 
					!consoleInput
					})){
				debugLog.writeToConsole(String.valueOf(debugLog.time(inv.getClass().getMethod("toggleOpen"), inv)));
			}
			
			if(inputMan.keyArgs(new boolean[]{inputMan.isKeyDown(Keyboard.KEY_RETURN), Keyboard.getEventKeyState()})){
				consoleInput = !consoleInput;
				if(consoleInput == false){
					console.checkCommand(console.breakString(console.getString()), debugLog);
					console.clear();
				}
			}
			
			if(consoleInput){
				console.start();
				console.drawCommandToGUI(consoleGUI);
			}
		}
	}
	
	private void initObjects() {
		player = new Entity(tileSize * 8, tileSize * 54, tileSize, tileSize, BRAIN_TEST, debugLog);
		map = new Map(debugLog);
		cam = new Camera(player.getX() + (tileSize * 4), player.getY() + (tileSize * 4));
		debugGUI = new GameGUI(tileSize * 4, tileSize * 4, debugLog);
		consoleGUI = new GameGUI(cam.getX() - 9 * tileSize, cam.getY() + 9 * tileSize, debugLog);
		console = new Console(cam.getX() - 9 * tileSize, cam.getY() + 9 * tileSize, cam.getX() + 3 * tileSize, cam.getY() + 8 * tileSize + 16, debugLog);
		inv = new Inventory(-5, 46, 3, 4, debugLog);
		inputMan = new InputManager(debugLog);
	}
	
	public void mainLoop() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		while (!Display.isCloseRequested()){
			// Render
			glClear(GL_COLOR_BUFFER_BIT); // Clear the 2D environment
			
			input();
			map.draw();
			updateVars();
			
			player.draw();
			
			cam.update(player);
			consoleGUI.drawText(console.getString(), Main.debugFont, Color.green, cam.getX() - (tileSize * 9), cam.getY() + (tileSize * 8));
			console.setPosition(cam.getX() - 9 * tileSize, cam.getY() + 9 * tileSize, cam.getX() + 3 * tileSize, cam.getY() + 8 * tileSize + 16);
			inv.setPosition(cam.getX()/64 - 7, cam.getY()/64 - 3);
			inv.draw();
			
			debugGUI.drawDebug(cam.getX() - (tileSize * 14), cam.getY() - (tileSize * 8));
						
			Display.update();
			Display.sync(60);
		}
		debugLog.endLogging();
		Display.destroy();
		System.exit(0);
	}
	
	public void initGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, screenWidth, 0, screenHeight, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);								// Transparency
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);// ^
	}
	
	public void initDisplay(){
		try {
			DisplayMode displayMode = null;
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
//	        for(DisplayMode i : modes){
//	        	System.out.println(i.toString());
//	        }

	         for (int i = 0; i < modes.length; i++)
	         {
	             if (modes[i].getWidth() == screenWidth
	             && modes[i].getHeight() == screenHeight
	             && modes[i].isFullscreenCapable())
	               {
	                    displayMode = modes[i];
	               }
	         }
			Display.setDisplayMode(modes[9]);
			Display.setFullscreen(true);
			Display.setTitle("World of Debug");
			Display.create();
			debugLog.startLogging();
			debugLog.writeHeader();
			debugLog.writeToLog("Current screen: " + displayMode.toString());
		} catch (LWJGLException e) {
			e.printStackTrace();
			debugLog.writeToLog("CRITICAL ERROR: ");
			e.printStackTrace(debugLog.p);
			debugLog.endLogging();
			Display.destroy();
			System.exit(1);
		}
	}

	private long lastFrame;

	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private void updateVars(){
		playerX = player.getX();
		playerY = player.getY();
	}

	@SuppressWarnings("unused")
	private double getDelta() {
		long currentTime = getTime();
		double delta = (double) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		new Main();
	}
}
