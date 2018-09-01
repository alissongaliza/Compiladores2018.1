
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {
   
   
    public static void main(String[] args) throws InterruptedException {

        boolean aux = false;//verificador do primeiro digito do arquivo
        try {
            
            // Criando um objeto da classe Scanner, ao qual vai ler o arquivo.
            Scanner s = new Scanner(new File("/home/jonathan/Documentos/p.txt")); 
            String ln = "";
            String ln2 = ""; 
            int nln = 0;
            int nchar = 0;
            
            while (s.hasNextLine()) {
                
                ln = s.nextLine();
                if(s.hasNextLine())
                    ln += "\n";
                nln++;
                ln2 += ln; 
                //System.out.println(ln);
                
            } 
            //System.out.println(ln2);
            
            ArrayList<Character> caracteres = new ArrayList<>(); 
            
            for (int i = 0; i<ln2.length(); i++) {
                caracteres.add(ln2.charAt(i));
               // System.out.println(caracteres.get(i));
            }
            
            //char[] caracteres = ln2.toCharArray();
            //System.out.println(caracteres.length);

            Lexico lexico = new Lexico((ArrayList)caracteres.clone()); 
            lexico.inicio();
            Sintatico sintatico;
            sintatico = new Sintatico(lexico.retornaToken());
              
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        
    }

}