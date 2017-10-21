package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

// INTERFAZ PARA SABER LOS MÉTODOS QUE TIENE
// EL OBJETO REMOTO
public interface Interfaz extends Remote
{
	public void setid( int id )throws RemoteException;
	public void setTemp( int temp )throws RemoteException;
	public void setHumedad( int humedad )throws RemoteException;
	public void setTipo( String tipo )throws RemoteException;
	public void setFecha( String fecha)throws RemoteException;
	
	public void crearSonda(String nombre)throws RemoteException;
	public void leerFichero( String nombre ) throws Exception, RemoteException;
}
