package Model;
import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.AIException;
import Exceptions.GameNotSetupException;
import Exceptions.PositionAlreadyHitException;
import Exceptions.PositionOutOfBoardException;
import Exceptions.SquareNotEmptyException;
import Interfaces.BattleShipAI;
import Interfaces.Observable;
import Interfaces.Observer;


public class Game implements Observable{
	
	private ArrayList<Observer> mObservers;
	
	private Board mPlayerBoard;
	private Board mComputerBoard;

	private int mBoardSize;
	
	private boolean mGameOver;
	private boolean mPlayerWin;
	
	private boolean mGameSetup;
	
	private Position mLastComputerPos;
	//Strategy pattern for AI
	private BattleShipAI mComputerAI;
	
	public Game(int size, BattleShipAI computerAI) throws AIException{
		//Observer Patter initialisation
		mObservers = new ArrayList<Observer>(); 

		mComputerAI = computerAI;
		
		mGameOver = false;
		mPlayerWin = false;
		mBoardSize = size;
		mGameSetup = false;
		
		mPlayerBoard = new Board(size);
		mComputerBoard = new Board(size);
		
		mLastComputerPos = null;
		
		try {
			initialiseComputerBoard();
		} catch (SquareNotEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new AIException();
		}
	}
	
	public void initialiseComputerBoard() throws SquareNotEmptyException{
		Boat[] computerBoats = mComputerAI.getBoatsPosition(mBoardSize);
		for(Boat b : computerBoats){
			mComputerBoard.addBoat(b);
		}
	}
	
	public void addPlayerBoat(Boat b) throws SquareNotEmptyException{
		mPlayerBoard.addBoat(b);
		if(mPlayerBoard.getBoatsAlive() == 5){
			mGameSetup = true;
		}
	}
	
	public Boat Play(Position pos) throws PositionOutOfBoardException, GameNotSetupException, PositionAlreadyHitException{
		//returns the boat hit or null if no boat was hit
		if(!mGameSetup){
			throw new GameNotSetupException();
		}
		//update board with player shot
		Boat b = mComputerBoard.fire(pos);
		//update board with AI shot
		mLastComputerPos = mComputerAI.nextPos(mPlayerBoard);
		mPlayerBoard.fire(mLastComputerPos);
		
		//check if game is over, first Computer then Player
		if(mPlayerBoard.isGameOver()){
			mGameOver = true;
			mPlayerWin = false;
		}
		if(mComputerBoard.isGameOver()){
			mGameOver = true;
			mPlayerWin = true;
		}
		notifyObservers();
		return b;
	}
	
	
	public void addObserver(Observer o){
		mObservers.add(o);
	}
	
	public void removeObserver(Observer o){
		mObservers.remove(o);
	}
	
	public void notifyObservers(){
		for(Observer o : mObservers){
			o.stateChanged();
		}
	}
	
	public Board getPlayerBoard(){
		return this.mPlayerBoard;
	}
	
	
	public Board getComputerBoard(){
		return this.mComputerBoard;
	}
	
	public boolean isGameOver(){
		return mGameOver;
	}
	
	public boolean isPlayerWinner(){
		return mPlayerWin;
	}
	
	public int getSize(){
		return this.mBoardSize;
	}
	
	public Position getLastComputerPosition(){
		return mLastComputerPos;
	}
}
