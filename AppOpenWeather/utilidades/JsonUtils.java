package com.jonathangf.EjercicioTema1.utilidades;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonathangf.EjercicioTema1.entidades.Ciudad;
//La clase JsonUtils tiene los metodos para trabajas con archivos en formato Json
public class JsonUtils {

	/**
	 * Metodo para extraer un objeto de la clase ciudad haciendo una llamada a la
	 * API openweather pasandole la url, accedera a un archivo en formato json,
	 * extraera los datos que necesito para rellenar los campos del constructor del
	 * objeto, los mostrarla por consola con el formato pedido en el ejercicio y
	 * devolvera el objeto.
	 * 
	 * @param cadenaCompleta
	 * @return
	 */
	public static Ciudad leerCiudad(String cadenaCompleta) {
		Object obj;
		Ciudad ciudad = null;
		try {
			obj = new JSONParser().parse(InternetUtils.readUrl(cadenaCompleta));
			JSONObject jo = (JSONObject) obj;
			String nombre = (String) jo.get("name");

			JSONObject sysObject = (JSONObject) jo.get("sys");
			String pais = (String) sysObject.get("country");

			JSONObject mainObject = (JSONObject) jo.get("main");
			double temperatura = (double) mainObject.get("temp");
			double temperaturaCelsius = (double) (temperatura - 273.15);

			Long humidity = (Long) mainObject.get("humidity");
			String humedad = String.valueOf(humidity);

			JSONArray clima = (JSONArray) jo.get("weather");
			JSONObject weatherObject = (JSONObject) clima.get(0);
			String main = (String) weatherObject.get("main");
			String description = (String) weatherObject.get("description");

			JSONObject coord = (JSONObject) jo.get("coord");

			double longitud = convertToDouble(coord.get("lon"));
			double latitud = convertToDouble(coord.get("lat"));

			ciudad = new Ciudad(nombre, pais, temperatura, humedad, main, description, longitud, latitud,
					LocalDate.now());
			System.out.println(ciudad);

		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ciudad;
	}
	/**
	 * Metodo que recibe una url y un objeto para devolver el objeto en json utilizando la libreria Gson
	 * @param <T>
	 * @param url
	 * @param clase
	 * @return
	 */
	public static <T> T leerGenerico(String url, Class<T> clase) {
		return new Gson().fromJson(InternetUtils.readUrl(url), clase);		
	}
	/**
	 * Metodo que recibe un objeto y lo muestra de manera elegante
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> String crearJsonPretty(T object) {
		return new GsonBuilder()
				.setPrettyPrinting()
				.create()
				.toJson(object);
	}
	/*
	 * Metodo que recoge un objeto y comprueba si es un long y lo convierte en
	 * double
	 */
	private static double convertToDouble(Object object) {
		if (object instanceof Long) {
			object = ((Long) object).doubleValue();
		} else {
			object = (double) object;
		}
		return (double) object;
	}
}
