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

		//Se o usu�rio existir, decifro o remetente e destinat�rio.
		if(usuario != null) {
			String usuarioRemetente = Criptografia.decifra(remetente, usuario.getChaveMestre());
			String usuarioDestino = Criptografia.decifra(destinatario, usuario.getChaveMestre());
			
			//Se o usu�rio remetente for quem ele diz ser, ent�o busco pelo destinat�rio. 
			if(usuarioRemetente.equals(usuario.getNome())) {
				usuario = buscarUsuario(usuarioDestino);
				
				//Se o destinat�rio existe, ent�o retorno a chave de sess�o para a solicita��o.
				if(usuario != null) {
					String chaveSessao = gerarChave(); //ChaveSessao gerada aleatoriamente.
					
					//Gera a chave de sess�o do remetente, exemplo: kSessao na kBob.
					usuario = buscarUsuario(usuarioRemetente);
					byte[] chaveSessaoRemetente = Criptografia.cifra(chaveSessao, usuario.getChaveMestre());
					
					//Gera a chave de sess�o do destinatario, exemplo: kSessao na kAlice.
					usuario = buscarUsuario(usuarioDestino);
					byte[] chaveSessaoDestinatario = Criptografia.cifra(chaveSessao, usuario.getChaveMestre());
					
					//Retorna a requisi��o para o usu�rio.
					ArrayList<byte[]> requisicao = new ArrayList<byte[]>();
					requisicao.add(chaveSessaoRemetente);
					requisicao.add(chaveSessaoDestinatario);
					
					return requisicao;
				} else {
					System.out.println("Destinat�rio inexistente!");
				}
			}
		} else {
			System.out.println("Remetente inv�lido!");
		}
		return null;
	}
}
