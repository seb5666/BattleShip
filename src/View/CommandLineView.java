package View;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import AI.RandomAI;
import Exceptions.AIException;
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
	
	private Game currentGame;
	private Scanner sc = new Scanner(System.in);
	
	public CommandLineView() throws AIException {
		initialiseGame();
		System.out.println("Computer's boats");
		print(currentGame.getComputerBoard());
		initialiseBoats();
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
				currentGame = new Game(size, new RandomAI());
				currentGame.addObserver(this);
				gameCreated = true;
				
			} catch (NumberFormatException nfe){
				System.out.println("\nYou need to enter a postive integer");
			} 
		}	
	}
	
	private void initialiseBoats(){
		System.out.println("To add a boat input h/v for horizontal/vertical and x,y for the top/left position of the boat");  
		System.out.println("example: v,1,9");
		addBoat("carrier");
		addBoat("battleship");
		addBoat("cruiser");
		addBoat("submarine");
		addBoat("destroyer");
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
			System.out.println("Add your "+ boatName +": ");
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
				if(x<0 || x >= currentGame.getSize()){
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
				if(y<0 || y >= currentGame.getSize()){
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
					currentGame.addPlayerBoat(b);	
					System.out.println(currentGame.getPlayerBoard());
					carrierAdded = true;
				} catch (SquareNotEmptyException e) {
					System.out.println("You cannot overlap boats");
				}
			}
		}
	}

	private void print(Board board) {
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
				if(board.getBoatPositions().contains(new Position(x,y))){
					s += "b\t";
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
		if(currentGame.isGameOver()){
			if(currentGame.isPlayerWinner()){
				System.out.println("Well played, you win");
			} else {
				System.out.println("Sorry but you lost");
			}
			System.out.println("Computer's board");
			print(currentGame.getComputerBoard());
		} else {
			
		}
	}

}
