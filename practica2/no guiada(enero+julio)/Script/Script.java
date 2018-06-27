package script;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Script {

	// enviar los mensajes al cliente .Net
	public void SendData (String p_data)
	{
		String hostname = "192.168.0.26";
        int port = 13000;
        
        try (Socket socket = new Socket(hostname, port)) 
        {
        	 PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        	 //printWriter.write(p_data+"\n");
        	 printWriter.write("hola");
        	 printWriter.flush();
             printWriter.close();
             socket.close();
        }
        catch (IOException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
        }
	}
	
	public String _actuador = null;
	public int _rangoA = 0;
	public int _rangoB = 0;
	public int _incremento = 0;
	public String _estado = null;
	public String _ID = null;
	public String _sonda = null;
	
	
	public void escribirFicheroAct (String p_dato, String p_valor, String p_indice)
	{
		String _route = "/home/ali/sensorFiles/actuador" + p_indice;
		
		String s1 = "incremento:" + LeerDato("actuador", "incremento", p_indice);
		String s2 = "suscrito:"   + LeerDato("actuador", "suscrito", p_indice);
		String s3 = "rangoA:" 	  + LeerDato("actuador", "rangoA", p_indice);
		String s4 = "rangoB:" 	  + LeerDato("actuador", "rangoB", p_indice);
		String s5 = "estado:" 	  + LeerDato("actuador", "estado", p_indice);
		
		if (p_dato.contains("incremento"))
			s1 = "incremento:" + p_valor;
		
		else if (p_dato.contains("suscrito"))
			s2 = "suscrito:" + p_valor;
		
		else if (p_dato.contains("rangoA"))
			s3 = "rangoA:" + p_valor;
		
		else if (p_dato.contains("rangoB"))
			s4 = "rangoB:" + p_valor;
		
		else if (p_dato.contains("estado"))
			s5 = "estado:" + p_valor;
		
		FileWriter fileWriter;
		
		try 
		{
			fileWriter = new FileWriter(_route);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    
		    printWriter.println(s1+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5);
		    
		    printWriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void escribirFicheroSonda (String p_dato, String p_valor, String p_indice)
	{
		String _route = "/home/ali/sensorFiles/sonda" + p_indice;

		String s1 = "temperatura:"  + LeerDato("sonda", "temperatura", p_indice);
		String s2 = "humedad:" 		+ LeerDato("sonda", "humedad", p_indice);
		String s3 = "cliente:" 		+ LeerDato("sonda", "cliente", p_indice);
		String s4 = "actuador:" 	+ LeerDato("sonda", "actuador", p_indice);
		String s5 = "rangoA:" 		+ LeerDato("sonda", "rangoA", p_indice);
		String s6 = "rangoB:" 		+ LeerDato("sonda", "rangoB", p_indice);
		
		if (p_dato.contains("temperatura"))
			s1 = "temperatura:" + p_valor;
		
		else if (p_dato.contains("humedad"))
			s2 = "humedad:" + p_valor;
		
		else if (p_dato.contains("cliente"))
			s3 = "cliente:" + p_valor;
		
		else if (p_dato.contains("actuador"))
			s4 = "actuador:" + p_valor;
		
		else if (p_dato.contains("rangoA"))
			s5 = "rangoA:" + p_valor;
		
		else if (p_dato.contains("rangoB"))
			s6 = "rangoB:" + p_valor;
		
		FileWriter fileWriter;
		
		try 
		{
			fileWriter = new FileWriter(_route);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    
		    printWriter.println(s1+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5+"\n"+s6);
		    
		    printWriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String LeerDato(String objeto, String dato, String _ID)
	{
		String _route = "/home/ali/sensorFiles/" + objeto + _ID;
		
		String salida = null;
		
		String filename = _route;
		String line = null;
		
		try 
		{
			FileReader fileReader = new FileReader(filename);
			 
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        
	        while((line = bufferedReader.readLine()) != null)
	        {
	     		String[] splited = line.split(":");
	
	     		if(splited[0].equals(dato)) 
	     		{
	     			salida = splited[1];
	     		}
	         }
	        
	        bufferedReader.close();  
            
		}
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "'");
        }
		
		return salida;
	}
	
	
	public static void main(String[] args) throws InterruptedException 
	{
		Script script = new Script();
		Scanner reader = new Scanner (System.in);
		
		System.out.print("[TIEMPO] ID: ");
		script._ID = reader.nextLine();
		reader.close();
		
		script._actuador = "actuador"+script._ID;
		script._sonda = "_sonda"+script._ID;
		script._rangoA = Integer.parseInt(script.LeerDato("actuador","rangoA",script._ID));
		script._rangoB = Integer.parseInt(script.LeerDato("actuador","rangoB",script._ID));
		script._incremento = Integer.parseInt(script.LeerDato("actuador","incremento",script._ID));
		script._estado = script.LeerDato("actuador", "estado",script._ID);
		
		int aux_humedad = Integer.parseInt(script.LeerDato("sonda","humedad",script._ID));
		int aux_temperatura = Integer.parseInt(script.LeerDato("sonda","temperatura",script._ID));
		
		
		boolean bajarH = true;
		boolean bajarT = true;
		while (true)
		{
			String mensajeH = null;
			String mensajeT = null;
			
			if (script._estado.equals("funciona") && 
					script.LeerDato("actuador","suscrito",script._ID).equals("true"))
			{
				/* valores humedad  */
				if (true)
				{
					if (aux_humedad < script._rangoA)
					{
						mensajeH = "alerta: rango bajo [hum]";
						bajarH = !bajarH;
						aux_humedad += script._incremento;
					}
					else if (aux_humedad > script._rangoB)
					{
						mensajeH = "alerta rango alto [hum]";
						bajarH = !bajarH;
						aux_humedad -= script._incremento;
					}
					else
					{
						if (bajarH == false)
							aux_humedad += script._incremento;
						else
							aux_humedad -= script._incremento;
					}
				}
				/* valores temperatura */
				if (true)
				{
					if (aux_temperatura < script._rangoA)
					{
						mensajeT = "alerta: rango bajo [temp]";
						bajarT = !bajarT;
						aux_temperatura += script._incremento;
					}
					else if (aux_temperatura > script._rangoB)
					{
						mensajeT = "alerta rango alto [temp]";
						bajarT = !bajarT;
						aux_temperatura -= script._incremento;
					}
					else
					{
						if (bajarT == false)
							aux_temperatura += script._incremento;
						else
							aux_temperatura -= script._incremento;
					}
				}
				
				// cuando se tengan mensaje que enviar
				String aux = "";
				if (mensajeT != null)
					aux += mensajeT+"\n";
				if (mensajeH != null)
					aux += mensajeH+"\n";
				
				String s = script.LeerDato("sonda", "cliente", script._ID);
				boolean b = s.equals("true");
				if (!aux.equals("") && b == true)
					script.SendData("sonda");
				
				
				mensajeT = null;
				mensajeH = null;
				
				TimeUnit.SECONDS.sleep((long) 2);
			}
			else
			{
				aux_temperatura += 5;
				aux_humedad -= 5;
				TimeUnit.SECONDS.sleep(4);
			}
			
			
			script.escribirFicheroSonda("humedad", String.valueOf(aux_humedad), script._ID);
			script.escribirFicheroSonda("temperatura", String.valueOf(aux_temperatura), script._ID);
			
			script._estado = script.LeerDato("actuador", "estado", script._ID);
			script._rangoA = Integer.parseInt(script.LeerDato("actuador","rangoA",script._ID));
			script._rangoB = Integer.parseInt(script.LeerDato("actuador","rangoB",script._ID));
			
			
			System.out.println("humedad: " + String.valueOf(aux_humedad));
			System.out.println("temperatura: " + String.valueOf(aux_temperatura));
			System.out.println("***\n");
		}
		
	}
	
}