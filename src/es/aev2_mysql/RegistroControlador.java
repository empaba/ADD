package es.aev2_mysql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class RegistroControlador {

	private final RegistroVista registroVista;
	private final Modelo modelo;

	public RegistroControlador(RegistroVista registroVista, Modelo modelo) {
		this.registroVista = registroVista;
		this.modelo = modelo;

		System.out.println("RegistroControlador Iniciando...");

		this.registroVista.setVisible(true);
		this.registroVista.getBtnAceptar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registroUsuario();
			}
		});
	}

	/**
	 * Valida que las contraseñas coincidan y tambien los credenciales desde
	 * el Modelo. Además, abre el formulario segun  el usuario cuando inicia sesion admin se habilita el acceso al registro
	 */
	private void registroUsuario() {
		String user = registroVista.getUserNameField().getText();
		String pass = new String(registroVista.getPasswordField().getPassword());
		String confirmPass = new String(registroVista.getConfirmPasswordField().getPassword());

		if (!pass.equals(confirmPass)) {
			JOptionPane.showMessageDialog(registroVista, "las contraseñas no coinciden)");
			return;
		}

		
		if (modelo.registrarUsuario(user, pass)) {
			JOptionPane.showMessageDialog(registroVista, "Usuario registrado correctamente");
			registroVista.dispose();// 

		} else {
			JOptionPane.showMessageDialog(registroVista, "Error al registrar al usuario");
		}

	}
}
