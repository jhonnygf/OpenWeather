package com.jonathangf.EjercicioTema1.entidades;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * La clase Dia representa los dias extraidos del archivo csv para mostrar la
 * evolucion de la temperatura a lo largo del dia por horas, para eso tiene como
 * atributos la fecha como identificador y dos listas, una de horas y otra de
 * temperaturas.
 */
@Data
@AllArgsConstructor
public class Dia {
	private String fecha;
	private List<String> horas;
	private List<String> temperaturas;

	public Dia(String fecha) {
		this.fecha = fecha;
		this.horas = new ArrayList();
		this.temperaturas = new ArrayList();
	}

	/**
	 * Metodo para añadir las horas y las temperaturas a la clase Dia, compara las
	 * horas para comprobar que nos esten duplicadas antes de añadirlas
	 * 
	 * @param hora
	 * @param temperatura
	 */
	public void addHoraTemperatura(String hora, String temperatura) {

		if (!horas.contains(hora)) {
			int index = 0;
			while (index < horas.size() && horas.get(index).compareTo(hora) < 0) {
				index++;
			}
			horas.add(index, hora);
			temperaturas.add(index, temperatura);
		}
	}

	/*
	 * El metodo toString de la clase Dia contiene un bucle para poder añadir la
	 * lista de horas y temperaturas en la misma linea ordenadas. Para que mantengan
	 * el formato pedido en el apartado 3 del ejercicio
	 */
	@Override
	public String toString() {
		String resultado = "";

		for (int i = 0; i < horas.size(); i++) {
			resultado += horas.get(i) + "->" + temperaturas.get(i) + " ";
		}

		return resultado.trim();
	}
}
