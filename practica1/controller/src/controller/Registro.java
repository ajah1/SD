package controller;

import java.rmi.*;
import java.net.*;

public class Registro{

	@SuppressWarnings("deprecation")
	public static void main (String args[]) throws RemoteException, MalformedURLException
	{
		String nombre = "";
		
		try
		{
			/*
			System.out.println("preparando...");
			for ( int i = 0; i < 2; ++i )
			{
				System.out.println(i);
				Sonda objetoRemoto = new Sonda();
				
				nombre = "/sonda"+i;
				
				Naming.rebind(nombre, objetoRemoto);
				
				System.out.println("Objeto del servidor preparado");
			}
			
						
			int tam = Naming.list("rmi://localhost:1099").length;
			System.out.println("desvinculando...");
			for ( int i = 0; i < tam; ++i )
			{
				nombre = "/sonda"+i;
				Naming.unbind(nombre);
			}
			*/
			Sonda objetoRemoto = new Sonda();
			
			nombre = "/sonda0";
			
			Naming.rebind(nombre, objetoRemoto);
			
			System.out.println("Objeto del servidor preparado");
		
		}
		catch ( Exception e)
		{
			System.out.println("ERROR. En el registro");
			System.out.println(e.toString());
		}
	}
	
}
