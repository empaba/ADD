package es.EmmaParis.swing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Modelo {

	public Modelo() {

	}

	/**
	 * Dado un directorio por el usuario, comprueba que existe y que es un directorio, si no es válido imprime mensaje de error
	 * si lo es, invoca al metodo listarDirectorios.
	 *  
	 * @param directorio: elegido por usuario
	 * @param busqueda: palabra a buscar
	 * @param ignorarMayus: filtrar respete o no las mayúsculas
	 * @param ignorarAcentos: filtrar que respete o no los acentos
	 * @return
	 * @throws IOException
	 */
	public int busquedaDirectorio(File directorio, String busqueda, boolean ignorarMayus, boolean ignorarAcentos)
			throws IOException {
		int totalCoincidencias;
		if (directorio.exists() && directorio.isDirectory()) {
			System.out.println("Contenido de " + directorio.getAbsolutePath() + ":");
			totalCoincidencias = listarDirectorios(directorio, "", busqueda, ignorarMayus, ignorarAcentos, null);
			System.out.println("Total coincidencias encontradas: " + totalCoincidencias);
			return totalCoincidencias;
		} else {
			System.out.println("No es un directorio válido");
			return 0;
		}

	}


	/**
	 * Muestra los directorios, subdirectorios,archivos y ficheros que contenga el directorio seleccionado por el usuario
	 * @param dir
	 * @param indent: indexe los resultados
	 * @param busqueda
	 * @param ignorarMayus
	 * @param ignorarAcentos
	 * @param textArea: se imprima en la parte inferior de la app
	 * @return
	 * @throws IOException
	 */
	public int listarDirectorios(File dir, String indent, String busqueda, boolean ignorarMayus, boolean ignorarAcentos,
			JTextArea textArea) throws IOException {
		File[] archivos = dir.listFiles();
		int coincidenciasDirectorio = 0;

		if (archivos != null && archivos.length > 0) {
			for (File archivo : archivos) {
				if (archivo.isDirectory()) {
					if (textArea != null) {
						textArea.append(indent + archivo.getName() + "(directorio)\n");
					}
					coincidenciasDirectorio += listarDirectorios(archivo, indent + "|-- ", busqueda, ignorarMayus,
							ignorarAcentos, textArea);
				} else {

					int coincidenciasArchivo = buscarEnArchivos(archivo, busqueda, ignorarMayus, ignorarAcentos);
					coincidenciasDirectorio += coincidenciasArchivo;

					if (textArea != null) {
						String nombreArchivo = archivo.getName();
						String sizeFormatted = formatoTamanyo(archivo.length());
						String fechaModifica = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
								.format(new Date(archivo.lastModified()));
						String info = String.format("%s|-- %s(%s- %s)(%d )\n", indent, nombreArchivo, sizeFormatted,
								fechaModifica, coincidenciasArchivo);
						textArea.append(info);
					}
				}
			}

		}
		return coincidenciasDirectorio;
	}

	
	/**Método para formatear tamaño bytes
	 * @param bytes
	 * @return
	 */
	private String formatoTamanyo(long bytes) {
		double size = bytes / 1024.0;
		String format;

		if (size < 1024) {
			format = "%.1f KB";
		} else {
			size /= 1024;
			format = "%.1f MB";
		}
		return String.format(format, size);
	}

	
	/**Método para eliminar acentos
	 * @param str palabra buscada
	 * @return
	 */
	public static String eliminarAcentos(String str) {
		String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
		return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static int buscarEnArchivos(File archivo, String busqueda, boolean ignorarMayus, boolean ignorarAcentos)
			throws IOException {

		if (archivo.getName().endsWith(".pdf")) {
			return buscarPdf(archivo, busqueda, ignorarMayus, ignorarAcentos);
		} else if(archivo.getName().endsWith(".txt")){
			return buscarEnTextoPlano(archivo, busqueda, ignorarMayus, ignorarAcentos);
		}
		return 0;
	}

	public static int buscarEnTextoPlano(File archivo, String busqueda, boolean ignorarMayus, boolean ignorarAcentos) {
		int coincidencias = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (coincidenciasEncontradas(linea, busqueda, ignorarMayus, ignorarAcentos)) {
					coincidencias++;
				}
			}

		} catch (IOException e) {
			System.out.println("Error al leer el archivo" + archivo.getAbsolutePath());
		}
		return coincidencias;
	}

	public static int buscarPdf(File archivo, String busqueda, boolean ignorarMayus, boolean ignorarAcentos)
			throws IOException {
		int coincidencias = 0;
		try (PDDocument documento = PDDocument.load(archivo)) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String texto = pdfStripper.getText(documento);
			String[] lineas = texto.split("\\r?\\n");

			for (int i = 0; i < lineas.length; i++) {
				if (coincidenciasEncontradas(lineas[i], busqueda, ignorarMayus, ignorarAcentos)) {
					//System.out.println("Texto encontrado en " + archivo.getName() + "Linea: " + (1 + i));
					coincidencias++;
				}
			}

		} catch (IOException e) {
			System.out.println("Error al leer el PDF: " + archivo.getAbsolutePath());
		}
		return coincidencias;
	}

	
	/**Verifica las coincidencias entre el texto y busqueda de la cadena
	 * @param texto
	 * @param busqueda
	 * @param ignorarMayus
	 * @param ignorarAcentos
	 * @return
	 */
	public static boolean coincidenciasEncontradas(String texto, String busqueda, boolean ignorarMayus,
			boolean ignorarAcentos) {

		if (ignorarAcentos) {
			texto = eliminarAcentos(texto);
			busqueda = eliminarAcentos(busqueda);
		}

		if (ignorarMayus) {
			texto = texto.toLowerCase();
			busqueda = busqueda.toLowerCase();
		}
		return texto.contains(busqueda);
	}

	/**Método para reemplaza una palabra/cadena de texto en los archivos
	 * @param palabraBuscada
	 * @param palabraRemplazo
	 * @param rutaArchivo
	 * @param rutaDestino
	 * @param ignorarMayus
	 * @param ignorarAcentos
	 * @return
	 */
	public int reemplazarCadena(String palabraBuscada, String palabraRemplazo, String rutaArchivo, String rutaDestino,
			boolean ignorarMayus, boolean ignorarAcentos) {
		int totalCambios = 0;
		File directorio = new File(rutaArchivo);
		if (!directorio.exists() || !directorio.isDirectory()) {
			System.out.println("Ruta de origen inválida");
			return totalCambios;
		}
		File[] archivos = directorio.listFiles();
		if (archivos != null) {
			for (File archivo : archivos) {
				if (archivo.isDirectory()) {
					totalCambios += reemplazarCadena(palabraBuscada, palabraRemplazo, archivo.getAbsolutePath(),
							rutaDestino, ignorarMayus, ignorarAcentos);
				} else if (archivo.getName().toLowerCase().endsWith(".txt")) {
					totalCambios += reemplazarEnArchivo(archivo, palabraBuscada, palabraRemplazo, ignorarMayus,
							ignorarAcentos);
				}
			}
		}
		return totalCambios;
	}


	/**Reemplaza la palabra en un archivo concreto
	 * @param archivo
	 * @param palabraBuscada
	 * @param palabraRemplazo
	 * @param ignorarMayus
	 * @param ignorarAcentos
	 * @return
	 */
	private int reemplazarEnArchivo(File archivo, String palabraBuscada, String palabraRemplazo, boolean ignorarMayus,
			boolean ignorarAcentos) {

		int coincidencias = 0;
		File temporal = new File(archivo.getAbsoluteFile() + ".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(archivo));
				BufferedWriter bw = new BufferedWriter(new FileWriter(temporal))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String lineaProcesada = linea;
				String palabraProcesada = palabraBuscada;
				

				if (ignorarAcentos) {
					lineaProcesada = eliminarAcentos(lineaProcesada);
					palabraProcesada = eliminarAcentos(palabraProcesada);
				}
				
				if (ignorarMayus) {
					lineaProcesada = lineaProcesada.toLowerCase();
					palabraProcesada = palabraProcesada.toLowerCase();
				}

				int ocurrencias = contarOcurrencias(lineaProcesada, palabraProcesada);
				coincidencias += ocurrencias;

				
				String lineaNueva = reemplazarPalabra(linea, palabraBuscada, palabraRemplazo, ignorarMayus,
						ignorarAcentos);
				bw.write(lineaNueva + "\n");
			}
		} catch (IOException e) {
			System.out.println("error al procesar el archivo: " + archivo.getAbsolutePath());
			return coincidencias;
		}
		
		if (!archivo.delete()) {
			System.out.println("No se pudo eliminar el archivo original: " + archivo.getName());
			return coincidencias;
		}
		if (!temporal.renameTo(archivo)) {
			System.out.println("No se pudo renombrar el archivo" + temporal.getName());
		}
		return coincidencias;
	}

	/**Cuenta las veces que aparece la palabra buscada en la linea
	 * @param linea
	 * @param palabra
	 * @return
	 */
	private int contarOcurrencias(String linea, String palabra) {
		int count = 0;
		int index = 0;
		while ((index = linea.indexOf(palabra, index)) != -1) {
			count++;
			index += palabra.length();
		}
		return count;
	}

	
	/**Cambia todas las veces que aparece la palabra buscada por la palabra que hemos puesto para reemplazar
	 * @param linea
	 * @param palabraBuscada
	 * @param palabraRemplazo
	 * @param ignorarMayus
	 * @param ignorarAcentos
	 * @return
	 */
	private String reemplazarPalabra(String linea, String palabraBuscada, String palabraRemplazo, boolean ignorarMayus,
			boolean ignorarAcentos) {

		if (ignorarMayus) {
			Pattern pattern;

			if (ignorarAcentos) {
				String palabrasSinAcentos = eliminarAcentos(palabraBuscada);
				pattern = Pattern.compile("?i)" + Pattern.quote(palabrasSinAcentos));
				eliminarAcentos(linea);
				return pattern.matcher(linea).replaceAll(palabraRemplazo);
			} else {
				pattern = Pattern.compile("?i)" + Pattern.quote(palabraBuscada));
				return pattern.matcher(linea).replaceAll(palabraRemplazo);
			}
		} else {
			if (ignorarAcentos) {
				String lineaSinAcentos = eliminarAcentos(linea);
				eliminarAcentos(palabraBuscada);
				return lineaSinAcentos.replace(palabraBuscada, palabraRemplazo);
			}
		}
		return palabraRemplazo;
	}

	/**Lee el contenido del archivo
	 * @param rutaArchivo
	 * @return
	 */
	public String leerArchivo(String rutaArchivo) {
		StringBuilder contenido = new StringBuilder();
		try (BufferedReader rd = new BufferedReader(new FileReader(rutaArchivo))) {
			String linea;
			while ((linea = rd.readLine()) != null) {
				contenido.append(linea).append(System.lineSeparator());
			}
		} catch (IOException e) {
			return null;
		}
		return contenido.toString();
	}

}
