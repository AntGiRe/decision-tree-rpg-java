# Memoria: Sistema de Aventura Basado en Grafos y Tablas Hash

## 1. Introducción y Estructura General

El proyecto es un sistema de aventura textual en Java que integra tres estructuras de datos fundamentales:

- **Árboles Binarios** (Práctica 1): Representan eventos de decisión dentro del juego
- **Grafos**: Modelan el mapa del mundo como red de localizaciones conectadas
- **Tablas Hash**: Gestionan el inventario del jugador y el registro de partida

El programa permite al jugador explorar un mundo, tomar decisiones en eventos y gestionar un inventario, todo con una simulación completa de partida que culmina con un resumen de la aventura.

---

## 2. Estructura del Programa

### 2.1 Componentes Principales

| Clase | Responsabilidad |
|-------|-----------------|
| `Mapa` | Grafo del mundo: localizaciones y caminos |
| `Localizacion` | Nodo del grafo con referencias a eventos |
| `Arista` | Conexión dirigida entre localizaciones con peso |
| `Inventario` | Tabla hash para objetos del jugador |
| `Item` | Objeto del juego (identificador y nombre) |
| `RegistroPartida` | Tablas hash para eventos completados y localizaciones visitadas |
| `Jugador` | Estado del jugador (vida, energía, oro, inventario) |
| `Main` | Orquestación de la partida y lógica de juego |

### 2.2 Flujo de Ejecución

```
1. Inicialización
   ├─ Seleccionar tipo de árbol (Dinámico o Secuencial)
   ├─ Crear 6 localizaciones
   ├─ Conectarlas con 9 aristas bidireccionales
   └─ Asignar eventos a 3 localizaciones

2. Simulación de Partida
   ├─ Bucle principal: mostrar mapa, listar vecinos, elegir movimiento
   ├─ Al movimiento: descontar energía, ejecutar evento si existe
   ├─ En evento: aplicar efectos, añadir objetos al inventario
   └─ Terminar: llegada a objetivo o pérdida de vida/energía

3. Resumen Final
   ├─ Mostrar ruta recorrida
   ├─ Mostrar decisiones tomadas
   ├─ Mostrar estado final (vida, energía, oro)
   ├─ Mostrar contenido del inventario y estadísticas hash
   └─ Calcular y mostrar camino mínimo alternativo (Dijkstra)
```

---

## 3. Mapa y Grafo

### 3.1 Descripción del Mundo

El mapa consta de 6 localizaciones conectadas por 9 aristas bidireccionales (18 dirigidas), con la siguiente topología:

```
Aldea Inicial (1) ─────10───── Bosque (2)
    │                 │           │
   15                 5          25
    │                 │           │
   Río (3)──────20────┴─────Puente Viejo (5)─────30─────Fortaleza (6)
    │                          │
   20                          5
    │                          │
 Mercado (4)──────────────────┘
```

**Eventos asociados:**
- Bosque (2): "Encuentro con viajero" (árbol de 7 nodos)
- Río (3): "Cruce antiguo" (árbol de 7 nodos)
- Mercado (4): "Emboscada" (árbol de 7 nodos)

**Pesos (energía):** Varían de 5 a 30, permitiendo estrategia en la ruta.

### 3.2 Representación Elegida: Lista de Adyacencia con Aristas

Se implementó la **lista de adyacencia** como representación principal del grafo, almacenando objetos `Arista` reales.

**Estructura:**
```java
private static class NodoArista {
    private final Arista arista;  // Contiene origen, destino, peso
    private NodoArista siguiente;
}

private final NodoArista[] listaAdyacencia;  // Array de listas enlazadas
private final Localizacion[] vertices;       // Array de localizaciones
```

**Justificación:**

| Criterio | Lista de Adyacencia | Matriz de Adyacencia |
|----------|-------------------|----------------------|
| Memoria | O(V + E) = O(6 + 18) = O(24) | O(V²) = O(36) |
| Recorridos (DFS/BFS) | O(V + E) = O(24) | O(V²) = O(36) |
| Buscar vecinos | O(grado) ≈ O(2-3) | O(V) = O(6) |
| Dijkstra | O((V + E) log V) | O(V²) |
| Insertar/Eliminar arista | O(grado) ≈ O(1) amortizado | O(1) |

**Conclusión:** La lista de adyacencia es **más eficiente** en memoria y recorridos para grafos dispersos. Con 6 vértices y 18 aristas, es la opción natural. La matriz sería innecesaria y desperdiciadora.

**Uso de Arista:** Cada arista es un objeto con `origen`, `destino` y `peso`, permitiendo un diseño orientado a objetos coherente y fácil de mantener.

---

## 4. Tabla Hash del Inventario

### 4.1 Implementación Manual

La clase `Inventario` implementa una tabla hash con resolución de colisiones por **encadenamiento separado** (listas enlazadas):

```java
private class NodoHash {
    String clave;      // ID del objeto
    Item valor;        // Objeto del juego
    NodoHash siguiente; // Siguiente en la cadena
}

private NodoHash[] tabla;     // Array de celdas
private int numElementos;     // Objetos guardados
private int numColisiones;    // Colisiones detectadas
```

### 4.2 Función Hash

```java
private int funcionHash(String clave) {
    int hash = 0;
    for (int i = 0; i < clave.length(); i++) {
        hash = 31 * hash + clave.charAt(i);  // Algoritmo polinomial
    }
    return Math.abs(hash) % tamanoTabla;
}
```

**Características:**
- Usa el algoritmo polinomial estándar (multiplicador 31)
- Convierte a valor positivo con `Math.abs()`
- Aplica módulo para obtener índice válido
- Distribución uniforme para claves como "I14", "I18", "I28"

### 4.3 Resolución de Colisiones: Encadenamiento Separado

Cuando dos claves hashean al mismo índice:

```
Tabla[5] → [I14|Mercancia] → [I28|Relicario] → null
```

**Ventajas:**
- Simple de implementar
- No requiere rehashing
- Perfil conocido y predecible

**Desventajas:**
- Costo de memoria por punteros
- Peor rendimiento si la cadena es larga

**En la práctica:** Con 16 celdas y máximo 3-5 objetos, las cadenas son cortas (típicamente 1-2 elementos).

### 4.4 Estadísticas

El sistema registra:

- **Tamaño de tabla:** 16 celdas
- **Factor de carga:** numElementos / tamanoTabla (típicamente 0.19 - 0.31)
- **Colisiones:** Incrementadas cada vez que se inserta en una celda ocupada

---

## 5. Tabla Hash de RegistroPartida

`RegistroPartida` usa **dos tablas hash** separadas:

```java
private final NodoRegistro[] localizacionesVisitadas;  // Hash de localizaciones
private final NodoRegistro[] eventosCompletados;       // Hash de eventos
```

Ambas usan el mismo mecanismo (encadenamiento, función hash) y permiten:

- Registrar localización visitada: O(1) amortizado
- Comprobar si fue visitada: O(1) amortizado
- Registrar evento completado: O(1) amortizado
- Comprobar si fue completado: O(1) amortizado

Esto evita repetir eventos en localizaciones ya visitadas.

---

## 6. Análisis de Eficiencia Comparado

### 6.1 Operaciones sobre el Grafo (Mapa)

| Operación | Complejidad | Notas |
|-----------|------------|-------|
| Insertar localización | O(1) | Append en array |
| Insertar camino | O(grado) ≈ O(1) | Append en lista enlazada |
| Eliminar camino | O(grado) ≈ O(1) | Búsqueda + eliminación en lista |
| Obtener vecinos | O(grado) ≈ O(1) | Recorrer lista de salida |
| DFS | O(V + E) = O(24) | Recorrido completo |
| BFS | O(V + E) = O(24) | Recorrido completo |
| Dijkstra | O((V + E) log V) ≈ O(100) | Con cola de prioridades manual |
| Conectividad | O(V + E) = O(24) | BFS con acceso precoz |

### 6.2 Operaciones sobre la Tabla Hash (Inventario)

| Operación | Complejidad | Notas |
|-----------|------------|-------|
| Insertar | O(1) amortizado | Calcula hash e inserta en cabeza o recorre cadena |
| Buscar | O(1) amortizado | Calcula hash, recorre cadena (típicamente 1-2 elementos) |
| Eliminar | O(1) amortizado | Calcula hash, busca y enlaza |
| Mostrar contenido | O(n) | n = numElementos guardados |
| Factor de carga | O(1) | Cálculo directo |

### 6.3 Tabla Hash vs. Array Lineal

Si el inventario fuera un array simple:

| Operación | Array Lineal | Tabla Hash |
|-----------|--------------|-----------|
| Insertar | O(n) | O(1) amortizado |
| Buscar por ID | O(n) | O(1) amortizado |
| Eliminar | O(n) | O(1) amortizado |
| Mostrar | O(n) | O(n) |

**Ganancia:** La tabla hash es **10-100× más rápida** para búsqueda y eliminación.

### 6.4 Lista de Adyacencia vs. Matriz

Para el grafo con 6 vértices y 18 aristas:

| Aspecto | Lista | Matriz |
|--------|-------|--------|
| Memoria | 6 + 18 = 24 referencias | 36 enteros + overhead |
| DFS/BFS | O(24) iteraciones | O(36) iteraciones |
| Vecinos | O(1-3) búsquedas | O(6) búsquedas |
| **Diferencia** | **2× más rápido** | Búsqueda lineal |

---

## 7. Conclusiones

1. **Estructura elegida es óptima:** Lista de adyacencia con `Arista` reales aprovecha la naturaleza dispersa del grafo.

2. **Tabla hash es indispensable:** Sin ella, búsqueda de objetos sería O(n); con ella es O(1).

3. **Encadenamiento separado funciona bien:** Con factores de carga bajos (< 0.5), las colisiones son raras y las cadenas cortas.

4. **Escalabilidad:** El sistema escala bien; agregar más localizaciones o items no degrada significativamente el rendimiento.

5. **Integración:** Grafo + Hash + Árboles forman una solución cohesiva que cumple todos los requisitos del enunciado.

---

**Proyecto:** Sistema de Aventura Basado en Grafos (Práctica 2)  
**Estructuras implementadas:** Árboles (Práctica 1), Grafos, Tablas Hash
**Fecha:** 14/05/2026

