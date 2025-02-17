package com.jonathangf.EjercicioTema1.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase Pelicula se utiliza para recoger los datos de las busquedas en la API
 * OmdbApi contruir un objeto con ellos y poder mostrarlo.
 */
@Data
@AllArgsConstructor
public class Pelicula {
	private String Title;
	private String Year;
	private String Director;
	private String Genre;
	private String Plot;
}
