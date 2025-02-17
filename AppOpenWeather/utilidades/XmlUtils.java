package com.jonathangf.EjercicioTema1.utilidades;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jonathangf.EjercicioTema1.entidades.Ciudad;
//La clase XmlUtils tiene los metodos para trabajar con archivos en formato Xml
public class XmlUtils {

	/**
	 * Metodo para extraer un objeto de la clase ciudad haciendo una llamada a la
	 * API openweather pasandole la url, accedera a un archivo en formato xml,
	 * extraera los datos que necesito para rellenar los campos del constructor del
	 * objeto, los mostrarla por consola con el formato pedido en el ejercicio 
	 * y devolvera el objeto.
	 * 
	 * @param cadenaCompleta
	 * @param c
	 * @return
	 */
	public static Ciudad procesarXml(String cadenaCompleta) {
		Ciudad c = null;
		try {
			// Este bloque consigue que en "doc" tengamos un xml correcto
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(cadenaCompleta);
			doc.getDocumentElement().normalize();

			Element currentElement = (Element) doc.getElementsByTagName("current").item(0);

			Element cityElement = (Element) currentElement.getElementsByTagName("city").item(0);
			String nombre = cityElement.getAttribute("name");
			String pais = cityElement.getElementsByTagName("country").item(0).getTextContent();
			
			Element coordElement = (Element) cityElement.getElementsByTagName("coord").item(0);
			double latitud = Double.parseDouble(coordElement.getAttribute("lat"));
			double longitud = Double.parseDouble(coordElement.getAttribute("lon"));

			Element temperatureElement = (Element) currentElement.getElementsByTagName("temperature").item(0);
			double temperatura = Double.parseDouble(temperatureElement.getAttribute("value"));
			String unidadTemperatura = temperatureElement.getAttribute("unit");

			Element humidityElement = (Element) currentElement.getElementsByTagName("humidity").item(0);
			String humedad = humidityElement.getAttribute("value");

			Element weatherElement = (Element) currentElement.getElementsByTagName("clouds").item(0);
			String clima = weatherElement.getAttribute("name");
			
			Element descripcionElement = (Element) currentElement.getElementsByTagName("weather").item(0);
			String descripcion = descripcionElement.getAttribute("value");

			c = new Ciudad(nombre, pais, temperatura, humedad, clima, descripcion, longitud, latitud,LocalDate.now());
			System.out.println(c.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}
}
