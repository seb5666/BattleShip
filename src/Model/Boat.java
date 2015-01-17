package Model;

import java.util.ArrayList;

import javax.management.MXBean;

import Exceptions.PositionOutOfBoatException;

public abstract class Boat {
	
	protected boolean mSunk;
	protected boolean mHorizonzal;
	protected int mLength;
	protected Position mStartPosition;
	protected ArrayList<Position> mBoatPositions;
	protected ArrayList<Position> mUnHitPositions;
	protected String mName;
	
	public Boat(Position startPos, boolean horizontal, int length){
		mStartPosition = startPos;
		mHorizonzal = horizontal;
		mLength = length;
		mSunk = false;
		mBoatPositions = new ArrayList<Position>();
		mUnHitPositions = new ArrayList<Position>();
	}
	
	public void hit(Position pos) throws PositionOutOfBoatException{
		if(mUnHitPositions.contains(pos)){
			mUnHitPositions.remove(pos);
		} else {
			throw new PositionOutOfBoatException();
		}
	}
	
	public boolean isSunk(){
		return (mUnHitPositions.size() == 0) ? true : false;
	}
	
	public void initialseBoat(){
		int startX = mStartPosition.getX();
		int startY = mStartPosition.getY();
		if(mHorizonzal){
			for(int i = 0; i<mLength; i++){
				mBoatPositions.add(new Position(startX , startY + i));
				mUnHitPositions.add(new Position(startX,  startY + i));
			}
		} else {
			for(int j = 0; j<mLength; j++){
				mBoatPositions.add(new Position(startX + j, startY));
				mUnHitPositions.add(new Position(startX + j, startY));
			}
		}
	}
	
	public String getName(){
		return mName;
	}
	
	public ArrayList<Position> getBoatPositions(){
		return mBoatPositions;
	}
	
	@Override
	public String toString(){
		return mName + ":\th: " + mHorizonzal + "(x,y): " + mStartPosition.toString();
	}
}
