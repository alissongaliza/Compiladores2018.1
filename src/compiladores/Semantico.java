package compiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.JOptionPane;

public class Semantico {
    
    public Stack<Token> pilhaEscopoRecorrencia;
    public Stack<String> pilhaCheckagemDeTipos;
    private int varEscopo;
    public ArrayList<Token> variaveisSemTipo;
    public HashMap<String,String> variaveisComTipo;
    
    
    public Semantico(){
        pilhaEscopoRecorrencia = new Stack<>();
        pilhaCheckagemDeTipos = new Stack<>();
        variaveisSemTipo = new ArrayList<>();
        variaveisComTipo = new HashMap<>();
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
        if(varEscopo == 0){
            int i = pilhaEscopoRecorrencia.size() -1;
            while(!pilhaEscopoRecorrencia.get(i).getNome().equals("#")){
                if(t.getNome().equals(pilhaEscopoRecorrencia.get(i).getNome()))
                    return false;
                i--;
            }
        }
        else{
            for (int i = pilhaEscopoRecorrencia.size()-1; i >= 0 ;i--){
                if(t.getNome().equals(pilhaEscopoRecorrencia.get(i).getNome()))
                    return true;
            }
        }
        
        return false;
    }
    
    public void desempilhaEscopo(){
        
        while(!pilhaEscopoRecorrencia.peek().getNome().equals("#")){
            pilhaEscopoRecorrencia.pop();
        }
        pilhaEscopoRecorrencia.pop();
        
        // if(pilha.peek.getTipo().equals("Marcador normal"))
        //     pilhaEscopoRecorrencia.pop()
        // else{

        // }

    }
    
    public boolean analisaTiposExpressao(int linha){
        if(!(pilhaCheckagemDeTipos.size() >= 2)){
            JOptionPane.showMessageDialog(null, "Numero de argumentos insuficientes na linha " + linha);
            return false;
        }
            
        if(pilhaCheckagemDeTipos.peek().equals("integer")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("integer")){
            atualizaPcT("integer");
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("real")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("real")){
            atualizaPcT("real");
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("integer")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("real")){
            atualizaPcT("real");
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("real")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("integer")){
            atualizaPcT("real");
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("boolean")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("boolean")){
            atualizaPcT("boolean");
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "Incompatibilidade de tipos na linha " + linha);
            System.exit(0);
            return false;
        }
        
    }
    
    public boolean analisaTiposAtribuicao(int linha){
        if(!(pilhaCheckagemDeTipos.size() >= 2)){
            JOptionPane.showMessageDialog(null, "Numero de argumentos insuficientes na linha " + linha);
            return false;
        }
            
        if(pilhaCheckagemDeTipos.peek().equals("integer")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("integer")){
            pilhaCheckagemDeTipos.pop();
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("real")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("real")){
            pilhaCheckagemDeTipos.pop();
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("boolean")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("boolean")){
            pilhaCheckagemDeTipos.pop();
            return true;
        }
        else if(pilhaCheckagemDeTipos.peek().equals("integer")
                && pilhaCheckagemDeTipos.get(pilhaCheckagemDeTipos.size() - 2).equals("real")){
            pilhaCheckagemDeTipos.pop();
            return true;
        }
        
        else{
            JOptionPane.showMessageDialog(null, "Incompatibilidade de tipo atribuido na linha " + linha);
            System.exit(0);
            return false;
        }
    } 
    
    private void atualizaPcT(String tipoRetorno){
        pilhaCheckagemDeTipos.pop();
        pilhaCheckagemDeTipos.pop();
        pilhaCheckagemDeTipos.push(tipoRetorno);
    }
}
