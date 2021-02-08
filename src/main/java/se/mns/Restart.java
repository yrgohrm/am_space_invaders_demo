package se.mns;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @version 2
 * @author naoyairikura, Mårten Hernebring
 *
 */
public class Restart extends JFrame implements ActionListener {

    private static final long serialVersionUID = -1156863117874978139L;

    private JButton restartButton = new JButton("Starta om");
    private JLabel hsLabel = new JLabel("Antal poäng: 2", JLabel.CENTER);

    public Restart() {

        setLayout(new GridLayout(2, 1));
        add(hsLabel);
        add(restartButton);
        getContentPane().setBackground(Color.yellow);
        Font font = new Font("SansSerif", Font.BOLD, 16);
        restartButton.setFont(font);
        hsLabel.setFont(font);

        // Give the frame an initial size
        setSize(275, 400);

        restartButton.addActionListener(this);
        restartButton.setEnabled(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            hsLabel.setText("Du klickade på omstart.");
        }

    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        Restart restart = new Restart();
    }

}
