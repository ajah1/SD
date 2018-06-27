package master;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Sensor
{
	public void escribirFichero (String p_dato, String p_valor, int p_indice)
	{
		String _route = "/home/ali/sensorFiles/sonda" + p_indice;

		String s1 = "temperatura:"  + LeerDato("temperatura", p_indice);
		String s2 = "humedad:" 		+ LeerDato("humedad", p_indice);
		String s3 = "cliente:" 		+ LeerDato("cliente", p_indice);
		String s4 = "actuador:" 	+ LeerDato("actuador", p_indice);
		String s5 = "rangoA:" 		+ LeerDato("rangoA", p_indice);
		String s6 = "rangoB:" 		+ LeerDato("rangoB", p_indice);
		
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
	
	public String LeerDato(String dato, int p_indice)
	{
		String _route = "/home/ali/sensorFiles/sonda" + p_indice;
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
	
	
	/* GETTERS/SETTERS */
	public String getActuador (int i)
	{
		return LeerDato("actuador", i);
	}
	
	public int getTemperatura (int i)
	{
		return Integer.parseInt(LeerDato("temperatura", i));
	}
	
	public int getHumedad (int i)
	{
		return Integer.parseInt(LeerDato("humedad", i));
	}
	
	public String getCliente (int i)
	{
		return LeerDato ("cliente", i);
	}
	
	public String isOkey() 
	{
		return "OK";
	}
	
}
