package files;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestProgram {
	
	/*
	 * Factory methods delegate to constructors.
	 * We only keep these because they were in the HTML script,
	 * and we had the requirement to leave that code alone.
	 * It could be re-written with "new Folder()" etc.
	 */

	public static Node createFolder(String name) {
	  return new Folder(name);
	}

	public static Node createRemoteFolder(String name) {
	  return new RemoteFolder(name);
	}

	public static Node createFile(String name, int size, String lastModified) {
	  return new File(name, size, lastModified);
	}

	public static Node createHiddenFile(String name, int size, String lastModified) {
	  return new HiddenFile(name, size, lastModified);
	}

	public static void main(String[] args) {
	    Node root = createFolder("JSCodingAssessment");
	    Node CSS = createFolder("CSS");
	    Node HTML = createFolder("HTML");
	    Node JavaScript1 = createFolder("JavaScript1");
	    Node JavaScript2 = createFolder("JavaScript2");

	    root.add(CSS);
	    root.add(HTML);
	    root.add(JavaScript1);
	    root.add(JavaScript2);

	    CSS.add(createFile("allegro.css", 131, "10/29/17"));
	    CSS.add(createFile("HotCrossBuns.html", 1155, "10/29/17"));

	    HTML.add(createFile("invoice123.html", 144, "10/29/17"));
	    HTML.add(createFile("invoice456.html", 144, "10/29/17"));
	    HTML.add(createFile("invoice789.html", 144, "10/29/17"));
	    HTML.add(createFile("summary.html", 583, "10/29/17"));

	    Node jQuery1 = createRemoteFolder("https://ajax.googleapis.com/ajax/libs/jquery/3.2.1");
	    Node authors = createFile("AUTHORS.txt", 12535, "9/4/16");
	    Node jqueryui = createFile("jquery-ui.min.js", 253385, "9/4/16");
	    Node jquery = createFile("jquery.js", 86351, "9/4/16");
	    Node license = createFile("LICENSE.txt", 1817, "9/4/16");

	    jQuery1.add(authors);
	    jQuery1.add(jqueryui);
	    jQuery1.add(jquery);
	    jQuery1.add(license);

	    JavaScript1.add(createFile("cart.html", 1526, "10/29/17"));
	    JavaScript1.add(jQuery1);
	    JavaScript1.add(createFile("redX.png", 3631, "10/29/17"));
	    JavaScript1.add(createFile("Shopping.css", 451, "10/29/17"));
	    JavaScript1.add(createHiddenFile(".Trashes", 500, "10/30/17"));

	    Node jQuery2 = createRemoteFolder("https://ajax.googleapis.com/ajax/libs/jquery/3.2.1");
	    jQuery2.add(authors);
	    jQuery2.add(jqueryui);
	    jQuery2.add(jquery);
	    jQuery2.add(license);

	    JavaScript2.add(createFile("correctAppearance.png", 77824, "10/30/17"));
	    JavaScript2.add(createFile("folders.css", 166, "10/29/17"));
	    JavaScript2.add(createFile("folders.html", 530, "10/30/17"));
	    JavaScript2.add(jQuery2);

	    System.out.println("<html><body>" + root.asHTML() + "</body></html>");
	    try ( PrintWriter out = new PrintWriter(new FileWriter("files.html")); ) {
	        out.print("<html><body>" + root.asHTML() + "</body></html>");
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	}
}
