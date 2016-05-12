package tokenslexico;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CUHelp_class extends JInternalFrame
{
    private JScrollPane spContent;
    private JTextArea jaContent;
    private final manageFile mf;
    private final String fileName;
    
    public CUHelp_class(String fileName, String currentFile){
        super("Ayuda sobre "+currentFile);
        setLayout(null);
        setSize(565,330);
        setLocation(735, 40);
        setClosable(true);
        setResizable(false);
        setVisible(true); 
        mf = new manageFile();
        this.fileName=fileName;
        createInterfaz();
    }
    
    private void createInterfaz(){
        String contenido = "";
        try {
            contenido = mf.getExampleHelp(fileName);
        } catch (Exception e) {
            System.err.println("Error interno: "+e);
            contenido = "";
        }
        
        jaContent = new JTextArea(contenido);
        jaContent.setEnabled(false);
        
        spContent = new JScrollPane(jaContent);
        spContent.setSize(550,290);
        spContent.setLocation(5,5);
        
        add(spContent);
    }
}
