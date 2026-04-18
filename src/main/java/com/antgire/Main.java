package com.antgire;

import java.util.Scanner;

public class Main {

    private static final int TAMANO_ARBOL_SECUENCIAL = 15;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Nombre del jugador
        System.out.print("Introduce el nombre del jugador: ");
        String nombre = sc.nextLine();

        Jugador jugador = new Jugador(nombre);

        // 2. Elegir tipo de árbol
        System.out.println("\nElige el tipo de sistema:");
        System.out.println("1. Árbol Dinámico");
        System.out.println("2. Árbol Secuencial");

        int tipo = leerEntero(sc, "Selecciona una opción: ");

        switch (tipo) {
            case 1 -> {
                System.out.println("\nSistema seleccionado: Árbol Dinámico");
                ejecutarPartida(new ArbolDinamico(), jugador, sc);
            }
            case 2 -> {
                System.out.println("\nSistema seleccionado: Árbol Secuencial");
                ejecutarPartida(new ArbolSecuencial(TAMANO_ARBOL_SECUENCIAL), jugador, sc);
            }
            default -> System.out.println("Tipo de sistema no válido.");
        }
    }

    private static <T> void ejecutarPartida(ArbolEvento<T> arbol, Jugador jugador, Scanner sc) {
        System.out.println("=== EVENTO 1: VIAJERO ===");
        T evento1 = crearEventoViajero(arbol);
        ejecutarEvento(arbol, evento1, jugador, sc);

        System.out.println("\n=== EVENTO 2: CAMINO ===");
        T evento2 = crearEventoCamino(arbol);
        ejecutarEvento(arbol, evento2, jugador, sc);

        System.out.println("\n=== EVENTO 3: BANDIDO ===");
        T evento3 = crearEventoBandido(arbol);
        ejecutarEvento(arbol, evento3, jugador, sc);

        System.out.println("\n--- RECORRIDOS ---");
        System.out.println("\nPreorden:");
        arbol.preorden(evento3);

        System.out.println("\nInorden:");
        arbol.inorden(evento3);

        System.out.println("\nPostorden:");
        arbol.postorden(evento3);

        System.out.println("\nPor niveles (anchura):");
        arbol.porNiveles(evento3);

        System.out.println("\n--- PROPIEDADES ---");
        System.out.println("Nodos: " + arbol.contarNodos(evento3));
        System.out.println("Hojas: " + arbol.contarHojas(evento3));
        System.out.println("Altura: " + arbol.altura(evento3));

        System.out.println("\n=== ESTADO FINAL DEL JUGADOR ===");
        jugador.mostrarEstado();
    }

    private static <T> T crearEventoViajero(ArbolEvento<T> arbol) {
        T raiz = arbol.insertarRaiz(new Decision(10, "Viajero", "Te encuentras con un viajero extraño...", "DECISION", 0, 0, 0));
        T hablar = arbol.insertarIzquierdo(raiz, new Decision(11, "Hablar", "Hablas con él y te ofrece ayuda.", "DECISION", 0, 0, 0));
        arbol.insertarDerecho(raiz, new Decision(12, "Ignorar", "Lo ignoras y sigues tu camino.", "RESULTADO", 0, 0, 0));
        arbol.insertarIzquierdo(hablar, new Decision(13, "Aceptar su ayuda", "Te da una poción. +20 vida", "RESULTADO", 20, 0, 0));
        arbol.insertarDerecho(hablar, new Decision(14, "Mantener distancia", "Era un ladrón. -10 oro", "RESULTADO", 0, 0, -10));
        return raiz;
    }

    private static <T> T crearEventoCamino(ArbolEvento<T> arbol) {
        T raiz = arbol.insertarRaiz(new Decision(20, "Cruce", "Llegas a un cruce de caminos...", "DECISION", 0, 0, 0));
        T bosque = arbol.insertarIzquierdo(raiz, new Decision(21, "Bosque", "Te adentras en el bosque oscuro.", "DECISION", -5, -10, 0));
        arbol.insertarDerecho(raiz, new Decision(22, "Camino seguro", "Sigues un camino seguro.", "RESULTADO", 0, 0, 10));
        arbol.insertarIzquierdo(bosque, new Decision(23, "Seguir un ruido", "Un lobo te ataca. -30 vida", "RESULTADO", -30, 0, 0));
        arbol.insertarDerecho(bosque, new Decision(24, "Explorar el claro", "Encuentras oro oculto +50", "RESULTADO", 0, 0, 50));
        return raiz;
    }

    private static <T> T crearEventoBandido(ArbolEvento<T> arbol) {
        T raiz = arbol.insertarRaiz(new Decision(30, "Bandido", "Un bandido te bloquea el paso...", "DECISION", 0, 0, 0));
        T luchar = arbol.insertarIzquierdo(raiz, new Decision(31, "Luchar", "Decides luchar.", "DECISION", -10, -10, 0));
        arbol.insertarDerecho(raiz, new Decision(32, "Huir", "Intentas huir.", "RESULTADO", -5, -20, 0));
        arbol.insertarIzquierdo(luchar, new Decision(33, "Atacar de frente", "Derrotas al bandido +100 oro", "RESULTADO", 0, 0, 100));
        arbol.insertarDerecho(luchar, new Decision(34, "Buscar una apertura", "El bandido te hiere gravemente -50 vida", "RESULTADO", -50, 0, 0));
        return raiz;
    }

    private static <T> void ejecutarEvento(ArbolEvento<T> arbol, T nodo, Jugador jugador, Scanner sc) {
        while (nodo != null) {
            Decision actual = arbol.obtenerDecision(nodo);
            System.out.println(actual.descripcion);

            jugador.modificarVida(actual.cambioVida);
            jugador.modificarEnergia(actual.cambioEnergia);
            jugador.oro += actual.recompensa;

            if (arbol.esHoja(nodo)) {
                System.out.println("FIN DEL EVENTO");
                jugador.mostrarEstado();
                break;
            }

            T izquierdo = arbol.hijoIzquierdo(nodo);
            T derecho = arbol.hijoDerecho(nodo);

            if (izquierdo != null) {
                System.out.println("1. " + arbol.obtenerDecision(izquierdo).titulo);
            }

            if (derecho != null) {
                System.out.println("2. " + arbol.obtenerDecision(derecho).titulo);
            }

            int opcion = leerEntero(sc, "Elige una opción: ");

            if (opcion == 1 && izquierdo != null) {
                nodo = izquierdo;
            } else if (opcion == 2 && derecho != null) {
                nodo = derecho;
            } else {
                System.out.println("Opción inválida");
            }
        }
    }


    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException ex) {
                System.out.println("Introduce un número válido.");
            }
        }
    }
}