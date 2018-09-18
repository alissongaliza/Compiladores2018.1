package compiladores; 

import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author Dayane
 */
public class Sintatico {

    private ArrayList<Token> tokens;

    private static final String[] TIPO = new String[] {
        "integer|INTEGER",
        "real|REAL",
        "boolean|BOOLEAN",
        "char|CHAR"
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
        ">=",
        "<>",
    };
    private static final String[] SINAL = new String[]{
        "+",
        "-"
    };
    
    private static final String INTEIRO = "Numero Inteiro";
    private static final String REAL = "Numero Real";
    private static final String IDENTIFICADOR = "Identificador";
    private static final String TRUE = "true|TRUE";
    private static final String FALSE = "false|FALSE";
    private static final String IF = "if|IF";
    private static final String THEN = "then|THEN";
    private static final String ELSE = "else|ELSE";
    private static final String WHILE = "while|WHILE";
    private static final String DO = "do|DO";
    private static final String BEGIN = "begin|BEGIN";
    private static final String END = "end|END";
    private static final String PROGRAM = "program|PROGRAM";
    private static final String VAR = "var|VAR";
    private static final String PROCEDURE = "procedure|PROCEDURE";
    private static final String NOT = "not|NOT";
    private Semantico s;
    
    public Sintatico(ArrayList<Token> listaLexico) {
        this.tokens = listaLexico;
        s = new Semantico();
    }
    
    private void removeCurrentToken(){
        tokens.remove(0);
    }

    private Token getCurrentToken() {
        return tokens.get(0);
    }
    
    private Token getNextToken(){
        return tokens.get(1);
    }
    
    private int getCurrentTokenPosition(){
        return tokens.get(0).getNumero();
    }

    public boolean program() {
        
        if (getCurrentToken().getNome().matches(PROGRAM)) {
            removeCurrentToken();
            if (getCurrentToken().getTipo().equals(IDENTIFICADOR)) {
                s.pilhaRecorrencia.push(new Token("#","Marcador de Programa"));
                s.pilhaRecorrencia.push(getCurrentToken());
                removeCurrentToken();
                if (getCurrentToken().getNome().equals(";")){
                    removeCurrentToken();
                    if (declaracoesVariaveis()) {
                        if (declaracaoDeSubprogramas()) {
                            if(comandoComposto()){
                                if(getCurrentToken().getNome().equals(".")){
                                    removeCurrentToken();
                                    //se chegou aqui deve ter a estrutura do estilo:    program id; declaracoesVariaveis declaracoesSubprogramas comandoComposto.
                                    JOptionPane.showMessageDialog(null, "success");
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
        
        if (getCurrentToken().getNome().matches(VAR)){
            removeCurrentToken();
            if(listaDeclaracoesVariaveis()){
                return true;
            }
            else{
           //     System.err.println("Declaracoes variaveis invalida (Falta lista de declaracoes de variaveis) na linha " + getCurrentTokenPosition());
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
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
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
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
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
                return false;
            }
            
        }
        else{
            //System.err.println("Lista de identificadores invalida (Falta identificador) na linha " + getCurrentTokenPosition());
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
                    return false;
                }

            }
            else{
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
        if (getCurrentToken().getNome().matches(PROCEDURE)) {
            removeCurrentToken();
            if (eIdentificador()) {
                s.pilhaRecorrencia.push(new Token("#","Marcador normal"));
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
                        return false;
                    }
                    
                }
                else{
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
                    return false;
                }
            }
            else{
                //se chegou aqui deve ter a estrutura do estilo:    (~sem argumentos OU argumentos errados~
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
            return false;
        }
        
        return true;
    }
    
    private boolean listaDeParametrosHash(){
        if(getCurrentToken().getNome().equals(";")){
            removeCurrentToken();
            if(listaIdentificadores()){
                if(getCurrentToken().getNome().equals(":")){
                    removeCurrentToken();
                    if(eTipo()){
                        removeCurrentToken();
                        return listaDeParametrosHash();
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            //permite epsilon
            return true;
        }
    }

    private boolean comandoComposto() {
        if (getCurrentToken().getNome().matches(BEGIN)) {
            removeCurrentToken();
            s.incEscopo();
            
            if(comandosOpcionais()){
                if (getCurrentToken().getNome().matches(END)) {
                    removeCurrentToken();
                    s.decEscopo();
                    //se o escopo for 0, posso desempilhar o escopo. SE não for 0, o 'end' foi de algum if/while
                    if(s.getEscopo() == 0)
                        s.desempilhaEscopo();
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }    
        }
        else{
            //System.err.println("Comando composto invalido (Falta 'begin') na linha " + getCurrentTokenPosition());
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
                //System.err.println("Lista de comandos invalida (Falta lista de comandos hash) na linha " + getCurrentTokenPosition());
                return false;
            }
            
        }
        else{
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
                    //System.err.println("Lista de comandos hash invalida (Falta comando) na linha " + getCurrentTokenPosition());
                    return false;
                }
        }
        else{
            //permite epsilon
            return true;
        }
    }

    private boolean comando() {
        
        if (variavel()) {          
            if (getCurrentToken().getNome().equals(":=")) {
                removeCurrentToken();
                if(expressao()){
                    //se chegou aqui deve ter a estrutura do estilo:    comando := expressao
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(getCurrentToken().getNome().equals("(")){
                removeCurrentToken();
                if(listaDeExpressoes()){
                    if(getCurrentToken().getNome().equals(")")){
                        removeCurrentToken();
                        //se chegou aqui deve ter a estrutura do estilo:    id (lista de expressoes) 
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
        }
        
        else if(ativacaoDeProcedimento()){
            return true;
        }
        
        else if(comandoComposto()){
            return true;
        }
        
        else if(getCurrentToken().getNome().matches(IF)){
            removeCurrentToken();
            if(expressao()){
                if(getCurrentToken().getNome().matches(THEN)){
                    removeCurrentToken();
                    if(comando()){
                        if(parteElse()){
                            //se chegou aqui deve ter a estrutura do estilo:    if expressao then comando parte_else
                            return true;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        
        else if(getCurrentToken().getNome().matches(WHILE)){
            removeCurrentToken();
            if(expressao()){
                if(getCurrentToken().getNome().matches(DO)){
                    removeCurrentToken();
                    if(comando()){
                        //se chegou aqui deve ter a estrutura do estilo:    while expressao do comando
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        
        //nenhuma das possibilidades de um comando
        return false;
    }
    
    private boolean variavel(){
        if(eIdentificador()){
            removeCurrentToken();
            return true;
        }
        else{
            return false;
        }
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
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                //se chegou aqui deve ter a estrutura do estilo:    id
                return true;
            }
        }
        else{
            //System.err.println("Ativacao de procedimento invalida (Falta identificador) na linha " + getCurrentTokenPosition());
            return false;
        }
        
    }
     
    private boolean listaDeExpressoes(){
        if(expressao()){
            if(listaDeExpressoesHash()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
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
                removeCurrentToken();
                if(expressaoSimples()){
                    //se chegou aqui deve ter a estrutura do estilo:    expressao operadorRelacional expressao
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        else{
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
            return false;
        }
        //se chegou aqui deve ter a estrutura do estilo:    termo expressaoSimples OU sinal termo expressaoSimples
        return true;
    }
    
    private boolean expressaoSimplesHash(){
        if(isAdditiveOperator()){
            removeCurrentToken();
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
            return false;
        }
    }
    
    private boolean termoHash(){
        if(isMultiplicativeOperator()){
            removeCurrentToken();
            if(fator()){
                termoHash();
                //se chegou aqui deve ter a estrutura do estilo:    operadorMultiplicativo fator termoHash
                return true;
            }
            else {
                return false;
            }
        }
        //termoHash pode ser epsilon
        return true;
        
    }
    
    private boolean fator(){
        if(eIdentificador()){
            removeCurrentToken();
            if(getCurrentToken().getNome().equals("(")){
                removeCurrentToken();
                if(listaDeExpressoes()){
                    if(getCurrentToken().getNome().equals(")")){
                        removeCurrentToken();
                    //se chegou aqui deve ter a estrutura do estilo:    id(listaDeExpressoes)
                        return true;
                    }
                    else{
                        return false;
                    }
                    
                }
                else{
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
        
        else if(getCurrentToken().getNome().matches(TRUE)){
            removeCurrentToken();
            return true;
        }
        
        else if(getCurrentToken().getNome().matches(FALSE)){
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
                    return false;
                }
            }
            else{
                return false;
            }
        }
        
        else if(getCurrentToken().getNome().matches(NOT)){
            removeCurrentToken();
            if(fator()){
                //se chegou aqui deve ter a estrutura do estilo:    not fator
                return true;
            }
            else{
                return false;
            }
        }
        
        else{
            return false;
        }
        
    }
    
    private boolean parteElse(){
        if(getCurrentToken().getNome().matches(ELSE)){
            removeCurrentToken();
            if(comando()){
                //se chegou aqui deve ter a estrutura do estilo:    else command
                return true;
            }
            else{
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
            if (getCurrentToken().getNome().matches(s)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean eIdentificador(){
        if(getCurrentToken().getTipo().matches(IDENTIFICADOR)){
            if(s.analisaExistencia(getCurrentToken())){
                if(s.getEscopo() == 0){
                    System.err.println("Variavel " + getCurrentToken().getNome() 
                            + " ja declarada na linha " + getCurrentToken().getNumero());
                }
                else{
                    //ta na pilha e to usando
                }
            }
            
            else{
                if(s.getEscopo() > 0){
                    System.err.println("Variavel " + getCurrentToken().getNome() 
                            + " nao declarada na linha " + getCurrentToken().getNumero());
                }
                else{
                    s.pilhaRecorrencia.push(getCurrentToken());
                }
            }
            return true;
        }
        else{
            return false;
        }
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
