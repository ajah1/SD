using System;
using System.Collections;
using System.Web.Services.Protocols;
using SDJulio.Actuador;
using SDJulio.Sensor;
using SDJulio.JUDDI;
using System.IO;

/// <summary>
/// Clase auxiliar que contiene los métodos necesarios para conectar
/// con los serivicio web, tanto de forma directa como por jUDDI
/// </summary>
namespace Cliente
{
    class WSDL
    {
        // Variable del servicio web Sensor
        static public Sensor sensor;
        // Variable del servicio web Actuador
        static public Actuador actuador;
        // ID del sensor al que se quiere conectar
        static public int ID = 9999;
        // El log se guarda en el mismo directorio donde se inicia el exe
        static string path = @"./Log.txt";

        public WSDL ()
        {
            sensor = new Sensor();
            actuador = new Actuador();
        }

        /* ESCRIBE EN EL LOG EL MENSAJE PASADO POR PARAMETRO*/
        static public void log (string text)
        {
            DateTime now = DateTime.Now;

            using (StreamWriter sw = File.AppendText(path))
            {
                sw.WriteLine("[" + now + "]  " + text);
            }
        }

        /*CONEXIÓN DIRECTA CON EL HOST PASADO POR PARAMETRO*/
        public bool Directa (string addresname)
        {
            sensor.Url = "http://" + addresname + ":8080/EjemploMaster/services/Sensor";
            sensor.Timeout = 1000;
            sensor.SoapVersion = SoapProtocolVersion.Soap11;

            actuador.Url = "http://" + addresname + ":8080/EjemploMaster/services/Actuador";
            actuador.Timeout = 1000;
            actuador.SoapVersion = SoapProtocolVersion.Soap11;
            
            // Detecta si al conexión no funciona
            sensor.isOkey();
            actuador.isOkey();

            return true;
        }

        /*CONECTA CON EL SERVIDOR jUDDI Y DEVUELVE LOS SERVICIOS WEB*/
        public ArrayList conectUDDI()
        {
            ArrayList toRet = new ArrayList();
            find_service servicio = new find_service();

            servicio.findQualifiers = new string[] { "approximateMatch" };
            servicio.name = new name[] { new name(), new name() };

            servicio.name[0].Value = "Sonda";
            servicio.name[1].Value = "Actuador";
            
            //Inquire
            UDDIInquiryService inquire = new UDDIInquiryService();

            //string host = "uddiconsole-jbossoverlord.rhcloud.com";
            string host = "192.168.207.3";
            inquire.Url = "http://" + host + ":8080/juddiv3/services/inquiry";

            //Lista de coincidencias
            serviceList lista;
            lista = inquire.find_service(servicio);

            //Construye el array de estaciones
            int estacionesRegistradas = lista.serviceInfos.Length;

            Console.OpenStandardOutput();

            for (int i = 0; i < estacionesRegistradas; i++)
            {
                serviceInfo informacion = lista.serviceInfos[i];

                get_serviceDetail gsd = new get_serviceDetail();
                gsd.serviceKey = new string[] { informacion.serviceKey };

                serviceDetail sd = new serviceDetail();
                sd = inquire.get_serviceDetail(gsd);

                businessService[] bs = sd.businessService;
                bindingTemplate[] bTemplate = bs[0].bindingTemplates;
                accessPoint AP = bTemplate[0].accessPoint;

                toRet.Add(AP.Value);
                Console.WriteLine(AP.Value);
            }

            log("Obteniendo servicios con jUDDI");

            return toRet;
        }

        /*CONECTA CON UN SERVICIO WEB USANDO LA URL DADA POR jUDDI*/
        public bool ConnectUDDIService(string badurl, string campo)
        { 
            if (campo.Equals("sonda"))
            {
                sensor.Url = badurl;
                sensor.Timeout = 1000;
                sensor.SoapVersion = SoapProtocolVersion.Soap11;
            }
            else if (campo.Equals("actuador"))
            {
                actuador.Url = badurl;
                actuador.Timeout = 1000;
                actuador.SoapVersion = SoapProtocolVersion.Soap11;
            }

            // probar la conexión
            sensor.isOkey();
            actuador.isOkey();
           
            return true;
        }
    }
}
