package Game;

import java.awt.Color;
import java.awt.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import static Game.Main.tileSize;

public class GameGUI {
	protected double x, y;
	
	private UnicodeFont fon;
	private Logger log;
	
	public GameGUI(double x, double y, Logger log){
		this.x = x;
		this.y = y;
		this.log = log;
	}
	
	public void drawDebug(double guiX, double guiY){
		this.drawText("Player Position: (" + (int)(Main.playerX/tileSize) + ", " + (int)(Main.playerY/tileSize) + ")", Main.debugFont, Color.white, guiX, guiY);
		this.drawText("On tile type: " + Main.map.getAt((int)(Main.playerX/tileSize), (int)(Main.playerY/tileSize)).getType(), Main.debugFont, Color.white, guiX, guiY + 24);
	}

	@SuppressWarnings("unchecked")
	public void drawText(String text, Font font, Color color, double x, double y){
		fon = new UnicodeFont(font);
		fon.getEffects().add(new ColorEffect(color));
		fon.addAsciiGlyphs();
		try {
			fon.loadGlyphs();
		} catch (SlickException e){
			e.printStackTrace();
			log.writeToLog("CRITICAL ERROR: ");
			e.printStackTrace(log.p);
		}
		
		fon.drawString((float)x, (float)y, text);
	}
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
}
