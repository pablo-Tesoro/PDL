package TokensTablas.Tokens;

import java.io.PrintWriter;

public class Simbolos {
    private String simbolo;
    private int cod;
    private PrintWriter log;

    public Simbolos(String sim) {
        this.simbolo = sim;
    }

    public int getCod () {
        if (simbolo.equals(",")) {
            this.cod = 18;
        }
        else if (simbolo.equals(";")) {
            this.cod = 19;
        }
        else if (simbolo.equals(":")) {
            this.cod = 20;
        }
        else if (simbolo.equals("(")) {
            this.cod = 21;
        }
        else if (simbolo.equals(")")) {
            this.cod = 22;
        }
        else if (simbolo.equals("{")) {
            this.cod = 23;
        }
        else if (simbolo.equals("}")) {
            this.cod = 24;
        }
        else{
            log.println("Error, no se encuentra el simbolo");
            cod = 0;
        }
        return cod;
    }
}
