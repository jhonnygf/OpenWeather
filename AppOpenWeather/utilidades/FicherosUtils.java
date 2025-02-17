package com.jonathangf.EjercicioTema1.utilidades;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.jonathangf.EjercicioTema1.entidades.Dia;
//La clase FicherosUtils tiene los metodos para acceder a ficheros
public class FicherosUtils {
	/**
	 * Metodo para leer un archivo csv y extraer datos para crear una lista de
	 * objetos de la clase dia, el metodo recibe la ruta donde se encuentra el
	 * archivo csv, una fecha de inicio y otra de fin. Busca las fechas que se
	 * encuentren entre esas dos y extrae fecha, horas y temperaturas de cada dia,
	 * crea una lista y la devuelve con los dias que cumplan la condicion.
	 * 
	 * @param ruta
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static List<Dia> leerCSV(String ruta, LocalDate fechaInicio, LocalDate fechaFin) {
		List<Dia> dias = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			List<String> lines = Files.readAllLines(Paths.get(ruta));
			// Con la instruccion skip me salto los resultados de la primera linea que son
			// los encabezados de la tabla csv
			lines.stream().skip(1).forEach(line -> {
				// En el primer split saco los dos campos que necesito, la fecha y la
				// temperatura
				String[] parts = line.split(",");

				String fechaYHora = parts[5];
				String temperatura = parts[6];

				// Como la fecha la obtengo en string pero como un DateTime vuelvo a splitearla
				// para obtener la fecha por un lado y la hora por otro
				String[] fechaHoraParts = fechaYHora.split(" ");

				String fechaString = fechaHoraParts[0];
				String hora = fechaHoraParts[1];
				// Formateo la fecha para mostrarla como pide el formato
				String horaFormateada = hora.substring(0, 2) + ":00";

				procesarDia(fechaInicio, fechaFin, dias, formatter, temperatura, fechaString, horaFormateada);
			});

			// Ordeno la lista de días por fecha
			dias.sort(Comparator.comparing(d -> LocalDate.parse(d.getFecha(), formatter)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return dias;
	}

	/**
	 * Metodo que recoge los datos necesarios ir creando dias diferenciandolos por
	 * su fecha, si existe les añade las horas y temperaturas y sino los crea para
	 * ello. Despues los va añadiendo una lista.
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param dias
	 * @param formatter
	 * @param temperatura
	 * @param fechaString
	 * @param horaFormateada
	 */
	private static void procesarDia(LocalDate fechaInicio, LocalDate fechaFin, List<Dia> dias,
			DateTimeFormatter formatter, String temperatura, String fechaString, String horaFormateada) {
		try {
			// Formateo la fecha en string al tipo LocalDate para compararlas
			LocalDate fecha = LocalDate.parse(fechaString, formatter);

			// Esta condicion compara que la fecha sea igual o posterior a la fecha de
			// inicio e igual o menor que la fecha de fin
			if ((fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio))
					&& (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin))) {

				// Busco o creo el objeto Dia para la fecha actual
				Dia dia = dias.stream().filter(d -> d.getFecha().equals(fechaString)).findFirst().orElse(null);

				if (dia == null) {
					dia = new Dia(fechaString);
					dias.add(dia);
				}

				// Agrego la hora y temperatura al Dia
				dia.addHoraTemperatura(horaFormateada, temperatura);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
