/*
 * class keeps track of time
 * implements comparable
 */
public abstract class Event implements Comparable <Event>
{
	public final int time;
	
	public Event (int t) {
		time = t;
	}
	
	abstract void processEvent( );
	/*
	 * compares event
	 * determines by time
	 */
	public int compareTo(Event e) {
		Event right =  e;
		if (time < right.time)  {
			return -1;
		}
		if (time == right.time) {
			return 0;
		}
		return 1;
	}

}
