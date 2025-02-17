package com.jonathangf.EjercicioTema1.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * La clase Ciudad se utiliza para contruir un objeto y representar las
 * busquedas que se hacen en las llamadas a la API OpenWeather
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Ciudad implements Serializable {
	@SerializedName("name")
	private String nombre;
	private String pais;
	private double temperatura;
	private String humedad;
	private String main;
	private String description;
	@ToString.Exclude
	private double latidud;
	@ToString.Exclude
	private double longitud;
	@EqualsAndHashCode.Include
	private LocalDate fecha;

	public String toString() {
		double tempCelsius = (double) (temperatura - 273.15);
		return "\nCiudad: " + nombre + " (" + pais + ")\n" + "Temperatura: " + String.format("%.2f", tempCelsius)
				+ "º (" + String.format("%.2f", temperatura) + "K)\n" + "Humedad: " + humedad + "%\n" + "Clima: " + main
				+ " - " + description;
	}
}