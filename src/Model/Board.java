package Model;
import java.util.ArrayList;
import java.util.TreeMap;

import Exceptions.PositionAlreadyHitException;
import Exceptions.PositionOutOfBoatException;
import Exceptions.SquareNotEmptyException;

public class Board {
	
	private int mSize;
	private TreeMap<Position, Boat> mBoard; //Map of Boats
	private ArrayList<Position> mHitPositions;
	private int mBoatsAlive;

	public Board(int size){
		mSize = size;
		mHitPositions = new ArrayList<Position>();
		mBoard = new TreeMap<Position, Boat>();
		mBoatsAlive = 0;
	}
	
	public void addBoat(Boat b) throws SquareNotEmptyException{
		ArrayList<Position> positions = b.getBoatPositions();
		for(Position p : positions){
			if(mBoard.containsKey(p)){
				//Boat already on square
				throw new SquareNotEmptyException();
			}
			mBoard.put(p, b);
		}
		mBoatsAlive++;
	}
	
	public String fire(Position pos) throws PositionAlreadyHitException, PositionOutOfBoatException{
		if(mHitPositions.contains(pos)){
			throw new PositionAlreadyHitException();
		}
		mHitPositions.add(pos);
		if(mBoard.containsKey(pos)){
			Boat b = mBoard.get(pos);
			b.hit(pos);
			return b.getName();
		} else {
			return "water";
		}
	}
	
	public boolean isGameOver(){
		return (mBoatsAlive == 0);
	}
	
	public int getBoatsAlive(){
		return mBoatsAlive;
	}
	
	public int getSize(){
		return this.mSize;
	}
	
	public ArrayList<Position> getBoatPositions(){
		return new ArrayList<Position>(mBoard.keySet());
	}
	
	@Override
	public String toString(){
		String s="Y\\X \t";
		for(int i=0; i < mSize; i++){
			//top row
			s += i + "\t"; 
		}
		s += "\n";
		for(int y=0; y < mSize; y++){
			s += y + "\t";
			for(int x=0; x < mSize; x++){
				if(mBoard.containsKey(new Position(x,y))){
					s += "b\t";
				} else {
					s += "~\t";
				}
			}
			s += "\n";
		}
		return s;
	}
	
}
