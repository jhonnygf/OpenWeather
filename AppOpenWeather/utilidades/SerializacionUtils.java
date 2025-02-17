package com.jonathangf.EjercicioTema1.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.jonathangf.EjercicioTema1.entidades.Ciudad;
//La clase SerializacionUtils tiene los metodos para serializar objetos
public class SerializacionUtils {
	/**
	 * Metodo para serializar la ultima busqueda realizada, recibe la ruta del
	 * archivo donde voy a serializar los datos del objeto y el objeto, comprueba si
	 * el archivo existe, sino existe lo crea y serializa, si el objeto es nulo
	 * devuelve false.
	 * 
	 * @param <T>
	 * @param rutaCompleta
	 * @param c
	 * @return
	 */
	public static <T> boolean serializarCiudad(String rutaCompleta, T c) {
		boolean resultado = false;
		try {

			File archivo = new File(rutaCompleta);
			List<T> listaActualizada = new ArrayList<>();

			if (archivo.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));
				List<T> listaExistente = (List<T>) ois.readObject();
				ois.close();

				// Elimino una ciudad si tiene la misma fecha
				listaExistente.removeIf(e -> ((Ciudad) e).getFecha().equals(((Ciudad) c).getFecha()));

				// Actualizo la lista
				listaActualizada.addAll(listaExistente);
			}

			// Añado la ultima búsqueda a la lista
			listaActualizada.add(c);

			// Serializo lista actualizada
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));
			oos.writeObject(listaActualizada);
			oos.close();

			resultado = true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * Comprueba si existe un archivo en la ruta, si existe deserializa la lista de
	 * objetos ahi, sino muestra un mensaje y devuelve una lista vacia.
	 * 
	 * @param <T>
	 * @param rutaCompleta
	 * @return
	 */
	public static <T> List<T> desserializarListaObjetos(String rutaCompleta) {

		try {
			File archivo = new File(rutaCompleta);
			if (!archivo.exists()) {
				// Muestro mensaje si el archivo no existe y devuelvo una lista vacía
				System.out.println("No se puede deserializar porque no existe el archivo.");
				return new ArrayList<>();
			}

			ObjectInputStream ficheroObjetos = new ObjectInputStream(new FileInputStream(archivo));
			@SuppressWarnings("unchecked")
			List<T> lista = (List<T>) ficheroObjetos.readObject();
			ficheroObjetos.close();
			System.out.println("Desserialización completa");
			return lista;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
