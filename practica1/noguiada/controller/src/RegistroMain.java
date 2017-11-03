
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistroMain {

	public static void main(String[] args) throws IOException
	{
		// variables auxiliares
		int rmiport = 1099;
		String aux = "";
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		
		// pedir el puerto asociado al rmi
		System.out.println("*** Asistente RegistroMain *** \n");
		System.out.print("Puerto rmi [pd 1099]: ");
		aux = br.readLine(); 
		if ( !aux.equals(""))
			rmiport= Integer.parseInt(aux);
		
		br.close();
		
		// registrar objeto Registro, en el registro
		Registry registry = LocateRegistry.getRegistry(rmiport);

		System.out.println("registro obtenido");
		Registro master = new Registro(registry);
		System.out.println("REGISTRANDO");
		registry.rebind(Registro.RMI_NAME, master);
		System.out.println("registrado objeto de tipo Registro");

		// registrar tantas sondas como sean enviadas
		System.out.print("\n Esperando nuevas sondas a registrar... \n");
		while ( true )
		{
			RegistroMain masterMain = new RegistroMain();
			synchronized (masterMain) {
				try {
					masterMain.wait();
				} catch (InterruptedException e) {
					System.out.print("REGISTROMAIN. ERROR. ");
					e.toString();
				}
			}
		}
		
		
		
	}
}
