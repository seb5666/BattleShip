package Model;


public class DestroyerBoat extends Boat {
	private final static int DESTROYER_LENGTH = 2;
	
	public DestroyerBoat(Position startPos, boolean horizontal){
		super(startPos, horizontal, DESTROYER_LENGTH);
		this.mName = "Destroyer";
		super.initialseBoat();
	}
}
