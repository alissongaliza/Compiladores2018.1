package compiladores;

import java.util.Stack;

public class Semantico {
    
    public Stack<Token> pilhaRecorrencia;
    private int varEscopo;
    
    public Semantico(){
        pilhaRecorrencia = new Stack<>();
        varEscopo = 0;
    }
    
    public void incEscopo(){
        varEscopo++;
    }
    
    public void decEscopo(){
        varEscopo--;
    }
    
    public boolean correctEscopo(){
        boolean retorno = (varEscopo == 0);
        return retorno;
    }
    
    public int getEscopo(){
        return varEscopo;
    }
    
    public boolean recorrencia(Token t){
        for (int i = pilhaRecorrencia.size()-1; i >= 0 ;i--){
            if(t.getNome().equals(pilhaRecorrencia.get(i).getNome()))
                return true;
        }
        return false;
    }
    
    public void desempilhaEscopo(){
        
        while(pilhaRecorrencia.pop().getNome() != "#");
        pilhaRecorrencia.pop();
    }
}
