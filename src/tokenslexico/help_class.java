package tokenslexico;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class help_class extends JInternalFrame
{
    private JScrollPane spContent;
    private JTextArea jaContent;
    private manageFile mf = new manageFile();
    
    public help_class(){
        super("Ayuda");
        setLayout(null);
        setSize(385,330);
        setLocation(735, 40);
        setClosable(true);
        setResizable(false);
        setVisible(true); 
        createInterfaz();
    }
    
    private void createInterfaz(){
        String contenido = "";
        try {
            contenido = mf.getArchivoContent("help.dat");
        } catch (Exception e) {
            System.err.println("Error interno: "+e);
            contenido = "";
        }
        
        jaContent = new JTextArea(contenido);
        jaContent.setEnabled(false);
        
        spContent = new JScrollPane(jaContent);
        spContent.setSize(360,290);
        spContent.setLocation(5,5);
        
        add(spContent);
    }
}
