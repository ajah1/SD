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
		
	}

	// FUNCION PARA LEER EL FICHERO QUE HACE DE SONDA
	// SI NO ESTÁ LANZAMOS EXCEPCIÓN
	@Override
	public void leerFichero(String nombre) throws Exception {
		
		try
		{
			System.out.println("ENTRAR LEER");
			// leer archivo y guardarlo en objeto tipo bufferReader
			File archivo = new File( nombre );
			System.out.println("ABIERTO");
			
			FileReader file = new FileReader( archivo );
			System.out.println("LEIDO");
			
			BufferedReader br = new BufferedReader( file );
			System.out.println("GUARDADO");
			
			// mientras queden lineas en el buffer
			String linea = br.readLine();
			
			while ( linea != null )
			{
				//obtenemos los valores de cada parámetro
				String aux[] = linea.split("=");
				System.out.print(aux[0]);
				System.out.println(aux[1]);
				
				if ( linea.contains("ID") )
					setid( Integer.parseInt(aux[1]) );
				
				else if ( linea.contains("Tipo") )
					setTipo( aux[1] );
				
				else if ( linea.contains("Fecha") )
					setFecha( aux[1] );
				
				else if ( linea.contains("Temperatura") )
					setTemp( Integer.parseInt(aux[1]) );

				else if ( linea.contains("Humedad") )
					setHumedad( Integer.parseInt(aux[1]) );
				
				linea = br.readLine();
			
			}
			
			br.close();
			file.close();
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
	public void crearSonda(String nombre)
	{
		try 
		{
			File file = new File( nombre);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(nombre));
			
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

	@Override
	public void setFecha(String fecha) {
		// TODO Auto-generated method stub
		this._fecha = fecha;
	}
	
	public static void main (String args[]) throws Exception
	{
		try {
			Sonda sonda= new Sonda(2,"humedad", 99, 99, "99/99/99");
			
			sonda.crearSonda("sonda2");
			System.out.println("sonda creada");
			
			sonda.leerFichero("sonda2");
			
			System.out.println("Sonda leida");
			
			/*
			System.out.println(sonda._fecha);
			System.out.println(sonda._humedad);
			System.out.println(sonda._id);
			System.out.println(sonda._temperatura);
			System.out.println(sonda._tipo);
			*/
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}






