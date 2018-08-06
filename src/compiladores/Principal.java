package compiladores; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Principal {
   
   
    public static void main(String[] args) throws InterruptedException {

        boolean aux = false;//verificador do primeiro digito do arquivo
        try {
            
            // Criando um objeto da classe Scanner, ao qual vai ler o arquivo.
//            Scanner s = new Scanner(new File("/home/rhenan/programa.txt")); 
            Scanner s = new Scanner(new File("C:\\Users\\Alisson\\Google Drive\\UF"
                    + "\\Compiladores\\AnalisadorLexico\\Compiladores2018.1\\programaPdf.txt")); 
            String linha = "";
            String linha2 = ""; 
            int nlinha = 0;
            int nchar = 0;
            
            while (s.hasNextLine()) {
                
                linha = s.nextLine();
                if(s.hasNextLine())
                    linha += "\n";
                nlinha++;
                linha2 += linha; 
                //System.out.println(linha);
                
            } 
            
            //System.out.println(linha2);
            
            ArrayList<Character> caracteres = new ArrayList<>(); 
            
            for (int i = 0; i<linha2.length(); i++) {
            
            caracteres.add(linha2.charAt(i));
                System.out.print(caracteres.get(i));
            
            }
            
            //char[] caracteres = linha2.toCharArray();
            //System.out.println(caracteres.length);

            Lexico lexico = new Lexico((ArrayList)caracteres.clone()); 
            lexico.inicio();
              
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        
    }

}
