
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jonathan
 */
public class Sintatico {

    private ArrayList<Token> tokens;

    private static final String[] TIPO = new String[] {
        "[Ii]nteger",
        "[Rr]eal",
        "[Bb]oolean"
        };

    public Sintatico(ArrayList<Token> listaLexico) {
        tokens = new ArrayList();
        this.tokens = (ArrayList) listaLexico;
    }

    private Token getNextToken() {
        tokens.remove(0);
        return tokens.get(0);
    }

    private Token getActualToken() {
        return tokens.get(0);
    }

    public boolean program() {

        String token = "";
        // token = tokens.get(0).getToken();

        boolean validaPrograma = false;

        if (getActualToken().getToken().equals("program")) {
            // tokens.remove(0);
            // token = tokens.get(0).getClassificacao();
            if (getNextToken().getClassificacao().equals("Identificador")) {
                // tokens.remove(0);
                // token += tokens.get(0).getToken();
                if (getNextToken().getToken().equals(";"))
                    // tokens.remove(0);
                    if (varDeclaration()) {
                        if (subprograma()) {

                        }
                    }

            }
        }

        return validaPrograma;
    }

    private boolean varDeclaration() {
        // String token = tokens.get(0).getClassificacao();
        if (getNextToken().getClassificacao().equals("var"))
            ;
        // tokens.remove(0);
        isVar();
        return true;

    }

    private boolean isVar() {
        // String token = tokens.get(0).getToken();

        if (isIdentifier()) {
            // tokens.remove(0);
            // if (getNextToken().getToken().equals(":")) {
            if (getActualToken().getToken().equals(":")) {
                // tokens.remove(0);
                if (isType()) {
                    // tokens.remove(0);
                    if (getNextToken().getToken().equals(";")) {
                        // tokens.remove(0);
                        if (getNextToken().getClassificacao().equals("Identificador")) {
                            isVar();
                        }
                    }
                }
            }
            // return true;
        }

        return true;
    }

    private boolean isIdentifier() {
        // String token = tokens.get(0).getToken();

        if (getNextToken().getClassificacao().equals("Identificador")) {
            if (getNextToken().getToken().equals(",")) {
                if (isIdentifier()){
                    return true; 
                }
            }
            else
                return true;
        }
        else{
            return false;
            
        }

    }

    private boolean isType() {

        String[] tipo = { "Integer", "boolean", "real" };
        String token = getNextToken().getToken();
        for (int i = 0; i < tipo.length; i++) {

            if (token.equals(tipo[i])) {
                return true;

            }
            break;
        }

        return true;
    }

    // Angel verifica se existe um identificador com o mesmo nome sendo utilizado em
    // outro local
    // n sei se isso vai pro semantico ou fica aqui
    private boolean subprograma() {
        // String token = tokens.get(0).getToken();
        // verifica se o token é Procedure
        if (getNextToken().getToken().equals("Procedure")) {
            tokens.remove(0);
            if (tokens.get(0).getToken().equals(";")) {
                // verifica se tem declaraçao de variaveis
                // Var
                if (varDeclaration()) {
                    if (compoundComando()) {
                        return true;
                    }
                }
            }

        }
        return true;
    }

    // coloquei o mesmo nome la pra n perder o foco
    private boolean compoundComando() {
        if (tokens.get(0).getToken().equals("begin")) {
            tokens.remove(0);
            if (comandoOpcional()) {
                // verificar se é end
                if (tokens.get(0).getToken().equals("end")) {
                    tokens.remove(0);
                    return true;
                }
            }
        }
        return true;
    }

    private boolean comandoOpcional() {
        if (tokens.get(0).getToken().equals("end")) {
            tokens.remove(0);
            return true;
        }
        return listadeComandos();
    }

    private boolean listadeComandos() {
        if (comandos()) {
            if (tokens.get(0).getToken().equals(";")) {
                tokens.remove(0);
                return listadeComandos();
            } else
                return true;
        }
        return false;
    }

    private boolean comandos() {
        // obrigação de ser um identificador
        if (tokens.get(0).getClassificacao().equals("Identificador")) {
            tokens.remove(0);
            // verifica o ':='
            if (tokens.get(0).getToken().equals(":=")) {

            }
        }
        return true;
    }
    // private boolean chamaProcedimento(){
    // //(
    // if(tokens.get(0).getToken().equals("(")){
    // tokens.remove(0);
    // //verifica se tem expressão
    // if(listadeExpressoes()){
    // if(tokens.get(0).getToken().equals(")")){
    // return true;
    // } //else ERRO
    // }
    // }else
    // return true;
    // return false;
    // }
    // private boolean listadeExpressoes(){
    // if(expressao()){
    // //verifica se tem ,
    // if(tokens.get(0).getClassificacao().equals("Identificador")){
    // return listadeExpressoes();
    // }
    // return true;
    //
    //
    // }
    // return false;
    // }
    // private boolean expressao(){
    // //verifica se é uma simples
    // if(expressaoSimples()){
    // //se tem operador Relacional
    // if(operadorRelacional()){
    // return true;
    // }
    // return true;
    // }
    // return false;
    // }
    // private boolean expressaoSimples(){
    // if(tokens.get(0).getToken().equals("+") ||
    // tokens.get(0).getToken().equals("-")){
    // sinal();//acho q n precisamos ainda
    // termo();
    // }
    // else{
    //
    // }
    // }
    // private boolean operadorRelacional(){
    //
    // }
    // private boolean termo(){
    //
    // }
}
