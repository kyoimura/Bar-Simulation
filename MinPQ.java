/*
 * priority queue class
 */
public class MinPQ <Key extends Comparable<Key>>
{
	private Key minPq[]; //priority queue
	private int N = 0; //helper variable
	
	/*
	 * constructor
	 * initializes priority queue with random size
	 */
	MinPQ()
	{
		minPq = (Key[]) new Comparable[10];	
	}
	
	//reverses element
	public void reverse(int a, int b)
	{
		Key temp = minPq[a];
		minPq[a] = minPq [b];
		minPq[b] = temp;
	}
	
	//calls compareTo, finds lesser one 
	public boolean less(int a, int b)
	{
		return minPq[a].compareTo(minPq[b]) < 0;
	}
	/*
	 * method that checks if elements need to be sorted 
	 * if bigger elements are on the top and small is on the bottom, reversees their position
	 */
	public void climbsHigher(int g)
	{
		while (g > 1 && less(g, g/2))
		{
			reverse(g/2, g);
			g = g/2;
		}
	}
	/*
	 * bigger element climbs down to correct position
	 * keeps moving down until elements have no smaller values under
	 */
	public void climbsLower(int g)
	{
		while(2*g <= N)
		{
			int c = 2*g;
			if(c < N && less(c+1, c)) c++;
			if(less(g,c))
			{
				break;
			}
			reverse(g, c);
			g=c;
		}
	}
	/*
	 * inserts new element into queue, sorts with climbsUp
	 */
	public  void insert(Key element)
	{
		if(isFull())
		{
			changeLength(minPq.length*2);
		}
		minPq[++N] = element;
		climbsHigher(N);

	}
	//returns lowest value
	public Key min()
	{
		return minPq[1];
	}
	//returns and deletes lowest value
	public Key deleteMin()
	{
		if(isEmpty())
		{
			System.out.println("Cannot delete from empty Queue");
			return null;
		}
		Key temp = minPq[1];
		reverse(1,N--);
		minPq[N+1] = null; 
		climbsLower(1);
		if(N == minPq.length/4)
		{
			changeLength(minPq.length/2);
		}
		return temp;
	}
	//increases size
	public void changeLength(int number)
	{
		Key temp[] = (Key[])new Comparable[number];
		System.arraycopy(minPq, 1, temp, 1, N);
		minPq = temp;
	}
	//checks if queue is full
	public boolean isFull()
	{
		return N == minPq.length-1;
	}
	//checks if queue is empty
	public boolean isEmpty()
	{
		return N == 0;
	}
	//cheks the size of queue
	public int size()
	{
		return N;
	}
	
}

