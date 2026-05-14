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
        System.out.println("\nElige el tipo de sistema para los eventos:");
        System.out.println("1. Árbol Dinámico");
        System.out.println("2. Árbol Secuencial");

        int tipo = leerEntero(sc, "Selecciona una opción: ");

        boolean usarDinamico = tipo == 1;

        // Inicializar componentes del juego
        Mapa mapa = new Mapa(10);
        RegistroPartida registro = new RegistroPartida(64);

        // Crear eventos y árboles según selección
        ArbolEvento<?> arbolViajero;
        ArbolEvento<?> arbolCamino;
        ArbolEvento<?> arbolBandido;

        if (usarDinamico) {
            ArbolDinamico a1 = new ArbolDinamico();
            crearEventoViajero(a1);
            arbolViajero = a1;

            ArbolDinamico a2 = new ArbolDinamico();
            crearEventoCamino(a2);
            arbolCamino = a2;

            ArbolDinamico a3 = new ArbolDinamico();
            crearEventoBandido(a3);
            arbolBandido = a3;
        } else {
            ArbolSecuencial a1 = new ArbolSecuencial(15);
            crearEventoViajero(a1);
            arbolViajero = a1;

            ArbolSecuencial a2 = new ArbolSecuencial(15);
            crearEventoCamino(a2);
            arbolCamino = a2;

            ArbolSecuencial a3 = new ArbolSecuencial(15);
            crearEventoBandido(a3);
            arbolBandido = a3;
        }

        // Crear localizaciones (al menos 6) y añadir al mapa
        Localizacion l1 = new Localizacion(1, "Aldea Inicial", "Tu punto de partida.", null);
        Localizacion l2 = new Localizacion(2, "Bosque", "Arboles y caminos ocultos.", arbolViajero);
        Localizacion l3 = new Localizacion(3, "Río", "Un río caudaloso corta el camino.", arbolCamino);
        Localizacion l4 = new Localizacion(4, "Mercado", "Puestos y mercaderes.", arbolBandido);
        Localizacion l5 = new Localizacion(5, "Puente Viejo", "Un puente frágil sobre un desfiladero.", null);
        Localizacion l6 = new Localizacion(6, "Fortaleza", "Destino objetivo.", null);

        mapa.insertarLocalizacion(l1);
        mapa.insertarLocalizacion(l2);
        mapa.insertarLocalizacion(l3);
        mapa.insertarLocalizacion(l4);
        mapa.insertarLocalizacion(l5);
        mapa.insertarLocalizacion(l6);

        // Insertar caminos (bidireccionales) - al menos 8 aristas
        insertarCaminoBidireccional(mapa,1,2,10);
        insertarCaminoBidireccional(mapa,1,3,15);
        insertarCaminoBidireccional(mapa,1,4,20);
        insertarCaminoBidireccional(mapa,2,3,5);
        insertarCaminoBidireccional(mapa,2,5,25);
        insertarCaminoBidireccional(mapa,3,5,10);
        insertarCaminoBidireccional(mapa,4,5,5);
        insertarCaminoBidireccional(mapa,5,6,30);
        insertarCaminoBidireccional(mapa,3,4,20);

        // Simulación de la partida
        int posicionActual = 1; // Aldea Inicial
        int objetivo = 6; // Fortaleza
        StringBuilder ruta = new StringBuilder();
        ruta.append(posicionActual);
        StringBuilder decisionesTomadas = new StringBuilder();
        int energiaGastada = 0;

        mapa.getLocalizacion(posicionActual).setVisitada(true);
        registro.registrarLocalizacionVisitada("L" + posicionActual);

        System.out.println("\nComienza la partida. Tu objetivo: Fortaleza (6).\n");

        while (posicionActual != objetivo && jugador.vida > 0 && jugador.energia > 0) {
            limpiarPantallaConsola();
            System.out.println("Estás en: " + mapa.getLocalizacion(posicionActual).getNombre());
            mapa.mostrarPanelMapa(posicionActual, objetivo);
            mapa.consultarVecinos(posicionActual);

            System.out.println("Opciones: ");
            System.out.println("1. Moverse a una localización vecina");
            System.out.println("2. Mostrar DFS desde la ubicación actual");
            System.out.println("3. Mostrar BFS desde la ubicación actual");
            System.out.println("4. Mostrar camino mínimo (Dijkstra) hacia objetivo");
            System.out.println("5. Ver estado e inventario");
            System.out.println("6. Salir partida");

            int opcion = leerEntero(sc, "Elige una opción: ");

            if (opcion == 1) {
                int[] vecinos = mapa.obtenerVecinos(posicionActual);
                if (vecinos.length == 0) {
                    System.out.println("No hay localizaciones accesibles desde aquí.");
                    pausarParaContinuar(sc);
                    continue;
                }

                System.out.println("\nElige destino:");
                for (int i = 0; i < vecinos.length; i++) {
                    int vid = vecinos[i];
                    int coste = mapa.getCosteEnergia(posicionActual, vid);
                    System.out.println((i + 1) + ". " + mapa.getLocalizacion(vid).getNombre() + " [id " + vid + "] (Coste: " + coste + ")");
                }

                int seleccion = leerEntero(sc, "Selecciona una opcion de destino: ");
                if (seleccion < 1 || seleccion > vecinos.length) {
                    System.out.println("Seleccion no valida.");
                    pausarParaContinuar(sc);
                    continue;
                }

                int destino = vecinos[seleccion - 1];

                int coste = mapa.getCosteEnergia(posicionActual, destino);

                // Mover
                jugador.modificarEnergia(-coste);
                energiaGastada += coste;
                posicionActual = destino;
                ruta.append(" -> ").append(posicionActual);
                registro.registrarLocalizacionVisitada("L" + posicionActual);
                mapa.getLocalizacion(posicionActual).setVisitada(true);

                // Al llegar, si hay evento y no completado, ejecutarlo
                Localizacion loc = mapa.getLocalizacion(posicionActual);
                ArbolEvento ar = (ArbolEvento) loc.getArbolEvento();
                if (ar != null) {
                    Object raiz = ar.obtenerRaiz();
                    if (raiz != null) {
                        Decision decRaiz = ar.obtenerDecision(raiz);
                        String claveEvento = "E" + (decRaiz != null ? decRaiz.id : posicionActual);
                        if (!registro.eventoCompletado(claveEvento)) {
                            System.out.println("Ejecutando evento en " + loc.getNombre());
                            ejecutarEventoRaw(ar, raiz, jugador, sc, registro, decisionesTomadas);
                            registro.registrarEventoCompletado(claveEvento);
                        } else {
                            System.out.println("Evento ya completado en esta localización.");
                            pausarParaContinuar(sc);
                        }
                    }
                }

            } else if (opcion == 2) {
                mapa.recorridoDFS(posicionActual);
                pausarParaContinuar(sc);
            } else if (opcion == 3) {
                mapa.recorridoBFS(posicionActual);
                pausarParaContinuar(sc);
            } else if (opcion == 4) {
                int costeOptimo = mapa.caminoMinimoDijkstra(posicionActual, objetivo);
                System.out.println("Coste mínimo desde aquí hasta objetivo: " + costeOptimo);
                pausarParaContinuar(sc);
            } else if (opcion == 5) {
                jugador.mostrarEstado();
                pausarParaContinuar(sc);
            } else if (opcion == 6) {
                System.out.println("Saliendo de la partida...");
                break;
            } else {
                System.out.println("Opción no válida.");
                pausarParaContinuar(sc);
            }
        }

        System.out.println("\n--- RESUMEN DE LA PARTIDA ---");
        System.out.println("Ruta seguida: " + ruta.toString());
        System.out.println("Decisiones tomadas: " + decisionesTomadas.toString());
        System.out.println("Energia gastada total: " + energiaGastada);
        System.out.println("Estado final del jugador:");
        jugador.mostrarEstado();
        System.out.println("Camino mínimo alternativo desde inicio a objetivo (Dijkstra):");
        mapa.caminoMinimoDijkstra(1, objetivo);
    }

    private static <T> void insertarResultados(ArbolEvento<T> arbol, T padre, Decision izquierda, Decision derecha) {
        arbol.insertarIzquierdo(padre, izquierda);
        arbol.insertarDerecho(padre, derecha);
    }

    private static <T> T crearEventoViajero(ArbolEvento<T> arbol) {
        T raiz = arbol.insertarRaiz(new Decision(10, "Encuentro en el camino", "Un viajero encapuchado te pide un minuto.", "DECISION", 0, 0, 0));

        T opcionA = arbol.insertarIzquierdo(raiz, new Decision(11, "Escuchar", "Le permites hablar y te ofrece dos rutas.", "DECISION", 0, 0, 0));
        T opcionB = arbol.insertarDerecho(raiz, new Decision(12, "Seguir", "Lo dejas atrás y tomas un desvío cercano.", "DECISION", 0, 0, 0));

        insertarResultados(arbol, opcionA,
                new Decision(13, "Ruta del pozo", "Encuentras provisiones junto a un pozo. +15 energía", "RESULTADO", 0, 15, 0),
                new Decision(14, "Mercader ambulante", "Consigues una venta favorable. +25 oro", "RESULTADO", 0, 0, 25));

        T desvioRocoso = arbol.insertarIzquierdo(opcionB, new Decision(15, "Desvío rocoso", "La senda se vuelve peligrosa y debes decidir rápido.", "DECISION", 0, 0, 0));
        arbol.insertarDerecho(opcionB, new Decision(16, "Paso tranquilo", "Avanzas sin incidentes y recuperas el ritmo. +10 energía", "RESULTADO", 0, 10, 0));

        insertarResultados(arbol, desvioRocoso,
                new Decision(17, "Borde estrecho", "Resbalas al cruzar una grieta. -15 vida", "RESULTADO", -15, 0, 0),
                new Decision(18, "Atajo oculto", "Encuentras una bolsa olvidada. +35 oro", "RESULTADO", 0, 0, 35));

        return raiz;
    }

    private static <T> T crearEventoCamino(ArbolEvento<T> arbol) {
        T raiz = arbol.insertarRaiz(new Decision(20, "Cruce antiguo", "Dos caminos se abren frente a una señal borrada.", "DECISION", 0, 0, 0));

        T opcionA = arbol.insertarIzquierdo(raiz, new Decision(21, "Bosque", "Entras en una zona de árboles densos.", "DECISION", 0, 0, 0));
        T opcionB = arbol.insertarDerecho(raiz, new Decision(22, "Llanura", "Marchas por terreno abierto hacia el este.", "DECISION", 0, 0, 0));

        insertarResultados(arbol, opcionA,
                new Decision(23, "Rastro reciente", "Un lobo te alcanza por sorpresa. -25 vida", "RESULTADO", -25, 0, 0),
                new Decision(24, "Choza vacía", "Hallaste agua y comida. +20 energía", "RESULTADO", 0, 20, 0));

        T puenteViejo = arbol.insertarIzquierdo(opcionB, new Decision(25, "Puente viejo", "La estructura cruje y debes elegir con cuidado.", "DECISION", 0, 0, 0));
        arbol.insertarDerecho(opcionB, new Decision(26, "Campamento aliado", "Te dan indicaciones y suministros. +15 energía", "RESULTADO", 0, 15, 0));

        insertarResultados(arbol, puenteViejo,
                new Decision(27, "Cruzar deprisa", "Una tabla cede y caes al agua. -10 vida, -10 energía", "RESULTADO", -10, -10, 0),
                new Decision(28, "Rodear por ruinas", "Descubres un relicario de valor. +40 oro", "RESULTADO", 0, 0, 40));

        return raiz;
    }

    private static <T> T crearEventoBandido(ArbolEvento<T> arbol) {
        T raiz = arbol.insertarRaiz(new Decision(30, "Emboscada", "Un bandido bloquea el paso en un desfiladero.", "DECISION", 0, 0, 0));

        T opcionA = arbol.insertarIzquierdo(raiz, new Decision(31, "Mantenerse firme", "Te preparas para un posible combate.", "DECISION", 0, 0, 0));
        T opcionB = arbol.insertarDerecho(raiz, new Decision(32, "Negociar", "Intentas ganar tiempo con palabras.", "DECISION", 0, 0, 0));

        insertarResultados(arbol, opcionA,
                new Decision(33, "Ataque frontal", "Lo desarmas tras un intercambio duro. -10 vida, +70 oro", "RESULTADO", -10, 0, 70),
                new Decision(34, "Paso lateral", "Fallaste el movimiento y recibes un corte. -30 vida", "RESULTADO", -30, 0, 0));

        T planDistraccion = arbol.insertarIzquierdo(opcionB, new Decision(35, "Distracción", "Intentas apartar su atención con una maniobra.", "DECISION", 0, 0, 0));
        arbol.insertarDerecho(opcionB, new Decision(36, "Retirada táctica", "Te alejas, pero el esfuerzo te agota. -20 energía", "RESULTADO", 0, -20, 0));

        insertarResultados(arbol, planDistraccion,
                new Decision(37, "Arrojar polvo", "Consigues escapar mientras tose. +20 oro", "RESULTADO", 0, 0, 20),
                new Decision(38, "Señuelo fallido", "El engaño no funciona y te golpea. -15 vida, -10 energía", "RESULTADO", -15, -10, 0));

        return raiz;
    }

    private static void aplicarEfectos(Jugador jugador, Decision decision) {
        jugador.modificarVida(decision.cambioVida);
        jugador.modificarEnergia(decision.cambioEnergia);
        jugador.oro += decision.recompensa;
    }

    private static Item crearItemRecompensa(Decision decision) {
        if (decision == null || decision.recompensa <= 0) return null;

        String nombreItem;
        switch (decision.id) {
            case 14 -> nombreItem = "Mercancia valiosa";
            case 18 -> nombreItem = "Bolsa olvidada";
            case 28 -> nombreItem = "Relicario antiguo";
            case 33 -> nombreItem = "Botin del bandido";
            case 37 -> nombreItem = "Bolsa de monedas";
            default -> nombreItem = "Recompensa de oro";
        }

        return new Item("I" + decision.id, nombreItem);
    }

    private static boolean esResultado(Decision decision) {
        return decision != null && "RESULTADO".equalsIgnoreCase(decision.tipoNodo);
    }

    private static <T> void mostrarOpcionesNarrativas(ArbolEvento<T> arbol, T izquierdo, T derecho) {
        if (izquierdo != null) {
            System.out.println("1. " + arbol.obtenerDecision(izquierdo).titulo);
        }
        if (derecho != null) {
            System.out.println("2. " + arbol.obtenerDecision(derecho).titulo);
        }
    }

    private static <T> void ejecutarEvento(ArbolEvento<T> arbol, T nodo, Jugador jugador, Scanner sc) {
        while (nodo != null) {
            Decision actual = arbol.obtenerDecision(nodo);
            System.out.println(actual.descripcion);

            if (esResultado(actual)) {
                aplicarEfectos(jugador, actual);
            }

            if (arbol.esHoja(nodo)) {
                System.out.println("FIN DEL EVENTO");
                jugador.mostrarEstado();
                break;
            }

            T izquierdo = arbol.hijoIzquierdo(nodo);
            T derecho = arbol.hijoDerecho(nodo);
            mostrarOpcionesNarrativas(arbol, izquierdo, derecho);

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

    // Inserta un camino en ambas direcciones
    private static void insertarCaminoBidireccional(Mapa mapa, int origen, int destino, int peso) {
        mapa.insertarCamino(origen, destino, peso);
        mapa.insertarCamino(destino, origen, peso);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void ejecutarEventoRaw(ArbolEvento arbol, Object nodo, Jugador jugador, Scanner sc, RegistroPartida registro, StringBuilder decisiones) {
        while (nodo != null) {
            Decision actual = arbol.obtenerDecision(nodo);
            System.out.println(actual.descripcion);

            if (esResultado(actual)) {
                aplicarEfectos(jugador, actual);
                if (actual.recompensa > 0) {
                    Item item = crearItemRecompensa(actual);
                    if (item != null) {
                        jugador.getInventario().insertar(item.getId(), item);
                        System.out.println("Has obtenido un objeto: " + item.getNombre());
                    }
                }
            }

            if (arbol.esHoja(nodo)) {
                System.out.println("FIN DEL EVENTO");
                jugador.mostrarEstado();
                decisiones.append("[Evento:" + (actual != null ? actual.titulo : "?") + "]");
                pausarParaContinuar(sc);
                break;
            }

            Object izquierdo = arbol.hijoIzquierdo(nodo);
            Object derecho = arbol.hijoDerecho(nodo);
            mostrarOpcionesNarrativasRaw(arbol, izquierdo, derecho);

            int opcion = leerEntero(sc, "Elige una opción: ");

            if (opcion == 1 && izquierdo != null) {
                decisiones.append("1->" + (arbol.obtenerDecision(izquierdo) != null ? arbol.obtenerDecision(izquierdo).titulo : "?") + ";");
                nodo = izquierdo;
            } else if (opcion == 2 && derecho != null) {
                decisiones.append("2->" + (arbol.obtenerDecision(derecho) != null ? arbol.obtenerDecision(derecho).titulo : "?") + ";");
                nodo = derecho;
            } else {
                System.out.println("Opción inválida");
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void mostrarOpcionesNarrativasRaw(ArbolEvento arbol, Object izquierdo, Object derecho) {
        if (izquierdo != null) {
            System.out.println("1. " + arbol.obtenerDecision(izquierdo).titulo);
        }
        if (derecho != null) {
            System.out.println("2. " + arbol.obtenerDecision(derecho).titulo);
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

    private static void limpiarPantallaConsola() {
        // Limpieza compatible con consola simple
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    private static void pausarParaContinuar(Scanner sc) {
        System.out.print("\nPulsa Enter para continuar...");
        sc.nextLine();
    }
}