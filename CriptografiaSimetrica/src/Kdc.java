import java.security.SecureRandom;
import java.util.ArrayList;

public class Kdc 
{
	private static Kdc kdc = null;
	private ArrayList<Usuario> usuarios;
	
	private Kdc() {
		usuarios = new ArrayList<Usuario>();
	}
	
	public static Kdc getInstance() {
		if(kdc == null) {
			kdc = new Kdc();
		}
		return kdc;
	}
	
	public void cadastrarUsuario(Usuario usuario) {
		usuario.setChaveMestre(gerarChave());
		usuarios.add(usuario);
	}
		
	private Usuario buscarUsuario(String nome) {
		for(Usuario usuario: usuarios) {
			if(usuario.getNome().equals(nome)) {
				return usuario;
			}
		}
		return null;
	}
	
	private String gerarChave() {
		String alfabeto = "abcdefghijklmnopqrstuvwxyz";
    		int tamanhoChave = 16;
    		StringBuilder sb = new StringBuilder(tamanhoChave);
    		SecureRandom random = new SecureRandom();
    	
    		for (int i = 0; i < tamanhoChave; i++) {
    			int randCharAt = random.nextInt(alfabeto.length());
    			char randChar = alfabeto.charAt(randCharAt);
    			sb.append(randChar);
    		}
    		return sb.toString();
    	}
	
	public ArrayList<byte[]> solicitarChaveSessao(String nome, byte[] remetente, byte[] destinatario) {
		Usuario usuario = buscarUsuario(nome);

		//Se o usuário existir, decifro o remetente e destinatário.
		if(usuario != null) {
			String usuarioRemetente = Criptografia.decifra(remetente, usuario.getChaveMestre());
			String usuarioDestino = Criptografia.decifra(destinatario, usuario.getChaveMestre());
			
			//Se o usuário remetente for quem ele diz ser, então busco pelo destinatário. 
			if(usuarioRemetente.equals(usuario.getNome())) {
				usuario = buscarUsuario(usuarioDestino);
				
				//Se o destinatário existe, então retorno a chave de sessão para a solicitação.
				if(usuario != null) {
					String chaveSessao = gerarChave(); //ChaveSessao gerada aleatoriamente.
					
					//Gera a chave de sessão do remetente, exemplo: kSessao na kBob.
					usuario = buscarUsuario(usuarioRemetente);
					byte[] chaveSessaoRemetente = Criptografia.cifra(chaveSessao, usuario.getChaveMestre());
					
					//Gera a chave de sessão do destinatario, exemplo: kSessao na kAlice.
					usuario = buscarUsuario(usuarioDestino);
					byte[] chaveSessaoDestinatario = Criptografia.cifra(chaveSessao, usuario.getChaveMestre());
					
					//Retorna a requisição para o usuário.
					ArrayList<byte[]> requisicao = new ArrayList<byte[]>();
					requisicao.add(chaveSessaoRemetente);
					requisicao.add(chaveSessaoDestinatario);
					
					return requisicao;
				} else {
					System.out.println("Destinatário inexistente!");
				}
			}
		} else {
			System.out.println("Remetente inválido!");
		}
		return null;
	}
}
