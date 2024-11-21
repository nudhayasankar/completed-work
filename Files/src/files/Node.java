package files;

import java.text.SimpleDateFormat;

/**
 * The File type encapsulates name, size, and mod date,
 * along with display logic.
 */
public abstract class Node {
	private String name;

	public Node(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract int getSize();

	public abstract String asHTML();

	public abstract void add(Node c);

}
