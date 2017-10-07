package httpServer;

import java.io.*;
import java.net.*;

public class Server {

	
	// Abrir socket
	public void abrirServer( int _puerto )
	{
		
		// comprobaciones parametros
		// esperar peticion
		// abrir en hilo distinto
		
		try
		{
			ServerSocket ss = new ServerSocket( _puerto );
			System.out.println( "Escucho el puerto" + _puerto );
	
			for(;;)
			{
				Socket sc = ss.accept();
		        System.out.println( "Sirviendo cliente..." );

		        Thread t = new ServerHilo( sc );
		        t.start();
			}
		}
		catch ( Exception e )
		{
			System.out.print ( "ERROR: al abrir server" );
			System.out.println ( e.toString() );
			
		}
		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
