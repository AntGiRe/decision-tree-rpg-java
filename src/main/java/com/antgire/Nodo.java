package com.antgire;

public class Nodo {
    Decision dato;
    Nodo izquierdo;
    Nodo derecho;

    public Nodo(Decision dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
    }
}