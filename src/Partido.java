public class Partido {
    private int idPartido; // ID de este partido
    private Equipo equipo1;
    private Equipo equipo2;
    private Equipo ganador;
    private String marcadorEq1;
    private String marcadorEq2;

    public Partido(Equipo equipo1, Equipo equipo2, int idPartido) {
        this.idPartido = idPartido;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.marcadorEq1 = null;
        this.marcadorEq2 = null;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public String getMarcadorEq1() {
        return marcadorEq1;
    }

    public void setMarcadorEq1(String marcadorEq1) {
        this.marcadorEq1 = marcadorEq1;
    }

    public String getMarcadorEq2() {
        return marcadorEq2;
    }

    public void setMarcadorEq2(String marcadorEq2) {
        this.marcadorEq2 = marcadorEq2;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }

    public Equipo getEquipo1() {
        return equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public Equipo getGanador() {
        return ganador;
    }

    public String ingresarResultado(String nombreGanador) {
        if (nombreGanador.equals(equipo1.getNombre())) {
            ganador = equipo1;
        } else if (nombreGanador.equals(equipo2.getNombre())) {
            ganador = equipo2;
        } else {
            return "El nombre del equipo ganador no coincide con los equipos de este partido.";
        }
        return "Resultado ingresado correctamente.";
    }

    public String getResultado() {
        if (ganador == null) {
            return "El partido aún no se ha jugado.";
        }
        return "El ganador es: " + ganador.getNombre();
    }
}
