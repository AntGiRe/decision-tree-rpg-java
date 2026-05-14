package com.antgire;

public class Jugador {
    String nombre;
    int vida;
    int energia;
    int oro;
    private final Inventario inventario;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.vida = 100;
        this.energia = 100;
        this.oro = 0;
        this.inventario = new Inventario(16);
    }

    public void modificarVida(int cantidad) {
        vida += cantidad;
    }

    public void modificarEnergia(int cantidad) {
        energia += cantidad;
    }

    public void mostrarEstado() {
        System.out.println("Vida: " + vida);
        System.out.println("Energia: " + energia);
        System.out.println("Oro: " + oro);
        inventario.mostrarContenido();
        inventario.mostrarEstadisticas();
    }

    public Inventario getInventario() {
        return inventario;
    }
}
