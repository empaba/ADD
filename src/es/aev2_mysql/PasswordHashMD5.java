package es.aev2_mysql;

import java.security.MessageDigest;

public class PasswordHashMD5 {
	
	//Metodo para generar HASH MD5
/**Permite formatear en HASH las contraseñas que introduce el Administrador
 * cuando realiza el registro de usuarios
 * @param textoPlano
 * @return
 */
public static String generaMD5(String textoPlano) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(textoPlano.getBytes());//CONVIERTE PASSWORD EN BYTES
				byte[] digest = md.digest();//CREA HASH
				StringBuilder sb = new StringBuilder();
				
				//CONVIERTE EL ARRAY EN UNA CADENA HEXADECIMAL
				for (byte b : digest) {
					sb.append(String.format("%02x", b));
				}
				return sb.toString();
			} catch (Exception e) {
				throw new RuntimeException("Error al generar hash MD5", e);
			}
		}
	
}
