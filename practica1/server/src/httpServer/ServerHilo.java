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
	 * lee del string el fichero estático a devoler
	 * si no lo encuentra muestra envia error 404
	 */
	public void enviarEstatico( String [] cadena )
	{
		try 
		{
			PrintWriter salida;
			salida = new PrintWriter( socketCliente.getOutputStream() );
			
			BufferedReader br;
			
			String datos = "";
			String archivo = "./";
			
			if ( cadena.length == 0 || cadena[1].equals("index.html") )
			{
				//System.out.println("preparando http para index..");
					
				archivo = archivo.concat("index.html");
				
				br = new BufferedReader( new FileReader( archivo ));
				
				salida.println("HTTP/1.1 200 OK");
				salida.println("Content-Type: text/html; charset=utf-8");
				salida.println("Server: MyHTTPServer");
				
				salida.println("");
				
				datos = br.readLine();
				while ( datos != null )
				{
					salida.println( datos );
					datos = br.readLine();
				}
				
				salida.flush();
				//salida.close();
				
				//System.out.println("http enviado");
			}
			else
			{
				archivo = archivo.concat("error.html");
				
				br = new BufferedReader( new FileReader( archivo ));
				
				salida.println("HTTP/1.1 404 Not Found");
				salida.println("Content-Type: text/html; charset=utf-8");
				salida.println("Server: MyHTTPServer");
				
				salida.println("");
				
				datos = br.readLine();
				while ( datos != null )
				{
					salida.println( datos );
					datos = br.readLine();
				}
				
				salida.flush();
				//salida.close();
			}
			
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: al enviar estático");
			e.printStackTrace();
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
			//System.out.println("COMANDO: " + aux1 );
			
			if ( aux1.equals("GET") )
			{
				aux1 = s.nextToken().toString();
				//System.out.println("URL: " + aux1);
				
				String [] cadena = aux1.split("/");

				if ( cadena.length == 0 || !cadena[1].equals("controladorSD") )
					enviarEstatico( cadena );

				else
				{
					System.out.println("ENTRAR EN DINAMICO");
					enviarDinamico( cadena );
				}
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
        catch (IOException e)
        {
            //Logger.getLogger(ServerHilo.class.getName()).log(Level.SEVERE, null, ex1);
            System.out.println( "ERROR: cerra socketCliente en ServerHilo" );
            System.out.println( e.toString() );
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



