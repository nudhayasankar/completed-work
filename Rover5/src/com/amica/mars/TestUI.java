package com.amica.mars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Interactive test UI for the {@link Rover}.
 * Places two rovers in the center of a small gid, and then lets the
 * user send any number of command strings to them. Animates their
 * movement and shows them as they navigate the grid.
 *
 * @author Will Provost
 */
@SuppressWarnings("serial")
public class TestUI extends JFrame {

	private static final int BLOCK_SIZE = 42;
	private static final int BORDER_SIZE = 2;
	private static final int BOTTOM_MARGIN = 92;
	private static final int SIZE = 13;
	private static final int SPEED = 1000;
	
	private Rover rover1;
	private Rover rover2;
	
	private Timer timer;
	private JButton[][] space;
	private JLabel[][] players;
	private JLabel label1;
	private JTextField commands1;
	private JLabel label2;
	private JTextField commands2;
	
	private ImageIcon blank;
	private ImageIcon[] direction;
	
	/**
	 * Helper to get the UI button associated with a given rover.
	 */
	private JButton getButton(Rover rover) {
		return space[SIZE / 2 - rover.getY()][rover.getX() + SIZE / 2];
	}

	/**
	 * Helper to (a) clear the UI where the given rover is, (b) tell the
	 * rover to move or turn, if it has a command to execute, and (c) update
	 * the UI where the rover has moved.
	 */
	private void moveRover(Rover rover) {
		getButton(rover).setIcon(blank);
		rover.takeNextStep();
		getButton(rover).setIcon(direction[rover.getDirection().ordinal()]);
	}
	
	/**
	 * Sets up the UI to show the game board -- initially blank.
	 * Sets a one-second, repeating timer, and moves each rover once per tick.
	 */
	public TestUI() {
		super("Rovers");
		
		rover1 = new Rover();
		rover2 = new Rover();
		
		blank = new ImageIcon(getClass().getResource("blank.jpg"));
		direction = new ImageIcon[4];
		direction[0] = new ImageIcon(getClass().getResource("north.jpg"));
		direction[1] = new ImageIcon(getClass().getResource("east.jpg"));
		direction[2] = new ImageIcon(getClass().getResource("south.jpg"));
		direction[3] = new ImageIcon(getClass().getResource("west.jpg"));
		
		JLayeredPane layers = new JLayeredPane();
		
		JPanel mainLayer = new JPanel();
		mainLayer.setLayout(new GridLayout(SIZE, SIZE));
		
		space = new JButton[SIZE][];
		players = new JLabel[SIZE][];
		for (int row = 0; row < SIZE; ++row) {
			space[row] = new JButton[SIZE];
			players[row] = new JLabel[SIZE];
			for (int col = 0; col < SIZE; ++col) {
				
				space[row][col] = new JButton(blank); 
				space[row][col].setBackground(new Color(220, 220, 255));
				mainLayer.add(space[row][col]);
			}
		}
		
		layers.add(mainLayer, 1);
		mainLayer.setBounds(BORDER_SIZE, BORDER_SIZE, 
				SIZE * BLOCK_SIZE, SIZE * BLOCK_SIZE);
		
		JPanel commandPanel = new JPanel();
		label1 = new JLabel("Rover 1:");
		commands1 = new JTextField(8);
		label2 = new JLabel("Rover 2:");
		commands2 = new JTextField(8);
		
		Font defaultFont = label1.getFont();
		Font font = new Font(defaultFont.getName(), defaultFont.getStyle(), 20);
		label1.setFont(font);
		commands1.setFont(font);
		label2.setFont(font);
		commands2.setFont(font);
		
		commandPanel.add(label1);
		commandPanel.add(commands1);
		commandPanel.add(label2);
		commandPanel.add(commands2);
		commandPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 8));

		setSize(SIZE * (BLOCK_SIZE + 2) + BORDER_SIZE * 2, 
				SIZE * (BLOCK_SIZE + 2) + BOTTOM_MARGIN);
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, layers);
		add(BorderLayout.SOUTH, commandPanel);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		commands1.addActionListener
			(ev -> rover1.receiveCommands(commands1.getText()));
		commands2.addActionListener
			(ev -> rover2.receiveCommands(commands2.getText()));
		
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					moveRover(rover1);
					moveRover(rover2);
				} catch (Exception ex) {
					ex.printStackTrace();
					timer.cancel();
				}
			}
		};
		
		timer = new Timer();
		timer.schedule(task, SPEED, SPEED);
	}
	
	public static void main(String[] args) {
		new TestUI();
	}
}
