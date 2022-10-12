package TokensTablas.Tokens;

public class PalabrasReservadas {
    private String lexema;
    private String[] palabras = {"boolean", "break", "case", "function", "if", "input", "int", "let", "print", "return", "string", "switch"};
    private int codigo;

    public PalabrasReservadas (String lex) {
        this.lexema = lex;
        this.codigo = 0;
    }

    public int getCod() {
        for(int i=0; i<12; i++) {
            if(palabras[i].equals(lexema)){
                codigo = i + 1;
            }
        }
        return codigo;
    }
}
