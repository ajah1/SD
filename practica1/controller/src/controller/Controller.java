package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.StringTokenizer;


// GENERA EL CONTENIDO DINÁMICO, 
// LEE LOS VALORES DE LAS SONDAS
public class Controller {
	
	private String hosthttp;
	private int portrmi;
	private int porhttp;
	private String url;
	private String peticion;
	
	// LEE LOS DATOS ESCRITOS EN EL SOCKET ENTRE SERVER
	// Y CONTROLADOR
	public String leeSocket(Socket _ss, String _buffer) 
	{
        try 
        {
            InputStream aux = _ss.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);
            
            _buffer = new String();
            _buffer = flujo.readUTF();
        } 
        catch (Exception e) 
        {
            System.out.println("Error al leer socket controller: ");
            System.out.println(e.toString());
        }
        return _buffer;
    }
	
	// ESCRIBE EN EL SOCKET ENTRE SERVER Y CONTROLADOR
    public void escribeSocket(Socket _ss, String _buffer) 
    {
        try 
        {
            PrintWriter out= new PrintWriter(_ss.getOutputStream());
            
            out.println(_buffer);
            out.flush();
            out.close();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR al escribir en socket del controller: ");
            System.out.println( e.toString() );
        }
        return;
    }
	
    // LLAMA A LOS OBJETOS REMOTOS (SONDAS)
    // PARA OBTENER LOS DATOS PEDIDOS
	@SuppressWarnings("deprecation")
	public void procesarPeticion()
    {
    
    	String [] datos = this.url.split("/");
    	
    	for ( String d : datos)
    		System.out.println(d);
    	
    	System.out.println("preparando conexión rmi");
    	// rmi://localhost:1099
    	String host= "localhost";
    	int port = 1099;
    	String servidor = "rmi://" + host + ":" + port;
        System.out.println("Servidor:" + servidor);
        
        String servidorConcreto = servidor.concat("/" + "sonda1");
        System.out.println("Objeto:" + servidorConcreto);
        
        try
        {
        	 System.setSecurityManager(new RMISecurityManager());
        	 
        	 String names [];
        	 
        	 System.out.println("obtener nombres objetos remotos");
        	 names = Naming.list(servidor);
        	 
        	 for ( String n : names)
        	 {
        		 System.out.println(n);
        	 }
        	 
        }
        catch( Exception e)
        {
        	System.out.println("ERROR. procesar peticion en controller");
        	System.out.println(e.toString());
        }
    }
    
    // ABRE UN SOCKET A LA ESCUCHA DE PETICIONES
    // DEL SERVER,
    public void abrirServer()
    {
    	try 
    	{
			ServerSocket ss = new ServerSocket(this.portrmi);
			System.out.println("Abrir server rmi, escuchando");
			
			for (;;)
			{
				
				Socket sc = ss.accept();
				System.out.println("Aceptado clientehttp");
				
				this.url = this.leeSocket(sc, url);
				
				System.out.println("URL: "+ this.url);
				
				System.out.println("cerrando petición atendida :O");
				
				sc.close();
				this.procesarPeticion();
			
			}
		} 
    	catch (IOException e) 
    	{
			System.out.println("ERROR: abrirServer");
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) 
    {
    	
    	Controller c= new Controller();
		
		c.hosthttp = "localhost";
		c.porhttp = 8080;
		c.portrmi = 8090;
		
		c.abrirServer();
    	
    	if ( args.length < 3)
    	{
    		System.out.println("Error argumentos incorrectos");
    		System.out.println("/Controller hostRMI portRMI portHTTP");
    	}
    	else 
    	{
    		Controller co = new Controller();
    		
    		c.hosthttp = args[0];
    		c.porhttp = Integer.parseInt(args[1]);
    		c.portrmi = Integer.parseInt(args[2]);
    		
    		c.abrirServer();
    	}
    }
}






