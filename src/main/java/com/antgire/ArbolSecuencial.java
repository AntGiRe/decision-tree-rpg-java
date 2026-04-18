package com.antgire;

public class ArbolSecuencial implements ArbolEvento<Integer> {

    private final Decision[] arbol;
    private final int sizeMax;

    public ArbolSecuencial(int size) {
        this.sizeMax = size;
        arbol = new Decision[sizeMax];
    }

    @Override
    public Integer insertarRaiz(Decision d) {
        arbol[0] = d;
        return 0;
    }

    @Override
    public Integer insertarIzquierdo(Integer indicePadre, Decision d) {
        if (indicePadre == null) return null;
        int indice = 2 * indicePadre + 1;
        if (indice < sizeMax) {
            arbol[indice] = d;
            return indice;
        }
        return null;
    }

    @Override
    public Integer insertarDerecho(Integer indicePadre, Decision d) {
        if (indicePadre == null) return null;
        int indice = 2 * indicePadre + 2;
        if (indice < sizeMax) {
            arbol[indice] = d;
            return indice;
        }
        return null;
    }

    @Override
    public Integer hijoIzquierdo(Integer nodo) {
        if (nodo == null) return null;
        int indice = 2 * nodo + 1;
        return indice < sizeMax && arbol[indice] != null ? indice : null;
    }

    @Override
    public Integer hijoDerecho(Integer nodo) {
        if (nodo == null) return null;
        int indice = 2 * nodo + 2;
        return indice < sizeMax && arbol[indice] != null ? indice : null;
    }

    @Override
    public boolean esHoja(Integer i) {
        if (i == null || i >= sizeMax || arbol[i] == null) return false;

        int izq = 2 * i + 1;
        int der = 2 * i + 2;

        return (izq >= sizeMax || arbol[izq] == null) &&
                (der >= sizeMax || arbol[der] == null);
    }

    @Override
    public Decision obtenerDecision(Integer nodo) {
        if (nodo == null || nodo >= sizeMax) return null;
        return arbol[nodo];
    }

    public int buscar(int id) {
        for (int i = 0; i < sizeMax; i++) {
            if (arbol[i] != null && arbol[i].id == id) {
                return i;
            }
        }
        return -1;
    }


    // Recorridos
    @Override
    public void preorden(Integer i) {
        if (i == null || i >= sizeMax || arbol[i] == null) return;

        System.out.println(arbol[i].titulo);
        preorden(2 * i + 1);
        preorden(2 * i + 2);
    }

    @Override
    public void inorden(Integer i) {
        if (i == null || i >= sizeMax || arbol[i] == null) return;

        inorden(2 * i + 1);
        System.out.println(arbol[i].titulo);
        inorden(2 * i + 2);
    }

    @Override
    public void postorden(Integer i) {
        if (i == null || i >= sizeMax || arbol[i] == null) return;

        postorden(2 * i + 1);
        postorden(2 * i + 2);
        System.out.println(arbol[i].titulo);
    }

    @Override
    public void porNiveles(Integer nodo) {
        if (nodo == null || nodo >= sizeMax || arbol[nodo] == null) return;

        java.util.Queue<Integer> cola = new java.util.ArrayDeque<>();
        cola.add(nodo);

        while (!cola.isEmpty()) {
            Integer actual = cola.poll();
            System.out.println(arbol[actual].titulo);

            Integer izquierdo = hijoIzquierdo(actual);
            Integer derecho = hijoDerecho(actual);

            if (izquierdo != null) cola.add(izquierdo);
            if (derecho != null) cola.add(derecho);
        }
    }

    @Override
    public int contarNodos(Integer nodo) {
        if (nodo == null || nodo >= sizeMax || arbol[nodo] == null) return 0;
        return 1 + contarNodos(2 * nodo + 1) + contarNodos(2 * nodo + 2);
    }

    @Override
    public int contarHojas(Integer nodo) {
        if (nodo == null || nodo >= sizeMax || arbol[nodo] == null) return 0;
        if (esHoja(nodo)) return 1;
        return contarHojas(2 * nodo + 1) + contarHojas(2 * nodo + 2);
    }

    @Override
    public int altura(Integer nodo) {
        if (nodo == null || nodo >= sizeMax || arbol[nodo] == null) return 0;
        return 1 + Math.max(altura(2 * nodo + 1), altura(2 * nodo + 2));
    }
}