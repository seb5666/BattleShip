package Model;

public class Position implements Comparable<Position>, Cloneable{
	private int mX;
	private int mY;
	
	public Position(int x, int y){
		mX = x;
		mY = y;
	}
	
	public int getX(){
		return mX;
	}
	
	public int getY(){
		return mY;
	}

	@Override
	public int compareTo(Position o) {
		if(this.mX == o.mX){
			return this.mY - o.mY;
		}
		return this.mX - o.mX;
	}
	
	@Override
	public boolean equals(Object p){
		return (this.mX == ((Position) p).mX && this.mY == ((Position) p).mY) ;
	}
	
	//might be useful later
	@Override
	public Position clone() throws CloneNotSupportedException{
		return (Position) super.clone();	
	}
	
	@Override 
	public String toString(){
		return "(" + mX + "," + mY + ")";
	}
}
