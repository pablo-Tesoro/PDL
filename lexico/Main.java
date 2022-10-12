package lexico;

public class Main {
    public static void main(String args[]) {
        AnalizadorLexico an = new AnalizadorLexico();
        an.generarToken();
        an.escribirTokens();
        an.generarTablaSimbolos();
    }
}
