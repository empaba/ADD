package es.aev2_mysql;

import java.awt.Font;

import javax.swing.*;

public class AdminVista extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton btnImportCVS;
	private JTextArea xmlArea;
	private JButton btnAceptaImport;

	public AdminVista() {
		initialize();
	}

	private void initialize() {
		setTitle("Panel Administrador");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		btnImportCVS = new JButton("Importar CSV");
		btnImportCVS.setBounds(325, 49, 135, 39);
		btnImportCVS.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(btnImportCVS);

		xmlArea = new JTextArea();
		xmlArea.setBounds(50, 150, 700, 400);
		xmlArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(xmlArea);
		scrollPane.setBounds(30, 150, 720, 286);
		getContentPane().add(scrollPane);

		btnAceptaImport = new JButton("Aceptar");
		btnAceptaImport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAceptaImport.setBounds(325, 474, 135, 39);
		getContentPane().add(btnAceptaImport);

		setVisible(true);

	}

	public JButton getBtnImportCVS() {
		return btnImportCVS;
	}

	public JTextArea getXmlArea() {
		return xmlArea;
	}

	public JButton getBtnAceptaImport() {
		return btnAceptaImport;
	}

}
