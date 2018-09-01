/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

/**
 *
 * @author Alisson
 */
public class PalavraIgnorada {
    
    private final String conteudo;
    private final String mensagem;

    public PalavraIgnorada(String conteudo, String tipo) {
        this.conteudo = conteudo;
        this.mensagem = tipo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getMensagem() {
        return mensagem;
    }

    @Override
    public String toString() {
        return "Conteudo:    " + conteudo + ", Mensagem:    " + mensagem;
    }
    
    
    
    
    
}
