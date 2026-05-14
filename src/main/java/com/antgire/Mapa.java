package com.antgire;

public class Mapa {
    private final Localizacion[] vertices;
    private final NodoArista[] listaAdyacencia;
    private final int maxVertices;
    private int numVertices;

    // Nodo manual de la lista de adyacencia. Cada nodo contiene una Arista real.
    private static class NodoArista {
        private final Arista arista;
        private NodoArista siguiente;

        NodoArista(Arista arista) {
            this.arista = arista;
        }
    }

    public Mapa(int maxVertices) {
        this.maxVertices = maxVertices;
        this.vertices = new Localizacion[maxVertices];
        this.listaAdyacencia = new NodoArista[maxVertices];
        this.numVertices = 0;
    }

    private int buscarIndice(int id) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i] != null && vertices[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public void insertarLocalizacion(Localizacion l) {
        if (l == null) {
            return;
        }
        if (buscarIndice(l.getId()) != -1) {
            System.out.println("Error: ya existe una localización con ese id.");
            return;
        }
        if (numVertices >= maxVertices) {
            System.out.println("Error: Mapa lleno.");
            return;
        }
        vertices[numVertices++] = l;
    }

    public void insertarCamino(int idOrigen, int idDestino, int peso) {
        Localizacion origen = getLocalizacion(idOrigen);
        Localizacion destino = getLocalizacion(idDestino);

        if (origen == null || destino == null) {
            return;
        }

        eliminarCamino(idOrigen, idDestino);

        int idxOrigen = buscarIndice(idOrigen);
        NodoArista nuevoNodo = new NodoArista(new Arista(origen, destino, peso));

        if (listaAdyacencia[idxOrigen] == null) {
            listaAdyacencia[idxOrigen] = nuevoNodo;
            return;
        }

        NodoArista actual = listaAdyacencia[idxOrigen];
        while (actual.siguiente != null) {
            actual = actual.siguiente;
        }
        actual.siguiente = nuevoNodo;
    }

    public void eliminarCamino(int idOrigen, int idDestino) {
        int idxOrigen = buscarIndice(idOrigen);
        if (idxOrigen == -1) {
            return;
        }

        NodoArista actual = listaAdyacencia[idxOrigen];
        NodoArista anterior = null;

        while (actual != null) {
            if (actual.arista.getDestino().getId() == idDestino) {
                if (anterior == null) {
                    listaAdyacencia[idxOrigen] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
    }

    public boolean estanConectadas(int idOrigen, int idDestino) {
        int origen = buscarIndice(idOrigen);
        int destino = buscarIndice(idDestino);

        if (origen == -1 || destino == -1) {
            return false;
        }
        if (origen == destino) {
            return true;
        }

        boolean[] visitados = new boolean[numVertices];
        int[] cola = new int[numVertices];
        int frente = 0;
        int fin = 0;

        visitados[origen] = true;
        cola[fin++] = origen;

        while (frente < fin) {
            int actual = cola[frente++];
            NodoArista nodo = listaAdyacencia[actual];

            while (nodo != null) {
                int vecino = buscarIndice(nodo.arista.getDestino().getId());
                if (vecino != -1 && !visitados[vecino]) {
                    if (vecino == destino) {
                        return true;
                    }
                    visitados[vecino] = true;
                    cola[fin++] = vecino;
                }
                nodo = nodo.siguiente;
            }
        }

        return false;
    }

    public void consultarVecinos(int id) {
        int idx = buscarIndice(id);
        if (idx == -1) {
            return;
        }

        System.out.print("Vecinos de " + vertices[idx].getNombre() + ": ");
        NodoArista actual = listaAdyacencia[idx];
        if (actual == null) {
            System.out.println("(ninguno)");
            return;
        }

        while (actual != null) {
            Localizacion destino = actual.arista.getDestino();
            System.out.print("[" + destino.getId() + "] " + destino.getNombre() + " (Coste: " + actual.arista.getPeso() + ") | ");
            actual = actual.siguiente;
        }
        System.out.println();
    }

    public int[] obtenerVecinos(int id) {
        int idx = buscarIndice(id);
        if (idx == -1) {
            return new int[0];
        }

        int count = 0;
        NodoArista actual = listaAdyacencia[idx];
        while (actual != null) {
            count++;
            actual = actual.siguiente;
        }

        if (count == 0) {
            return new int[0];
        }

        int[] vecinos = new int[count];
        int i = 0;
        actual = listaAdyacencia[idx];
        while (actual != null) {
            vecinos[i++] = actual.arista.getDestino().getId();
            actual = actual.siguiente;
        }
        return vecinos;
    }

    private Arista buscarArista(int idOrigen, int idDestino) {
        int idx = buscarIndice(idOrigen);
        if (idx == -1) {
            return null;
        }

        NodoArista actual = listaAdyacencia[idx];
        while (actual != null) {
            if (actual.arista.getDestino().getId() == idDestino) {
                return actual.arista;
            }
            actual = actual.siguiente;
        }

        return null;
    }

    public void recorridoDFS(int idInicio) {
        int inicioIdx = buscarIndice(idInicio);
        if (inicioIdx == -1) {
            return;
        }

        boolean[] visitados = new boolean[numVertices];
        System.out.print("Recorrido DFS: ");
        dfsRecursivo(inicioIdx, visitados);
        System.out.println();
    }

    private void dfsRecursivo(int idx, boolean[] visitados) {
        visitados[idx] = true;
        System.out.print(vertices[idx].getNombre() + " -> ");

        NodoArista actual = listaAdyacencia[idx];
        while (actual != null) {
            int vecino = buscarIndice(actual.arista.getDestino().getId());
            if (vecino != -1 && !visitados[vecino]) {
                dfsRecursivo(vecino, visitados);
            }
            actual = actual.siguiente;
        }
    }

    public void recorridoBFS(int idInicio) {
        int inicioIdx = buscarIndice(idInicio);
        if (inicioIdx == -1) {
            return;
        }

        boolean[] visitados = new boolean[numVertices];
        int[] cola = new int[numVertices];
        int frente = 0;
        int fin = 0;

        visitados[inicioIdx] = true;
        cola[fin++] = inicioIdx;

        System.out.print("Recorrido BFS: ");
        while (frente < fin) {
            int actual = cola[frente++];
            System.out.print(vertices[actual].getNombre() + " -> ");

            NodoArista nodo = listaAdyacencia[actual];
            while (nodo != null) {
                int vecino = buscarIndice(nodo.arista.getDestino().getId());
                if (vecino != -1 && !visitados[vecino]) {
                    visitados[vecino] = true;
                    cola[fin++] = vecino;
                }
                nodo = nodo.siguiente;
            }
        }
        System.out.println();
    }

    public int caminoMinimoDijkstra(int idOrigen, int idDestino) {
        int origenIdx = buscarIndice(idOrigen);
        int destinoIdx = buscarIndice(idDestino);

        if (origenIdx == -1 || destinoIdx == -1) {
            return -1;
        }

        int[] distancias = new int[numVertices];
        boolean[] visitados = new boolean[numVertices];
        int[] padres = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distancias[i] = Integer.MAX_VALUE;
            visitados[i] = false;
            padres[i] = -1;
        }

        distancias[origenIdx] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = minDistancia(distancias, visitados);
            if (u == -1) {
                break;
            }
            visitados[u] = true;

            NodoArista nodo = listaAdyacencia[u];
            while (nodo != null) {
                int v = buscarIndice(nodo.arista.getDestino().getId());
                int peso = nodo.arista.getPeso();

                if (v != -1 && !visitados[v] && distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                    distancias[v] = distancias[u] + peso;
                    padres[v] = u;
                }
                nodo = nodo.siguiente;
            }
        }

        if (distancias[destinoIdx] != Integer.MAX_VALUE) {
            System.out.print("Ruta mínima calculada por Dijkstra: ");
            mostrarCaminoDijkstra(padres, destinoIdx);
            System.out.println();
            return distancias[destinoIdx];
        }

        return -1;
    }

    private int minDistancia(int[] distancias, boolean[] visitados) {
        int min = Integer.MAX_VALUE;
        int minIdx = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!visitados[v] && distancias[v] <= min) {
                min = distancias[v];
                minIdx = v;
            }
        }

        return minIdx;
    }

    private void mostrarCaminoDijkstra(int[] padres, int j) {
        if (padres[j] == -1) {
            System.out.print(vertices[j].getNombre());
            return;
        }
        mostrarCaminoDijkstra(padres, padres[j]);
        System.out.print(" => " + vertices[j].getNombre());
    }

    public Localizacion getLocalizacion(int id) {
        int idx = buscarIndice(id);
        return idx != -1 ? vertices[idx] : null;
    }

    public int getCosteEnergia(int idOrigen, int idDestino) {
        Arista arista = buscarArista(idOrigen, idDestino);
        return arista != null ? arista.getPeso() : -1;
    }

    public void mostrarPanelMapa(int idActual, int idObjetivo) {
        System.out.println("================ MAPA ================");
        for (int i = 0; i < numVertices; i++) {
            Localizacion loc = vertices[i];
            String marca = "   ";
            if (loc.getId() == idActual) {
                marca = "[P]";
            } else if (loc.getId() == idObjetivo) {
                marca = "[O]";
            } else if (loc.isVisitada()) {
                marca = "[V]";
            }
            System.out.println(marca + " " + loc.getId() + " - " + loc.getNombre());
        }

        System.out.println("---------- Conexiones desde tu posicion ----------");
        int[] vecinos = obtenerVecinos(idActual);
        if (vecinos.length == 0) {
            System.out.println("Sin conexiones disponibles.");
        } else {
            for (int vecino : vecinos) {
                int coste = getCosteEnergia(idActual, vecino);
                Localizacion locVecina = getLocalizacion(vecino);
                System.out.println(" -> " + vecino + " (" + locVecina.getNombre() + ") | coste " + coste);
            }
        }
        System.out.println("======================================");
    }
}