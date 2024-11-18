package files;

/**
 * The File type encapsulates name, size, and mod date,
 * along with display logic.
 */
public abstract class Node {
	private String name;
	private Date lastModified;
	private ArrayList<Node> children;
	private SimpleDateFormat readFormat = new SimpleDateFormat("MM/dd/yy");
	// Format given date in yyyy-MM-dd format
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public Node(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract int getSize();

	public abstract void add(Node child);

	protected String getNameHTML() {
		return "<strong>" + getName() + "</strong>";
	}

	protected String getSizeHTML() {
		return getSizeHTML() + " bytes";
	}

	protected String getChildrenHTML() {
		if(children == null || children.isEmpty()){
			return "";
		}

		StringBuilder htmlString = new StringBuilder();
		htmlString.append("<ul>");
		for(Node c : children){
			htmlString.append("<li>").append(c.asHTML()).append("</li>");
		}
		htmlString.append("</ul>");
		return htmlString.toString();
	}

	protected abstract String getHeaderHTML();
	protected abstract String getDetailsHTML();
	protected abstract String getFooterHTML();

	public String asHTML(){
		StringBuilder htmlString = new StringBuilder();
		htmlString.append(getHeaderHTML());
		htmlString.append(getNameHTML());
		htmlString.append("(").append(getSizeHTML());
		htmlString.append(getDetailsHTML());
		htmlString.append(getChildrenHTML());
		htmlString.append(getFooterHTML());
		return htmlString.toString();
	}

}
