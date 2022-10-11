package TokensTablas.Tokens;

public class Token {
    private int key;
    private String value;

    public Token(int cod, String cadena) {
        this.key = cod;
        this.value = cadena;
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
}
