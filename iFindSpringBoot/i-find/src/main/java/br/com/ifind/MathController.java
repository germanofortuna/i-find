package br.com.ifind;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifind.utils.CalculatorUtils;


@RestController
public class MathController {
	
	//PathVariable - usada para recuperar dados da URL, obrigatória
	
	@GetMapping(value = "/sum/{numberOne}/{numberTwo}")
	public Double sum(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {

		return CalculatorUtils.sum(numberOne, numberTwo);
	}
	
	@GetMapping(value = "/media/{numberOne}/{numberTwo}")
	public Double media(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		
		return CalculatorUtils.media(numberOne, numberTwo);
	}

	@GetMapping(value = "/sub/{numberOne}/{numberTwo}")
	public Double sub(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		
		return CalculatorUtils.sub(numberOne, numberTwo);
	}

	@GetMapping(value = "/mult/{numberOne}/{numberTwo}")
	public Double mult(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		
		return CalculatorUtils.mult(numberOne, numberTwo);
	}
	
	@GetMapping(value = "/div/{numberOne}/{numberTwo}")
	public Double div(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		
		return CalculatorUtils.div(numberOne, numberTwo);
	}
	
	@GetMapping(value = "/square/{number}")
	public Double squareRoot(
			@PathVariable(value = "number") String number) throws Exception {
		
		return CalculatorUtils.squareRoot(number);
	}
	
}
