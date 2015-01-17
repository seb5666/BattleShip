package Model;

public class CarrierBoat extends Boat {
	private final static int CARRIER_LENGTH = 5;
	
	public CarrierBoat(Position startPos, boolean horizontal){
		super(startPos, horizontal, CARRIER_LENGTH);
		this.mName = "Carrier";
		super.initialseBoat();
	}
}
