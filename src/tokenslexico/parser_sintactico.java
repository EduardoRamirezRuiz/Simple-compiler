package tokenslexico;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class parser_sintactico 
{
    private ArrayList<MyObject> tablaSímbolos;
    private ArrayList<MyObject> expresiones = new ArrayList<MyObject>();
    private ArrayList<String> expresiones2 = new ArrayList<>();
    private pila_class pila_llaves =  new pila_class();
    private pila_class pila_parentesis =  new pila_class();
    private pila_class pila_SI =  new pila_class();
    private String errores;
    int estado = 1;
    int i;
    
    public parser_sintactico(ArrayList<MyObject> tablaSímbolos) {
        this.tablaSímbolos = tablaSímbolos;
        errores = "";
        parser();
    }

    public String getErrores() {
        if(errores.equalsIgnoreCase(""))
            errores = "Sintácticamente correcto";
        return errores;
    }
    private void parser(){
        boolean continuar = true;
        int aux = 0;
        if(tablaSímbolos.get(0).id!=1)
            errores+="Error 230: No se declaro el inicio del método en la línea 1\n";
        for (i = 0; i < tablaSímbolos.size(); i++) {
            aux = i;
            switch(tablaSímbolos.get(i).id){
                //inicio
                case 1:
                    continuar = inicial();
                    if(continuar==false)
                        errores+="Error 240: Error en el método inicio, línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                //crear variable
                case 24: 
                    continuar = createVar();
                    if(continuar==false)
                        errores+="Error 140: Al crear una variable, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                case 20: 
                    continuar = createVar();
                    if(continuar==false)
                        errores+="Error 140: Al crear una variable, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                //cambiar/aignar variable
                case 12:
                    continuar = cambiarValVar();
                    if(continuar==false)
                        errores+="Error 150: Al asignar el valor a una variable, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                //imprime
                case 32:
                    continuar = funcImprime();
                    if(continuar==false)
                        errores+="Error 160: En la función Imprime, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                //mientras
                case 16:
                    continuar = mientras();
                    if(continuar==false)
                        errores+="Error 180: En la función mientras, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                //si, si-sino
                case 17:
                    continuar = si();
                    if(continuar==false)
                        errores+="Error 200: En la estructura si, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    else
                        pila_SI.mete_pila(17);
                    break;
                case 18:
                    if(pila_SI.tamaño>0 && tablaSímbolos.get(i-1).id==61){
                        continuar = sino();
                        if(continuar==false)
                            errores+="Error 220: En la estructura sino, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    }
                    else
                        errores+="Error 225: Uso de SINO sin antes un SI, cerca de línea: "+tablaSímbolos.get(aux).linea+"\n";
                    break;
                case 61:
                    try{ 
                        pila_llaves.sacar_pila();
                    } 
                    catch(Exception ex){
                        continuar=false;
                        errores+="Error 190: Posible llave abierta, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";
                    }
                    break;
            }
            if(continuar == false)
                break;
        }
        if(!(continuar && pila_parentesis.tamaño==0 && pila_llaves.tamaño==0)){
            if(pila_parentesis.tamaño>0)
                errores+="Error 190: Posible parentésis abierta cerca de línea (undefined)\n";
            if(pila_llaves.tamaño>0)
                errores+="Error 190: Posible llave abierta cerca de línea (undefined)\n";
        }
    }
    
    private boolean inicial(){
        estado = 1;
        try {
            if(estado == 1 && tablaSímbolos.get(i+1).id==60){
                estado = 2;
                pila_llaves.mete_pila(1);
                i++;
            }
            else{
                estado = 1000;
            }
        } catch (Exception e) {
            estado = 1000;
        }
        
        return estado==2;
    }
    
    private boolean sino(){
        estado=1;
        try {
            if(estado==1 && tablaSímbolos.get(i+1).id==60){
                estado=2;
                pila_llaves.mete_pila(60);
                i++;
            }
        } catch (Exception e) {
            estado=1000;
        }
        
        //fin del autómata
        return estado==2;
    }
    
    private boolean si(){
        estado = 1;
        boolean condicion = true;
        try {            
            if(estado==1 && tablaSímbolos.get(i+1).id==58){
                estado=2;
                pila_parentesis.mete_pila(58);
                i++;
            }
            if(estado==2){
                condicion = condicion();
                if(condicion)
                    estado=3;
                else{
                    errores+="Error 170: Condicion con error sintactico, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";
                    estado = 1000;
                }
            }
            if(estado==3 && tablaSímbolos.get(i+1).id==59){
                try{ 
                    pila_parentesis.sacar_pila();
                    estado = 4;
                    i++;
                } 
                catch(Exception ex){estado=1000; errores+="Error 210: Error en la condicion del SI, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";}
            }
            if(estado==4 && tablaSímbolos.get(i+1).id==60){
                estado=5;
                pila_llaves.mete_pila(60);
                i++;
            }
        } catch (Exception e) {
            estado=1000;
        }
        //fin del autómata
        if(estado==5)
            return true;
        else
            return false;
    }
    
    private boolean mientras(){
        boolean condicion = true;
        estado = 1;
        try {
            if(estado==1 && tablaSímbolos.get(i+1).id==58){
                estado=2;
                pila_parentesis.mete_pila(58);
                i++;
            }
            if(estado==2){
                condicion = condicion();
                if(condicion)
                    estado=3;
                else{
                    errores+="Error 170: Condicion con error sintactico, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";
                    estado = 1000;
                }
            }
            if(estado==3 && tablaSímbolos.get(i+1).id==59)
                try{ 
                    pila_parentesis.sacar_pila();
                    estado = 4;
                    i++;
                } 
                catch(Exception ex){estado=1000; errores+="Error 180: Ciclo mientras con error sintactico, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";}

            if(estado==4 && tablaSímbolos.get(i+1).id==60){
                estado=5;
                pila_llaves.mete_pila(60);
                i++;
            }
        } catch (Exception e) {
            estado=1000;
        }
        
        return estado==5;
    }
    
    private boolean condicion(){
        estado = 0;
        try {
            if(estado==0 && (tablaSímbolos.get(i+1).id==12 || tablaSímbolos.get(i+1).id==2)){
                estado=1;
                i++;
            }
            
            //105 <, 111 ==, 110 >=, 109 <=, 107 >
            if(estado==1 && (tablaSímbolos.get(i+1).id==105 || tablaSímbolos.get(i+1).id==111 
                    || tablaSímbolos.get(i+1).id==110 || tablaSímbolos.get(i+1).id==109 || tablaSímbolos.get(i+1).id==107)){
                estado=2;
                i++;
            }
            if(estado==2 && (tablaSímbolos.get(i+1).id==12 || tablaSímbolos.get(i+1).id==2)){
                estado=3;
                i++;
            }
        } catch (Exception e) {
            estado=1000;
        }
        //fin del autómata
        return estado == 3;
    }
    
    private boolean funcImprime(){
        estado = 1;
        try {        
            if(estado==1 && tablaSímbolos.get(i+1).id==58){
                estado=2;
                i++;
            }
            if(estado==2 && tablaSímbolos.get(i+1).id==57){
                estado=3;
                i++;
            }
            if(estado==3 && tablaSímbolos.get(i+1).id==59){
                estado=4;
                i++;
            }
            if(estado==3 && tablaSímbolos.get(i+1).id==64){
                estado=5;
                i++;
            }
            if(estado==5 && (tablaSímbolos.get(i+1).id==2 || tablaSímbolos.get(i+1).id==12 || tablaSímbolos.get(i+1).id==57)){
                estado=7;
                i++;
            }
            if(estado==7 && tablaSímbolos.get(i+1).id==59){
                estado=4;
                i++;
            }
            if(estado==2 && tablaSímbolos.get(i+1).id==12){
                estado=6;
                i++;
            }
            if(estado==6 && tablaSímbolos.get(i+1).id==59){
                estado=4;
                i++;
            }
            if(estado==4 && tablaSímbolos.get(i+1).id==62)
                estado=8;
            i++;
        } catch (Exception e) {
            estado=1000;
        }
        
        return estado==8;
    }
    
    private boolean cambiarValVar(){
        estado = 1;
        try {
            if(estado==1 && tablaSímbolos.get(i+1).id==112){
                estado=2;
                i++;
            }
            if(estado==2 && tablaSímbolos.get(i+1).id==57){
                estado=3;
                i++;
            }
            
            if(estado==3 && tablaSímbolos.get(i+1).id==62){
                estado=7;
                i++;
            }
            if(estado==3 && tablaSímbolos.get(i+1).id==64){
                estado=4;
                i++;
            }
            if(estado==4 && (tablaSímbolos.get(i+1).id==2 || tablaSímbolos.get(i+1).id==12)){
                estado=5;
                i++;
            }
            if(estado==5 && tablaSímbolos.get(i+1).id==62){
                estado=7;
                i++;
            }
            if(estado==2 && (tablaSímbolos.get(i+1).id==2 || tablaSímbolos.get(i+1).id==12)){
                estado=6;
                i++;
            }
            if(estado==6 && tablaSímbolos.get(i+1).id==62){
                estado=7;
                i++;
            }
            if(estado==6 && (tablaSímbolos.get(i+1).id==5 || tablaSímbolos.get(i+1).id==6
                        || tablaSímbolos.get(i+1).id==7 || tablaSímbolos.get(i+1).id==8)){
                estado=8;
                i++;
            }
            if(estado==8 && (tablaSímbolos.get(i+1).id==2 || tablaSímbolos.get(i+1).id==12)){
                estado=9;
                i++;
            }
            
            if(estado==9 && tablaSímbolos.get(i+1).id==62){
                estado=7;
                i++;
            }
            if(estado==2 && tablaSímbolos.get(i+1).id==58){
                pila_parentesis.mete_pila(58);
                estado=10;
                i++;
            }
            if(estado==10 && tablaSímbolos.get(i+1).id==2){
                estado=11;
                i++;
            }
            if(estado==11 && tablaSímbolos.get(i+1).id==59){
                try{
                    pila_parentesis.sacar_pila();
                    estado=12;
                    i++;
                }catch(Exception ex){
                    estado=1000;
                    errores+="Error 155: Sin: Asignación mal a variable, paréntesis mal estructurados con número negativo, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";
                }
            }
            if(estado==12 && tablaSímbolos.get(i+1).id==62){
                estado=7;
                i++;
            }
        } catch (Exception e) {
            estado=1000;
        }
        if(estado==11)
            errores+="Error 155: Sin: Asignación mal a variable, paréntesis mal estructurados con número negativo, cerca de línea: "+tablaSímbolos.get(i).linea+"\n";
        return estado==7;
    }
    
    private boolean createVar(){
        estado = 1;
        try {
            if(estado==1 && tablaSímbolos.get(i+1).id==12)
                estado=2;
            if(estado==2 && tablaSímbolos.get(i+2).id==62)
                estado=3;
            i+=2;
        } catch (Exception e) {
                estado=1000;
        }

        return estado==3;
    }
}
