package com.jonathangf.EjercicioTema1.utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
//La clase IntertUtils tiene los metodos para acceder a las urls
public class InternetUtils {
	/**
	 * Recibe una url en formato cadena y devuelve el contenido de esa Url como una
	 * cadena.
	 * 
	 * @param web la cadena que almacena la url
	 * @return Todo el contenido de esa url en un String
	 */
	public static String readUrl(String web) {
		try {
			URL url = new URI(web).toURL();
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("User-Agent", "PostmanRuntime/7.20.1");
			uc.connect();
			String lines = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8))
				.lines().collect(Collectors.joining());
			// System.out.println(lines);
			return lines;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("No se ha podido leer la URL: " + web);
			System.out.println(e.getStackTrace() + " " + e.getCause());
		}
		return null;
	}
}
