package TokensTablas.Tokens;

public class Token {
    private String key;
    private String value;

    public Token(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "<" + getKey() + "," + getValue() + ">\n";
    }
}
fgfedc
