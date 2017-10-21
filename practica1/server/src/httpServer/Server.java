package httpServer;

import java.net.*;
import java.util.Scanner;

public class Server {

	
	// CREA UN SOCKET, EN UN HILO DISTINTO, PARA CADA UNA DE LAS
	// PETICIONES RECIBIDAS DEL CLIENTE (NAVEGADOR)
	public void abrirServer( int _ps, int _cc, int _pc, String _hc )
	{
		try
		{
			// SERVIDOR A LA ESPERA DE PETICIONES EN EL PUERTO _ps
			ServerSocket ss = new ServerSocket( _ps );
			System.out.println( "Escucho el puerto " + _ps );
	
			for(;;)
			{
				if ( _cc > 0)
				{
					_cc--;
					// SE ACEPTA UNA PETICIÓN
					Socket sc = ss.accept();
					System.out.println( "Cliente(" + (_cc + 1) +  ") aceptado" );
	
					// NUEVO THREAD PARA CADA PETICIÓN ACEPTADA
			        Thread t = new ServerHilo( sc, _pc, _hc );
			        t.start();
			        
			        System.out.println( "Cliente(" + (_cc + 1) +  ") cerrado" + "\n");
			        
			        _cc++;
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
	
	// MUESTRA EL MENÚ CON LO DATOS NECESARIOS PARA 
	// INICIAR EL SOCKET
	public static void main(String[] args) {
		
		Server sv= new Server();

		// VALORES POR DEFECTO
		int ps = 8080;
		int pc = 8090;
		int concurrencias = 5;
		
		String hostController = "localhost";

		Scanner sc = new Scanner( System.in );
		
		// PEDIR LOS DATOS
		System.out.println( "*** Asistente minihttpserver ***" + "\n" );
		
		System.out.print( "Puerto server http [pd 8080]: " );
		ps = sc.nextInt();
		
		System.out.print( "Concurrencias [pd 5]: " );
		concurrencias = sc.nextInt();
		
		System.out.print("Puerto controller [pd 8090]: ");
		pc = sc.nextInt();
		
		System.out.println("Host del controller [pd localhost] :");
		hostController = sc.nextLine();
		
		System.out.println( "\n" + "Abriendo server..." );
		sv.abrirServer( ps, concurrencias, pc, hostController );
		
		
	}
	
	
	
	
	
	

}
