package com.antgire;

public class RegistroPartida {

    private class NodoRegistro {
        String clave;
        NodoRegistro siguiente;

        NodoRegistro(String clave) {
            this.clave = clave;
            this.siguiente = null;
        }
    }

    private final NodoRegistro[] localizacionesVisitadas;
    private final NodoRegistro[] eventosCompletados;
    private final int tamano;

    public RegistroPartida(int tamanoInicial) {
        this.tamano = tamanoInicial;
        this.localizacionesVisitadas = new NodoRegistro[tamanoInicial];
        this.eventosCompletados = new NodoRegistro[tamanoInicial];
    }

    private int funcionHash(String clave) {
        int hash = 0;
        for (int i = 0; i < clave.length(); i++) {
            hash = 31 * hash + clave.charAt(i);
        }
        return Math.abs(hash) % tamano;
    }

    private void registrarEnTabla(NodoRegistro[] tabla, String clave) {
        int indice = funcionHash(clave);
        NodoRegistro nuevo = new NodoRegistro(clave);

        if (tabla[indice] == null) {
            tabla[indice] = nuevo;
        } else {
            NodoRegistro actual = tabla[indice];
            while (actual != null) {
                if (actual.clave.equals(clave)) return;
                if (actual.siguiente == null) break;
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    private boolean comprobarEnTabla(NodoRegistro[] tabla, String clave) {
        int indice = funcionHash(clave);
        NodoRegistro actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public void registrarLocalizacionVisitada(String clave) {
        registrarEnTabla(localizacionesVisitadas, clave);
    }

    public void registrarEventoCompletado(String clave) {
        registrarEnTabla(eventosCompletados, clave);
    }

    public boolean localizacionVisitada(String clave) {
        return comprobarEnTabla(localizacionesVisitadas, clave);
    }

    public boolean eventoCompletado(String clave) {
        return comprobarEnTabla(eventosCompletados, clave);
    }

    // Compatibilidad con la API anterior
    public void registrar(String clave) {
        registrarLocalizacionVisitada(clave);
    }

    public boolean comprobar(String clave) {
        return localizacionVisitada(clave) || eventoCompletado(clave);
    }
}