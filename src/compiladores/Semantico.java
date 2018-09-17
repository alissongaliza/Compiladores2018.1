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
        return varEscopo == 0;
    }
    
    public int getEscopo(){
        return varEscopo;
    }
    
    public boolean analisaExistencia(Token t){
        for (int i = pilhaRecorrencia.size()-1; i >= 0 ;i--){
            if(t.getNome().equals(pilhaRecorrencia.get(i).getNome()))
                return true;
        }
        return false;
    }
    
    public void desempilhaEscopo(){
        
        while(pilhaRecorrencia.peek().getNome.equals('#')){
            pilhaRecorrencia.pop();
        }
        pilhaRecorrencia.pop();
        
        // if(pilha.peek.getTipo().equals("Marcador normal"))
        //     pilhaRecorrencia.pop()
        // else{

        // }

    }
}
