package compiladores;

import java.util.ArrayList;

public class Lexico {
    
    private static String[] palavrasChaves = new String[] {
    "program",
    "var",
    "integer",
    "real",
    "boolean",
    "procedure",
    "begin",
    "end",
    "if",
    "else",
    "then",
    "while",
    "do",
    "not",
    "or"};
    
    private int linha_atual;
    private ArrayList<Token> tokens;
    private ArrayList<Character> stringao;
    
    public Lexico (ArrayList<Character> stringao) {
        
        tokens = new ArrayList();
        linha_atual = 1;
        this.stringao = (ArrayList) stringao;
        
    }
    
    public char limpaCaracteres(){
        
        while(true){
            if(Character.toString(stringao.get(0)).matches("[\t\r\f\040]"))
               stringao.remove(0);
            else if(Character.toString(stringao.get(0)).matches("[\n]")){
                linha_atual += 1;
                stringao.remove(0);   
            }   
            else
                return stringao.get(0);
        }    
    }
    public void inicio(){
        char charAtual;
        while(!stringao.isEmpty()){
            charAtual = limpaCaracteres();
            
            
            if(Character.toString(charAtual).matches("[a-zA-Z_]"))
                //Chama Identificador
                identificador();
            else if(Character.toString(charAtual).matches("[0-9]"))
                numeros();              
//          
            else if(Character.toString(charAtual).matches("[\\/\\+\\-\\*]"))
                operadores();
        
            else if(Character.toString(charAtual).matches("[/./;/{/},;:]"))
                delimitador();                
            else if(Character.toString(charAtual).matches("[=<>]"))
                relacional();
//            else if(Character.toString(charAtual).matches("[+ -/\*]"))
//            
//            else
                //error
        }
        System.out.println(tokens);
    }
    public void identificador(){
        String nome_token = "";
        nome_token += stringao.get(0);
        stringao.remove(0);
        char atual;
        while(!stringao.isEmpty()){
            
            atual = stringao.get(0);
            if(Character.toString(atual).matches("[a-zA-Z0-9_]")){
                nome_token += atual;
                stringao.remove(0);
            }
            else 
                break;
        }
        System.out.println(nome_token);
        if(nome_token.equals("or"))
            criaToken(nome_token, "Operador Aditivo");
        else if(nome_token.equals("and"))
            criaToken(nome_token, "Operador Multiplitivo");
        else
            verificaPR(nome_token);
    }
    private void numeros(){
        String nome_token = "";
        nome_token += stringao.get(0);
        stringao.remove(0);
        char atual;
        boolean real = false;
        boolean invalido=false;
        while(!stringao.isEmpty()){
            
            atual = stringao.get(0);
            if(Character.toString(atual).matches("[0-9]")){
                nome_token += atual;
                stringao.remove(0);
            }
            else if(Character.toString(atual).matches("[.]") && !real){
                nome_token += atual;
                real = true;
                stringao.remove(0);
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
            else if(Character.toString(atual).matches("[ \\;\\:\\-\\+\\*\\/<>=\\n]")){
               break;
               //tratar o erro
            }
        }
        System.out.println(nome_token);
        if (real){
            criaToken(nome_token, "Numero Real");
        }
        else {
            criaToken(nome_token, "Numero Inteiro");
        }
    }
    
    private void verificaPR(String token){
        boolean isPR = false;
        for (String i:palavrasChaves){
            if(i.equals(token))
                isPR = true;
        }
        if(isPR)
            criaToken(token,"Palavra Reservada");
        else
            criaToken(token, "Identificador");
    }
    
    private void criaToken(String nome, String tipo){
        tokens.add(new Token(tipo,nome,linha_atual));
    }
    private void erroSintax(){
        char chr;
        chr = stringao.get(0);
        
        while(chr != '\n'){
            stringao.remove(0); 
            chr = stringao.get(0);
        }
        System.err.println("Erro de Sintaxe na Linha: "+linha_atual);
        linha_atual++;
    }
    public void relacional(){
        String nome_token = "";
        nome_token += stringao.get(0);
        //stringao.remove(0);
        char atual;
        while(!stringao.isEmpty()){
            stringao.remove(0);
            atual = stringao.get(0);
            if((nome_token == ">" && atual == '=') || (nome_token == "<" && (atual == '='))){
                nome_token+=atual;
                criaToken(nome_token,"Relacional");
                return;
            }
            else if ((nome_token == "<" && atual == '>')){
                nome_token+=atual;
                criaToken(nome_token,"Relacional");
                return;
            }
            else
                break;
        }
        criaToken(nome_token, "Relacional");
    }
    public void delimitador(){
        String nome_token = "";
        nome_token += stringao.get(0);
        stringao.remove(0);
        char atual;
        while(!stringao.isEmpty()){
            atual = stringao.get(0);
            //System.out.println("aqui");
            if(nome_token.equals(":")){
                if(atual == '='){
                    nome_token+=atual;
                    System.out.println(nome_token);
                    criaToken(nome_token,"Atribuição");
                    return;
                }
                break;
            }
            else
                break;
        }
        System.out.println(nome_token);
        criaToken(nome_token,"Delimitador");
    }
    public void operadores(){
        String nome_token = "";
        nome_token += stringao.get(0);
        stringao.remove(0);
        if(nome_token.equals("+")||nome_token.equals("-")){
            System.out.println(nome_token);
            criaToken(nome_token,"Operador aditivo");
        }    
        else if (nome_token.equals("/")||nome_token.equals("*")){
            System.out.println(nome_token);
            criaToken(nome_token,"Operador Multiplicativo");
            System.out.println("succ");
        }    
    }
}
