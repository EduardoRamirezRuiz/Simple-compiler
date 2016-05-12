package tokenslexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class manageFile {
    //Crear un objeto FileChooser
    private final JFileChooser FileChooser;
    private File archivoElegido = null;    
    private FileReader filereader = null;
    
    public manageFile() {
        FileChooser = new JFileChooser();
    }
    public manageFile(String rutaString) {
        FileChooser = new JFileChooser();
        FileChooser.setCurrentDirectory(new File(rutaString));
    }
    private void openFile(){
        //filtro de archivos
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        FileChooser.setFileFilter(filtro);
        //ventana para seleccionar archivo
        int respuesta = FileChooser.showOpenDialog(null);
        //Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION)
        {
            //Crear un objeto File con el archivo elegido
            archivoElegido = FileChooser.getSelectedFile();
            
            try {
                filereader = new FileReader(archivoElegido);
            } catch (FileNotFoundException ex) {
                System.out.println(""+ ex);
            }
        }
    }
    
    public String getArchivoContent(String nombre) throws Exception
    {
        String contenido = openFileSpecific("src/files/"+nombre);
        if(!contenido.equalsIgnoreCase(""))
            return contenido;
        else
            throw new Exception("Error al abrir el archivo");
    }
    
    public String getExampleHelp(String nombre) throws Exception
    {
        String contenido = openFileSpecific("src/casos_de_uso/"+nombre);
        if(!contenido.equalsIgnoreCase(""))
            return contenido;
        else
            throw new Exception("Error al abrir el archivo");
    }
    
    private String openFileSpecific(String ruta) {
        String linea = "";
        String contenido = "";
        //System.out.println(ruta);
        File file = new File(ruta);
        try {
            FileReader f = new FileReader(file);
            FileInputStream fis = new FileInputStream(ruta);
            InputStreamReader is = new InputStreamReader(fis, "ISO-8859-1");
            BufferedReader b = new BufferedReader(is);
            while((linea = b.readLine())!=null) {
                contenido += linea+"\n";
            }
            b.close();
        } catch (Exception e) {
            System.err.println("Error interno, archivo no encontrado");
        }
        
      return contenido;
    }

    public FileReader getArchivoElegido() throws Exception
    {
        openFile();
        if(archivoElegido!=null)
            return filereader;
        else
            throw new Exception("Error al abrir el archivo");
    }

    public String getArchivoName() {
        return archivoElegido.getName();
    }
    
    public boolean saveFile(String code){
        
        File file;
        FileWriter fw;
        BufferedWriter bw;
        PrintWriter pw;
        file = new File("src/code.dat");
        Boolean bandera = false;
        try{
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);  
            pw.write(code);//escribimos en el archivo
            pw.close();
            bw.close();
            bandera = true;
        }catch(IOException ex){bandera=false;};

        return bandera;
    }
    
    public void savePermanenteFile(String texto)
    {
        try{
            String nombre="";
            JFileChooser file=new JFileChooser();
            file.showSaveDialog(null);
            File guarda =file.getSelectedFile();

            if(guarda !=null)
            {
             /*guardamos el archivo y le damos el formato directamente,
              * si queremos que se guarde en formato doc lo definimos como .doc*/
              FileWriter  save=new FileWriter(guarda+".txt");
              save.write(texto);
              save.close();
              JOptionPane.showMessageDialog(null,
                   "El archivo se a guardado Exitosamente",
                       "Información",JOptionPane.INFORMATION_MESSAGE);
              }
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(null,
            "Su archivo no se ha guardado",
               "Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }
}
