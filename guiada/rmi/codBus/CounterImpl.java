import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;

public class CounterImpl extends UnicastRemoteObject 
						 implements Counter, Serializable
{
 	private static final long seralVersionID = 1L;
 
 	private int count;

	public CounterImpl() throws RemoteException
	{
		count = 0;
	}

	public int dec()
	{
		count--;
		return count;
	}

	public int inc()
	{
		count++;
		return count;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}
