package files;

import java.util.ArrayList;
import java.util.List;

public class Folder extends Node{
    private List<Node> children;

    public Folder(String name) {
        super(name);
        children = new ArrayList<>();
    }

    @Override
    // total size of all child files and folders
    public int getSize() {
        int size = 0;
        for (Node child : children){
            size += child.getSize();
        }
        return size;
    }

    @Override
    public String asHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNameHTML());
        sb.append("(").append(getSizeHTML());
        sb.append(getDetailsHTML());
        sb.append(getChildrenHTML());
        return sb.toString();
    }

    public void add(Node child) {
        children.add(child);
    }

    protected String getDetailsHTML() {
        return ")";
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

    protected String getNameHTML() {
        return "<strong>" + getName() + "</strong>";
    }

    protected String getSizeHTML() {
        return getSize() + " bytes";
    }
}
