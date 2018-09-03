package compiladores; 

import java.util.ArrayList;

/**
 *
 * @author Alisson
 */
public class Sintatico {

    private ArrayList<Token> tokens;

    private static final String[] TIPO = new String[] {
        "integer",
        "real",
        "boolean"
        };
    private static final String[] OPERADOR_ADITIVO = new String[]{
        "+",
        "-",
        "or"
    };
    private static final String[] OPERADOR_MULTIPLICATIVO = new String[]{
        "*",
        "/",
        "and"
    };
    private static final String[] OPERADOR_RELACIONAL = new String[]{
        "=",
        "<",
        ">",
        "<=",
        "<=",
        "<>",
    };
    private static final String[] SINAL = new String[]{
        "+",
        "-"
    };
    
    private static final String IDENTIFICADOR = "Identificador";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String INTEIRO = "inteiro";
    private static final String REAL = "real";
    private static final String IF = "if";
    private static final String THEN = "then";
    private static final String ELSE = "else";
    private static final String WHILE = "while";
    private static final String DO = "do";
    private static final String BEGIN = "begin";
    private static final String END = "end";

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

    private Token getCurrentToken() {
        return tokens.get(0);
    }
    
    private int getCurrentTokenPosition(){
        return tokens.get(0).getNumero();
    }

    public boolean program() {

        if (getCurrentToken().getNome().matches("[Pp]rogram")) {
            // tokens.remove(0);
            // token = tokens.get(0).getTipo();
            if (getNextToken().getTipo().equals(IDENTIFICADOR)) {
                // tokens.remove(0);
                // token += tokens.get(0).getNome();
                if (getNextToken().getNome().equals(";")){
                    // tokens.remove(0);
                    if (declaracoesVariaveis()) {
                        //pode ser epsilon
                        if (declaracaoDeSubprogramas()) {
                            
                            if(comandoComposto()){

                                if(getNextTokenWithoutRemoving().getNome().equals(".")){
                                    removeCurrentToken();
                                    //se chegou aqui deve ter a estrutura do estilo:    program id; declaracoesVariaveis declaracoesSubprogramas comandoComposto .
                                    return true;
                                }
                                else{
                                    System.err.println("Estrutura basica do programa invalida (Falta '.') na linha " + getCurrentTokenPosition());
                                    return false;
                                }
                            }
                            else{
                                System.err.println("Comando composto Invalido na linha " + getCurrentTokenPosition());
                                return false;
                            }

                        }
                        else{
                            System.err.println("Declaracao de subprogramas invalida na linha " + getCurrentTokenPosition());
                            return false;
                        }
                    }
                    else{
                        System.err.println("Declaracao de variaveis invalida na linha " + getCurrentTokenPosition());
                        return false;
                    }

                
                }
                else{
                    System.err.println("Estrutura basica do programa invalida (Falta ';') na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Estrutura basica do programa invalida (Falta identificador) na linha" + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            System.err.println("Estrutura basica do programa invalida (Falta 'program') na linha" + getCurrentTokenPosition());
            return false;
        }
    }

    private boolean declaracoesVariaveis() {
        
        if (getNextTokenWithoutRemoving().getNome().matches("[Vv]ar")){
            removeCurrentToken();
            if(listaDeclaracoesVariaveis()){
                return true;
            }
            else{
                System.err.println("Declaracoes variaveis invalida (Falta lista de declaracoes de variaveis) na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        //pode ser epsilon
        return true;
        
    }

    private boolean listaDeclaracoesVariaveis() {

        if (listaIdentificadores()) {
            if (getNextTokenWithoutRemoving().getNome().equals(":")) {
                removeCurrentToken();
                if (eTipo()) {
                    removeCurrentToken();
                    if (getNextTokenWithoutRemoving().getNome().equals(";")) {
                        removeCurrentToken();
                        return listaDeclaracoesVariaveisHash();
                    }
                    else{
                        System.err.println("Lista de declaracoes de variaveis invalida (Falta ';') na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
                else{
                    System.err.println("Lista de declaracoes de variaveis invalida (Falta tipo) na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Lista de declaracoes de variaveis invalida (Falta ':') na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            System.err.println("Lista de declaracoes de variaveis invalida (Falta lista de identificadores) na linha " + getCurrentTokenPosition());
            return false;
        }

    }
    
    private boolean listaDeclaracoesVariaveisHash(){
        
        if (listaIdentificadores()) {
            if (getNextTokenWithoutRemoving().getNome().equals(":")) {
                removeCurrentToken();
                if (eTipo()) {
                    removeCurrentToken();
                    if (getNextTokenWithoutRemoving().getNome().equals(";")) {
                        removeCurrentToken();
                        return listaDeclaracoesVariaveisHash();
                    }
                    else{
                        System.err.println("Lista de declaracoes de variaveis hash invalida (Falta ';') na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
                else{
                    System.err.println("Lista de declaracoes de variaveis hash invalida (Falta tipo) na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Lista de declaracoes de variaveis hash invalida (Falta ':') na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            //pode ser epsilon
            return true;
        }
    }

    private boolean listaIdentificadores() {

        if(eIdentificador()) {
            removeCurrentToken();
            if(listaIdentificadoresHash()){
                //se chegou aqui deve ter a estrutura do estilo:          id listaDeIdentificadoresHash
                return true;
            }
            else{
                System.err.println("Lista de identificadores invalida (Falta lista de identificadores hash) na linha " + getCurrentTokenPosition());
                return false;
            }
            
        }
        else{
            System.err.println("Lista de identificadores invalida (Falta identificador) na linha " + getCurrentTokenPosition());
            return false;
        }
    }
    
    private boolean listaIdentificadoresHash(){
        
        if(getNextTokenWithoutRemoving().getNome().equals(",")){
            if(eIdentificador()) {
                removeCurrentToken();
                if(listaIdentificadoresHash()){
                    //se chegou aqui deve ter a estrutura do estilo:          id listaDeIdentificadoresHash
                    return true;
                }
                else{
                    System.err.println("Lista de identificadores invalida (Falta lista de identificadores hash) na linha " + getCurrentTokenPosition());
                    return false;
                }

            }
            else{
                System.err.println("Lista de identificadores invalida (Falta identificador) na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            //permite epsilon
            return true;
        }
    }

    private boolean declaracaoDeSubprogramas(){
        if(declaracaoDeSubprogramasHash()){
            return true;
        }
        else{
            System.err.println("Declaracao de subprogramas invalida (Falta declaracao de subprogramas) na linha " + getCurrentTokenPosition());
            return false;
        }
    }
    
    private boolean declaracaoDeSubprogramasHash(){
        if(declaracaoDeSubprograma()){
            if(getNextTokenWithoutRemoving().getNome().equals(";")){
                removeCurrentToken();
                return declaracaoDeSubprogramasHash();
            }
        }
        //permite epsilon
        return true;
    }
    
    private boolean declaracaoDeSubprograma() {
        if (getNextTokenWithoutRemoving().getNome().matches("[Pp]rocedure")) {
            removeCurrentToken();
            if (eIdentificador()) {
                removeCurrentToken();
                if(argumentos()){
                    if(getNextTokenWithoutRemoving().getNome().equals(";")){
                        removeCurrentToken();
                        if(declaracoesVariaveis())
                            if(declaracaoDeSubprogramas());
                                if(comandoComposto())
                                    return true;
                    }
                    else{
                        System.err.println("Declaracao de subprograma invalida (Falta ';') na linha " + getCurrentTokenPosition());
                        return false;
                    }
                    
                }
                else{
                    System.err.println("Declaracao de subprograma invalida (Falta argumentos) na linha "+ getCurrentTokenPosition());
                    return false;
                }          
            }
        }
        
        return false;
    }
    
    private boolean argumentos(){
        if(getNextTokenWithoutRemoving().getNome().equals("(")){
            removeCurrentToken();
            if(listaDeParametros()){
                if(getNextTokenWithoutRemoving().getNome().equals(")")){
                    removeCurrentToken();
                    //se chegou aqui deve ter a estrutura do estilo:          (~lista de argumentos~)
                    return true;
                }
                else{
                    //se chegou aqui deve ter a estrutura do estilo:    (~lista de agumentos~ ~nao fechou o parenteses~)
                    System.err.println("Argumento invalido (Falta fechar parenteses) na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                //se chegou aqui deve ter a estrutura do estilo:    (~sem argumentos OU argumentos errados~
                System.err.println("Abriu parenteses e apresentou erros nos parametros ou sem parametros");
                return false;
            }
        }
        //permite epsilon
        return true;
    }
    
    private boolean listaDeParametros(){
        if(listaIdentificadores()){
            if(getNextTokenWithoutRemoving().getNome().equals(":")){
                removeCurrentToken();
                if(eTipo()){
                    removeCurrentToken();
                    if(listaDeParametrosHash()){
                        return true;
                    }
                }
            }
        }
        else{
            System.err.println("Declaracao de identificadores invalida na linha " + getCurrentTokenPosition());
            return false;
        }
        
        return true;
    }
    
    private boolean listaDeParametrosHash(){
        if(getNextTokenWithoutRemoving().getNome().equals(";")){
            removeCurrentToken();
            if(listaIdentificadores()){
                if(getNextTokenWithoutRemoving().getNome().equals(":")){
                    if(eTipo()){
                        return listaDeParametrosHash();
                    }
                    else{
                        System.err.println("Lista de parametros hash invalida (Falta tipo) na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
                else{
                    System.err.println("Lista de parametros hash invalida (Falta ':') na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Lista de parametros hash invalida (Falta lista de identificadores) na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            //permite epsilon
            return true;
        }
    }

    private boolean comandoComposto() {
        if (getNextTokenWithoutRemoving().getNome().equals(BEGIN)) {
            removeCurrentToken();
            
            if(comandosOpcionais()){
                if (getNextTokenWithoutRemoving().getNome().equals(END)) {
                    removeCurrentToken();
                    return true;
                }
                else{
                    System.err.println("Comando composto invalido (Falta 'end') na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                return false;
            }    
        }
        else{
            System.err.println("Comando composto invalido (Falta 'begin') na linha " + getCurrentTokenPosition());
            return false;
        }
        
    }

    private boolean comandosOpcionais() {
        if (listaDeComandos()){
            return true;
        }
        else{
            //permite epsilon
            return true;
        }
    }

    private boolean listaDeComandos() {
        if (comando()) {
            if(listaDeComandosHash()){
                return true;
            }
            else{
                System.err.println("Lista de comandos invalida (Falta lista de comandos hash) na linha " + getCurrentTokenPosition());
                return false;
            }
            
        }
        else{
            System.err.println("Lista de comandos invalida (Falta comando) na linha " + getCurrentTokenPosition());
            return false;
        }
        
    }
    
    private boolean listaDeComandosHash(){
        if (getNextTokenWithoutRemoving().getNome().equals(";")) {
                removeCurrentToken();
                //se chegou aqui deve ter a estrutura do estilo:        comando; ~com pelo menos mais um comando a frente~
                if(comando()){
                    return listaDeComandosHash();
                }
                else{
                    System.err.println("Lista de comandos hash invalida (Falta comando) na linha " + getCurrentTokenPosition());
                    return false;
                }
        }
        else{
            //permite epsilon
            return true;
        }
    }

    private boolean comando() {
        
        if (getNextTokenWithoutRemoving().getTipo().equals(IDENTIFICADOR)) {
            removeCurrentToken();
            
            if (getNextTokenWithoutRemoving().getNome().equals(":=")) {
                removeCurrentToken();
                if(expressao()){
                    //se chegou aqui deve ter a estrutura do estilo:    comando ':=' expressao
                    return true;
                }
                else{
                    System.err.println("Comando desconhecido (Falta Expressao) na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Comando desconhecido (Falta ':=') na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        
        else if(chamadaDeProcedure()){
            return true;
        }
        
        else if(comandoComposto()){
            return true;
        }
        
        else if(getNextTokenWithoutRemoving().getNome().equals(IF)){
            removeCurrentToken();
            if(expressao()){
                if(getNextTokenWithoutRemoving().getNome().equals(THEN)){
                    removeCurrentToken();
                    if(comando()){
                        if(elsePart()){
                            //se chegou aqui deve ter a estrutura do estilo:    if expressao then comando parte_else
                            return true;
                        }
                        //se chegou aqui deve ter a estrutura do estilo:    if expressao then comando
                        return true;
                    }
                    else{
                        System.err.println("Comando invalido (Falta comando) na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
                else{
                    System.err.println("Comando 'if' invalido (Falta 'then') na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Comando invalido (Falta expressao) na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        
        else if(getNextTokenWithoutRemoving().getNome().equals(WHILE)){
            removeCurrentToken();
            if(expressao()){
                if(getNextTokenWithoutRemoving().getNome().equals(DO)){
                    removeCurrentToken();
                    if(comando()){
                        //se chegou aqui deve ter a estrutura do estilo:    while expressao do comando
                        return true;
                    }
                    else{
                        System.err.println("Comando 'do' invalido (Falta comando) na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
                else{
                    System.err.println("Comando 'while' invalido (Falta 'do') na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Comando 'while' invalido (Falta expressao) na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        //nenhuma das possibilidades de um comando
        return false;
    }
    
    private boolean chamadaDeProcedure(){
        if(eIdentificador()){
            if(getNextTokenWithoutRemoving().getNome().equals("(")){
                if(expressionsList()){
                    if(getNextTokenWithoutRemoving().getNome().equals(")")){
                        removeCurrentToken();
                        //se chegou aqui deve ter a estrutura do estilo:    id (lista de expressoes) 
                        return true;
                    }
                    else{
                        System.err.println("Nao fechou o parenteses na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
            }
            else{
                //se chegou aqui deve ter a estrutura do estilo:    id
                return true;
            }
        }
        return false;
    }
     
    private boolean expressionsList(){
        if(expressao()){
            if(getNextTokenWithoutRemoving().getNome().equals(",")){
                removeCurrentToken();
                return expressionsList();
            }
            else{
                //se chegou aqui deve ter a estrutura do estilo:    ~qqr numero de expressoes atras~ expressao
                return true;
            }
        }
        else{
            System.err.println("Chamada de procedimento invalida (Faltam argumentos) na linha " + getCurrentTokenPosition());
            return false;
        }
    }
    
    private boolean expressao(){
        if(simpleExpression()){
            if(isRelationalOperator()){
                if(simpleExpression()){
                    //se chegou aqui deve ter a estrutura do estilo:    expressao operadorRelacional expressao
                    return true;
                }
                else{
                    System.err.println("Expressao invalida (Esperando expressao simples) na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
        }
        else{
            System.err.println("Expressao invalida na linha " + getCurrentTokenPosition());
            return false;
        }
        //se chegou aqui deve ter a estrutura do estilo:    expressao
        return true;
            
        
    }
    
    private boolean simpleExpression(){
        if(term()){
            simpleExpressionHash();
        }
        else if(isSignal()){
            removeCurrentToken();
            if(term()){
                simpleExpressionHash();
            }
        }
        else{
            System.err.println("Expressao simples invalida na linha " + getCurrentTokenPosition());
            return false;
        }
        //se chegou aqui deve ter a estrutura do estilo:    termo expressaoSimples OU sinal termo expressaoSimples
        return true;
    }
    
    private boolean simpleExpressionHash(){
        if(isAdditiveOperator()){
            if(term()){
                return simpleExpressionHash();
            }
        }
        //se chegou aqui deve ter a estrutura do estilo:    /(operadorAditivo termo/)*
        return true;
    }
    
    private boolean term(){
        if(factor()){
            termHash();
            //se chegou aqui deve ter a estrutura do estilo:    fator termoHash OU fator
            return true;
        }
        System.err.println("Termo invalido (Falta fator) na linha " + getCurrentTokenPosition());
        return false;
    }
    
    private boolean termHash(){
        if(isMultiplicativeOperator()){
            if(factor()){
                termHash();
                //se chegou aqui deve ter a estrutura do estilo:    operadorMultiplicativo fator termoHash
                return true;
            }
            else {
                System.err.println("Operador multiplicativo sem fator na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        //termoHash pode ser epsilon
        return true;
        
    }
    
    private boolean factor(){
        if(eIdentificador()){
            if(getNextTokenWithoutRemoving().getNome().equals("(")){
                removeCurrentToken();
                if(expressionsList()){
                    if(getNextTokenWithoutRemoving().getNome().equals(")")){
                        removeCurrentToken();
                    //se chegou aqui deve ter a estrutura do estilo:    id(listaDeExpressoes)
                        return true;
                    }
                    else{
                        System.err.println("Parenteses nao fechado na linha " + getCurrentTokenPosition());
                        return false;
                    }
                    
                }
                else{
                    System.err.println("Abriu parenteses sem expressao valida na linha "+ getCurrentTokenPosition());
                    return false;
                }
            }
            //se chegou aqui deve ter a estrutura do estilo:    id
            return true;
        }
        
        else if(isIntegerNumber()){
            removeCurrentToken();
            return true;
        }
        
        else if(isRealNumber()){
            removeCurrentToken();
            return true;
        }
        
        else if(getNextTokenWithoutRemoving().getNome().equals(TRUE)){
            removeCurrentToken();
            return true;
        }
        
        else if(getNextTokenWithoutRemoving().getNome().equals(FALSE)){
            removeCurrentToken();
            return true;
        }
        
        else if(getNextTokenWithoutRemoving().getNome().equals("(")){
            removeCurrentToken();
            if(expressao()){
                if(getNextTokenWithoutRemoving().getNome().equals(")")){
                    removeCurrentToken();
                    //se chegou aqui deve ter a estrutura do estilo:    (expressao)
                    return true;
                }
                else{
                    System.err.println("Parenteses nao fechado apos expressao na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Abriu parenteses e expressao foi invalida na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        
        else if(getNextTokenWithoutRemoving().getNome().equals("not")){
            removeCurrentToken();
            if(factor()){
                //se chegou aqui deve ter a estrutura do estilo:    not fator
                return true;
            }
            else{
                System.err.println("Fator esperado em " + getCurrentTokenPosition());
                return false;
            }
        }
        
        else{
            System.err.println("Fator invalido na linha " + getCurrentTokenPosition());
            return false;
        }
        
    }
    
    private boolean elsePart(){
        if(getNextTokenWithoutRemoving().getNome().equals(ELSE)){
            if(comando()){
                //se chegou aqui deve ter a estrutura do estilo:    else command
                return true;
            }
            else{
                System.err.println("'else' nao acompanhado de um comando na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        return true;
    }
    
    private boolean eTipo() {
        for (String s : TIPO) {
            if (getNextTokenWithoutRemoving().getNome().equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean eIdentificador(){
        return getNextTokenWithoutRemoving().getTipo().equals(IDENTIFICADOR);
    }
    
    private boolean isIntegerNumber(){
        return getNextTokenWithoutRemoving().getTipo().equals(INTEIRO);
    }
    
    private boolean isRealNumber(){
        return getNextTokenWithoutRemoving().getTipo().equals(REAL);
    }
    
    private boolean isSignal(){
        for (String s : SINAL) {
            if(s.equals(getNextTokenWithoutRemoving().getNome()))
                return true;
        }
        return false;
    }
    
    private boolean isMultiplicativeOperator(){
        for (String s : OPERADOR_MULTIPLICATIVO) {
            if(s.equals(getNextTokenWithoutRemoving().getNome()))
                return true;
        }
        return false;
    }
    
    private boolean isRelationalOperator(){
        for (String s : OPERADOR_RELACIONAL) {
            if(s.equals(getNextTokenWithoutRemoving().getNome()))
                return true;
        }
        return false;
    }
    
    private boolean isAdditiveOperator(){
        for (String s : OPERADOR_ADITIVO) {
            if(s.equals(getNextTokenWithoutRemoving().getNome()))
                return true;
        }
        return false;
    }
    
   
}
