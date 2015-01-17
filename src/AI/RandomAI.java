package AI;

import java.util.ArrayList;
import java.util.Random;

import Interfaces.BattleShipAI;
import Model.BattleShipBoat;
import Model.Board;
import Model.Boat;
import Model.CarrierBoat;
import Model.CruiserBoat;
import Model.DestroyerBoat;
import Model.Position;
import Model.SubmarineBoat;

public class RandomAI implements BattleShipAI {

	private Random rd;
	private ArrayList<Position> mPastPositions;
	
	public RandomAI() {
		rd = new Random();
		mPastPositions = new ArrayList<Position>();
	}
	
	@Override
	public Boat[] getBoatsPosition(int boardSize) {
		Boat[] boats = new Boat[5];
		ArrayList<Position> positions = new ArrayList<Position>();
		
		boats[0] = addBoat("carrier", boardSize, positions);
		boats[1] = addBoat("battleship", boardSize, positions);
		boats[2] = addBoat("cruiser", boardSize, positions);
		boats[3] = addBoat("submarine", boardSize, positions);
		boats[4] = addBoat("destroyer", boardSize, positions);
		
		return boats;
	}
	
	@Override
	public Position nextPos(Board b) {
		int x = 0;
		int y = 0;
		Position p = null;
		while(true){
			x = randomPosition(b.getSize());
			y = randomPosition(b.getSize());
			p = new Position(x, y);
			if(!mPastPositions.contains(p)){
				mPastPositions.add(p);
				return p;
			}
		}
	}
	
	private Boat addBoat(String boatName, int boardSize, ArrayList<Position> positions){

		int x = 0;
		int y = 0;
		boolean horizontal = false;
		
		boolean boatAdded = false;

		//BAD CODE, SHOULD get the length from the appropriate boat class instead of hard coding
		int boatLength = 0;
		if(boatName.equals("carrier")){
			boatLength = 5;
		} 
		else if (boatName.equals("battleship")){
			boatLength = 4;
		}
		else if (boatName.equals("cruiser")){
			boatLength = 3;
		}
		else if (boatName.equals("submarine")){
			boatLength = 3;
		}
		else if (boatName.equals("destroyer")){
			boatLength = 2;
		}
		
		while(!boatAdded){
			x = randomPosition(boardSize);
			y = randomPosition(boardSize);
			horizontal = randomBoolean();
			
			boolean positionExists = false;
			for(int i=0; i<boatLength; i++){
				Position pos;
				if(horizontal){
					pos = new Position(x, y +i);
				} else {
					pos = new Position(x + i, y);
				}
				if(positions.contains(pos)){
					positionExists = true;
				} 
			}
			if(!positionExists){
				boatAdded = true;
				for(int i=0; i<boatLength; i++){
					Position pos;
					if(horizontal){
						pos = new Position(x, y +i);
					} else {
						pos = new Position(x + i, y);
					}
					positions.add(pos);				
				}
			}
		}
		if(boatName.equals("carrier")){
			return new CarrierBoat(new Position(x,y), horizontal);
		} 
		else if (boatName.equals("battleship")){
			return new BattleShipBoat(new Position(x,y), horizontal);
		}
		else if (boatName.equals("cruiser")){
			return new CruiserBoat(new Position(x,y), horizontal);
		}
		else if (boatName.equals("submarine")){
			return new SubmarineBoat(new Position(x,y), horizontal);
		}
		else if (boatName.equals("destroyer")){
			return new DestroyerBoat(new Position(x,y), horizontal);
		}
		return null;
	}

	private boolean randomBoolean(){
		int a = rd.nextInt(2);
		return (a == 1);
	}
	
	private int randomPosition(int boardSize){
		return rd.nextInt(boardSize);
	}
}
