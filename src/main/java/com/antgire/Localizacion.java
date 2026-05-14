package com.antgire;

public class Localizacion {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean visitada;
    private ArbolEvento<?> arbolEvento;

    public Localizacion(int id, String nombre, String descripcion, ArbolEvento<?> arbolEvento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.visitada = false;
        this.arbolEvento = arbolEvento;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isVisitada() { return visitada; }
    public void setVisitada(boolean visitada) { this.visitada = visitada; }
    public ArbolEvento<?> getArbolEvento() { return arbolEvento; }
}
