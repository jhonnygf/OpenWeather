package com.jonathangf.EjercicioTema1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.jonathangf.EjercicioTema1.entidades.Ciudad;
import com.jonathangf.EjercicioTema1.entidades.Dia;
import com.jonathangf.EjercicioTema1.entidades.Pelicula;
import com.jonathangf.EjercicioTema1.utilidades.FicherosUtils;
import com.jonathangf.EjercicioTema1.utilidades.InternetUtils;
import com.jonathangf.EjercicioTema1.utilidades.JsonUtils;
import com.jonathangf.EjercicioTema1.utilidades.SerializacionUtils;
import com.jonathangf.EjercicioTema1.utilidades.XmlUtils;

/**
 * Programa que realiza llamadas a APIS para extraer datos de objetos en formato
 * Json y Xml, también lee un archivo desde un fichero csv para obtener una
 * lista de objetos que se encuentren entre dos fechas proporcionadas por el
 * usuario, puedes serializar la ultima busqueda obtenida en el apartado 1 o 2
 * del menu, deserializar una lista de busquedas realizadas en diferentes días y
 * acceder a otra API y hacer una llamada introduciendo el titulo de una
 * pelicula para que te devuelva sus datos en formato Json. Siento haber metido
 * los metodos de validación en esta clase, pero tuve un problema con el scanner
 * al ejecutarlos desde otra clase con otro scanner.
 */
public class App {

	static Scanner sc = new Scanner(System.in);
	private static final String TOKEN = "9f0456a7ae9e1d27ffe795df79591494";
	private static final String API_KEY = "97425bfd";

	static List<Ciudad> ciudades = new ArrayList();
	static Ciudad c = null;

	public static void main(String[] args) {

		int opcion;

		do {
			mostrarMenu();
			opcion = getInt("Elige una opción: ");
			sc.nextLine();

			switch (opcion) {

			case 1:
				c = busquedaPorCoordenadas();
				break;
			case 2:
				c = busquedaPorNombre();
				break;
			case 3:
				obtenerEvolucionTemperatura();
				break;
			case 4:
				serializarUltimaBusqueda();
				break;
			case 5:
				deserializarBúsquedasGuardadas();
				break;
			case 6:
				llamarAPIOmdbApi();
				break;
			case 0:
				System.out.println("Saliendo del programa...");
				break;
			default:
				System.out.println("Opción inválida. Por favor, elige una opción válida.");
			}

		} while (opcion != 0);

		sc.close();
	}

	private static void mostrarMenu() {
		System.out.println("\n--- Menú ---");
		System.out.println("1. Obtener información por coordenadas");
		System.out.println("2. Obtener información por nombre de ciudad");
		System.out.println("3. Obtener evolución de la temperatura");
		System.out.println("4. Serializar datos de la última búsqueda");
		System.out.println("5. Deserializar datos de las búsquedas guardadas");
		System.out.println("6. Buscar información en otra API");
		System.out.println("0. Salir");
	}

	/**
	 * Metodo para hacer una llamada a una API de peliculas llamada OMDBAPI, el
	 * usuario introducira el titulo de la peliucla que desea buscar, se depurara el
	 * titulo por si tiene espacios o caracteres especiales, añade el titulo
	 * depurado y la key de la api a la url para hacer la llamada y si todo es
	 * correcto lo muestra utilizando el metodo jsonPretty de Gson.
	 */
	private static void llamarAPIOmdbApi() {

		String titulo = getString("Introduce el título de la película: ");

		try {
			// Codificar el título para incluirlo en la URL por si tiene espacios o
			// caracteres especiales
			String tituloDepurado = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
			String url = "http://www.omdbapi.com/?t=" + tituloDepurado + "&apikey=" + API_KEY;
			String respuestaApi = InternetUtils.readUrl(url);

			if (respuestaApi != null) {
				Pelicula p = JsonUtils.leerGenerico(
						"http://www.omdbapi.com/?t=" + tituloDepurado + "&apikey=" + API_KEY, Pelicula.class);
				System.out.println(JsonUtils.crearJsonPretty(p));
			} else {
				System.out.println("No se pudo obtener la respuesta de la API.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para deserializar los datos las busquedas de los ultimos dias
	 */
	private static void deserializarBúsquedasGuardadas() {
		String ruta = "./data/ciudades.dat";
		ciudades = SerializacionUtils.desserializarListaObjetos(ruta);
		ciudades.forEach(e -> System.out.println(e));
	}

	/**
	 * Metodo para serializar los datos de la ultima busqueda realizada en el
	 * apartado 1 o 2, si existe un dato de ese dia lo sobrescribe pero mantiene la
	 * lista existente sin sobrescribirla sino existe datos de busqueda mostrara un
	 * mensaje
	 */
	private static void serializarUltimaBusqueda() {
		String ruta = "./data/ciudades.dat";
		if (c != null) {
			boolean existe = SerializacionUtils.serializarCiudad(ruta, c);
			System.out.println(existe ? "Serialización correcta" : "No se ha serializado.");
		} else {
			System.out.println("No hay búsqueda para serializar.");
		}
	}

	/**
	 * Metodo para obtener la evolucion te temperaturas por horas a lo largo de un
	 * día, pide una fecha de inicio y otra de fin que se validaran hasta que sean
	 * correctas, creara una lista de objetos de la clase Dia de los que se
	 * encuentren entre las dos fechas recibidas y los mostrara con el formato
	 * pedido en el ejercicio.
	 */
	private static void obtenerEvolucionTemperatura() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate fechaInicio = getDate("Ingresa la fecha de inicio (AAAA-MM-DD): ", formatter);
		LocalDate fechaFin = getDate("Ingresa la fecha de fin (AAAA-MM-DD): ", formatter);

		// Valido que la fecha inicial no sea posterior a la fecha final
		if (fechaInicio.isAfter(fechaFin)) {
			System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin.");
			return;
		}

		// Leo y proceso el CSV por fechas
		List<Dia> dias = FicherosUtils.leerCSV("./data/datos.csv", fechaInicio, fechaFin);

		// Mostrar resultados
		DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		for (Dia dia : dias) {
			LocalDate fecha = LocalDate.parse(dia.getFecha(), formatter);
			String fechaFormateada = fecha.format(outputDateFormatter);
			System.out.println(fechaFormateada + "  " + dia.toString());
		}
	}

	/**
	 * Metodo para obtener información de la búsqueda por el nombre de una ciudad
	 * 
	 * @return
	 */
	private static Ciudad busquedaPorNombre() {
		String nombreCodificado = "";
		boolean ciudadValida = false;

		nombreCodificado = getNombreCiudad(nombreCodificado, ciudadValida);

		try {
			String url = "https://api.openweathermap.org/data/2.5/weather?q=" + nombreCodificado
					+ "&mode=xml&lang=sp&appid=" + TOKEN;
			c = XmlUtils.procesarXml(url);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Metodo que pide el nombre hasta que sea correcto, lo codifica para que no
	 * haya errores y lo valida.
	 * 
	 * @param nombreCodificado
	 * @param ciudadValida
	 * @return
	 */
	private static String getNombreCiudad(String nombreCodificado, boolean ciudadValida) {
		String nombre;
		do {
			nombre = getString("Escribe el nombre de una ciudad:");
			if (nombre.isEmpty()) {
				System.out.println(
						"El nombre de la ciudad no puede estar vacío o compuesto solo por espacios. Por favor, introduce un nombre válido.");
			} else {
				try {
					// Codificar el nombre de la ciudad una sola vez
					nombreCodificado = URLEncoder.encode(nombre, StandardCharsets.UTF_8.toString());
					ciudadValida = esCiudadValida(nombreCodificado);
					if (!ciudadValida) {
						System.out.println(
								"La ciudad no es válida o no se encontró. Por favor, introduce un nombre de ciudad válido.");
					}
				} catch (UnsupportedEncodingException e) {
					System.out.println("Error al codificar el nombre de la ciudad: " + e.getMessage());
					return null;
				}
			}
		} while (nombre.isEmpty() || !ciudadValida);
		return nombreCodificado;
	}

	/**
	 * Metodo para validar el nombre de la ciudad, recibe el nombre de la ciudad y
	 * comprueba haciendo una llamada a la api si devuelve algun tipo de error, si
	 * lo hace devuelve un false para qu el bucle de la funcion anterior siga
	 * pidiendo un nombre correcto. Si el nombre es correcto y la api no devuelve
	 * ningun error devuelve un true.
	 * 
	 * @param nombre
	 * @return
	 */
	private static boolean esCiudadValida(String nombre) {
		boolean esValido = true;
		try {
			String url = "https://api.openweathermap.org/data/2.5/weather?q=" + nombre + "&appid=" + TOKEN;
			String respuesta = InternetUtils.readUrl(url);

			if (respuesta == null || respuesta.contains("\"cod\":\"404\"")) {
				esValido = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			esValido = false;
		}
		return esValido;
	}

	/**
	 * Metodo para obtener información de la búsqueda introduciendo latitud y
	 * longitud de una ciudad, un objeto de la clase Ciudad
	 * 
	 * @return
	 */
	private static Ciudad busquedaPorCoordenadas() {
		String latitud = getDouble("Latitud:");
		String longitud = getDouble("Longitud:");
		String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitud + "&lon=" + longitud
				+ "&lang=sp&appid=" + TOKEN;
		c = JsonUtils.leerCiudad(url);
		return c;
	}

	/**
	 * Metodo para validar fechas.
	 * 
	 * @param mensaje
	 * @param formatter
	 * @return
	 */
	public static LocalDate getDate(String mensaje, DateTimeFormatter formatter) {
		LocalDate fecha = null;
		while (fecha == null) {
			System.out.print(mensaje);
			String inputFecha = App.sc.nextLine();

			try {
				fecha = LocalDate.parse(inputFecha, formatter);
			} catch (DateTimeParseException e) {
				System.out.println("Formato de fecha inválido. Intenta de nuevo.");
			}
		}
		return fecha;
	}

	public static String getDouble(String mensaje) {
		double valor = Double.NaN;
		double min = -90;
		double max = 90;
		while (Double.isNaN(valor) || valor < min || valor > max) {
			String input = getString(mensaje);

			try {
				valor = Double.parseDouble(input);
				if (valor < min || valor > max) {
					System.out.println("El valor debe estar entre " + min + " y " + max + ".");
				}
			} catch (NumberFormatException e) {
				System.out.println("Por favor, introduce un número válido.");
			}
		}
		return String.valueOf(valor);
	}

	/**
	 * Metodo para validar enteros.
	 * 
	 * @param mensaje
	 * @return
	 */
	public static int getInt(String mensaje) {
		System.out.print(mensaje);
		int entero = -1;
		boolean valido = true;
		do {
			try {
				entero = sc.nextInt();
				valido = true;
			} catch (InputMismatchException e) {
				System.out.print("No es un entero, prueba otra vez: ");
				sc.nextLine();
				valido = false;
			}
		} while (!valido);
		return entero;
	}

	/**
	 * Metodo para recoger strings.
	 * 
	 * @param mensaje
	 * @return
	 */
	public static String getString(String mensaje) {
		System.out.print(mensaje);
		return sc.nextLine().trim();
	}
}
