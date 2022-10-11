package lexico;

import java.util.ArrayList;
import java.io.*;
import TokensTablas.*;
import TokensTablas.Tokens.Asignacion;
import TokensTablas.Tokens.Token;
import java.io.PrintWriter;

public class AnalizadorLexico {
    private ArrayList<String> palReservadas = new ArrayList<String>();
    private String[] palabras = {"boolean", "break", "case", "function", "if", "input", "int", "let", "print", "return", "string", "switch"};
    public FileReader fr;
    public FileWriter fw;
    public PrintWriter pw;
    private String caracter;
    private String lexema = "";
    private ArrayList<Token> tokensGenerados;
    private PrintWriter log;

    public AnalizadorLexico() {
        for(int i=0; i < palabras.length; i++){
            palReservadas.add(i, palabras[i]);
        }
        try{
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\prueba.txt");
			this.fr = new FileReader(path);
            this.tokensGenerados = new ArrayList<Token>();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        try {
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\errores.txt");
			this.log = new PrintWriter(path,"UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void leerCar() {
        try{
            int pos = fr.read();
            this.caracter = String.valueOf((char) pos);
            System.out.println("Caracter leido: " + caracter);
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void generarToken() {
        Token token = null;
        int estado = 0;
        boolean fin = false;
        leerCar();
        while (!fin){
            switch(estado){
                case 0: 
                    if(Character.isLetter(caracter.charAt(0))){
                        estado = 1;
                        lexema += caracter;
                    }
                    else if (Character.isDigit(caracter.charAt(0))){
                        estado = 2;
                        lexema += caracter;
                    }
                    else if(caracter.equals("+")){
                        estado = 3;
                        lexema += caracter;
                    }
                    else if(caracter.equals("-")){
                        estado = 5;
                        lexema += caracter;
                    }
                    else if(caracter.equals("%")){
                        Asignacion asig = new Asignacion(caracter);
                        token = new Token(asig.getCod(), "");
                        tokensGenerados.add(token);
                        
                    }
                    else if(caracter.equals("=")){
                        Asignacion asig = new Asignacion(caracter);
                        token = new Token(asig.getCod(), "");
                        tokensGenerados.add(0, token);
                        fin = true;
                    }
                    else{
                        token = null;
                        fin = true;
                    }
                    fin = true;
                    break;
            }
        }
    }

    public void escribirTokens(){
        try{
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\tokens.txt");
            fw = new FileWriter(path);
            pw = new PrintWriter(fw);
            String token = tokensGenerados.get(0).toString();
            pw.println("Token 1: " + token);
            pw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    
    public String getToken() {
        return tokensGenerados.get(0).toString();
    }
}
