package com.antgire;

public class Item {
    private String id;
    private String nombre;

    public Item(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
}