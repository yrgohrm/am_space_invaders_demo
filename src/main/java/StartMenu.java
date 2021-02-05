

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StartMenu implements ActionListener{
	
	JLabel label;
	
	public StartMenu() {
		
		//Create a new JFrame container
		JFrame frame = new JFrame();
		
		//Give the frame an initial size
		frame.setSize(275, 400);
		
		//Terminate the program when the user closes the application
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Make two buttons
		JButton buttonStart = new JButton("Start");
		JButton buttonHighestScore = new JButton("Highest score");
		
		//Add action listeners
		buttonStart.addActionListener(this);
		buttonHighestScore.addActionListener(this);
		
		//Add the buttons to content pane
		frame.add(buttonStart);
		frame.add(buttonHighestScore);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(2, 1));
		panel.add(buttonStart);
		panel.add(buttonHighestScore);
		
		frame.add(panel);
		
		
		//Create a text-based label
		label = new JLabel("Welcome to Jumpy Birb!");
		
		//Add the label to the content pane
		frame.add(label);
		
		//Display the frame
		frame.setVisible(true);
		
		
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new StartMenu();
			}
		});
	}

	//Handle button events
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Start")) {
			label.setText("You pressed start.");
		} else {
			label.setText("Highest score is xxx");
		}
		
	}
		
}
