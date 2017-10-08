package httpServer;

import java.net.*;
import java.util.Scanner;

public class Server {

	
	// Abrir socket
	@SuppressWarnings("deprecation")
	public void abrirServer( int _puerto, int _cc )
	{
		try
		{
			ServerSocket ss = new ServerSocket( _puerto );
			System.out.println( "Escucho el puerto " + _puerto );
	
			for(;;)
			{
				if ( _cc > 0)
				{
					_cc--;
					Socket sc = ss.accept();
			        System.out.println( "Cliente(" + (_cc + 1) +  ") aceptado" );
	
			        Thread t = new ServerHilo( sc );
			        t.start();
			        System.out.println( "Cliente(" + (_cc + 1) +  ") cerrado" );
			        _cc++;
			        t.stop();
				}
				else
					System.out.println( "Cliente rechazado :O" );
			}
		}
		catch ( Exception e )
		{
			System.out.println ( "ERROR: al abrir server" );
			System.out.println ( e.toString() );
		}
	}
	
	public static void main(String[] args) {
		
		Server sv= new Server();

		int puerto = 8080;
		int concurrencias = 5;

		Scanner sc = new Scanner( System.in );
		
		System.out.println( "*** Asistente minihttpserver ***" + "\n" );
		
		System.out.print( "Puerto [pd 8080]: " );
		puerto = sc.nextInt();
		
		System.out.print( "Concurrencias [pd 5]: " );
		concurrencias = sc.nextInt();
		
		System.out.println( "\n" + "Abriendo server..." );
		sv.abrirServer( puerto, concurrencias );
		
		
	}
	
	
	
	
	
	

}
