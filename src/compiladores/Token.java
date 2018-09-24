package compiladores;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lumo
 */
public class Token {
    private int numeroLinha;
    private final String tipo;
    private final String nome;
    
    
    public Token(String nome, String tipo, int numero){
        this.nome = nome;
        this.tipo = tipo;
        numeroLinha = numero;
        
        
    }
    
    public Token(String nome, String tipo){
        this.nome = nome;
        this.tipo = tipo;
        
    }
	
    public String getNome(){

	return nome;
    }

    public int getNumero(){

	return numeroLinha;
    }
	
    public String getTipo(){

	return tipo;
    }
}
    

