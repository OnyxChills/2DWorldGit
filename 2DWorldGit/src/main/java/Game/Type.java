package Game;

public enum Type {
	DIRT_WALL(Main.textureFolder + "terrain/dirt_wall.png", true),
	STONE_WALL(Main.textureFolder + "terrain/stone_wall.png", true),
	BRICK_WALL(Main.textureFolder + "terrain/brick_wall.png", false),
	BRAIN_TEST(Main.textureFolder + "testing/braininjar.png", false),
	INVENTORY_SLOT(Main.textureFolder + "gui/playerInventorySlot.png", false),
	AIR(Main.textureFolder + "testing/air.png", true);
	public final String location;
	public final boolean canPassThrough;
	Type(String location, boolean canPassThrough){
		this.location = location;
		this.canPassThrough = canPassThrough;
	}
}
