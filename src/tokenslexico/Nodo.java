package tokenslexico;

public class Nodo 
{
    int info; Nodo siguiente;
    
    public Nodo (int I)
    {
        info=I; siguiente=null;
    }
    
    public Nodo(int I, Nodo S)
    {
        info=I; siguiente=S;
    }
}
