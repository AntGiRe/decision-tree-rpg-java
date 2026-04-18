package com.antgire;

import java.util.ArrayList;

public class Jugador {
    String nombre;
    int vida;
    int energia;
    int oro;
    ArrayList<String> inventario;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.vida = 100;
        this.energia = 100;
        this.oro = 0;
        this.inventario = new ArrayList<>();
    }

    public void modificarVida(int cantidad) {
        vida += cantidad;
    }

    public void modificarEnergia(int cantidad) {
        energia += cantidad;
    }

    public void añadirObjeto(String obj) {
        inventario.add(obj);
    }

    public void mostrarEstado() {
        System.out.println("Vida: " + vida);
        System.out.println("Energia: " + energia);
        System.out.println("Oro: " + oro);
        System.out.println("Inventario: " + inventario);
    }
}
