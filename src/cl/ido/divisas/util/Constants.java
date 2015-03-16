package cl.ido.divisas.util;

public interface Constants {

	String JSON_URL = "http://indicadoresdeldia.cl/webservice/indicadores.json";
	String AUTHOR_LINKEDIN = "http://cl.linkedin.com/pub/cristóbal-sánchez-orellana/22/a88/112/";
	String AUTHOR_MAIL = "cristobal.sanchez.orellana@gmail.com"; 
	
	String WS_METHOD_NAME = "ConversionRate";
	String WS_NAMESPACE = "http://www.webserviceX.NET/";
	String WS_SOAP_ACTION = WS_NAMESPACE + WS_METHOD_NAME;
	String WS_URL = "http://www.webservicex.net/CurrencyConvertor.asmx?WSDL"; 

	String INDICADOR_EXTRA_USD  = "dolar";
	String INDICADOR_EXTRA_EUR  = "euro";
	String INDICADOR_EXTRA_UF  = "uf";
	String INDICADOR_EXTRA_UTM = "utm"; 
	String INDICADOR_EXTRA_INDICADOR = "indicador";
	String INDICADOR_EXTRA_MONEDA = "moneda";
	String INDICADOR_EXTRA_FECHA  = "fecha";
	
	String INDICADOR_EXTRA_BUNDLE = "BundleIndicadores";
	
	int POSICION_DOLAR = 0;
	int POSICION_EURO = 1;
	
	int POSICION_UF = 0;
	int POSICION_UTM = 1;
	
}