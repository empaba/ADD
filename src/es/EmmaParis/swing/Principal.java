package es.EmmaParis.swing;

import java.io.IOException;

public class Principal {

	public static void main(String[] args) throws IOException {
		Vista vista = new Vista();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo, vista);
	}

}
