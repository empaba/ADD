package es.aev2_mysql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**En esta clase vamos a manejar las acciones del Administrador, al logearse,
 * le dirige al formulario "AdminVista" y puede interactuar
 * el usuario Admin.
 * @author Usuario
 *
 */
public class AdminControlador {
	private final AdminVista adminVista;
	private final Modelo modelo;

	public AdminControlador(AdminVista adminVista, Modelo modelo) {
		this.adminVista = adminVista;
		this.modelo = modelo;
		this.adminVista.setVisible(true);

		this.adminVista.getBtnImportCVS().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				importarCVS();

			}
		});
	}

	/**
	 * En esta acción, nos permite, seleccionar el archivo que vamos a usar, indicandole 
	 * la ubicación en nuestro directorio personal.
	 */
	private void importarCVS() {
		JFileChooser fch = new JFileChooser();
		int result = fch.showOpenDialog(adminVista);

		if (result == JFileChooser.APPROVE_OPTION) {
			File csv = fch.getSelectedFile();
			if (csv == null || !csv.getName().endsWith(".csv")) {
				JOptionPane.showMessageDialog(adminVista, "Archivo seleccionado no es un archivo csv");
				return;
			}
			try {
				modelo.procesarCsv(csv, adminVista.getXmlArea());
				JOptionPane.showMessageDialog(adminVista, "Archivo CSV cargado correctamente");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(adminVista, "Error al cargar el archivo" + e.getMessage());
			}

		}

	}
}
