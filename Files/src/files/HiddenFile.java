package files;

public class HiddenFile extends File{

    public HiddenFile(String name, int size, String lastModified) {
        super(name, size, lastModified);
    }

    protected String getHeaderHTML() {
        return "<span style=\"color : grey;\">";
    }

    protected String getDetailsHTML() {
        return "<em>[hidden]</em>";
    }

    protected String getFooterHTML() {
        return "</span>";
    }

    @Override
    public String asHTML(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderHTML());
        sb.append(super.asHTML());
        sb.append(getDetailsHTML());
        sb.append(getFooterHTML());
        return sb.toString();
    }
}
