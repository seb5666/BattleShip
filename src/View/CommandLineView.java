package View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import AI.RandomAI;
import Exceptions.AIException;
import Exceptions.GameNotSetupException;
import Exceptions.PositionAlreadyHitException;
import Exceptions.PositionOutOfBoardException;
import Exceptions.SquareNotEmptyException;
import Interfaces.Observer;
import Model.BattleShipBoat;
import Model.Board;
import Model.Boat;
import Model.CarrierBoat;
import Model.CruiserBoat;
import Model.DestroyerBoat;
import Model.Game;
import Model.Position;
import Model.SubmarineBoat;

public class CommandLineView implements Observer {
	
	private Game mCurrentGame;
	private Scanner sc = new Scanner(System.in);
	
	public CommandLineView() throws AIException {
		initialiseGame();
		initialisePlayerBoats();
		startGame();
	}

	private void startGame() {
		while(true){  //game loop, break out when game over
			System.out.println("\n\n----------\nOpponent's board:");
			printOpponentBoard();
			if(mCurrentGame.isGameOver()){
				if(mCurrentGame.isPlayerWinner()){
					System.out.println("Well played, you win");
				} else {
					System.out.println("Sorry but you lost");
				}
				System.out.println("Computer's board");
				printOpponentBoard();
				System.exit(0);
			} else {
				boolean correctNextPosition = false;
				while(!correctNextPosition){
					System.out.println("Where do you want to fire \"x,y\": ");
					String[] input = sc.nextLine().split(",");
					if(input.length != 2){
						System.out.println("You need to enter a position in the \"x,y\" format");
					} else {
						int x =0;
						int y =0;
						Boat b = null;
						try{
							x = Integer.parseInt(input[0]);
							y = Integer.parseInt(input[1]);
							b = mCurrentGame.Play(new Position(x,y));
							correctNextPosition = true;
						} catch (NumberFormatException nfe) {
							System.out.println("Please enter two integers between 0 and the size of the board");
							correctNextPosition = false;
						} catch (PositionOutOfBoardException e) {
							System.out.println("The position must be inside of the board");
							correctNextPosition = false;
						} catch (GameNotSetupException e) {
							e.printStackTrace();
							System.exit(0);
						} catch (PositionAlreadyHitException e) {
							System.out.println("You already hit this position");
							correctNextPosition = false;
						} finally{
							if(correctNextPosition){
								if(b != null){
									System.out.println("You hit a " + b.getName());
								} else {
									System.out.println("MISS");
								}
								
								System.out.println("Press any key to continue");
								sc.nextLine();
								
								System.out.println("The computer fired at " + mCurrentGame.getLastComputerPosition());
								System.out.println("Your board: (b is an unhit boat; H is a hit boat and x is hit water)");
								printPlayerBoard();
								System.out.println("Press any key to continue");
								sc.nextLine();
							}
						}
					}
				}
			}
		}

	}

	private void initialiseGame() throws AIException {
		int size = 0;
		boolean gameCreated = false;
		while(!gameCreated){
			try{
				System.out.println("Please enter the size of the board (min 8)");
				String input = sc.nextLine();
				size = Integer.parseInt(input);
				while(size <= 0 || size < 8){
					System.out.println("\nYou need to enter a positive integer bigger then 8");
					size = Integer.parseInt(sc.nextLine());
				}
				mCurrentGame = new Game(size, new RandomAI());
				mCurrentGame.addObserver(this);
				gameCreated = true;
				
			} catch (NumberFormatException nfe){
				System.out.println("\nYou need to enter a postive integer");
			} 
		}	
	}
	
	private void initialisePlayerBoats(){
		System.out.println("To add a boat input h/v for horizontal/vertical and x,y for the top/left position of the boat");  
		System.out.println("example: v,1,9");
		addBoat("carrier");
		addBoat("battleship");
		addBoat("cruiser");
		addBoat("submarine");
		addBoat("destroyer");
		
		System.out.println("Your board:");
		print(mCurrentGame.getPlayerBoard());
		System.out.println("Press any key to start playing");
		while(true){
			sc.nextLine();
			break;
		}
	}
	
	private void addBoat(String boatName){
		
		boolean horizontal = false;
		int x = 0;
		int y = 0;
		
		boolean carrierAdded = false;
		boolean correctXInput = false;
		boolean correctYInput = false;
		boolean correctHVInput = false;

		while(!carrierAdded) {
			correctHVInput = false;
			correctXInput = false;
			correctYInput = false;
			System.out.println("\nAdd your "+ boatName +": ");
			String s = sc.nextLine();
			String[] initial = s.split(",");
			
			//More or less then 3 inputs
			if(initial.length != 3) { 
				System.out.println("\nYou must enter in the format \"v,1,1\")");
				continue;
			}
			
			//Get x coordinate
			try {
				x = Integer.parseInt(initial[1]);
				if(x<0 || x >= mCurrentGame.getSize()){
					throw new NumberFormatException();
				} else {
					correctXInput = true;
				}
			} catch(NumberFormatException nfe){
				System.out.println("X position must be an integer between 0 inclusive and the board size exclusive");
			}
			
			//Get y coordinate
			try {
				y = Integer.parseInt(initial[2]);
				if(y<0 || y >= mCurrentGame.getSize()){
					throw new NumberFormatException();
				} else {
					correctYInput = true;
				}
			} catch(NumberFormatException nfe){
				System.out.println("Y position must be an integer between 0 inclusive and the board size exclusive");
			}
			
			//Horizontal/Vertical input error
			if (!initial[0].equals("v") && !initial[0].equals("h")){
				System.out.println("Please enter v or h to indicate if you want to place your boat horizontally");
			} else {
				//Correct input
				if(initial[0].equals("h")){
					horizontal = true;
				}
				correctHVInput = true;
			}		
			
			if(correctHVInput && correctXInput && correctYInput){
				//Add the boat
				try {
					Boat b = null;
					if (boatName.equals("carrier")) {
						b = new CarrierBoat(new Position(x,y), horizontal);
					}
					else if (boatName.equals("battleship")) {
						b = new BattleShipBoat(new Position(x,y), horizontal);
					}
					else if (boatName.equals("cruiser")) {
						b = new CruiserBoat(new Position(x,y), horizontal);
					}
					else if (boatName.equals("submarine")) {
						b = new SubmarineBoat(new Position(x,y), horizontal);
					}
					else if (boatName.equals("destroyer")) {
						b = new DestroyerBoat(new Position(x,y), horizontal);
					}
					mCurrentGame.addPlayerBoat(b);	
					carrierAdded = true;
				} catch (SquareNotEmptyException e) {
					System.out.println("You cannot overlap boats");
				}
			}
		}
	}

	private void printPlayerBoard(){
		String s="Y\\X \t";
		HashMap<Position, Boat> boatsHit = mCurrentGame.getPlayerBoard().getBoatHitPositions();
		ArrayList<Position> boatsPositions = mCurrentGame.getPlayerBoard().getBoatPositions();
		int boardSize = mCurrentGame.getPlayerBoard().getSize();
		
		for(int i=0; i < boardSize; i++){
			//top row
			s += i + "\t"; 
		}
		
		Position p = null;
		for(int y=0; y < boardSize; y++){
			s += "\n";
			s += y + "\t";
			for(int x=0; x < boardSize; x++){
				Boat b = null;
				p = new Position(x, y);
				if(boatsHit.keySet().contains(p)){
					b = boatsHit.get(p);
					if(b != null ){
						s += "H\t";
					}
				}
				else if(boatsPositions.contains(p)){
					b = mCurrentGame.getPlayerBoard().boatAt(p);
					if(b != null ){
						s += "b\t";
					}
				}
				else { //no boat hit
					if(mCurrentGame.getPlayerBoard().isPositionHit(p)){
						s += "x\t";
					} else {
						s += "~\t";
					}
				}
			}
		}
		System.out.println(s);
	}
	
	private void printOpponentBoard(){
		String s="Y\\X \t";
		HashMap<Position, Boat> boatsHit = mCurrentGame.getComputerBoard().getBoatHitPositions();
		int boardSize = mCurrentGame.getComputerBoard().getSize();
		
		for(int i=0; i < boardSize; i++){
			//top row
			s += i + "\t"; 
		}
		
		Position p = null;
		for(int y=0; y < boardSize; y++){
			s += "\n";
			s += y + "\t";
			for(int x=0; x < boardSize; x++){
				
				p = new Position(x, y);
				if(boatsHit.keySet().contains(p)){
					Boat b = boatsHit.get(p);
					if(b != null ){
						if (b instanceof CarrierBoat){
							s += "C";
						} 
						else if (b instanceof BattleShipBoat){
							s += "b";
						}
						else if (b instanceof CruiserBoat){
							s += "c";
						}
						else if (b instanceof SubmarineBoat){
							s += "s";
						} 
						else if (b instanceof DestroyerBoat){
							s += "d";
						}
						s+="\t";
					}
				} else { //no boat hit
					if(mCurrentGame.getComputerBoard().isPositionHit(p)){
						s += "x\t";
					} else {
						s += "~\t";
					}
				}
			}
		}
		
		System.out.println(s);
	}
	
	private void print(Board board){
		String s="Y\\X \t";
		int boardSize = board.getSize();
		for(int i=0; i < boardSize; i++){
			//top row
			s += i + "\t"; 
		}
		s += "\n";
		for(int y=0; y < boardSize; y++){
			s += y + "\t";
			for(int x=0; x < boardSize; x++){
				Boat b = board.boatAt(new Position(x,y));
				if(b != null ){
					if (b instanceof CarrierBoat){
						s += "C";
					} 
					else if (b instanceof BattleShipBoat){
						s += "b";
					}
					else if (b instanceof CruiserBoat){
						s += "c";
					}
					else if (b instanceof SubmarineBoat){
						s += "s";
					} 
					else if (b instanceof DestroyerBoat){
						s += "d";
					}
					s+="\t";
				} else {
					s += "~\t";
				}
			}
			s += "\n";
		}
		
		System.out.println(s);
	}
	
	@Override
	public void stateChanged() {
	}

}
