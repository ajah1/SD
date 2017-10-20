package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.*;

public class Sonda implements Serializable, Interfaz
{
	private int _id;
	private String _tipo;
	private int _humedad;
	private int _temperatura;
	private String _fecha;
	
	// INICIALIZAR LA SONDA CON EL ID PASADO 
	public Sonda( int id, 
					String tipo, 
					int humedad, 
					int temperatura,
					String fecha ) throws RemoteException
	{
		super();
		
		this._id = id;
		this._fecha = fecha;
		this._tipo = tipo;
		this._humedad = humedad;
		this._temperatura = temperatura;
		
		// leemos la sonda añadida
		// si no se puede leer lanzamos excepción
		// en caso contrario leemos
		
	}

	// FUNCION PARA LEER EL FICHERO QUE HACE DE SONDA
	// SI NO ESTÁ LANZAMOS EXCEPCIÓN
	@Override
	public void leerFichero(String nombre) throws Exception {
		
		try
		{
			// leer archivo y guardarlo en objeto tipo bufferReader
			File archivo = new File( nombre );
			
			FileReader file = new FileReader( archivo );
			
			BufferedReader br = new BufferedReader( file );
			
			// mientras queden lineas en el buffer
			String linea = br.readLine();
			
			while ( !linea.isEmpty() )
			{
				//obtenemos los valores de cada parámetro
				String aux[] = linea.split("=");
				
				if ( linea.contains("id") )
					setid( Integer.parseInt(aux[1]) );
				
				else if ( linea.contains("tipo") )
					setTipo( aux[1] );
				
				else if ( linea.contains("temperatura") )
					setTemp( Integer.parseInt(aux[1]) );
				
				else if ( linea.contains("humedad") )
					setHumedad( Integer.parseInt(aux[1]) );
				
				br.close();
				file.close();
			}
		}
		catch( Exception e)
		{
			System.out.println("ERROR: al leer fichero sonda");
			System.out.println(e.toString());
		}
	}

	// FUNCION PARA AÑADIR UNA NUEVA SONDA (FICHERO)
	// CON LOS PARAMETROS AÑADIDOS AL CREAR EL OBJETO
	@Override
	public void crearSonda()
	{
		try 
		{
			File file = new File( "sonda" + this._id );
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("sonda" + this._id));
			
			String texto = "ID="+_id+"\n"+
							"Tipo="+_tipo+"\n"+
							"Fecha="+_fecha+"\n";
			
			if ( _tipo.equals("temperatura"))
				texto += "Temperatura="+_temperatura;
			else
				texto += "Humedad="+_humedad;
			
			bw.write(texto);
			bw.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: al crear sonda nueva (fichero)");
			System.out.println(e.toString());
		}
		
	}
	
	@Override
	public void setid(int id) {
		// TODO Auto-generated method stub
		this._id = id;
	}

	@Override
	public void setTemp(int temp) {
		// TODO Auto-generated method stub
		this._temperatura = temp;
	}

	@Override
	public void setHumedad(int humedad) {
		// TODO Auto-generated method stub
		this._humedad = humedad;
	}

	@Override
	public void setTipo(String tipo) {
		// TODO Auto-generated method stub
		this._tipo = tipo;
	}
	
	public static void main (String args[])
	{
		try {
			Sonda sonda= new Sonda(1,"temperatura", 23, 50, "12/12/12");
			
			sonda.crearSonda();
			System.out.println("sonda creada");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}






