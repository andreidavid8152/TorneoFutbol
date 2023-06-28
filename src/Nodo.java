public class Nodo {
    private Partido partido;
    private Nodo izquierdo;
    private Nodo derecho;

    public Nodo(Partido partido) {
        this.partido = partido;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }

    public Nodo getIzquierdo() {
        return izquierdo;
    }

    public Nodo getDerecho() {
        return derecho;
    }
}
