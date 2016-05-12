package tokenslexico;

public class automaton_lex {

    int indice = 0, estado =0, codigoAscii = 0;
    
    public int isBegin(String lexema){
        indice = 0;
        estado = 0;
        while(indice <= lexema.length()){
            try{
                codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            switch(estado){
                case 0:
                    if(indice==0 && codigoAscii==105) //i
                        estado = 1;
                    else
                        estado=1000;
                    break;
                case 1:
                    if(codigoAscii==110) //n
                        estado = 2;
                    else
                        estado=1000;
                    break;
                case 2:
                    if(codigoAscii==105) //i
                        estado = 3;
                    else
                        estado=1000;
                    break;
                case 3:
                    if(codigoAscii==99) //c
                        estado = 4;
                    else
                        estado=1000;
                    break;
                case 4:
                    if(codigoAscii==105) //i
                        estado = 5;
                    else
                        estado=1000;
                    break;
                case 5:
                    if(codigoAscii==111) //o
                        estado = 6;
                    else
                        estado=1000;
                    break;
                case 6:
                    if(codigoAscii==-1) //lamda
                        estado = 7;
                    else
                        estado=1000;
                    break;
            }
            indice++;
        }
        
        return estado == 7 ? 1 : -1;
    }
    
    public int isReservedWord(String lexema){
        indice = 0;
        estado = 0;
        while(indice <= lexema.length()){
            try{
                codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            
            switch(estado){
                case 0:
                    if(codigoAscii==115)//s                
                        estado=1;
                    if(codigoAscii==109)//m
                        estado=2;
                    if(codigoAscii==99)//c
                        estado=17;
                    if (codigoAscii==101)//e
                        estado=21;
                    if (codigoAscii==105)//i
                        estado=25;
                    if(codigoAscii!=115 && codigoAscii!=109 && codigoAscii!=99 && codigoAscii!=101 && codigoAscii!=105)
                        estado=1000;
                break;
                case 1:
                     if(codigoAscii==105) //i               
                        estado=3;
                    break;
                case 2:
                    if(codigoAscii==105) //i               
                        estado=9;     
                break;
                case 3:
                     if(codigoAscii==110)//n               
                        estado=4;
                     else if(codigoAscii==-1 || codigoAscii==105)
                         estado=7;
                         else
                         estado=1000;
                    break;
                case 4:
                     if(codigoAscii==111) //o                
                        estado=6;
                    break;
                case 5:
                    //estado omitido por conveniencia del manejo de lamda en el estado 3
                    break;
                case 6:
                    if(codigoAscii==-1 || codigoAscii==111)//fin palabra reservada sino
                        estado=8;
                    else
                        estado=1000;
                    break;
                    
                case 9:
                     if(codigoAscii==101) //e                
                        estado=10;   
                    break;
                case 10:
                    if(codigoAscii==110)//n                
                        estado=11;
                    break;
                 case 11:
                   if(codigoAscii==116) //t              
                        estado=12;
                    break;
                case 12:
                   if(codigoAscii==114) //r               
                        estado=13;
                    break;
                 case 13:
                     if(codigoAscii==97) //a                
                        estado=14;   
                    break;
                case 14:
                  if(codigoAscii==115) //s                
                        estado=15;
                    break;
                case 15:
                    if(codigoAscii==-1 || codigoAscii==115)//fin palabra reservada mientras
                        estado=16;
                    else
                        estado=1000;
                    break;
                case 17:
                    if(codigoAscii==97) //a              
                        estado=18;
                    break;
                case 18:
                    if(codigoAscii==100) //d              
                        estado=19;
                    break;
                case 19:
                    if(codigoAscii==-1||codigoAscii==100)//fin palabra reservada cad
                        estado=20;
                    else
                        estado=1000;
                    break;
                case 21:
                    if(codigoAscii==110) //n               
                        estado=22;
                    break;
                case 22:
                    if(codigoAscii==116) //t            
                        estado=23;
                    break;
                case 23:
                    if(codigoAscii==-1 || codigoAscii==116)//fin palabra reservada ent
                        estado=24;
                    else
                        estado=1000;
                    break;
                case 25:
                    if(codigoAscii==109)//m             
                        estado=26;
                    break;
                case 26:
                    if(codigoAscii==112) //p             
                        estado=27;
                    break;
                case 27:
                    if(codigoAscii==114) //r               
                        estado=28;
                    break;
                case 28:
                    if(codigoAscii==105)//i              
                        estado=29;
                    break;
                case 29:
                    if(codigoAscii==109) //m             
                        estado=30;
                    break;
                case 30:
                    if(codigoAscii==101) //e              
                        estado=31;
                    break;
                case 31:
                    if(codigoAscii==-1 || codigoAscii==101)//fin palabra reservada imprime
                        estado=32;
                    else
                        estado=1000;
                    break;                   
             }
            
            indice++;
        }
        
        if(estado == 7 || estado == 8 || estado == 16 
            || estado == 20 || estado == 24 || estado == 32){ //conjunto de estado finales
            
            if(estado == 7 || estado == 8)
                estado+=10;
            
            return estado;
        }
        else
            return -1;
    }
    
    public int isArithmetic(String lexema){
        indice = 0;
        estado = 0;
        
        while(indice <= 1){
            
            try{ codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            
            switch(estado){
                case 0:
                    if(codigoAscii==43) //+              
                        estado=1;
                    if(codigoAscii==45)//-
                        estado=2;
                    if(codigoAscii==42)//*
                        estado=3;
                    if (codigoAscii==47)///
                        estado=4;
                    if(codigoAscii!=43 && codigoAscii!=45 && codigoAscii!=42 && codigoAscii!=47)
                        estado=1000;
                    break;
                case 1:
                    if(codigoAscii==-1)
                        estado=5;
                    break;
                case 2:
                    if(codigoAscii==-1)
                        estado=6;
                    break;
                case 3:
                    if(codigoAscii==-1)
                        estado=7;
                    break;
                case 4:
                    if(codigoAscii==-1)
                        estado=8;
                    break;
                default:
                    estado=1000;
                    break;
            }
            
            indice++;
        }
        
        if(estado == 5 || estado == 6 || estado == 7 || estado == 8)
            return estado;
        else
            return -1;
    }
    
    public int isComparative(String lexema){
        indice = 0;
        estado = 0;
        
        for(;indice <= lexema.length();indice++){
            
            try{ codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            
            switch(estado){
                case 0:
                    if(codigoAscii==60) //<              
                        estado=1;
                    if(codigoAscii==62)//>
                        estado=2;    
                    if(codigoAscii==61) //=
                        estado=3;
                    if(codigoAscii!=60  && codigoAscii!=62  && codigoAscii!=61)
                        estado=1000;
                break;
                case 1:
                    if(codigoAscii==61)//=
                        estado=4;
                    if(codigoAscii==-1)
                        estado=5;
                    break;
                case 2:
                    if(codigoAscii==61)//=
                        estado=6;
                    if(codigoAscii==-1)
                        estado=7;                                                
                break;
                case 3:
                    if(codigoAscii==61)//=
                        estado=8;
                    if(codigoAscii==-1)
                        estado=12;
                    break;
                case 4:
                    if(codigoAscii==-1)
                        estado=9;
                    break;
                case 6:
                    if(codigoAscii==-1)
                        estado=10;
                    break;
                case 8:
                    if(codigoAscii==-1)
                        estado=11;
                    break;                  
            }
        }
        
        if(estado == 9 || estado == 5 || estado == 10 || estado == 7
            || estado == 11 || estado == 12)
            return estado+100;
        else
            return -1;
    }
    
    public int isIdentifier(String lexema){
        indice = 0;
        estado = 0;
        
        for(;indice <= lexema.length();indice++){
            
            try{ codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            
            switch(estado){
                case 0:
                    if(codigoAscii >=97 && codigoAscii<=122)//a-z
                        if(indice==0)
                            estado = 1;
                        else
                            estado = 1000;
                    break;
                case 1:
                    if((codigoAscii >=97 && codigoAscii<=122)||(codigoAscii>=48 && codigoAscii<=57))//a-z & 0-9
                        estado=1;
                    if(codigoAscii == -1)
                        estado=2;
                    break;
            }
        }
        if(estado == 2)
            return estado+10;
        else
            if(estado == 1000)
                return -2;
            else
                return -1;
    }
    
    public int isSign(String lexema){
        indice = 0;
        estado = 0;
        
        for(;indice <= lexema.length();indice++){
            
            try{ codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            
            switch(estado){
                case 0:
                    if(codigoAscii == 40)//(
                        estado=1;
                    if(codigoAscii == 41)//)
                        estado=2;
                    if(codigoAscii == 123)//{
                        estado=3;
                    if(codigoAscii == 125)//}
                        estado=4;
                    if(codigoAscii == 59)//;
                        estado=5;
                    if(codigoAscii == 34)//"
                        estado=6;
                    if(codigoAscii == 46) //.
                        estado= 13;
                    
                    if(codigoAscii!=40 && codigoAscii!=41  && codigoAscii!=123 && codigoAscii!=125
                        && codigoAscii!=59 && codigoAscii!=34 && codigoAscii!=46)
                        estado=1000;
                    break;
                case 1:
                    if(codigoAscii==-1)
                        estado=8;
                    break;
                case 2:
                    if(codigoAscii==-1)
                        estado=9;
                    break;
                case 3:
                    if(codigoAscii==-1)
                        estado=10;
                    break;
                case 4:
                    if(codigoAscii==-1)
                        estado=11;
                    break;
                case 5:
                    if(codigoAscii==-1)
                        estado=12;
                    break;
                case 6:
                    if((codigoAscii >=97 && codigoAscii<=122)||(codigoAscii>=48 && codigoAscii<=57))//a-z & 0-9
                        estado=6;
                    if(codigoAscii == 34)//"
                        estado=7;
                    break;
                case 13:
                    if(codigoAscii == -1)
                        estado = 14;
            }
        }
        
        if(estado == 8 || estado == 9 || estado == 10 || estado == 11 
            || estado == 12 || estado == 7 || estado == 14)
            return estado+50;
        else
            return -1;
    }
    
    public int isNumber(String lexema){
        indice = 0;
        estado = 0;
        
        for(;indice <= lexema.length();indice++){
            
            try{ codigoAscii=(int) lexema.charAt(indice); }
            catch(Exception ex){ codigoAscii=-1; /*simulación vacío (lamda)*/ }
            switch(estado){
                case 0:
                    if(codigoAscii>=48 && codigoAscii<=57 || codigoAscii==45) //numeros 0-9 or -
                        estado=1;
                    break;
                case 1:
                    if(codigoAscii>=48 && codigoAscii<=57) //numeros 0-9
                        estado=1;
                    else
                        estado=1000;
                    if(codigoAscii==-1)
                        estado=2;
                    break;
            }
        }
        if(estado==2)
            return estado;
        else
                return -1;
    }
}
