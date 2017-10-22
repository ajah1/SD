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
			System.out.println("ERROR: al crear sonda nueva (fichero)");
			System.out.println(e.toString());
		}
		
	}

	@Override
	public int getid() throws RemoteException {
		return _id;
	}
	@Override
	public int getTemp() throws RemoteException {
		return _temperatura;
	}
	@Override
	public int getHumedad() throws RemoteException {
		return _humedad;
	}
	@Override
	public String getTipo() throws RemoteException {
		return _tipo;
	}
	@Override
	public String getFecha() throws RemoteException {
		return _fecha;
	}

}






