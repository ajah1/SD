import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pruebas {

	public static void main(String[] args) throws Exception 
	{
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		System.out.println(hourdateFormat.format(date).toString());
		
	}
	
}
