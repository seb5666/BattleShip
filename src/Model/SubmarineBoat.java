package Model;


public class SubmarineBoat extends Boat {
	private final static int SUBMARINE_LENGTH = 3;
	
	public SubmarineBoat(Position startPos, boolean horizontal){
		super(startPos, horizontal, SUBMARINE_LENGTH);
		this.mName = "Carrier";
		super.initialseBoat();
	}
}
