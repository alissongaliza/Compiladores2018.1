/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rhenan
 */
public class Lexico {
    
    private static String[] palavrasChaves = new String[] {
    "[Pp]rogram",
    "[Vv]ar",
    "[Ii]nteger",
    "[Rr]eal",
    "[Bb]oolean",
    "[Pp]rocedure",
    "[Bb]egin",
    "[Ee]nd",
    "[Ii]f",
    "[Ee]lse",
    "[Tt]hen",
    "[Ww]hile",
    "[Dd]o",
    "[Nn]ot",
    "[Oo]r",
    "[Tr]rue",
    "[Ff]alse"};
//
//static String[] relacionais = new String[] {
//    "=",
//    ">=",
//    "<=",
//    "!=",
//    "<>"};
    
    private int linha_atual;
    private ArrayList<Token> tokens;
    private ArrayList<Character> codigo;
    
    public Lexico (ArrayList<Character> codigo) {
        
        tokens = new ArrayList();
        linha_atual = 1;
        this.codigo = (ArrayList) codigo;
        
    }
    
    public char limpaCaracteres(){
        
        while(true){
            if(Character.toString(codigo.get(0)).matches("[\t\r\f\040]"))
               codigo.remove(0);
            else if(Character.toString(codigo.get(0)).matches("[\n]")){
                linha_atual += 1;
                codigo.remove(0);   
            }   
            else
                return codigo.get(0);
        }    
    }
    
    public void inicio(){
        char charAtual;
        while(!codigo.isEmpty()){
            charAtual = limpaCaracteres();
            
            
            if(Character.toString(charAtual).matches("[a-zA-Z_]"))
                //Chama Identificador
                identificador();
            
            else if(Character.toString(charAtual).matches("[0-9]"))
                numeros();              
//            else if(Character.toString(charAtual).matches("[\{]"))
//            
            else if(Character.toString(charAtual).matches("[/./;/{/}/(/),;:]"))
                delimitador();
            
            else if(Character.toString(charAtual).matches("[=<>]")){
                relacional();
            }
            
            else if(Character.toString(charAtual).matches("[+-/*]")){
                aritmetico();
            }
//            
//            else
                //error
            proximoCaractere();
        }
        printTokens();
    }
    
    public void identificador(){
        String nome_token = "";
        nome_token += codigo.get(0);
        codigo.remove(0);
        char atual;
        while(!codigo.isEmpty()){
            
            atual = codigo.get(0);
            if(Character.toString(atual).matches("[a-zA-Z0-9_]")){
                nome_token += atual;
                codigo.remove(0);
            }
            else 
                break;
        }
        //System.out.println(nome_token);
        if(nome_token.matches("[Oo]r"))//TODO
            criaToken(nome_token, "Aritmetico Aditivo");
        else if(nome_token.matches("[Aa]nd"))
            criaToken(nome_token, "Aritmetico Multiplicativo");
        else
            verificaPR(nome_token);
    }
    
    private void numeros(){
        String nome_token = "";
        nome_token += codigo.get(0);
        codigo.remove(0);
        char atual;
        boolean real = false;
        boolean invalido=false;
        while(!codigo.isEmpty()){
            
            atual = codigo.get(0);
            if(Character.toString(atual).matches("[0-9]")){
                nome_token += atual;
                codigo.remove(0);
            }
            else if(Character.toString(atual).matches("[.]") && !real){
                nome_token += atual;
                real = true;
                codigo.remove(0);
            }
            else if(Character.toString(atual).matches("[.]") && real){
               invalido = true;
               break;
               //tratar o erro
            }
            else if(Character.toString(atual).matches("[a-zA-Z_]") && real){
               invalido = true; 
               break;
               //tratar o erro
            }
            else if(Character.toString(atual).matches("[-+*/<>=\\n;()]")){
               break;
               //tratar o erro
            }
        }
        if (real){
            criaToken(nome_token, "Numero Real");
        }
        else {
            criaToken(nome_token, "Numero Inteiro");
        }
    }
    
    private void verificaPR(String token){
        boolean isPR = false;
        for (String i : palavrasChaves){
            if(token.matches(i)){
                isPR = true;
                break;
            }
        }
        if(isPR)
            criaToken(token,"Palavra Reservada");
        else
            criaToken(token, "Identificador");
    }
    
    private void criaToken(String nome, String tipo){
        tokens.add(new Token(nome,tipo,linha_atual));
    }
    
    public void relacional(){
        String nome_token = "";
        nome_token += codigo.get(0);
        codigo.remove(0);
        char atual;
        while(!codigo.isEmpty()){
            atual = codigo.get(0);
            codigo.remove(0);
            //redundancia proposital
            
            if((nome_token.equals(">") && atual == '=') || (nome_token.equals("<") && atual == '=')){   //maior/menor igual que
              nome_token+=atual;
              criaToken(nome_token,"Relacional");
              break;
            }
            else if (nome_token.equals("<") && atual == '>'){   //diferente
                nome_token+=atual;
                criaToken(nome_token,"Relacional");
              break;
            }
            else if(nome_token.equals("=") && (atual == '>' || atual == '<')){  //nao deve acontecer
                //erro
                break;
            }
            else{           //apenas maior/menor
                criaToken(nome_token,"Relacional");
                break;
            }
        }
    }
    
    private void aritmetico(){
        String nome_token = "";
        nome_token += codigo.get(0);
        switch(nome_token){
            case "+":
            criaToken(nome_token,"Aritmetico Aditivo");
                break;
            case "-":
            criaToken(nome_token,"Aritmetico Subtrativo");
                break;
            case "*":
            criaToken(nome_token,"Aritmetico Multiplicativo");
                break;
            case "/":
            criaToken(nome_token,"Aritmetico Divisivo");
                break;
        }
        
        codigo.remove(0);
        
    }
    
    public void delimitador(){
        String nome_token = "";
        nome_token += codigo.get(0);
        codigo.remove(0);
        char atual;
        
        while(!codigo.isEmpty()){
            atual = codigo.get(0);
            if(nome_token.equals(":")){
                if(atual == '='){
                    nome_token+=atual;
                    criaToken(nome_token,"Atribuição");
                    codigo.remove(0);
                    return;
                }
                else{
                    break;
                }
            }
            else
                break;
        }
        criaToken(nome_token,"Delimitador");
    }
    
    private void proximoCaractere(){
        if(codigo.isEmpty())
            return;
        if(codigo.get(0).equals(' ') )
            codigo.remove(0);
    }
    
    private void erroSintax(){
        char chr;
        chr = codigo.get(0);
        
        while(chr != '\n'){
            codigo.remove(0); 
            chr = codigo.get(0);
        }
        System.err.println("Erro de Sintaxe na Linha: "+linha_atual);
        linha_atual++;
    
    }
    
    private void printTokens(){
        
        Object[][] table = new String[tokens.size()][];
        
        for(int i = 0; i < tokens.size(); i++){
            table[i] = new String[] { tokens.get(i).getNome(), tokens.get(i).getTipo(),
                                      String.valueOf(tokens.get(i).getNumero())};
        }

        System.out.format("\n\n\n%-15s%-15s%20s\n", "Token", "Tipo", "Linha \n");
        
        for (Object[] row : table) {
            System.out.format("%-15s%-15s%15s\n",row[0], row[1], row[2]);
        }
    }
}
