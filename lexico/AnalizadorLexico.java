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

    public AnalizadorLexico() {
        try{
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\prueba.txt");
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
                System.out.println(caracter);
            }
            else {
                this.caracter = Character.toString((char) pos);
                System.out.println("Caracter: " + caracter);
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
                System.out.println("Entro al estado 0");
                if(car.esDelimitador(caracter) || caracter.equals("\r")){
                    System.out.println("Es delimitador");
                    System.out.println(fin);
                    leerCar();
                }
                else if(car.esLetra(caracter) || caracter.equals("_")){
                    System.out.println("Es letra");
                    estado = 1;
                    lexema += caracter;
                    System.out.println(lexema);
                }
                else if (car.esDigito(caracter)){
                    System.out.println("Es digito");
                    estado = 2;
                    lexema += caracter;
                    System.out.println(estado);
                    System.out.println(fin);
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
                    System.out.println("Entro al caso de =");
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
                    System.out.println("Fichero leido");
                    token = new Token(30, "");
                    tokensGenerados.add(token);                
                    fin = true;
                }
                else if(caracter.equals("'")){
                    System.out.println("Es cadena");
                    leerCar();
                    while(!caracter.equals("'")){
                        System.out.println("While de cadena");
                        lexema += caracter;
                        leerCar();   
                    }
                    estado = 22;
                }
                else if(caracter.equals("/")){
                    leerCar();
                    if(caracter.equals("/")){
                        System.out.println("Es comentario");
                        estado = 18;
                    }
                    else{
                        System.out.println("Token no esperado");
                    }
                }
                else{
                    System.out.println(caracter);
                    System.out.println("Error afbrsfdbrsgzhbsrgvbagfvabethgf");
                    leerCar();
                }
                break;
            case 1:
                System.out.println("Entro al estado 1");
                leerCar();
                if(car.esDigito(caracter) || car.esLetra(caracter) || caracter.equals("_")){
                    lexema += caracter;
                }
                else {
                    estado = 20;
                }
                break;
            case 2:
                System.out.println("Entro al estado 2");
                leerCar();
                if(car.esDigito(caracter)){
                    lexema += caracter;
                }
                else {
                    estado = 21;
                }
                break;
            case 5: 
                System.out.println("Entro al estado 5");
                Aritmetico ar = new Aritmetico(caracter);
                token = new Token(ar.getCod(), "");
                tokensGenerados.add(token);
                System.out.println("Token aritmetico generado");
                estado = 0;
                leerCar();
                break;
            case 8:
                Asignacion asig = new Asignacion(lexema);
                token = new Token(asig.getCod(), "");
                tokensGenerados.add(token);
                System.out.println("Genero el token asignacion");
                estado = 0;
                leerCar();
                lexema = "";
                break;
            case 11:
                Relacion rel = new Relacion(caracter);
                token = new Token(rel.getCod(), "");
                tokensGenerados.add(token);
                System.out.println("Genero el token relacion");
                estado = 0;
                leerCar();
                break;
            case 15:
                Logico log = new Logico();
                token = new Token(log.getCod(), "");
                tokensGenerados.add(token);
                System.out.println("Genero el token !");
                estado = 0;
                leerCar();
                break;
            case 16:
                Simbolos sim = new Simbolos(caracter);
                token = new Token(sim.getCod(), "");
                tokensGenerados.add(token);
                System.out.println("Genero token simbolo");
                estado = 0;
                leerCar();
                break;
            case 18:
                System.out.println("Entro al estado 18");
                while(!caracter.equals("\r") && !caracter.equals("EOF")){
                    leerCar();
                }
                System.out.println("Termina el comentario");
                leerCar();
                estado = 0;
                break;
            case 20:
                System.out.println("Entro al estado 20");
                PalRes palres = new PalRes();
                System.out.println("Lexema: " + lexema);
                if(palres.esPalRes(lexema)){
                    System.out.println("Es palabra reservada");
                    PalabrasReservadas palabra = new PalabrasReservadas(lexema);
                    token = new Token(palabra.getCod(), "");
                    tokensGenerados.add(token);
                    System.out.println("Genero el token " + lexema);
                    lexema = "";
                    estado = 0;
                }
                else{
                    Identificador id = new Identificador();
                    Boolean anadir = true;
                    if(identificadores.size()>0){
                        for(int i=0; i<identificadores.size(); i++){
                            if(identificadores.get(i).equals(lexema)){
                                System.out.println("lexema repetido");
                                anadir = false;
                                token = new Token(id.getCod(), (i+1) + "");
                            }
                        }
                        if(anadir){
                            System.out.println("Añado");
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
                    System.out.println("Token identificador generado");
                    lexema = "";
                    estado = 0;
                }
                break;
            case 21:
                System.out.println("Entro al estado 21");
                Entero num = new Entero();
                token = new Token(num.getCod(), lexema);
                tokensGenerados.add(token);
                System.out.println("Genero el token entero");
                estado = 0;
                lexema = "";
                break;
            case 22:
                System.out.println("Entro al estado 22");
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
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}