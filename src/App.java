import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class App extends JFrame {
    private javax.swing.JPanel JPanel;
    private JComboBox comboNumEquipos;
    private JButton crearTorneoButton;
    private JTextArea textArea;
    private JButton nombrarButton;
    private JComboBox comboBoxEquipo;
    private JTextField textFieldNombreEquipo;
    private JComboBox comboBoxPartido;
    private JButton jugarButton;
    private JTextField textFieldEquipo1;
    private JTextField textFieldEquipo2;
    private JButton terminarButton;
    private JLabel equipo1;
    private JLabel equipo2;
    private JButton quemarDatosButton;
    Torneo torneo;
    Partido p;

    public App() {
        setContentPane(JPanel);
        crearTorneoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearTorneo();
            }
        });
        nombrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombrarEquipo();
            }
        });
        jugarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                empezarPartido();
            }
        });
        terminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jugarPartido();
            }
        });
        textFieldEquipo1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || textFieldEquipo1.getText().length() >= 3) {
                    e.consume();  // Ignorar el evento de la tecla
                }
            }
        });
        textFieldEquipo2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || textFieldEquipo2.getText().length() >= 3) {
                    e.consume();  // Ignorar el evento de la tecla
                }
            }
        });
        quemarDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quemarDatos();
            }
        });
    }

    public void crearTorneo() {
        int numEquipos = Integer.parseInt(comboNumEquipos.getSelectedItem().toString());
        torneo = new Torneo(numEquipos);
        actualizarEquiposBox(numEquipos);
        p = null;
        iniciarBotones(true);
        mostrarTorneo();
    }

    public void nombrarEquipo() {
        if (!textFieldNombreEquipo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, torneo.asignarNombreEquipo(comboBoxEquipo.getSelectedItem().toString(), textFieldNombreEquipo.getText()));
            mostrarTorneo();
        } else {
            JOptionPane.showMessageDialog(null, "Error. El nombre no debe ser nulo");
        }
    }

    public void empezarPartido() {
        p = torneo.obtenerPartidoPorId(Integer.parseInt(comboBoxPartido.getSelectedItem().toString()));
        if (p.getEquipo1().getNombre() == null || p.getEquipo2().getNombre() == null) {
            terminarButton.setEnabled(false);
            textFieldEquipo1.setEnabled(false);
            textFieldEquipo2.setEnabled(false);

            textFieldEquipo1.setText("");
            textFieldEquipo2.setText("");

            equipo1.setText("Equipo: ");
            equipo2.setText("Equipo: ");
            JOptionPane.showMessageDialog(null, "Error. El partido no puede empezar ya que por lo menos hay un equipo que no esta asignado al partido.");
        } else {
            equipo1.setText("Equipo: " + p.getEquipo1().getNombre());
            equipo2.setText("Equipo: " + p.getEquipo2().getNombre());
            terminarButton.setEnabled(true);
            textFieldEquipo1.setEnabled(true);
            textFieldEquipo2.setEnabled(true);
        }
    }

    public void jugarPartido() {
        if (!textFieldEquipo1.getText().isEmpty()) {
            if (!textFieldEquipo2.getText().isEmpty()) {
                if (Integer.parseInt(textFieldEquipo1.getText()) == Integer.parseInt(textFieldEquipo2.getText())) {
                    JOptionPane.showMessageDialog(null, "Error. El partido no puede quedar empate");
                } else {
                    if (Integer.parseInt(textFieldEquipo1.getText()) > Integer.parseInt(textFieldEquipo2.getText())) {
                        JOptionPane.showMessageDialog(null, torneo.jugarPartido(Integer.parseInt(comboBoxPartido.getSelectedItem().toString()), p.getEquipo1().getNombre(), textFieldEquipo1.getText(), textFieldEquipo2.getText()));
                    } else {
                        JOptionPane.showMessageDialog(null, torneo.jugarPartido(Integer.parseInt(comboBoxPartido.getSelectedItem().toString()), p.getEquipo2().getNombre(), textFieldEquipo1.getText(), textFieldEquipo2.getText()));
                    }
                    terminarButton.setEnabled(false);
                    textFieldEquipo1.setEnabled(false);
                    textFieldEquipo2.setEnabled(false);

                    textFieldEquipo1.setText("");
                    textFieldEquipo2.setText("");

                    equipo1.setText("Equipo: ");
                    equipo2.setText("Equipo: ");

                    mostrarTorneo();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error. No se han ingresado el resultado del equipo 2.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error. No se han ingresado el resultado del equipo 1.");
        }
    }

    public void actualizarEquiposBox(int numEquipos) {
        comboBoxEquipo.removeAllItems(); // limpia el comboBox para evitar duplicados
        comboBoxPartido.removeAllItems();

        // crea los equipos y los agrega al comboBox
        for (int i = 1; i <= numEquipos; i++) {
            comboBoxEquipo.addItem("Equipo " + i);

            if (i < numEquipos) {
                comboBoxPartido.addItem(i);
            }

        }
    }

    public void iniciarBotones(boolean bool) {
        quemarDatosButton.setEnabled(bool);
        nombrarButton.setEnabled(bool);
        jugarButton.setEnabled(bool);
        textFieldNombreEquipo.setEnabled(bool);
        textFieldNombreEquipo.setText("");
        textFieldEquipo1.setText("");
        textFieldEquipo2.setText("");
    }

    public void quemarDatos() {
        Random rand = new Random();
        String[] nombresEquipos = {
                "Real Madrid", "Barcelona", "Atletico Madrid", "Sevilla", "Manchester United",
                "Manchester City", "Chelsea", "Liverpool", "Arsenal", "Tottenham", "Juventus",
                "Inter Milan", "AC Milan", "Roma", "Napoli", "Paris Saint-Germain", "Lyon",
                "Monaco", "Bayern Munich", "Borussia Dortmund", "Schalke 04", "Bayer Leverkusen",
                "Ajax", "PSV Eindhoven", "Feyenoord", "Benfica", "Porto", "Sporting Lisbon",
                "Shakhtar Donetsk", "Dynamo Kiev", "River Plate", "Boca Juniors", "Club America",
                "Monterrey", "Flamengo", "Palmeiras", "Sao Paulo", "Corinthians", "Celtic",
                "Rangers", "Zenit St. Petersburg", "CSKA Moscow", "Al Ahly", "Esperance de Tunis",
                "Kaizer Chiefs", "Mamelodi Sundowns", "Guangzhou Evergrande", "Beijing Guoan",
                "Urawa Red Diamonds", "Kashima Antlers", "Jeonbuk Hyundai Motors", "Al Hilal",
                "Al Nassr", "Persepolis", "Esteghlal", "Sydney FC", "Melbourne Victory",
                "Los Angeles FC", "Seattle Sounders", "New York City FC", "Toronto FC",
                "Club Brugge", "Anderlecht", "Galatasaray", "Fenerbahce", "Besiktas",
                "Olympiacos", "PAOK", "Copenhagen", "Malmo FF"
        };

        int numEquipos = Integer.parseInt(comboNumEquipos.getSelectedItem().toString());

        for (int i = 0; i < numEquipos; i++) {
            torneo.asignarNombreEquipo("Equipo " + (i + 1), nombresEquipos[i]);
        }

        mostrarTorneo();


        for(int i = 0; i < numEquipos-1; i++){
            Partido p = torneo.obtenerPartidoPorId(torneo.obtenerNodosHojaYPadres().get(i));
            int res1 = rand.nextInt(5);  //Genera un número aleatorio entre 0 y 4 para el equipo 1
            int res2;

            do {
                res2 = rand.nextInt(5);  //Genera un número aleatorio entre 0 y 4 para el equipo 2
            } while (res1 == res2);  //Si los resultados son iguales, genera un nuevo resultado para el equipo 2

            String ganador = res1 > res2 ? p.getEquipo1().getNombre() : p.getEquipo2().getNombre();
            JOptionPane.showMessageDialog(null, torneo.jugarPartido(torneo.obtenerNodosHojaYPadres().get(i), ganador, String.valueOf(res1), String.valueOf(res2)));
            mostrarTorneo();
        }

        quemarDatosButton.setEnabled(false);

    }

    public void mostrarTorneo() {
        textArea.setText(torneo.representarArbol());
    }

}
