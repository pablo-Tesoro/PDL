package Gestor;

import lex.AnalizadorLexico;
import sintactico.AnalizadorSintactico;

public class Main {
    public static void main(String args[]) {
        AnalizadorLexico anLex = new AnalizadorLexico("C:/Users/pteso/Documents/PracticaPDL/PDL/pruebas/prueba.txt");
        GestorErrores  gestorErrores = new GestorErrores();
        anLex.generarToken();
        anLex.escribirTokens();    
        AnalizadorSintactico anSin = new AnalizadorSintactico(anLex);
        anSin.generarTS();
        anSin.genParse();
        gestorErrores.imprimirErrores();
    }
}
