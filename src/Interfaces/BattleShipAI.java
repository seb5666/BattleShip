package Interfaces;

import Model.Board;
import Model.Boat;
import Model.Position;

public interface BattleShipAI {
	
	//List of boats positions based on game board size
	public Boat[] getBoatsPosition(int boardSize); 
	
	//get position of next shot
	public Position nextPos(Board b);
}
