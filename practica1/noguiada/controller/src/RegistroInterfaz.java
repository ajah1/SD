
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Define que pueden hacer los nodos Maseter.
 */
public interface RegistroInterfaz extends Remote { 

	public void registerSlave(SondaInterfaz slave) throws RemoteException;
}
