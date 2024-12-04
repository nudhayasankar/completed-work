package com.amica.mars;

/**
 * In this version, all state and behavior are encapsulated as
 * instance variables and instance methods. There is also an enhanced
 * interface that allows the caller to send a command string and then
 * direct the rover to execute one command at a time.
 * 
 * @author Will Provost
 */
public class Rover {

	public enum Direction { NORTH, EAST, SOUTH, WEST }
	
	private int x;
	private int y;
	private Direction direction;
	private StringBuffer commands;
	private int id;

	public int getId() {
		return id;
	}

	public Rover(int id) {
		this(0, 0, Direction.NORTH, id);
	}

	
	public Rover(int x, int y, Direction direction, int id) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.commands = new StringBuffer();
		this.id = id;
	}

	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void turnLeft() {
		direction = Direction.values()[(direction.ordinal() + 3) % 4];
	}
	
	public void turnRight() {
		direction = Direction.values()[(direction.ordinal() + 1) % 4];
	}
	
	public void move() {
		if (direction == Direction.NORTH) {
			++y;
		} else if (direction == Direction.EAST) {
			++x;
		} else if (direction == Direction.SOUTH) {
			--y;
		} else if (direction == Direction.WEST) {
			--x;
		}  
	}

	public void move(int distance) {
		for (int i = 0; i < distance; ++i) {
			move();
		}
	}
	
	public String getStatus() {

		String dirString = direction.toString().toLowerCase();

		return String.format("The rover is now at (%d,%d), and facing %s.",
				x, y, dirString);
	}
	
	public void receiveCommands(String newCommands) {
		for (int c = 0; c < newCommands.length(); ++c) {
			char command = newCommands.charAt(c);
			if (Character.isDigit(command)) {
				commands.append("M".repeat(Character.getNumericValue(command)));
			} else {
				commands.append(command);
			}
		}
	}
	
	public boolean isBusy() {
		return commands.length() != 0;
	}
	
	public void takeNextStep() {
		if (isBusy()) {
			char command = commands.charAt(0);
			commands.deleteCharAt(0);
			execute(command);
		}
	}

	protected void execute(char command){
		if (command == 'L') {
			turnLeft();
		} else if (command == 'R') {
			turnRight();
		} else if (command == 'M') {
			move();
		} else {
			System.out.println("Unrecognized command: " + command);
		}
	}
	
	public static void main(String[] args) {
		
		String commands = "4R2R1L2";
		//String commands = "LL4R2R1L2";
		//String commands = "R4LL2R17";
		
		Rover rover = new Rover(1);
		for (int i = 0; i < commands.length(); ++i) {
			char command = commands.charAt(i);
			if (command == 'L') {
				rover.turnLeft();
			} else if (command == 'R') {
				rover.turnRight();
			} else if (Character.isDigit(command)) {
				rover.move(command - '0');
			} else {
				System.out.println("Unrecognized command: " + command);
				break;
			}
		}
		
		System.out.println(rover.getStatus());
	}
}
