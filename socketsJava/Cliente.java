 import java.io.*;
 import java.net.*;
 
 public class Cliente {
 
 
	public static void escribirSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
			System.exit(-1);
		}
	}

 	public static void main(String[] arg)
 	{
 		Cliente client = new Cliente();
 		
 		String host;
 		int puerto;
 		String mensaje = "";
 		
 		if (arg.length < 2)
 		{
 			System.out.println("ERROR: debe indicar la direccion del servidor y el puerto");
 			
 			System.out.println("./Cliente servidor puerto");
 			
 			System.exit(-1);
 		}
 		
 		
 		host = arg[0];
 		puerto = Integer.parseInt(arg[1])	;
 		
 		try{
 			Socket sc = new Socket(host, puerto);
 			
	 		while (mensaje != "fin")
			{
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader (isr);
				
				mensaje = br.readLine();
				
				escribirSocket(sc, mensaje);
			}
 		}
 		catch (Exception e)
 		{
 			System.out.println(e);
 		}
 	}
 }
