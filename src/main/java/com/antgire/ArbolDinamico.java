package com.antgire;

import java.util.LinkedList;
import java.util.Queue;

public class ArbolDinamico implements ArbolEvento<Nodo> {
    private Nodo raiz;

    @Override
    public Nodo insertarRaiz(Decision d) {
        raiz = new Nodo(d);
        return raiz;
    }

    @Override
    public Nodo insertarIzquierdo(Nodo padre, Decision d) {
        if (padre == null) return null;
        padre.izquierdo = new Nodo(d);
        return padre.izquierdo;
    }

    @Override
    public Nodo insertarDerecho(Nodo padre, Decision d) {
        if (padre == null) return null;
        padre.derecho = new Nodo(d);
        return padre.derecho;
    }

    @Override
    public Nodo hijoIzquierdo(Nodo nodo) {
        return nodo == null ? null : nodo.izquierdo;
    }

    @Override
    public Nodo hijoDerecho(Nodo nodo) {
        return nodo == null ? null : nodo.derecho;
    }

    @Override
    public boolean esHoja(Nodo nodo) {
        return nodo != null && nodo.izquierdo == null && nodo.derecho == null;
    }

    @Override
    public Decision obtenerDecision(Nodo nodo) {
        return nodo == null ? null : nodo.dato;
    }

    public Nodo buscar(Nodo actual, int id) {
        if (actual == null) return null;
        if (actual.dato.id == id) return actual;

        Nodo izq = buscar(actual.izquierdo, id);
        if (izq != null) return izq;

        return buscar(actual.derecho, id);
    }


    @Override
    public void porNiveles(Nodo nodo) {
        if (nodo == null) return;

        Queue<Nodo> cola = new LinkedList<>();
        cola.add(nodo);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            System.out.println(actual.dato.titulo);

            if (actual.izquierdo != null) cola.add(actual.izquierdo);
            if (actual.derecho != null) cola.add(actual.derecho);
        }
    }

    // Recorridos
    @Override
    public void preorden(Nodo n) {
        if (n != null) {
            System.out.println(n.dato.titulo);
            preorden(n.izquierdo);
            preorden(n.derecho);
        }
    }

    @Override
    public void inorden(Nodo n) {
        if (n != null) {
            inorden(n.izquierdo);
            System.out.println(n.dato.titulo);
            inorden(n.derecho);
        }
    }

    @Override
    public void postorden(Nodo n) {
        if (n != null) {
            postorden(n.izquierdo);
            postorden(n.derecho);
            System.out.println(n.dato.titulo);
        }
    }

    // Propiedades
    @Override
    public int contarNodos(Nodo n) {
        if (n == null) return 0;
        return 1 + contarNodos(n.izquierdo) + contarNodos(n.derecho);
    }

    @Override
    public int contarHojas(Nodo n) {
        if (n == null) return 0;
        if (n.izquierdo == null && n.derecho == null) return 1;
        return contarHojas(n.izquierdo) + contarHojas(n.derecho);
    }

    @Override
    public int altura(Nodo n) {
        if (n == null) return 0;
        return 1 + Math.max(altura(n.izquierdo), altura(n.derecho));
    }
}