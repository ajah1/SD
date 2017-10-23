package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

// INTERFAZ PARA SABER LOS MÉTODOS QUE TIENE
// EL OBJETO REMOTO
public interface Interfaz extends Remote
{
	public int getid()throws RemoteException;
	public int getTemp()throws RemoteException;
	public int getHumedad()throws RemoteException;
	public String getTipo()throws RemoteException;
	public String getFecha()throws RemoteException;
	
	public void crearSonda(String nombre)throws RemoteException;
	public String leerFichero( String nombre, String dato ) throws Exception, RemoteException;
}
