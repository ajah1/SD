 import java.net.MalformedURLException;
 import java.rmi.Naming;
 import java.rmi.RemoteException;
 
 public class Cliente
 {
 	public static void main(String[] args)
 		throws RemoteException, MalformedURLException, InterruptedException
 	{
 		Counter = new CounterImpl();
 		
 		Naming.rebind("rcounter", counter);
 		
 		for (int i = 0; i < 1000; ++i)
 		{
 			System.out.println(String.format("Cliente loop number: %d. Counter value: %d.", i, counter.getCount()));
 			Thread.sleep(1000);
 		}
 	}
 }
