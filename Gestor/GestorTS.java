package Gestor;

import java.util.ArrayList;

public class GestorTS {
    public GestorTS(){
    }

    public elemTS buscarTS(ArrayList<elemTS> tabla, String lex){
        if (tabla != null){
            for (int i=0; i<tabla.size(); i++){
                if(tabla.get(i).getLexema().equals(lex)){
                    return tabla.get(i);
                }
            }
        }
        return null;
    }

    public int calcDesp (String tipo){

        if(tipo.equals("boolean")){
            return 1;
        }
        else if(tipo.equals("string")){
            return 64;
        }
        else if(tipo.equals("entero")){
            return 1;
        }
        else{
            return 0;
        }
    }

}