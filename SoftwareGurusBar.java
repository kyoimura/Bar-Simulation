
import java.util.Scanner;
import java.util.Vector;
/*
 * CIS 256 Project 3 
 * Kyo Imura
 * 
 * this class handles the events that happen at the bar
 */
public class SoftwareGurusBar {
	private int freeChairs= 50;  
	private double profit = 0.0;
	
	private Vector<Integer> beerProbability; //probability of the three types of beer (local, import, special)
	private Vector<Integer> groupSizeProbabilities; //probability of group size
	private SimulationFramework simulation = new SimulationFramework( ); 
	
	public static void main(String[] args) {
		SoftwareGurusBar world = new SoftwareGurusBar( );
	}
	/*
	 * constructor
	 * asks for user input for how many seats the user wants the bar to have
	 * runs the bar simulation
	 * finds the total profit
	 */
	SoftwareGurusBar( ){ 
		
		beerProbability =  new Vector<>();
		beerProbability.insertElementAt(15, 0 );beerProbability.insertElementAt(60, 1);beerProbability.insertElementAt(25, 2); // the percentage of the probability of the type of beer
		groupSizeProbabilities = new Vector<>();
		groupSizeProbabilities.insertElementAt(15, 0);groupSizeProbabilities.insertElementAt(25, 1);//the probability of the group sizes
		groupSizeProbabilities.insertElementAt(20, 2);groupSizeProbabilities.insertElementAt(20, 3);
		groupSizeProbabilities.insertElementAt(20, 4);
		
		Scanner scan = new Scanner(System.in);
		int t = 0; 
		System.out.println("Welcome to Bar Simulation! how many seats do you want to simulate?");
		freeChairs =scan.nextInt();
		scan.close();
		while (t < 240) { // simulate 4 hours of bar operation
			t += randBetween(2, 5); // new group every 2-5 minutes
			int groupSize = 1 + SimulationFramework.weightedProbability(groupSizeProbabilities, randBetween(1,100));
			simulation.scheduleEvent(new ArriveEvent(t, groupSize)); //the group at a specific time with a specific number in group
		} 
		simulation.run( );
		System.out.println("Total profits " + profit); 
	}
	
	
	/*
	 * returns true if the party that came into the bar can be seated
	 * returns false if the party that came in cannot be seated due to limited amt of chairs
	 */
	public boolean canSeat(int numberOfPeople)
    {
	      System.out.println("Group of "+ numberOfPeople +" customers arrive at time "+ simulation.time());
	      
	      if(numberOfPeople<freeChairs)
	      {
	    	 System.out.println("Group of " + numberOfPeople + " is seated");
	         freeChairs -= numberOfPeople;
	         return true;
	      }
	      else
	      {
	         System.out.println("No room, Group leaves.");
	         return false;
	      }
	 }
	/*
	 * method that orders the beer 
	 * adds the cost of beer to profit
	 */
	public void order (int beerType) {
	
		int beerType123 = beerType - 1;  //beer types 1, 2, and 3
		System.out.println("Serviced order for beer type " + beerType123 +" at time " + simulation.time( ));
	
		// update profit knowing beerType(left for you)}
		profit += (double) beerType;
	
	}
	/*
	 * 
	 * method that empties the chairs of the group that is leaving
	 */
	public void leave (int numberOfPeople) {
		System.out.println("Group of size " + numberOfPeople + " leaves at time "+ simulation.time( ));
		freeChairs += numberOfPeople;
	}
	/* method that generates a random number that is between the parameters low and high 
	 * 
	 */
	private int randBetween(int low, int high) {
		return low + (int) ((high -low + 1) * Math.random( ));
	}
	
	/*
	 * private class that deals with groups that arrive
	 */
	private class ArriveEvent extends Event {
		
		private int groupSize;
		
		/**
		 * the constructor handles the time and the group size of the people that arrives
		 * calls Event's constructor
		 * @param time
		 * @param gs
		 */
		ArriveEvent(int time, int gs) { 
			super(time);
			groupSize= gs;
		} 
		/**
		 * puts orderEvent object into simulation queue
		 * 
		 */
		public void processEvent( ){
			if (canSeat(groupSize)) {// place an order within 2 & 10 minutes
				simulation.scheduleEvent(new OrderEvent(time + randBetween(2,10),groupSize));
			}
		}
	}
	
	/**
	 * 
	 * private class that handles the ordering of the group
	 */
	private class OrderEvent extends Event {
		private int groupSize;
		/** 
		 * 
		 * constructor that calls Event's constructor using super
		 * @param time
		 * @param gs
		 */
	OrderEvent(int time, int gs) {
		super(time);
		groupSize= gs;
	}
		/**
		 * method that processes the order of the group members by loop
		 * finds if the group reorders or not by calling ReOrderEvent
		 */
	public void processEvent( ){// each member of the group orders a beer (type 1,2,3)
			for (int i= 0; i < groupSize; i++) {
				
				int orderNum = 2 + SimulationFramework.weightedProbability(beerProbability, randBetween(1,100));
				order(orderNum);
			}
			simulation.scheduleEvent (new ReOrderEvent(time+randBetween(30,60),groupSize));
		}
	}
	/*
	 * method deals with reorder
	 * if the probability of reordering is higher, group reorders
	 * decreases probability of reorder
	 * checks again
	 * 
	 */
	private class ReOrderEvent extends Event {
		private int groupSize;
		private int probability;
		
		//initial
		ReOrderEvent(int time, int gs) {
			super(time);
			groupSize = gs;
			probability = 50;
			
		}
		//to decrease probability of reorder
		ReOrderEvent(int time, int gs, int prob) {
			super(time);
			groupSize = gs;
			probability = prob;
		}
		
		public void processEvent() {
			if(randBetween(1, 100 ) < probability) {
					System.out.println("Another round for group of " + groupSize +" at time " + simulation.time( ));
					for(int i = 0; i < groupSize; i ++) {
						int orderNum = 2 + SimulationFramework.weightedProbability(beerProbability, randBetween(1,100));
						order(orderNum);
					}
					simulation.scheduleEvent (new ReOrderEvent(time + randBetween(30,60),groupSize, probability/2 ));
			} else {
				simulation.scheduleEvent (new LeaveEvent(time, groupSize));
			}
			
		}
	}
	/**
	 * private class that handles the leaving of the group
	 */
	private class LeaveEvent extends Event {
		private int groupSize;
		/**
		 * constructor that calls Event's constructor using super
		 * @param time
		 * @param gs
		 */
		LeaveEvent(int time, int gs) {
			super(time);
			groupSize = gs; 
		}
	
	//method that processes the leaving of the group
	public void processEvent( ){ 
			leave(groupSize); 
			}
	}
	
		
	
	
}


