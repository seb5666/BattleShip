package Model;

public class BattleShipBoat extends Boat {
private final static int BATTLE_SHIP_LENGTH = 4;
	
	public BattleShipBoat(Position startPos, boolean horizontal){
		super(startPos, horizontal, BATTLE_SHIP_LENGTH);
		this.mName = "Battleship";
		super.initialseBoat();
	}
}
