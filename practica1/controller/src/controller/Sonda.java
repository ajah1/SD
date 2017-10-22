package controller;

import java.io.*;

import java.rmi.*;
import java.rmi.server.*;

// IMPLEMENTACIÓN DE LA INTERFAZ
// ESTE OBJETO REPRESENTA CADA UNA DE LAS
// SONDAS A CREAR.

@SuppressWarnings("serial")
public class Sonda extends UnicastRemoteObject
				implements Serializable, Interfaz
{
	private int _id = 0;
	private String _tipo = "humedad";
	private int _humedad = 0;
	private int _temperatura = 0;
	private String _fecha = "nofecha";
	
	public Sonda() throws RemoteException
	{
		super();
	}
	
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
			
			File archivo = new File( nombre );
			System.out.println("ABIERTO");
			
			FileReader file = new FileReader( archivo );
			System.out.println("LEIDO");
			
			BufferedReader br = new BufferedReader( file );
			System.out.println("GUARDADO");
			
			
			String linea = br.readLine();
			// MIENTRAS QUEDEN LINEAS POR LEER...
			while ( linea != null )
			{
				// OBTENER LOS VALORES DE LA SONDA
				// EN FUNCIÓN DEL TIPO DE SONDA
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
			File file = new File(nombre);
			
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
	
	// SETTERS PARA MODIFICAR SOLO LOS VALORES
	// DEL FICHERO QUE REPRESENTA LA SONDA
	@Override
	public void setid(int id) {
		// TODO Auto-generated method stub
		this._id = id;
		//crearSonda("sonda"+_id);
	}

	@Override
	public void setTemp(int temp) {
		// TODO Auto-generated method stub
		this._temperatura = temp;
		//crearSonda("sonda"+_id);
	}

	@Override
	public void setHumedad(int humedad) {
		// TODO Auto-generated method stub
		this._humedad = humedad;
		//crearSonda("sonda"+_id);
	}

	@Override
	public void setTipo(String tipo) {
		// TODO Auto-generated method stub
		this._tipo = tipo;
		//crearSonda("sonda"+_id);
	}

	@Override
	public void setFecha(String fecha) {
		// TODO Auto-generated method stub
		this._fecha = fecha;
		//crearSonda("sonda"+_id);
	}
	/*
	public static void main (String args[]) throws Exception
	{
		try {
			Sonda sonda= new Sonda(2,"humedad", 99, 99, "99/99/99");
			
			sonda.crearSonda("sonda2");
			System.out.println("sonda creada");
			
			sonda.leerFichero("sonda2");
			
			System.out.println("Sonda leida");

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/

}






