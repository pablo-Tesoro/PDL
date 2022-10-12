package TokensTablas.Tokens;

public class Asignacion {
    private int codigo;
    private String caracter;

    public Asignacion (String car){
        this.caracter = car;
    }

    public int getCod() {
        if(caracter.equals("=")){
            this.codigo = 17;
        }
        else if(caracter.equals("%=")) {
            this.codigo = 16;
        }
        else{
            System.out.println("Error, no se encuentra el operador de asignacion: " + caracter);
            codigo = 0;
        }
        return codigo;
    }
}
