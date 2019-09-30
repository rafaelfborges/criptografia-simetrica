import java.util.ArrayList;
import java.util.Random;

public class Usuario {
	private String nome;
	private String chaveMestre;
	private String chaveSessao;
	private Nonce funcNonce;
	private int nonce;
	
	
	public Usuario(String nome, Nonce funcNonce) {
		this.nome = nome;
		this.funcNonce = funcNonce;
	}

	public String getNome() {
		return nome;
	}

	public String getChaveMestre() {
		return chaveMestre;
	}

	public String getChaveSessao() {
		return chaveSessao;
	}
	
	public Nonce getFuncNonce() {
		return funcNonce;
	}
	
	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	public void setChaveMestre(String chaveMestre) {
		this.chaveMestre = chaveMestre;
	}
	
	public boolean setChaveSessao(byte[] chaveSessao) {
		if(decifrarChaveSessao(chaveSessao) != null) {
			this.chaveSessao = decifrarChaveSessao(chaveSessao);
			return true;
		}
		return false;
	}

	public byte[] comunicarKdc(String nomeDestinatario) {
		ArrayList<byte[]> respostaRequisicao;
		Kdc kdc = Kdc.getInstance();
		
		/*
		 * Solicita a chave de sessão enviando a identificação, o remetente e destinatário cifrados. A resposta será uma
		 * lista contendo as duas chaves de sessão, uma deve ser armazenada e outra encaminhanda para o destinatário.
		 */
		byte[] remetente = Criptografia.cifra(this.nome, this.chaveMestre);
		byte[] destinatario = Criptografia.cifra(nomeDestinatario, this.chaveMestre);
		respostaRequisicao = kdc.solicitarChaveSessao(this.nome, remetente, destinatario);
		
		remetente = respostaRequisicao.get(0);
		this.setChaveSessao(remetente);
		
		destinatario = respostaRequisicao.get(1);	 
		return destinatario;
	}
	
	public byte[] encaminharChaveSessao(Usuario usuario, byte[] chaveSessao) {
		if(usuario.setChaveSessao(chaveSessao) == true) {
			Random random = new Random();
			int nonce = random.nextInt(101);
			
			usuario.setNonce(usuario.getFuncNonce().calcularNonce(nonce));
			
			byte[] nonceCifrado = Criptografia.cifra(Integer.toString(nonce), usuario.getChaveSessao());
			return nonceCifrado;
		}
		return null;
	}
	
	public void encaminharNonce(Usuario usuario, byte[] nonceCifrado) {
		int nonceDecifrado = Integer.parseInt(Criptografia.decifra(nonceCifrado, usuario.getChaveSessao()));
		if(usuario.getNonce() == nonceDecifrado) {
			System.out.println("Nonce válido!");
		} else {
			System.out.println("Nonce inválido!");
		}
	}
	
	public byte[] validaNonce(byte[] nonceCifrado) {
		int nonce = Integer.parseInt(Criptografia.decifra(nonceCifrado, this.chaveSessao));
		nonce = funcNonce.calcularNonce(nonce);
		return Criptografia.cifra(String.valueOf(nonce), this.chaveSessao);
	}
	
	private String decifrarChaveSessao(byte[] chaveSessao) {
		return Criptografia.decifra(chaveSessao, this.chaveMestre);
	}
}
