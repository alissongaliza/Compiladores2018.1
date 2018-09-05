package compiladores; 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class Principal {
   
   
    public static void main(String[] args) throws InterruptedException, IOException {

        try {
            
            ArrayList<Character> caracteres = leArquivo();
            
            Lexico lexico = new Lexico((ArrayList)caracteres.clone()); 
            
            ArrayList<Token> tokens = lexico.percorreCodigo();
            
            Sintatico sintatico = new Sintatico ((ArrayList<Token>)tokens.clone());
            
            if(sintatico.program())
                JOptionPane.showMessageDialog(null,("Sintatico concluído com sucesso"));
            else{
                JOptionPane.showMessageDialog(null,("Houve problemas com o analisador sintatico"));
            }
              
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

        
    }
    
    private static ArrayList<Character> leArquivo() throws FileNotFoundException, IOException{
        
//            Scanner s = new Scanner(new File ("/home/lumo/Compiladores2018.1/programaSite.txt"));
//            Scanner s = new Scanner(new File("C:\\Users\\Eugenio\\Desktop\\Compiladores2018.1\\programaPdf.txt"));
            Scanner s = new Scanner(new File("C:\\Users\\Alisson\\Google Drive\\UF\\Compiladores\\Compiladores2018.1\\programaMod.txt"));
            String linha;
            String linha2 = "";
            while (s.hasNextLine()) {
                
                linha = s.nextLine();
                if(s.hasNextLine())
                    linha += "\n";
                linha2 += linha; 
                
            } 
            
            ArrayList<Character> caracteres = new ArrayList<>();
            
            System.out.println("Codigo: \n");
            for (int i = 0; i<linha2.length(); i++) {
            caracteres.add(linha2.charAt(i));
                System.out.print(caracteres.get(i));
            
            }
            
            System.out.flush();
            
            return caracteres;
    }

}
