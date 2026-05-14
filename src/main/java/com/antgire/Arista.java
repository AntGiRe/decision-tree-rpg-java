package com.antgire;

public class Arista {
    private Localizacion origen;
    private Localizacion destino;
    private int peso; // Coste de energía

    public Arista(Localizacion origen, Localizacion destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    // Getters
    public Localizacion getOrigen() { return origen; }
    public Localizacion getDestino() { return destino; }
    public int getPeso() { return peso; }
}
