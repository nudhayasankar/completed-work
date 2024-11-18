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
    public void add(Node child) {
        children.add(child);
    }

    @Override
    protected String getHeaderHTML() {
        return "";
    }

    @Override
    protected String getDetailsHTML() {
        return ")";
    }

    @Override
    protected String getFooterHTML() {
        return "";
    }
}
