
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Define que pueden hacer los nodos Slave.
 */
public interface SondaInterfaz extends Remote {

	public String getRmiName() throws RemoteException;
	
	public int getid()throws RemoteException;
	public int getTemp()throws RemoteException;
	public int getHumedad()throws RemoteException;
	public String getTipo()throws RemoteException;
	public String getFecha()throws RemoteException;
	
	public void crearSonda(String nombre)throws RemoteException;
	public String leerFichero( String nombre, String dato ) throws Exception, RemoteException;
	
	public boolean getActuador() throws RemoteException, NumberFormatException, Exception;
	public void ModificarDato ( ) throws RemoteException;
}
