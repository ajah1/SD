import java.rmi.Remote;
import java.rmi.RemoteException;
/*
	interfaz del objeto a llamar
*/

public interface Counter extends Remote
{
	int dec() throws RemoteException;
	
	int inc() throws RemoteException;
	
	int getCount() throws RemoteException;
}
