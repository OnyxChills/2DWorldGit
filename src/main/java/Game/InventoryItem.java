package Game;

public class InventoryItem extends Tile{
	private int amount;
	
	public InventoryItem(double x, double y, double width, double height, int amount, Type type, Logger log) {
		super(x, y, width, height, type, log);
		amount = this.amount;
	}
	
	public int getAmount(){
		return this.amount;
	}
	
	public void setAmount(int amount){
		this.amount = amount;
	}
	
	public void addAmount(int amount){
		this.amount = this.amount + amount;
	}
	
	public void subtractAmount(int amount){
		this.amount = this.amount - amount;
	}
}
