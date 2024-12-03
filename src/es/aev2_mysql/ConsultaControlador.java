package es.aev2_mysql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

/**
 * En esta clase vamos a manejar las acciones del usuario corriente, al logearse,
 * le dirige al formulario "ConsultaVista" con el que puede interactuar con menú consultas.
 *  
 * @author Usuario
 *
 */
public class ConsultaControlador {

	private final ConsultaVista consultaVista = new ConsultaVista();
	private final Modelo modelo;
	private String usuarioLogeado;

	public ConsultaControlador(ConsultaVista consulta, Modelo modelo, String usuarioLogeado) {
		this.modelo = modelo;
		this.usuarioLogeado = usuarioLogeado;

		this.consultaVista.setVisible(true);

		this.consultaVista.getBtnAceptar().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ejecutarConsulta();

			}

		});
		
	
		this.consultaVista.getBtnExportCVS().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					exportarCSV();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 *En esta acciÁn permite que el usuario haga las consultas,
	 *dependiendo de si es de tipo cliente o admin, le permitira hacer más consultas o 
	 *menos.
	 */
	private void ejecutarConsulta() {
		String consulta = consultaVista.getConsultaTextField().getText().trim();

		if (!consulta.toLowerCase().startsWith("select")) {
			JOptionPane.showMessageDialog(consultaVista, "Error solo se permite consultas SELECT");
			
			return;
		}

		if (usuarioLogeado.equals("client") && !consulta.toLowerCase().contains("population")) {
			JOptionPane.showMessageDialog(consultaVista, "Denegado: los usuarios clieentes solo pueden consultar la tabla: Population");
			return;
		}
		try (Connection conn = modelo.conectarseBd("Pepe", "123");
				Statement sta = conn.createStatement();
				ResultSet rs = sta.executeQuery(consulta)) {

			StringBuilder resultados = new StringBuilder();
			ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			//crear tabla
			DefaultTableModel model = new DefaultTableModel();
			for (int i = 1; i <= columnCount; i++) {
				resultados.append(metaData.getColumnName(i));
			}
			while (rs.next()) {
				Object[] row = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					row[i - 1]=rs.getObject(i);
					
				}
				model.addRow(row);
			}
			consultaVista.getResultadoTabla().setModel(model);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(consultaVista, "Error al ejecutar consulta");
		}

	}

	private void exportarCSV() throws IOException {
		DefaultTableModel model=(DefaultTableModel) consultaVista.getResultadoTabla().getModel();
		int rowCount=model.getRowCount();
		int columnCount= model.getColumnCount();
		
		if (rowCount == 0 ){
			JOptionPane.showMessageDialog(consultaVista, "No hay datos para exportar");
			return;
		}
		
		try (BufferedWriter wr = new BufferedWriter(new FileWriter("resultados.csv"))) {
			
			for(int i=0; i< columnCount; i++) {
				wr.write(model.getColumnName(i)+(i<columnCount - 1 ? ";" : ""));
			}
			wr.newLine();
			for (int i=0; i< rowCount; i++) {
				for(int j=0; j< columnCount; i++) {
				wr.write(model.getValueAt(i, j).toString() + (j<columnCount-1 ? ";" : ""));
				wr.newLine();
			}
				JOptionPane.showMessageDialog(consultaVista, "Datos exportados correctamente a Resultados.csv");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(consultaVista, "Error al exportar csv: " + e.getMessage());
		}

	}
}
