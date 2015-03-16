package cl.ido.divisas.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Utils {

	/**
	 * Devuelve un número (float) a partir de texto con formato $X.XXX.XXX,XX
	 * @param txt
	 * @return
	 */
	public static float getNumberFromString(String txt) {
		txt = txt.replace("$", "");
		txt = txt.replace(".", "");
		txt = txt.replace(",", ".");
		float f;
		try {
			f = Float.parseFloat(txt);
		} catch (ClassCastException e) {
			f = 0;
		} 
		return f;
	}
	
	
	/**
	 * Redondea el número a la cantidad de decimales dada y lo exporta con separador de miles
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static String roundNumber(float number, int decimals){
	      BigDecimal big = new BigDecimal( String.valueOf(number)  );
	      big = big.setScale(decimals, RoundingMode.HALF_UP);
	      
	      DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
	      
	      return decimalFormat.format(big.doubleValue());
	}
	
}