package httpServer;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHilo extends Thread{

	private Socket socketCliente;
	
	public ServerHilo ( Socket _sc )
	{	
		socketCliente = _sc;
		this.run();
	}
	
	/* 
	 * Lee el socket escrito por el cliente
	 */
	public String leerSocket ( Socket _sc, String _buffer )
	{
		try
		{
			InputStream aux = _sc.getInputStream();
			
			BufferedReader entrada = new BufferedReader(new InputStreamReader(aux));;
			
			_buffer = new String();
			
			while ( entrada.ready() )
			{
				_buffer = _buffer.concat(entrada.readLine() + "\n"); 
			}
			
			return _buffer;
			
		}
		catch ( Exception e )
		{
			System.out.print( "ERROR: al leer socket cliente" );
			System.out.println( e.toString() );
		}
		
		return "";
	}
	
	/*
	 * Escribe en el socket del cliente,
	 * los datos pasados por parámetro
	 */
	public void escribirCliente( Socket _sc, String _buffer )
	{
		try
		{
            OutputStream aux = _sc.getOutputStream();
            DataOutputStream flujo = new DataOutputStream(aux);
            
            flujo.writeUTF( _buffer );
            
		}
		catch ( Exception e )
		{
			System.out.print( "ERROR: al escribir en el cliente" );
			System.out.println( e.toString() );
		}
	}
	
	/*
	 * lee del string el fichero estático a devoler
	 * si no lo encuentra muestra envia error 404
	 */
	public void enviarEstatico( String [] cadena )
	{
		if ( cadena.length == 0 || cadena[1].equals("index.html") )
		{
			System.out.println("preparando http para index..");
			
			System.out.println("http enviado");
		}
			
	}
	
	public void enviarDinamico( String [] cadena )
	{
		System.out.println("cosas dinámicas :O");
	}
	
	
	public void procesarPeticion ()
	{
		String aux1 = "";
		String buffer = "";
		
		buffer = this.leerSocket(socketCliente, buffer);
		
		if ( !buffer.isEmpty() )
		{
			StringTokenizer s = new StringTokenizer( buffer );

			// guardamos tipo de comando
			aux1 = s.nextToken().toString();
			System.out.println("COMANDO: " + aux1 );
			
			if ( aux1.equals("GET") )
			{
				aux1 = s.nextToken().toString();
				System.out.println("URL: " + aux1);
				
				String [] cadena = aux1.split("/");

				if ( cadena.length == 0 )
					enviarEstatico( cadena );
				
				else if ( !cadena[1].equals("controladorSD") )
					enviarEstatico( cadena );
				
				else
					enviarDinamico( cadena );
					
			}
		}
	}
	/*
	 * Cierra el cliente
	 */
	public void cerrarCliente()
	{
        try 
        {
            socketCliente.close();
        }
        catch (IOException ex1)
        {
            Logger.getLogger(ServerHilo.class.getName()).log(Level.SEVERE, null, ex1);
        }
	}
	
	/*
	 * Iniciar petición :O
	 */
	public void run()
	{
		/*
		String tramaHttp = "";
		tramaHttp = this.leerSocket(socketCliente, tramaHttp);	
		System.out.println( tramaHttp );
		*/
		
		this.procesarPeticion();
		
		//this.cerrarCliente();

	}
	
}



