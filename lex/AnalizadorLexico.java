package lex;

import java.util.ArrayList;
import java.io.*;
import TokensTablas.Tokens.*;
import Gestor.GestorErrores;
import Gestor.Error;

public class AnalizadorLexico {
    private FileReader fr;
    private FileWriter fw;
    private BufferedWriter bw;
    private String caracter;
    private ArrayList<Token> tokensGenerados;
    private boolean fin;
    private ArrayList<String> identificadores;
    private int pos = 1;
    private int contadorLinea = 1;
    private GestorErrores gestorErrores;

    public AnalizadorLexico(String fichero, GestorErrores gestorErr) {
        try{
            //String path = new File("").getAbsolutePath();
			//path = path.concat("\\pruebas\\" + fichero + ".txt");
			this.fr = new FileReader(fichero);
            this.fin = false;
            this.tokensGenerados = new ArrayList<Token>();
            this.identificadores = new ArrayList<String>();
            this.gestorErrores = gestorErr;
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
                        Error error = new Error();
                        error.setError("(Error Lexico) Caracter no esperado en la linea " + contadorLinea + ": " + caracter);
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
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
                    Error error = new Error();
                    error.setError("(Error Lexico) Caracter no esperado en la linea " + contadorLinea + ": " + caracter);
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
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
                token = new Token(ar.getCod(), "", lexema);
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 4:
                Asignacion asig = new Asignacion(lexema);
                token = new Token(asig.getCod(), "", lexema);
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                lexema = "";
                break;
            case 5:
                Relacion rel = new Relacion();
                token = new Token(rel.getCod(), "", lexema);
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 6:
                Logico log = new Logico();
                token = new Token(log.getCod(), "", lexema);
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 7:
                Simbolos sim = new Simbolos(caracter);
                token = new Token(sim.getCod(), "", lexema);
                tokensGenerados.add(token);
                estado = 0;
                leerCar();
                break;
            case 8:
                token = new Token(28, "", lexema);
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
                    Error error = new Error();
                    error.setError("(Error Lexico) Caracter no esperado en la linea " + contadorLinea + ": " + caracter);
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    leerCar();
                    estado = 0;
                }
                break;
            case 11:
                PalRes palres = new PalRes();
                if(palres.esPalRes(lexema)){
                    PalabrasReservadas palabra = new PalabrasReservadas(lexema);
                    token = new Token(palabra.getCod(), "", lexema);
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
                                token = new Token(id.getCod(), (i+1) + "", lexema);
                            }
                        }
                        if(anadir){
                            identificadores.add(lexema);
                            pos += 1;
                            token = new Token(id.getCod(), pos + "", lexema);
                        }      
                    }
                    else {
                        identificadores.add(lexema);
                        token = new Token(id.getCod(), pos + "", lexema);
                    }
                    tokensGenerados.add(token);
                    lexema = "";
                    estado = 0;
                }
                break;
            case 12:
                Entero num = new Entero();
                token = new Token(num.getCod(), lexema, lexema);
                tokensGenerados.add(token);
                estado = 0;
                lexema = "";
                break;
            case 13:
                Cadena cadena = new Cadena(lexema);
                token = new Token(cadena.getCod(), "\"" + cadena.getValue() + "\"", lexema);
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
    
    public ArrayList<Token> getTokens(){
        return this.tokensGenerados;
    }

    public GestorErrores getGestorErrores(){
        return this.gestorErrores;
    }
}