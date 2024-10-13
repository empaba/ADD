package es.EmmaParis.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtDircampo;
	private JTextField textPalabraCampo;
	private JTextField textNuevaPCampo;
	private JTextArea textAreaResult;
	private JButton btnSel;
	private JButton btnLis;
	private JButton btnBuscar;
	private JButton btnReemp;
	private JCheckBox chcbxMayus;
	private JCheckBox chcbxAcen;

	public Vista() {
		initialize();
	}

	/**
	 * Se inician los componentes
	 */
	public void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 872, 566);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Gestor de Archivos y Ficheros");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setBounds(300, 10, 300, 20);
		contentPane.add(lblTitulo);

		JLabel lblDir = new JLabel("Directorio: ");
		lblDir.setBounds(10, 46, 100, 25);
		contentPane.add(lblDir);

		txtDircampo = new JTextField();
		txtDircampo.setBounds(120, 50, 250, 20);
		contentPane.add(txtDircampo);
		txtDircampo.setColumns(10);

		btnSel = new JButton("Seleccionar");
		btnSel.setBounds(380, 49, 120, 23);
		contentPane.add(btnSel);

		btnLis = new JButton("Listar");
		btnLis.setBounds(520, 49, 100, 23);
		contentPane.add(btnLis);

		JLabel lblPalabra = new JLabel("Palabra:");
		lblPalabra.setBounds(10, 100, 100, 25);
		contentPane.add(lblPalabra);

		textPalabraCampo = new JTextField();
		textPalabraCampo.setBounds(120, 100, 250, 20);
		contentPane.add(textPalabraCampo);
		textPalabraCampo.setColumns(10);

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(687, 101, 100, 23);
		contentPane.add(btnBuscar);

		chcbxMayus = new JCheckBox("Mayúsculas");
		chcbxMayus.setBounds(380, 99, 130, 23);
		contentPane.add(chcbxMayus);

		chcbxAcen = new JCheckBox("Acentos");
		chcbxAcen.setBounds(530, 99, 150, 23);
		contentPane.add(chcbxAcen);

		JLabel lblNuevaPalabra = new JLabel("Nueva palabra:");
		lblNuevaPalabra.setBounds(10, 150, 100, 25);
		contentPane.add(lblNuevaPalabra);

		textNuevaPCampo = new JTextField();
		textNuevaPCampo.setBounds(120, 150, 250, 20);
		contentPane.add(textNuevaPCampo);
		textNuevaPCampo.setColumns(10);

		btnReemp = new JButton("Reemplazar");
		btnReemp.setBounds(380, 149, 120, 23);
		contentPane.add(btnReemp);

		JLabel lblResultados = new JLabel("Resultados:");
		lblResultados.setBounds(303, 204, 83, 14);
		contentPane.add(lblResultados);

		textAreaResult = new JTextArea();
		textAreaResult.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textAreaResult);
		scrollPane.setBounds(10, 230, 830, 270);
		contentPane.add(scrollPane);

		this.setVisible(true);
	}

	/**Todos los siguientes getters sirven para que interactuen los componentes, clases 
	 * los elementos de la GUI
	 * @return
	 */
	public JTextArea getTextAreaResult() {
		return textAreaResult;
	}

	public JButton getBtnSel() {
		return btnSel;
	}

	public JButton getBtnLis() {
		return btnLis;
	}

	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	public JButton getBtnReemp() {
		return btnReemp;
	}

	public JCheckBox getChcbxMayus() {
		return chcbxMayus;
	}

	public JCheckBox getChcbxAcen() {
		return chcbxAcen;
	}

	public JTextField getTextPalabraCampo() {

		return textPalabraCampo;
	}

	public JTextField getTextNuevaPCampo() {
		return textNuevaPCampo;
	}

	public JTextField getTextDirCampo() {
		return txtDircampo;
	}

}
