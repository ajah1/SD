package httpServer;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHilo extends Thread{

	private Socket socketCliente;
	private Socket socketController;
	private int puertoController;
	private String hostController;
	private String comando;
	
	public ServerHilo ( Socket _sc, int _pc, String _hc )
	{	
		socketCliente = _sc;
		puertoController = _pc;
		hostController = _hc;
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
				
				br = new BufferedReader( new FileReader( archivo ) );
				
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
	
    
   public void escribeSocket(Socket sk, String datos) 
   {
       try 
       {
           OutputStream aux = sk.getOutputStream();
           DataOutputStream flujo = new DataOutputStream(aux);
           flujo.writeUTF(datos);
       } 
       catch (Exception e) 
       {
           System.out.println("No se ha podido conectar con el controlador: " + e.toString());
       }
   }
	
	public void enviarDinamico( String [] cadena )
	{
		System.out.println("cosas dinámicas :O");
		System.out.println("Contectando al socket del controller...");
		try
		{
			// conectar con el socket del controller
			this.socketController = new Socket(this.hostController, this.puertoController);
			
			String datos = "invernadero=1@sonda=1";
			this.escribeSocket(socketController, this.comando);
			
			System.out.println("Peticion escrita en socket del controller");
			this.socketController.close();
			
		}
		catch ( Exception e)
		{
			System.out.println("ERROR: conectando con el controller");
			System.out.println(e.toString());
		}
		
		System.out.println("Conectado");
		
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
				this.comando = aux1;
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
	 * Iniciar petición :O
	 */
	public void run()
	{
		this.procesarPeticion();
	}
	
}



