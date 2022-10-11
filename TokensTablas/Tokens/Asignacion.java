package TokensTablas.Tokens;

import java.io.PrintWriter;

public class Asignacion {
    private int codigo;
    private String caracter;
    private PrintWriter log;

    public Asignacion (String car){
        this.caracter = car;
    }

    public int getCod() {
        if(caracter.equals("=")){
            this.codigo = 17;
        }
        else if(caracter.equals("%")) {
            this.codigo = 16;
        }
        else{
            log.println("Error, no se encuentra el operador de asignacion");
            codigo = 0;
        }
        return codigo;
    }
}
