package Gestor;

import java.util.ArrayList;

public class elemTS {
    private String lexema;
	private String tipo;
	private int desplazamiento;
	private int nArgs;
	private ArrayList<String> tipoArgs;
	private String tipoRetorno;
    private int id;
    private boolean retorno;

    public elemTS(){
        
    }
    public String getLexema(){
        return this.lexema;
    }
    public String getTipo(){
        return this.tipo;
    }
    public int getDesp(){
        return this.desplazamiento;
    }
    public int getNArgs(){
        return this.nArgs;
    }
    public ArrayList<String> getTipoArgs(){
        return this.tipoArgs;
    }
    public String getTipoRetorno(){
        return this.tipoRetorno;
    }
    public int getID(){
        return this.id;
    }
    public boolean getRetorno(){
        return this.retorno;
    }
    public void setLexema(String lex){
        this.lexema = lex;
    }
    public void setTipo(String tip){
        this.tipo = tip;
    }
    public void setDesp(int desp){
        this.desplazamiento = desp;
    }
    public void setNArgs(int args){
        this.nArgs = args;
    }
    public void setTipoArgs(ArrayList<String> tipos){
        this.tipoArgs = tipos;
    }
    public void setTipoRetorno(String ret){
        this.tipoRetorno = ret;
    }
    public void setID(int i){
        this.id = i;
    }
    public void setRetorno(boolean ret){
        this.retorno = ret;
    }
}
