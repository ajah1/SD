package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Calendar;

public class Registro{

	// CLASE PARA AÑADIR EN EL REGISTRO LAS SONDAS QUE QUERAMOS
	// ESTAN SON CREADAS TRAS REGISTRARLAS
	@SuppressWarnings("deprecation")
	public static void main (String args[]) throws RemoteException, MalformedURLException
	{
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("*** REGISTRO DE SONDAS ***");
		
		// AÑADOR SONDAS POR DEFECTO
		try	
		{
			// SONDAS POR DEFECTO
			System.out.println("\n"+">creando sondas por defecto...");
			
			
			Sonda sonda0 = new Sonda(0,"humedad",40,0,"10/10/10");
			Sonda sonda1 = new Sonda(1,"humedad",40,0,"2/10/20");
			
			System.out.println("[rmi://localhost:1099/"+("sonda0")+"]");
			Naming.rebind("/sonda0", sonda0);
			System.out.println("[rmi://localhost:1099/"+("sonda1")+"]");
			Naming.rebind("/sonda1", sonda1);
			
			sonda0.crearSonda("sonda0");
			sonda1.crearSonda("sonda1");
			System.out.println("[Sondas (ficheros) creadas]");
			
			// PEDIR LAS SONDAS QUE SE QUIERA
			System.out.print(">Registrar mas sondas...: ");
			
			Sonda sonda;
			int cont = 2;
			String tipo = "";
			int humedad = 0;
			int temperatura = 0;
			String fecha = "";
			
			while(true)
			{
				System.out.println("REGISTRO SONDA NUMERO "+cont);
				
				System.out.println("Tipo de sonda [humedad/temperatura]");
				tipo = br.readLine();
				
				if ( tipo.equals("temperatura"))
				{
				System.out.print("Valor de la temperatura: ]");
				temperatura = Integer.parseInt( br.readLine() );
				}
				else
				{
				System.out.print("Valor de la humedad: ]");
				humedad = Integer.parseInt( br.readLine() );
				}
				fecha = Calendar.getInstance().toString();
				
				sonda = new Sonda(cont,tipo,humedad,temperatura,fecha);
				sonda.crearSonda("sonda"+cont);
				Naming.rebind("/sonda"+cont, sonda);
				cont++;
			}
			
		}
		catch ( Exception e)
		{
			System.out.println("ERROR. En el registro");
			System.out.println(e.toString());
		}
	}
	
}
