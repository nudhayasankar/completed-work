package files;

import java.util.ArrayList;
import java.util.List;

public class RemoteFolder extends Node{
    private List<Node> children;

    public RemoteFolder(String name) {
        super(name);
        children = new ArrayList<>();
    }

    @Override
    // total size of all child files and folders
    public int getSize() {
        return 0;
    }

    @Override
    public String asHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<em><strong>");
        html.append(getName());
        html.append("</strong> (");
        html.append(getSize());
        html.append(" bytes)");
        if(!children.isEmpty()){
            html.append("<ul>");
            for (Node child : children){
                html.append("<li>");
                html.append(child.asHTML());
                html.append("</li>");
            }
            html.append("</ul>");
        }
        html.append("</em>");
        return html.toString();
    }

    @Override
    public void add(Node child) {
        children.add(child);
    }
}
