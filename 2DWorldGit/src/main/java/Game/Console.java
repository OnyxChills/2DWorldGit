package Game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.lwjgl.input.Keyboard;

public class Console{
	@SuppressWarnings("unused")
	private double x, y, width, height;
	private int i = 0;
	private String currentString = "";
	private String[] c = new String[256];
	private Logger log;
	
	/**
	 * @param x The 'x' coordinate of the <b>Console</b>.
	 * @param y The 'y' coordinate of the <b>Console</b>.
	 * @param width The width of the <b>Console</b>.
	 * @param height The height of the <b>Console</b>.
	 * @param log The log in which data for this <b>Console</b> is stored.
	 */
	public Console(double x, double y, double width, double height, Logger log){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.log = log;
	}
	
	/**
	 * Run the console text capture.
	 */
	public void start(){		
		catchText();
	}
	
	private void catchText(){
		if(Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			
		}
		else if(Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			if(Keyboard.getEventKeyState()){
				if(!Keyboard.isRepeatEvent()){
					c[i] = Keyboard.getKeyName(Keyboard.getEventKey());		
					currentString = currentString.concat(" ");
					i++;
				}
			}
		}
		else if(Keyboard.getEventKey() == Keyboard.KEY_PERIOD){
			if(Keyboard.getEventKeyState()){
				if(!Keyboard.isRepeatEvent()){
					c[i] = Keyboard.getKeyName(Keyboard.getEventKey());		
					currentString = currentString.concat(".");
					i++;
				}
			}
		}
		else if(Keyboard.getEventKey() == Keyboard.KEY_BACKSLASH){
			if(Keyboard.getEventKeyState()){
				if(!Keyboard.isRepeatEvent()){
					c[i] = Keyboard.getKeyName(Keyboard.getEventKey());		
					currentString = currentString.concat("/");
					i++;
				}
			}
		}
		else if(Keyboard.getEventKey() == Keyboard.KEY_BACK){
			if(Keyboard.getEventKeyState()){
				if(!Keyboard.isRepeatEvent()){
					if(i > 0){
						c[i] = Keyboard.getKeyName(Keyboard.getEventKey());		
						currentString = currentString.substring(0, i-1);
						log.writeToConsole(currentString);
						i--;
					}
				}
			}
		}		
		else{
			if(Keyboard.getEventKeyState()){
				if(!Keyboard.isRepeatEvent()){
					c[i] = Keyboard.getKeyName(Keyboard.getEventKey()).toLowerCase();		
					currentString = currentString.concat(c[i]);
					i++;
				}
			}
		}
	}
	
	public void drawCommandToGUI(GameGUI gui){
		gui.drawText(this.currentString, Main.debugFont, Color.blue, this.x, this.y);
	}
	
	public void checkCommand(List<String> command, Logger log){
		log.writeToBoth("Console ran command: " + command);
		currentString = "";
		if(command.size() > 0){
			if(command.get(0).equalsIgnoreCase(Command.EXIT.command) && command.size() < 2){
				Commands.exit(log);
			}
			else if(command.get(0).equalsIgnoreCase(Command.LOAD.command)){
				Commands.load(command.get(1), Main.map, log);
			}
			else if(command.get(0).equalsIgnoreCase(Command.SAVE.command)){
				Commands.save(command.get(1), Main.map, log);
			}
			else{
				log.writeToBoth("Command did nothing.");
			}
		}
		else{
			log.writeToBoth("Empty command ran.");
		}
	}
	
	public List<String> breakString(String line){
		StringTokenizer sToken;
		List<String> cmd = new ArrayList<String>();
		sToken = new StringTokenizer(line);
		while(sToken.hasMoreTokens()){
			cmd.add(sToken.nextToken());
		}
		return cmd;
	}
	
	public String getString(){
		return this.currentString;
	}
	
	public void clear(){
		this.i = 0;
	}
	
	public void setPosition(double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
