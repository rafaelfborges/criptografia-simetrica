import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Criptografia {
	private static final String ALGORITMO = "AES";
	
	public static byte[] cifra(String texto, String chave) {
		try {
			Key key = new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), ALGORITMO);			
			Cipher cifrador = Cipher.getInstance(ALGORITMO);
			cifrador.init(Cipher.ENCRYPT_MODE, key);
			byte[] textoCifrado = cifrador.doFinal(texto.getBytes());
			return textoCifrado;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}	
	
	public static String decifra(byte[] texto, String chave) {
		try {
			Key key = new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), ALGORITMO);
			Cipher decifrador = Cipher.getInstance(ALGORITMO);
			decifrador.init(Cipher.DECRYPT_MODE, key);
	    	byte[] textoDecifrado = decifrador.doFinal(texto);
	    	return new String(textoDecifrado);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
