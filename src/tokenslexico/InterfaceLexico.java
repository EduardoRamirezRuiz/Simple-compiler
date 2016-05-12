package tokenslexico;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public final class InterfaceLexico extends JInternalFrame
{
    JTable tbla;
    DefaultTableModel modelo;
    JScrollPane scroll;
    Object[]etiq={"LEXEMA","TIPO","VALOR","DIRECCION","ID","LINEA"};
    Object[][]dat=new Object[0][6];
    JDesktopPane dskpanel;

    ArrayList<MyObject> tablaSimbolos;
            
    public InterfaceLexico(ArrayList<MyObject> tablaSimbolos)
    {
        super("Tabla Tokens");
        UIManager.getBorder(this);
        UIManager.put("nimbusBlueGrey",new Color(220, 240, 255));
        UIManager.put("control", new Color(245, 245, 245));
        try {

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                    
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
        dskpanel=new JDesktopPane();
        setContentPane(dskpanel);
        
        this.tablaSimbolos = tablaSimbolos;
        setLayout(null);
        crearInterfaz();
        setResizable(false);
        setVisible(true);
        setClosable(true);
        setSize(780,450); 
    }
    
    private void crearInterfaz()
    {
      tbla=new JTable();
      tbla.setFont(new java.awt.Font("Century Gothic", 0, 12)); 
      tbla.setForeground(Color.black);
      
      modelo=new DefaultTableModel(dat,etiq);
      tbla.setModel(modelo);
      tbla.setEnabled(false);
      
      scroll = new JScrollPane(tbla);
      scroll.setSize(680,350);
      scroll.setLocation(40,40);
      add(scroll);
      
      printTable();
    }
    
    private void printTable(){
        String [] renglon = new String [6];
        for(MyObject objeto: tablaSimbolos) {
            renglon[0]= ""+ objeto.lexema;
            renglon[1]= ""+ objeto.typeLexema;
            renglon[2]= ""+ objeto.valorIdent;
            if(objeto.direccion!=null)
                renglon[3]= ""+  objeto.direccion.toString() ;
            else
                renglon[3]="";
            renglon[4]= ""+ objeto.id;
            renglon[5]= ""+ objeto.linea;
            modelo.addRow(renglon);
        }
    }
    
}