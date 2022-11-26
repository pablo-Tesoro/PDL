package lex;

import java.util.ArrayList;
import java.util.Map;
import java.io.*;

public class GestorErrores {
    private ArrayList<Map.Entry<Integer, String>> gestor;
    private ArrayList<Integer> lineas;
    private FileWriter fw;
    private BufferedWriter bw;

    public GestorErrores(){
        this.gestor = new ArrayList<>();
        this.lineas = new ArrayList<>();
    }

    void add (Map.Entry<Integer, String> entrada){
        gestor.add(entrada);
    } 

    void anadirLinea(int line){
        lineas.add(line);
    }

    void imprimirErrores(int line){
        try{
            String path = new File("").getAbsolutePath();
		    path = path.concat("\\pruebas\\errores.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            for (int i=0; i<gestor.size(); i++){
                bw.write("Error " + gestor.get(i).getKey() + ": Caracter no esperado en la linea " + lineas.get(i) + " --> " + gestor.get(i).getValue());
                if(i != gestor.size() - 1){
                    bw.newLine();
                }
                
            }
            System.out.println("Errores generados correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}
