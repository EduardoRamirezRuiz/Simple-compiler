package tokenslexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class manageTokens
{   
    public FileReader archivo = null;
    private BufferedReader buffereader;
    private String linea, errores=""; 
    private String []linea2;
    private final ArrayList<MyObject> tablaSimbolos;
    public manageFile mf;
    
    String [] letras={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",};
    
    public manageTokens(){
        this.tablaSimbolos = new ArrayList<>();
        divideToken();
        identifiedType();
        asignar();
        direccion();
        //new InterfaceLexico(tablaSimbolos, errores);
    }
    
    public ArrayList<MyObject> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public String getErrores() {
        if(errores.equalsIgnoreCase(""))
            errores = "Lexicamente correcto";
        return errores;
    }
    
    private void divideToken(){
        int ctnLinea=1;
        int i;
        String token = "";
        try{
            archivo = new FileReader(new File("src/code.dat"));
            buffereader = new BufferedReader(archivo);
            
            linea = buffereader.readLine();
            while(linea!=null) {
                for(i = 0; i < linea.length(); i++){
                    int ascii = (int) linea.charAt(i);
                    //if(ascii==45)
                      //  System.err.println(ascii+"    sig: "+linea.charAt(i+1));
                    if(ascii!=32 && ascii!=61 && ascii!=123 && ascii!=125 
                            && ascii!=40 && ascii!=41 && ascii!=42 && ascii!=43 
                            && ascii!=45 && ascii!=47 && ascii!=59 && ascii!=60 
                            && ascii!=62 && ascii!=46)    
    //32 espacio, 61 =,123 {, 125 }, 40 (, 41 ), 42 *, 43 +, 45 -, 47 /, 48 al 57 digitos [0-9], 59 ;, 60 <, 62 >, " 34, . 46
                    {
                        if(token.contains("\"") && (int)linea.charAt(i+1)==32){
                            token +=linea.charAt(i)+" ";
                            i+=1;
                        }
                        else
                            token += linea.charAt(i);  //si no es ningún caracter signo, agrega el caracter al token
                    }   
                    else{
                        if(!token.equalsIgnoreCase("")){
                            //entra cadena
                            tablaSimbolos.add(new MyObject(token, null, "", null,0,ctnLinea));
                            token = "";
                        }
                        
                        if( ascii==61 && (int)linea.charAt(i+1)==61) //caso de ==
                        {
                            token +=  linea.charAt(i)+""+linea.charAt(i+1);
                            tablaSimbolos.add(new MyObject(token, null, "", null,0,ctnLinea));
                            token = "";
                            i+=1;
                        }
                        else{
                            if( (ascii==60 || ascii==62) && (int)linea.charAt(i+1)==61) //caso de <= or >=
                            {
                                token +=  linea.charAt(i)+""+linea.charAt(i+1);
                                tablaSimbolos.add(new MyObject(token, null, "",null,0,ctnLinea));
                                
                                token = "";
                                i+=1;
                            }
                            else{
                                if(ascii==60 || ascii==62 || ascii==61 || ascii==123 || ascii==125 
                                    || ascii==28 || ascii==29 || ascii==42 || ascii==43 || ascii==45 
                                        || ascii==47 || ascii==59 || ascii==40 || ascii==41 || ascii==46) 
                                        //caso de >, <,{,},(,),+,-,*,/,",;,(,)
                                {
                                    try{
                                        if(ascii==45 && (linea.charAt(i+1) > 47 && linea.charAt(i+1) < 58)
                                            && (((int)(linea.charAt(i-1)))==40 || ((int)(linea.charAt(i-2)))==40 )){ //es un número negativo
                                            //System.err.println("****** "+ascii+"    sig: "+linea.charAt(i+1));
                                            token +=  linea.charAt(i) +""+ linea.charAt(i+1);
                                            i++;
                                        }
                                        else{
                                            token +=  linea.charAt(i);
                                            tablaSimbolos.add(new MyObject(token, null, "", null,0,ctnLinea));
                                            token = "";
                                        }
                                    }
                                    catch(Exception ex){
                                        System.out.println("Error in last case");
                                    }
                                }
                            }
                        }
                    }
                }
                if(!token.equalsIgnoreCase("")){
                    tablaSimbolos.add(new MyObject(token, null, "", null,0,ctnLinea));
                    token="";
                }
                linea = buffereader.readLine();
                ctnLinea++;
            }
        }
        catch(Exception ex){
            System.out.println("Exception: Error mientras se leía los caracteres " +ex);
        }
    }
    
    private ArrayList repetido(String v){
        ArrayList<Integer> posiciones = new ArrayList<Integer>();
        for(int j=0;j<tablaSimbolos.size();j++){
            if(v.equals(tablaSimbolos.get(j).lexema)) 
                posiciones.add(j);
        }
        return posiciones;
    }
    
    private void identifiedType(){
        
        int search;
        
        automaton_lex aux = new automaton_lex();
        for(MyObject objeto: tablaSimbolos){
            
            search = aux.isBegin(""+objeto.lexema);   //palabra de inicio
            if(search != -1){
                objeto.id= search;
                objeto.typeLexema="rw";
            }
            else{
                search = aux.isReservedWord(""+objeto.lexema);   //palabra reservada
                if(search != -1){
                        objeto.id= search;
                        objeto.typeLexema="rw";
                }
                else{
                    search = aux.isArithmetic(""+objeto.lexema); //operadores aritméticos
                    if(search != -1){
                        objeto.id= search;
                        objeto.typeLexema="op";
                    }
                    else{
                        search = aux.isComparative(""+objeto.lexema); //comparativos
                        if(search != -1){
                            objeto.id= search;
                            if(search==112) //si es el signo igual
                                objeto.typeLexema="eq";
                            else
                                objeto.typeLexema="cp";
                        }
                        else{
                            search = aux.isNumber(""+objeto.lexema); //números
                            if(search != -1){
                                    objeto.id= search;
                                    objeto.typeLexema="nm";
                            }
                            else{
                                search = aux.isSign(""+objeto.lexema); //signos y cadenas
                                if(search != -1){
                                    objeto.id= search;
                                    if(search==57) //si es una cadena
                                        objeto.typeLexema="sc";
                                    else 
                                        if(search == 62)
                                            objeto.typeLexema="sd";
                                        else
                                            objeto.typeLexema="sg";
                                }
                                else{
                                    search = aux.isIdentifier(""+objeto.lexema); //identificador
                                    if(search != -1){
                                        if(search != -2){
                                            objeto.id= search;
                                            objeto.typeLexema="id";
                                        }
                                        else{
                                            objeto.id= 1000;
                                            objeto.typeLexema="uk";
                                            errores+="Error 120: Posible identificador declarado "
                                            + "o valor numérico incorrectamente, "
                                                    + "línea "+objeto.linea+"\n";
                                        }
                                    }
                                    else{
                                        objeto.typeLexema="1000";
                                        objeto.typeLexema="uk";
                                        errores+="Error 130: Lexema imposible de clasificar, "
                                                + "línea "+objeto.linea+"\n";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void asignar(){
        for(int i=0;i<tablaSimbolos.size();i++){
            if(tablaSimbolos.get(i).typeLexema.equals("sc")){
                if(tablaSimbolos.get(i-1).lexema.equals("=")){
                    tablaSimbolos.get(i-2).valorIdent=tablaSimbolos.get(i).lexema;
                    tablaSimbolos.get(i).valorIdent=tablaSimbolos.get(i).lexema;
                }
                else{
                String ptoken="GEN";
                int pos;
                for(int j=0;j<4;j++){
                    pos=(int) Math.round(Math.random()*26);
                    if(pos>25)
                        pos=25;
                    ptoken+=letras[pos];
                }
                
                
                tablaSimbolos.get(i).valorIdent=tablaSimbolos.get(i).lexema;
                tablaSimbolos.get(i).lexema=ptoken;
            }
            }
            
        }
        
        for(int i=0;i<tablaSimbolos.size();i++){
            if(tablaSimbolos.get(i).typeLexema.equals("nm")){
                if(tablaSimbolos.get(i-1).lexema.equals("=")){
                    tablaSimbolos.get(i-2).valorIdent=tablaSimbolos.get(i).lexema;
                    tablaSimbolos.get(i).valorIdent=tablaSimbolos.get(i).lexema;
                }
            }
            
        }
    }
    
    private void direccion(){
        ArrayList<Integer> b = new ArrayList<Integer>();
        int direc=0;
        for(int i=0;i<tablaSimbolos.size();i++){
            if(tablaSimbolos.get(i).typeLexema.equals("id")&& tablaSimbolos.get(i).direccion == null){
                int m;
                b= repetido(tablaSimbolos.get(i).lexema+"");
                for(int k=0; k<b.size(); k++){
                    m = (int) b.get(k);
                    tablaSimbolos.get(m).direccion=i;
                }
            }
        }
        
    }
}
