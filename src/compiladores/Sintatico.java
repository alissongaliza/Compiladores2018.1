package compiladores; 

import java.util.ArrayList;

/**
 *
 * @author Alisson
 */
public class Sintatico {

    private ArrayList<Token> tokens;

    private static final String[] TIPO = new String[] {
        "[Ii]nteger",
        "[Rr]eal",
        "[Bb]oolean"
        };

    public Sintatico(ArrayList<Token> listaLexico) {
        this.tokens = listaLexico;
    }

    private Token getNextToken() {
        tokens.remove(0);
        return tokens.get(0);
    }
    
    private Token getNextTokenWithoutRemoving() {
        return tokens.get(1);
    }
    
    private void removeCurrentToken(){
        tokens.remove(0);
    }

    private Token getActualToken() {
        return tokens.get(0);
    }

    public boolean program() {

        String token = "";
        // token = tokens.get(0).getNome();

        boolean validaPrograma = false;

        if (getActualToken().getNome().matches("[Pp]rogram")) {
            // tokens.remove(0);
            // token = tokens.get(0).getTipo();
            if (getNextToken().getTipo().equals("Identificador")) {
                // tokens.remove(0);
                // token += tokens.get(0).getNome();
                if (getNextToken().getNome().equals(";"))
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
        // String token = tokens.get(0).getTipo();
        if (getNextToken().getNome().matches("[Vv]ar")){
        // tokens.remove(0);
            isVar();
        }
        return true;
        
    }

    private boolean isVar() {
        // String token = tokens.get(0).getNome();

        if (isIdentifier()) {
            // tokens.remove(0);
            if (getNextToken().getNome().equals(":")) {
//            if (getActualToken().getNome().equals(":")) {
                // tokens.remove(0);
                if (isType()) {
                    // tokens.remove(0);
                    if (getNextToken().getNome().equals(";")) {
                        // tokens.remove(0);
                        if (getNextTokenWithoutRemoving().getTipo().equals("Identificador")) {
                            isVar();
                        }
                    }
                }
            }
            else{
                // sem o ":" para definir o tipo
            }
            // return true;
        }

        return true;
    }

    private boolean isIdentifier() {
        // String token = tokens.get(0).getNome();

        if (getNextToken().getTipo().equals("Identificador")) {
            if (getNextTokenWithoutRemoving().getNome().equals(",")) {
                removeCurrentToken();
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
        return true; //so pra parar de ficar vermelho, nao testei o sentido ainda
    }

    private boolean isType() {
        for (String TIPO1 : TIPO) {
            if (getNextTokenWithoutRemoving().getNome().matches(TIPO1)) {
                removeCurrentToken();
                return true;
            }
        }

        return true;
    }

    // Angel verifica se existe um identificador com o mesmo nome sendo utilizado em
    // outro local
    // n sei se isso vai pro semantico ou fica aqui
    private boolean subprograma() {
        // String token = tokens.get(0).getNome();
        // verifica se o token é Procedure
        if (getNextToken().getNome().equals("Procedure")) {
            tokens.remove(0);
            if (tokens.get(0).getNome().equals(";")) {
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
        if (tokens.get(0).getNome().equals("begin")) {
            tokens.remove(0);
            if (comandoOpcional()) {
                // verificar se é end
                if (tokens.get(0).getNome().equals("end")) {
                    tokens.remove(0);
                    return true;
                }
            }
        }
        return true;
    }

    private boolean comandoOpcional() {
        if (tokens.get(0).getNome().equals("end")) {
            tokens.remove(0);
            return true;
        }
        return listadeComandos();
    }

    private boolean listadeComandos() {
        if (comandos()) {
            if (tokens.get(0).getNome().equals(";")) {
                tokens.remove(0);
                return listadeComandos();
            } else
                return true;
        }
        return false;
    }

    private boolean comandos() {
        // obrigação de ser um identificador
        if (tokens.get(0).getTipo().equals("Identificador")) {
            tokens.remove(0);
            // verifica o ':='
            if (tokens.get(0).getNome().equals(":=")) {

            }
        }
        return true;
    }
    // private boolean chamaProcedimento(){
    // //(
    // if(tokens.get(0).getNome().equals("(")){
    // tokens.remove(0);
    // //verifica se tem expressão
    // if(listadeExpressoes()){
    // if(tokens.get(0).getNome().equals(")")){
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
    // if(tokens.get(0).getTipo().equals("Identificador")){
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
    // if(tokens.get(0).getNome().equals("+") ||
    // tokens.get(0).getNome().equals("-")){
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
