 import java.io.*;
 import java.net.*;
 
 public class Servidor {
 
 	/*
 		leer datos socket
 	*/
 	public String leerSocket()
 	{
 		String salida = "";
 		
 		return salida;
 	}
 
 
	/*
		escribir en el socket
	*/
	public void escribirSocket()
	{}
	
	public static void main(String[] arg)
	{
		int puerto = 0;
		int resultado = 0;
		
		String mensaje = "";
		
		Servidor server = new Servidor();
		
		// comprobar argumentos
		if (arg.length < 1)
		{
			System.out.println("ERROR: debe indicar el puerto del servidor");
			System.out.println("./Servidor puerto_server");
			
			System.exit(1);
		}
		
		try
		{
			// creamos socket
			puerto = Integer.parseInt(arg[0]);
			ServerSocket ss = new ServerSocket(puerto);
			System.out.println("Escucho el puerto");
			
			
			// mantener conexion con el cliente
			for(;;)
			{
				// se espera un cliente
				Socket sc = ss.accept();
				System.out.println("cliente acpetado");
				
				// mantener conexión hasta condición
				while (mensaje != "fin")
				{
					// leer socket del cliente
					
					// esciribir socket del cliente
				}
				
				// cerrar socket
				sc.close();
				System.exit(0);
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: " + e.toString()); 
		}
	
	}

 }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
