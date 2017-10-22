package controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import controller.Sonda;
// GENERA EL CONTENIDO DINÁMICO, 
// LEE LOS VALORES DE LAS SONDAS
public class Controller {
	
	// datos del controller
	private String hosthttp = "";
	private int porthttp = 0;
	private int portcontroller = 0;
	
	// url obtenida en la peticion
	private String url = "";
	
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
    		System.out.print(d);
    	
    	
    	System.out.println(" ");
    	System.out.println("conectando con objeto remoto");
    	// rmi://localhost:1099
    	String host= "localhost";
    	int port = 1099;
    	String servidor = "rmi://" + host + ":" + port;
        System.out.println("Servidor:" + servidor);
        
        String servidorConcreto = servidor.concat("/" + "sonda0");
        System.out.println("Objeto:" + servidorConcreto);
        System.out.println(" ");
        
        try
        {
        	 System.setSecurityManager(new RMISecurityManager());
        	 
        	 System.out.println("obtener nombres objetos remotos");
        	 String names [];
        	 names = Naming.list(servidor);
        	 System.out.println("names: " + names[0]);
        	 
        	 Interfaz sonda;
        	 System.out.println("enlazar con sonda registrada");
        	 sonda = (Interfaz) Naming.lookup("//localhost:1099/sonda0");
        	 
        	 System.out.println("inicializar la sonda");
        	 
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
    		System.out.println(">Abriendo controller...");
			ServerSocket ss = new ServerSocket( this.portcontroller );
						
			for (;;)
			{
				Socket sc = ss.accept();
				System.out.println( "\n"+"==>Cliente aceptado" );
				
				this.url = this.leeSocket(sc, url);
				
				System.out.println("URL: "+ this.url);
				
				sc.close();
				System.out.println( "==>Cliente leido");
				
				this.procesarPeticion();
			
			}
		} 
    	catch (IOException e) 
    	{
			System.out.println("ERROR: abrirServer");
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws IOException 
    {
    	Controller c= new Controller();
		String aux = "";
		
		c.hosthttp = "localhost";
		c.porthttp = 8080;
		c.portcontroller = 8090;
		
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		
    	System.out.println("*** ASISTENTE CONTROLLER ***"+"\n");
    	
		System.out.print( ">Puerto del controller [pd 8090]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.portcontroller = Integer.parseInt(aux);
    	
		System.out.print( ">Host minihttpserver [pd localhost]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.hosthttp = aux;
		
		System.out.print( ">Puerto minihttpserver [pd 8080]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.porthttp = Integer.parseInt(aux);
		
		System.out.println( "\n"+"Datos a usar en el controller..." );
		System.out.println("portcontroller: " + c.portcontroller);
		System.out.println("hosthttp: " + c.hosthttp);
		System.out.println("porthttp: " + c.porthttp + "\n");

		
		c.abrirServer();

    }
}






