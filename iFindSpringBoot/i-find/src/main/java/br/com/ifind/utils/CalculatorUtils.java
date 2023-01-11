package br.com.ifind.utils;

import br.com.ifind.exceptions.ResourceNotFoundException;

public class CalculatorUtils {
	
	public static Double sum(String numberOne, String numberTwo) throws Exception {
		
		checkIsNumeric(numberOne);
		checkIsNumeric(numberTwo);
		
		return convertToDouble(numberOne) + convertToDouble(numberTwo);
	}
	
	public static Double media(String numberOne, String numberTwo) throws Exception {
		
		checkIsNumeric(numberOne);
		checkIsNumeric(numberTwo);
		
		return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
	}

	public static Double sub(String numberOne, String numberTwo) throws Exception {
		
		checkIsNumeric(numberOne);
		checkIsNumeric(numberTwo);
		
		return convertToDouble(numberOne) - convertToDouble(numberTwo);
	}

	public static Double mult(String numberOne, String numberTwo) throws Exception {
		
		checkIsNumeric(numberOne);
		checkIsNumeric(numberTwo);
		
		return convertToDouble(numberOne) * convertToDouble(numberTwo);
	}
	
	public static Double div(String numberOne, String numberTwo) throws Exception {
		checkIsNumeric(numberOne);
		checkIsNumeric(numberTwo);
		
		if(convertToDouble(numberTwo) == 0.0) {
			throw new ResourceNotFoundException("Cannot divide by zero");
		}
		
		return convertToDouble(numberOne) / convertToDouble(numberTwo);
	}
	
	public static Double squareRoot(String number) throws Exception {	
		checkIsNumeric(number);
		
		return Math.sqrt(convertToDouble(number));
	}
	
	public static void checkIsNumeric(String strNumber) {
		if(strNumber == null || !strNumber.replaceAll(",", ".").matches("[-+]?[0-9]*\\.?[0-9]+")) {
			throw new ResourceNotFoundException("Please set a numeric value");
		}
	}
	
	public static Double convertToDouble(String strNumber) {
		if(strNumber == null) return 0D;
		
		String number = strNumber.replaceAll(",", ".");
		return Double.parseDouble(number);
	}
	
	
}
