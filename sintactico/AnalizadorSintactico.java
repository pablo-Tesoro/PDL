package sintactico;

import lex.AnalizadorLexico;
import java.util.ArrayList;
import TokensTablas.Tokens.Token;

public class AnalizadorSintactico {
    
    private ArrayList<Integer> parse;
    private ArrayList<Token> tokens;
    private int cont;
    private Token token;

    public AnalizadorSintactico(AnalizadorLexico lexico){
        this.parse = new ArrayList<>();
        this.tokens = lexico.getTokens();
        this.cont = 0;
        this.token = getToken();
    }

    public void PP(){
        if (token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "if":
                    parse.add(1);
                    P();
                    return;
                case "let":
                    parse.add(1);
                    P();
                    return;
                case "id":
                    parse.add(1);
                    P();
                    return;
                case "print":
                    parse.add(1);
                    P();
                    return;
                case "input":
                    parse.add(1);
                    P();
                    return;
                case "return":
                    parse.add(1);
                    P();
                    return;
                case "switch":
                    parse.add(1);
                    P();
                    return;
                case "function":
                    parse.add(1);
                    P();
                    return;
                case "EOF":
                    parse.add(1);
                    return;
            }
        }
    }

    public void P(){
        if (token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "if":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "let":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "id":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "print":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "input":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "return":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "switch":
                    parse.add(2);
                    B();
                    P();
                    return;
                case "function":
                    parse.add(3);
                    F();
                    P();
                    return;
                case "EOF":
                    parse.add(4);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 116");
                    break;
            }
        }
    }

    public void B(){
        if (token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "if":
                    parse.add(5);
                    token = getToken();
                    if (token.getTipo().equals("(")){
                        token = getToken();
                    }
                    E();
                    if(token.getTipo() == ")"){
                        token = getToken();
                    }
                    S();
                    return;
                case "let":
                    parse.add(6);
                    token = getToken();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                        T();
                        token = getToken();
                        if(token.getTipo().equals(";")){
                            token = getToken();
                        }
                    }
                    return;
                case "id":
                    parse.add(7);
                    S();
                    return;
                case "print":
                    parse.add(7);
                    S();
                    return;
                case "input":
                    parse.add(7);
                    S();
                    return;
                case "return":
                    parse.add(7);
                    S();
                    return;
                case "switch":
                    parse.add(8);
                    token = getToken();
                    if(token.getTipo().equals("(")){
                        token = getToken();
                    }
                    E();
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    if(token.getTipo().equals("{")){
                        token = getToken();
                    }
                    CASE();
                    if(token.getTipo().equals("}")){
                        token = getToken();
                    }
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 187");
                    break;
            }
        }
    }

    public void CASE(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "case":
                    parse.add(42);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(":")){
                        token = getToken();
                    }
                    C();
                    CASE2();
                    return;
                case "}":
                    parse.add(43);
                    return;
                default:    
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 209");
                    break;
            }
        }
    }

    public void CASE2(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "break":
                    parse.add(44);
                    token = getToken();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    CASE();
                    return;
                case "case":
                    parse.add(45);
                    CASE();
                    return;
                case "}":
                    parse.add(46);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 234");
                    break;
            }
        }
    }

    public void T(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "boolean":
                    parse.add(9);
                    token = getToken();
                    return;
                case "int":
                    parse.add(10);
                    token = getToken();
                    return;
                case "string":
                    parse.add(11);
                    token = getToken();
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 263");
                    break;
            }
        }
    }

    public void S(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "id":
                    parse.add(12);
                    token = getToken();
                    SP();
                    return;
                case "print":
                    parse.add(13);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    return;
                case "input":
                    parse.add(14);
                    token = getToken();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                        if(token.getTipo().equals(";")){
                            token = getToken();
                        }
                    }
                    return;
                case "return":
                    parse.add(15);
                    token = getToken();
                    X();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 306");
                    break;
            }
        }
    }

    public void SP(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "=":
                    parse.add(16);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    return;
                case "%=":
                    parse.add(17);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    return;
                case "(":
                    parse.add(18);
                    token = getToken();
                    L();
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 338");
                    break;
            }
        }
    }

    public void X(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(19);
                    E();
                    return;
                case "id":
                    parse.add(19);
                    E();
                    return;
                case "(":
                    parse.add(19);
                    E();
                    return;
                case "entero":
                    parse.add(19);
                    E();
                    return;
                case "cadena":
                    parse.add(19);
                    E();
                    return;
                case ";":
                    parse.add(20);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 373");
                    break;
            }
        }
    }

    public void C(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "if":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "let":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "id":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "print":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "input":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "return":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "switch":
                    parse.add(21);
                    B();
                    C();
                    return;
                case "}":
                    parse.add(22);
                    return;
                case "break":
                    parse.add(22);
                    return;
                case "case":
                    parse.add(22);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 427");
                    break;
            }
        }
    }

    public void F(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "function":
                    parse.add(23);
                    token = getToken();
                    if(token.getTipo().equals("id")){
                        token = getToken(); 
                    }
                    H();
                    if(token.getTipo().equals("(")){
                        token = getToken();
                    }
                    A();
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    if(token.getTipo().equals("{")){
                        token = getToken();
                    }
                    C();
                    if(token.getTipo().equals("}")){
                        token = getToken();
                    }
                    return;
                default: 
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 474");
                    break;
            }
        }
    }

    public void H(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "boolean":
                    parse.add(24);
                    T();
                    return;
                case "int":
                    parse.add(24);
                    T();
                    return;
                case "string":
                    parse.add(24);
                    T();
                    return;
                case "(":
                    parse.add(25);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 498");
                    break;
            }
        }
    }

    public void A(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "boolean":
                    parse.add(26);
                    T();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                    }
                    K();
                    return;
                case "int":
                    parse.add(26);
                    T();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                    }
                    K();
                    return;
                case "string":
                    parse.add(26);
                    T();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                    }
                    K();
                    return;
                case ")":
                    parse.add(27);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 538");
                    break;
            }
        }
    }

    public void K(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case ",":
                    parse.add(28);
                    token = getToken();
                    T();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                    }
                    K();
                    return;
                case ")":
                    parse.add(29);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 563");
                    break;
            }
        }
    }

    public void L(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "id":
                    parse.add(30);
                    E();
                    Q();
                    return;
                case "(":
                    parse.add(30);
                    E();
                    Q();
                    return;
                case "entero":
                    parse.add(30);
                    E();
                    Q();
                    return;
                case "cadena":
                    parse.add(30);
                    E();
                    Q();
                    return;
                case ")":
                    parse.add(31);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 599");
                    break;
            }
        }
    }

    public void Q(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case ",":
                    parse.add(32);
                    token = getToken();
                    E();
                    Q();
                    return;
                case ")":
                    parse.add(33);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 621");
                    break;
            }
        }
    }

    public void E(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(34);
                    R();
                    EP();
                    return;
                case "id":
                    parse.add(34);
                    R();
                    EP();
                    return;
                case "(":
                    parse.add(34);
                    R();
                    EP();
                    return;
                case "entero":
                    parse.add(34);
                    R();
                    EP();
                    return;
                case "cadena":
                    parse.add(34);
                    R();
                    EP();
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 659");
                    break;
            }

        }
    }

    public void EP(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case ">":
                    parse.add(35);
                    token = getToken();
                    R();
                    EP();
                    return;
                case ",":
                    parse.add(36);
                    return;
                case ";":
                    parse.add(36);
                    return;
                case ")":
                    parse.add(36);
                    return;
                case ":":
                    parse.add(36);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 663");
                    break;
            }
        }
    }

    public void R(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(37);
                    U();
                    RP();
                    return;
                case "id":
                    parse.add(37);
                    U();
                    RP();
                    return;
                case "(":
                    parse.add(37);
                    U();
                    RP();
                    return;
                case "entero":
                    parse.add(37);
                    U();
                    RP();
                    return;
                case "cadena":
                    parse.add(37);
                    U();
                    RP();
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 729");
                    break;
            }
        }
    }

    public void RP(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "+":
                    parse.add(38);
                    token = getToken();
                    U();
                    RP();
                    return;
                case ",":
                    parse.add(39);
                    return;
                case ";":
                    parse.add(39);
                    return;
                case ")":
                    parse.add(39);
                    return;
                case ":":
                    parse.add(39);
                    return;
                case ">":
                    parse.add(39);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 751");
                    break;
            }
        }
    }

    public void U(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(40);
                    token = getToken();
                    V();
                    return;
                case "id":
                    parse.add(41);
                    V();
                    return;
                case "(":
                    parse.add(41);
                    V();
                    return;
                case "entero":
                    parse.add(41);
                    V();
                    return;
                case "cadena":
                    parse.add(41);
                    V();
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 797");
                    break;
            }
        }
    }

    public void V(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "id":
                    parse.add(47);
                    token = getToken();
                    VP();
                    return;
                case "(":
                    parse.add(48);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    return;
                case "entero":
                    parse.add(49);
                    token = getToken();
                    return;
                case "cadena":
                    parse.add(50);
                    token = getToken();
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 831");
                    break;
            }
        }
    }

    public void VP(){
        if(token == null){
            return;
        }
        else{
            switch(token.getTipo()){
                case "(":
                    parse.add(51);
                    token = getToken();
                    L();
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    return;
                case "+":
                    parse.add(52);
                    return;
                case ">":
                    parse.add(52);
                    return;
                case ")":
                    parse.add(52);
                    return;
                case ";":
                    parse.add(52);
                    return;
                case ":":
                    parse.add(52);
                    return;
                case ",":
                    parse.add(52);
                    return;
                default:
                    System.out.println("Error, tipo de token no permitido: " + token.getTipo() + " linea 870");
                    break;
            }
        }
    }

    public ArrayList<Integer> getParse(){
        return this.parse;
    }

    public void generarParse(){
        PP();
    }

    public Token getToken(){
        Token tok;
        if(cont < tokens.size()){
            tok = tokens.get(cont++);
        }
        else{
            tok = null;
        }
        return tok;
    }
}
