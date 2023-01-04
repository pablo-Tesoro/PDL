package Gestor;

import java.util.ArrayList;
import java.io.*;

public class GestorErrores {
    private ArrayList<Error> errores;
    private FileWriter fw;
    private BufferedWriter bw;
    private int nErrores;

    public GestorErrores(){
        this.errores = new ArrayList<Error>();
        this.nErrores = 0;
    }

    public void add (Error error){
        errores.add(error);
    }

    public void incrErrores(){
        this.nErrores++;
    }

    public void imprimirErrores(){
        try{
            String path = new File("").getAbsolutePath();
            path = path.concat("\\pruebas\\errores.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            if(this.nErrores > 0){
                for (int i=0; i<errores.size(); i++){
                    bw.write("Error " + (i+1) + " --> " + errores.get(i).getError());
                    if(i != errores.size() - 1){
                        bw.newLine();
                    }
                }
            }
            else{
                bw.write("No hay ningun error");
                bw.newLine();
            }
            System.out.println("Errores generados correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}
