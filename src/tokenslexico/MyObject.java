package tokenslexico;

public class MyObject {
    /*tabla de Símbolos*/
    Object lexema;
    Object typeLexema;
    Object valorIdent;
    Object direccion;
    int id;
    int linea;
    /*Sintáctico*/
    String expresion;
    boolean estadoRevision;
    
    public MyObject(Object lexema,  Object typeLexema, Object valorIdent, Object direccion, int id, int linea){
        this.lexema = lexema;
        this.typeLexema = typeLexema;
        this.valorIdent = valorIdent;
        this.direccion=direccion;
        this.id=id;
        this.linea=linea;
    }
    
    public MyObject(String expresion,boolean estadoRevision){
        this.expresion=expresion;
        this.estadoRevision=estadoRevision;
    }
}
