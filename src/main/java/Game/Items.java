package Game;

public enum Items{
	AIR(Main.textureFolder + "testing/air.png");
	
	public final String location;
	Items(String location){
		this.location = location;
	}
}
