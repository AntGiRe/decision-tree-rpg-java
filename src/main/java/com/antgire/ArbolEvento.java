package com.antgire;

public interface ArbolEvento<T> {
    T insertarRaiz(Decision decision);

    T insertarIzquierdo(T padre, Decision decision);

    T insertarDerecho(T padre, Decision decision);

    T hijoIzquierdo(T nodo);

    T hijoDerecho(T nodo);

    boolean esHoja(T nodo);

    Decision obtenerDecision(T nodo);

    void preorden(T nodo);

    void inorden(T nodo);

    void postorden(T nodo);

    void porNiveles(T nodo);

    int contarNodos(T nodo);

    int contarHojas(T nodo);

    int altura(T nodo);
}
