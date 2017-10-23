package controller;

import java.rmi.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Registro{

	@SuppressWarnings("deprecation")
	public static void main (String args[]) throws RemoteException, MalformedURLException
	{
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		
		//System.setSecurityManager(new RMISecurityManager());
		
		int humedad = 40; 
		String fecha = "12/12/12";
		
		System.out.println("*** REGISTRO DE SONDAS ***");
		
		// AÑADOR SONDAS POR DEFECTO
		try	
		{
			System.out.println("\n"+">creando sondas por defecto...");
			for ( int i = 0; i < 2; ++i )
			{
				Sonda sonda = new Sonda(i,"humedad",humedad,0,fecha);
				Naming.rebind("/sonda"+i, sonda);
				System.out.println("[rmi://localhost:1099/"+("sonda"+i)+"]");
				sonda.crearSonda("sonda"+i);
				
			}

			System.out.print(">Sonda personalizada...: ");
			System.out.print("[temperatura: ]");
			int temperatura = Integer.parseInt( br.readLine() );
			br.close();
			
			Sonda sonda = new Sonda(2,"temperatura",0,temperatura,fecha);
			sonda.crearSonda("sonda2");
			Naming.rebind("/sonda2", sonda);
			System.out.println("[rmi://localhost:1099/"+"/sonda2"+"]");
			
		}
		catch ( Exception e)
		{
			System.out.println("ERROR. En el registro");
			System.out.println(e.toString());
		}
	}
	
}
