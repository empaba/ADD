package es.aev2_mysql;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConsultaVista extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTable resultadoTabla;
	private JButton btnExportCVS;
	private JButton btnAceptar;
	private JTextField consultaTextField;
	
	public ConsultaVista() {
		initialize();
	}

	private void initialize() {
		setTitle("Panel Consultas Usuario");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		btnExportCVS = new JButton("Exportar CSV");
		btnExportCVS.setBounds(502, 464, 135, 39);
		btnExportCVS.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(btnExportCVS);

		
		resultadoTabla= new JTable();
		JScrollPane scrollPane= new JScrollPane(resultadoTabla);
		scrollPane.setBounds(30,150,720,286);
		getContentPane().add(scrollPane);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(308, 100, 135, 39);
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(btnAceptar);

		JLabel lblTitulo = new JLabel("Consultas Usuario");
		lblTitulo.setBounds(308, 11, 156, 14);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		getContentPane().add(lblTitulo);
		
		consultaTextField = new JTextField();
		consultaTextField.setBounds(30, 36, 706, 47);
		getContentPane().add(consultaTextField);
		consultaTextField.setColumns(10);
		consultaTextField.setText("Escribe tu consulta SELECT aquí..");

		

	}

	public JButton getBtnExportCVS() {
		return btnExportCVS;
	}

	public JTable getResultadoTabla() {
		return resultadoTabla;
	}
	public JTextField getConsultaTextField() {
		return consultaTextField;
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}
}
