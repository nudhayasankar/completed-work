package files;

import java.util.ArrayList;
import java.util.List;

public class RemoteFolder extends Node{

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
    public void add(Node child) {
        children.add(child);
    }

    @Override
    protected String getHeaderHTML() {
        return "<em>";
    }

    @Override
    protected String getDetailsHTML() {
        return ")";
    }

    @Override
    protected String getFooterHTML() {
        return "</em>";
    }
}
