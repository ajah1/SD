
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.regex.Pattern;
// GENERA EL CONTENIDO DINÁMICO, 
// LEE LOS VALORES DE LAS SONDAS
public class Controller {
	
	// datos del controller
	private String hosthttp = "";
	private int porthttp = 0;
	private int portcontroller = 0;
	
	// url obtenida en la peticion
	private String url = "";
	
	// datos del host que contiene rmi
	private String hostrmi = "";
	private int portrmi = 0;
	
	// LEE LOS DATOS ESCRITOS EN EL SOCKET ENTRE SERVER
	// Y CONTROLADOR
	public String leeSocket(Socket _ss, String _buffer) 
	{
        try 
        {
            InputStream aux = _ss.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);
            
            _buffer = new String();
            _buffer = flujo.readUTF();
        } 
        catch (Exception e) 
        {
            System.out.println("Error al leer socket controller: ");
            System.out.println(e.toString());
        }
        return _buffer;
    }
	
	// ESCRIBE EN EL SOCKET ENTRE SERVER Y CONTROLADOR
    public void escribeSocket(Socket _ss, String _buffer) 
    {
        try 
        {
            PrintWriter out= new PrintWriter(_ss.getOutputStream());
            
            out.println(_buffer);
            out.flush();
            out.close();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR al escribir en socket del controller: ");
            System.out.println( e.toString() );
        }
        return;
    }
	
    // DEVUELVE INDEX AL SERVER, LISTA LAS SONDAS REGITRADAS
    // Y LOS DATOS DE CADA UNA
    public String index()
    {
    	String http = "";
    	String pagina = "";
    	String cabeza = "";
    	
    	// http
    	
    	http=  "HTTP/1.1 200 OK \n"
    			+ "Content-Type: text/html; charset=utf-8 \n"
    			+ "Server: MyHTTPServer \n"
    			+ "\n";
	
    	// primera parte del html
    	cabeza =  "<!DOCTYPE html>\n<html> \n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + " 		<title>ESTACIÓN </title>\n"
                + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>LISTADO DE SONDAS Y SUS LECTURAS</h1>\n"
                + "        <a href=\"/index.html\">Inicio</a>\n";

    	try {
			// obtener objetos remotos registrados 
    		String servidor = "rmi://" + this.hostrmi + ":" + this.portrmi;
    		
			String names [] = Naming.list(servidor);
			
			SondaInterfaz sonda;
			String datos = "";
			String datosObjetos = "";
			
			// obtener datos objeto
			for ( String n : names )
			{
				if ( !n.equals("//localhost:"+this.portrmi+"/Registro") )
				{
					sonda = (SondaInterfaz) Naming.lookup(n);
					
					int v = 0;
					String t = sonda.getTipo();
					if ( t.equals("humedad") )
						datos = 
								"<div> \n"
								+ "<p>SONDA "+ sonda.getid() + "</p> \n "
								+ "<p>Tipo=" + t + "</p> \n"
								+ "<p>Fecha=" + sonda.getFecha() + "</p> \n"
								+ "<p>Humedad=" + sonda.getHumedad() + "</p> \n"
								+ "</div> \n";
					else
						datos = 
							"<div> \n"
							+ "<p>SONDA "+ sonda.getid() + "</p> \n "
							+ "<p>Tipo=" + t + "</p> \n"
							+ "<p>Fecha=" + sonda.getFecha() + "</p> \n"
							+ "<p>Temperatura=" + sonda.getTemp() + "</p> \n"
							+ "</div> \n";
					
					datosObjetos = datosObjetos + datos;
					
				}
			}
				pagina =   http + cabeza + datosObjetos + "</body> \n </html> \n";
    	}
    	catch( Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	
    	return pagina;
    }
    
    // PAGINA DE ERROR SONDA NO ENCONTRADA
    public String noSondaFound ( String sonda ) 
    {
    	
    	String http = "";
    	String cabeza = "";
    	String cuerpo = "";
    	
    	http=  "HTTP/1.1 404 Not Found\n"
    			+ "Content-Type: text/html; charset=utf-8 \n"
    			+ "Server: MyHTTPServer \n"
    			+ "\n";
    	
    	cabeza =  "<!DOCTYPE html>\n<html> \n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + " 		<title>ESTACIÓN </title>\n"
                + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    </head>\n";
    	
    	cuerpo = "<body> \n"
    			+ "<h1> Dipositivo " + sonda + " no encontrado </h1> \n"
    			+ "</body> \n "
    			+ "</html> \n";
    	
    	
    	return http + cabeza + cuerpo;
    }
    
    // PAGINA DE ERROR DATO PEDIDO CON COINCIDE CON EL TIPO
    public String noTypeFound ( String sonda, String tipo )
    {
    	String http = "";
    	String cabeza = "";
    	String cuerpo = "";
    	
    	http=  "HTTP/1.1 404 Not Found\n"
    			+ "Content-Type: text/html; charset=utf-8 \n"
    			+ "Server: MyHTTPServer \n"
    			+ "\n";
    	
    	cabeza =  "<!DOCTYPE html>\n<html> \n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + " 		<title>ESTACIÓN </title>\n"
                + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    </head>\n";
    	
    	cuerpo = "<body> \n"
    			+ "<h1> Dato no encontrado </h1> \n"
    			+ "<p>" +sonda+ " es de tipo "+tipo+ "</p> \n"
    			+ "<p> No concuerda el dato pedido con el tipo de sonda</p> \n" 
    			+ "</body> \n "
    			+ "</html> \n";
    	
    	return http + cabeza + cuerpo;
    }
    
    // DEVUELVE UNA PÁGINA CON LA INFORMACIÓN PEDIDA DE LA
    // SONDA PASADA
    public String datoConreto ( String sonda, String dato ) throws RemoteException, MalformedURLException, NotBoundException
    {
    	String http = "";
    	String cabeza = "";
    	String cuerpo = "";
    	
    	String valor = "";

    	String datoSondaPedido = dato.toLowerCase();
    	
    	boolean pTipo = datoSondaPedido.contains ( "tipo" );
    	boolean pHumedad = datoSondaPedido.contains ( "humedad" );
    	boolean pTemperatura = datoSondaPedido.contains ( "temperatura" );
    	boolean pFecha = datoSondaPedido.contains ( "fecha" );
    	
		SondaInterfaz objetoRemoto = (SondaInterfaz) Naming.lookup(sonda);
		
    	if ( pTipo )
    		valor = objetoRemoto.getTipo();
    	
    	else if ( pHumedad )
    		valor = String.valueOf( objetoRemoto.getHumedad() );
    	
    	else if ( pTemperatura )
    		valor =  String.valueOf( objetoRemoto.getTemp() );
    		
    	else if ( pFecha )
    		valor = objetoRemoto.getFecha();
    	
    	http=  "HTTP/1.1 404 Not Found\n"
    			+ "Content-Type: text/html; charset=utf-8 \n"
    			+ "Server: MyHTTPServer \n"
    			+ "\n";
    	
    	cabeza =  "<!DOCTYPE html>\n<html> \n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + " 		<title>ESTACIÓN </title>\n"
                + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    </head>\n";
    	
    	cuerpo = "<body> \n"
    			+ "<h1> Dato encontrado </h1> \n"
    			+ "<p>  Para la sonda" +sonda+ " "+ dato + " = " +valor+ "</p> \n"
    			+ "</body> \n "
    			+ "</html> \n";
    	
    	return http + cabeza + cuerpo;
    }
    
    public String datoNoValido ( String dato )
    {
    	String http = "";
    	String cabeza = "";
    	String cuerpo = "";
    	
    	http=  "HTTP/1.1 404 Not Found\n"
    			+ "Content-Type: text/html; charset=utf-8 \n"
    			+ "Server: MyHTTPServer \n"
    			+ "\n";
    	
    	cabeza =  "<!DOCTYPE html>\n<html> \n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + " 		<title>ESTACIÓN </title>\n"
                + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    </head>\n";
    	
    	cuerpo = "<body> \n"
    			+ "<h1> Dato no valido </h1> \n"
    			+ "<p>  El dato a obtener (" +dato+ ")"+" no forma parte del sistema </p> \n"
    			+ "</body> \n "
    			+ "</html> \n";
    	
    	return http + cabeza + cuerpo;
    
    }
    
    // DEVUELVE UNA PÁGINA CON EL DATO DE LA SONDA PEDIDA
    // SI LA SONDA O DATO NO CUADRAN ERROR Y AU
    public String obtenerDatos ( String dato ) throws MalformedURLException, RemoteException, NotBoundException
    {
    	
    	System.out.println("Datos a procesar: " + dato );

		String separador = Pattern.quote("?");
		String [] hola = dato.split(separador);

    	// sonda=1    ====>    sonda1
    	String[] ns = hola[1].split("=");
    	String sonda = ns[0] +ns[1];
    	System.out.println("sonda a buscar: "+ sonda);
    	
    	boolean sondaExiste = false;
    	String [] files = Naming.list("rmi://" + this.hostrmi + ":" + this.portrmi);
    	for ( String n : files )
    	{
    		if ( n.equals( "//" + this.hostrmi + ":" + this.portrmi + "/" + sonda ) )
    			sondaExiste = true;
    	}
    	System.out.println( "existe: "+sondaExiste);
    	
    	String datoSondaPedido = hola[0].toLowerCase();
    	
    	boolean pTipo = datoSondaPedido.contains ( "tipo" );
    	boolean pHumedad = datoSondaPedido.contains ( "humedad" );
    	boolean pTemperatura = datoSondaPedido.contains ( "temperatura" );
    	boolean pFecha = datoSondaPedido.contains ( "fecha" );
    	
    	boolean datoValido = pTipo || pHumedad || pTemperatura || pFecha;
    	if ( sondaExiste && datoValido)
    	{
    		SondaInterfaz objetoRemoto = (SondaInterfaz) Naming.lookup(sonda);
    		String type = objetoRemoto.getTipo ();
    		
    		boolean pideHumedad = datoSondaPedido.contains ( "humedad" );
    		boolean pideTemperatura = datoSondaPedido.contains ( "temperatura" );
    		boolean coincideDato = datoSondaPedido.contains( type );
    		
    		if ( ( pideHumedad || pideTemperatura )  && !coincideDato )
	    		return  this.noTypeFound ( sonda, type );
    		else
    			return this.datoConreto ( sonda, hola[0] );
    	}
    	else if ( !datoValido )
    		return this.datoNoValido( hola[0] );
    	else
    		return this.noSondaFound ( sonda );
    }

    public String soloSonda ( String psonda ) throws RemoteException, MalformedURLException, NotBoundException
    {
      	String http = "";
    	String cabeza = "";
    	String cuerpo = "";
    	
    	String separador = Pattern.quote("=");
		String [] separado = psonda.split(separador);
		String sonda = separado[0]+separado[1];
    	
    	boolean sondaExiste = false;
    	String [] files = Naming.list("rmi://" + this.hostrmi + ":" + this.portrmi);
    	for ( String n : files )
    	{
    		if ( n.equals( "//" + this.hostrmi + ":" + this.portrmi + "/" + sonda ) )
    			sondaExiste = true;
    	}
    	
    	if ( sondaExiste )
    	{
    		SondaInterfaz objetoRemoto = (SondaInterfaz) Naming.lookup( sonda );
    		String type = objetoRemoto.getTipo ();
    		
    		http=  "HTTP/1.1 200 OK \n"
        			+ "Content-Type: text/html; charset=utf-8 \n"
        			+ "Server: MyHTTPServer \n"
        			+ "\n";
        	
        	cabeza =  "<!DOCTYPE html>\n<html> \n"
                    + "    <head>\n"
                    + "        <meta charset=\"utf-8\">\n"
                    + " 		<title>ESTACIÓN </title>\n"
                    + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                    + "    </head>\n";
        	
        	cuerpo = "<body> \n"
        			+ "<div>"
        			+ "<h1> Datos del dispositivo " + sonda+ "</h1> \n"
					+ "<p>SONDA "+ objetoRemoto.getid() + "</p> \n "
					+ "<p>Tipo=" + objetoRemoto.getTipo() + "</p> \n"
					+ "<p>Fecha=" + objetoRemoto.getFecha() + "</p> \n"
					+ "<p>Humedad=" + objetoRemoto.getHumedad() + "</p> \n"
					+ "<p>Temperatura=" + objetoRemoto.getTemp() + "</p> \n"
        			+ "</div>"
        			+ "</body> \n";
        	
        	return http + cabeza + cuerpo;
    	}
    	else
    	{
    		return noSondaFound( sonda );
    	}
    
    }
    
    // DIVIDE LA URL OBTENIDO Y LLAMA A LAS FUNCIONES CORRESPONDIENTES
    // EN FUNCIÓN DE LOS DATOS PEDIDOS EN LA URL
	@SuppressWarnings("deprecation")
	public String procesarPeticion() throws MalformedURLException, NotBoundException, RemoteException
    {
    
    	String [] datos = this.url.split("/");
    	System.out.println("LONG: "+datos.length);
    	for ( String d : datos)
    		System.out.println(d);
    	
    	String [] names = Naming.list("rmi://" + this.hostrmi + ":" + this.portrmi);
    	for ( String n : names )
    		System.out.println(n);

    	// http://192.168.1.1:3000/controladorSD/obtenerTemperatura?sonda=1
    	if ( datos.length == 2 )
    		return index();
    	else if ( datos.length == 3 && datos[2].equals("index") )	
    		return index();
    	else if ( datos.length == 3 && !datos[2].contains("obtener") )
    		return soloSonda( datos[2] );
    	else if ( datos[2].contains("obtener"))
    		return obtenerDatos(datos[2]);
    	
    	return "";

    }
    
    // ABRE UN SOCKET A LA ESCUCHA DE PETICIONES
    // DEL SERVER,
    public void abrirServer() throws NotBoundException
    {
    	try 
    	{
    		System.out.println(">Abriendo controller...");
			ServerSocket ss = new ServerSocket( this.portcontroller );
						
			for (;;)
			{
				Socket sc = ss.accept();
				System.out.println( "\n"+"==>Cliente aceptado" );
				
				this.url = this.leeSocket(sc, url);
				
				System.out.println("URL: "+ this.url);
				
				String pet = this.procesarPeticion();
				System.out.println(pet);
				
				this.escribeSocket(sc, pet);
				System.out.println( "==>Cliente respondido");
				
				//sc.close();
			
			}
		} 
    	catch (IOException e) 
    	{
			System.out.println("ERROR: abrirServer");
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws IOException, NotBoundException 
    {
    	Controller c= new Controller();
		String aux = "";
		
		c.hosthttp = "localhost";
		c.porthttp = 8080;
		c.portcontroller = 8090;
		c.hostrmi="localhost";
		c.portrmi=1099;
		
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		
    	System.out.println("*** ASISTENTE CONTROLLER ***"+"\n");
    	
		System.out.print( ">Puerto del controller [pd 8090]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.portcontroller = Integer.parseInt(aux);
    	
		System.out.print( ">Host minihttpserver [pd localhost]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.hosthttp = aux;
		
		System.out.print( ">Puerto minihttpserver [pd 8080]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.porthttp = Integer.parseInt(aux);
		
		System.out.print( ">Host rmi [pd localhost]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.hostrmi = aux;
		
		System.out.print( ">Port rmi [pd 1099]: " );
		aux = br.readLine();
		if ( !aux.equals(""))
			c.portrmi = Integer.parseInt(aux);
		
		System.out.println( "\n"+"Datos a usar en el controller..." );
		System.out.println("portcontroller: " + c.portcontroller);
		System.out.println("hosthttp: " + c.hosthttp);
		System.out.println("porthttp: " + c.porthttp);
		System.out.println("hostrmi: " + c.hostrmi);
		System.out.println("porRmi: " + c.portrmi + "\n");

		
		c.abrirServer();

    }
}





