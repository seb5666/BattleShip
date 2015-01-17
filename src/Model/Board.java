package Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import Exceptions.PositionAlreadyHitException;
import Exceptions.PositionOutOfBoardException;
import Exceptions.SquareNotEmptyException;

public class Board {
	
	private int mSize;
	private TreeMap<Position, Boat> mBoardMap; //Map of Boats
	private ArrayList<Position> mHitPositions;
	private int mBoatsAlive;

	public Board(int size){
		mSize = size;
		mHitPositions = new ArrayList<Position>();
		mBoardMap = new TreeMap<Position, Boat>();
		mBoatsAlive = 0;
	}
	
	public void addBoat(Boat b) throws SquareNotEmptyException{
		ArrayList<Position> positions = b.getBoatPositions();
		for(Position p : positions){
			if(mBoardMap.containsKey(p)){
				//Boat already on square
				throw new SquareNotEmptyException();
			}
			mBoardMap.put(p, b);
		}
		mBoatsAlive++;
	}
	
	public Boat fire(Position pos) throws PositionAlreadyHitException, PositionOutOfBoardException{
		if(pos.getX() < 0 || pos.getX() >= mSize || pos.getY() < 0 || pos.getY() >= mSize){
			throw new PositionOutOfBoardException();
		}
		if(mHitPositions.contains(pos)){
			throw new PositionAlreadyHitException();
		}
		
		mHitPositions.add(pos);
		if(mBoardMap.containsKey(pos)){
			Boat b = mBoardMap.get(pos);
			b.hit(pos);
			if(b.isSunk()){
				mBoatsAlive--;
			}
			return b;
		} else {
			return null;
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
	
	public Boat boatAt(Position p){
		if(mBoardMap.containsKey(p)){
			return mBoardMap.get(p);
		}
		return null;
	}
	
	public ArrayList<Position> getBoatPositions(){
		return new ArrayList<Position>(mBoardMap.keySet());
	}
	
	public boolean isPositionHit(Position p){
		return mHitPositions.contains(p);
	}
	
	public HashMap<Position, Boat> getBoatHitPositions(){
		HashMap<Position, Boat> hitBoatPositions = new HashMap<Position, Boat>();
		for(Position p : mBoardMap.keySet()){
			if(mHitPositions.contains(p)){
				hitBoatPositions.put(p, boatAt(p));
			}
		}
		return hitBoatPositions;
	}
	
	public ArrayList<Position> getHitPositions(){
		return mHitPositions;
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
				if(mBoardMap.containsKey(new Position(x,y))){
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
