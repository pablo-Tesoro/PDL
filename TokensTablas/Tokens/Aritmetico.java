package TokensTablas.Tokens;

public class Aritmetico {
    private int codigo;
    private String simbolo;

    public Aritmetico(String sim){
        this.simbolo = sim;
    }

    public int getCod() {
        if(simbolo.equals("+")){
            this.codigo = 25;
        }
        else if (simbolo.equals("-")){
            this.codigo = 26;
        }
        else {
            this.codigo = 0;
        }
        return this.codigo;
    }
}
