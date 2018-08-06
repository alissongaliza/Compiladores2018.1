package compiladores;

public class Token {
    private int numeroLinha;
    private String tipo;
    private String token;
    
    
    public Token(String tp,String tk,int n){
        token = tk;
        tipo = tp;
        numeroLinha = n;
        
    }

    @Override
    public String toString() {
        return "Token{" +"Token=" + token + ", numeroLinha=" + numeroLinha + ", tipo=" + tipo + '}'+"\n";
    }
    
    public void setLinha(String Linha){

	this.token = Linha;
    }
	
    public String getLinha(){

	return token;
    }
    public void setNumero(int numeroLinha){

	this.numeroLinha = numeroLinha;
    }
	
    public int getNumero(){

	return numeroLinha;
    }
    public void setTipo(String tipo){

	this.tipo = tipo;
    }
	
    public String getTipo(){

	return tipo;
    }
    public void print() {
        System.out.println(""+token+""+numeroLinha+""+tipo);
    }
}
    

