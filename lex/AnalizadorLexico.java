package lex;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.io.*;
import TokensTablas.Tokens.*;

public class AnalizadorLexico {
    public FileReader fr;
    public FileWriter fw;
    private BufferedWriter bw;
    private String caracter;
    private ArrayList<Token> tokensGenerados;
    private boolean fin;
    private ArrayList<String> identificadores = new ArrayList<String>();
    private int pos = 1;
    private int contadorLinea = 1;
    private int contadorErrores = 0;
    private GestorErrores gestorErrores;
    private Entry<Integer, String> entrada;

    public AnalizadorLexico(String fichero) {
        try{
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\" + fichero + ".txt");
			this.fr = new FileReader(path);
            this.fin = false;
            this.tokensGenerados = new ArrayList<Token>();
            this.gestorErrores = new GestorErrores();
            leerCar();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void leerCar() {
        try{
            int pos1 = fr.read();
            if(pos1 == -1){
                this.caracter = "EOF";
            }
            else {
                this.caracter = Character.toString((char) pos1);
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
                    if(caracter.equals("\r")){
                        contadorLinea++;
                    }
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
                    estado = 3;
                }
                else if(caracter.equals("%")){
                    lexema += caracter;
                    leerCar();
                    if(caracter.equals("=")){
                        estado = 4;
                        lexema += caracter;
                    }
                    else{
                        System.out.println("Error, caracter esperado: =, caracter recibido: " + caracter);
                        leerCar();
                        estado = 0;
                        lexema = "";
                    }
                }
                else if(caracter.equals("=")){
                    lexema += caracter;
                    estado = 4;
                }
                else if(caracter.equals(">")){
                    estado = 5;
                }
                else if(caracter.equals("!")){
                    estado = 6;
                }
                else if(caracter.equals("(") || caracter.equals(")") || caracter.equals("{") || caracter.equals("}") || caracter.equals(":") || caracter.equals(",") || caracter.equals(";")){
                    estado = 7;
                }
                else if(caracter.equals("EOF")){
                    estado = 8;
                }
                else if(caracter.equals("'")){
                    estado = 13;
                    
                    estado = 9;
                }
                else if(caracter.equals("/")){
                    estado = 10;
                }
                else{
                    contadorErrores += 1;
                    this.entrada = new AbstractMap.SimpleEntry<>(contadorErrores, caracter);
                    gestorErrores.anadirLinea(contadorLinea);
                    gestorErrores.add(entrada);
                    leerCar();
                }
                break;
            case 1:
                leerCar();
                if(car.esDigito(caracter) || car.esLetra(caracter) || caracter.equals("_")){
                    lexema += caracter;
                }
                else {
                    estado = 11;
                }
                break;
            case 2:
                leerCar();
                if(car.esDigito(caracter)){
                    lexema += caracter;
                }
                else {
                    estado = 12;
                }
                break;
            case 3: 
                Aritmetico ar = new Aritmetico();
                token = new Token(ar.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 4:
                Asignacion asig = new Asignacion(lexema);
                token = new Token(asig.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                lexema = "";
                break;
            case 5:
                Relacion rel = new Relacion();
                token = new Token(rel.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 6:
                Logico log = new Logico();
                token = new Token(log.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 7:
                Simbolos sim = new Simbolos(caracter);
                token = new Token(sim.getCod(), "");
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 8:
                token = new Token(28, "");
                tokensGenerados.add(token);                
                fin = true;
                break;
            case 9:
                leerCar();
                while(!caracter.equals("'")){
                    lexema += caracter;
                    leerCar();
                }
                estado = 13;
                break;
            case 10:
                leerCar();
                if(caracter.equals("/")){
                    estado = 14;
                }
                else{
                    System.out.println("Error, caracter esperado: /, caracter recibido: " + caracter);
                    leerCar();
                    estado = 0;
                }
                break;
            case 11:
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
            case 12:
                Entero num = new Entero();
                token = new Token(num.getCod(), lexema);
                tokensGenerados.add(token);
                estado = 0;
                lexema = "";
                break;
            case 13:
                Cadena cadena = new Cadena(lexema);
                token = new Token(cadena.getCod(), "\"" + cadena.getValue() + "\"");
                tokensGenerados.add(token);
                estado = 0;
                lexema = "";
                leerCar();
                break;
            case 14:
                while(!caracter.equals("\r") && !caracter.equals("EOF")){
                    leerCar();
                }
                leerCar();
                estado = 0;
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
    
    public void generarErrores(){
        if(contadorErrores > 0){
            gestorErrores.imprimirErrores(contadorLinea);
        }
    }
    public ArrayList<Token> getTokens(){
        return this.tokensGenerados;
    }
}