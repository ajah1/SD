
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SondaMain {

	public static void main(String[] args) throws Exception 
	{
		// variables auxiliares
		String controllerHost = "localhost";
		int rmiport = 1099;

		String aux = "";
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		
		// perdir puerto escucha rmi
		System.out.println("*** Asistente SondaMain *** \n");
		System.out.print("Host del controller [pd localhost]: ");
		aux = br.readLine();
		if ( !aux.equals(""))
			controllerHost= aux;
		
		System.out.print("puerto del registro rmi [pd 1099]: ");
		aux = br.readLine();
		if ( !aux.equals(""))
			rmiport = Integer.parseInt(aux);
		
		final Registry registry = LocateRegistry.getRegistry(controllerHost, rmiport);
		
		
		int cont = 0;
		String tipo = "";
		int humedad = 0;
		int temperatura = 0;
		String fecha = "";
		
		// registra las sondas que queramos
		while ( true )
		{
			// pedir los datos de la sonda
			System.out.println(" \n Registrando sonda "+cont);
			
			System.out.print("Tipo de sonda [humedad/temperatura]: ");
			tipo = br.readLine();
			
			if ( tipo.equals("temperatura"))
			{
			System.out.print("Valor de la temperatura]: ");
			temperatura = Integer.parseInt( br.readLine() );
			}
			else
			{
			System.out.print("Valor de la humedad]: ");
			humedad = Integer.parseInt( br.readLine() );
			}
			fecha = "acordarse de hacer la fecha";
			
			// crear objeto sonda e inicializar el fichero de la sonda
			Sonda sonda = new Sonda(cont,tipo,humedad,temperatura,fecha);
			sonda.crearSonda("sonda"+cont);
	
			// registrar la sonda en rmi del controller
			System.out.println("Intentando registrar " 
								+ sonda.getRmiName()
								+ " en el registro remoto");
			final RegistroInterfaz master = (RegistroInterfaz) registry.lookup(Registro.RMI_NAME);
			master.registerSlave(sonda);
			
			cont++;
		}
	}
}
