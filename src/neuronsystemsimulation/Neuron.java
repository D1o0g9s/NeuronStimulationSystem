package neuronsystemsimulation;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 * The Neuron is a JPanel that is interactive with the mouse. It holds the
 * information that pertains to a neuron, i.e. its neighbors, excitement
 * threshold, and leak rate (the rate at which the neuron loses its excitement).
 *
 * @author Geeling Chau
 * @date May 2016
 */
public class Neuron extends JPanel implements MouseListener {

    private final int i;
    private final int j;
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private static final int NUM_SYNAPSES = 4;
    private static final int MAX_ROUNDS_SINCE_LTP = 10;
    private static final double MAX_SIGNAL = 5;
    private final double LEAK_RATE; //0<=x<0.5

    private int northBorder = 0;
    private int eastBorder = 0;
    private int southBorder = 0;
    private int westBorder = 0;

    private JLabel stateLabel;
    private JLabel thresholdLabel;
    private JLabel leakLabel;
    private Synapse[] connections = new Synapse[NUM_SYNAPSES];

    private final double THRESHOLD;
    private final double MAX_STIMULI = 5;
    private final int DEDUCTION_RATIO = 3;
    private final int MAX_COLOR = 250;
    private double state = 0; // as a decimal 
    private double newState = 0;
    private int color;

    /**
     * Creates a neuron with a random leak rate at row i, col j. 
     * 
     * @param i - int - row the neuron is in
     * @param j - int - col the neuron is in
     */
    Neuron(int i, int j) {
        super();
        this.i = i;
        this.j = j;
        LEAK_RATE = Math.random() + 1;
        THRESHOLD = LEAK_RATE + 0.5;
        stateLabel = new JLabel(String.format("%.2f", state));
        stateLabel.setForeground(Color.GRAY);
        this.add(stateLabel);

        leakLabel = new JLabel("leak rate:" + String.format(
                "%.2f", LEAK_RATE));
        leakLabel.setForeground(Color.GRAY);
        this.add(leakLabel);

        thresholdLabel = new JLabel("threshold:" + String.format(
                "%.2f", THRESHOLD));
        thresholdLabel.setForeground(Color.GRAY);
        this.add(thresholdLabel);

        addMouseListener(this);

    }

    /**
     * Updates the display of the neuron and its synapse borders. 
     */
    public void display() {
        for (int i = 0; i < NUM_SYNAPSES; i++) {
            if (connections[i] != null) {
                switch (i) {
                    case NORTH:
                        northBorder
                                = connections[NORTH].getResistance();
                        break;
                    case EAST:
                        eastBorder
                                = connections[EAST].getResistance();
                        break;
                    case SOUTH:
                        southBorder
                                = connections[SOUTH].getResistance();
                        break;
                    case WEST:
                        westBorder
                                = connections[WEST].getResistance();
                        break;
                    default:
                        System.out.println(
                                "i not found. Neuron display()");
                        break;
                }
            }
        }
        stateLabel.setText(String.format("%.2f", state));
        if (state > MAX_STIMULI) {
            color = MAX_COLOR;
        } else {
            color = (int) ((state * MAX_COLOR) / MAX_STIMULI);

        }

        this.setBorder(BorderFactory.createMatteBorder(
                northBorder,
                westBorder,
                southBorder,
                eastBorder, Color.RED));
        this.setBackground(new Color(color, color, color));

    }

    /**
     * Loops and updates all the synapses depending on neighbor excitement.
     */
    public void update() {
        if (state >= THRESHOLD) {
            for (int i = 0; i < NUM_SYNAPSES; i++) {
                if (connections[i] != null
                        && connections[i].getPre().equals(this)) {
                    if ((state - 
                            (connections[i].getResistance() / DEDUCTION_RATIO)) 
                            > MAX_SIGNAL) {
                        newState = state - MAX_SIGNAL;
                        connections[i].getPost().incrementNewState(MAX_SIGNAL);
                        connections[i].LTP();
                    } else if ((state - connections[i].getResistance() / 
                            DEDUCTION_RATIO) > 0) {
                        newState = state - (state - 
                                connections[i].getResistance() / 
                                DEDUCTION_RATIO);
                        connections[i].getPost().incrementNewState(state - 
                                (connections[i].getResistance() / 
                                        DEDUCTION_RATIO));
                        connections[i].LTP();
                    }
                }
            }
        }
        if (newState >= 0) {
            if (newState > LEAK_RATE) {
                newState -= LEAK_RATE;
            } else {
                newState = 0;
            }
            for (int i = 0; i < NUM_SYNAPSES; i++) {
                if (connections[i] != null && 
                        connections[i].getPre().equals(this)) {
                    connections[i].incrementRoundsSinceLTP();
                    if (connections[i].getRoundsSinceLTP() > 
                            MAX_ROUNDS_SINCE_LTP) {
                        connections[i].LTD();
                    }
                }
            }
        }
    }

    public void updateNewState() {
        newState = state;
    }

    public void updateState() {
        state = newState;
    }

    public void incrementNewState(double s) {
        newState += s;
    }

    public void setNorthSynapse(Synapse s) {
        connections[NORTH] = s;

    }

    public void setEastSynapse(Synapse s) {
        connections[EAST] = s;

    }

    public void setSouthSynapse(Synapse s) {
        connections[SOUTH] = s;

    }

    public void setWestSynapse(Synapse s) {
        connections[WEST] = s;

    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    /*
     Methods to click mouse events
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        state += 4;
        display();
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
