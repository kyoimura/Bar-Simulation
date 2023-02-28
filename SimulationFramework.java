import java.util.Vector;
//class runs simulation 
public class SimulationFramework {

	private int currentTime = 0;
	
	//returns current time
	public int time () {
		return currentTime; 
	}
	//the priority queue
	private MinPQ<Event> eventQueue = new MinPQ<Event>();

	//adds newEvent to the queue
	public void scheduleEvent(Event newEvent) 
	{
		eventQueue.insert(newEvent);
	}
	/*
	 * processes all Events
	 * basically starts business
	 */
	public void run()
   	{
      while(!eventQueue.isEmpty())
      {
         Event nextEvent = (Event) eventQueue.deleteMin();
         currentTime = nextEvent.time;
 
         nextEvent.processEvent();
      }
   }
	/*
	 *  
	 * method that returns the index of where num fits
	 * inside total of the values added up
	 */
	public static int weightedProbability(Vector<Integer> values, int num) 
	   {

	      int total = 0;
	      for(int j=0; j<values.size(); j++)
	      {
	        total += values.get(j);
	         if(num<= total)
	            return j;
	      }
	      return -1;
	   }
}


 
