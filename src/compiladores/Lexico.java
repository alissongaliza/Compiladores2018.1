/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

import java.util.ArrayList;

/**
 *
 * @author rhenan
 */
public class Lexico {
    
    private static final String[] PALAVRAS_CHAVES = new String[] {
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
    
    private int linhaAtual;
    private ArrayList<Token> tokens;
    private ArrayList<Character> codigo;
    private ArrayList<PalavraIgnorada> palavrasIgnoradas;
    
    public Lexico (ArrayList<Character> codigo) {
        
        tokens = new ArrayList();
        linhaAtual = 1;
        this.codigo = (ArrayList) codigo;
        palavrasIgnoradas = new ArrayList();
        
    }
    
    public char limpaCaracteres(){
        
        while(true){
            if(Character.toString(codigo.get(0)).matches("[\t\r\f\040]"))
               codigo.remove(0);
            else if(Character.toString(codigo.get(0)).matches("[\n]")){
                linhaAtual += 1;
                codigo.remove(0);   
            }   
            else
                return codigo.get(0);
        }    
    }
    
    public void percorreCodigo(){
        char charAtual;
        while(!codigo.isEmpty()){
            charAtual = limpaCaracteres();
            
            if(Character.toString(charAtual).matches("[a-zA-Z_]"))
                variavel();
            
            else if(Character.toString(charAtual).matches("[0-9]"))
                operando();              
            
            else if(Character.toString(charAtual).matches("[{]"))
                comentario();
            
            else if(Character.toString(charAtual).matches("[.(),;:]"))
                delimitador();
            
            else if(Character.toString(charAtual).matches("[=<>]"))
                relacional();
            
            else if(Character.toString(charAtual).matches("[+-/*]"))
                aritmetico();
            
            else{
                erroNaSintaxe(charAtual);
            }
            
            proximoCaractere();
        }
        
        printTokens();
        //evitar print fora de ordem
        System.out.flush();
        
        printIgnoradas();
    }
    
    private void variavel(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        codigo.remove(0);
        char atual;
        while(!codigo.isEmpty()){
            
            atual = codigo.get(0);
            if(Character.toString(atual).matches("[a-zA-Z0-9_]")){
                palavraAcumulada += atual;
                codigo.remove(0);
            }
            else 
                break;
        }
        //System.out.println(palavraAcumulada);
        if(palavraAcumulada.matches("[Oo]r"))//TODO
            criaToken(palavraAcumulada, "Aritmetico Aditivo");
        else if(palavraAcumulada.matches("[Aa]nd"))
            criaToken(palavraAcumulada, "Aritmetico Multiplicativo");
        else
            isPalavraReservada(palavraAcumulada);
    }
    
    private void operandoOld(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        codigo.remove(0);
        char atual;
        boolean real = false;
        boolean tresD = false;
        boolean erro = false;
        while(!codigo.isEmpty()){
            
            atual = codigo.get(0);
            if(Character.toString(atual).matches("[0-9]")){
                palavraAcumulada += atual;
                codigo.remove(0);
            }
            else if(Character.toString(atual).matches("[.]") && !real){
                palavraAcumulada += atual;
                if(palavraAcumulada.contains("x")){
                    real = false;
                    tresD = true;
                }
                    
                else{
                    real = true;
                    
                }
                codigo.remove(0);
            }
            
//            else if(Character.toString(atual).matches("[xyz]") && real){
//                if(atual == 'x' && (!palavraAcumulada.contains("y") && !palavraAcumulada.contains("z"))){
//                    palavraAcumulada+=atual;
//                }
//            }
            else if(Character.toString(atual).matches("[xyz]") && (real || tresD)){
                
                if(atual == 'x' && palavraAcumulada.matches("[0-9]+.[0-9]+")){
                    palavraAcumulada+=atual;
                    codigo.remove(0);
                    real = false;
                    tresD = true;
                    erro=true;
                }
                else if(atual == 'y' && palavraAcumulada.matches("[0-9]+.[0-9]+x[0-9]+.[0-9]+")){
                    palavraAcumulada+=atual;
                    codigo.remove(0);
                    erro=true;
                }
                else if(atual == 'z' && palavraAcumulada.matches("[0-9]+.[0-9]+x[0-9]+.[0-9]+y[0-9]+.[0-9]+")){
                    palavraAcumulada+=atual;
                    codigo.remove(0);
                    erro = false;
                    real = false;
                    break;
                }
            }
            
            else if(Character.toString(atual).matches("[.]") && real && !palavraAcumulada.contains("x")){
                //o real acabou e nao e um numero 3d
                break;
            }
            else if(Character.toString(atual).matches("[a-zA-Z_]") && real){
               //tratar o erro
            }
            else if(Character.toString(atual).matches("[-+*/<>=\\n;()]")){
                break;
               //tratar o erro
            }
        }
        if(erro){
            while(!(palavraAcumulada.matches("[0-9]+") || palavraAcumulada.matches("[0-9]+.[0-9]+"))){
                palavraAcumulada = palavraAcumulada.substring(0, palavraAcumulada.length() - 1);
            }
            if(palavraAcumulada.contains(".")){
                real=true;
            }
        }
            
        
        if (real){
            criaToken(palavraAcumulada, "Numero Real");
        }
        else if (tresD){
            criaToken(palavraAcumulada, "Numero 3D");
        }
        else {
            criaToken(palavraAcumulada, "Numero Inteiro");
        }
    }
    
    private void operando(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        codigo.remove(0);
        char atual;
        boolean real = false;
        boolean tresD = false;
        boolean inteiro = true;
        boolean erro = false;
        while(!codigo.isEmpty()){
            
            atual = codigo.get(0);
            if(Character.toString(atual).matches("[0-9]")){
                palavraAcumulada += atual;
                codigo.remove(0);
            }
            else if(Character.toString(atual).matches("[.]")){
                
                if(isDecimal(palavraAcumulada)){
                    palavraAcumulada += atual;
                    inteiro = false;
                    codigo.remove(0);
                    
                }
                else{
                    erro = true;
                    break;
                }
                
            }
            else if(Character.toString(atual).matches("[xyz]")){
                
                if(atual == 'x' && palavraAcumulada.matches("[0-9]+.[0-9]+")){
                    palavraAcumulada+=atual;
                    codigo.remove(0);
                    real = false;
                    erro=true;
                }
                else if(atual == 'y' && palavraAcumulada.matches("[0-9]+.[0-9]+x[0-9]+.[0-9]+")){
                    palavraAcumulada+=atual;
                    codigo.remove(0);
                    erro=true;
                }
                else if(atual == 'z' && palavraAcumulada.matches("[0-9]+.[0-9]+x[0-9]+.[0-9]+y[0-9]+.[0-9]+")){
                    palavraAcumulada+=atual;
                    codigo.remove(0);
                    erro = false;
                    tresD = true;
                    break;
                }
            }
            else{
                break;
            }
        }
        if(erro){
            while(!(   palavraAcumulada.matches("[0-9]+") 
                    || palavraAcumulada.matches("[0-9]+.[0-9]+") 
                    || palavraAcumulada.matches("[0-9]+.[0-9]+x[0-9]+.[0-9]+y[0-9]+.[0-9]+z"))){
                palavraAcumulada = palavraAcumulada.substring(0, palavraAcumulada.length() - 1);
            }
            if(palavraAcumulada.matches("[0-9]+.[0-9]+x[0-9]+.[0-9]+y[0-9]+.[0-9]+z")){
                tresD = true;
                real= false;
                inteiro = false;
            }
            else if(palavraAcumulada.matches("[0-9]+.[0-9]+")){
                real = true;
                tresD= false;
                inteiro = false;
            }
            else if(palavraAcumulada.matches("[0-9]+")){
                real = false;
                tresD= false;
                inteiro = true;
                
            }
        }
            
        
        if (real){
            criaToken(palavraAcumulada, "Numero Real");
        }
        else if (tresD){
            criaToken(palavraAcumulada, "Numero 3D");
        }
        else if (inteiro){
            criaToken(palavraAcumulada, "Numero Inteiro");
        }
    }
    
    private boolean isDecimal(String acumulada){
        if(String.valueOf(codigo.get(1)).matches("[0-9]")){
            if(acumulada.contains("."))
                return acumulada.matches(".*[a-zA-Z].*");
            else{
                return true;
            }
        }
        return false;
    }
    
    private void isPalavraReservada(String token){
        boolean isPR = false;
        for (String i : PALAVRAS_CHAVES){
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
    
    private void relacional(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        codigo.remove(0);
        char atual;
        while(!codigo.isEmpty()){
            atual = codigo.get(0);
            codigo.remove(0);
            //redundancia proposital
            
            if((palavraAcumulada.equals(">") && atual == '=') || (palavraAcumulada.equals("<") && atual == '=')){   //maior/menor igual que
              palavraAcumulada+=atual;
              criaToken(palavraAcumulada,"Relacional");
              break;
            }
            else if (palavraAcumulada.equals("<") && atual == '>'){   //diferente
                palavraAcumulada+=atual;
                criaToken(palavraAcumulada,"Relacional");
              break;
            }
            else if(palavraAcumulada.equals("=") && (atual == '>' || atual == '<')){  //nao deve acontecer
                //erro
                break;
            }
            else{           //apenas maior/menor
                criaToken(palavraAcumulada,"Relacional");
                break;
            }
        }
    }
    
    private void aritmetico(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        switch(palavraAcumulada){
            case "+":
                criaToken(palavraAcumulada,"Operador Aditivo");
                codigo.remove(0);
                break;
            case "-":
                criaToken(palavraAcumulada,"Operador Aditivo");
                codigo.remove(0);
                break;
            case "*":
                criaToken(palavraAcumulada,"Operador Multiplicativo");
                codigo.remove(0);
                break;
            case "/":
                codigo.remove(0);
                char atual = codigo.get(0);
                if(atual == '/'){
                    palavraAcumulada += atual;
                    codigo.remove(0);
                    atual = codigo.get(0);
                    while(atual != '\n' && !codigo.isEmpty()){
                        
                        palavraAcumulada += atual;
                        codigo.remove(0);
                        if(!codigo.isEmpty())
                            atual = codigo.get(0);
                    }
                    criaPalavrasIgnoradas(palavraAcumulada, "Comentário na linha " + linhaAtual);
                }
                else{
                    criaToken(palavraAcumulada,"Operador Multiplicativo");
                }
                
                break;
        }
        
    }
    
    private void delimitador(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        codigo.remove(0);
        char atual;
        
        while(!codigo.isEmpty()){
            atual = codigo.get(0);
            if(palavraAcumulada.equals(":")){
                if(atual == '='){
                    palavraAcumulada+=atual;
                    criaToken(palavraAcumulada, "Atribuição");
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
        criaToken(palavraAcumulada, "Delimitador");
    }
    
    private void comentario(){
        String palavraAcumulada = "";
        palavraAcumulada += codigo.get(0);
        codigo.remove(0);
        char atual;
        int linhaFechamento = linhaAtual;
        int contador = 1;
        
        while(!codigo.isEmpty()){
            
            atual = codigo.get(0);
            palavraAcumulada += atual;
            switch (atual) {
                case '{':
                    contador++;
                    codigo.remove(0);
                    break;
                case '}':
                    contador--;
                    if(contador == 0){
                        criaPalavrasIgnoradas(palavraAcumulada, "Comentário da linha " + linhaFechamento + " ate linha " + linhaAtual);
                        codigo.remove(0);
                        return;
                    }
                    codigo.remove(0);
                    break;
                case '\n':
                    linhaAtual++;
                    codigo.remove(0);
                    break;
                default:
                    codigo.remove(0);
                    break;
            }
            
        }
        
        //se chegar aqui significa que nao encontrou um fechar comentario "}"
        System.out.println("\n---------------------------------------------------------------------------------------------------");
        System.out.println("\n\nComentário da linha " + linhaFechamento + " nao fechado ate linha atual(" + linhaAtual + ")\n");
        System.exit(0);
        
    }
    
    private void proximoCaractere(){
        if(codigo.isEmpty())
            return;
        if(codigo.get(0).equals(' ') )
            codigo.remove(0);
    }
    
    private void erroNaSintaxe(char charAtual){
        criaPalavrasIgnoradas(String.valueOf(charAtual), "Caractere fora da gramatica na linha " + linhaAtual + ". Ignorando e continuando o codigo...");
        codigo.remove(0);
    }
    
    private void criaPalavrasIgnoradas(String palavraAcumulada, String mensagem){
        palavrasIgnoradas.add(new PalavraIgnorada(palavraAcumulada, mensagem));
    }
    
    private void criaToken(String nome, String tipo){
        tokens.add(new Token(nome,tipo,linhaAtual));
    }
    
    private void printTokens(){
        
        System.out.println("\n---------------------------------------------------------------------------------------------------");
        System.out.println("\n\n\nTabela de Tokens:");
        Object[][] table = new String[tokens.size()][];
        
        for(int i = 0; i < tokens.size(); i++){
            table[i] = new String[] { tokens.get(i).getNome(), tokens.get(i).getTipo(),
                                      String.valueOf(tokens.get(i).getNumero())};
        }

        System.out.format("\n%-15s%-15s%20s\n", "Token", "Tipo", "Linha \n");
        
        for (Object[] row : table) {
            System.out.format("%-15s%-15s%15s\n",row[0], row[1], row[2]);
        }
    }
    
    private void printIgnoradas(){
        
        System.out.println("\n---------------------------------------------------------------------------------------------------");
        System.out.println("\n\nPalavras ignoradas: \n");
        
        int i = 1;
        for (PalavraIgnorada palavraIgnorada : palavrasIgnoradas) {
            
            System.out.println(i + " - " + palavraIgnorada + "\n");
            i++;
        }
    }
}
