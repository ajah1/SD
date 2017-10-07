package httpServer;

import java.io.*;
import java.net.*;

public class ServerHilo extends Thread{

	private Socket socketCliente;
	
	public ServerHilo ( Socket _sc )
	{
		this.socketCliente = _sc;
	}	
	
	/* 
	 * CLIENTE:
	 * Lee el socket escrito por el cliente
	 */
	public String leerCliente ( Socket _sc, String _buffer )
	{
		try
		{
			InputStream aux = _sc.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			
			_buffer = new String();
			_buffer = flujo.readUTF();
			
		}
		catch ( Exception e )
		{
			System.out.print( "ERROR: al leer socket cliente" );
			System.out.println( e.toString() );
		}
		
		return _buffer;
	}
	
	/*
	 * CLIENTE:
	 * Escribe en el socket del cliente
	 * 
	 */
	public void escribirCliente( Socket _sc, String _buffer )
	{
		try
		{
			OutputStream aux = _sc.getOutputStream();
			DataOutputStream flujo = new DataOutputStream( aux );
			
			flujo.writeUTF( _buffer );
		}
		catch ( Exception e )
		{
			System.out.print( "ERROR: al escribir en el cliente" );
			System.out.println( e.toString() );
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
