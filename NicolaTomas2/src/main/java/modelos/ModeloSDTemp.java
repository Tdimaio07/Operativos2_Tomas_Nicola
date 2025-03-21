package modelos;

import micelaneos.Node;

public class ModeloSDTemp {
    int capacidad;
    int bloquesLibres;
    Node[] bloques;
    String[][] tabla;

    public ModeloSDTemp(int capacidad, int bloquesLibres, Node[] bloques, String[][] tabla) {
        this.capacidad = capacidad;
        this.bloquesLibres = bloquesLibres;
        this.bloques = bloques;
        this.tabla = tabla;
    }
}