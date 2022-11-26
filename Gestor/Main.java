package Gestor;

import java.util.Scanner;
import lex.AnalizadorLexico;
import sintactico.AnalizadorSintactico;
import java.util.ArrayList;
import java.io.*;

public class Main {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre de la prueba: ");
        String fichero = scanner.next();
        AnalizadorLexico anLex = new AnalizadorLexico(fichero);
        ArrayList<Integer> parse;
        anLex.generarToken();
        anLex.escribirTokens();
        anLex.generarTablaSimbolos();
        anLex.generarErrores();
        AnalizadorSintactico anSin = new AnalizadorSintactico(anLex);
        anSin.generarParse();
        parse = anSin.getParse();
        generarParse(parse);
        scanner.close();
    }

    public static void generarParse(ArrayList<Integer> parse){
        try{
            FileWriter fw;
            BufferedWriter bw;
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\parse.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            for(int i=0; i<parse.size(); i++) {
                String regla = parse.get(i).toString();
                if(i==0){
                    bw.write("Descendente " + regla);
                }
                else{
                    bw.write(" " + regla);
                }
                
            }
            System.out.println("Parse generado correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}
