package com.antgire;

public class Decision {
    int id;
    String titulo;
    String descripcion;
    String tipoNodo; // Deicison o resultado

    int cambioVida;
    int cambioEnergia;
    int recompensa; // oro

    public Decision(int id, String titulo, String descripcion, String tipoNodo,
                    int cambioVida, int cambioEnergia, int recompensa) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoNodo = tipoNodo;
        this.cambioVida = cambioVida;
        this.cambioEnergia = cambioEnergia;
        this.recompensa = recompensa;
    }
}
