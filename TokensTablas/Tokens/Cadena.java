package TokensTablas.Tokens;

public class Cadena {
    private String cadena;
    private int cod;

    public Cadena(String cad){
        this.cadena = cad;
        this.cod = 14;
    }

    public int getCod() {
        return this.cod;
    }

    public String getValue(){
        return this.cadena;
    }
}