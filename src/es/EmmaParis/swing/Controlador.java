
package es.EmmaParis.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class Controlador {
	private Modelo modelo;
	private Vista vista;

	public Controlador(Modelo modelo, Vista vista) {

		this.modelo = modelo;
		this.vista = vista;
		control();
	}

	
	/**Asignamos listeners a botones, para que se realice la acción al pulsarlos
	 * 
	 */
	public void control() {

		
		vista.getBtnSel().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				seleccionarDirectorio();
			}

		});

		
		vista.getBtnLis().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				listarDirectorio();
			}

		});

		
		vista.getBtnBuscar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				buscarPalabra();
			}
		});

		
		vista.getBtnReemp().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				reemplazarPalabra();

			}
		});

	}

	/**JFILECHOSSER paraq ue el usuario seleccione el directorio
	 * 
	 */
	private void seleccionarDirectorio() {
		JFileChooser selec = new JFileChooser();
		selec.setCurrentDirectory(new java.io.File("."));
		selec.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		selec.setAcceptAllFileFilterUsed(false);

		if (selec.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
			File directorioSel = selec.getSelectedFile();
			vista.getTextDirCampo().setText(directorioSel.getAbsolutePath());

		}
	}

	
	/**
	 * Lista los archivos y directorios seleccionados y los muestra en el panel inferior de la app
	 * Usamos SwingWorker para evitar que la interfaz se congele.
	 */
	private void listarDirectorio() {
		String ruta = vista.getTextDirCampo().getText().trim();
		if (ruta.isEmpty()) {
			JOptionPane.showMessageDialog(vista, "Por favor introduce un directorio: ", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		File directorio = new File(ruta);
		if (!directorio.exists() || !directorio.isDirectory()) {
			JOptionPane.showMessageDialog(vista, "No es un directorio válido ", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}

		bloquearComponentes(true);

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				vista.getTextAreaResult().setText("");

				try {
					int totalCoincidencias = modelo.listarDirectorios(directorio, "", "", false, false,
							vista.getTextAreaResult());
					vista.getTextAreaResult().append("Total coincidencias encontradas; " + totalCoincidencias);

				} catch (IOException e) {

					JOptionPane.showMessageDialog(null, "Error al listar directorios" + e.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
				return null;

			}

			@Override
			protected void done() {
				bloquearComponentes(false);
			}
		};
		worker.execute();
	}

	/**
	 * Busca una palabra en los archivos del directorio seleccionado
	 */
	private void buscarPalabra() {
		String ruta = vista.getTextDirCampo().getText().trim();
		String busqueda = vista.getTextPalabraCampo().getText().trim();
		boolean ignorarMayus = vista.getChcbxMayus().isSelected();
		boolean ignorarAcentos = vista.getChcbxAcen().isSelected();

		if (ruta.isEmpty() || busqueda.isEmpty()) {
			JOptionPane.showMessageDialog(vista, "Por favor ingresa una palabra para buscar", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		vista.getTextAreaResult().setText("");

		try {
			File directorio = new File(ruta);
			if (!directorio.exists() || !directorio.isDirectory()) {
				JOptionPane.showMessageDialog(vista, "No es un directorio válido ", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int totalCoincidencias = modelo.busquedaDirectorio(directorio, busqueda, ignorarMayus, ignorarAcentos);
			vista.getTextAreaResult().append("Total coincidencias encontradas" + totalCoincidencias);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(vista, "Error al buscar la palabra: " + e.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * Reemplaza una palabra por otra en los archivos del directorio seleccionado
	 */
	private void reemplazarPalabra() {
		String ruta = vista.getTextDirCampo().getText().trim();
		String palabraBuscada = vista.getTextPalabraCampo().getText().trim();
		String palabraReemplazo = vista.getTextNuevaPCampo().getText().trim();
		boolean ignorarMayus = vista.getChcbxMayus().isSelected();
		boolean ignorarAcentos = vista.getChcbxAcen().isSelected();

		if (ruta.isEmpty() || palabraBuscada.isEmpty() || palabraReemplazo.isEmpty()) {
			JOptionPane.showMessageDialog(null, " por favor ingrese la palabra que desea buscar y reemplazar ",
					"Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}
		File directorio = new File(ruta);
		if (!directorio.exists() || !directorio.isDirectory()) {
			JOptionPane.showMessageDialog(vista, "No es un directorio válido ", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String rutaDestino = ruta;
		int totalCambios = modelo.reemplazarCadena(palabraBuscada, palabraReemplazo, ruta, rutaDestino, ignorarMayus,
				ignorarAcentos);

		if (totalCambios > 0) {
			vista.getTextAreaResult().append("Total cambios: " + totalCambios);
			JOptionPane.showMessageDialog(vista, "Palabra reemplazada con exito", "Bravo",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			vista.getTextAreaResult().append("No se encontraraon coincidencias en los archivos");
			JOptionPane.showMessageDialog(vista, "No se encontraron coincidencias", "Información",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	
	/**Bloquea los botones cuando se esta listando el directorio
	 * @param bloquear
	 */
	private void bloquearComponentes(boolean bloquear) {
		vista.getBtnBuscar().setEnabled(!bloquear);
		vista.getBtnReemp().setEnabled(!bloquear);
		vista.getChcbxMayus().setEnabled(!bloquear);
		vista.getChcbxAcen().setEnabled(!bloquear);
	}

}
