package es.aev2_mysql;

import javax.swing.SwingUtilities;

public class Principal {

/**Esta es la clase Principal de la aplicación.En mi caso LoginVista es el formulario principal
 * ,en el que según el usuario  se movera por unos formularios u otros. 
 * 
 * @param args
 */
public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Modelo modelo = new Modelo();
			LoginVista loginvista = new LoginVista();

			new LoginControlador(modelo, loginvista);

		});

	}
	
}
