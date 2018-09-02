package neuronsystemsimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Main class. Creates the GUI and brain. 
 * 
 * @author Geeling Chau
 * @date May 2016
 */
public class NeuronSystemSimulation extends JFrame implements ActionListener {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
    public static final int FIELD_SIZE = 3;

    private JTextField textN;
    private JTextField textM;

    private JButton runStopToggle;
    private JButton autoManualToggle;
    private JLabel generation;

    private int generationCount = 0;
    private boolean running = false;
    private boolean auto = false;
    Timer timer;
    Timer timer2;

    private Brain brain;

    public NeuronSystemSimulation() {
        super("Neuron System Simulation");

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        brain = new Brain();
        add(brain, BorderLayout.CENTER);

        //BorderLayout.NORTH Control: Cell setup interface
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JLabel initializeLabel = new JLabel("Initialize: ");
        northPanel.add(initializeLabel);

        JButton randomSetUpButton = new JButton("Random");
        randomSetUpButton.addActionListener(this);
        northPanel.add(randomSetUpButton);

        JButton structuredSetUpButton = new JButton("Structured");
        structuredSetUpButton.addActionListener(this);
        northPanel.add(structuredSetUpButton);

        generation = new JLabel("Generation: " + brain.getGeneration());
        northPanel.add(generation);

        add(northPanel, BorderLayout.NORTH);
        
        // Step time interval, Run/Stop, Auto/Manual Stimulation
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.LIGHT_GRAY);
        controlPanel.setLayout(new GridLayout(2, 1));

        //Button section of Control Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(this);
        buttonPanel.add(stepButton);

        runStopToggle = new JButton("Run/Stop");
        runStopToggle.addActionListener(this);
        buttonPanel.add(runStopToggle);

        autoManualToggle = new JButton("Auto Stimulation");
        autoManualToggle.addActionListener(this);
        buttonPanel.add(autoManualToggle);

        controlPanel.add(buttonPanel);

        //Reset section of Control Panel
        JPanel resetPanel = new JPanel();
        resetPanel.setLayout(new FlowLayout());

        JLabel NxMLabel = new JLabel("Reset the board size here (NxM): ");
        resetPanel.add(NxMLabel);

        textN = new JTextField(String.valueOf(brain.getN()), FIELD_SIZE);
        resetPanel.add(textN);

        JLabel x = new JLabel("x");
        resetPanel.add(x);

        textM = new JTextField(String.valueOf(brain.getM()), FIELD_SIZE);
        resetPanel.add(textM);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetPanel.add(resetButton);

        controlPanel.add(resetPanel);

        add(controlPanel, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        NeuronSystemSimulation gui = new NeuronSystemSimulation();
        gui.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonString = e.getActionCommand();

        switch (buttonString) {
            case "Step":
                step();
                if (running) {
                    running = false;
                    timer.cancel();
                    System.out.println("Stopped");
                    runStopToggle.setText("Run");
                }
                break;
            case "Run":
            case "Stop":
            case "Run/Stop":
                running = !running;
                if (running) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            step();
                        }
                    }, 0, 200);
                    System.out.println("Running");
                    runStopToggle.setText("Stop");
                } else {
                    timer.cancel();
                    System.out.println("Stopped");
                    runStopToggle.setText("Run");
                }
                break;
            case "Random":
                if (running) {
                    running = false;
                    timer.cancel();
                    System.out.println("Stopped");
                    runStopToggle.setText("Run");
                }
                remove(brain);
                brain = new Brain(Integer.parseInt(textN.getText()), 
                        Integer.parseInt(textM.getText()), "Random");
                add(brain, BorderLayout.CENTER);
                validate();
                brain.setAuto(auto);
                break;
            case "Structured":
                if (running) {
                    running = false;
                    timer.cancel();
                    System.out.println("Stopped");
                    runStopToggle.setText("Run");
                }
                remove(brain);
                brain = new Brain(Integer.parseInt(textN.getText()), 
                        Integer.parseInt(textM.getText()), "Structured");
                add(brain, BorderLayout.CENTER);
                validate();
                brain.setAuto(auto);
                break;
            case "Reset":
                if (running) {
                    running = false;
                    timer.cancel();
                    System.out.println("Stopped");
                    runStopToggle.setText("Run");
                }
                remove(brain);
                brain = new Brain(Integer.parseInt(textN.getText()), 
                        Integer.parseInt(textM.getText()), brain.getSetup());
                add(brain, BorderLayout.CENTER);
                validate();
                brain.setAuto(auto);

                break;
            case "Auto Stimulation":
            case "Manual Stimulation":
                auto = !auto;
                brain.setAuto(auto);
                if (auto) {
                    System.out.println("Auto Stimulation");
                    autoManualToggle.setText("Manual Stimulation");
                } else {
                    System.out.println("Manual Stimulation");
                    autoManualToggle.setText("Auto Stimulation");
                }
                break;

        }
    }

    private void step() {
        brain.update();
        brain.display();
        generation.setText("Generation: " + brain.getGeneration());
    }

}
