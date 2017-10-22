package httpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class Server {

	
	// CREA UN SOCKET, EN UN HILO DISTINTO, PARA CADA UNA DE LAS
	// PETICIONES RECIBIDAS DEL CLIENTE (NAVEGADOR)
	public void abrirServer( int _ps, int _cc, int _pc, String _hc )
	{
		try
		{
			// SERVIDOR A LA ESPERA DE PETICIONES EN EL PUERTO _ps
			ServerSocket ss = new ServerSocket( _ps );
			System.out.println( ">Escucho el puerto " + _ps );
	
			for(;;)
			{
				if ( _cc > 0)
				{
					_cc--;
					// SE ACEPTA UNA PETICIÓN
					Socket sc = ss.accept();
					System.out.println( "\n"+"==>Cliente(" + (_cc + 1) +  ") aceptado" );
	
					// NUEVO THREAD PARA CADA PETICIÓN ACEPTADA
			        Thread t = new ServerHilo( sc, _pc, _hc );
			        t.start();
			        
			        System.out.println( "==>Cliente(" + (_cc + 1) +  ") finaliza");
			        
			        _cc++;
				}
				else
					System.out.println( "Cliente rechazado concurrencias al maximo" );
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
	public static void main(String[] args) throws IOException {
		
		Server sv= new Server();

		// DATOS POR DEFECTO LAS CONEXIONES
		int ps = 8080;
		int pc = 8090;
		int concurrencias = 6;
		
		String hostController = "localhost";
		String aux = "";
		
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		// PEDIR LOS DATOS
		System.out.println( "*** ASISTENTE MINIHTTPSERVER ***" + "\n" );
		
		System.out.print( ">Puerto server http [pd 8080]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			ps = Integer.parseInt(aux);
		
		System.out.print( ">Concurrencias [pd 6]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			concurrencias = Integer.parseInt(aux);
		
		System.out.print(">Puerto controller [pd 8090]: ");
		aux = br.readLine();
		if ( !aux.equals(""))
			pc = Integer.parseInt(aux);
		
		System.out.println(">Host del controller [pd localhost] :");
		aux = br.readLine();
		if ( !aux.equals(""))
			hostController = aux;
		
		System.out.println( "Datos a usar en el server..." );
		System.out.println("puertoServer: " + ps);
		System.out.println("concurrencias: " + concurrencias);
		System.out.println("puertoController: " + pc);
		System.out.println("hostController: " + hostController);
		
		System.out.println( "\n" + ">Abriendo server..." );
		sv.abrirServer( ps, concurrencias, pc, hostController );
		
	}
	
	
	
	
	
	

}
