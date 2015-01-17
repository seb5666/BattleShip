package Model;


public class CruiserBoat extends Boat {
private final static int CRUISER_LENGTH = 3;
	
	public CruiserBoat(Position startPos, boolean horizontal){
		super(startPos, horizontal, CRUISER_LENGTH );
		this.mName = "Battleship";
		super.initialseBoat();
	}
}
