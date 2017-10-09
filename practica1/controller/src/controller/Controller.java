package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;

public class Controller {
	
	private String hosthttp;
	private int portrmi;
	private int porhttp;
	private String peticion;
	/*
	 * Lee los datos escritos en el socket del controller
	 */
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
	
	/*
	 * Escribe en el socket del cliente
	 */
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
	
    /*
     * 	dependiendo del tipo de petición se enviará una páquina
     * distinta, con los datos correspodientes
     */
    public void procesarPeticion()
    {
    	
    }
    /*
     * Abre el server del controlador a la espera de peticiones
     * del servidor http, cada petición es procesada
     */
    public void abrirServer()
    {
    	try 
    	{
			ServerSocket ss = new ServerSocket(this.portrmi);
			System.out.println("Abrir server rmi, escuchando");
			
			for (;;)
			{
				Socket sc = ss.accept();
				System.out.println("Aceptando clientehttp");
				
				this.peticion = this.leeSocket(sc, peticion);
				System.out.println("PETICION: "+ this.peticion);
				
				//this.procesarPeticion();
			
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






