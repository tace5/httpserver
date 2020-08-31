package httpserver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author ruben.hume
 */

public class Gui extends JFrame implements ActionListener {
    
    private static JPanel leftPanel;
    private static JPanel rightPanel;
    private static SpinnerNumberModel spinnerModelThreads;
    private static SpinnerNumberModel spinnerModelTasks;
    private static JSpinner spinnerThreads;
    private static JSpinner spinnerTasks;
    private static JLabel lblThreads;
    private static JLabel lblTasks;
    private static JLabel lblPort;
    private static JTextField portNumber;
    private static JButton btnStart;
    private static JButton btnStop;
    private static JScrollPane scrollPane;
    private static JTextArea consoleWindow;
    private HttpReceiver hr;
    
    public Gui() {
        // Sets look and feel of the program
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 500));
        add(leftPanel, BorderLayout.WEST);
        
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(400, 500));
        add(rightPanel, BorderLayout.EAST);
        
        lblThreads = new JLabel("Number of threads:");
        leftPanel.add(lblThreads);
        
        spinnerModelThreads = new SpinnerNumberModel(20, 0, 1000, 10);
        spinnerThreads = new JSpinner(spinnerModelThreads);
        spinnerThreads.setPreferredSize(new Dimension(40, 30));
        leftPanel.add(spinnerThreads);
        
        lblTasks = new JLabel("Max nr of queries:  ");
        leftPanel.add(lblTasks);
        
        spinnerModelTasks = new SpinnerNumberModel(40, 0, 1500, 10);
        spinnerTasks = new JSpinner(spinnerModelTasks);
        spinnerTasks.setPreferredSize(new Dimension(40, 30));
        leftPanel.add(spinnerTasks);
        
        lblPort = new JLabel("Port number:");
        lblPort.setPreferredSize(new Dimension(100, 30));
        leftPanel.add(lblPort);
        
        portNumber = new JTextField("80");
        portNumber.setPreferredSize(new Dimension(50, 30));
        leftPanel.add(portNumber);
        
        btnStart = new JButton("START SERVER");
        btnStart.setPreferredSize(new Dimension(150, 80));
        leftPanel.add(btnStart);
        
        btnStop = new JButton("STOP SERVER");
        btnStop.setPreferredSize(new Dimension(150, 80));
        leftPanel.add(btnStop);
        
        consoleWindow = new JTextArea();
        consoleWindow.setBackground(new Color(200, 200, 200));
        consoleWindow.setMinimumSize(new Dimension(390, 490));
        consoleWindow.setEditable(false);
        scrollPane = new JScrollPane(consoleWindow);
        scrollPane.setPreferredSize(new Dimension(390, 490));
        rightPanel.add(scrollPane);
        
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        
        setSize(600, 500);
        setResizable(false);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) { // Waits for user to press a button
        if(e.getSource() == btnStart) { // Triggers if user presses start button
            ThreadPool tp = new ThreadPool((int) spinnerThreads.getValue(), (int) spinnerTasks.getValue()); // Creates a ThreadPool
            hr = new HttpReceiver(tp, Integer.parseInt(portNumber.getText())); // Creates HttpReceiver 
            hr.start();
        }
        if(e.getSource() == btnStop) { // Triggers if user presses stop button
            hr.terminate(); // Stops the server
        }
    }
    
    // Method that prints the msg argument into a console window
    public static void msgToConsole(String type, String msg) { 
        String formattedMsg = "[" + type + "]> " + msg + "\r\n";
        consoleWindow.append(formattedMsg); // Appends the message to the console
        try {
            // Puts the message in a log file
            FileWriter out = new FileWriter("log.txt", true);
            String date = ResponseHeader.getServerTime();
            out.write("[" + date + "]" + " " + formattedMsg);
            out.close();
        } catch (IOException ex) {
            // Keep running
        }
    }
    
    public static void main(String[] args) {
        Gui mainFrame = new Gui(); // Starts new instance of Gui
    }
}