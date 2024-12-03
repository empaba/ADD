package es.aev2_mysql;



import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class RegistroVista extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField usuariotextField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JButton btnAceptar;

	
 public RegistroVista() {
		initialize();
	}

	
	private void initialize() {
		
		setBounds(100, 100, 684, 506);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		usuariotextField = new JTextField();
		usuariotextField.setBounds(270, 85, 155, 31);
		getContentPane().add(usuariotextField);
		usuariotextField.setColumns(10);
		
		JLabel lblTitulo = new JLabel("Registro de Usuarios");
		lblTitulo.setBounds(238, 11, 187, 31);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		getContentPane().add(lblTitulo);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(201, 91, 59, 14);
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(lblUsuario);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(270, 141, 155, 31);
		getContentPane().add(passwordField);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(176, 147, 84, 14);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(lblPassword);
		
		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(270, 205, 155, 31);
		getContentPane().add(confirmPasswordField);
		
		JLabel lblConfirmarContrasea = new JLabel("Confirmar Contraseña:");
		lblConfirmarContrasea.setBounds(98, 213, 162, 14);
		lblConfirmarContrasea.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(lblConfirmarContrasea);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(290, 283, 135, 39);
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(btnAceptar);
		
		
	}
	public JTextField getUserNameField() {
		return usuariotextField;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public JPasswordField getConfirmPasswordField() {
		return confirmPasswordField;
	}
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
}
