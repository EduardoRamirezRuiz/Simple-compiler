package tokenslexico;

import java.util.ArrayList;

public class semantico {
    private final ArrayList<MyObject> tablaSímbolos;
    private final ArrayList<MyObject> varDeclaradas, varBloque;
    //private ArrayList<String> palabrasReservadas, palabrasTablaS;
    int i,j;
    private String errores;
    
    public semantico(ArrayList<MyObject> tablaSímbolos) {
        this.tablaSímbolos = tablaSímbolos;
        varBloque = new ArrayList<>();
        varDeclaradas = new ArrayList<>();
        errores = "";
        
        arrVarDeclaradas();
        asignarTipo();
        divisionCero();
        varNula();
        valorNumerico();
        opAritmeticas();
        tiposEnCondicion();
        bloque();
        usedBeforeDeclared();
    }

    public String getErrores() {
        if(errores.equalsIgnoreCase(""))
            errores = "Semánticamente correcto";
        return errores;
    }
    
    private void bloque(){
        String errBloque="";
        for (j = 0;j < tablaSímbolos.size(); j++)
            if(tablaSímbolos.get(j).id == 16 || tablaSímbolos.get(j).id == 17 || tablaSímbolos.get(j).id == 18){//16 mientras, 17 si, 18 sino
                while(tablaSímbolos.get(j).id != 60)
                    j++;
                
                i = j;
                while(tablaSímbolos.get(i).id != 61)
                    i++;
                
                while(tablaSímbolos.get(j).id != 61){
                    if(tablaSímbolos.get(j).id == 24 )
                        varBloque.add(new MyObject(tablaSímbolos.get(j+1).lexema+"", "ent", null, null, 24 , i));
                    if(tablaSímbolos.get(j).id == 20 )
                        varBloque.add(new MyObject(tablaSímbolos.get(j+1).lexema+"", "cad", null, null, 20 , i));
                    j++;
                }
            }
        try{
        for (i = 0;i < varBloque.size(); i++)
            for (j = 0;j < tablaSímbolos.size(); j++)
                if(tablaSímbolos.get(j).lexema.equals(varBloque.get(i).lexema))
                    if(tablaSímbolos.get(j-1).id != 24 || tablaSímbolos.get(j-1).id != 20 )
                        if(j > varBloque.get(i).linea)
                            errBloque += "ERROR! fuera del bloque la variable:   " + varBloque.get(i).lexema;
        }catch(Exception ex){System.out.println("bloque: "+ex);}
        
        if(!errBloque.equals(""))
            errores += "Error 370: Ámbito de variables: \n"+errBloque;    
    }
    
    private void tiposEnCondicion(){
        int tipo=0;
        try{
            for(i=0; i < tablaSímbolos.size(); i++){
                tipo = 0;
                if(tablaSímbolos.get(i).id==111 || tablaSímbolos.get(i).id==110 || tablaSímbolos.get(i).id==105
                        || tablaSímbolos.get(i).id==107 || tablaSímbolos.get(i).id==109){ //si es un signo de condicion
                    
                    if(tablaSímbolos.get(i-1).id==12){ //si el anterior al signo es un identificador
                        for(j=0; j < tablaSímbolos.size(); j++) //comienza a recorrer
                            if(tablaSímbolos.get(i-1).lexema.equals(tablaSímbolos.get(j).lexema)){ //si son iguales revisa si el anterior es un 24 o 20
                                if(tablaSímbolos.get(j-1).id==24 || tablaSímbolos.get(j-1).id==20)
                                    tipo = tablaSímbolos.get(j-1).id;
                            }
                        if(tipo != 24){
                            errores += "Erro 350: Tipo incorrecto en una condición, cerca de la linea "+tablaSímbolos.get(i).linea;
                        }
                    }
                    
                    if(tablaSímbolos.get(i+1).id==12){ //si el anterior al signo es un identificador
                        for(j=0; j < tablaSímbolos.size(); j++) //comienza a recorrer
                            if(tablaSímbolos.get(i+1).lexema.equals(tablaSímbolos.get(j).lexema)){ //si son iguales revisa si el anterior es un 24 o 20
                                if(tablaSímbolos.get(j-1).id==24 || tablaSímbolos.get(j-1).id==20)
                                    tipo = tablaSímbolos.get(j-1).id;
                            }
                        if(tipo != 24){
                            errores += "Erro 350: Tipo incorrecto en una condición, cerca de la linea "+tablaSímbolos.get(i).linea;
                        }
                    }
                    
                }
            }
        }catch(Exception ex){System.out.println("Error interno en tipos de condicion " + ex);}
    }
    
    private void arrVarDeclaradas(){
        for (i = 0; i < tablaSímbolos.size(); i++){
            if(tablaSímbolos.get(i).id == 20)
                varDeclaradas.add(new MyObject
                        (tablaSímbolos.get(i+1).lexema+"", "cad", null, null,
                        20 , tablaSímbolos.get(i).linea));
            if(tablaSímbolos.get(i).id == 24)
                varDeclaradas.add(new MyObject
                        (tablaSímbolos.get(i+1).lexema+"", "ent", null, null, 
                        24 , tablaSímbolos.get(i).linea));
        }
        
        //revisar que no se repitan con el mismo nombre
        int j = -1;
        String errVarRepe="";
        for(MyObject str: varDeclaradas){
            for(MyObject str2: varDeclaradas)
                if(str.lexema.equals(str2.lexema))
                    j++;
            
            if(j>0)
                errVarRepe += str.lexema + " cerca de linea "+str.linea+"\n";
            j = -1;
        }
        
        if(!errVarRepe.equals(""))
            errores += "Error 270: Variables repetidas:\n"+errVarRepe;
            //System.out.println("ERROR SEMÁNTICO:    Se repiten variables    " + errVarRepe);
        
        
        String errNoDeclarada="";
        int aux = 0;
        //variable no declarada
        for (i = 0; i < tablaSímbolos.size(); i++)
           if(tablaSímbolos.get(i).id == 12){
               if(!(tablaSímbolos.get(i-1).id == 20) || !(tablaSímbolos.get(i-1).id == 24))
                   for (int k = 0; k < varDeclaradas.size(); k++)
                       if(tablaSímbolos.get(i).lexema.equals(varDeclaradas.get(k).lexema)){
                           aux++;
                           k = varDeclaradas.size();
                       }
               
               if(aux == 0)
                   errNoDeclarada += tablaSímbolos.get(i).lexema + 
                           " cerca de linea "+tablaSímbolos.get(i).linea+"\n";
               
               aux = 0;
           }
        
        if(!errNoDeclarada.equals(""))
            errores += "Error 280: Las siguientes variables no han sido declaradas: "
                    + "\n" + errNoDeclarada;
        
        
        
            //System.out.println("ERROR SEMANTICO:    Las siguientes variables no estan declaradas:   " + errNoDeclarada);
        
        
        //variable declarada pero nunca usada
        String errVarNoUsada="";
        int cont=0;
        for (i = 0; i < varDeclaradas.size(); i++){
            for (j = 0; j < tablaSímbolos.size(); j++){
                if(varDeclaradas.get(i).lexema.equals(tablaSímbolos.get(j).lexema) 
                        && (tablaSímbolos.get(j-1).id != 20 && tablaSímbolos.get(j-1).id != 24))
                    cont++;
            }
            if(cont==0)
                errVarNoUsada += varDeclaradas.get(i).lexema +"\n";
            
            cont=0;
        }
        
        if(!errVarNoUsada.equals(""))
            errores +="Error 290: Las siguientes variables se declaran pero no se usan:\n" + errVarNoUsada;
        
        
        
        
            //System.out.println("ERROR SEMANTICO: Las siguientes variables se declaran pero no se usan:   " + errVarNoUsada);
    }
    
    private void asignarTipo(){
        //asginar variable de diferente tipo. ent x; x="hola";
        String errDifTipo="";
        for (i = 0; i < varDeclaradas.size(); i++){
            for (j = 0; j < tablaSímbolos.size(); j++){
                if(tablaSímbolos.get(j).lexema.equals(varDeclaradas.get(i).lexema)){
                    if(tablaSímbolos.get(j-1).id != 20 && tablaSímbolos.get(j-1).id != 24){
                        if(tablaSímbolos.get(j+1).id == 112){
                            if(varDeclaradas.get(i).id==20 && !(tablaSímbolos.get(j+2).id==57))//if(varDeclaradas.get(i).typeLexema.equals("cad") && !tablaSímbolos.get(j+2).typeLexema.equals("sc"))//cadena
                                errDifTipo += varDeclaradas.get(i).lexema + "   ";
                            
                            boolean b1 = !(tablaSímbolos.get(j+2).id==2); //typeLexema.equals("nm");
                            boolean b2 = !(tablaSímbolos.get(j+2).id==12);//typeLexema.equals("id");
                            boolean b3 = !(tablaSímbolos.get(j+3).id==2);//typeLexema.equals("nm");
                            boolean b4 = !(tablaSímbolos.get(j+3).id==12);//typeLexema.equals("id");
                            
                            
                            //if(varDeclaradas.get(i).typeLexema.equals("ent") && (!tablaSímbolos.get(j+2).typeLexema.equals("nm")&&
                                    //!tablaSímbolos.get(j+2).typeLexema.equals("id")))
                            if(varDeclaradas.get(i).id==24 && ( b1 && b2 && b3 && b4 ))//if(varDeclaradas.get(i).typeLexema.equals("ent") && ( b1 && b2 && b3 && b4 ))
                                errDifTipo += varDeclaradas.get(i).lexema + " cerca de linea "+tablaSímbolos.get(j+2).linea+"\n";
                        }
                    }
                }
            }
        }
        
        if(!errDifTipo.equals(""))
            errores += "Error 300: La asignación de valor a las siguientes variables es incorrecto: \n"+ errDifTipo;
            //System.out.println("ERROR SEMANTICO: Las siguientes variables son de dif tipo al declarado:   " + errDifTipo);
    }
    
    private void divisionCero(){
        int valor=100000;
        int linea=0;
        for (i = 0; i < tablaSímbolos.size(); i++){
            if(tablaSímbolos.get(i).id == 8){
                for(int m=0; m < i; m++){
                    if(tablaSímbolos.get(m).valorIdent!="" 
                        && tablaSímbolos.get(m).lexema.equals(tablaSímbolos.get(i+1).lexema))
                        try{
                            String auxiliar = ""+tablaSímbolos.get(m).valorIdent;
                            valor = Integer.parseInt(auxiliar);
                            linea = tablaSímbolos.get(i).linea;
                        }
                        catch(Exception ex){ valor=1000; System.out.println(ex);}
                }
                
                if(tablaSímbolos.get(i+1).lexema.equals("0"))
                    errores += "Error 310: División entre cero, valor de: "+tablaSímbolos.get(i-1).lexema+" cerca de la"
                            + " linea "+tablaSímbolos.get(i+1).linea+"\n";
                if(valor==0){
                    errores += "Error 310: División entre cero, varlor de: "+tablaSímbolos.get(i-1).lexema+" cerca de la"
                            + " linea "+linea+"\n";
                    valor = 10000;
                    linea = 0;
                }
            }
        }
    }
    
    private void varNula(){
        String errVarNula="";
        int linea=0;
        boolean b=false;
        for (i = 0; i < varDeclaradas.size(); i++){
            for (j = 0; j < tablaSímbolos.size(); j++){
                if(tablaSímbolos.get(j).lexema.equals(varDeclaradas.get(i).lexema)){
                    if(tablaSímbolos.get(j-1).id != 20 && tablaSímbolos.get(j-1).id != 24){
                        if(tablaSímbolos.get(j+1).id != 112){
                            j = tablaSímbolos.size();
                            try{
                                linea = tablaSímbolos.get(j+1).linea;
                            }catch(Exception ex){linea=0;}
                            b = true;
                        }else
                            j = tablaSímbolos.size();
                    }
                }
            }
            if(b==true){
                errVarNula += varDeclaradas.get(i).lexema + " cerca de linea "+linea+"\n";
                b = false;
            }
        }
        
        if(!errVarNula.equals(""))
            errores += "Error 320: Variable con valor nulo: \n"+errVarNula;
            //System.out.println("ERROR SEMANTICO: las siguientes variables son nulas   " + errVarNula);
    }
    
    private void valorNumerico(){
        String errValorNum = "";
        for (i = 0; i < varDeclaradas.size(); i++)
            if(varDeclaradas.get(i).id==24)//typeLexema.equals("ent"))
                for (j = 0; j < tablaSímbolos.size(); j++)
                    if(tablaSímbolos.get(j).lexema.equals(varDeclaradas.get(i).lexema))
                        if(tablaSímbolos.get(j-1).id != 20 && tablaSímbolos.get(j-1).id != 24)
                            if(tablaSímbolos.get(j+1).id == 112){
                                if(tablaSímbolos.get(j+2).id == 58){
                                    if(tablaSímbolos.get(j+3).id==2)//typeLexema.equals("ent"))
                                        if(Integer.parseInt(tablaSímbolos.get(j+3).lexema+"") < (-32768))
                                            errValorNum += varDeclaradas.get(i).lexema + " ";
                                }
                                else{
                                    if(tablaSímbolos.get(j+2).id==2)//typeLexema.equals("ent"))
                                        if(Integer.parseInt(tablaSímbolos.get(j+2).lexema+"") > 32767)
                                            errValorNum += varDeclaradas.get(i).lexema + " cerca de linea "+tablaSímbolos.get(j+2).linea+"\n";
                                }
                            }
                            
        
        
        if(!errValorNum.equals(""))
            errores += "Error 330: Las siguientes variables numéricas exceden el limite: \n"+errValorNum+"\n";
            //System.out.println("ERROR SEMANTICO: Las siguientes variables numericas exceden el limite:  " + errValorNum);
    }
    
    private void opAritmeticas(){
        String errOpAritmeticas = "";
        String aux;
        for (j = 0; j < tablaSímbolos.size(); j++){
            if(tablaSímbolos.get(j).id == 5 || tablaSímbolos.get(j).id == 6 || tablaSímbolos.get(j).id == 7 || tablaSímbolos.get(j).id == 8){
                if(tablaSímbolos.get(j-1).id != 2){
                    if(tablaSímbolos.get(j-1).id == 12)
                        for (i = 0; i < varDeclaradas.size(); i++)
                            if(varDeclaradas.get(i).lexema.equals(tablaSímbolos.get(j-1).lexema))
                                if(varDeclaradas.get(i).id==20)//typeLexema.equals("cad"))
                                    errOpAritmeticas += tablaSímbolos.get(j-1).lexema + " cerca de linea "+tablaSímbolos.get(j-1).linea+"\n";
                            
                    
                }
                if(tablaSímbolos.get(j+1).id != 2){
                    if(tablaSímbolos.get(j+1).id == 12){
                        aux = tablaSímbolos.get(j+1).lexema + "";
                        for (i = 0; i < varDeclaradas.size(); i++)
                            if(varDeclaradas.get(i).lexema.equals(tablaSímbolos.get(j+1).lexema))
                                if(varDeclaradas.get(i).id==20)//typeLexema.equals("cad"))
                                    errOpAritmeticas += tablaSímbolos.get(j+1).lexema + " cerca de linea "+tablaSímbolos.get(j+1).linea+"\n";
                    }else
                        errOpAritmeticas += tablaSímbolos.get(j+1).lexema + "   ";
                }
            }
        }
        
        if(!errOpAritmeticas.equals(""))
            errores += "Error 340: Se intenta realizar operaciones aritméticas con: \n"+errOpAritmeticas;
            //System.out.println("ERROR SEMANTICO: Se intento hacer operacion aritmetica con: " + errOpAritmeticas);
    }

    private void usedBeforeDeclared() {
        for(MyObject registro: tablaSímbolos){
            if(registro.id==12){
                for(MyObject variable: varDeclaradas){
                    if(registro.lexema.equals(variable.lexema))
                        if(registro.linea<variable.linea)
                            errores+="Error 380: Variable usada antes de ser declarada, var: "+registro.lexema+", cerca de línea "+registro.linea+"\n";
                }
            }
        }
    }
}