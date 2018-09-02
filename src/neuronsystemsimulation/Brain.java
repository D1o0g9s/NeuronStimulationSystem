package neuronsystemsimulation;

import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * The Brain class is a JPanel that holds all the cells and synapse information
 * to be able to display itself in a container. 
 * 
 * @author Geeling Chau
 * @date May 2016
 */
public class Brain extends JPanel{
    
    private int N; //num rows
    private int M; //num cols
    private int synapseArrayVerticalN; 
    private int synapseArrayVerticalM;
    private int synapseArrayHorizontalN; 
    private int synapseArrayHorizontalM; 
    
    private Neuron[][] neuronArray; 
    private Synapse[][] synapseArrayVertical; 
    private Synapse[][] synapseArrayHorizontal; 
    
    private int generation = 0; 
    private boolean auto; 
    
    private static final int NUM_THICKNESSES = 4; // 0, 1, 2, 3
    private static final int DEFAULT_HORIZONTAL_THICKNESS = 2; 
    private static final int DEFAULT_VERTICAL_THICKNESS = 1; 
    private static final int MAX_RANDOM_STIMULATION = 3;
    
    private String setup; 
    
    Brain() {
        this(4, 8, "Random"); //default board dimensions 
    }

    /**
     * Creates brain and calls initialization functions to create neurons and 
     * synapses. 
     * 
     * @param N - int - num rows 
     * @param M - int - num cols
     * @param setupCommand - String - "Random" or "Structured" 
     */
    Brain(int N, int M, String setupCommand) {
        super();
        generation = 0; 
        if(N>=2 && M>=2){ //min board size 2x2
            this.N = N; 
            this.M = M;
        }
        else{
            this.N = 2; //minimize board size
            this.M = 2;
            System.out.println("Brain size too small! ");
        }
        
        initializeArrays();
        
        
        
        if(setupCommand.equals("Random")){
            initializeRandomSetUp();
            setup = "Random";
        }
        else if (setupCommand.equals("Structured")){
            initializeStructuredSetUp(); 
            setup = "Structured"; 
        }
        else {
            System.out.println("Unrecognized setup command");
        }
        
        display(); 
        
    }
    
    /**
     * Initializes the synapse array and the neuron array. 
     */
    private void initializeArrays(){
        synapseArrayVerticalN = N; 
        synapseArrayVerticalM = M-1; 
        synapseArrayHorizontalN = N-1; 
        synapseArrayHorizontalM = M; 
        
        setLayout(new GridLayout(N, M));
        synapseArrayVertical = 
                new Synapse[synapseArrayVerticalN][synapseArrayVerticalM];
        synapseArrayHorizontal = 
                new Synapse[synapseArrayHorizontalN][synapseArrayHorizontalM];
        neuronArray = new Neuron[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                neuronArray[i][j] = new Neuron(i, j);
                this.add(neuronArray[i][j]);
            }
        }
        
    }
    
    /**
     * Randomly selects the direction of all the synapses. 
     */
    private void initializeRandomSetUp() {
        for (int i = 0; i < synapseArrayVerticalN; i++) {
            for (int j = 0; j < synapseArrayVerticalM; j++) {
                if((int)(Math.random()*3) == 0){
                    synapseArrayVertical[i][j] = new Synapse(neuronArray[i][j], 
                            neuronArray[i][j+1], 
                            (int)(Math.random()*NUM_THICKNESSES));
                }
                else{
                    synapseArrayVertical[i][j] = new Synapse(
                            neuronArray[i][j+1], neuronArray[i][j], 
                            (int)(Math.random()*NUM_THICKNESSES));
                }
                neuronArray[i][j].setEastSynapse(synapseArrayVertical[i][j]);
                neuronArray[i][j+1].setWestSynapse(synapseArrayVertical[i][j]);
                
            }
        }
        for (int i = 0; i < synapseArrayHorizontalN; i++) {
            for (int j = 0; j < synapseArrayHorizontalM; j++) {
                if((int)(Math.random()*4) == 0){
                    synapseArrayHorizontal[i][j] = new Synapse(
                            neuronArray[i][j], neuronArray[i+1][j], 
                            (int)(Math.random()*NUM_THICKNESSES));
                }
                else{
                    synapseArrayHorizontal[i][j] = new Synapse(
                            neuronArray[i+1][j], neuronArray[i][j], 
                            (int)(Math.random()*NUM_THICKNESSES));
                }
                neuronArray[i][j].setSouthSynapse(
                        synapseArrayHorizontal[i][j]);
                neuronArray[i+1][j].setNorthSynapse(
                        synapseArrayHorizontal[i][j]);
                
            }
        }
    }
    
    /**
     * Initialized a structured set up where the right and south borders are 
     * permeable to the next neurons. 
     */
    private void initializeStructuredSetUp() {
        for (int i = 0; i < synapseArrayVerticalN; i++) {
            for (int j = 0; j < synapseArrayVerticalM; j++) {
                synapseArrayVertical[i][j] = new Synapse(neuronArray[i][j], 
                        neuronArray[i][j+1], DEFAULT_VERTICAL_THICKNESS);
                neuronArray[i][j].setEastSynapse(synapseArrayVertical[i][j]);
                neuronArray[i][j+1].setWestSynapse(synapseArrayVertical[i][j]);
            }
        }
        for (int i = 0; i < synapseArrayHorizontalN; i++) {
            for (int j = 0; j < synapseArrayHorizontalM; j++) {
                synapseArrayHorizontal[i][j] = new Synapse(neuronArray[i][j], 
                        neuronArray[i+1][j],  DEFAULT_HORIZONTAL_THICKNESS);
                neuronArray[i][j].setSouthSynapse(
                        synapseArrayHorizontal[i][j]);
                neuronArray[i+1][j].setNorthSynapse(
                        synapseArrayHorizontal[i][j]);
            }
        }
    }
    
    /**
     * Updates the state of each neuron.
     */
    protected void update(){ 
        generation++; 
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                neuronArray[i][j].updateNewState();
            }
        }
        if(auto){
            for(int i = 0; i < N; i++){
                for(int j = 0; j < M; j++){
                    if(Math.random()>0.8){
                        neuronArray[i][j].incrementNewState(
                                Math.random()*MAX_RANDOM_STIMULATION);
                    }
                    
                }
            }
        }
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                neuronArray[i][j].update();
            }
        }
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                neuronArray[i][j].updateState();
            }
        }
        
    }
    
    /**
     * Calls the display function on all the neurons. 
     */
    public void display(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                neuronArray[i][j].display();
            }
        }
    }
    
    public int getN(){
        return N; 
    }
    public int getM(){
        return M; 
    }
    
    public int getGeneration(){
        return generation; 
    }
    
    public String getSetup(){
        return setup; 
    }
    
    public void setAuto(boolean a){
        auto = a; 
    }

    
    
}
