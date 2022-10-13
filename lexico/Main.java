package lexico;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre de la prueba: ");
        String fichero = scanner.next();
        AnalizadorLexico an = new AnalizadorLexico(fichero);   
        an.generarToken();
        an.escribirTokens();
        an.generarTablaSimbolos();
    }
}
