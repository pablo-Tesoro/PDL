package TokensTablas.Tokens;

import java.io.PrintWriter;

public class Relacion {
    private int codigo;
    private String caracter;
    private PrintWriter log;

    public Relacion (String car){
        this.caracter = car;
    }

    public int getCod() {
        if(caracter.equals(">")){
            this.codigo = 29;
        }
        else if(caracter.equals("<")) {
            this.codigo = 28;
        }
        else{
            log.println("Error, no se encuentra el operador de relacion");
            codigo = 0;
        }
        return codigo;
    }
}
