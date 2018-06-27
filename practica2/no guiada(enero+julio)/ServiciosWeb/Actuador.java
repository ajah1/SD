package master;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Actuador {
	
	/*
	 * LA PERSISTENCIA ESTÁ EN LOS FICHEROS "actuadorid.txt"
	 */
	
	
	public Actuador ()
	{
	}
	
	
	public void escribirFichero (String p_dato, String p_valor, int p_indice)
	{
		String _route = "/home/ali/sensorFiles/actuador" + p_indice;
		
		String s1 = "incremento:" + LeerDato("incremento", p_indice);
		String s2 = "suscrito:"   + LeerDato("suscrito", p_indice);
		String s3 = "rangoA:" 	  + LeerDato("rangoA", p_indice);
		String s4 = "rangoB:" 	  + LeerDato("rangoB", p_indice);
		String s5 = "estado:" 	  + LeerDato("estado", p_indice);
		
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
	
	
	public String LeerDato(String dato, int p_indice)
	{
		String _route = "/home/ali/sensorFiles/actuador" + p_indice;
		
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
	
	public String isOkey() 
	{
		return "OK";
	}
}
