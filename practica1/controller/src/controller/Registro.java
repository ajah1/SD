package controller;

import java.rmi.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Registro{

	@SuppressWarnings("deprecation")
	public static void main (String args[]) throws RemoteException, MalformedURLException
	{
		String nombre = "";
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		String resp = "";
		
		System.out.println("*** REGISTRO DE SONDAS ***");
		try
		{
			System.out.println("\n"+">creando sondas por defecto...");
			for ( int i = 0; i < 2; ++i )
			{
				Sonda objetoRemoto = new Sonda();
				nombre = "/sonda"+i;
				Naming.rebind(nombre, objetoRemoto);
				
				System.out.println("[rmi://localhost:1099/"+nombre+"]");
			}
		
			int j = 2;
			System.out.print(">Añadir otra sonda? [no/yes]: ");
			resp = br.readLine();
			
			while ( resp.equals("yes") )
			{
				Sonda objetoRemoto = new Sonda();
				nombre = "/sonda"+j;
				Naming.rebind(nombre, objetoRemoto);
				System.out.println("[rmi://localhost:1099/"+nombre+"]");
				j++;
				
				System.out.println("\n"+">Añadir otra sonda? [no/yes]: ");
				resp = br.readLine();
			}
		}
		catch ( Exception e)
		{
			System.out.println("ERROR. En el registro");
			System.out.println(e.toString());
		}
	}
	
}
