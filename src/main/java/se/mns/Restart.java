package se.mns;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @version 1
 * @author naoyairikura
 *
 */
public class Restart extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1156863117874978139L;
	
	private JButton restartButton = new JButton("Starta om");
	
	public Restart() {
		
		setLayout(new GridLayout(1,1));
		add(restartButton);
		getContentPane().setBackground(Color.blue);
		Font font = new Font("SansSerif", Font.BOLD, 16);
		restartButton.setFont(font);
		setSize(100, 50);
		restartButton.addActionListener(this);
		restartButton.setEnabled(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==restartButton) {
			System.exit(0);
		}
		
	}
	
	
}
