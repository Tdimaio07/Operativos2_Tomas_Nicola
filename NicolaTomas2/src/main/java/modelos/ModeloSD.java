package modelos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect.Type;
import micelaneos.*;

public class ModeloSD {
    int capacidad;
    int bloquesLibres;
    Node[] bloques;
    List tabla;
      
    public ModeloSD(int bloquesLibres, int capacidad, Node[] nodos, List lista) {
        this.bloquesLibres = bloquesLibres;
        this.capacidad = capacidad;
        bloques = nodos;
        tabla = lista;
    }
     // Method to save the ModeloSD object as JSON
        public void saveAsJson(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            ModeloSDTemp temp = new ModeloSDTemp(capacidad, bloquesLibres, bloques, convertListToArray(tabla));
            gson.toJson(temp, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModeloSD readFromJson(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader(filename)) {
            java.lang.reflect.Type modeloSDTempType = new TypeToken<ModeloSDTemp>() {}.getType();
            ModeloSDTemp temp = gson.fromJson(reader, modeloSDTempType);
            List tabla = convertArrayToList(temp.tabla);
            return new ModeloSD(temp.bloquesLibres, temp.capacidad, temp.bloques, tabla);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String[][] convertListToArray(List list) {
        int size = list.getSize();
        String[][] array = new String[size][];
        NodoList current = list.getHead();
        int index = 0;
        while (current != null) {
            array[index] = (String[]) current.getValue();
            current = current.getpNext();
            index++;
        }
        return array;
    }

    private static List convertArrayToList(String[][] array) {
        List list = new List();
        for (String[] item : array) {
            list.appendLast(item);
        }
        return list;
    }
    
    public Node[] obtenerBloques() {
        return bloques;
    }
    
    public void borrarArchivo(String nombreArchivo) {
        for (int i = 0; i < capacidad; i++) {
            Node nodo = bloques[i];
            if(nodo.getFile() != null) {
                if(nodo.getFile().equals(nombreArchivo)) {
                    nodo.setFile(null);
                    nodo.setNext(-1);
                    bloquesLibres++;
                }
            }
        }
        NodoList siguienteNodo = tabla.getHead();
        while(siguienteNodo != null) {
            if(((String[])siguienteNodo.getValue())[0].equals(nombreArchivo)) {
                tabla.delete(siguienteNodo);
                break;
            }
            siguienteNodo = siguienteNodo.getpNext();
        }
    }

    public void crearArchivo(int numeroBloques, String nombreArchivo) throws Exception {
        if(bloquesLibres < numeroBloques) {
            throw new Exception("no espacio");
        } else if(this.nombreExiste(nombreArchivo)) {
            throw new Exception("es un archivo");
        } else {
            asignarBloques(numeroBloques, nombreArchivo);
        }
    }

    public void actualizarArchivo(String nombreArchivo, String nuevoNombreArchivo) throws Exception {
        if(this.nombreExiste(nuevoNombreArchivo)) {
            throw new Exception("Ya existe ese archivo");
        }
        NodoList siguienteNodo = tabla.getHead();
        while(siguienteNodo != null) {
            if(((String[])siguienteNodo.getValue())[0].equals(nombreArchivo)) {
                ((String[]) siguienteNodo.getValue())[0] = nuevoNombreArchivo;
                break;
            }
        }
        int i = inicioArchivo(nombreArchivo); 
        while(i != -1) {
            bloques[i].setFile(nuevoNombreArchivo);
            i = bloques[i].getNext();
        }
    }

    public boolean esArchivo(String nombreArchivo) {
        return obtenerBloques(nombreArchivo) > 0;
    }
    
    public int inicioArchivo(String nombreArchivo) {
        for (int i = 0; i < capacidad; i++) {
            if(bloques[i].getFile() != null) {
                if(bloques[i].getFile().equals(nombreArchivo)) return i;
            }
        }
        return -1;
    }
    
    private int obtenerBloques(String nombreArchivo) {
        int cantidad = 0;
        for (int i = 0; i < capacidad; i++) {
            String siguiente = bloques[i].getFile();
            if(siguiente != null && siguiente.equals(nombreArchivo)) {
                cantidad++;
            }
        }
        return cantidad;
    }
    
    private boolean nombreExiste(String nombreArchivo) {
        for (int i = 0; i < capacidad; i++) {
            String siguiente = bloques[i].getFile();
            if(siguiente != null && siguiente.equals(nombreArchivo)) {
                return true;
            }
        }
        return false;
    }
    
    private int siguienteBloqueLibre() {
        for (int i = 0; i < capacidad; i++) {
            String siguiente = bloques[i].getFile();
            if(siguiente == null) return i;
        }
        return -1;
    }

    private void asignarBloques(int numeroBloques, String nombreArchivo) {
        for (int i = 0; i < numeroBloques; i++) {
            int j = this.siguienteBloqueLibre();
            if(i == 0) {
                String[] array = {nombreArchivo, Integer.toString(j), Integer.toString(numeroBloques)};
                tabla.appendLast(array);
            }
            Node siguiente = bloques[j];
            siguiente.setFile(nombreArchivo);
            if(i != numeroBloques - 1) {
                int siguienteLibre = this.siguienteBloqueLibre();
                siguiente.setNext(siguienteLibre);
            }
            bloquesLibres--;
        }
    }

    public List getTabla() {
        return tabla;
    }
    
}
