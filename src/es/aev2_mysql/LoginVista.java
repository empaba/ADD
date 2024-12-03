package es.aev2_mysql;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//La interfaz con la que interactua el usuario
public class LoginVista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField usuarioField;
	private JPasswordField passwordField;
	private JButton btnInicio;
	private JButton btnAdminFunc;
	private JButton btnCerrar;

	public LoginVista() {
		initialize();
	}

	private void initialize() {
		setTitle("Login");
		setBounds(100, 100, 887, 628);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblUsuario = new JLabel(" Usuario:");
		lblUsuario.setBounds(126, 49, 75, 52);
		getContentPane().add(lblUsuario);
		usuarioField = new JTextField();
		usuarioField.setBounds(225, 44, 270, 63);
		getContentPane().add(usuarioField);

		JLabel lblPassword = new JLabel(" Password:");
		lblPassword.setBounds(126, 150, 75, 52);
		getContentPane().add(lblPassword);
		passwordField = new JPasswordField();
		passwordField.setBounds(225, 145, 270, 63);
		getContentPane().add(passwordField);

		btnInicio = new JButton("Iniciar sesión");
		btnInicio.setBounds(82, 269, 173, 63);
		getContentPane().add(btnInicio);

		btnAdminFunc = new JButton("Funciones Admin");
		btnAdminFunc.setBounds(309, 269, 173, 63);
		btnAdminFunc.setEnabled(false);
		getContentPane().add(btnAdminFunc);

		btnCerrar = new JButton("Cerrar sesión");
		btnCerrar.setBounds(537, 269, 173, 63);
		getContentPane().add(btnCerrar);

		setVisible(true);
	}

	public JTextField getUserNameField() {
		return usuarioField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JButton getBtnInicio() {
		return btnInicio;
	}

	public JButton getBtnAdminFunc() {
		return btnAdminFunc;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

}
