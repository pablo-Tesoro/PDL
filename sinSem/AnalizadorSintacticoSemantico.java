package sinSem;

import lex.AnalizadorLexico;
import java.util.ArrayList;
import java.io.*;
import Gestor.GestorErrores;
import Gestor.Error;
import Gestor.GestorTS;
import Gestor.elemTS;
import Tokens.Token;

public class AnalizadorSintacticoSemantico {
    
    private ArrayList<Integer> parse;
    private ArrayList<Token> tokens;
    private int cont;
    private Token token;
    private boolean mayor;
    private boolean yaDevuelto;
    private ArrayList<elemTS> tablaGlobal;
    private ArrayList<elemTS> tablaLocal;
    private ArrayList<ArrayList<elemTS>> tablas;
    private ArrayList<String> VaArgs;
    private ArrayList<String> LArgs;
    private ArrayList<String> lexFunciones;
    private boolean cond;
    private boolean hayError;
    private boolean esCase;
    private boolean varGlobal;
    private ArrayList<elemTS> elemGlobal;
    private GestorTS gestorTS;
    private GestorErrores gestorErrores;
    private elemTS funcion;
    private int idFunc;
    private boolean llamadaFunc;
    private elemTS T, SP, X, H, E, R, RP, V, U;

    public AnalizadorSintacticoSemantico(AnalizadorLexico lexico, GestorErrores gestorErrores){
        this.parse = new ArrayList<>();
        this.tokens = lexico.getTokens();
        this.cont = 0;
        this.mayor = false;
        this.hayError = false;
        this.token = getToken();
        this.yaDevuelto = false;
        this.idFunc = 1;
        this.cond = false;
        this.varGlobal = false;
        this.esCase = false;
        this.elemGlobal = new ArrayList<elemTS>();
        this.llamadaFunc = false;
        this.lexFunciones = new ArrayList<String>();
        this.tablaGlobal = new ArrayList<elemTS>();
        this.tablaLocal = new ArrayList<elemTS>();
        this.tablas = new ArrayList<ArrayList<elemTS>>();
        this.gestorTS = new GestorTS();
        this.gestorErrores = gestorErrores;
        this.LArgs = new ArrayList<String>();
        this.VaArgs = new ArrayList<String>();
        T = SP = X = H = E = R = RP = V = U = new elemTS();
        tablas.add(0, tablaGlobal);
        PP();
        tablas.set(0, tablaGlobal);
        
    }

    public void PP(){
        if (token == null){
            return;
        }
        else if(hayError == true){
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
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void P(){
        if (token == null){
            return;
        }
        else if(hayError == true){
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
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void B(){
        if (token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "if":
                    parse.add(5);
                    token = getToken();
                    if (token.getTipo().equals("(")){
                        token = getToken();
                        E();
                        if(!E.getTipo().equals("boolean")){
                            Error error = new Error();
                            error.setError("(Error Semantico) La expresion de dentro del if no es correcta");
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                        }
                        if(token.getTipo() == ")"){
                            token = getToken();
                            S();
                        }
                        else{
                            Error error = new Error();
                            error.setError("(Error Sintactico) Caracter esperado: ) Caracter recibido: " + token.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            hayError = true;
                            return;
                        }
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ( Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "let":
                    parse.add(6);
                    token = getToken();
                    String lex = token.getLexema();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                        T();
                        if(funcion == null){
                            if(gestorTS.buscarTS(tablaGlobal, lex) != null){
                                Error error = new Error();
                                error.setError("(Error Semantico) La variable '" + lex + "' ya esta declarada");
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                            else{
                                elemTS elem = new elemTS();
                                elem.setLexema(lex);
                                elem.setDesp(gestorTS.calcDesp(T.getTipo()));
                                elem.setTipo(T.getTipo());
                                tablaGlobal.add(elem);
                            }
                        }
                        else{
                            if(gestorTS.buscarTS(tablaLocal,lex) != null && gestorTS.buscarTS(tablaGlobal,lex) != null){
                                Error error = new Error();
                                error.setError("(Error Semantico) La variable '" + lex + "' ya esta declarada");
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                            else{
                                elemTS elem = new elemTS();
                                elem.setLexema(lex);
                                elem.setDesp(gestorTS.calcDesp(T.getTipo()));
                                elem.setTipo(T.getTipo());
                                tablaLocal.add(elem);
                            }
                        }
                        if(token.getTipo().equals(";")){
                            token = getToken();
                        }
                        else{
                            Error error = new Error();
                            error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            hayError = true;
                            return;
                        }
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
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
                        E();
                        if(!E.getTipo().equals("entero")){
                            Error error = new Error();
                            error.setError("(Error Semantico) La expresion del switch deberia ser de tipo entero, pero es de tipo " + E.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                        }
                        if(token.getTipo().equals(")")){
                            token = getToken();
                            if(token.getTipo().equals("{")){
                                token = getToken();
                                CASE();
                                if(token.getTipo().equals("}")){
                                    token = getToken();
                                }
                                else{
                                    Error error = new Error();
                                    error.setError("(Error Sintactico) Caracter esperado: } Caracter recibido: " + token.getTipo());
                                    gestorErrores.incrErrores();
                                    gestorErrores.add(error);
                                    hayError = true;
                                    return;
                                }
                            }
                            else{
                                Error error = new Error();
                                error.setError("(Error Sintactico) Caracter esperado: { Caracter recibido: " + token.getTipo());
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                                hayError = true;
                                return;
                            }
                        }
                        else{
                            Error error = new Error();
                            error.setError("(Error Sintactico) Caracter esperado: ) Caracter recibido: " + token.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            hayError = true;
                            return;
                        }
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ( Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void CASE(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "case":
                    esCase = true;
                    parse.add(42);
                    token = getToken();
                    E();
                    if(!E.getTipo().equals("entero")){
                        Error error = new Error();
                        error.setError("(Error Semantico) La expresion del case deberia ser de tipo entero, pero es de tipo " + E.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    if(token.getTipo().equals(":")){
                        token = getToken();
                        C();
                        CASE();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: : Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "break":
                    parse.add(43);
                    token = getToken();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                        CASE();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "}":
                    parse.add(44);
                    return;
                default:    
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void T(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "boolean":
                    parse.add(9);
                    T.setTipo("boolean");
                    token = getToken();
                    return;
                case "int":
                    parse.add(10);
                    T.setTipo("entero");
                    token = getToken();
                    return;
                case "string":
                    parse.add(11);
                    T.setTipo("string");
                    token = getToken();
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void S(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "id":
                    parse.add(12);
                    elemTS elem = null;
                    String lex = token.getLexema();
                    if(funcion == null){
                        if((elem=gestorTS.buscarTS(tablaGlobal, token.getLexema())) == null){
                            elem = new elemTS();
                            elem.setTipo("entero");
                            elem.setDesp(gestorTS.calcDesp("entero"));
                            elem.setLexema(token.getLexema());
                            tablaGlobal.add(elem);
                        }
                    }
                    else{
                        if((elem=gestorTS.buscarTS(tablaGlobal, token.getLexema())) == null && (elem=gestorTS.buscarTS(tablaLocal, token.getLexema())) == null && !funcion.getLexema().equals(lex)){
                            if(elemGlobal.size() > 0){
                                boolean encontrado = false;
                                for (int i=0; i<elemGlobal.size(); i++){
                                    if(elemGlobal.get(i).getLexema().equals(token.getLexema())){
                                        encontrado = true;
                                    }
                                }
                                if(!encontrado){
                                    varGlobal = true;
                                    elemTS elemGlob = new elemTS();
                                    elemGlob = new elemTS();
                                    elemGlob.setTipo("entero");
                                    elemGlob.setDesp(gestorTS.calcDesp("entero"));
                                    elemGlob.setLexema(token.getLexema());
                                    elemGlobal.add(elemGlob);
                                    elem = elemGlob;
                                }
                            }
                            else{
                                varGlobal = true;
                                elemTS elemGlob = new elemTS();
                                elemGlob = new elemTS();
                                elemGlob.setTipo("entero");
                                elemGlob.setDesp(gestorTS.calcDesp("entero"));
                                elemGlob.setLexema(token.getLexema());
                                elemGlobal.add(elemGlob);
                                elem = elemGlob;
                            }
                        }
                        else if(elem == null && funcion.getLexema().equals(lex)){
                            elem = funcion;
                        }
                    }
                    token = getToken();
                    SP();
                    if(SP.getTipo() == null){
                        if((elem=gestorTS.buscarTS(tablaGlobal, lex)) == null){
                            if(funcion != null && (elem=funcion).getLexema().equals(lex)){
                                boolean aux = true;
                                if(elem.getNArgs() == LArgs.size()){
                                    for (int i=0; i<elem.getNArgs(); i++){
                                        if(!tablaLocal.get(i).getTipo().equals(LArgs.get(i))){
                                            aux = false;
                                        }
                                    }
                                    if(!aux){
                                        Error error = new Error();
                                        error.setError("(Error Semantico) No se ha llamado a la funcion " + lex + " con los argumentos correctos");
                                        gestorErrores.incrErrores();
                                        gestorErrores.add(error);
                                    }
                                }
                                else{
                                    Error error = new Error();
                                    error.setError("(Error Semantico) No se ha llamado a la funcion " + lex + " con los argumentos correctos");
                                    gestorErrores.incrErrores();
                                    gestorErrores.add(error);          
                                }
                            }
                            else{
                                Error error = new Error();
                                error.setError("(Error Semantico) La funcion " + lex + " no se ha declarado");
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                        }
                        else{
                            boolean aux = true;
                            if(elem.getNArgs() == LArgs.size()){
                                for (int i=0; i<elem.getNArgs(); i++){
                                    if(!tablas.get(elem.getID()).get(i).getTipo().equals(LArgs.get(i))){
                                        aux = false;
                                    }
                                }
                                if(!aux){
                                    Error error = new Error();
                                    error.setError("(Error Semantico) No se ha llamado a la funcion " + token.getLexema() + " con los argumentos correctos");
                                    gestorErrores.incrErrores();
                                    gestorErrores.add(error);
                                }
                            }
                        }
                    }
                    else{
                        System.out.println("1");
                        if(elem != null){
                            System.out.println(SP.getTipo());
                            if(!SP.getTipo().equals(elem.getTipo()) && SP.getTipo() != "function"){
                                Error error = new Error();
                                error.setError("(Error Semantico) No se puede asignar un valor de tipo " + SP.getTipo() + " a la variable '" + lex + "' de tipo " + elem.getTipo());
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                        }
                    }
                    LArgs.clear();
                    return;
                case "print":
                    parse.add(13);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "input":
                    parse.add(14);
                    token = getToken();
                    if(token.getTipo().equals("id")){
                        if(gestorTS.buscarTS(tablaGlobal, token.getLexema()) == null && gestorTS.buscarTS(tablaLocal, token.getLexema()) == null){
                            elem = new elemTS();
                            elem.setLexema(token.getLexema());
                            elem.setTipo("entero");
                            elem.setDesp(gestorTS.calcDesp("entero"));
                            if(funcion != null){
                                varGlobal = true;
                                elemGlobal.add(elem);
                            }
                            else{
                                tablaGlobal.add(elem);
                            }
                        }
                        else{
                            elem=gestorTS.buscarTS(tablaGlobal, token.getLexema());
                            if(elem == null){
                                elem = gestorTS.buscarTS(tablaLocal, token.getLexema());
                            }
                            if(elem.getTipo().equals("boolean")){
                                Error error = new Error();
                                error.setError("(Error Semantico) No se puede hacer un input de la variable " + elem.getLexema() + " de tipo " + elem.getTipo());
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                        }
                        token = getToken();
                        if(token.getTipo().equals(";")){
                            token = getToken();
                        }
                        else{
                            Error error = new Error();
                            error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            hayError = true;
                            return;
                        }
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "return":
                    parse.add(15);
                    yaDevuelto = true;
                    token = getToken();
                    X();
                    if(funcion != null){
                        if(!cond && !esCase){
                            funcion.setRetorno(true);
                        }
                        if(!X.getTipo().equals(funcion.getTipoRetorno())){
                            Error error = new Error();
                            error.setError("(Error Semantico) La funcion " + funcion.getLexema() + " no puede devolver un " + X.getTipo() + ", tiene que devolver un " + funcion.getTipoRetorno());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                        }
                    }
                    else if(!esCase){
                        Error error = new Error();
                        error.setError("(Error Semantico) El return se ha ejecutado mal");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    if(token.getTipo().equals(";")){
                        token = getToken();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    esCase = false;
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void SP(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
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
                        SP.setTipo(E.getTipo());
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "%=":
                    parse.add(17);
                    token = getToken();
                    E();
                    if(token.getTipo().equals(";")){
                        token = getToken();
                        SP.setTipo(E.getTipo());
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "(":
                    parse.add(18);
                    token = getToken();
                    L();
                    SP.setTipo(null);
                    if(token.getTipo().equals(")")){
                        token = getToken();
                        if(token.getTipo().equals(";")){
                            token = getToken();
                        }
                        else{
                            Error error = new Error();
                            error.setError("(Error Sintactico) Caracter esperado: ; Caracter recibido: " + token.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            hayError = true;
                            return;
                        }
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ) Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void X(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(19);
                    E();
                    X.setTipo(E.getTipo());
                    return;
                case "id":
                    parse.add(19);
                    E();
                    X.setTipo(E.getTipo());
                    return;
                case "(":
                    parse.add(19);
                    E();
                    X.setTipo(E.getTipo());
                    return;
                case "entero":
                    parse.add(19);
                    E();
                    X.setTipo(E.getTipo());
                    return;
                case "cadena":
                    parse.add(19);
                    E();
                    X.setTipo(E.getTipo());
                    return;
                case ";":
                    parse.add(20);
                    X.setTipo("vacio");
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void C(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "if":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    B();
                    C();
                    return;
                case "let":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    B();
                    C();
                    return;
                case "id":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    B();
                    C();
                    return;
                case "print":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    B();
                    C();
                    return;
                case "input":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    B();
                    C();
                    return;
                case "return":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
                    B();
                    C();
                    return;
                case "switch":
                    parse.add(21);
                    if(funcion != null && funcion.getRetorno()){
                        Error error = new Error();
                        error.setError("(Error Semantico) Lo que hay despues del return no se ejecutara");
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                    }
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
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void F(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "function":
                    parse.add(23);
                    token = getToken();
                    String lex = token.getLexema();
                    if(token.getTipo().equals("id")){
                        token = getToken();
                        H();
                        if((funcion=gestorTS.buscarTS(tablaGlobal, lex)) != null){
                            Error error = new Error();
                            error.setError("(Error Semantico) La funcion " + lex + " ya se ha declarado");
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            funcion = new elemTS();
                            funcion.setTipo("Null");
                            funcion.setTipoRetorno(H.getTipo());
                            funcion.setLexema(lex);
                            tablaLocal.clear();
                            funcion.setRetorno(false);
                        }
                        else{
                            funcion = new elemTS();
                            funcion.setTipo("function");
                            funcion.setTipoRetorno(H.getTipo());
                            funcion.setLexema(lex);
                            lexFunciones.add(lex);
                            funcion.setNArgs(0);
                            tablaLocal.clear();
                            funcion.setRetorno(false);
                        }       
                        if(token.getTipo().equals("(")){
                            token = getToken();
                            A();  
                            if(token.getTipo().equals(")")){
                                token = getToken();
                                if(token.getTipo().equals("{")){
                                    token = getToken();
                                    C();
                                    if(!funcion.getTipo().equals("Null")){
                                        ArrayList<elemTS> aux = new ArrayList<elemTS>(tablaLocal);
                                        tablas.add(idFunc, aux);
                                        funcion.setID(idFunc);
                                        idFunc += 1;
                                        tablaGlobal.add(funcion);
                                        if(varGlobal){
                                            for(int i=0; i<elemGlobal.size(); i++){
                                                tablaGlobal.add(elemGlobal.get(i));
                                            }
                                        }
                                        elemGlobal.clear();
                                        varGlobal = false;
                                    }
                                    if(token.getTipo().equals("}")){
                                        token = getToken();
                                        if(!funcion.getTipoRetorno().equals("vacio") && !yaDevuelto){
                                            Error error = new Error();
                                            error.setError("(Error Semantico) La funcion " + funcion.getLexema() + " no ha devuelto nada y deberia devolver un " + funcion.getTipoRetorno());
                                            gestorErrores.incrErrores();
                                            gestorErrores.add(error);
                                        }
                                        yaDevuelto = false;
                                        tablaLocal.clear();
                                        funcion = null;
                                    }
                                    else{
                                        Error error = new Error();
                                        error.setError("(Error Sintactico) Caracter esperado: } Caracter recibido: " + token.getTipo());
                                        gestorErrores.incrErrores();
                                        gestorErrores.add(error);
                                        hayError = true;
                                        return;
                                    }
                                }
                                else{
                                    Error error = new Error();
                                    error.setError("(Error Sintactico) Caracter esperado: { Caracter recibido: " + token.getTipo());
                                    gestorErrores.incrErrores();
                                    gestorErrores.add(error);
                                    hayError = true;
                                    return;
                                }
                            }
                            else{
                                Error error = new Error();
                                error.setError("(Error Sintactico) Caracter esperado: ) Caracter recibido: " + token.getTipo());
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                                hayError = true;
                                return;
                            }
                        }
                        else{
                            Error error = new Error();
                            error.setError("(Error Sintactico) Caracter esperado: ( Caracter recibido: " + token.getTipo());
                            gestorErrores.incrErrores();
                            gestorErrores.add(error);
                            hayError = true;
                            return;
                        }
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                default: 
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void H(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "boolean":
                    parse.add(24);
                    T();
                    H.setTipo("boolean");
                    return;
                case "int":
                    parse.add(24);
                    T();
                    H.setTipo("entero");
                    return;
                case "string":
                    parse.add(24);
                    T();
                    H.setTipo("string");
                    return;
                case "(":
                    parse.add(25);
                    H.setTipo("vacio");
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void A(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "boolean":
                    parse.add(26);
                    T();
                    if(token.getTipo().equals("id")){
                        elemTS elem = new elemTS();
                        elem.setLexema(token.getLexema());
                        elem.setDesp(gestorTS.calcDesp(T.getTipo()));
                        elem.setTipo(T.getTipo());
                        funcion.setNArgs(funcion.getNArgs() + 1);
                        tablaLocal.add(elem);
                        token = getToken();
                        K();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "int":
                    parse.add(26);
                    T();
                    if(token.getTipo().equals("id")){
                        elemTS elem = new elemTS();
                        elem.setLexema(token.getLexema());
                        elem.setDesp(gestorTS.calcDesp(T.getTipo()));
                        elem.setTipo(T.getTipo());
                        funcion.setNArgs(funcion.getNArgs() + 1);
                        tablaLocal.add(elem);
                        token = getToken();
                        K();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "string":
                    parse.add(26);
                    T();
                    if(token.getTipo().equals("id")){
                        elemTS elem = new elemTS();
                        elem.setLexema(token.getLexema());
                        elem.setDesp(gestorTS.calcDesp(T.getTipo()));
                        elem.setTipo(T.getTipo());
                        funcion.setNArgs(funcion.getNArgs() + 1);
                        tablaLocal.add(elem);
                        token = getToken();
                        K();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case ")":
                    parse.add(27);
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void K(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case ",":
                    parse.add(28);
                    token = getToken();
                    T();
                    if(token.getTipo().equals("id")){
                        elemTS elem = new elemTS();
                        elem.setLexema(token.getLexema());
                        elem.setDesp(gestorTS.calcDesp(T.getTipo()));
                        elem.setTipo(T.getTipo());
                        funcion.setNArgs(funcion.getNArgs() + 1);
                        tablaLocal.add(elem);
                        token = getToken();
                        K();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Se esperaba un id y se ha recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case ")":
                    parse.add(29);
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void L(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(30);
                    E();
                    LArgs.clear();
                    LArgs.add(E.getTipo());
                    Q();
                    return;
                case "id":
                    parse.add(30);
                    E();
                    LArgs.clear();
                    LArgs.add(E.getTipo());
                    Q();
                    return;
                case "(":
                    parse.add(30);
                    E();
                    LArgs.clear();
                    LArgs.add(E.getTipo());
                    Q();
                    return;
                case "entero":
                    parse.add(30);
                    E();
                    LArgs.clear();
                    LArgs.add(E.getTipo());
                    Q();
                    return;
                case "cadena":
                    parse.add(30);
                    E();
                    LArgs.clear();
                    LArgs.add(E.getTipo());
                    Q();
                    return;
                case ")":
                    parse.add(31);
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void Q(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case ",":
                    parse.add(32);
                    token = getToken();
                    E();
                    LArgs.add(E.getTipo());
                    Q();
                    return;
                case ")":
                    parse.add(33);
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void E(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(34);
                    R();
                    E.setTipo(U.getTipo());
                    EP();
                    if(mayor){
                        E.setTipo("boolean");
                    }
                    mayor = false;
                    return;
                case "id":
                    parse.add(34);
                    R();
                    E.setTipo(U.getTipo());
                    EP();
                    if(mayor){
                        E.setTipo("boolean");
                    }
                    mayor = false;
                    return;
                case "(":
                    parse.add(34);
                    R();
                    E.setTipo(U.getTipo());
                    EP();
                    if(mayor){
                        E.setTipo("boolean");
                    }
                    mayor = false;
                    return;
                case "entero":
                    parse.add(34);
                    R();
                    E.setTipo(U.getTipo());
                    EP();
                    if(mayor){
                        E.setTipo("boolean");
                    }
                    mayor = false;
                    return;
                case "cadena":
                    parse.add(34);
                    R();
                    E.setTipo(U.getTipo());
                    EP();
                    if(mayor){
                        E.setTipo("boolean");
                    }
                    mayor = false;
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void EP(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case ">":
                    parse.add(35);
                    token = getToken();
                    R();
                    mayor = true;
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
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void R(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(37);
                    U();
                    R.setTipo(U.getTipo());
                    RP();
                    return;
                case "id":
                    parse.add(37);
                    U();
                    R.setTipo(U.getTipo());
                    RP();
                    return;
                case "(":
                    parse.add(37);
                    U();
                    R.setTipo(U.getTipo());
                    RP();
                    return;
                case "entero":
                    parse.add(37);
                    U();
                    R.setTipo(U.getTipo());
                    RP();
                    return;
                case "cadena":
                    parse.add(37);
                    U();
                    R.setTipo(U.getTipo());
                    RP();
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void RP(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "+":
                    parse.add(38);
                    token = getToken();
                    U();
                    RP.setTipo("entero");
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
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void U(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "!":
                    parse.add(40);
                    token = getToken();
                    V();
                    U.setTipo(V.getTipo());
                    return;
                case "id":
                    parse.add(41);
                    V();
                    U.setTipo(V.getTipo());
                    return;
                case "(":
                    parse.add(41);
                    V();
                    U.setTipo(V.getTipo());
                    return;
                case "entero":
                    parse.add(41);
                    V();
                    U.setTipo(V.getTipo());
                    return;
                case "cadena":
                    parse.add(41);
                    V();
                    U.setTipo(V.getTipo());
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void V(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "id":
                    parse.add(45);
                    String lex = token.getLexema();
                    token = getToken();
                    VP();
                    elemTS elem;
                    if(llamadaFunc){
                        boolean aux = true;
                        ArrayList<elemTS> aux2;
                        if((elem=gestorTS.buscarTS(tablaGlobal, lex)) == null){   
                            if(funcion != null && (elem=funcion).getLexema().equals(lex)){
                                V.setTipo(elem.getTipoRetorno());
                                aux2 = new ArrayList<elemTS>(tablaLocal);
                                if (VaArgs.size() == elem.getNArgs()){
                                    for(int i=0; i<VaArgs.size(); i++){
                                        if(!VaArgs.get(i).equals(aux2.get(i).getTipo())){
                                            aux = false;
                                        }
                                    }
                                    if (!aux){
                                        Error error = new Error();
                                        error.setError("(Error Semantico) No se ha llamado a la funcion " + lex + " con los argumentos correctos");
                                        gestorErrores.incrErrores();
                                        gestorErrores.add(error);
                                    }
                                }
                                else{
                                    Error error = new Error();
                                    error.setError("(Error Semantico) No se ha llamado a la funcion " + lex + " con los argumentos correctos");
                                    gestorErrores.incrErrores();
                                    gestorErrores.add(error);
                                }
                                aux2.clear();
                            }
                            else{
                                Error error = new Error();
                                error.setError("(Error Semantico) La funcion " + lex + " no se ha declarado");
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                        }
                        else{
                            V.setTipo(elem.getTipoRetorno());
                            aux2 = new ArrayList<elemTS>(tablas.get(elem.getID()));
                            if(VaArgs.size() == elem.getNArgs()){
                                for(int i=0; i<VaArgs.size(); i++){
                                    if(!VaArgs.get(i).equals(aux2.get(i).getTipo())){
                                        aux = false;
                                    }
                                }
                                if(!aux){
                                    Error error = new Error();
                                    error.setError("(Error Semantico) No se ha llamado a la funcion " + lex + " con los argumentos correctos");
                                    gestorErrores.incrErrores();
                                    gestorErrores.add(error);
                                }
                            }
                            else{
                                Error error = new Error();
                                error.setError("(Error Semantico) No se ha llamado a la funcion " + lex + " con los argumentos correctos");
                                gestorErrores.incrErrores();
                                gestorErrores.add(error);
                            }
                            aux2.clear();
                        }
                        llamadaFunc = false;
                    }
                    else{
                        if(funcion == null){
                            if((elem=gestorTS.buscarTS(tablaGlobal, lex)) != null){
                                V.setTipo(elem.getTipo());
                            }
                            else{
                                elemTS elemGlob = new elemTS();
                                elemGlob.setLexema(lex);
                                elemGlob.setDesp(gestorTS.calcDesp("entero"));
                                elemGlob.setTipo("entero");
                                tablaGlobal.add(elemGlob);
                            }
                        }
                        else{
                            if((elem=gestorTS.buscarTS(tablaLocal, lex)) != null || (elem=gestorTS.buscarTS(tablaGlobal, lex)) != null){
                                V.setTipo(elem.getTipo());
                            }
                            else{
                                if(elemGlobal.size() > 0){
                                    boolean encontrado = false;
                                    for (int i=0; i<elemGlobal.size(); i++){
                                        if(elemGlobal.get(i).getLexema().equals(lex)){
                                            encontrado = true;
                                        }
                                    }
                                    if(!encontrado){
                                        varGlobal = true;
                                        elemTS elemGlob = new elemTS();
                                        elemGlob = new elemTS();
                                        elemGlob.setTipo("entero");
                                        elemGlob.setDesp(gestorTS.calcDesp("entero"));
                                        elemGlob.setLexema(lex);
                                        elemGlobal.add(elemGlob);
                                        elem = elemGlob;
                                    }
                                }
                                else{
                                    varGlobal = true;
                                    elemTS elemGlob = new elemTS();
                                    elemGlob = new elemTS();
                                    elemGlob.setTipo("entero");
                                    elemGlob.setDesp(gestorTS.calcDesp("entero"));
                                    elemGlob.setLexema(lex);
                                    elemGlobal.add(elemGlob);
                                    elem = elemGlob;
                                }
                                V.setTipo("entero");
                            }
                        }
                    }
                    return;
                case "(":
                    parse.add(46);
                    token = getToken();
                    E();
                    V.setTipo(E.getTipo());
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ) Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "entero":
                    parse.add(47);
                    V.setTipo("entero");
                    token = getToken();
                    return;
                case "cadena":
                    parse.add(48);
                    V.setTipo("string");
                    token = getToken();
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public void VP(){
        if(token == null){
            Error error = new Error();
            error.setError("(Error Sintactico) No existen mas tokens");
            gestorErrores.incrErrores();
            gestorErrores.add(error);
            hayError = true;
            return;
        }
        else if(hayError == true){
            return;
        }
        else{
            switch(token.getTipo()){
                case "(":
                    parse.add(49);
                    token = getToken();
                    L();
                    VaArgs = LArgs;
                    llamadaFunc = true;
                    if(token.getTipo().equals(")")){
                        token = getToken();
                    }
                    else{
                        Error error = new Error();
                        error.setError("(Error Sintactico) Caracter esperado: ) Caracter recibido: " + token.getTipo());
                        gestorErrores.incrErrores();
                        gestorErrores.add(error);
                        hayError = true;
                        return;
                    }
                    return;
                case "+":
                    parse.add(50);
                    llamadaFunc = false;
                    return;
                case ">":
                    parse.add(50);
                    llamadaFunc = false;
                    return;
                case ")":
                    parse.add(50);
                    llamadaFunc = false;
                    return;
                case ";":
                    parse.add(50);
                    llamadaFunc = false;
                    return;
                case ":":
                    parse.add(50);
                    llamadaFunc = false;
                    return;
                case ",":
                    parse.add(50);
                    llamadaFunc = false;
                    return;
                default:
                    Error error = new Error();
                    error.setError("(Error Sintactico) Token no permitido: " + token.getTipo());
                    gestorErrores.incrErrores();
                    gestorErrores.add(error);
                    hayError = true;
                    break;
            }
        }
    }

    public ArrayList<Integer> getParse(){
        return this.parse;
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

    public void genParse(){
        try{
            FileWriter fw;
            BufferedWriter bw;
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\parse.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            for(int i=0; i<parse.size(); i++) {
                String regla = parse.get(i).toString();
                if(i==0){
                    bw.write("Descendente " + regla);
                }
                else{
                    bw.write(" " + regla);
                }
                
            }
            System.out.println("Parse generado correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void generarTS(){
        try{
            FileWriter fw;
            BufferedWriter bw;
            String path = new File("").getAbsolutePath();
			path = path.concat("\\pruebas\\tablaSimbolos.txt");
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            int desp = 0;
            bw.write("TABLA PRINCIPAL #1:");
            bw.newLine();
            int aux = 1;
            for(int i=0; i<tablas.get(0).size(); i++) {
                if(tablas.get(0).get(i).getTipo().equals("function")){
                    int nArgs = tablas.get(0).get(i).getNArgs();
                    int cont = 1;
                    bw.write("* LEXEMA : '" + tablas.get(0).get(i).getLexema() + "'");
                    bw.newLine();
                    bw.write("  ATRIBUTOS:");
                    bw.newLine();
                    bw.write("    + tipo:                  'function'");
                    bw.newLine();
                    bw.write("      + numParam:          " + nArgs);
                    bw.newLine();
                    for (int j=0; j<nArgs; j++){
                        bw.write("      + TipoParam0" + cont + ":    '" + tablas.get(aux).get(j).getTipo() + "'");
                        bw.newLine();
                        cont+=1;
                    }
                    bw.write("    + TipoRetorno:      '" + tablas.get(0).get(i).getTipoRetorno() + "'");
                    bw.newLine();
                    bw.write("  + EtiqFuncion:       'funcion0" + aux + "'");
                    bw.newLine();
                    bw.write("  -------------- --------------");
                    bw.newLine();
                    aux += 1;
                }
                else{
                    bw.write("* LEXEMA : '" + tablas.get(0).get(i).getLexema() + "'");
                    bw.newLine();
                    bw.write("  ATRIBUTOS:");
                    bw.newLine();
                    bw.write("  + tipo:        '" + tablas.get(0).get(i).getTipo() + "'");
                    bw.newLine();
                    bw.write("  + despl:          " + desp);
                    bw.newLine();
                    bw.write("  -------------- --------------");
                    bw.newLine();
                    desp = desp + tablas.get(0).get(i).getDesp(); 
                }
                
            }
            int args;
            int contador;
            for(int k=0; k<lexFunciones.size(); k++){
                desp = 0;
                args = 0;
                contador = 0;
                bw.write("TABLA de la FUNCION " + lexFunciones.get(k) + " #" + (k+2) + ":");
                bw.newLine();
                while(contador < tablas.get(k+1).size()){
                    if(args != gestorTS.buscarTS(tablaGlobal, lexFunciones.get(k)).getNArgs()){
                        bw.write("* LEXEMA : '" + tablas.get(k+1).get(contador).getLexema() + "' (parametro)");
                        bw.newLine();
                        args += 1;
                    }
                    else{
                        bw.write("* LEXEMA : '" + tablas.get(k+1).get(contador).getLexema() + "'");
                        bw.newLine();
                    }
                    bw.write("  + tipo:       '" + tablas.get(k+1).get(contador).getTipo() + "'");
                    bw.newLine();
                    bw.write("  + despl:          " + desp);
                    bw.newLine();
                    bw.write("  -------------- --------------");
                    bw.newLine();
                    desp = desp + tablas.get(k+1).get(contador).getDesp();
                    contador += 1;
                }
            }
            System.out.println("Tabla de simbolos generada correctamente");
            bw.close();
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
    }

    public GestorErrores getGestorErrores(){
        return this.gestorErrores;
    }
}