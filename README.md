# Memoria - Tarea 1 (Árbol de Habilidades)

## 1. Estructura del programa

El proyecto implementa un sistema de eventos narrativos con decisiones binarias, donde cada evento se modela como un arbol binario. El jugador avanza por los nodos del arbol y, segun las decisiones tomadas, se aplican cambios en su estado (vida, energia y oro).

### 1.1 Clases principales

- `Main`: punto de entrada del programa. Solicita nombre del jugador, permite elegir tipo de arbol (dinamico o secuencial), ejecuta los eventos y muestra recorridos y propiedades del ultimo evento.
- `Jugador`: representa el estado del jugador (`vida`, `energia`, `oro`, `inventario`) y ofrece metodos para modificarlo.
- `Decision`: encapsula la informacion de cada nodo narrativo (`id`, `titulo`, `descripcion`, `tipoNodo`, cambios de estado y recompensa).
- `ArbolEvento<T>`: interfaz comun para ambas implementaciones de arbol. Define operaciones de construccion, navegacion, recorridos y calculo de propiedades.
- `ArbolDinamico`: implementacion enlazada mediante nodos (`Nodo izquierdo`, `Nodo derecho`).
- `ArbolSecuencial`: implementacion en arreglo (`Decision[]`) usando indices de arbol binario completo.
- `Nodo`: estructura de apoyo para el arbol dinamico.

### 1.2 Flujo general de ejecucion

1. Se crea el jugador.
2. Se selecciona el tipo de estructura de arbol.
3. Se construyen y ejecutan tres eventos: Viajero, Camino y Bandido.
4. En cada nodo se muestra descripcion, se aplican efectos al jugador y se ofrecen opciones 1/2.
5. Al finalizar el ultimo evento, se muestran:
   - recorridos (`preorden`, `inorden`, `postorden`, `porNiveles`)
   - propiedades (`contarNodos`, `contarHojas`, `altura`)

---

## 2. Descripcion de los eventos disenados

### 2.1 Evento 1: Viajero

- **Contexto**: el jugador encuentra a un viajero extrano.
- **Decision inicial**:
  - Hablar
  - Ignorar
- **Ramas relevantes**:
  - Si habla, aparecen dos posibles desenlaces:
    - Aceptar su ayuda (`+20 vida`)
    - Mantener distancia (`-10 oro`)

### 2.2 Evento 2: Camino

- **Contexto**: cruce de caminos.
- **Decision inicial**:
  - Bosque (impacto inicial: `-5 vida`, `-10 energia`)
  - Camino seguro (`+10 oro`)
- **Ramas del bosque**:
  - Seguir un ruido (`-30 vida`)
  - Explorar el claro (`+50 oro`)

### 2.3 Evento 3: Bandido

- **Contexto**: un bandido bloquea el paso.
- **Decision inicial**:
  - Luchar (impacto inicial: `-10 vida`, `-10 energia`)
  - Huir (`-5 vida`, `-20 energia`)
- **Ramas de lucha**:
  - Atacar de frente (`+100 oro`)
  - Buscar una apertura (`-50 vida`)

---

## 3. Representacion del arbol de un evento (Evento Bandido)

A continuacion se representa el evento `Bandido` como arbol binario:

```text
(30) Bandido
├─(31) Luchar
│  ├─(33) Atacar de frente
│  └─(34) Buscar una apertura
└─(32) Huir
```

Interpretacion:
- La raiz (`30`) es el inicio del evento.
- Los nodos internos (`31`) son nodos de decision con hijos.
- Las hojas (`33`, `34`, `32`) representan desenlaces finales del evento.

Propiedades de este arbol:
- Nodos totales: `5`
- Hojas: `3`
- Altura: `3`

---

## 4. Comparacion: implementacion secuencial vs dinamica

| Criterio | Arbol dinamico (`ArbolDinamico`) | Arbol secuencial (`ArbolSecuencial`) |
|---|---|---|
| Representacion | Nodos enlazados con referencias | Arreglo de `Decision` e indices |
| Insercion de hijos | Directa por referencias (`izquierdo`, `derecho`) | Calculada por formula (`2*i+1`, `2*i+2`) |
| Uso de memoria | Flexible, solo nodos existentes | Puede reservar posiciones vacias |
| Limite de tamano | No fijo de antemano | Fijo por `sizeMax` |
| Simplicidad conceptual | Muy natural para arboles no completos | Muy directa para arboles casi completos |
| Riesgo principal | Mayor manejo de objetos/referencias | Desbordar capacidad o dejar huecos |

### Conclusiones de comparacion

- Para un juego con eventos pequenos y estructura variable, la implementacion **dinamica** es mas flexible.
- La implementacion **secuencial** es util cuando se conoce el tamano maximo y se quiere una representacion indexada simple.
- En este proyecto, ambas comparten el mismo contrato (`ArbolEvento<T>`), lo que permite cambiar de una a otra sin modificar la logica narrativa de `Main`.

---

## 5. Cierre

El sistema demuestra separacion de responsabilidades:
- `Main` orquesta la partida.
- Las clases de arbol encapsulan operaciones de recorridos y metricas.
- `Decision` y `Jugador` concentran el modelo narrativo y de estado.

La interfaz comun permite comparar de forma limpia dos estrategias de implementacion de arbol binario en un mismo problema practico.

