package Game;

import static Game.Main.tileSize;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Inventory extends GameGUI{
	private Logger log;
	private double x, y;
	private double slotWidth = 68;
	private double slotHeight = 68;
	private int sizeVer, sizeHor;
	private List<InventorySlot>[] slots;
	private List<InventoryItem>[] items;
	private boolean isOpen = false;
	
	@SuppressWarnings("unchecked")
	public Inventory(double x, double y, int sizeVer, int sizeHor, Logger log) {
		super(x, y, log);
		this.log = log;
		this.x = x;
		this.y = y;
		this.sizeHor = sizeHor;
		this.sizeVer = sizeVer;
		
		slots = new ArrayList[sizeHor];
		items = new ArrayList[sizeHor];
		for(int i = 0; i < sizeHor; i++){
			slots[i] = new ArrayList<InventorySlot>();
			items[i] = new ArrayList<InventoryItem>();
		}
		
		for(int i = 0; i < sizeHor; i++){
			for(int v = 0; v < sizeVer; v++){
				slots[i].add(new InventorySlot(x * tileSize + i * slotWidth, y * tileSize + v * slotHeight, slotWidth, slotHeight, Type.INVENTORY_SLOT, log));
				items[i].add(new InventoryItem(x * tileSize + i * slotWidth + slotWidth/30, y * tileSize + v * slotHeight + slotHeight/30, tileSize, tileSize, 0, Type.AIR, log));
			}
		}
	}
	
	public void setItemAt(Type item, int slotX, int slotY, int amount){
		items[slotX].get(slotY).setType(Type.STONE_WALL);
		items[slotX].get(slotY).setAmount(amount);
	}
	
	public void addItemAt(int slotX, int slotY, int amount){
		items[slotX].get(slotY).addAmount(amount);
	}
	
	public void subtractItemAt(int slotX, int slotY, int amount){
		items[slotX].get(slotY).subtractAmount(amount);
	}
	
	public void draw(){
		if(this.isOpen){
			long now, then, delta;
			then = System.nanoTime();

			glEnable(GL_TEXTURE_2D);
			for(int i = 0; i < sizeHor; i++){
				for(int v = 0; v < sizeVer; v++){
					slots[i].get(v).setPos(x * tileSize + i * slotWidth, y * tileSize + v * slotHeight);
					items[i].get(v).setPos(x * tileSize + i * slotWidth + slotWidth/30, y * tileSize + v * slotHeight + slotHeight/30);
					slots[i].get(v).draw();
					items[i].get(v).draw();
					drawItemAmount(i, v);
				}
			}
			now = System.nanoTime();
			delta = now - then;
			this.log.writeToLog("Creating Inventory data, " + delta + " ns, " + (delta / 1000000) + " ms.");
		}
	}
	
	public boolean isOpen(){
		return this.isOpen;
	}
	
	public void toggleOpen(){
		this.isOpen = !this.isOpen;
	}
	
	public int getWidth(){
		return this.sizeHor;
	}
	
	public int getHeight(){
		return this.sizeVer;
	}
	
	public Type getItemAtSlot(int x, int y){
		return items[x].get(y).getType();
	}
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	private void drawItemAmount(double x, double y){
		super.drawText(String.valueOf(items[(int) x].get((int) y).getAmount()), Main.debugFont, Color.red, slots[(int) x].get((int) y).getX() + 4, slots[(int) x].get((int) y).getY() + 2);
	}
}
