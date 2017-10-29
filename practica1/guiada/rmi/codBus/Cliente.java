import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Cliente
{
	public static void main(String[] args)
		throws NotBoundException, RemoteException, MalformedURLException, InterruptedException
	{
		Counter counter;
		counter = (Counter) Naming.lookup("//localhost/rcounter");
		
		for (int i = 0; i < 1000; ++i)
		{
			counter.inc();
			System.out.println(String.format("Servidor loop number: %d.", i));
			Thread.sleep(1000);
		}
	}
}
