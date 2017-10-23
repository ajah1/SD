package controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.*;
import java.rmi.server.*;
import java.util.List;

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

	// DEVUELVE EL DATO DE LA SONDA A LEER
	@Override
	public String leerFichero(String nombre, String dato) throws Exception {
		
		String out = "";
		
		try
		{
			List<String> lines = Files.readAllLines(Paths.get(nombre));
			System.out.println("fichero leido...");
			
			for ( String line : lines )
			{
				String aux[] = line.split("=");
				if ( aux[0].equals(dato) )
				{
					out = aux[1];
					break;
				}
			}
			if ( out == "")
				return "0";
		}
		catch( Exception e)
		{
			System.out.println("ERROR: al leer fichero sonda");
			System.out.println(e.toString());
		}
		
		return out;
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
	public int getid() throws RemoteException{
			
		int out = 0;
		try {
			out = Integer.parseInt(this.leerFichero("sonda"+_id, "ID"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	@Override
	public int getTemp() throws RemoteException {
		int out = 0;
		try {
			out = Integer.parseInt(this.leerFichero("sonda"+_id, "Temperatura"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	@Override
	public int getHumedad() throws RemoteException {
		int out = 0;
		try {
			out = Integer.parseInt(this.leerFichero("sonda"+_id, "Humedad"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	@Override
	public String getTipo() throws RemoteException {
		String out = "";
		try {
			out = this.leerFichero("sonda"+_id, "Tipo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	@Override
	public String getFecha() throws RemoteException {
		String out = "";
		try {
			out = this.leerFichero("sonda"+_id, "Fecha");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

}






