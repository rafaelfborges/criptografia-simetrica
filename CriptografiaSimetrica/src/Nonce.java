
public class Nonce {
	private int coeficiente;
	
	public Nonce(int coeficiente) {
		this.coeficiente = coeficiente;
	}
	
	public int calcularNonce(int nonce) {
		return nonce *= coeficiente;
	}
}
