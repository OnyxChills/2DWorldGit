package Game;

import java.io.File;

import org.lwjgl.opengl.Display;

public class Commands{
	
	public static void exit(Logger log){
		Display.destroy();
		log.endLogging();
		System.exit(0);
	}
	
	public static void load(String mapName, Map currentMap, Logger log){
		currentMap.load(new File(Main.mapFolder + mapName + ".xml"), currentMap);
		log.writeToLog("Loaded map \'" + mapName + "\' from \'" + "\'");
	}
	
	public static void save(String mapName, Map currentMap, Logger log){
		currentMap.save(new File(Main.mapFolder + mapName + ".xml"));
		log.writeToLog("Saved map \'" + mapName + "\'");
	}
}
