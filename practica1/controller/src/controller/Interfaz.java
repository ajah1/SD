package controller;

public interface Interfaz {

	public void leerFichero( String nombre ) throws Exception;
	public void setid( int id );
	public void setTemp( int temp );
	public void setHumedad( int humedad );
	public void setTipo( String tipo );
	public void setFecha( String fecha);
	
	public void crearSonda();
	

}
