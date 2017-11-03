
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sonda extends UnicastRemoteObject implements SondaInterfaz {

	private static final long serialVersionUID = 1L;

	private final String rmiName;

	private int _id = 0;
	private String _tipo = "humedad";
	private int _humedad = 0;
	private int _temperatura = 0;
	private String _fecha = "nofecha";
	
	// si es true => incrementar, si es false => decrementar
	private boolean actuador;
	
	public Sonda( int id, 
			String tipo, 
			int humedad, 
			int temperatura,
			String fecha ) throws RemoteException
	{	
		super();
		this._id = id;
		this._tipo = tipo;
		this._humedad = humedad;
		this._temperatura = temperatura;
		this.rmiName = "sonda"+_id;
		
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		this._fecha = hourdateFormat.format(date).toString();
	}

	public void sayHelloWorld() throws RemoteException {
		System.out.println("I'm the slave " + getRmiName() + ", and I say: Hellow world !!!");
	}

	public String getRmiName() throws RemoteException {
		return rmiName;
	}

	@Override
	public int getid() throws RemoteException{
			
		int out = 0;
		try {
			out = Integer.parseInt(this.leerFichero("sonda"+_id, "ID"));
		} catch (Exception e) {
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
			e.printStackTrace();
		}
		// tras leer el valor actual, calculamos el nuevo
		getActuador();
		return out;
	}
	@Override
	public int getHumedad() throws RemoteException {
		int out = 0;
		try {
			out = Integer.parseInt(this.leerFichero("sonda"+_id, "Humedad"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// tras leer el valor actual, calculamos el nuevo
		getActuador();
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

	// DEVUELVE EL DATO DE LA SONDA A LEER
	@Override
	public String leerFichero(String nombre, String dato) throws Exception {
		
		String out = "";
		
		try
		{
			List<String> lines = Files.readAllLines(Paths.get(nombre));
			
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
			
			if (  _tipo.equals( "temperatura" ) )
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
	public void ModificarDato ( ) throws RemoteException 
	{
		try 
		{
			String nombre = "actuador"+_id;
			String texto = "";
			File file = new File(nombre);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(nombre));
			
			texto = "ID="+_id+"\n"+
					"Funcion="+_tipo+"\n";
			
			if ( actuador )
				texto += "Modo=incrementar \n";
			else
				texto += "Modo=decrementar \n";

			bw.write(texto);
			bw.close();
		
		} catch (IOException e) {
			System.out.println("ERROR: al crear actuador nuevo (fichero)");
			System.out.println(e.toString());
		}
	}
	
	// SI EL VALOR SE ENCUENTRA DENTRO DEL RANGO
	// SIEMPRE SE LEE DEL FICHERO POR SI ESTE ES MODIFICADO A MANO EN LA CORRECCIÖN
	@Override
	public boolean getActuador() throws RemoteException 
	{
		// el rango de valores depende del tipo
		if (  _tipo.equals ( "temperatura" ) )
		{
			try {
				_temperatura = Integer.parseInt(leerFichero("sonda"+_id, "Temperatura"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ( _temperatura < 25 )
			{
				actuador = true;
				_temperatura += 5;
			}
			else if ( _temperatura > 35 )
			{
				actuador = false;
				_temperatura -= 5;
			}
			else
				_temperatura += ThreadLocalRandom.current().nextInt( -10, 10 );
			
			// modifica el fichero de la sonda
			crearSonda("sonda"+_id);
			// actualiza el fichero del actuador
			ModificarDato();
		}
		else if (  _tipo.equals ( "humedad" ) )
		{
			try {
				_humedad = Integer.parseInt(leerFichero("sonda"+_id, "Humedad"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ( _humedad < 40 )
			{
				actuador = true;
				_humedad += 5;
			}
			else if ( _humedad > 75 )
			{
				actuador = false;
				_humedad -= 5;
			}
			else
				_humedad += ThreadLocalRandom.current().nextInt( -10, 10 );
			
			// modifica el fichero de la sonda
			crearSonda("sonda"+_id);
			// actualiza el fichero del actuador
			ModificarDato();
		}

		
		return actuador;
	}

}
