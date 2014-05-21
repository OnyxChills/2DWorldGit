package Game;

import org.lwjgl.input.Keyboard;

public class InputManager{
	@SuppressWarnings("unused")
	private Logger log;
	
	public InputManager(Logger log){
		this.log = log;
	}
	
	public boolean keyArgs(boolean[] args){
		int count = 0;
		for(boolean i : args){
			if(i)
				count++;
		}
		if(count == args.length)
			return true;
		else
			return false;
	}
	
	public boolean isKeyDown(int key){
		if(Keyboard.getEventKey() == key && Keyboard.isKeyDown(key))
			return true;
		else
			return false;
	}
}
