package Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import static Game.Main.*;

public class Map {
	
	private Tile[][] tiles = new Tile[64][64];
	private Logger log;
	public Logger mapLog;
	private boolean doLog = false;
	private int fpsCount = 0;
	
	public Map(Logger log){	
		this.log = log;
		this.mapLog = new Logger(logFolder + mapLog.getCallerClassName().toString() + " - MapLog");
		this.mapLog.startLogging();
		doLog = true;
		for(int x = 0; x < 64; x++){
			for(int y = 0; y < 64; y++){
				tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, Type.BRICK_WALL, mapLog);
			}
		}
	}
	
	public Map(){	
		for(int x = 0; x < 64; x++){
			for(int y = 0; y < 64; y++){
				tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, Type.BRICK_WALL);
			}
		}
	}
	
	public void load(File loadFile, Map currentMap){
		long now, then, delta;
		SAXBuilder builder = new SAXBuilder();
		try {
			// XML open
			then = System.nanoTime();
			Document document = builder.build(loadFile);
			Element root = document.getRootElement();
			now = System.nanoTime();
			delta = now - then;
			if(doLog)
				mapLog.writeToLog("LOAD: Create Document and Element objects for loadmap, " + delta + " ns, " + (delta / 1000000) + " ms.");
			// tile loop
			then = System.nanoTime();

			for(Object tile : root.getChildren()){
				double x = Double.parseDouble(((Element) tile).getAttributeValue("x"));
				double y = Double.parseDouble(((Element) tile).getAttributeValue("y"));
				Type t = Type.valueOf(((Element) tile).getAttributeValue("type"));
				
				if(currentMap.getAt((int)x, (int)y).getType() != t && this.doLog){
					tiles[(int) x][(int) y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, t, this.mapLog);
				}
				if(currentMap.getAt((int)x, (int)y).getType() != t && !this.doLog){
					tiles[(int) x][(int) y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, t);
				}
			}
			now = System.nanoTime();
			delta = now - then;
			
			fpsCount++;
			
			if(doLog && fpsCount == 60){
				mapLog.writeToLog("Tile loop, " + delta + " ns, " + (delta / 1000000) + " ms.");
				fpsCount = 0;
			}
			
		} catch (JDOMException e) {
			e.printStackTrace();
			if(doLog){
				log.writeToLog("CRITICAL ERROR: ");
				e.printStackTrace(log.p);
				log.endLogging();
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(doLog){
				log.writeToLog("CRITICAL ERROR: ");
				e.printStackTrace(log.p);
				log.endLogging();
			}
		}
	}
	
	public void save(File saveFile){
		long now, then, delta;
		then = System.nanoTime();
		Document document = new Document();
		Element root = new Element("tiles");
		document.setRootElement(root);
		now = System.nanoTime();
		delta = now - then;
		if(doLog)
			log.writeToLog("SAVE: Create Document and Element objects, " + delta + " ns, " + (delta / 1000000) + " ms.");

		then = System.nanoTime();
		for(int x = 0; x < 64; x++){
			for(int y = 0; y < 64; y++){
				Element tile = new Element("tile");
				tile.setAttribute("x", String.valueOf((int)tiles[x][y].getX() / tileSize));
				tile.setAttribute("y", String.valueOf((int)tiles[x][y].getY() / tileSize));
				tile.setAttribute("type", String.valueOf(tiles[x][y].getType()));
				root.addContent(tile);
			}
		}
		if(!saveFile.exists()){
			try {
				saveFile.createNewFile();
			}catch (IOException e){
				e.printStackTrace();
				if(doLog){
					log.writeToLog("CRITICAL ERROR: ");
					e.printStackTrace(log.p);
					log.endLogging();
				}
			}
		}
		
		XMLOutputter output = new XMLOutputter();
		try {
			output.output(document, new FileOutputStream(saveFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if(doLog){
				log.writeToLog("CRITICAL ERROR: ");
				e.printStackTrace(log.p);
				log.endLogging();
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(doLog){
				log.writeToLog("CRITICAL ERROR: ");
				e.printStackTrace(log.p);
				log.endLogging();
			}
		}
		now = System.nanoTime();

		delta = now - then;
		if(doLog)
			this.log.writeToLog("Tile loop, " + delta + " ns, " + (delta / 1000000) + " ms.");
	}
	
	public void setAt(int x, int y, Type b){
		if(this.doLog)
			tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, b, this.mapLog);
		else
			tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, b);
	}
	
	public Tile getAt(int x, int y){
		return tiles[x][y];
	}
	
	public void draw(){
		for(int x = 0; x < 64; x++){
			for(int y = 0; y < 64; y++){
				tiles[x][y].draw();
			}
		}
	}
}
