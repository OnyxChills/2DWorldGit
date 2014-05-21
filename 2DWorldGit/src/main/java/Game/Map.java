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
	private boolean doLog = false;
	
	public Map(Logger log){	
		this.log = log;
		doLog = true;
		for(int x = 0; x < 64; x++){
			for(int y = 0; y < 64; y++){
				tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, Type.BRICK_WALL, log);
			}
		}
	}
	
	public Map(){	
		for(int x = 0; x < 64; x++){
			for(int y = 0; y < 64; y++){
				tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, Type.BRICK_WALL, log);
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
				log.writeToLog("LOAD: Create Document and Element objects for loadmap, " + delta + " ns, " + (delta / 1000000) + " ms.");
			// tile loop
			then = System.nanoTime();

			for(Object tile : root.getChildren()){
				double x = Double.parseDouble(((Element) tile).getAttributeValue("x"));
				double y = Double.parseDouble(((Element) tile).getAttributeValue("y"));
				Type t = Type.valueOf(((Element) tile).getAttributeValue("type"));
				if(currentMap.getAt((int)x, (int)y).getType() != t){
					tiles[(int) x][(int) y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, t, log);
				}
			}
			now = System.nanoTime();

			delta = now - then;
			if(doLog)
				log.writeToLog("Tile loop, " + delta + " ns, " + (delta / 1000000) + " ms.");
			
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
			log.writeToLog("Tile loop, " + delta + " ns, " + (delta / 1000000) + " ms.");
	}
	
	public void setAt(int x, int y, Type b){
		tiles[x][y] = new Tile(x * tileSize, y * tileSize, tileSize, tileSize, b, log);
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
