package com.antgire;

public class Inventario {

    // Nodo para resolver colisiones por encadenamiento
    private class NodoHash {
        String clave;
        Item valor;
        NodoHash siguiente;

        NodoHash(String clave, Item valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private NodoHash[] tabla;
    private int tamanoTabla;
    private int numElementos;
    private int numColisiones;

    public Inventario(int tamanoInicial) {
        this.tamanoTabla = tamanoInicial;
        this.tabla = new NodoHash[tamanoInicial];
        this.numElementos = 0;
        this.numColisiones = 0;
    }

    // Función Hash obligatoria
    private int funcionHash(String clave) {
        int hash = 0;
        for (int i = 0; i < clave.length(); i++) {
            hash = 31 * hash + clave.charAt(i); // Algoritmo polinomial clásico estándar
        }
        return Math.abs(hash) % tamanoTabla;
    }

    public void insertar(String clave, Item valor) {
        int indice = funcionHash(clave);
        NodoHash nuevo = new NodoHash(clave, valor);

        if (tabla[indice] == null) {
            tabla[indice] = nuevo;
        } else {
            // Existe colisión en esta celda
            numColisiones++;
            NodoHash actual = tabla[indice];
            // Si la clave ya existe, actualizamos el valor para evitar duplicados
            while (actual != null) {
                if (actual.clave.equals(clave)) {
                    actual.valor = valor;
                    return;
                }
                if (actual.siguiente == null) break;
                actual = actual.siguiente;
            }
            // Insertamos al final de la lista enlazada de la celda
            actual.siguiente = nuevo;
        }
        numElementos++;
    }

    public Item buscar(String clave) {
        int indice = funcionHash(clave);
        NodoHash actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null; // No encontrado
    }

    public void eliminar(String clave) {
        int indice = funcionHash(clave);
        NodoHash actual = tabla[indice];
        NodoHash anterior = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                numElementos--;
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
    }

    public void mostrarContenido() {
        System.out.println("--- Contenido del Inventario ---");
        for (int i = 0; i < tamanoTabla; i++) {
            NodoHash actual = tabla[i];
            if (actual != null) {
                System.out.print("Celda " + i + ": ");
                while (actual != null) {
                    System.out.print("[" + actual.valor.getNombre() + " (ID: " + actual.clave + ")]");
                    if (actual.siguiente != null) {
                        System.out.print(" -> ");
                    }
                    actual = actual.siguiente;
                }
                System.out.println();
            }
        }
    }

    // Estadísticas requeridas
    public float calcularFactorCarga() {
        return (float) numElementos / tamanoTabla;
    }

    public void mostrarEstadisticas() {
        System.out.println("--- Estadísticas de la Tabla Hash ---");
        System.out.println("Tamaño total (Celdas): " + tamanoTabla);
        System.out.println("Número de objetos guardados: " + numElementos);
        System.out.println("Factor de carga actual: " + calcularFactorCarga());
        System.out.println("Número de colisiones totales: " + numColisiones);
    }

    public int size() { return numElementos; }
}