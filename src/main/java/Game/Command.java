package Game;

public enum Command{
	EXIT("exit", new String[] {}),
	LOAD("load", new String[] {}),
	SAVE("save", new String[] {}),
	TEST("test", new String[] {"test2", "test3"})
	;
	public final String command;
	public final String[] args;
	Command(String command, String[] args){
		this.command = command;
		this.args = args;
	}
}