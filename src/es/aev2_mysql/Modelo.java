package es.aev2_mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Modelo {

	private static Connection con;

	/**Metodo permite conectase con la BD 
	 * @param login
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static Connection conectarseBd(String login, String password) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/population";
		con = DriverManager.getConnection(url, login, password);

		return con;
	}


	/**Metodo valida si usuario y contraseña son correctos; formatea el password en Hash, establece los parametros de consulta y 
	 * compara  hash en bd  y valida que la contraseña y usuarios son correctos.
	 * 
	 * @param usuario
	 * @param password
	 * @return
	 */
	public boolean validarUsuario(String usuario, String password) {

		String query = "SELECT * FROM users WHERE login = ? AND password = ? ";

		try (Connection conn = conectarseBd("admin", "admin"); PreparedStatement stmt = conn.prepareStatement(query)) {
			String passHash = PasswordHashMD5.generaMD5(password);

			stmt.setString(1, usuario);
			stmt.setString(2, passHash);

			try (ResultSet rs = stmt.executeQuery()) {
				
				return rs.next();
			}

		} catch (SQLException e) {
			System.err.println("Error validando usuario: " + e.getMessage());
			return false;
		}

	}

	
	/** Primero conecta con la bd, luego Registra el usuario,lo inserta en la tabla "users"
	 * con la contraseña encriptada y le asina permisos 
	 * @param usuario
	 * @param password
	 * @return
	 */
	public boolean registrarUsuario(String usuario, String password) {
		String passHash = PasswordHashMD5.generaMD5(password);
		String insertQuery = "INSERT INTO users (login, password,type) VALUES(?,?,'client')";
		String createUserQuery = "CREATE USER ? IDENTIFIED BY ?";
		String grantQuery = "GRANT SELECT ON population.population TO ?";

		try (Connection conn = conectarseBd("admin", "admin"); 
				PreparedStatement ps = conn.prepareStatement(insertQuery);
				PreparedStatement createUser = conn.prepareStatement(createUserQuery);
				PreparedStatement grantPs = conn.prepareStatement(grantQuery)) {

			ps.setString(1, usuario);
			ps.setString(2, passHash); 
			ps.executeUpdate();

			createUser.setString(1, usuario);
			createUser.setString(2, passHash);
			createUser.executeUpdate();

			grantPs.setString(1, usuario);
			grantPs.executeUpdate();

			return true;

		} catch (SQLException e) {
			System.err.println("Error registrando usuario: " + e.getMessage());
			return false;
		}
	}

	// metodo principal carga csv
	/**En este metodo intervienen distintos metodos 
	 * complementarios o auxiliares que permiten realizar distintas operaciones en un mismo metodo
	 * Por ejemplo, que limpie los encabezados del csv para que peermita leer y almacenar las lineas
	 * O que cree un archivo xml, l,o muestro por textArea y lo insertre en la base de datos
	 * @param csv
	 * @param xmlArea
	 * @throws IOException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public void procesarCsv(File csv, JTextArea xmlArea)
			throws IOException, SQLException, ParserConfigurationException, SAXException, TransformerException {
		try {
			validarCsv(csv);
			String[] headers;
			List<String[]> data = new ArrayList<>();
			List<String[]>datosInconsistentes=new ArrayList<>();

			try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
				String linea = br.readLine();
				if (linea != null) {
					headers = linea.split(";");

					for (int i = 0; i < headers.length; i++) {
						String limpio = limpiarNombreColumna(headers[i]);
						System.out.println("Encabezado original: " + headers[i] + " -> limpiado: " + limpio);
					}

					borrarTabla("population");
					crearTabla(headers);

					linea = br.readLine();
					while (linea != null) {
						linea=linea.trim();
						String[] values = linea.split(",");
						
						for(int i=0;i<values.length;i++) {
							values[i]=values[i].trim().replace(",", ".");
						}
						
						if (values.length != headers.length) {
							System.err.println("Fila de datos con datos inconsistentes" + Arrays.toString(values));
							datosInconsistentes.add(values);
							linea=br.readLine();
							continue;
						}
						data.add(values);

						String country = obtenerCampo(headers, values, "country");
						if (country != null) {
							crearXml(country, headers, values);
						}
						linea=br.readLine();
					}
					
					mostrarContenidoXml(xmlArea);

					insertarDatos(headers);
					System.out.println("Insertado correctamente");
					
					 String archivo ="countries.xml";
					
					if(!datosInconsistentes.isEmpty()){
						crearDatosInconsistentes(archivo, headers, datosInconsistentes);
						System.out.println("Archivo xml creado correctamente");
					}

				}

			} catch (IOException | SQLException | ParserConfigurationException | SAXException
					| TransformerException e) {
				e.printStackTrace();
				System.out.println("Error procesado archivo CSV " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Error general "+ e.getMessage());
		}

	}

	
	/**Crea el archivo xml a partir de la lectura del csv
	 * @param archivo
	 * @param headers
	 * @param datosInconsistentes
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	private void crearDatosInconsistentes(String archivo, String[] headers, List<String[]> datosInconsistentes) throws ParserConfigurationException, TransformerException {
		//crea doc xml
		DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder build = dFact.newDocumentBuilder();
		Document doc = build.newDocument();

		Element raiz = doc.createElement("Countries");
		doc.appendChild(raiz);
		
		//agrega cada fila
		for (String[] values: datosInconsistentes) {
			Element record = doc.createElement("Record");
			raiz.appendChild(record);
			
			for(int i=0;i<headers.length;i++) {
				Element campo=doc.createElement(headers[i]);
				String value=i<values.length ? values[i]: "MISSING";
				campo.appendChild(doc.createTextNode(value));
				record.appendChild(campo);
			}
		}
		// guardar documento en disco formateando el xml
					TransformerFactory tranFactory = TransformerFactory.newInstance(); // Crear serializador
					Transformer aTransformer = tranFactory.newTransformer();

					aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); // darle Formato al documento
					aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
					aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

					DOMSource source = new DOMSource(doc);
					try {
						
						Path currentDir= Paths.get("").toAbsolutePath();
						System.out.println("directorio Actual;" + currentDir);
						File archivoXML= new File(currentDir.toFile(), archivo);
						
						// Definir el nombre del fichero y guardar
						FileWriter fw = new FileWriter(archivoXML);
						StreamResult result = new StreamResult(fw);
						aTransformer.transform(source, result);
						fw.close();
						System.out.println("archivo guardado" + archivoXML.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
	}

	/** Este metodo comprueba que el csv tiene datos
	 * @param csv
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void validarCsv(File csv) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			String linea = br.readLine();
			if (linea == null || linea.trim().isEmpty()) {
				throw new IOException("El archivo CSV está vacio");
			}
			String[] headers = linea.split(",");
			if (headers.length == 0) {
				throw new IOException("encabezados no validos");
			}
			System.out.println("Encabezados encontrados: " + Arrays.toString(headers));
		}
	}

	
	/**Metodo  permite borrar la tabla population  que fue creada originalmente
	 * @param nombreTabla
	 * @throws SQLException
	 */
	private void borrarTabla(String nombreTabla) throws SQLException {
		try (Connection con = conectarseBd("admin", "admin"); java.sql.Statement stm = con.createStatement()) {
			stm.execute("DROP TABLE IF EXISTS " + nombreTabla);
		}
	}

	/**Metodo para crear una nueva tabla
	 * @param headers
	 * @throws SQLException
	 */
	private void crearTabla(String[] headers) throws SQLException {

		StringBuilder creatabla = new StringBuilder("CREATE TABLE population(");
		for (int i = 0; i < headers.length; i++) {
			creatabla.append(limpiarNombreColumna(headers[i])).append(" VARCHAR(30)");
			if (i < headers.length - 1) {
				creatabla.append(", ");
			}
		}
		creatabla.append(")");

		System.out.println(creatabla);

		try (Connection con = conectarseBd("admin", "admin"); java.sql.Statement stm = con.createStatement()) {
			stm.execute(creatabla.toString());
			System.out.println("Tabla creada correctamente");
		}
	}

	
	/**En este metodo se crea un documento xml, añade los datos de la fila, guarda el fichero en disco formateandolo al xml
	 * @param country
	 * @param headers
	 * @param values
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	private void crearXml(String country, String[] headers, String[] values)
			throws IOException, ParserConfigurationException, SAXException, TransformerException {
		
		File directorioXml = new File("xml");
		if (!directorioXml.exists()) {
			directorioXml.mkdirs();
		}
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.newDocument();
		
		Element raiz = document.createElement("country-data");
		document.appendChild(raiz);
		
		for (int i = 0; i < headers.length; i++) {
			Element element = document.createElement(headers[i]);
			element.setTextContent(values[i]);
			raiz.appendChild(element);
		}

		TransformerFactory tranFactory = TransformerFactory.newInstance();
		Transformer aTransformer = tranFactory.newTransformer();

		aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); 
		aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource source = new DOMSource(document);
		
		StreamResult result = new StreamResult(new File(directorioXml, country + ".xml"));
		aTransformer.transform(source, result);
	}

	private String limpiarNombreColumna(String columna) {
		return columna.replaceAll("[^a-zA-Z0-9-]", "_").toLowerCase();
	}
	private String obtenerNodo(Document document, String tagName) {
		Node node=document.getElementsByTagName(tagName).item(0);
		return node != null ? node.getTextContent(): null;
	}

	/**Inserta los datos que proviene de los encabezados del csv y
	 * @param headers
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void insertarDatos(String[] headers)
			throws SQLException, ParserConfigurationException, SAXException, IOException {
		File directorioXml = new File("xml");
		File[] archivosXml = directorioXml.listFiles((dir, name) -> name.endsWith(".xml"));
		String insertQuery = "INSERT INTO population(" + String.join(",", headers) + ") VALUES ("
				+ "?,".repeat(headers.length).replaceAll(",$", "") + ")";
		if (archivosXml != null) {
			try (Connection con = conectarseBd("admin", "admin")) {
				try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
					for (File archivoXml : archivosXml) {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document document = dBuilder.parse(archivoXml);

						NodeList nodeList = document.getDocumentElement().getChildNodes();
						String[] filaDatos = new String[headers.length];

						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							System.out.println("");
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								for (int j = 0; j < headers.length; j++) {
									if (node.getNodeName().equalsIgnoreCase(headers[j])) {
										filaDatos[j] = node.getTextContent();
									}
								}

							}

						}
						for (int i = 0; i < filaDatos.length; i++) {
							String values= obtenerNodo(document, filaDatos[i]);
							stmt.setString(i + 1, values);
						}
						stmt.executeUpdate();
					}
				}
			}
		}
	}

	
	/**Metodo permite obtener el valor de un campo especifico
	 * @param headers
	 * @param values
	 * @param campo
	 * @return
	 */
	private String obtenerCampo(String[] headers, String[] values, String campo) {
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].equalsIgnoreCase(campo)) {
				return values[i];
			}
		}
		return null;
	}

	
	/**Metodo que permite mostrar el contenido en el textArea
	 * @param xmlArea
	 * @throws IOException
	 */
	private void mostrarContenidoXml(JTextArea xmlArea) throws IOException {
		StringBuilder concatenaXmlContenido = new StringBuilder();
		File directorioXml = new File("xml");
		File[] ficherosXml = directorioXml.listFiles((dir, name) -> name.endsWith(".xml"));

		if (ficherosXml != null) {
			for (File file : ficherosXml) {
				concatenaXmlContenido.append(Files.readString(file.toPath(), StandardCharsets.UTF_8)).append("\n\n");

			}
		}
		xmlArea.setText(concatenaXmlContenido.toString());
	}
	

}
