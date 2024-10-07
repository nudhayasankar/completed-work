package treasure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import treasure.Island.Contents;
import treasure.Island.Coordinates;

/**
 * User interface for the treasure-hunting game -- or, really, more of a 
 * viewer, since the game is not played interactively. Knows how to show
 * the state of the island (which means the user only sees what the players
 * have discovered up to any given time, with other squares obscured).
 * can set a timer that will run the game turn-by-turn and call your
 * playing logic each turn (once per second). Also offers a footer where
 * you can show a simple text message.
 *
 * @author Will Provost
 */
@SuppressWarnings("serial")
public class Game extends JFrame {

	private static final int BLOCK_SIZE = 92;
	private static final int BORDER_SIZE = 15;
	private static final int BOTTOM_MARGIN = 92;
	
	private Island island;
	private Timer timer;
	private JButton[][] space;
	private JLabel[][] players;
	private JLabel messageLabel;
	private Collection<Coordinates> coinLocations = new ArrayList<>();
	
	/**
	 * Sets up the UI to show the game board -- initially blank.
	 */
	public Game(Island island) {
		super("Treasure Hunt");
		
		this.island = island;
		
		int height = island.getHeight();
		int width = island.getWidth();
		
		JLayeredPane layers = new JLayeredPane();
		
		JPanel mainLayer = new JPanel();
		mainLayer.setLayout(new GridLayout(height, width));
		JPanel playerLayer = new JPanel();
		playerLayer.setOpaque(false);
		playerLayer.setLayout(new GridLayout(height, width));
		
		space = new JButton[height][];
		players = new JLabel[height][];
		for (int row = 0; row < height; ++row) {
			space[row] = new JButton[width];
			players[row] = new JLabel[width];
			for (int col = 0; col < width; ++col) {
				
				space[row][col] = new JButton(new ImageIcon
						(getClass().getResource("unknown.jpg"))); 
				space[row][col].setBackground(new Color(220, 220, 255));
				mainLayer.add(space[row][col]);
				
				players[row][col] = new JLabel(new ImageIcon
						(getClass().getResource("noPlayer.gif"))); 
				playerLayer.add(players[row][col]);
			}
		}
		
		layers.add(mainLayer, new Integer(1));
		mainLayer.setBounds(BORDER_SIZE, BORDER_SIZE, 
				width * BLOCK_SIZE, height * BLOCK_SIZE);
		layers.add(playerLayer, new Integer(2));
		playerLayer.setBounds(BORDER_SIZE, BORDER_SIZE, 
				width * BLOCK_SIZE, height * BLOCK_SIZE);
		
		messageLabel = new JLabel("");
		Font defaultFont = messageLabel.getFont();
		messageLabel.setFont(new Font(defaultFont.getName(), 
				defaultFont.getStyle(), 20));
		messageLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 8));

		setSize(width * (BLOCK_SIZE + 2) + BORDER_SIZE * 2, 
				height * (BLOCK_SIZE + 2) + BOTTOM_MARGIN);
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, layers);
		add(BorderLayout.SOUTH, messageLabel);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	/**
	 * Sets a one-second, repeating timer, and calls the given function
	 * on each timer tick. Exceptions are caught and their messages shown
	 * in the message area of GUI; they are also dumped to the console.
	 * If the callback function returns false, the game is over, so we
	 * stop the timer, but leave the GUI showing.
	 */
	public void onEachTurnCall(Supplier<Boolean> playFunction) {
		
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					if (!playFunction.get()) {
						timer.cancel();
						System.out.println("GAME OVER");
					}
				} catch (Exception ex) {
					showMessage(ex.getMessage());
					ex.printStackTrace();
					timer.cancel();
				}
			}
		};
		
		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}
	
	/**
	 * Show the contents of one square of the island grid.
	 * Mostly this means a specific image resource per type of content:
	 * empty, coin, wizard, or treasure. But we also show a separate image
	 * to mark where a coin was, after it's been picked up by a player.
	 */
	public void showContents(int row, int col) {
		Contents contents = island.getContents(row,  col);
		String imageName = contents.toString().toLowerCase();
		Coordinates here = new Coordinates(row, col);
		if (contents == Contents.COIN) {
			coinLocations.add(here);
		} else if (contents == Contents.EMPTY && coinLocations.contains(here)) {
			imageName = "coinTrace";
		}
		
		space[row][col].setIcon(new ImageIcon
				(getClass().getResource(imageName + ".jpg")));
		space[row][col].setBackground(Color.white);
	}
	
	/**
	 * Shows the given message in the GUI.
	 */
	public void showMessage(String message) {
		messageLabel.setText("   " + message);
	}

	/**
	 * Shows a player icon, superimposed over the contents of the square.
	 * Be sure to call {@link #playerleaves playerLeaves()} to remove the icon
	 * before moving to another square, or the player icoins will proliferate.
	 */
	public void playerEnters(int row, int col) {
		players[row][col].setIcon(new ImageIcon(getClass().getResource("player.gif")));
	}
	
	/**
	 * Removes the player icon from this square.
	 */
	public void playerLeaves(int row, int col) {
		players[row][col].setIcon(new ImageIcon(getClass().getResource("noPlayer.gif")));
	}
}
