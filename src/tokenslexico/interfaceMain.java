package tokenslexico;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class interfaceMain extends JFrame implements ActionListener
{
    private JDesktopPane dskpanel;
    private JScrollPane spLine,spCode,spMistakes;
    private String  stCode="", erroresLexicos, erroresSintacticos, erroresSemanticos;
    private String selected;
    //private int contLinea = 1;
    private JLabel jlTitulo;
    private JTextPane jaCode,jaMistakes;
    private TextLineNumber tlnLines;
    private JMenuBar menuBar;
    private JMenu mArchivo,mAyuda;
    private JMenuItem itmOpenFile, itmAbout, itmTabla, 
            itmErrores, itmCasosLex, itmCasosSin, itmCasosSem, itmAutomatas,itmGramatica, itmAyuda,
            itmMapa,itmVersiones,itmAutomatas2, itmLenguaje, itmBitacora, itmSave, itmDiagramas;
    private JButton btnGuardar, btnLexico, btnSintactico, btnSemantico, btnEjemplos, btnVideo;
    private ImageIcon iiLexico, iiSintactico, iiSemantico, iiLexico2, iiSintactico2, iiSemantico2, iiExample, iiVideo;
    private FileReader archivo;
    private manageFile mf;
    private manageTokens analizadorLexico;
    private parser_sintactico analizadorSintactico;
    private semantico analizadorSemantico;
    private ArrayList<MyObject> tablaSimbolos;
    
    
    public interfaceMain() {
        super("Código fuente");
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
        ImageIcon icono=new ImageIcon(this.getClass().getResource("/recursos/icono.png"));
        
        setIconImage(icono.getImage());
        
        inicialize();
        
        setContentPane(dskpanel);
        setLayout(null);
        setResizable(true);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //cargar();
        makeTop();
        setJMenuBar(menuBar);
        makeBody();
    }
    
    private void inicialize(){
        this.dskpanel=new JDesktopPane();
        this.tablaSimbolos = null;
        this.mf = new manageFile();
        erroresLexicos = "";
        erroresSintacticos = "";
        erroresSemanticos = "";
        iiLexico = new ImageIcon("src/recursos/lexico1.png");
        iiSintactico = new ImageIcon("src/recursos/sintactico1.png");
        iiSemantico = new ImageIcon("src/recursos/semantico1.png");
        iiLexico2 = new ImageIcon("src/recursos/lexico2.png");
        iiSintactico2 = new ImageIcon("src/recursos/sintactico2.png");
        iiSemantico2 = new ImageIcon("src/recursos/semantico2.png");
        iiExample = new ImageIcon("src/recursos/example.png");
        iiVideo = new ImageIcon("src/recursos/play.png");
    }
    
    private void loadFile(boolean opcion) {
        if(opcion)
            mf = new manageFile();
        else
            mf = new manageFile("src/casos_de_uso");
        String linea;
        stCode = "";
        try {
            archivo = mf.getArchivoElegido();
        } catch (Exception ex) {
            System.out.println(""+ex);
        }
        
        if(archivo != null){
            try{
                BufferedReader br = new BufferedReader(archivo);
                linea = br.readLine();
                while(linea!=null){
                    stCode += linea+"\n";
                    linea=br.readLine();
                }
                br.close();
                jaCode.setText(stCode);
                
                manageButtons(1);
                
                jlTitulo.setText(mf.getArchivoName());
            }
            catch(Exception ex){
                System.err.println("Error con el archivo");
            }
        }
        else
            System.out.println("Open file error");
    }
    
    private void saveFile(){
        boolean bandera =  false;
        if(!jaCode.getText().equalsIgnoreCase("")){
            bandera = mf.saveFile(jaCode.getText());
            if(bandera){
                manageButtons(2);
            }
            else
                JOptionPane.showMessageDialog(null, "Error al guardar, reintente", "Guardar", JOptionPane.ERROR_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(null, "Nada para guardar", "Guardar", JOptionPane.WARNING_MESSAGE);
    }
    
    private void manageButtons(int opc){
        if(opc == 1){
            btnGuardar.setEnabled(true);
            btnLexico.setEnabled(false);//removeActionListener(null);
            btnSintactico.setEnabled(false);//removeActionListener(null);
            btnSemantico.setEnabled(false);//removeActionListener(null);
            btnLexico.setIcon(iiLexico);
            btnSintactico.setIcon(iiSintactico);
            btnSemantico.setIcon(iiSemantico);
        }
        else
        {
            btnGuardar.setEnabled(false);
            btnLexico.setEnabled(true);//removeActionListener(null);
            btnSintactico.setEnabled(false);//removeActionListener(null);
            btnSemantico.setEnabled(false);//removeActionListener(null);
            jaMistakes.setText("");
        }
    }
    
    private void drawCode(){
        jaCode.setText("");
        int lineas = tablaSimbolos.get(tablaSimbolos.size()-1).linea;
        int n = 2;
        Color verde = new Color(39, 174, 96);
        Color azul = new Color(41, 128, 185);
        Color naranja = new Color(211, 84, 0);
        Color negro = new Color(44, 62, 80);
        String lexema;
        String typeLexema;
        for (MyObject tablaSimbolo : tablaSimbolos) {
            lexema =tablaSimbolo.lexema+" ";
            //if(tablaSimbolo.typeLexema.equals("nm"))
                //System.err.println(lexema);
            try {
                if (lexema.charAt(0)=='G' && lexema.charAt(1)=='E' && lexema.charAt(2)=='N') {
                    lexema = tablaSimbolo.valorIdent+" ";
                }
            }catch(Exception ex){}
            
            typeLexema = "" + tablaSimbolo.typeLexema;
            if (n > tablaSimbolo.linea) {
                switch(typeLexema){
                    case "rw":
                        addToPane(jaCode, lexema, azul);
                        break;
                    case "id":
                        addToPane(jaCode, lexema, verde);
                        break;
                    case "sc":
                        addToPane(jaCode, lexema, naranja);
                        break;
                    default:
                        addToPane(jaCode, lexema, negro);
                        break;
                }
            } else {
                n++;
                switch(typeLexema){
                    case "rw":
                        addToPane(jaCode, "\n"+lexema, azul);
                        break;
                    case "id":
                        addToPane(jaCode, "\n"+lexema, verde);
                        break;
                    case "sc":
                        addToPane(jaCode, "\n"+lexema, naranja);
                        break;
                    default:
                        addToPane(jaCode, "\n"+lexema, negro);
                        break;    
                }
            }
        }
    }
    
    private void addToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        File path;
        Color verde = new Color(39, 174, 96);
        switch(e.getActionCommand())
        {
            case "Abrir Archivo":
                loadFile(true);
                break;
            case "Léxico":
                analizadorLexico =  new manageTokens();
                this.tablaSimbolos = analizadorLexico.getTablaSimbolos();
                this.erroresLexicos =  analizadorLexico.getErrores();
                jaMistakes.setText("");
                if(tablaSimbolos!=null)
                    drawCode();
                if(erroresLexicos.equalsIgnoreCase("Lexicamente correcto")){
                    addToPane(jaMistakes, erroresLexicos, verde);
                    btnLexico.setIcon(iiLexico2);
                    btnSintactico.setEnabled(true);
                }else
                    addToPane(jaMistakes, erroresLexicos, Color.red);
                break;
            case "Sintáctico":
                //if(erroresLexicos.equalsIgnoreCase("Lexicamente correcto")){
                analizadorSintactico = new parser_sintactico(tablaSimbolos);
                erroresSintacticos =  analizadorSintactico.getErrores();
                jaMistakes.setText("");
                if(erroresSintacticos.equalsIgnoreCase("Sintácticamente correcto")){
                    addToPane(jaMistakes, erroresSintacticos, verde);
                    btnSintactico.setIcon(iiSintactico2);
                    btnSemantico.setEnabled(true);
                }
                else
                    addToPane(jaMistakes, erroresSintacticos, Color.red);
                break;
            case "Semántico":
                analizadorSemantico = new semantico(tablaSimbolos);
                erroresSemanticos = analizadorSemantico.getErrores();
                jaMistakes.setText("");
                if(erroresSemanticos.equalsIgnoreCase("Semánticamente correcto")){
                    addToPane(jaMistakes, erroresSemanticos, verde);
                    btnSemantico.setIcon(iiSintactico2);
                }
                else
                    addToPane(jaMistakes, erroresSemanticos, Color.red);
                break;
            case "Guardar":
                saveFile();
                break;
            case "Ver Tokens":
                if(tablaSimbolos!=null)
                    dskpanel.add(new InterfaceLexico(tablaSimbolos));
                break;
            case "Acerca de...":
                dskpanel.add(new about());
                break;
            case "C.D.U. Semántico":
                try {
                    path = new File ("src/files/cusemanticos.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
            case "C.D.U. Léxico":
                try {
                    path = new File ("src/files/culexico.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "C.D.U. Sintáctico":
                try {
                    path = new File ("src/files/cusintacticos.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Gramática":
                try {
                    path = new File ("src/files/gramatica.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
                
            case "Errores":
                try {
                    path = new File ("src/files/errores.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Mapa Símbolos":
                try {
                    path = new File ("src/files/mapa_simbolos.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Versiones":
                try {
                    path = new File ("src/files/versiones.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Bitácora":
                try {
                    path = new File ("src/files/bitacora.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Diagramas":
                try {
                    path = new File ("src/files/diagramas.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Autómatas Sintácticos":
                try {
                    path = new File ("src/files/auto_sintacticos.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Autómatas Léxicos":
                try {
                    path = new File ("src/files/auto_lexicos.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Lenguaje":
                try {
                    path = new File ("src/files/lenguaje.pdf");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Ayuda":
                dskpanel.add(new help_class());
                break;
            case "Ejemplos":
                loadFile(false);
                cargarAyudaCasosUso();
                break;
            case "Video":
                try {
                    path = new File ("src/files/video.mp4");
                    Desktop.getDesktop().open(path);
                }catch (IOException ex) {JOptionPane.showMessageDialog(null, "Error: "+ex, "PDF Error", JOptionPane.ERROR_MESSAGE);}
                break;
            case "Guardar archivo":
                mf.savePermanenteFile(jaCode.getText());
                break;
        }
    }
    
    private void cargarAyudaCasosUso(){
        String currentFile = jlTitulo.getText();
        switch(currentFile){
            case "lexicos.txt":
                dskpanel.add(new CUHelp_class("ins_lexico.dat",currentFile));
                break;
            case "semantico1.txt":
                dskpanel.add(new CUHelp_class("ins_seman1.dat",currentFile));
                break;
            case "semantico2.txt":
                dskpanel.add(new CUHelp_class("ins_seman2.dat",currentFile));
                break;
            case "semantico3.txt":
                dskpanel.add(new CUHelp_class("ins_seman3.dat",currentFile));
                break;
            case "sintactico1.txt":
                dskpanel.add(new CUHelp_class("ins_sinta1.dat",currentFile));
                break;
            case "sintactico2.txt":
                dskpanel.add(new CUHelp_class("ins_sinta2.dat",currentFile));
                break;
            case "sintactico3.txt":
                dskpanel.add(new CUHelp_class("ins_sinta3.dat",currentFile));
                break;
        }
    }
    
    private void makeTop(){
        menuBar=new JMenuBar();
        
        createArchivo();
        
        mArchivo.add(itmOpenFile);
        mArchivo.add(itmSave);
        mArchivo.add(itmTabla);
        
        createAyuda();
        
        mAyuda.add(itmLenguaje);
        mAyuda.add(itmGramatica);
        mAyuda.add(itmErrores);
        mAyuda.add(itmAutomatas);
        mAyuda.add(itmMapa);
        mAyuda.add(itmVersiones);
        mAyuda.add(itmBitacora);
        mAyuda.add(itmDiagramas);
        mAyuda.add(itmAutomatas2);
        mAyuda.add(itmCasosLex);
        mAyuda.add(itmCasosSin);
        mAyuda.add(itmCasosSem);
        mAyuda.add(itmAyuda);
        mAyuda.add(itmAbout);
        
        menuBar.add(mArchivo);
        menuBar.add(mAyuda);
    }
    
    private void createAyuda(){
        ImageIcon iiHelp = new ImageIcon("src/recursos/help.png");
        ImageIcon iiAbout = new ImageIcon("src/recursos/about.png");
        ImageIcon iiErrores = new ImageIcon("src/recursos/errores.png");
        ImageIcon iiGramatica = new ImageIcon("src/recursos/gramatica.png");
        ImageIcon iiAutomatas = new ImageIcon("src/recursos/automata.png");
        ImageIcon iiMapa = new ImageIcon("src/recursos/mapa.png");
        ImageIcon iiVersiones = new ImageIcon("src/recursos/versiones.png");
        ImageIcon iiLenguaje = new ImageIcon("src/recursos/abc.png");
        ImageIcon iiBitacora = new ImageIcon("src/recursos/bitacora.png");
        ImageIcon iiDiagramas = new ImageIcon("src/recursos/diagramas.png");
        
        mAyuda=new JMenu();
        mAyuda.setIcon(iiHelp);
        mAyuda.setText("Ayuda");
        
        itmLenguaje=new JMenuItem();
        itmLenguaje.setIcon(iiLenguaje);
        itmLenguaje.setText("Lenguaje");
        itmLenguaje.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        itmLenguaje.addActionListener(this);
        
        itmGramatica=new JMenuItem();
        itmGramatica.setIcon(iiGramatica);
        itmGramatica.setText("Gramática");
        itmGramatica.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        itmGramatica.addActionListener(this);
        
        itmErrores=new JMenuItem();
        itmErrores.setIcon(iiErrores);
        itmErrores.setText("Errores");
        itmErrores.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        itmErrores.addActionListener(this);
        
        itmAutomatas=new JMenuItem();
        itmAutomatas.setIcon(iiAutomatas);
        itmAutomatas.setText("Autómatas Léxicos");
        itmAutomatas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        itmAutomatas.addActionListener(this);
        
        itmAutomatas2=new JMenuItem();
        itmAutomatas2.setIcon(iiAutomatas);
        itmAutomatas2.setText("Autómatas Sintácticos");
        itmAutomatas2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        itmAutomatas2.addActionListener(this);
        
        itmMapa=new JMenuItem();
        itmMapa.setIcon(iiMapa);
        itmMapa.setText("Mapa Símbolos");
        itmMapa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        itmMapa.addActionListener(this);
        
        itmVersiones=new JMenuItem();
        itmVersiones.setIcon(iiVersiones);
        itmVersiones.setText("Versiones");
        itmVersiones.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        itmVersiones.addActionListener(this);
        
        itmBitacora=new JMenuItem();
        itmBitacora.setIcon(iiBitacora);
        itmBitacora.setText("Bitácora");
        itmBitacora.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        itmBitacora.addActionListener(this);
        
        itmDiagramas=new JMenuItem();
        itmDiagramas.setIcon(iiDiagramas);
        itmDiagramas.setText("Diagramas");
        itmDiagramas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        itmDiagramas.addActionListener(this);
        
        itmCasosLex=new JMenuItem();
        itmCasosLex.setIcon(iiLexico);
        itmCasosLex.setText("C.D.U. Léxico");
        itmCasosLex.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        itmCasosLex.addActionListener(this);
        
        itmCasosSin=new JMenuItem();
        itmCasosSin.setIcon(iiSintactico2);
        itmCasosSin.setText("C.D.U. Sintáctico");
        itmCasosSin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        itmCasosSin.addActionListener(this);
        
        itmCasosSem=new JMenuItem();
        itmCasosSem.setIcon(iiSemantico2);
        itmCasosSem.setText("C.D.U. Semántico");
        itmCasosSem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        itmCasosSem.addActionListener(this);
        
        itmAyuda=new JMenuItem();
        itmAyuda.setIcon(iiHelp);
        itmAyuda.setText("Ayuda");
        itmAyuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        itmAyuda.addActionListener(this);
        
        itmAbout=new JMenuItem();
        itmAbout.setIcon(iiAbout);
        itmAbout.setText("Acerca de...");
        itmAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        itmAbout.addActionListener(this);
    }
    
    private void createArchivo(){
        ImageIcon iiArchivo = new ImageIcon("src/recursos/archivo.png");
        ImageIcon iiOpenFile = new ImageIcon("src/recursos/openFile.png");
        ImageIcon iiTabla = new ImageIcon("src/recursos/tabla.png");
        
        mArchivo=new JMenu();
        mArchivo.setIcon(iiArchivo);
        mArchivo.setText("Archivo");
        
        itmOpenFile=new JMenuItem();
        itmOpenFile.setIcon(iiOpenFile);
        itmOpenFile.setText("Abrir Archivo");
        itmOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        itmOpenFile.addActionListener(this);
        
        itmSave=new JMenuItem();
        itmSave.setIcon(new ImageIcon("src/recursos/guardar.png"));
        itmSave.setText("Guardar archivo");
        itmSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        itmSave.addActionListener(this);
        
        itmTabla=new JMenuItem();
        itmTabla.setIcon(iiTabla);
        itmTabla.setText("Ver Tokens");
        itmTabla.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        itmTabla.addActionListener(this);
    }
    
    private void searchSelected(String lexema){
        boolean bandera = false;
        MyObject extracto = null;
        if(tablaSimbolos!=null){
            for(MyObject objeto: tablaSimbolos)
                if(objeto.lexema.equals(lexema)){
                    bandera = true;
                    extracto = objeto;
                    break;
                }
            if(bandera){
                if(extracto.typeLexema.equals("rw")){
                    String mensaje = diccionary(lexema);
                    JOptionPane.showMessageDialog(null,mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                if(extracto.id == 12)
                    JOptionPane.showMessageDialog(null,"Lexema: "+ lexema+"\nTipo: Identificador \n Dirección: "+extracto.direccion, "Información", JOptionPane.INFORMATION_MESSAGE);
                
            }else
                System.out.println("Lexema no encontrado"+lexema);
        }else
            JOptionPane.showMessageDialog(null, "Si desea información primero debe generar la tabla de símbolos, ésta se genera luego de realizar el análisis léxico.\nGracias", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String diccionary(String lexema){
        String mensaje = "";
        switch(lexema){
            case "inicio":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          inicio{ /*bloque de código*/ }\n\nInformación:\nPalabra que denota el inicio del método a analizar, debe ser la primera instrucción.";
                break;
            case "si":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          si(condicion){ /*bloque de código*/ }sino{/*bloque de código*/}\n\nInformación:\nPalabra reservada para la aplicación de un bloque de código a ejecutar cuando cierta condicón es verdadera, la parte 'sino' es opcional.";
                break;
            case "sino":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          si(condicion){ /*bloque de código*/ }sino{/*bloque de código*/}\n\nInformación:\nPalabra reservada para la aplicación de un bloque de código a ejecutar cuando cierta condicón no se cumple en el 'si', un 'sino' no puede existir sin un 'si' previo.";
                break;
            case "mientras":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          mientras(condicion){ /*bloque de código*/ }\n\nInformación:\nPalabra reservada para ejecutar un bloque de código repetidas veces hasta que la condición sea falsa.";
                break;
            case "ent":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          ent <Nombre de variable>\n\nInformación:\nPalabra reservada para declarar variables del tipo entero.";
                break;
            case "cad":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          cad <Nombre de variable>\n\nInformación:\nPalabra reservada para declarar variables del tipo cadena de texto.";
                break;
            case "imprime":
                mensaje = "Lexema: "+lexema+"\n\nTipo: Palabra reservada\n\nEstructura:\n          imprime(<cadena o variable tipo candena> | <cadena>.<variable>)\n\nInformación:\nPalabra reservada para imprimir a pantalla cierto mensaje.";
                break;
        }
        
        return mensaje;
    }
    
    private void makeBody(){
        jaCode = new JTextPane();
        //jaCode.setFont(new Font("Lucila Console", Font.PLAIN, 12));
        jaMistakes = new JTextPane();
        jaMistakes.setForeground(Color.blue);
        tlnLines = new TextLineNumber(jaCode);
        tlnLines.setCurrentLineForeground(Color.blue);
        
        jaCode.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                if(!(e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()== KeyEvent.VK_DOWN 
                        || e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()== KeyEvent.VK_RIGHT))
                {
                    manageButtons(1);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        addToPane(jaCode, "", Color.black);
        
        jaCode.addMouseListener(new MouseAdapter() {
           @Override
           public void mousePressed(MouseEvent e) {
                  if (e.getButton() == MouseEvent.BUTTON3) {
                     selected =jaCode.getSelectedText();
                     if(selected != null)
                        searchSelected(selected);
                  }
           }
        });
        
        jlTitulo = new JLabel("Titulo de archivo");
        jlTitulo.setSize(390,50);
        jlTitulo.setForeground(Color.WHITE);
        jlTitulo.setFont( new Font("Serif", Font.BOLD, 16));
        jlTitulo.setLocation(40, 5);
        
        spLine = new JScrollPane();
        spLine.setRowHeaderView(tlnLines);
        spLine.setSize(80,350);
        spLine.setLocation(40,40);
        
        spCode = new JScrollPane(jaCode);
        spCode.setSize(595,350);
        spCode.setLocation(125,40);
      
        spMistakes = new JScrollPane(jaMistakes);
        spMistakes.setSize(680,80);
        spMistakes.setLocation(40,390);
        
        btnGuardar=new JButton("Guardar",new ImageIcon("src/recursos/guardar.png"));
        btnGuardar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(SwingConstants.BOTTOM) ;
        btnGuardar.setVerticalAlignment(SwingConstants.CENTER);
        btnGuardar.setSize(90, 70);
        btnGuardar.setLocation(60, 480);
        btnGuardar.addActionListener(this);
        
        btnLexico=new JButton("Léxico",iiLexico);
        btnLexico.setHorizontalTextPosition(SwingConstants.CENTER);
        btnLexico.setVerticalTextPosition(SwingConstants.BOTTOM) ;
        btnLexico.setVerticalAlignment(SwingConstants.CENTER);
        btnLexico.setSize(90, 70);
        btnLexico.setLocation(165, 480);
        btnLexico.addActionListener(this);
        btnLexico.setEnabled(false);
        
        btnSintactico=new JButton("Sintáctico",iiSintactico);
        btnSintactico.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSintactico.setVerticalTextPosition(SwingConstants.BOTTOM) ;
        btnSintactico.setVerticalAlignment(SwingConstants.CENTER);
        btnSintactico.setSize(90, 70);
        btnSintactico.setLocation(270, 480);
        btnSintactico.addActionListener(this);
        btnSintactico.setEnabled(false);
        
        btnSemantico=new JButton("Semántico",iiSemantico);
        btnSemantico.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSemantico.setVerticalTextPosition(SwingConstants.BOTTOM) ;
        btnSemantico.setVerticalAlignment(SwingConstants.CENTER);
        btnSemantico.setSize(90, 70);
        btnSemantico.setLocation(375, 480);
        btnSemantico.addActionListener(this);
        btnSemantico.setEnabled(false);
        
        btnEjemplos=new JButton("Ejemplos",iiExample);
        btnEjemplos.setHorizontalTextPosition(SwingConstants.CENTER);
        btnEjemplos.setVerticalTextPosition(SwingConstants.BOTTOM) ;
        btnEjemplos.setVerticalAlignment(SwingConstants.CENTER);
        btnEjemplos.setSize(90, 70);
        btnEjemplos.setLocation(480, 480);
        btnEjemplos.addActionListener(this);
        
        btnVideo=new JButton("Video",iiVideo);
        btnVideo.setHorizontalTextPosition(SwingConstants.CENTER);
        btnVideo.setVerticalTextPosition(SwingConstants.BOTTOM) ;
        btnVideo.setVerticalAlignment(SwingConstants.CENTER);
        btnVideo.setSize(90, 70);
        btnVideo.setLocation(585, 480);
        btnVideo.addActionListener(this);
        
        add(jlTitulo);
        add(spLine);
        add(spCode);
        add(spMistakes);
        add(btnGuardar);
        add(btnLexico);
        add(btnSintactico);
        add(btnSemantico);
        add(btnEjemplos);
        add(btnVideo);
    }
}