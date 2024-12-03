package es.aev2_mysql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class LoginControlador {
	@SuppressWarnings("unused")
	private static String usuarioLogeado;
	private final Modelo modelo;
	private final LoginVista loginVista;

	public LoginControlador(Modelo modelo, LoginVista loginVista) {
		this.modelo = modelo;
		this.loginVista = loginVista;

		this.loginVista.setVisible(true);

		this.loginVista.getBtnInicio().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				presionaBotonInicio();

			}

		});

		this.loginVista.getBtnCerrar().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				presionaBotonCerrar();

			}

		});

		this.loginVista.getBtnAdminFunc().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				abrirFuncionesAdministrador();

			}

		});
	}

	// Accion boton inicio sesion
	/**En esta acción al pressionar el boton, se logea el usuario.También se validan las credenciales(Modelo), 
	 * si es Admin o usuario corriente. Además,si es usuario Admin muestra el formulario de registro
	 * 
	 */
	private void presionaBotonInicio() {
		String user = loginVista.getUserNameField().getText();
		String pass = new String(loginVista.getPasswordField().getPassword());

		
		if (modelo.validarUsuario(user, pass)) {
			usuarioLogeado = user;
			mostrarMensaje("Bienvenido Usuario" + user);
			
			if ("admin".equals(user)) {
				entradaAdmin();
				abrirFuncionesAdministrador();
				new RegistroControlador(new RegistroVista(), modelo);
				abrirConsultas();
			} else  {
				
				abrirConsultas();
			}

		} else {
			mostrarMensaje("Credenciales incorrectas,intentelo de nuevo");
		}
	}

	/**
	 * Si es un usuario corriente le abre el formulario de consultas
	 * cuando se logea
	 */
	private void abrirConsultas(){
		loginVista.dispose();
		ConsultaVista consultaVista=new ConsultaVista();
				new ConsultaControlador(consultaVista, modelo, usuarioLogeado);
				mostrarMensaje("Bienvenido " + usuarioLogeado + "puedes realizar consultas");
	}
	/**Al pulsar el boton "Cerrar sesión" el usuario logeado
	 * se cierra la sesión, apereciendo para logearse de nuevo
	 * 
	 */
	private void presionaBotonCerrar() {
		usuarioLogeado = null;
		mostrarMensaje("has cerrado sesión correctamente");
		loginVista.dispose();
		new LoginControlador(modelo, new LoginVista());
	}

	/**Mensaje pop up -informativo.
	 * @param mensaje
	 */
	private void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(loginVista, mensaje);
	}

	/**
	 * Nos informa que estamos en modo Admin
	 */
	private void entradaAdmin() {
		mostrarMensaje("acceso permitido para el Administrador");
		
	}

	/**
	 * Si eres usuario Admin puedes acceder a sus funciones
	 * personalizadas
	 */
	private void abrirFuncionesAdministrador() {
		new AdminControlador(new AdminVista(), modelo);
	}

	public static void main(String[] args) {

	}

}