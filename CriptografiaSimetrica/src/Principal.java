
public class Principal {
	
	public static void main(String[] args) throws Exception {
		byte[] chaveSessao, nonce;
		/*
		 * Verifica se há instância do Objeto KDC, caso houver, retorna a instância.
		 */
		Kdc kdc = Kdc.getInstance();
		
		/*
		 * Cadastra os usuários no KDC (Bob e Alice) e entrega suas respectivas chaves mestre (k_bob e k_alice). 
		 * Nesse momento define-se também a função em comum para ambos calcularem o Nonce posteriormente.
		 */	
		Nonce funcNonce = new Nonce(10);
		Usuario bob = new Usuario("Bob", funcNonce);
		Usuario alice = new Usuario("Alice", funcNonce);
		kdc.cadastrarUsuario(bob);
		kdc.cadastrarUsuario(alice);
				
		/*
		 * Bob solicita comunicação com o KDC para falar com Alice. A resposta será as chaves de sessão, uma será
		 * armazenada e a outra encaminhada para Alice. Alice irá decifrar a chave e retornará um nonce cifrado.
		 */
		chaveSessao = bob.comunicarKdc("Alice");		
		nonce = bob.encaminharChaveSessao(alice, chaveSessao);
		
		/*
		 * Bob decifra o nonce, válida na função combinada, gera um novo nonce cifrado e encaminha para Alice.
		 * A resposta dessa solicitação será a validação do nounce, confirmando a criptografia ou não. 
		 */
		nonce = bob.validaNonce(nonce);
		bob.encaminharNonce(alice, nonce);
	}

}
