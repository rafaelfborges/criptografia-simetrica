
public class Principal {
	
	public static void main(String[] args) throws Exception {
		byte[] chaveSessao, nonce;
		/*
		 * Verifica se h� inst�ncia do Objeto KDC, caso houver, retorna a inst�ncia.
		 */
		Kdc kdc = Kdc.getInstance();
		
		/*
		 * Cadastra os usu�rios no KDC (Bob e Alice) e entrega suas respectivas chaves mestre (k_bob e k_alice). 
		 * Nesse momento define-se tamb�m a fun��o em comum para ambos calcularem o Nonce posteriormente.
		 */	
		Nonce funcNonce = new Nonce(10);
		Usuario bob = new Usuario("Bob", funcNonce);
		Usuario alice = new Usuario("Alice", funcNonce);
		kdc.cadastrarUsuario(bob);
		kdc.cadastrarUsuario(alice);
				
		/*
		 * Bob solicita comunica��o com o KDC para falar com Alice. A resposta ser� as chaves de sess�o, uma ser�
		 * armazenada e a outra encaminhada para Alice. Alice ir� decifrar a chave e retornar� um nonce cifrado.
		 */
		chaveSessao = bob.comunicarKdc("Alice");		
		nonce = bob.encaminharChaveSessao(alice, chaveSessao);
		
		/*
		 * Bob decifra o nonce, v�lida na fun��o combinada, gera um novo nonce cifrado e encaminha para Alice.
		 * A resposta dessa solicita��o ser� a valida��o do nounce, confirmando a criptografia ou n�o. 
		 */
		nonce = bob.validaNonce(nonce);
		bob.encaminharNonce(alice, nonce);
	}

}
