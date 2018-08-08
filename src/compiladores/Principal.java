package compiladores; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Principal {
   
   
    public static void main(String[] args) throws InterruptedException {

        try {
            
            Scanner s = new Scanner(new File("C:\\Users\\Alisson\\Google Drive\\UF"
                    + "\\Compiladores\\AnalisadorLexico\\Compiladores2018.1\\programaSlide.txt")); 
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
            
            Lexico lexico = new Lexico((ArrayList)caracteres.clone()); 
            lexico.percorreCodigo();
              
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

        
    }

}
