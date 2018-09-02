package neuronsystemsimulation;

/**
 * The Synapse class keeps track of how resistant the synapse is. 
 * 
 * @author Geeling Chau
 * @date May 2016
 */
public class Synapse {

    private Neuron presynaptic;
    private Neuron postsynaptic;

    private int iPresynaptic; //the i value of the presynaptic neuron
    private int jPresynaptic;
    private int iPostsynaptic;
    private int jPostsynaptic;

    private int roundsSinceLTP;

    private static final int NUM_THICKNESSES = 4; // 0, 1, 2, 3
    private static final int MAX_RESISTANCE = 3;
    private int resistance; //0, 1, 2, 3 //informs border thickness

    Synapse(Neuron pre, Neuron post, int res) {
        presynaptic = pre;
        iPresynaptic = pre.getI();
        jPresynaptic = pre.getJ();

        postsynaptic = post;
        iPostsynaptic = post.getI();
        jPostsynaptic = post.getJ();
        setResistance(res);
    }

    public void setResistance(int res) {
        if (res >= 0 && res < NUM_THICKNESSES) {
            resistance = res;
        }
    }

    public int getResistance() {
        return resistance;
    }

    public Neuron getPre() {
        return presynaptic;
    }

    public void incrementRoundsSinceLTP() {
        roundsSinceLTP++;
    }

    public Neuron getPost() {
        return postsynaptic;
    }

    /**
     * LTP() (long term potentiation) makes the synapse less resistant.
     */
    public void LTP() {
        if (resistance > 0) {
            resistance--;
        }
        roundsSinceLTP = 0;
    }

    /**
     * LTD() (long term depression) makes the synapse more resistant. 
     */
    public void LTD() {
        if (resistance < MAX_RESISTANCE) {
            resistance++;
        }
        roundsSinceLTP = 0;
    }

    public int getRoundsSinceLTP() {
        return roundsSinceLTP;
    }

}
