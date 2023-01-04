package TokensTablas.Tokens;

public class Token {
    private int key;
    private String value;
    private String lex;

    public Token(int cod, String cadena, String lexema) {
        this.key = cod;
        this.value = cadena;
        this.lex = lexema;
    }

    public int getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "<" + getKey() + "," + getValue() + ">\n";
    }

    public String getLexema(){
        return this.lex;
    }

    public String getTipo(){
        switch(getKey()){
            case 1:
                return "boolean";
            case 2:
                return "break";
            case 3:
                return "case";
            case 4:
                return "function";
            case 5:
                return "if";
            case 6:
                return "input";
            case 7:
                return "int";
            case 8:
                return "let";
            case 9:
                return "print";
            case 10:
                return "return";
            case 11:
                return "string";
            case 12:
                return "switch";
            case 13:
                return "entero";
            case 14:
                return "cadena";
            case 15:
                return "id";
            case 16:
                return "%=";
            case 17:
                return "=";
            case 18:
                return ",";
            case 19:
                return ";";
            case 20:
                return ":";
            case 21:
                return "(";
            case 22:
                return ")";
            case 23:
                return "{";
            case 24:
                return "}";
            case 25:
                return "+";
            case 26:
                return "!";
            case 27:
                return ">";
            case 28: 
                return "EOF";
            default:
                System.out.println("Error, codigo de token no identificado: " + this.key);
                return "";
        }
    }
}
