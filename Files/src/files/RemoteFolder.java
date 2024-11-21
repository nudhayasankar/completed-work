package files;

public class RemoteFolder extends Folder{

    public RemoteFolder(String name) {
        super(name);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void add(Node child) {
        super.add(child);
    }

    protected String getHeaderHTML() {
        return "<em>";
    }

    protected String getFooterHTML() {
        return "</em>";
    }

    public String asHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderHTML());
        sb.append(super.asHTML());
        sb.append(getFooterHTML());
        return sb.toString();
    }
}
