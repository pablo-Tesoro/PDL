package lexico;

public class PalRes {
    private boolean res;
    private String[] palabras = {"boolean", "break", "case", "function", "if", "input", "int", "let", "print", "return", "string", "switch"};
    public PalRes() {
        this.res = false;
    }

    public boolean esPalRes(String lex) {
        for (int i=0; i<palabras.length; i++) {
            if(palabras[i].equals(lex)){
                res = true;
            }
        }
        return res;
    }
}
