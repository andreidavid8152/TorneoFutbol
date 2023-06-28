import java.util.*;

public class Torneo {
    private Nodo raiz;
    private Map<String, Equipo> equipos = new HashMap<>();
    private List<Equipo> equiposList = new ArrayList<>();
    private Map<Integer, Partido> partidos = new HashMap<>();
    private int contadorPartidos = 1; // Contador para IDs de partidos

    public Torneo(int numEquipos) {
        raiz = crearArbol(numEquipos/2);
        for (int i = 1; i <= numEquipos; i++) {
            Equipo equipo = new Equipo("Equipo " + i);
            equipos.put(equipo.getId(), equipo);
            equiposList.add(equipo);
        }
        // Asignamos los equipos a los nodos del árbol después de crear todo el arbol.
        asignarEquipos(raiz);
    }

    private void asignarEquipos(Nodo nodo) {
        if (nodo == null) {
            return;
        }
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            if (!equiposList.isEmpty()) {
                nodo.getPartido().setEquipo1(equiposList.remove(0));
            }
            if (!equiposList.isEmpty()) {
                nodo.getPartido().setEquipo2(equiposList.remove(0));
            }
        } else {
            asignarEquipos(nodo.getIzquierdo());
            asignarEquipos(nodo.getDerecho());
        }
    }

    private Nodo crearArbol(int numEquipos) {
        if (numEquipos <= 0) {
            return null;
        }
        Partido partido = new Partido(new Equipo(null), new Equipo(null), contadorPartidos++);
        Nodo nodo = new Nodo(partido);
        partidos.put(partido.getIdPartido(), partido);
        if (numEquipos == 1) {
            return nodo;
        }
        int equiposIzquierda = numEquipos / 2;
        int equiposDerecha = numEquipos / 2;
        // Si el número de equipos es impar, añade un equipo extra al subárbol derecho
        if (numEquipos % 2 != 0) {
            equiposDerecha++;
        }
        nodo.setIzquierdo(crearArbol(equiposIzquierda));
        nodo.setDerecho(crearArbol(equiposDerecha));
        return nodo;
    }

    public Partido obtenerPartidoPorId(int idPartido) {
        return partidos.get(idPartido);
    }

    public String asignarNombreEquipo(String idEquipo, String nombre) {
        // Comprueba si el nombre ya existe
        for (Equipo equipo : equipos.values()) {
            if (nombre.equals(equipo.getNombre())) {
                return "El nombre del equipo '" + nombre + "' ya está en uso.";
            }
        }

        Equipo equipo = equipos.get(idEquipo);
        if (equipo == null) {
            return "El equipo con el id " + idEquipo + " no existe.";
        }
        equipo.setNombre(nombre);
        return "Nombre '" + nombre + "' asignado correctamente al " + idEquipo + ".";
    }

    private String ingresarResultado(Nodo nodo, String equipo1, String equipo2, String ganador) {
        if (nodo == null) {
            return "Los equipos proporcionados no tienen un partido.";
        }
        Partido partido = nodo.getPartido();
        if (partido.getEquipo1().getNombre().equals(equipo1) && partido.getEquipo2().getNombre().equals(equipo2)) {
            return partido.ingresarResultado(ganador);
        }
        String resultadoIzquierdo = ingresarResultado(nodo.getIzquierdo(), equipo1, equipo2, ganador);
        if (!resultadoIzquierdo.equals("Los equipos proporcionados no tienen un partido.")) {
            return resultadoIzquierdo;
        }
        return ingresarResultado(nodo.getDerecho(), equipo1, equipo2, ganador);
    }

    private void mostrarResultados(Nodo nodo, StringBuilder builder) {
        if (nodo == null) {
            return;
        }
        mostrarResultados(nodo.getIzquierdo(), builder);
        builder.append(nodo.getPartido().getResultado()).append("\n");
        mostrarResultados(nodo.getDerecho(), builder);
    }

    public boolean esFinal(Partido partido) {
        return raiz.getPartido().getIdPartido() == partido.getIdPartido();
    }

    public String jugarPartido(int idPartido, String nombreGanador, String resEq1, String resEq2) {
        Partido partido = obtenerPartidoPorId(idPartido);
        if (partido == null) {
            return "El partido con el ID " + idPartido + " no existe.";
        }
        String resultado = partido.ingresarResultado(nombreGanador);
        if (resultado.contains("Resultado ingresado correctamente")) {
            partido.setMarcadorEq1(resEq1);
            partido.setMarcadorEq2(resEq2);
            avanzarEquipo(partido, nombreGanador);
            if (esFinal(partido)) {
                resultado = nombreGanador + " ha ganado el campeonato!";
            } else {
                resultado = nombreGanador + " ha ganado el partido!, Avanza a la siguiente ronda";
            }
        }
        return resultado;
    }

    private void avanzarEquipo(Partido partido, String nombreGanador) {
        Nodo nodo = encontrarNodo(raiz, partido);
        if (nodo != null) {
            Nodo padre = encontrarPadre(raiz, nodo);
            if (padre != null) {
                if (padre.getIzquierdo() == nodo) {
                    padre.getPartido().setEquipo1(nombreGanador.equals(partido.getEquipo1().getNombre()) ? partido.getEquipo1() : partido.getEquipo2());
                } else if (padre.getDerecho() == nodo) {
                    padre.getPartido().setEquipo2(nombreGanador.equals(partido.getEquipo1().getNombre()) ? partido.getEquipo1() : partido.getEquipo2());
                }
            }
        }
    }

    private Nodo encontrarNodo(Nodo nodo, Partido partido) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getPartido() == partido) {
            return nodo;
        }
        Nodo nodoIzquierdo = encontrarNodo(nodo.getIzquierdo(), partido);
        if (nodoIzquierdo != null) {
            return nodoIzquierdo;
        }
        return encontrarNodo(nodo.getDerecho(), partido);
    }

    private Nodo encontrarPadre(Nodo nodo, Nodo hijo) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getIzquierdo() == hijo || nodo.getDerecho() == hijo) {
            return nodo;
        }
        Nodo padreIzquierdo = encontrarPadre(nodo.getIzquierdo(), hijo);
        if (padreIzquierdo != null) {
            return padreIzquierdo;
        }
        return encontrarPadre(nodo.getDerecho(), hijo);
    }

    public String representarArbol() {
        return representarArbol(raiz, "", true);
    }

    private String representarArbol(Nodo nodo, String prefijo, boolean esIzquierdo) {
        if (nodo == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append(prefijo);
        builder.append(esIzquierdo ? "├── " : "└── ");

        Partido partido = nodo.getPartido();
        builder.append("Partido ").append(partido.getIdPartido()).append(": [");

        if(partido.getGanador() == null){
            builder.append(partido.getEquipo1().getId() + " " + partido.getEquipo1().getNombre() + " vs ");
            builder.append(partido.getEquipo2().getId() + " " + partido.getEquipo2().getNombre() + "] " + "(" + partido.getMarcadorEq1() + "-" + partido.getMarcadorEq2() + ")\n");
        }else if(partido.getGanador().equals(partido.getEquipo1())){
            builder.append(partido.getEquipo1().getId() + " (G) " + partido.getEquipo1().getNombre() + " vs ");
            builder.append(partido.getEquipo2().getId() + " " + partido.getEquipo2().getNombre() + "] " + "(" + partido.getMarcadorEq1() + "-" + partido.getMarcadorEq2() + ")\n");
        }else if(partido.getGanador().equals(partido.getEquipo2())){
            builder.append(partido.getEquipo1().getId() + " " + partido.getEquipo1().getNombre() + " vs ");
            builder.append(partido.getEquipo2().getId() + " (G) " + partido.getEquipo2().getNombre() + "] " + "(" + partido.getMarcadorEq1() + "-" + partido.getMarcadorEq2() + ")\n");
        }else{
            builder.append(partido.getEquipo1().getId() + " " + partido.getEquipo1().getNombre() + " vs ");
            builder.append(partido.getEquipo2().getId() + " " + partido.getEquipo2().getNombre() + "] " + "( null - null)\n");
        }

        builder.append(representarArbol(nodo.getIzquierdo(), prefijo + (esIzquierdo ? "│   " : "    "), true));
        builder.append(representarArbol(nodo.getDerecho(), prefijo + (esIzquierdo ? "│   " : "    "), false));

        return builder.toString();
    }
    public List<Integer> obtenerNodosHojaYPadres() {
        List<Integer> nodosHojaYPadres = new ArrayList<>();
        obtenerNodosHojaYPadres(raiz, nodosHojaYPadres);
        return nodosHojaYPadres;
    }

    private void obtenerNodosHojaYPadres(Nodo nodo, List<Integer> nodosHojaYPadres) {
        if (nodo == null) {
            return;
        }
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            Partido partido = nodo.getPartido();
            nodosHojaYPadres.add(partido.getIdPartido());
        } else {
            obtenerNodosHojaYPadres(nodo.getIzquierdo(), nodosHojaYPadres);
            obtenerNodosHojaYPadres(nodo.getDerecho(), nodosHojaYPadres);
            Partido partido = nodo.getPartido();
            nodosHojaYPadres.add(partido.getIdPartido());
        }
    }
}
