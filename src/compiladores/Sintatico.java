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
    private static final String INTEIRO = "Numero Inteiro";
    private static final String REAL = "Numero Real";
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
            removeCurrentToken();
            if (getCurrentToken().getTipo().equals(IDENTIFICADOR)) {
                removeCurrentToken();
                if (getCurrentToken().getNome().equals(";")){
                    removeCurrentToken();
                    if (declaracoesVariaveis()) {
                        if (declaracaoDeSubprogramas()) {
                            if(comandoComposto()){
                                if(getCurrentToken().getNome().equals(".")){
                                    removeCurrentToken();
                                    //se chegou aqui deve ter a estrutura do estilo:    program id; declaracoesVariaveis declaracoesSubprogramas comandoComposto.
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
        
        if (getCurrentToken().getNome().matches("[Vv]ar")){
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
            if (getCurrentToken().getNome().equals(":")) {
                removeCurrentToken();
                if (eTipo()) {
                    removeCurrentToken();
                    if (getCurrentToken().getNome().equals(";")) {
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
            if (getCurrentToken().getNome().equals(":")) {
                removeCurrentToken();
                if (eTipo()) {
                    removeCurrentToken();
                    if (getCurrentToken().getNome().equals(";")) {
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
        
        if(getCurrentToken().getNome().equals(",")){
            removeCurrentToken();
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
            if(getCurrentToken().getNome().equals(";")){
                removeCurrentToken();
                return declaracaoDeSubprogramasHash();
            }
        }
        //permite epsilon
        return true;
    }
    
    private boolean declaracaoDeSubprograma() {
        if (getCurrentToken().getNome().matches("[Pp]rocedure")) {
            removeCurrentToken();
            if (eIdentificador()) {
                removeCurrentToken();
                if(argumentos()){
                    if(getCurrentToken().getNome().equals(";")){
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
        if(getCurrentToken().getNome().equals("(")){
            removeCurrentToken();
            if(listaDeParametros()){
                if(getCurrentToken().getNome().equals(")")){
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
            if(getCurrentToken().getNome().equals(":")){
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
        if(getCurrentToken().getNome().equals(";")){
            removeCurrentToken();
            if(listaIdentificadores()){
                if(getCurrentToken().getNome().equals(":")){
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
        if (getCurrentToken().getNome().equals(BEGIN)) {
            removeCurrentToken();
            
            if(comandosOpcionais()){
                if (getCurrentToken().getNome().equals(END)) {
                    removeCurrentToken();
                    return true;
                }
                else{
                    System.err.println("Comando composto invalido (Falta 'end') na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                System.err.println("Comando composto invalido (Falta comandos opcionais) na linha " + getCurrentTokenPosition());
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
        if (getCurrentToken().getNome().equals(";")) {
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
        
        if (getCurrentToken().getTipo().equals(IDENTIFICADOR)) {        //TODO: ALTERAR E CRIAR METODO DE VARIAVEL
            removeCurrentToken();
            
            if (getCurrentToken().getNome().equals(":=")) {
                removeCurrentToken();
                if(expressao()){
                    //se chegou aqui deve ter a estrutura do estilo:    comando := expressao
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
        
        else if(ativacaoDeProcedimento()){
            return true;
        }
        
        else if(comandoComposto()){
            return true;
        }
        
        else if(getCurrentToken().getNome().equals(IF)){
            removeCurrentToken();
            if(expressao()){
                if(getCurrentToken().getNome().equals(THEN)){
                    removeCurrentToken();
                    if(comando()){
                        if(parteElse()){
                            //se chegou aqui deve ter a estrutura do estilo:    if expressao then comando parte_else
                            return true;
                        }
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
        
        else if(getCurrentToken().getNome().equals(WHILE)){
            removeCurrentToken();
            if(expressao()){
                if(getCurrentToken().getNome().equals(DO)){
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
    
    private boolean ativacaoDeProcedimento(){
        if(eIdentificador()){
            removeCurrentToken();
            if(getCurrentToken().getNome().equals("(")){
                if(listaDeExpressoes()){
                    if(getCurrentToken().getNome().equals(")")){
                        removeCurrentToken();
                        //se chegou aqui deve ter a estrutura do estilo:    id (lista de expressoes) 
                        return true;
                    }
                    else{
                        System.err.println("Nao fechou o parenteses na linha " + getCurrentTokenPosition());
                        return false;
                    }
                }
                else{
                    System.err.println("Ativacao de procedimento invalida (Falta lista de expressoes) na linha " + getCurrentTokenPosition());
                    return false;
                }
            }
            else{
                //se chegou aqui deve ter a estrutura do estilo:    id
                return true;
            }
        }
        else{
            System.err.println("Ativacao de procedimento invalida (Falta identificador) na linha " + getCurrentTokenPosition());
            return false;
        }
        
    }
     
    private boolean listaDeExpressoes(){
        if(expressao()){
            if(listaDeExpressoesHash()){
                return true;
            }
            else{
                System.err.println("Lista de expressoes invalida (Falta lista de expressoes hash) na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            System.err.println("Chamada de procedimento invalida (Faltam argumentos) na linha " + getCurrentTokenPosition());
            return false;
        }
    }
    
    private boolean listaDeExpressoesHash(){
        if(getCurrentToken().getNome().equals(",")){
            removeCurrentToken();
            if(expressao()){
                return listaDeExpressoesHash();

            }
            else{
                System.err.println("Lista de expressoes hash invalida (Falta expressao)" + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            //se chegou aqui deve ter a estrutura do estilo:    ~qqr numero de expressoes atras~ expressao
            return true;
        }
    }
    
    private boolean expressao(){
        if(expressaoSimples()){
            if(isRelationalOperator()){
                if(expressaoSimples()){
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
    
    private boolean expressaoSimples(){
        if(termo()){
            expressaoSimplesHash();
        }
        else if(isSignal()){
            removeCurrentToken();
            if(termo()){
                expressaoSimplesHash();
            }
        }
        else{
            System.err.println("Expressao simples invalida na linha " + getCurrentTokenPosition());
            return false;
        }
        //se chegou aqui deve ter a estrutura do estilo:    termo expressaoSimples OU sinal termo expressaoSimples
        return true;
    }
    
    private boolean expressaoSimplesHash(){
        if(isAdditiveOperator()){
            if(termo()){
                return expressaoSimplesHash();
            }
        }
        //se chegou aqui deve ter a estrutura do estilo:    /(operadorAditivo termo/)*
        return true;
    }
    
    private boolean termo(){
        if(fator()){
            if(termoHash()){
                //se chegou aqui deve ter a estrutura do estilo:    fator termoHash OU fator
                return true;
            }
            else{
                return false;
            }
        }
        else{
            System.err.println("Termo invalido (Falta fator) na linha " + getCurrentTokenPosition());
            return false;
        }
    }
    
    private boolean termoHash(){
        if(isMultiplicativeOperator()){
            if(fator()){
                termoHash();
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
    
    private boolean fator(){
        if(eIdentificador()){
            if(getCurrentToken().getNome().equals("(")){
                removeCurrentToken();
                if(listaDeExpressoes()){
                    if(getCurrentToken().getNome().equals(")")){
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
        
        else if(getCurrentToken().getNome().equals(TRUE)){
            removeCurrentToken();
            return true;
        }
        
        else if(getCurrentToken().getNome().equals(FALSE)){
            removeCurrentToken();
            return true;
        }
        
        else if(getCurrentToken().getNome().equals("(")){
            removeCurrentToken();
            if(expressao()){
                if(getCurrentToken().getNome().equals(")")){
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
        
        else if(getCurrentToken().getNome().equals("not")){
            removeCurrentToken();
            if(fator()){
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
    
    private boolean parteElse(){
        if(getCurrentToken().getNome().equals(ELSE)){
            removeCurrentToken();
            if(comando()){
                //se chegou aqui deve ter a estrutura do estilo:    else command
                return true;
            }
            else{
                System.err.println("'else' nao acompanhado de um comando na linha " + getCurrentTokenPosition());
                return false;
            }
        }
        else{
            //parte else pode ser epsilon
            return true;
        }
    }
    
    private boolean eTipo() {
        for (String s : TIPO) {
            if (getCurrentToken().getNome().equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean eIdentificador(){
        return getCurrentToken().getTipo().equals(IDENTIFICADOR);
    }
    
    private boolean isIntegerNumber(){
        return getCurrentToken().getTipo().equals(INTEIRO);
    }
    
    private boolean isRealNumber(){
        return getCurrentToken().getTipo().equals(REAL);
    }
    
    private boolean isSignal(){
        for (String s : SINAL) {
            if(s.equals(getCurrentToken().getNome()))
                return true;
        }
        return false;
    }
    
    private boolean isMultiplicativeOperator(){
        for (String s : OPERADOR_MULTIPLICATIVO) {
            if(s.equals(getCurrentToken().getNome()))
                return true;
        }
        return false;
    }
    
    private boolean isRelationalOperator(){
        for (String s : OPERADOR_RELACIONAL) {
            if(s.equals(getCurrentToken().getNome()))
                return true;
        }
        return false;
    }
    
    private boolean isAdditiveOperator(){
        for (String s : OPERADOR_ADITIVO) {
            if(s.equals(getCurrentToken().getNome()))
                return true;
        }
        return false;
    }
    
   
}
