package tokenslexico;
public class pila_class 
{
    Nodo inicio;
    int tamaño = 0;
    public pila_class()
    {
        inicio=null;
    }
    private boolean pila_vacia()
    {
        return inicio==null;
    }
    
    public void mete_pila(int valor)
    {
        inicio=new Nodo(valor,inicio);
        tamaño++;
    }
    
    public int sacar_pila() throws Exception
    {
        int regresa;
        if(!pila_vacia())
        {
            regresa = inicio.info;
            inicio = inicio.siguiente;
            tamaño--;
            return regresa;
        }
        else
        {
            throw new Exception("Pila vacia");
        }
    }
}
