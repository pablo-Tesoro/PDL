package lexico;

import java.util.ArrayList;
import java.io.*;
import TokensTablas.Tokens.*;
import TokensTablas.Tablas.*;

public class AnalizadorLexico {
    public FileReader fr;
    public FileWriter fw;
    private BufferedWriter bw;
    private String caracter;
    private ArrayList<Token> tokensGenerados;
    private boolean fin;
    private int contTabla = 1;
    private ArrayList<String> identificadores = new ArrayList<String>();
    private int pos = 1;

    public AnalizadorLexico(String fichero) {
        try{
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\" + fichero + ".txt");
			this.fr = new FileReader(path);
            this.fin= false;
            this.tokensGenerados = new ArrayList<Token>();
            leerCar();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void leerCar() {
        try{
            int pos = fr.read();
            if(pos == -1){
                this.caracter = "EOF";
            }
            else {
                this.caracter = Character.toString((char) pos);
            }
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void generarToken() {
        Token token = null;
        int estado = 0;
        String lexema = "";
        Caracter car = new Caracter();
        while(!fin){
            switch(estado){
            case 0:
                if(car.esDelimitador(caracter) || caracter.equals("\r")){
                    leerCar();
                }
                else if(car.esLetra(caracter) || caracter.equals("_")){
                    estado = 1;
                    lexema += caracter;
                }
                else if (car.esDigito(caracter)){
                    estado = 2;
                    lexema += caracter;
                }
                else if(caracter.equals("+")){
                    estado = 5;
                }
                else if(caracter.equals("-")){
                    estado = 5;
                }
                else if(caracter.equals("%")){
                    lexema += caracter;
                    estado = 8;
                    leerCar();
                    lexema += caracter;
                }
                else if(caracter.equals("=")){
                    lexema += caracter;
                    estado = 8;
                }
                else if(caracter.equals("<")){
                    estado = 11;
                }
                else if(caracter.equals(">")){
                    Relacion rel = new Relacion(caracter);
                    token = new Token(rel.getCod(), "");
                    tokensGenerados.add(token);
                }
                else if(caracter.equals("!")){
                    estado = 15;
                }
                else if(caracter.equals("(") || caracter.equals(")") || caracter.equals("{") || caracter.equals("}") || caracter.equals(":") || caracter.equals(",") || caracter.equals(";")){
                    estado = 16;
                }
                else if(caracter.equals("EOF")){
                    token = new Token(30, "");
                    tokensGenerados.add(token);                
                    fin = true;
                }
                else if(caracter.equals("'")){

                    leerCar();
                    while(!caracter.equals("'")){
                        lexema += caracter;
                        leerCar();   
                    }
                    estado = 22;
                }
                else if(caracter.equals("/")){
                    leerCar();
                    if(caracter.equals("/")){
                        estado = 18;
                    }
                }
                else{
                    leerCar();
                }
                break;
            case 1:
                leerCar();
                if(car.esDigito(caracter) || car.esLetra(caracter) || caracter.equals("_")){
                    lexema += caracter;
                }
                else {
                    estado = 20;
                }
                break;
            case 2:
                leerCar();
                if(car.esDigito(caracter)){
                    lexema += caracter;
                }
                else {
                    estado = 21;
                }
                break;
            case 5: 
                Aritmetico ar = new Aritmetico(caracter);
                token = new Token(ar.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 8:
                Asignacion asig = new Asignacion(lexema);
                token = new Token(asig.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                lexema = "";
                break;
            case 11:
                Relacion rel = new Relacion(caracter);
                token = new Token(rel.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 15:
                Logico log = new Logico();
                token = new Token(log.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 16:
                Simbolos sim = new Simbolos(caracter);
                token = new Token(sim.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 18:
                while(!caracter.equals("\r") && !caracter.equals("EOF")){
                    leerCar();
                }
                leerCar();
                estado = 0;
                break;
            case 20:
                PalRes palres = new PalRes();
                if(palres.esPalRes(lexema)){
                    PalabrasReservadas palabra = new PalabrasReservadas(lexema);
                    token = new Token(palabra.getCod(), "");
                    tokensGenerados.add(token);
                    lexema = "";
                    estado = 0;
                }
                else{
                    Identificador id = new Identificador();
                    Boolean anadir = true;
                    if(identificadores.size()>0){
                        for(int i=0; i<identificadores.size(); i++){
                            if(identificadores.get(i).equals(lexema)){
                                anadir = false;
                                token = new Token(id.getCod(), (i+1) + "");
                            }
                        }
                        if(anadir){
                            identificadores.add(lexema);
                            pos += 1;
                            token = new Token(id.getCod(), pos + "");
                        }      
                    }
                    else {
                        identificadores.add(lexema);
                        token = new Token(id.getCod(), pos + "");
                    }
                    tokensGenerados.add(token);
                    lexema = "";
                    estado = 0;
                }
                break;
            case 21:
                Entero num = new Entero();
                token = new Token(num.getCod(), lexema);
                tokensGenerados.add(token);
                estado = 0;
                lexema = "";
                break;
            case 22:
                Cadena cadena = new Cadena(lexema);
                token = new Token(cadena.getCod(), "\"" + cadena.getValue() + "\"");
                tokensGenerados.add(token);
                estado = 0;
                lexema = "";
                leerCar();
                break;
            }
        }
    }

    public void escribirTokens(){
        try{
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\tokens.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            for(int i=0; i<tokensGenerados.size(); i++) {
                String token = tokensGenerados.get(i).toString();
                bw.write(token);
            }
            System.out.println("Tokens generados correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    public void generarTablaSimbolos(){
        try{
            String path = new File("").getAbsolutePath();
		    path = path.concat("\\pruebas\\tabla.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write("CONTENIDOS DE LA TABLA # 1 :");
            bw.newLine();
            bw.newLine();
            for(int i=0; i<identificadores.size(); i++){
                bw.write("* LEXEMA : " + "'" + identificadores.get(i) + "'");
                bw.newLine();
                bw.write("  --------- ---------");
                bw.newLine();
            }
            System.out.println("Tabla de Simbolos generada correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}